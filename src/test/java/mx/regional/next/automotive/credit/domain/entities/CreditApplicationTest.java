package mx.regional.next.automotive.credit.domain.entities;

import mx.regional.next.automotive.credit.domain.valueobjects.*;
import mx.regional.next.automotive.credit.domain.enums.*;
import mx.regional.next.automotive.credit.domain.exceptions.InvalidCreditAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreditApplicationTest {

    private Customer validCustomer;
    private Vehicle validVehicle;
    private CreditAmount validAmount;

    @BeforeEach
    void setUp() {
        validCustomer = new Customer(
            new DocumentNumber("12345678"),
            DocumentType.CEDULA,
            "Juan",
            "Perez",
            "juan@email.com",
            "3001234567",
            LocalDate.of(1990, 1, 1),
            new CreditAmount(BigDecimal.valueOf(300000)),
            new CreditAmount(BigDecimal.valueOf(50000)),
            "Ingeniero",
            36
        );

        validVehicle = new Vehicle(
            new VehicleVIN("1HGBH41JXMN109186"),
            "TOYOTA",
            "COROLLA",
            2022,
            VehicleType.SEDAN,
            new CreditAmount(BigDecimal.valueOf(800000000)),
            50000,
            "Blanco",
            "2.0L",
            "Automática"
        );

        validAmount = new CreditAmount(BigDecimal.valueOf(600000000));
    }

    @Test
    void shouldCreateValidCreditApplication() {
        // When
        CreditApplication application = new CreditApplication(validCustomer, validVehicle, validAmount);

        // Then
        assertNotNull(application.getId());
        assertEquals(validCustomer, application.getCustomer());
        assertEquals(validVehicle, application.getVehicle());
        assertEquals(validAmount, application.getRequestedAmount());
        assertEquals(CreditStatus.PENDING, application.getStatus());
        assertNotNull(application.getApplicationDate());
        assertNotNull(application.getLastUpdateDate());
        assertTrue(application.isPending());
        assertFalse(application.isApproved());
        assertFalse(application.isRejected());
    }

    @Test
    void shouldThrowExceptionForNullCustomer() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> new CreditApplication(null, validVehicle, validAmount));
    }

    @Test
    void shouldThrowExceptionForNullVehicle() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> new CreditApplication(validCustomer, null, validAmount));
    }

    @Test
    void shouldThrowExceptionForNullAmount() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> new CreditApplication(validCustomer, validVehicle, null));
    }

    @Test
    void shouldThrowExceptionWhenAmountExceedsVehicleMaxCredit() {
        // Given
        CreditAmount excessiveAmount = new CreditAmount(BigDecimal.valueOf(800000000)); // Más del 90% del valor
        
        // When & Then
        assertThrows(InvalidCreditAmountException.class,
                    () -> new CreditApplication(validCustomer, validVehicle, excessiveAmount));
    }

    @Test
    void shouldApproveApplicationWithValidScore() {
        // Given
        CreditApplication application = new CreditApplication(validCustomer, validVehicle, validAmount);
        CreditScore validScore = new CreditScore(750);

        // When
        application.approve(validScore);

        // Then
        assertEquals(CreditStatus.APPROVED, application.getStatus());
        assertEquals(validScore, application.getCreditScore());
        assertTrue(application.isApproved());
        assertFalse(application.isPending());
    }

    @Test
    void shouldThrowExceptionWhenApprovingWithLowScore() {
        // Given
        CreditApplication application = new CreditApplication(validCustomer, validVehicle, validAmount);
        CreditScore lowScore = new CreditScore(550);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> application.approve(lowScore));
    }

    @Test
    void shouldThrowExceptionWhenApprovingNonPendingApplication() {
        // Given
        CreditApplication application = new CreditApplication(validCustomer, validVehicle, validAmount);
        application.reject("Test rejection");
        CreditScore validScore = new CreditScore(750);

        // When & Then
        assertThrows(IllegalStateException.class,
                    () -> application.approve(validScore));
    }

    @Test
    void shouldRejectApplicationWithReason() {
        // Given
        CreditApplication application = new CreditApplication(validCustomer, validVehicle, validAmount);
        String rejectionReason = "Insufficient income";

        // When
        application.reject(rejectionReason);

        // Then
        assertEquals(CreditStatus.REJECTED, application.getStatus());
        assertEquals(rejectionReason, application.getRejectionReason());
        assertTrue(application.isRejected());
        assertFalse(application.isPending());
    }

    @Test
    void shouldThrowExceptionWhenRejectingWithEmptyReason() {
        // Given
        CreditApplication application = new CreditApplication(validCustomer, validVehicle, validAmount);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                    () -> application.reject(""));
    }

    @Test
    void shouldThrowExceptionWhenRejectingNonPendingApplication() {
        // Given
        CreditApplication application = new CreditApplication(validCustomer, validVehicle, validAmount);
        CreditScore validScore = new CreditScore(750);
        application.approve(validScore);

        // When & Then
        assertThrows(IllegalStateException.class,
                    () -> application.reject("Test rejection"));
    }

    @Test
    void shouldCancelApplication() {
        // Given
        CreditApplication application = new CreditApplication(validCustomer, validVehicle, validAmount);

        // When
        application.cancel();

        // Then
        assertEquals(CreditStatus.CANCELLED, application.getStatus());
    }

    @Test
    void shouldEvaluateEligibilityForProcessing() {
        // Given
        CreditApplication application = new CreditApplication(validCustomer, validVehicle, validAmount);

        // When
        boolean isEligible = application.isEligibleForProcessing();

        // Then
        assertTrue(isEligible);
    }
}