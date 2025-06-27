package mx.regional.next.automotive.credit.infrastructure.mcp.tools;

import mx.regional.next.automotive.credit.domain.entities.Vehicle;
import mx.regional.next.automotive.credit.domain.valueobjects.VehicleVIN;
import mx.regional.next.automotive.credit.domain.services.CreditEligibilityService;
import mx.regional.next.automotive.credit.application.ports.out.VehicleValidationPort;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Year;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;

@Component
public class CheckVehicleEligibilityTool {
    
    private static final Logger log = LoggerFactory.getLogger(CheckVehicleEligibilityTool.class);
    
    private final CreditEligibilityService creditEligibilityService;
    private final VehicleValidationPort vehicleValidationPort;
    
    private static final List<String> AUTHORIZED_BRANDS = List.of(
        "TOYOTA", "CHEVROLET", "RENAULT", "NISSAN", "HYUNDAI", "KIA", "MAZDA", "FORD"
    );
    
    public CheckVehicleEligibilityTool(
            CreditEligibilityService creditEligibilityService,
            VehicleValidationPort vehicleValidationPort) {
        this.creditEligibilityService = creditEligibilityService;
        this.vehicleValidationPort = vehicleValidationPort;
    }
    
    @Tool(name = "check_vehicle_eligibility", 
          description = "Verifica si un vehículo cumple con los criterios de elegibilidad para crédito automotriz.")
    public String checkVehicleEligibility(
            @ToolParam(description = "VIN del vehículo (17 caracteres) - OBLIGATORIO", required = true) 
            String vehicleVin,
            
            @ToolParam(description = "Marca del vehículo - OBLIGATORIO", required = true) 
            String brand,
            
            @ToolParam(description = "Modelo del vehículo - OBLIGATORIO", required = true) 
            String model,
            
            @ToolParam(description = "Año del vehículo - OBLIGATORIO", required = true) 
            String yearStr,
            
            @ToolParam(description = "Valor comercial del vehículo en pesos - OBLIGATORIO", required = true) 
            String valueStr,
            
            @ToolParam(description = "Kilometraje del vehículo - OBLIGATORIO", required = true) 
            String kilometersStr) {
        
        try {
            log.info("Verificando elegibilidad de vehículo vía MCP: VIN={}, marca={}, modelo={}", 
                     vehicleVin, brand, model);
            
            // Validar parámetros básicos
            if (vehicleVin == null || vehicleVin.length() != 17) {
                return "❌ **Error:** VIN inválido. Debe tener exactamente 17 caracteres.";
            }
            
            int year;
            BigDecimal value;
            int kilometers;
            
            try {
                year = Integer.parseInt(yearStr);
                value = new BigDecimal(valueStr);
                kilometers = Integer.parseInt(kilometersStr);
            } catch (NumberFormatException e) {
                return "❌ **Error:** Formato inválido en año, valor o kilometraje.";
            }
            
            // Crear objeto Vehicle para validación
            Vehicle vehicle = Vehicle.builder()
                .vin(VehicleVIN.of(vehicleVin))
                .brand(brand.toUpperCase())
                .model(model)
                .year(year)
                .value(value)
                .kilometers(kilometers)
                .build();
            
            // Realizar verificaciones de elegibilidad
            EligibilityResult result = performEligibilityChecks(vehicle);
            
            return formatEligibilityResponse(vehicle, result);
            
        } catch (Exception e) {
            log.error("Error verificando elegibilidad de vehículo vía MCP", e);
            return formatErrorResponse(e.getMessage());
        }
    }
    
    @Tool(name = "get_authorized_vehicle_brands", 
          description = "Obtiene la lista de marcas de vehículos autorizadas para financiamiento.")
    public String getAuthorizedBrands() {
        try {
            StringBuilder result = new StringBuilder();
            result.append("🚗 **MARCAS DE VEHÍCULOS AUTORIZADAS**\n\n");
            result.append("✅ **Marcas Aprobadas para Financiamiento:**\n");
            
            AUTHORIZED_BRANDS.forEach(brand -> 
                result.append("• ").append(brand).append("\n"));
            
            result.append("\n📋 **Criterios Adicionales:**\n");
            result.append("• Año mínimo: 2018 (máximo 6 años de antigüedad)\n");
            result.append("• Kilometraje máximo: 100,000 km\n");
            result.append("• Valor mínimo: $50,000,000\n");
            result.append("• Valor máximo: $300,000,000\n");
            result.append("• SOAT y tecnomecánica vigentes\n");
            
            return result.toString();
            
        } catch (Exception e) {
            log.error("Error consultando marcas autorizadas", e);
            return formatErrorResponse(e.getMessage());
        }
    }
    
    private EligibilityResult performEligibilityChecks(Vehicle vehicle) {
        EligibilityResult result = new EligibilityResult();
        
        // Verificar VIN
        try {
            VehicleVIN.of(vehicle.getVin().getValue());
            result.addCheck("VIN", true, "VIN válido de 17 caracteres");
        } catch (Exception e) {
            result.addCheck("VIN", false, "VIN inválido: " + e.getMessage());
        }
        
        // Verificar marca autorizada
        boolean brandAuthorized = AUTHORIZED_BRANDS.contains(vehicle.getBrand().toUpperCase());
        result.addCheck("Marca", brandAuthorized, 
            brandAuthorized ? "Marca autorizada" : "Marca no autorizada para financiamiento");
        
        // Verificar año del vehículo
        int currentYear = Year.now().getValue();
        int vehicleAge = currentYear - vehicle.getYear();
        boolean yearValid = vehicleAge <= 6;
        result.addCheck("Año", yearValid, 
            yearValid ? String.format("Vehículo %d (%d años)", vehicle.getYear(), vehicleAge)
                     : String.format("Vehículo muy antiguo (%d años, máximo 6)", vehicleAge));
        
        // Verificar valor del vehículo
        BigDecimal minValue = BigDecimal.valueOf(50_000_000);
        BigDecimal maxValue = BigDecimal.valueOf(300_000_000);
        boolean valueValid = vehicle.getValue().compareTo(minValue) >= 0 && 
                           vehicle.getValue().compareTo(maxValue) <= 0;
        result.addCheck("Valor", valueValid, 
            valueValid ? "Valor dentro del rango permitido"
                      : String.format("Valor fuera del rango ($%s - $%s)", 
                                    formatCurrency(minValue), formatCurrency(maxValue)));
        
        // Verificar kilometraje
        boolean kmValid = vehicle.getKilometers() <= 100_000;
        result.addCheck("Kilometraje", kmValid, 
            kmValid ? String.format("%,d km (dentro del límite)", vehicle.getKilometers())
                   : String.format("%,d km (excede límite de 100,000 km)", vehicle.getKilometers()));
        
        // Verificar elegibilidad general usando el servicio de dominio
        try {
            boolean generalEligibility = creditEligibilityService.isVehicleEligible(vehicle);
            result.addCheck("Elegibilidad General", generalEligibility, 
                generalEligibility ? "Vehículo cumple criterios generales"
                                  : "Vehículo no cumple algunos criterios");
        } catch (Exception e) {
            result.addCheck("Elegibilidad General", false, "Error evaluando: " + e.getMessage());
        }
        
        return result;
    }
    
    private String formatEligibilityResponse(Vehicle vehicle, EligibilityResult result) {
        StringBuilder response = new StringBuilder();
        
        response.append("🚗 **VERIFICACIÓN DE ELEGIBILIDAD DEL VEHÍCULO**\n\n");
        response.append("🆔 **VIN:** ").append(vehicle.getVin().getValue()).append("\n");
        response.append("🚙 **Vehículo:** ").append(vehicle.getBrand())
                .append(" ").append(vehicle.getModel())
                .append(" ").append(vehicle.getYear()).append("\n");
        response.append("💰 **Valor:** ").append(formatCurrency(vehicle.getValue())).append("\n");
        response.append("🛣️ **Kilometraje:** ").append(String.format("%,d km", vehicle.getKilometers())).append("\n\n");
        
        response.append("📊 **RESULTADO GENERAL:** ");
        if (result.isOverallEligible()) {
            response.append("✅ **VEHÍCULO ELEGIBLE**\n\n");
        } else {
            response.append("❌ **VEHÍCULO NO ELEGIBLE**\n\n");
        }
        
        response.append("📋 **DETALLE DE VERIFICACIONES:**\n");
        result.getChecks().forEach(check -> {
            String icon = check.passed ? "✅" : "❌";
            response.append(icon).append(" **").append(check.category).append(":** ")
                    .append(check.message).append("\n");
        });
        
        if (!result.isOverallEligible()) {
            response.append("\n🔧 **ACCIONES REQUERIDAS:**\n");
            result.getChecks().stream()
                .filter(check -> !check.passed)
                .forEach(check -> response.append("• Resolver problema con: ").append(check.category).append("\n"));
        } else {
            response.append("\n🎯 **SIGUIENTE PASO:**\n");
            response.append("• El vehículo puede proceder a evaluación crediticia\n");
            response.append("• Asegurar documentación completa del vehículo\n");
        }
        
        return response.toString();
    }
    
    private String formatErrorResponse(String errorMessage) {
        return String.format("""
            ⚠️ **ERROR EN VERIFICACIÓN DE VEHÍCULO**
            
            ❌ **Error:** %s
            
            🔧 **Acción Requerida:**
            - Verificar que todos los datos del vehículo sean correctos
            - Asegurar formato válido de VIN (17 caracteres)
            - Contactar soporte técnico si el problema persiste
            """, errorMessage);
    }
    
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "N/A";
        return NumberFormat.getCurrencyInstance(new Locale("es", "CO")).format(amount);
    }
    
    private static class EligibilityResult {
        private final List<EligibilityCheck> checks = new java.util.ArrayList<>();
        
        public void addCheck(String category, boolean passed, String message) {
            checks.add(new EligibilityCheck(category, passed, message));
        }
        
        public boolean isOverallEligible() {
            return checks.stream().allMatch(check -> check.passed);
        }
        
        public List<EligibilityCheck> getChecks() {
            return checks;
        }
    }
    
    private static class EligibilityCheck {
        final String category;
        final boolean passed;
        final String message;
        
        EligibilityCheck(String category, boolean passed, String message) {
            this.category = category;
            this.passed = passed;
            this.message = message;
        }
    }
}