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
public class EmploymentVerificationResponse {

    private String requestId;
    private String employeeDocumentNumber;
    private LocalDateTime verificationDate;
    private String status;
    private String errorMessage;

    // Resultado de verificación de empleo
    private Boolean employmentVerified;
    private String employmentStatus;
    private String verificationMethod;
    private String verificationSource;

    // Información laboral verificada
    private String companyName;
    private String companyNit;
    private String jobTitle;
    private String department;
    private LocalDate hireDate;
    private String contractType;
    private String workSchedule;
    private String employmentDuration;

    // Verificación de ingresos
    private Boolean incomeVerified;
    private BigDecimal verifiedBaseSalary;
    private BigDecimal verifiedVariableIncome;
    private BigDecimal verifiedTotalIncome;
    private BigDecimal averageMonthlyIncome;
    private String incomeStability;

    // Historial de ingresos
    private List<IncomeRecord> incomeHistory;
    private Integer verificationPeriodMonths;
    private BigDecimal incomeGrowthRate;

    // Información de la empresa
    private CompanyInformation companyInfo;
    
    // Contactos verificados
    private List<ContactVerification> contactsVerified;

    // Análisis de estabilidad
    private String stabilityRating;
    private String jobSecurityAssessment;
    private List<String> riskFactors;
    private List<String> positiveFactors;

    // Documentos verificados
    private List<DocumentVerification> documentsVerified;

    // Observaciones y notas
    private List<String> observations;
    private List<String> warnings;
    private List<String> recommendations;

    // Confiabilidad de la información
    private String confidenceLevel;
    private String dataQuality;
    private String reliabilityScore;

    // Metadatos
    private String responseCode;
    private String responseMessage;
    private LocalDateTime expirationDate;
    private String verificationCost;
    private String provider;

    @Data
    @Builder
    @Jacksonized
    public static class IncomeRecord {
        private String period;
        private LocalDate payDate;
        private BigDecimal baseSalary;
        private BigDecimal variableIncome;
        private BigDecimal bonuses;
        private BigDecimal overtime;
        private BigDecimal totalGrossIncome;
        private BigDecimal deductions;
        private BigDecimal netIncome;
    }

    @Data
    @Builder
    @Jacksonized
    public static class CompanyInformation {
        private String companyName;
        private String companyNit;
        private String sector;
        private String industry;
        private String companySize;
        private Integer numberOfEmployees;
        private String creditRating;
        private String financialStability;
        private String businessRegistration;
        private Boolean operatingLicense;
        private LocalDate foundedDate;
        private String legalStructure;
    }

    @Data
    @Builder
    @Jacksonized
    public static class ContactVerification {
        private String contactType;
        private String contactName;
        private String contactTitle;
        private String contactPhone;
        private String contactEmail;
        private Boolean contactReached;
        private LocalDateTime contactDate;
        private String verificationResult;
        private String comments;
    }

    @Data
    @Builder
    @Jacksonized
    public static class DocumentVerification {
        private String documentType;
        private String documentSource;
        private Boolean documentVerified;
        private String verificationMethod;
        private LocalDate documentDate;
        private String documentValidity;
        private String observations;
    }
}