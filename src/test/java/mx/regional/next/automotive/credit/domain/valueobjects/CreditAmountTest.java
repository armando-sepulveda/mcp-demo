package mx.regional.next.automotive.credit.domain.valueobjects;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CreditAmountTest {

    @Test
    void shouldCreateValidCreditAmount() {
        // Given
        BigDecimal validAmount = BigDecimal.valueOf(100000);
        
        // When
        CreditAmount creditAmount = new CreditAmount(validAmount);
        
        // Then
        assertEquals(validAmount, creditAmount.getValue());
    }

    @Test
    void shouldThrowExceptionForNullAmount() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> new CreditAmount(null));
    }

    @Test
    void shouldThrowExceptionForAmountBelowMinimum() {
        // Given
        BigDecimal belowMinimum = BigDecimal.valueOf(40000);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                    () -> new CreditAmount(belowMinimum));
        assertTrue(exception.getMessage().contains("Monto mínimo"));
    }

    @Test
    void shouldThrowExceptionForAmountAboveMaximum() {
        // Given
        BigDecimal aboveMaximum = BigDecimal.valueOf(3000000000L);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                    () -> new CreditAmount(aboveMaximum));
        assertTrue(exception.getMessage().contains("Monto máximo"));
    }

    @Test
    void shouldAddTwoCreditAmounts() {
        // Given
        CreditAmount amount1 = new CreditAmount(BigDecimal.valueOf(100000));
        CreditAmount amount2 = new CreditAmount(BigDecimal.valueOf(50000));
        
        // When
        CreditAmount result = amount1.add(amount2);
        
        // Then
        assertEquals(BigDecimal.valueOf(150000), result.getValue());
    }

    @Test
    void shouldCompareAmounts() {
        // Given
        CreditAmount smaller = new CreditAmount(BigDecimal.valueOf(100000));
        CreditAmount larger = new CreditAmount(BigDecimal.valueOf(200000));
        
        // When & Then
        assertTrue(larger.isGreaterThan(smaller));
        assertFalse(smaller.isGreaterThan(larger));
    }

    @Test
    void shouldImplementEqualsCorrectly() {
        // Given
        CreditAmount amount1 = new CreditAmount(BigDecimal.valueOf(100000));
        CreditAmount amount2 = new CreditAmount(BigDecimal.valueOf(100000));
        CreditAmount amount3 = new CreditAmount(BigDecimal.valueOf(200000));
        
        // When & Then
        assertEquals(amount1, amount2);
        assertNotEquals(amount1, amount3);
        assertEquals(amount1.hashCode(), amount2.hashCode());
    }
}