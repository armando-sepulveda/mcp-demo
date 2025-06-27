package mx.regional.next.automotive.credit.domain.entities;

import mx.regional.next.automotive.credit.domain.valueobjects.VehicleVIN;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditAmount;
import mx.regional.next.automotive.credit.domain.enums.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Vehicle {
    private static final Set<String> APPROVED_BRANDS = Set.of(
        "TOYOTA", "CHEVROLET", "RENAULT", "NISSAN", "HYUNDAI", "KIA", "MAZDA", "FORD"
    );
    
    private final VehicleVIN vin;
    private final String brand;
    private final String model;
    private final int year;
    private final VehicleType type;
    private final CreditAmount value;
    private final int kilometers;
    private final String color;
    private final String engine;
    private final String transmission;
    
    public Vehicle(VehicleVIN vin, String brand, String model, int year, 
                  VehicleType type, CreditAmount value, int kilometers, 
                  String color, String engine, String transmission) {
        
        validateConstructorParameters(vin, brand, model, year, value, kilometers);
        
        this.vin = vin;
        this.brand = brand.toUpperCase();
        this.model = model;
        this.year = year;
        this.type = type;
        this.value = value;
        this.kilometers = kilometers;
        this.color = color;
        this.engine = engine;
        this.transmission = transmission;
    }
    
    private void validateConstructorParameters(VehicleVIN vin, String brand, String model,
                                             int year, CreditAmount value, int kilometers) {
        if (vin == null) {
            throw new IllegalArgumentException("El VIN es obligatorio");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca es obligatoria");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo es obligatorio");
        }
        if (year < 2010 || year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Año del vehículo inválido");
        }
        if (value == null) {
            throw new IllegalArgumentException("El valor del vehículo es obligatorio");
        }
        if (kilometers < 0) {
            throw new IllegalArgumentException("El kilometraje no puede ser negativo");
        }
    }
    
    public boolean isFromApprovedBrand() {
        return APPROVED_BRANDS.contains(brand.toUpperCase());
    }
    
    public boolean isEligibleForCredit() {
        return isFromApprovedBrand() &&
               year >= 2018 &&
               kilometers <= 100000 &&
               value.getValue().compareTo(BigDecimal.valueOf(50000000)) >= 0;
    }
    
    public CreditAmount getMaxCreditAmount() {
        // Máximo 90% del valor del vehículo
        BigDecimal maxAmount = value.getValue().multiply(BigDecimal.valueOf(0.90));
        return new CreditAmount(maxAmount);
    }
    
    public int getVehicleAge() {
        return LocalDate.now().getYear() - year;
    }
    
    public boolean isNew() {
        return getVehicleAge() <= 1;
    }
    
    public boolean isSemiNew() {
        return getVehicleAge() > 1 && getVehicleAge() <= 3;
    }
    
    public boolean isUsed() {
        return getVehicleAge() > 3;
    }
    
    // Getters
    public VehicleVIN getVin() { return vin; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public VehicleType getType() { return type; }
    public CreditAmount getValue() { return value; }
    public int getKilometers() { return kilometers; }
    public String getColor() { return color; }
    public String getEngine() { return engine; }
    public String getTransmission() { return transmission; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle vehicle = (Vehicle) obj;
        return Objects.equals(vin, vehicle.vin);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "vin=" + vin +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", value=" + value +
                '}';
    }
}