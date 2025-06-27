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
public class CreditBureauResponse {

    private String requestId;
    private String documentNumber;
    private String documentType;
    private String fullName;
    private LocalDateTime consultationDate;
    private String status;
    private String errorMessage;

    // Score crediticio
    private Integer creditScore;
    private String scoreCategory;
    private String scoreModel;
    private LocalDate scoreDate;
    private String riskLevel;

    // Resumen financiero
    private BigDecimal totalDebt;
    private BigDecimal availableCredit;
    private BigDecimal usedCredit;
    private BigDecimal monthlyPayments;
    private Integer activeAccounts;
    private Integer closedAccounts;

    // Historial de pagos
    private Integer daysInDefault;
    private Integer timesInDefault;
    private LocalDate lastDefaultDate;
    private BigDecimal currentDefaultAmount;
    private String paymentBehavior;

    // Consultas recientes
    private Integer consultationsLast6Months;
    private Integer consultationsLast12Months;
    private LocalDate lastConsultationDate;
    private String lastConsultingEntity;

    // Información demográfica
    private String address;
    private String city;
    private String department;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private Integer age;

    // Detalles por producto
    private List<CreditProductDetail> creditProducts;
    private List<CreditInquiry> recentInquiries;
    private List<PublicRecord> publicRecords;
    private List<Reference> commercialReferences;

    // Alertas y observaciones
    private List<String> alerts;
    private List<String> warnings;
    private String generalObservations;

    // Metadatos de respuesta
    private String responseCode;
    private String responseMessage;
    private LocalDateTime expirationDate;
    private String consultationCost;
    private String sourceBureau;

    @Data
    @Builder
    @Jacksonized
    public static class CreditProductDetail {
        private String productType;
        private String entityName;
        private String accountNumber;
        private BigDecimal creditLimit;
        private BigDecimal currentBalance;
        private BigDecimal monthlyPayment;
        private LocalDate openDate;
        private LocalDate lastPaymentDate;
        private String accountStatus;
        private String paymentHistory;
        private Integer daysOverdue;
        private String guaranteeType;
    }

    @Data
    @Builder
    @Jacksonized
    public static class CreditInquiry {
        private LocalDate inquiryDate;
        private String inquiringEntity;
        private String inquiryType;
        private String inquiryPurpose;
        private BigDecimal requestedAmount;
    }

    @Data
    @Builder
    @Jacksonized
    public static class PublicRecord {
        private String recordType;
        private String description;
        private LocalDate recordDate;
        private BigDecimal amount;
        private String status;
        private String court;
        private String caseNumber;
    }

    @Data
    @Builder
    @Jacksonized
    public static class Reference {
        private String referenceType;
        private String entityName;
        private String relationship;
        private LocalDate relationshipStart;
        private String evaluation;
        private String comments;
    }
}