package mx.regional.next.automotive.credit.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

public class VehicleVIN {
    private static final Pattern VIN_PATTERN = Pattern.compile("^[A-HJ-NPR-Z0-9]{17}$");
    
    private final String value;
    
    public VehicleVIN(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El VIN no puede ser nulo o vacío");
        }
        
        String cleanValue = value.trim().toUpperCase();
        
        if (!VIN_PATTERN.matcher(cleanValue).matches()) {
            throw new IllegalArgumentException("Formato de VIN inválido: " + value);
        }
        
        this.value = cleanValue;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VehicleVIN that = (VehicleVIN) obj;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "VehicleVIN{" + "value='" + value + '\'' + '}';
    }
}