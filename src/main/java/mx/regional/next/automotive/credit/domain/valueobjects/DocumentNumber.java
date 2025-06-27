package mx.regional.next.automotive.credit.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

public class DocumentNumber {
    private static final Pattern CEDULA_PATTERN = Pattern.compile("^[0-9]{8,10}$");
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{6,8}$");
    
    private final String value;
    
    public DocumentNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de documento no puede ser nulo o vacío");
        }
        
        String cleanValue = value.trim().toUpperCase();
        
        if (!isValidDocument(cleanValue)) {
            throw new IllegalArgumentException("Formato de documento inválido: " + value);
        }
        
        this.value = cleanValue;
    }
    
    private boolean isValidDocument(String document) {
        return CEDULA_PATTERN.matcher(document).matches() || 
               PASSPORT_PATTERN.matcher(document).matches();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DocumentNumber that = (DocumentNumber) obj;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "DocumentNumber{" + "value='" + value + '\'' + '}';
    }
}