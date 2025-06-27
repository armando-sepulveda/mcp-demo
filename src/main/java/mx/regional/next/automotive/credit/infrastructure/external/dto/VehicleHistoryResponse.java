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
public class VehicleHistoryResponse {

    private String requestId;
    private String vin;
    private LocalDateTime consultationDate;
    private String status;
    private String errorMessage;

    // Información básica del vehículo
    private String brand;
    private String model;
    private String version;
    private Integer year;
    private String engineNumber;
    private String chassisNumber;

    // Historial de propiedad
    private Integer numberOfOwners;
    private List<OwnerHistory> ownerHistory;
    private String currentOwnerType;
    private LocalDate lastOwnershipChange;

    // Historial de accidentes
    private Boolean hasAccidentHistory;
    private Integer numberOfAccidents;
    private List<AccidentRecord> accidentHistory;
    private String accidentSeverity;

    // Historial de mantenimiento
    private Boolean hasServiceHistory;
    private List<ServiceRecord> serviceHistory;
    private LocalDate lastServiceDate;
    private Integer totalServiceRecords;

    // Registros legales
    private Boolean hasLegalIssues;
    private List<LegalRecord> legalRecords;
    private Boolean isStolen;
    private Boolean hasLiens;

    // Uso del vehículo
    private String vehicleUse;
    private String operationType;
    private Boolean wasCommercialUse;
    private Boolean wasRentalCar;

    // Inspecciones técnicas
    private List<InspectionRecord> inspectionHistory;
    private LocalDate lastInspectionDate;
    private String lastInspectionResult;

    // Recalls y campañas
    private List<RecallRecord> recalls;
    private Boolean hasOpenRecalls;
    private Integer completedRecalls;

    // Estimación de kilometraje
    private Integer estimatedMileage;
    private String mileageConsistency;
    private List<MileageRecord> mileageHistory;

    // Calificación general
    private String overallRating;
    private String reliabilityScore;
    private List<String> riskFactors;
    private List<String> positiveFactors;

    // Metadatos
    private String responseCode;
    private String responseMessage;
    private LocalDateTime expirationDate;
    private String sourceDatabases;
    private String reportCost;

    @Data
    @Builder
    @Jacksonized
    public static class OwnerHistory {
        private Integer ownerNumber;
        private String ownerType;
        private LocalDate ownershipStart;
        private LocalDate ownershipEnd;
        private String ownershipDuration;
        private String acquisitionType;
        private String location;
    }

    @Data
    @Builder
    @Jacksonized
    public static class AccidentRecord {
        private LocalDate accidentDate;
        private String severity;
        private String accidentType;
        private BigDecimal estimatedDamage;
        private String damageAreas;
        private String repairStatus;
        private String insuranceClaim;
        private String location;
    }

    @Data
    @Builder
    @Jacksonized
    public static class ServiceRecord {
        private LocalDate serviceDate;
        private String serviceType;
        private String serviceProvider;
        private Integer mileageAtService;
        private String workPerformed;
        private BigDecimal serviceCost;
        private String partsReplaced;
    }

    @Data
    @Builder
    @Jacksonized
    public static class LegalRecord {
        private String recordType;
        private LocalDate recordDate;
        private String description;
        private String status;
        private String authority;
        private String caseNumber;
        private BigDecimal amount;
    }

    @Data
    @Builder
    @Jacksonized
    public static class InspectionRecord {
        private LocalDate inspectionDate;
        private String inspectionType;
        private String result;
        private String inspector;
        private Integer mileageAtInspection;
        private List<String> defectsFound;
        private String certificateNumber;
        private LocalDate expirationDate;
    }

    @Data
    @Builder
    @Jacksonized
    public static class RecallRecord {
        private String recallNumber;
        private LocalDate recallDate;
        private String recallDescription;
        private String severity;
        private String component;
        private String remedy;
        private String completionStatus;
        private LocalDate completionDate;
    }

    @Data
    @Builder
    @Jacksonized
    public static class MileageRecord {
        private LocalDate recordDate;
        private Integer mileage;
        private String source;
        private String recordType;
        private Boolean anomalyDetected;
    }
}