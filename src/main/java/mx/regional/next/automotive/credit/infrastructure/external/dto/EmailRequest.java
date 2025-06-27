package mx.regional.next.automotive.credit.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class EmailRequest {

    @NotBlank(message = "Email ID is required")
    private String emailId;

    @Email(message = "Invalid sender email")
    @NotBlank(message = "From email is required")
    private String fromEmail;

    private String fromName;

    @Email(message = "Invalid recipient email")
    @NotBlank(message = "To email is required")
    private String toEmail;

    private String toName;

    private List<@Email String> ccEmails;
    private List<@Email String> bccEmails;

    @Email(message = "Invalid reply-to email")
    private String replyToEmail;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String textContent;
    private String htmlContent;

    // Plantilla
    private String templateId;
    private Map<String, Object> templateData;

    // Archivos adjuntos
    private List<EmailAttachment> attachments;

    // Configuración de entrega
    @Builder.Default
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT

    private LocalDateTime scheduleTime;

    @Builder.Default
    private Boolean requestReadReceipt = false;

    @Builder.Default
    private Boolean trackOpening = true;

    @Builder.Default
    private Boolean trackClicks = true;

    // Headers personalizados
    private Map<String, String> customHeaders;

    // Categorización
    private String category;
    private String campaign;
    private List<String> tags;

    // Metadatos
    private String clientId;
    private String applicationId;
    private Map<String, String> metadata;

    @Builder.Default
    private LocalDateTime requestTime = LocalDateTime.now();

    @Data
    @Builder
    @Jacksonized
    public static class EmailAttachment {
        private String filename;
        private String contentType;
        private String content; // Base64 encoded
        private String contentId; // For inline attachments
        private Boolean inline;
        private Integer size;
    }
}