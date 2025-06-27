package mx.regional.next.automotive.credit.infrastructure.adapters.external.adapters;

import mx.regional.next.automotive.credit.application.ports.out.VehicleValidationPort;
import mx.regional.next.automotive.credit.domain.entities.Vehicle;
import mx.regional.next.automotive.credit.domain.valueobjects.VehicleVIN;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditAmount;
import mx.regional.next.automotive.credit.domain.enums.VehicleType;
import mx.regional.next.shared.common.annotations.Adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Adapter
public class VehicleValidationAdapter implements VehicleValidationPort {
    
    private static final Logger log = LoggerFactory.getLogger(VehicleValidationAdapter.class);
    
    private static final Set<String> APPROVED_BRANDS = Set.of(
        "TOYOTA", "CHEVROLET", "RENAULT", "NISSAN", "HYUNDAI", "KIA", "MAZDA", "FORD"
    );
    
    private final ObjectMapper objectMapper;
    
    public VehicleValidationAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public VehicleValidationResult validateVehicle(String vin, String brand, String model, int year) {
        try {
            log.info("Validando vehículo - VIN: {}, Marca: {}, Modelo: {}, Año: {}", 
                    vin, brand, model, year);
            
            // Validar VIN
            if (!isValidVIN(vin)) {
                return VehicleValidationResult.invalid("VIN inválido: " + vin);
            }
            
            // Validar marca
            if (!isApprovedBrand(brand)) {
                return VehicleValidationResult.invalid("Marca no autorizada: " + brand);
            }
            
            // Validar año
            if (!isValidYear(year)) {
                return VehicleValidationResult.invalid("Año del vehículo no válido: " + year);
            }
            
            // Estimar valor del vehículo
            BigDecimal estimatedValue = estimateVehicleValue(brand, model, year);
            
            if (estimatedValue.compareTo(BigDecimal.valueOf(50_000_000)) < 0) {
                return VehicleValidationResult.invalid("Valor del vehículo inferior al mínimo requerido");
            }
            
            // Crear vehículo validado
            Vehicle vehicle = new Vehicle(
                new VehicleVIN(vin),
                brand.toUpperCase(),
                model.toUpperCase(),
                year,
                determineVehicleType(model),
                new CreditAmount(estimatedValue),
                0, // kilometros no especificados
                "N/A", // color
                "N/A", // motor
                "N/A"  // transmisión
            );
            
            log.info("Vehículo validado exitosamente - VIN: {}", vin);
            return VehicleValidationResult.valid(vehicle);
            
        } catch (Exception e) {
            log.error("Error validando vehículo - VIN: {}", vin, e);
            return VehicleValidationResult.invalid("Error en validación: " + e.getMessage());
        }
    }
    
    private boolean isValidVIN(String vin) {
        if (vin == null || vin.trim().isEmpty()) {
            return false;
        }
        
        String cleanVin = vin.trim().toUpperCase();
        
        // El VIN debe tener exactamente 17 caracteres
        if (cleanVin.length() != 17) {
            return false;
        }
        
        // No debe contener las letras I, O, Q
        return !cleanVin.matches(".*[IOQ].*") && cleanVin.matches("^[A-HJ-NPR-Z0-9]{17}$");
    }
    
    private boolean isApprovedBrand(String brand) {
        return brand != null && APPROVED_BRANDS.contains(brand.toUpperCase());
    }
    
    private boolean isValidYear(int year) {
        int currentYear = java.time.LocalDate.now().getYear();
        return year >= 2018 && year <= currentYear;
    }
    
    private BigDecimal estimateVehicleValue(String brand, String model, int year) {
        try {
            // En un entorno real, esto consultaría un servicio externo o base de datos
            // Por ahora, usaremos valores estimados basados en el catálogo
            
            Map<String, BigDecimal> baseValues = Map.of(
                "TOYOTA", BigDecimal.valueOf(80_000_000),
                "CHEVROLET", BigDecimal.valueOf(70_000_000),
                "RENAULT", BigDecimal.valueOf(60_000_000),
                "NISSAN", BigDecimal.valueOf(75_000_000),
                "HYUNDAI", BigDecimal.valueOf(65_000_000),
                "KIA", BigDecimal.valueOf(65_000_000),
                "MAZDA", BigDecimal.valueOf(75_000_000),
                "FORD", BigDecimal.valueOf(70_000_000)
            );
            
            BigDecimal baseValue = baseValues.getOrDefault(brand.toUpperCase(), BigDecimal.valueOf(60_000_000));
            
            // Aplicar depreciación por año
            int currentYear = java.time.LocalDate.now().getYear();
            int vehicleAge = currentYear - year;
            BigDecimal depreciationFactor = BigDecimal.valueOf(Math.pow(0.85, vehicleAge)); // 15% anual
            
            return baseValue.multiply(depreciationFactor);
            
        } catch (Exception e) {
            log.warn("Error estimando valor del vehículo, usando valor por defecto", e);
            return BigDecimal.valueOf(60_000_000); // Valor por defecto
        }
    }
    
    private VehicleType determineVehicleType(String model) {
        String upperModel = model.toUpperCase();
        
        if (upperModel.contains("SUV") || upperModel.contains("RAV") || 
            upperModel.contains("TRACKER") || upperModel.contains("DUSTER") ||
            upperModel.contains("KICKS")) {
            return VehicleType.SUV;
        } else if (upperModel.contains("PICKUP") || upperModel.contains("HILUX")) {
            return VehicleType.PICKUP;
        } else if (upperModel.contains("HATCHBACK") || upperModel.contains("ONIX")) {
            return VehicleType.HATCHBACK;
        } else {
            return VehicleType.SEDAN; // Por defecto
        }
    }
}