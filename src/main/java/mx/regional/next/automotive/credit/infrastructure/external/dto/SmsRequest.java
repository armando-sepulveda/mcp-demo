package mx.regional.next.automotive.credit.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class SmsRequest {

    @NotBlank(message = "SMS ID is required")
    private String smsId;

    @NotBlank(message = "Recipient phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String toPhoneNumber;

    private String fromPhoneNumber;
    private String fromAlias;

    @NotBlank(message = "Message content is required")
    @Size(max = 1600, message = "Message content cannot exceed 1600 characters")
    private String message;

    // Configuración del mensaje
    @Builder.Default
    private String messageType = "TEXT"; // TEXT, UNICODE, BINARY

    @Builder.Default
    private Boolean flashSms = false;

    @Builder.Default
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT

    // Configuración de entrega
    private LocalDateTime scheduleTime;

    @Builder.Default
    private Integer validityPeriod = 2880; // minutes (48 hours default)

    @Builder.Default
    private Boolean requestDeliveryReport = true;

    // Plantilla
    private String templateId;
    private Map<String, Object> templateData;

    // Configuración de reintento
    @Builder.Default
    private Integer maxRetries = 3;

    @Builder.Default
    private Integer retryInterval = 300; // seconds

    // Categorización
    private String category;
    private String campaign;

    // Metadatos
    private String clientId;
    private String applicationId;
    private Map<String, String> metadata;

    @Builder.Default
    private LocalDateTime requestTime = LocalDateTime.now();

    // Configuración específica del operador
    private String preferredCarrier;
    private String route;
    private String encoding;

    // Información del destinatario
    private String recipientName;
    private String recipientTimezone;
    private String recipientLanguage;

    // Configuración de contenido
    @Builder.Default
    private Boolean allowUnicode = true;

    @Builder.Default
    private Boolean concatenate = true; // For long messages

    private String messageClass;
}