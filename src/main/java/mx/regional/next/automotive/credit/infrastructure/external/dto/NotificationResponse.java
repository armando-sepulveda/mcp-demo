package mx.regional.next.automotive.credit.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class NotificationResponse {

    private String notificationId;
    private String messageId;
    private String status;
    private String statusDescription;
    private String errorMessage;
    private String errorCode;

    // Timestamps
    private LocalDateTime requestTime;
    private LocalDateTime processedTime;
    private LocalDateTime sentTime;
    private LocalDateTime deliveredTime;
    private LocalDateTime readTime;

    // Información de entrega
    private String deliveryStatus;
    private String deliveryStatusDescription;
    private String recipientStatus;
    private String carrierResponse;

    // Información del canal utilizado
    private String channelUsed;
    private String provider;
    private String providerId;

    // Métricas de entrega
    private DeliveryMetrics deliveryMetrics;

    // Tracking de interacciones
    private TrackingInfo trackingInfo;

    // Información de costo
    private String cost;
    private String currency;
    private String billingUnit;

    // Reintentos
    private Integer attemptNumber;
    private Integer maxAttempts;
    private List<DeliveryAttempt> deliveryAttempts;
    private LocalDateTime nextRetryTime;

    // Información de expiración
    private LocalDateTime expirationTime;
    private Boolean expired;

    // Información del destinatario
    private String recipientId;
    private String recipientChannel;
    private String recipientDevice;
    private String recipientLocation;

    // Metadatos adicionales
    private Map<String, String> additionalInfo;
    private String traceId;
    private String correlationId;

    @Data
    @Builder
    @Jacksonized
    public static class DeliveryMetrics {
        private Long processingTimeMs;
        private Long deliveryTimeMs;
        private Long totalTimeMs;
        private Integer queuePosition;
        private String deliveryRoute;
        private String optimizationApplied;
    }

    @Data
    @Builder
    @Jacksonized
    public static class TrackingInfo {
        private Boolean opened;
        private LocalDateTime openTime;
        private Integer openCount;
        private Boolean clicked;
        private LocalDateTime clickTime;
        private Integer clickCount;
        private List<String> linksClicked;
        private String userAgent;
        private String ipAddress;
        private String deviceInfo;
    }

    @Data
    @Builder
    @Jacksonized
    public static class DeliveryAttempt {
        private Integer attemptNumber;
        private LocalDateTime attemptTime;
        private String status;
        private String errorMessage;
        private String provider;
        private Long processingTimeMs;
        private String failureReason;
        private String carrierResponse;
    }
}