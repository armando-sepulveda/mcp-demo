package mx.regional.next.automotive.credit.domain.services;

import mx.regional.next.automotive.credit.domain.entities.CreditApplication;
import mx.regional.next.automotive.credit.domain.entities.Customer;
import mx.regional.next.automotive.credit.domain.entities.Vehicle;
import mx.regional.next.automotive.credit.domain.valueobjects.*;
import mx.regional.next.automotive.credit.domain.services.RiskCalculationService.RiskAssessment;
import mx.regional.next.automotive.credit.domain.services.RiskCalculationService.RiskFactor;
import mx.regional.next.automotive.credit.domain.services.RiskCalculationService.RiskLevel;

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
@DisplayName("RiskCalculationService Tests")
class RiskCalculationServiceTest {

    @InjectMocks
    private RiskCalculationService riskCalculationService;

    private Customer customerHighIncome;
    private Customer customerLowIncome;
    private Vehicle newVehicle;
    private Vehicle oldVehicle;
    private CreditApplication applicationLowRisk;
    private CreditApplication applicationHighRisk;

    @BeforeEach
    void setUp() {
        // Customer con ingresos altos (bajo riesgo)
        customerHighIncome = Customer.builder()
            .documentNumber(DocumentNumber.of("12345678901"))
            .firstName("Juan")
            .lastName("Pérez")
            .monthlyIncome(MonthlyIncome.of(BigDecimal.valueOf(8_000_000)))
            .workExperienceMonths(48) // 4 años
            .build();

        // Customer con ingresos bajos (alto riesgo)
        customerLowIncome = Customer.builder()
            .documentNumber(DocumentNumber.of("98765432109"))
            .firstName("María")
            .lastName("González")
            .monthlyIncome(MonthlyIncome.of(BigDecimal.valueOf(2_000_000)))
            .workExperienceMonths(12) // 1 año
            .build();

        // Vehículo nuevo (bajo riesgo)
        newVehicle = Vehicle.builder()
            .vin(VehicleVIN.of("1HGBH41JXMN109186"))
            .brand("TOYOTA")
            .model("COROLLA")
            .year(Year.now().getValue())
            .value(BigDecimal.valueOf(80_000_000))
            .kilometers(5_000)
            .build();

        // Vehículo usado (mayor riesgo)
        oldVehicle = Vehicle.builder()
            .vin(VehicleVIN.of("1HGBH41JXMN109187"))
            .brand("CHEVROLET")
            .model("AVEO")
            .year(2019)
            .value(BigDecimal.valueOf(35_000_000))
            .kilometers(85_000)
            .build();

        // Aplicación de bajo riesgo
        applicationLowRisk = CreditApplication.builder()
            .customer(customerHighIncome)
            .vehicle(newVehicle)
            .requestedAmount(CreditAmount.of(BigDecimal.valueOf(60_000_000)))
            .build();

        // Aplicación de alto riesgo
        applicationHighRisk = CreditApplication.builder()
            .customer(customerLowIncome)
            .vehicle(oldVehicle)
            .requestedAmount(CreditAmount.of(BigDecimal.valueOf(30_000_000)))
            .build();
    }

    @Nested
    @DisplayName("Risk Assessment Tests")
    class RiskAssessmentTests {

        @Test
        @DisplayName("Should approve low risk application")
        void shouldApproveLowRiskApplication() {
            // Given
            CreditScore excellentScore = CreditScore.of(780);

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(applicationLowRisk, excellentScore);

            // Then
            assertThat(assessment).isNotNull();
            assertThat(assessment.isApproved()).isTrue();
            assertThat(assessment.getOverallRiskLevel()).isEqualTo(RiskLevel.LOW);
            assertThat(assessment.getRiskScore()).isGreaterThan(BigDecimal.valueOf(75));
            assertThat(assessment.getFactors()).hasSizeGreaterThan(3);
        }

        @Test
        @DisplayName("Should reject high risk application")
        void shouldRejectHighRiskApplication() {
            // Given
            CreditScore poorScore = CreditScore.of(580);

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(applicationHighRisk, poorScore);

            // Then
            assertThat(assessment).isNotNull();
            assertThat(assessment.isApproved()).isFalse();
            assertThat(assessment.getOverallRiskLevel()).isEqualTo(RiskLevel.HIGH);
            assertThat(assessment.getRiskScore()).isLessThan(BigDecimal.valueOf(60));
        }

        @Test
        @DisplayName("Should have medium risk for borderline application")
        void shouldHaveMediumRiskForBorderlineApplication() {
            // Given
            CreditScore averageScore = CreditScore.of(670);
            
            // Create a medium risk customer
            Customer mediumRiskCustomer = Customer.builder()
                .documentNumber(DocumentNumber.of("11111111111"))
                .firstName("Carlos")
                .lastName("Medina")
                .monthlyIncome(MonthlyIncome.of(BigDecimal.valueOf(4_000_000)))
                .workExperienceMonths(30)
                .build();

            Vehicle mediumRiskVehicle = Vehicle.builder()
                .vin(VehicleVIN.of("1HGBH41JXMN109188"))
                .brand("NISSAN")
                .model("SENTRA")
                .year(2021)
                .value(BigDecimal.valueOf(55_000_000))
                .kilometers(40_000)
                .build();

            CreditApplication mediumRiskApplication = CreditApplication.builder()
                .customer(mediumRiskCustomer)
                .vehicle(mediumRiskVehicle)
                .requestedAmount(CreditAmount.of(BigDecimal.valueOf(45_000_000)))
                .build();

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(mediumRiskApplication, averageScore);

            // Then
            assertThat(assessment).isNotNull();
            assertThat(assessment.getOverallRiskLevel()).isIn(RiskLevel.MEDIUM, RiskLevel.LOW);
            assertThat(assessment.getRiskScore()).isBetween(BigDecimal.valueOf(60), BigDecimal.valueOf(80));
        }
    }

    @Nested
    @DisplayName("Credit Score Evaluation Tests")
    class CreditScoreEvaluationTests {

        @Test
        @DisplayName("Should return LOW risk for excellent credit score")
        void shouldReturnLowRiskForExcellentScore() {
            // Given
            CreditScore excellentScore = CreditScore.of(800);

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(applicationLowRisk, excellentScore);

            // Then
            RiskFactor creditScoreFactor = assessment.getFactors().stream()
                .filter(factor -> "CREDIT_SCORE".equals(factor.getCategory()))
                .findFirst()
                .orElseThrow();

            assertThat(creditScoreFactor.getLevel()).isEqualTo(RiskLevel.LOW);
            assertThat(creditScoreFactor.getScore()).isGreaterThanOrEqualTo(85);
        }

        @Test
        @DisplayName("Should return HIGH risk for poor credit score")
        void shouldReturnHighRiskForPoorScore() {
            // Given
            CreditScore poorScore = CreditScore.of(550);

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(applicationHighRisk, poorScore);

            // Then
            RiskFactor creditScoreFactor = assessment.getFactors().stream()
                .filter(factor -> "CREDIT_SCORE".equals(factor.getCategory()))
                .findFirst()
                .orElseThrow();

            assertThat(creditScoreFactor.getLevel()).isEqualTo(RiskLevel.HIGH);
            assertThat(creditScoreFactor.getScore()).isLessThanOrEqualTo(30);
        }

        @Test
        @DisplayName("Should return MEDIUM risk for average credit score")
        void shouldReturnMediumRiskForAverageScore() {
            // Given
            CreditScore averageScore = CreditScore.of(650);

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(applicationLowRisk, averageScore);

            // Then
            RiskFactor creditScoreFactor = assessment.getFactors().stream()
                .filter(factor -> "CREDIT_SCORE".equals(factor.getCategory()))
                .findFirst()
                .orElseThrow();

            assertThat(creditScoreFactor.getLevel()).isEqualTo(RiskLevel.MEDIUM);
            assertThat(creditScoreFactor.getScore()).isBetween(45, 75);
        }
    }

    @Nested
    @DisplayName("Payment Capacity Tests")
    class PaymentCapacityTests {

        @Test
        @DisplayName("Should have low risk for high income customer")
        void shouldHaveLowRiskForHighIncome() {
            // Given
            CreditScore goodScore = CreditScore.of(720);

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(applicationLowRisk, goodScore);

            // Then
            RiskFactor paymentCapacityFactor = assessment.getFactors().stream()
                .filter(factor -> "PAYMENT_CAPACITY".equals(factor.getCategory()))
                .findFirst()
                .orElseThrow();

            assertThat(paymentCapacityFactor.getLevel()).isEqualTo(RiskLevel.LOW);
            assertThat(paymentCapacityFactor.getScore()).isGreaterThanOrEqualTo(65);
        }

        @Test
        @DisplayName("Should have higher risk for high debt-to-income ratio")
        void shouldHaveHigherRiskForHighDebtToIncomeRatio() {
            // Given
            CreditScore goodScore = CreditScore.of(720);
            
            // Create customer with lower income for higher ratio
            Customer lowIncomeCustomer = Customer.builder()
                .documentNumber(DocumentNumber.of("22222222222"))
                .firstName("Ana")
                .lastName("López")
                .monthlyIncome(MonthlyIncome.of(BigDecimal.valueOf(1_500_000)))
                .workExperienceMonths(24)
                .build();

            CreditApplication highRatioApplication = CreditApplication.builder()
                .customer(lowIncomeCustomer)
                .vehicle(newVehicle)
                .requestedAmount(CreditAmount.of(BigDecimal.valueOf(50_000_000)))
                .build();

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(highRatioApplication, goodScore);

            // Then
            RiskFactor paymentCapacityFactor = assessment.getFactors().stream()
                .filter(factor -> "PAYMENT_CAPACITY".equals(factor.getCategory()))
                .findFirst()
                .orElseThrow();

            assertThat(paymentCapacityFactor.getLevel()).isIn(RiskLevel.MEDIUM, RiskLevel.HIGH);
        }
    }

    @Nested
    @DisplayName("Vehicle Risk Evaluation Tests")
    class VehicleRiskEvaluationTests {

        @Test
        @DisplayName("Should have low risk for new vehicle")
        void shouldHaveLowRiskForNewVehicle() {
            // Given
            CreditScore goodScore = CreditScore.of(720);

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(applicationLowRisk, goodScore);

            // Then
            RiskFactor vehicleRiskFactor = assessment.getFactors().stream()
                .filter(factor -> "VEHICLE_RISK".equals(factor.getCategory()))
                .findFirst()
                .orElseThrow();

            assertThat(vehicleRiskFactor.getLevel()).isEqualTo(RiskLevel.LOW);
            assertThat(vehicleRiskFactor.getScore()).isGreaterThanOrEqualTo(80);
            assertThat(vehicleRiskFactor.getDetails()).contains("nuevo");
        }

        @Test
        @DisplayName("Should have higher risk for old high-mileage vehicle")
        void shouldHaveHigherRiskForOldHighMileageVehicle() {
            // Given
            CreditScore goodScore = CreditScore.of(720);

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(applicationHighRisk, goodScore);

            // Then
            RiskFactor vehicleRiskFactor = assessment.getFactors().stream()
                .filter(factor -> "VEHICLE_RISK".equals(factor.getCategory()))
                .findFirst()
                .orElseThrow();

            assertThat(vehicleRiskFactor.getLevel()).isIn(RiskLevel.MEDIUM, RiskLevel.HIGH);
            assertThat(vehicleRiskFactor.getDetails()).contains("usado");
        }
    }

    @Nested
    @DisplayName("Interest Rate Calculation Tests")
    class InterestRateCalculationTests {

        @Test
        @DisplayName("Should calculate lower interest rate for low risk")
        void shouldCalculateLowerInterestRateForLowRisk() {
            // Given
            CreditScore excellentScore = CreditScore.of(780);
            RiskAssessment lowRiskAssessment = riskCalculationService.calculateRisk(applicationLowRisk, excellentScore);

            // When
            BigDecimal interestRate = riskCalculationService.calculateRecommendedInterestRate(lowRiskAssessment, excellentScore);

            // Then
            assertThat(interestRate).isNotNull();
            assertThat(interestRate).isLessThan(BigDecimal.valueOf(0.16)); // Menos del 16%
            assertThat(interestRate).isGreaterThanOrEqualTo(BigDecimal.valueOf(0.12)); // Mínimo 12%
        }

        @Test
        @DisplayName("Should calculate higher interest rate for high risk")
        void shouldCalculateHigherInterestRateForHighRisk() {
            // Given
            CreditScore poorScore = CreditScore.of(580);
            RiskAssessment highRiskAssessment = riskCalculationService.calculateRisk(applicationHighRisk, poorScore);

            // When
            BigDecimal interestRate = riskCalculationService.calculateRecommendedInterestRate(highRiskAssessment, poorScore);

            // Then
            assertThat(interestRate).isNotNull();
            assertThat(interestRate).isGreaterThan(BigDecimal.valueOf(0.18)); // Más del 18%
            assertThat(interestRate).isLessThanOrEqualTo(BigDecimal.valueOf(0.25)); // Máximo 25%
        }

        @Test
        @DisplayName("Should respect minimum and maximum rate limits")
        void shouldRespectMinimumAndMaximumRateLimits() {
            // Given - Extreme cases
            CreditScore extremelyHighScore = CreditScore.of(850);
            CreditScore extremelyLowScore = CreditScore.of(400);
            
            RiskAssessment veryLowRisk = riskCalculationService.calculateRisk(applicationLowRisk, extremelyHighScore);
            RiskAssessment veryHighRisk = riskCalculationService.calculateRisk(applicationHighRisk, extremelyLowScore);

            // When
            BigDecimal minRate = riskCalculationService.calculateRecommendedInterestRate(veryLowRisk, extremelyHighScore);
            BigDecimal maxRate = riskCalculationService.calculateRecommendedInterestRate(veryHighRisk, extremelyLowScore);

            // Then
            assertThat(minRate).isGreaterThanOrEqualTo(BigDecimal.valueOf(0.12)); // Mínimo 12%
            assertThat(maxRate).isLessThanOrEqualTo(BigDecimal.valueOf(0.25)); // Máximo 25%
        }
    }

    @Nested
    @DisplayName("Risk Assessment Edge Cases")
    class RiskAssessmentEdgeCasesTests {

        @Test
        @DisplayName("Should handle multiple high risk factors")
        void shouldHandleMultipleHighRiskFactors() {
            // Given
            CreditScore veryPoorScore = CreditScore.of(500);

            // Create worst case scenario
            Customer poorCustomer = Customer.builder()
                .documentNumber(DocumentNumber.of("99999999999"))
                .firstName("Pedro")
                .lastName("Riesgo")
                .monthlyIncome(MonthlyIncome.of(BigDecimal.valueOf(1_000_000))) // Muy bajo
                .workExperienceMonths(6) // Poca experiencia
                .build();

            Vehicle poorVehicle = Vehicle.builder()
                .vin(VehicleVIN.of("1HGBH41JXMN109189"))
                .brand("CHEVROLET")
                .model("SPARK")
                .year(2017) // Muy usado
                .value(BigDecimal.valueOf(25_000_000))
                .kilometers(120_000) // Alto kilometraje
                .build();

            CreditApplication poorApplication = CreditApplication.builder()
                .customer(poorCustomer)
                .vehicle(poorVehicle)
                .requestedAmount(CreditAmount.of(BigDecimal.valueOf(20_000_000)))
                .build();

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(poorApplication, veryPoorScore);

            // Then
            assertThat(assessment).isNotNull();
            assertThat(assessment.isApproved()).isFalse();
            assertThat(assessment.getOverallRiskLevel()).isEqualTo(RiskLevel.HIGH);
            
            // Should have at least 2 high risk factors
            long highRiskFactors = assessment.getFactors().stream()
                .filter(factor -> factor.getLevel() == RiskLevel.HIGH)
                .count();
            assertThat(highRiskFactors).isGreaterThanOrEqualTo(1);
        }

        @Test
        @DisplayName("Should handle empty or null scenarios gracefully")
        void shouldHandleEdgeCasesGracefully() {
            // Given
            CreditScore minimumScore = CreditScore.of(600);

            // Test with minimum viable application
            Customer minCustomer = Customer.builder()
                .documentNumber(DocumentNumber.of("11111111111"))
                .firstName("Mínimo")
                .lastName("Viable")
                .monthlyIncome(MonthlyIncome.of(BigDecimal.valueOf(300_000))) // Mínimo
                .workExperienceMonths(12)
                .build();

            Vehicle minVehicle = Vehicle.builder()
                .vin(VehicleVIN.of("1HGBH41JXMN109190"))
                .brand("TOYOTA")
                .model("YARIS")
                .year(2018) // Límite mínimo
                .value(BigDecimal.valueOf(50_000_000)) // Valor mínimo
                .kilometers(100_000) // Límite máximo
                .build();

            CreditApplication minApplication = CreditApplication.builder()
                .customer(minCustomer)
                .vehicle(minVehicle)
                .requestedAmount(CreditAmount.of(BigDecimal.valueOf(50_000)))
                .build();

            // When
            RiskAssessment assessment = riskCalculationService.calculateRisk(minApplication, minimumScore);

            // Then
            assertThat(assessment).isNotNull();
            assertThat(assessment.getFactors()).isNotEmpty();
            assertThat(assessment.getRiskScore()).isNotNull();
            assertThat(assessment.getOverallRiskLevel()).isNotNull();
        }
    }
}