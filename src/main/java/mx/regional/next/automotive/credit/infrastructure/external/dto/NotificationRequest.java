package mx.regional.next.automotive.credit.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class NotificationRequest {

    @NotBlank(message = "Notification ID is required")
    private String notificationId;

    @NotNull(message = "Notification type is required")
    @Pattern(regexp = "^(EMAIL|SMS|PUSH|IN_APP|WEBHOOK)$", message = "Invalid notification type")
    private String notificationType;

    @NotBlank(message = "Recipient is required")
    private String recipient;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Message content is required")
    private String message;

    // Configuración de prioridad y entrega
    @Pattern(regexp = "^(LOW|NORMAL|HIGH|URGENT)$", message = "Invalid priority")
    @Builder.Default
    private String priority = "NORMAL";

    @Builder.Default
    private Boolean immediate = false;

    private LocalDateTime scheduledTime;

    // Plantilla y personalización
    private String templateId;
    private Map<String, Object> templateVariables;

    // Configuración de reintento
    @Builder.Default
    private Integer maxRetries = 3;

    @Builder.Default
    private Integer retryInterval = 300; // seconds

    // Configuración de expiración
    private LocalDateTime expirationTime;

    // Información del remitente
    private String senderName;
    private String senderEmail;
    private String senderPhone;

    // Configuración específica por canal
    private EmailConfig emailConfig;
    private SmsConfig smsConfig;
    private PushConfig pushConfig;

    // Tracking y analytics
    @Builder.Default
    private Boolean trackOpening = true;

    @Builder.Default
    private Boolean trackClicks = true;

    @Builder.Default
    private Boolean requireDeliveryConfirmation = false;

    // Metadatos
    private String clientId;
    private String applicationId;
    private String campaignId;
    private String category;
    private Map<String, String> metadata;

    @Builder.Default
    private LocalDateTime requestTime = LocalDateTime.now();

    @Data
    @Builder
    @Jacksonized
    public static class EmailConfig {
        @Email
        private String fromEmail;
        private String fromName;
        @Email
        private String replyTo;
        private String[] ccRecipients;
        private String[] bccRecipients;
        private Boolean htmlFormat;
        private String[] attachments;
        private Boolean requestReadReceipt;
    }

    @Data
    @Builder
    @Jacksonized
    public static class SmsConfig {
        private String fromNumber;
        private Boolean flashSms;
        private String validity;
        private Boolean unicode;
        private String deliveryReport;
    }

    @Data
    @Builder
    @Jacksonized
    public static class PushConfig {
        private String title;
        private String icon;
        private String sound;
        private String badge;
        private Map<String, Object> customData;
        private String clickAction;
        private String[] tags;
    }
}