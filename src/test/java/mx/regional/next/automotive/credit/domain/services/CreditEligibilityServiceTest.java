package mx.regional.next.automotive.credit.domain.services;

import mx.regional.next.automotive.credit.domain.entities.*;
import mx.regional.next.automotive.credit.domain.valueobjects.*;
import mx.regional.next.automotive.credit.domain.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreditEligibilityServiceTest {

    private CreditEligibilityService eligibilityService;
    private Customer validCustomer;
    private Vehicle validVehicle;
    private CreditApplication validApplication;

    @BeforeEach
    void setUp() {
        eligibilityService = new CreditEligibilityService();

        validCustomer = new Customer(
            new DocumentNumber("12345678"),
            DocumentType.CEDULA,
            "Juan",
            "Perez",
            "juan@email.com",
            "3001234567",
            LocalDate.of(1985, 1, 1),
            new CreditAmount(BigDecimal.valueOf(500000)), // Buen ingreso
            new CreditAmount(BigDecimal.valueOf(100000)), // Deuda moderada
            "Ingeniero",
            48 // Buena experiencia laboral
        );

        validVehicle = new Vehicle(
            new VehicleVIN("1HGBH41JXMN109186"),
            "TOYOTA",
            "COROLLA",
            2020, // Año válido
            VehicleType.SEDAN,
            new CreditAmount(BigDecimal.valueOf(800000000)),
            80000, // Kilometraje válido
            "Blanco",
            "2.0L",
            "Automática"
        );

        validApplication = new CreditApplication(
            validCustomer,
            validVehicle,
            new CreditAmount(BigDecimal.valueOf(600000000))
        );
    }

    @Test
    void shouldReturnTrueForEligibleApplication() {
        // When
        boolean isEligible = eligibilityService.isEligible(validApplication);

        // Then
        assertTrue(isEligible);
    }

    @Test
    void shouldReturnFalseForInsufficientIncome() {
        // Given
        Customer lowIncomeCustomer = new Customer(
            new DocumentNumber("12345678"),
            DocumentType.CEDULA,
            "Juan",
            "Perez",
            "juan@email.com",
            "3001234567",
            LocalDate.of(1985, 1, 1),
            new CreditAmount(BigDecimal.valueOf(200000)), // Ingreso bajo (menor a 300,000)
            new CreditAmount(BigDecimal.valueOf(50000)),
            "Empleado",
            24
        );

        CreditApplication lowIncomeApplication = new CreditApplication(
            lowIncomeCustomer,
            validVehicle,
            new CreditAmount(BigDecimal.valueOf(600000000))
        );

        // When
        boolean isEligible = eligibilityService.isEligible(lowIncomeApplication);

        // Then
        assertFalse(isEligible);
    }

    @Test
    void shouldReturnFalseForHighDebtRatio() {
        // Given
        Customer highDebtCustomer = new Customer(
            new DocumentNumber("12345678"),
            DocumentType.CEDULA,
            "Juan",
            "Perez",
            "juan@email.com",
            "3001234567",
            LocalDate.of(1985, 1, 1),
            new CreditAmount(BigDecimal.valueOf(500000)),
            new CreditAmount(BigDecimal.valueOf(250000)), // 50% de deuda/ingreso
            "Ingeniero",
            48
        );

        CreditApplication highDebtApplication = new CreditApplication(
            highDebtCustomer,
            validVehicle,
            new CreditAmount(BigDecimal.valueOf(600000000))
        );

        // When
        boolean isEligible = eligibilityService.isEligible(highDebtApplication);

        // Then
        assertFalse(isEligible);
    }

    @Test
    void shouldReturnFalseForOldVehicle() {
        // Given
        Vehicle oldVehicle = new Vehicle(
            new VehicleVIN("1HGBH41JXMN109186"),
            "TOYOTA",
            "COROLLA",
            2015, // Muy viejo
            VehicleType.SEDAN,
            new CreditAmount(BigDecimal.valueOf(800000000)),
            50000,
            "Blanco",
            "2.0L",
            "Automática"
        );

        CreditApplication oldVehicleApplication = new CreditApplication(
            validCustomer,
            oldVehicle,
            new CreditAmount(BigDecimal.valueOf(600000000))
        );

        // When
        boolean isEligible = eligibilityService.isEligible(oldVehicleApplication);

        // Then
        assertFalse(isEligible);
    }

    @Test
    void shouldReturnFalseForHighKilometersVehicle() {
        // Given
        Vehicle highKmVehicle = new Vehicle(
            new VehicleVIN("1HGBH41JXMN109186"),
            "TOYOTA",
            "COROLLA",
            2020,
            VehicleType.SEDAN,
            new CreditAmount(BigDecimal.valueOf(800000000)),
            150000, // Muchos kilómetros
            "Blanco",
            "2.0L",
            "Automática"
        );

        CreditApplication highKmApplication = new CreditApplication(
            validCustomer,
            highKmVehicle,
            new CreditAmount(BigDecimal.valueOf(600000000))
        );

        // When
        boolean isEligible = eligibilityService.isEligible(highKmApplication);

        // Then
        assertFalse(isEligible);
    }

    @Test
    void shouldCalculateMaxEligibleAmount() {
        // When
        CreditAmount maxAmount = eligibilityService.calculateMaxEligibleAmount(validCustomer, validVehicle);

        // Then
        assertNotNull(maxAmount);
        assertTrue(maxAmount.getValue().compareTo(BigDecimal.ZERO) > 0);
        
        // Verificar que no excede el 90% del valor del vehículo
        BigDecimal maxByVehicle = validVehicle.getValue().getValue().multiply(BigDecimal.valueOf(0.90));
        assertTrue(maxAmount.getValue().compareTo(maxByVehicle) <= 0);
    }

    @Test
    void shouldLimitMaxAmountByVehicleValue() {
        // Given - Customer con mucho ingreso
        Customer highIncomeCustomer = new Customer(
            new DocumentNumber("12345678"),
            DocumentType.CEDULA,
            "Juan",
            "Perez",
            "juan@email.com",
            "3001234567",
            LocalDate.of(1985, 1, 1),
            new CreditAmount(BigDecimal.valueOf(500000)), // Ingreso muy alto
            new CreditAmount(BigDecimal.valueOf(100000)),
            "CEO",
            120
        );

        // When
        CreditAmount maxAmount = eligibilityService.calculateMaxEligibleAmount(highIncomeCustomer, validVehicle);

        // Then
        BigDecimal expectedMaxByVehicle = validVehicle.getValue().getValue().multiply(BigDecimal.valueOf(0.90));
        assertTrue(maxAmount.getValue().compareTo(expectedMaxByVehicle) <= 0);
    }
}