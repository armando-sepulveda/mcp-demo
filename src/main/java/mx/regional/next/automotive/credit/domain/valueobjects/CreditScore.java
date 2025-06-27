package mx.regional.next.automotive.credit.domain.valueobjects;

import java.util.Objects;

public class CreditScore {
    private static final int MIN_SCORE = 300;
    private static final int MAX_SCORE = 850;
    
    private final int value;
    
    public CreditScore(int value) {
        if (value < MIN_SCORE || value > MAX_SCORE) {
            throw new IllegalArgumentException(
                String.format("Score debe estar entre %d y %d", MIN_SCORE, MAX_SCORE));
        }
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public boolean isExcellent() {
        return value >= 750;
    }
    
    public boolean isGood() {
        return value >= 700 && value < 750;
    }
    
    public boolean isFair() {
        return value >= 650 && value < 700;
    }
    
    public boolean isPoor() {
        return value >= 600 && value < 650;
    }
    
    public boolean isVeryPoor() {
        return value < 600;
    }
    
    public String getCategory() {
        if (isExcellent()) return "EXCELENTE";
        if (isGood()) return "BUENO";
        if (isFair()) return "REGULAR";
        if (isPoor()) return "DEFICIENTE";
        return "MUY_DEFICIENTE";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CreditScore that = (CreditScore) obj;
        return value == that.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "CreditScore{" + "value=" + value + ", category='" + getCategory() + '\'' + '}';
    }
}