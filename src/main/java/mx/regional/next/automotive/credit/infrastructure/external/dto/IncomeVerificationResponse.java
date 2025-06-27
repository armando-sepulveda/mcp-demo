package mx.regional.next.automotive.credit.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Jacksonized
public class IncomeVerificationResponse {

    private String requestId;
    private String employeeDocumentNumber;
    private LocalDateTime verificationDate;
    private String status;
    private String errorMessage;

    // Verificación de ingresos principales
    private Boolean incomeVerified;
    private BigDecimal declaredIncome;
    private BigDecimal verifiedIncome;
    private BigDecimal incomeVariance;
    private BigDecimal incomeVariancePercentage;

    // Componentes de ingresos
    private IncomeBreakdown incomeBreakdown;

    // Análisis de estabilidad de ingresos
    private String incomeStability;
    private BigDecimal incomeVolatility;
    private String incomePattern;
    private String seasonalityImpact;

    // Historial de ingresos
    private List<MonthlyIncome> monthlyIncomeHistory;
    private Integer verificationPeriodMonths;
    private BigDecimal incomeGrowthRate;
    private String incomeGrowthTrend;

    // Análisis comparativo
    private BigDecimal industryAverageIncome;
    private String incomePercentile;
    private String incomeCompetitiveness;

    // Fuentes de verificación
    private List<VerificationSource> verificationSources;
    private String primaryVerificationMethod;
    private String secondaryVerificationMethod;

    // Análisis de riesgo
    private String incomeRiskRating;
    private List<String> incomeRiskFactors;
    private List<String> incomeStabilityFactors;

    // Proyección de ingresos
    private BigDecimal projectedIncome12Months;
    private BigDecimal projectedIncome24Months;
    private String projectionConfidence;

    // Observaciones
    private List<String> observations;
    private List<String> warnings;
    private List<String> recommendations;

    // Confiabilidad
    private String confidenceLevel;
    private String verificationQuality;
    private String reliabilityScore;

    // Metadatos
    private String responseCode;
    private String responseMessage;
    private LocalDateTime expirationDate;
    private String verificationCost;

    @Data
    @Builder
    @Jacksonized
    public static class IncomeBreakdown {
        private BigDecimal baseSalary;
        private BigDecimal variableIncome;
        private BigDecimal bonuses;
        private BigDecimal commissions;
        private BigDecimal overtime;
        private BigDecimal benefits;
        private BigDecimal otherIncome;
        private BigDecimal totalGrossIncome;
        private BigDecimal totalDeductions;
        private BigDecimal netIncome;
        private BigDecimal disposableIncome;
    }

    @Data
    @Builder
    @Jacksonized
    public static class MonthlyIncome {
        private String period;
        private LocalDate payPeriodEnd;
        private BigDecimal grossIncome;
        private BigDecimal netIncome;
        private BigDecimal variableComponent;
        private BigDecimal fixedComponent;
        private String incomeType;
        private Boolean anomalyDetected;
        private String comments;
    }

    @Data
    @Builder
    @Jacksonized
    public static class VerificationSource {
        private String sourceType;
        private String sourceName;
        private String sourceReliability;
        private LocalDate verificationDate;
        private String dataQuality;
        private BigDecimal incomeReported;
        private String verificationMethod;
        private String sourceComments;
    }
}