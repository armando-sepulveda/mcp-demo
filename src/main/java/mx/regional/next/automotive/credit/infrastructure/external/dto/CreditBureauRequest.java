package mx.regional.next.automotive.credit.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
public class CreditBureauRequest {

    @NotBlank(message = "Document number is required")
    @Pattern(regexp = "^[0-9]{8,11}$", message = "Invalid document number format")
    private String documentNumber;

    @NotBlank(message = "Document type is required")
    @Pattern(regexp = "^(CC|CE|TI|PA|NIT)$", message = "Invalid document type")
    private String documentType;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String middleName;

    private String secondLastName;

    @NotNull(message = "Request type is required")
    @Pattern(regexp = "^(FULL_REPORT|SCORE_ONLY|BASIC_CHECK)$", message = "Invalid request type")
    private String requestType;

    private String purpose;

    @Builder.Default
    private LocalDateTime requestDate = LocalDateTime.now();

    private String requestId;

    private String clientReference;

    // Datos adicionales para consulta específica
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    private String department;

    // Configuración de la consulta
    @Builder.Default
    private Boolean includePositiveHistory = true;

    @Builder.Default
    private Boolean includeNegativeHistory = true;

    @Builder.Default
    private Boolean includeCommercialReferences = true;

    @Builder.Default
    private Boolean includeFinancialHistory = true;

    @Builder.Default
    private Integer historicalMonths = 24;

    // Metadatos
    private String requestingEntity;
    private String requestingUser;
    private String ipAddress;
}