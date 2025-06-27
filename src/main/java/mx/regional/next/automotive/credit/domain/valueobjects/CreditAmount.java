package mx.regional.next.automotive.credit.domain.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

public class CreditAmount {
    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(50000);
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(2000000000);
    
    private final BigDecimal value;
    
    public CreditAmount(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        if (value.compareTo(MIN_AMOUNT) < 0) {
            throw new IllegalArgumentException("Monto mínimo: " + MIN_AMOUNT);
        }
        if (value.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException("Monto máximo: " + MAX_AMOUNT);
        }
        
        this.value = value;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public CreditAmount add(CreditAmount other) {
        return new CreditAmount(this.value.add(other.value));
    }
    
    public boolean isGreaterThan(CreditAmount other) {
        return this.value.compareTo(other.value) > 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CreditAmount that = (CreditAmount) obj;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "CreditAmount{" + "value=" + value + '}';
    }
}