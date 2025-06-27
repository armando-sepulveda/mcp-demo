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
public class CreditScoreResponse {

    private String requestId;
    private String documentNumber;
    private String documentType;
    private LocalDateTime consultationDate;
    private String status;
    private String errorMessage;

    // Score principal
    private Integer creditScore;
    private String scoreCategory;
    private String riskLevel;
    private String scoreModel;
    private LocalDate scoreCalculationDate;

    // Componentes del score
    private ScoreComponent paymentHistory;
    private ScoreComponent creditUtilization;
    private ScoreComponent creditHistory;
    private ScoreComponent creditMix;
    private ScoreComponent newCredit;

    // Factores que afectan el score
    private List<String> positiveFactors;
    private List<String> negativeFactors;
    private List<String> improvementRecommendations;

    // Histórico del score
    private List<ScoreHistory> scoreHistory;

    // Comparación con población
    private String percentileRanking;
    private BigDecimal averageScoreForAge;
    private String comparisonWithPeers;

    // Probabilidad de incumplimiento
    private BigDecimal defaultProbability12Months;
    private BigDecimal defaultProbability24Months;
    private String riskProfile;

    // Alertas específicas
    private List<String> riskAlerts;
    private List<String> fraudAlerts;
    private Boolean identityVerified;

    // Metadatos
    private String responseCode;
    private String responseMessage;
    private LocalDateTime expirationDate;
    private String consultationCost;

    @Data
    @Builder
    @Jacksonized
    public static class ScoreComponent {
        private String componentName;
        private Integer componentScore;
        private Integer componentWeight;
        private String componentGrade;
        private String description;
        private String impact;
    }

    @Data
    @Builder
    @Jacksonized
    public static class ScoreHistory {
        private LocalDate date;
        private Integer score;
        private String scoreCategory;
        private String changeReason;
        private Integer scoreChange;
    }
}