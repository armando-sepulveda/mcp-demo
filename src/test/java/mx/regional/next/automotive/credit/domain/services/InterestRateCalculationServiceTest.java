package mx.regional.next.automotive.credit.domain.services;

import mx.regional.next.automotive.credit.domain.entities.Customer;
import mx.regional.next.automotive.credit.domain.entities.Vehicle;
import mx.regional.next.automotive.credit.domain.valueobjects.*;
import mx.regional.next.automotive.credit.domain.services.InterestRateCalculationService.InterestRateCalculation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("InterestRateCalculationService Tests")
class InterestRateCalculationServiceTest {

    @InjectMocks
    private InterestRateCalculationService interestRateCalculationService;

    private Customer excellentCustomer;
    private Customer averageCustomer;
    private Customer poorCustomer;
    private Vehicle newVehicle;
    private Vehicle usedVehicle;
    private Vehicle oldVehicle;

    @BeforeEach
    void setUp() {
        // Customer excelente (ingresos altos)
        excellentCustomer = Customer.builder()
            .documentNumber(DocumentNumber.of("12345678901"))
            .firstName("Juan")
            .lastName("Excelente")
            .monthlyIncome(MonthlyIncome.of(BigDecimal.valueOf(10_000_000)))
            .workExperienceMonths(60)
            .build();

        // Customer promedio
        averageCustomer = Customer.builder()
            .documentNumber(DocumentNumber.of("23456789012"))
            .firstName("María")
            .lastName("Promedio")
            .monthlyIncome(MonthlyIncome.of(BigDecimal.valueOf(4_000_000)))
            .workExperienceMonths(36)
            .build();

        // Customer con perfil bajo
        poorCustomer = Customer.builder()
            .documentNumber(DocumentNumber.of("34567890123"))
            .firstName("Carlos")
            .lastName("Básico")
            .monthlyIncome(MonthlyIncome.of(BigDecimal.valueOf(2_000_000)))
            .workExperienceMonths(18)
            .build();

        // Vehículo nuevo
        newVehicle = Vehicle.builder()
            .vin(VehicleVIN.of("1HGBH41JXMN109186"))
            .brand("TOYOTA")
            .model("COROLLA")
            .year(Year.now().getValue())
            .value(BigDecimal.valueOf(80_000_000))
            .kilometers(1_000)
            .build();

        // Vehículo usado (3 años)
        usedVehicle = Vehicle.builder()
            .vin(VehicleVIN.of("1HGBH41JXMN109187"))
            .brand("NISSAN")
            .model("SENTRA")
            .year(Year.now().getValue() - 3)
            .value(BigDecimal.valueOf(55_000_000))
            .kilometers(45_000)
            .build();

        // Vehículo viejo (6 años)
        oldVehicle = Vehicle.builder()
            .vin(VehicleVIN.of("1HGBH41JXMN109188"))
            .brand("CHEVROLET")
            .model("AVEO")
            .year(Year.now().getValue() - 6)
            .value(BigDecimal.valueOf(35_000_000))
            .kilometers(90_000)
            .build();
    }

    @Nested
    @DisplayName("Interest Rate Calculation Tests")
    class InterestRateCalculationTests {

        @Test
        @DisplayName("Should calculate lowest rate for excellent customer with new vehicle")
        void shouldCalculateLowestRateForExcellentCustomerWithNewVehicle() {
            // Given
            CreditScore excellentScore = CreditScore.of(780);
            CreditAmount amount = CreditAmount.of(BigDecimal.valueOf(60_000_000));
            int termMonths = 48;

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                excellentScore, excellentCustomer, newVehicle, amount, termMonths);

            // Then
            assertThat(calculation).isNotNull();
            assertThat(calculation.getFinalRate()).isLessThan(BigDecimal.valueOf(0.14)); // Menos del 14%
            assertThat(calculation.getFinalRate()).isGreaterThanOrEqualTo(BigDecimal.valueOf(0.10)); // Mínimo 10%
            assertThat(calculation.getBaseRate()).isEqualTo(BigDecimal.valueOf(0.115)); // 11.5% para score 750+
            assertThat(calculation.getTotalAdjustments()).isLessThanOrEqualTo(BigDecimal.ZERO); // Debería tener descuentos
        }

        @Test
        @DisplayName("Should calculate higher rate for poor customer with old vehicle")
        void shouldCalculateHigherRateForPoorCustomerWithOldVehicle() {
            // Given
            CreditScore poorScore = CreditScore.of(620);
            CreditAmount amount = CreditAmount.of(BigDecimal.valueOf(30_000_000));
            int termMonths = 72; // Plazo largo

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                poorScore, poorCustomer, oldVehicle, amount, termMonths);

            // Then
            assertThat(calculation).isNotNull();
            assertThat(calculation.getFinalRate()).isGreaterThan(BigDecimal.valueOf(0.17)); // Más del 17%
            assertThat(calculation.getFinalRate()).isLessThanOrEqualTo(BigDecimal.valueOf(0.28)); // Máximo 28%
            assertThat(calculation.getBaseRate()).isEqualTo(BigDecimal.valueOf(0.175)); // 17.5% para score 600-649
            assertThat(calculation.getTotalAdjustments()).isGreaterThanOrEqualTo(BigDecimal.ZERO); // Debería tener sobrecargos
        }

        @Test
        @DisplayName("Should calculate medium rate for average customer")
        void shouldCalculateMedianRateForAverageCustomer() {
            // Given
            CreditScore averageScore = CreditScore.of(690);
            CreditAmount amount = CreditAmount.of(BigDecimal.valueOf(50_000_000));
            int termMonths = 60;

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, averageCustomer, usedVehicle, amount, termMonths);

            // Then
            assertThat(calculation).isNotNull();
            assertThat(calculation.getFinalRate()).isBetween(
                BigDecimal.valueOf(0.14), BigDecimal.valueOf(0.18)); // Entre 14% y 18%
            assertThat(calculation.getBaseRate()).isEqualTo(BigDecimal.valueOf(0.155)); // 15.5% para score 650-699
        }
    }

    @Nested
    @DisplayName("Base Rate by Credit Score Tests")
    class BaseRateByCreditScoreTests {

        @Test
        @DisplayName("Should return 11.5% for excellent score (750+)")
        void shouldReturn11_5PercentForExcellentScore() {
            // Given
            CreditScore excellentScore = CreditScore.of(780);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                excellentScore, averageCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getBaseRate()).isEqualTo(BigDecimal.valueOf(0.115));
        }

        @Test
        @DisplayName("Should return 13.5% for very good score (700-749)")
        void shouldReturn13_5PercentForVeryGoodScore() {
            // Given
            CreditScore veryGoodScore = CreditScore.of(720);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                veryGoodScore, averageCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getBaseRate()).isEqualTo(BigDecimal.valueOf(0.135));
        }

        @Test
        @DisplayName("Should return 15.5% for good score (650-699)")
        void shouldReturn15_5PercentForGoodScore() {
            // Given
            CreditScore goodScore = CreditScore.of(670);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                goodScore, averageCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getBaseRate()).isEqualTo(BigDecimal.valueOf(0.155));
        }

        @Test
        @DisplayName("Should return 17.5% for fair score (600-649)")
        void shouldReturn17_5PercentForFairScore() {
            // Given
            CreditScore fairScore = CreditScore.of(630);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                fairScore, averageCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getBaseRate()).isEqualTo(BigDecimal.valueOf(0.175));
        }

        @Test
        @DisplayName("Should return 21.5% for poor score (below 600)")
        void shouldReturn21_5PercentForPoorScore() {
            // Given
            CreditScore poorScore = CreditScore.of(580);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                poorScore, averageCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getBaseRate()).isEqualTo(BigDecimal.valueOf(0.215));
        }
    }

    @Nested
    @DisplayName("Vehicle Adjustment Tests")
    class VehicleAdjustmentTests {

        @Test
        @DisplayName("Should apply discount for new vehicle")
        void shouldApplyDiscountForNewVehicle() {
            // Given
            CreditScore averageScore = CreditScore.of(700);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, averageCustomer, newVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getAdjustments()).containsKey("VEHICLE");
            assertThat(calculation.getAdjustments().get("VEHICLE")).isEqualTo(BigDecimal.valueOf(-0.005)); // -0.5%
        }

        @Test
        @DisplayName("Should apply surcharge for old vehicle")
        void shouldApplySurchargeForOldVehicle() {
            // Given
            CreditScore averageScore = CreditScore.of(700);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, averageCustomer, oldVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getAdjustments()).containsKey("VEHICLE");
            assertThat(calculation.getAdjustments().get("VEHICLE")).isEqualTo(BigDecimal.valueOf(0.01)); // +1%
        }

        @Test
        @DisplayName("Should have no vehicle adjustment for medium age vehicle")
        void shouldHaveNoVehicleAdjustmentForMediumAgeVehicle() {
            // Given
            CreditScore averageScore = CreditScore.of(700);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, averageCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getAdjustments()).doesNotContainKey("VEHICLE");
        }
    }

    @Nested
    @DisplayName("Amount Adjustment Tests")
    class AmountAdjustmentTests {

        @Test
        @DisplayName("Should apply discount for high amount")
        void shouldApplyDiscountForHighAmount() {
            // Given
            CreditScore averageScore = CreditScore.of(700);
            CreditAmount highAmount = CreditAmount.of(BigDecimal.valueOf(200_000_000)); // 200M

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, averageCustomer, usedVehicle, highAmount, 60);

            // Then
            assertThat(calculation.getAdjustments()).containsKey("AMOUNT");
            assertThat(calculation.getAdjustments().get("AMOUNT")).isEqualTo(BigDecimal.valueOf(-0.005)); // -0.5%
        }

        @Test
        @DisplayName("Should have no amount adjustment for regular amount")
        void shouldHaveNoAmountAdjustmentForRegularAmount() {
            // Given
            CreditScore averageScore = CreditScore.of(700);
            CreditAmount regularAmount = CreditAmount.of(BigDecimal.valueOf(80_000_000)); // 80M

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, averageCustomer, usedVehicle, regularAmount, 60);

            // Then
            assertThat(calculation.getAdjustments()).doesNotContainKey("AMOUNT");
        }
    }

    @Nested
    @DisplayName("Term Adjustment Tests")
    class TermAdjustmentTests {

        @Test
        @DisplayName("Should apply surcharge for long term")
        void shouldApplySurchargeForLongTerm() {
            // Given
            CreditScore averageScore = CreditScore.of(700);
            int longTerm = 72; // Más de 60 meses

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, averageCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), longTerm);

            // Then
            assertThat(calculation.getAdjustments()).containsKey("TERM");
            assertThat(calculation.getAdjustments().get("TERM")).isEqualTo(BigDecimal.valueOf(0.005)); // +0.5%
        }

        @Test
        @DisplayName("Should have no term adjustment for standard term")
        void shouldHaveNoTermAdjustmentForStandardTerm() {
            // Given
            CreditScore averageScore = CreditScore.of(700);
            int standardTerm = 60; // 60 meses o menos

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, averageCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), standardTerm);

            // Then
            assertThat(calculation.getAdjustments()).doesNotContainKey("TERM");
        }
    }

    @Nested
    @DisplayName("Customer Adjustment Tests")
    class CustomerAdjustmentTests {

        @Test
        @DisplayName("Should apply discount for high income customer")
        void shouldApplyDiscountForHighIncomeCustomer() {
            // Given
            CreditScore averageScore = CreditScore.of(700);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, excellentCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getAdjustments()).containsKey("CUSTOMER");
            assertThat(calculation.getAdjustments().get("CUSTOMER")).isEqualTo(BigDecimal.valueOf(-0.005)); // -0.5%
        }

        @Test
        @DisplayName("Should have no customer adjustment for regular income")
        void shouldHaveNoCustomerAdjustmentForRegularIncome() {
            // Given
            CreditScore averageScore = CreditScore.of(700);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                averageScore, averageCustomer, usedVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 60);

            // Then
            assertThat(calculation.getAdjustments()).doesNotContainKey("CUSTOMER");
        }
    }

    @Nested
    @DisplayName("Monthly Installment Calculation Tests")
    class MonthlyInstallmentCalculationTests {

        @Test
        @DisplayName("Should calculate correct monthly installment")
        void shouldCalculateCorrectMonthlyInstallment() {
            // Given
            CreditAmount loanAmount = CreditAmount.of(BigDecimal.valueOf(60_000_000));
            BigDecimal annualRate = BigDecimal.valueOf(0.15); // 15%
            int termMonths = 60;

            // When
            BigDecimal monthlyInstallment = interestRateCalculationService.calculateMonthlyInstallment(
                loanAmount, annualRate, termMonths);

            // Then
            assertThat(monthlyInstallment).isNotNull();
            assertThat(monthlyInstallment).isGreaterThan(BigDecimal.valueOf(1_000_000)); // Al menos 1M
            assertThat(monthlyInstallment).isLessThan(BigDecimal.valueOf(2_000_000)); // Menos de 2M
            
            // Verificar que la cuota es razonable para un préstamo de 60M a 60 meses
            BigDecimal expectedRange = loanAmount.getValue().divide(BigDecimal.valueOf(termMonths), 2, BigDecimal.ROUND_HALF_UP);
            assertThat(monthlyInstallment).isGreaterThan(expectedRange); // Debe ser mayor que solo capital
        }

        @Test
        @DisplayName("Should handle edge case with minimum values")
        void shouldHandleEdgeCaseWithMinimumValues() {
            // Given
            CreditAmount minAmount = CreditAmount.of(BigDecimal.valueOf(50_000));
            BigDecimal minRate = BigDecimal.valueOf(0.10); // 10%
            int minTerm = 12;

            // When
            BigDecimal monthlyInstallment = interestRateCalculationService.calculateMonthlyInstallment(
                minAmount, minRate, minTerm);

            // Then
            assertThat(monthlyInstallment).isNotNull();
            assertThat(monthlyInstallment).isGreaterThan(BigDecimal.ZERO);
            assertThat(monthlyInstallment).isLessThan(minAmount.getValue()); // Cuota menor que el total
        }

        @Test
        @DisplayName("Should throw exception for invalid parameters")
        void shouldThrowExceptionForInvalidParameters() {
            // Given
            CreditAmount validAmount = CreditAmount.of(BigDecimal.valueOf(1_000_000));
            BigDecimal validRate = BigDecimal.valueOf(0.15);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                interestRateCalculationService.calculateMonthlyInstallment(validAmount, validRate, 0));
            
            assertThrows(IllegalArgumentException.class, () -> 
                interestRateCalculationService.calculateMonthlyInstallment(validAmount, validRate, -5));
            
            assertThrows(IllegalArgumentException.class, () -> 
                interestRateCalculationService.calculateMonthlyInstallment(validAmount, BigDecimal.ZERO, 60));
            
            assertThrows(IllegalArgumentException.class, () -> 
                interestRateCalculationService.calculateMonthlyInstallment(validAmount, BigDecimal.valueOf(-0.05), 60));
        }
    }

    @Nested
    @DisplayName("Rate Limits Tests")
    class RateLimitsTests {

        @Test
        @DisplayName("Should not go below minimum rate")
        void shouldNotGoBelowMinimumRate() {
            // Given - Escenario que podría resultar en tasa muy baja
            CreditScore excellentScore = CreditScore.of(850);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                excellentScore, excellentCustomer, newVehicle, CreditAmount.of(BigDecimal.valueOf(200_000_000)), 48);

            // Then
            assertThat(calculation.getFinalRate()).isGreaterThanOrEqualTo(BigDecimal.valueOf(0.10)); // Mínimo 10%
        }

        @Test
        @DisplayName("Should not exceed maximum rate")
        void shouldNotExceedMaximumRate() {
            // Given - Escenario que podría resultar en tasa muy alta
            CreditScore veryPoorScore = CreditScore.of(400);

            // When
            InterestRateCalculation calculation = interestRateCalculationService.calculateInterestRate(
                veryPoorScore, poorCustomer, oldVehicle, CreditAmount.of(BigDecimal.valueOf(50_000_000)), 84);

            // Then
            assertThat(calculation.getFinalRate()).isLessThanOrEqualTo(BigDecimal.valueOf(0.28)); // Máximo 28%
        }
    }

    @Nested
    @DisplayName("Legacy Method Compatibility Tests")
    class LegacyMethodCompatibilityTests {

        @Test
        @DisplayName("Should work with legacy method signature")
        void shouldWorkWithLegacyMethodSignature() {
            // Given
            BigDecimal loanAmount = BigDecimal.valueOf(50_000_000);
            BigDecimal annualRate = BigDecimal.valueOf(0.15);
            int termMonths = 60;

            // When
            BigDecimal monthlyInstallment = interestRateCalculationService.calculateMonthlyInstallment(
                loanAmount, annualRate, termMonths);

            // Then
            assertThat(monthlyInstallment).isNotNull();
            assertThat(monthlyInstallment).isGreaterThan(BigDecimal.ZERO);
        }
    }
}