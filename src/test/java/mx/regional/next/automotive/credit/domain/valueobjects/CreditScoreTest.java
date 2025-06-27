package mx.regional.next.automotive.credit.domain.valueobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CreditScoreTest {

    @Test
    void shouldCreateValidCreditScore() {
        // Given
        int validScore = 750;
        
        // When
        CreditScore creditScore = new CreditScore(validScore);
        
        // Then
        assertEquals(validScore, creditScore.getValue());
    }

    @Test
    void shouldThrowExceptionForScoreBelowMinimum() {
        // Given
        int belowMinimum = 250;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> new CreditScore(belowMinimum));
    }

    @Test
    void shouldThrowExceptionForScoreAboveMaximum() {
        // Given
        int aboveMaximum = 900;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> new CreditScore(aboveMaximum));
    }

    @Test
    void shouldIdentifyExcellentScore() {
        // Given
        CreditScore excellentScore = new CreditScore(800);
        
        // When & Then
        assertTrue(excellentScore.isExcellent());
        assertFalse(excellentScore.isGood());
        assertEquals("EXCELENTE", excellentScore.getCategory());
    }

    @Test
    void shouldIdentifyGoodScore() {
        // Given
        CreditScore goodScore = new CreditScore(720);
        
        // When & Then
        assertTrue(goodScore.isGood());
        assertFalse(goodScore.isExcellent());
        assertFalse(goodScore.isFair());
        assertEquals("BUENO", goodScore.getCategory());
    }

    @Test
    void shouldIdentifyFairScore() {
        // Given
        CreditScore fairScore = new CreditScore(670);
        
        // When & Then
        assertTrue(fairScore.isFair());
        assertFalse(fairScore.isGood());
        assertFalse(fairScore.isPoor());
        assertEquals("REGULAR", fairScore.getCategory());
    }

    @Test
    void shouldIdentifyPoorScore() {
        // Given
        CreditScore poorScore = new CreditScore(620);
        
        // When & Then
        assertTrue(poorScore.isPoor());
        assertFalse(poorScore.isFair());
        assertFalse(poorScore.isVeryPoor());
        assertEquals("DEFICIENTE", poorScore.getCategory());
    }

    @Test
    void shouldIdentifyVeryPoorScore() {
        // Given
        CreditScore veryPoorScore = new CreditScore(550);
        
        // When & Then
        assertTrue(veryPoorScore.isVeryPoor());
        assertFalse(veryPoorScore.isPoor());
        assertEquals("MUY_DEFICIENTE", veryPoorScore.getCategory());
    }

    @Test
    void shouldImplementEqualsCorrectly() {
        // Given
        CreditScore score1 = new CreditScore(750);
        CreditScore score2 = new CreditScore(750);
        CreditScore score3 = new CreditScore(700);
        
        // When & Then
        assertEquals(score1, score2);
        assertNotEquals(score1, score3);
        assertEquals(score1.hashCode(), score2.hashCode());
    }
}