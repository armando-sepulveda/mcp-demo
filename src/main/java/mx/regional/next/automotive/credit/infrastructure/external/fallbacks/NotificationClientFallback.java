package mx.regional.next.automotive.credit.infrastructure.external.fallbacks;

import mx.regional.next.automotive.credit.infrastructure.external.clients.NotificationClient;
import mx.regional.next.automotive.credit.infrastructure.external.dto.NotificationRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.NotificationResponse;
import mx.regional.next.automotive.credit.infrastructure.external.dto.EmailRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.SmsRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class NotificationClientFallback implements NotificationClient {

    private static final Logger log = LoggerFactory.getLogger(NotificationClientFallback.class);

    @Override
    public NotificationResponse sendEmail(EmailRequest request) {
        log.warn("Email service unavailable. Email to {} could not be sent: {}", 
                request.getToEmail(), request.getSubject());
        
        // Log critical information for manual follow-up
        log.error("MANUAL EMAIL REQUIRED - To: {}, Subject: {}, Content: {}", 
                request.getToEmail(), request.getSubject(), 
                request.getTextContent() != null ? request.getTextContent().substring(0, Math.min(100, request.getTextContent().length())) : "HTML only");
        
        return NotificationResponse.builder()
                .notificationId(request.getEmailId())
                .messageId("FALLBACK_" + UUID.randomUUID().toString())
                .status("FAILED")
                .statusDescription("Email service unavailable")
                .errorMessage("Email notification service is temporarily unavailable. Email queued for manual processing.")
                .errorCode("503")
                .requestTime(request.getRequestTime())
                .processedTime(LocalDateTime.now())
                .sentTime(null)
                .deliveredTime(null)
                .readTime(null)
                .deliveryStatus("FAILED")
                .deliveryStatusDescription("Service unavailable")
                .recipientStatus("UNKNOWN")
                .carrierResponse("Email service offline")
                .channelUsed("EMAIL")
                .provider("FALLBACK")
                .providerId("EMAIL_FALLBACK")
                .deliveryMetrics(NotificationResponse.DeliveryMetrics.builder()
                        .processingTimeMs(100L)
                        .deliveryTimeMs(null)
                        .totalTimeMs(100L)
                        .queuePosition(null)
                        .deliveryRoute("FALLBACK")
                        .optimizationApplied("NONE")
                        .build())
                .trackingInfo(null)
                .cost("0.00")
                .currency("COP")
                .billingUnit("EMAIL")
                .attemptNumber(1)
                .maxAttempts(3)
                .deliveryAttempts(List.of(
                        NotificationResponse.DeliveryAttempt.builder()
                                .attemptNumber(1)
                                .attemptTime(LocalDateTime.now())
                                .status("FAILED")
                                .errorMessage("Service unavailable")
                                .provider("EMAIL_SERVICE")
                                .processingTimeMs(100L)
                                .failureReason("External service unavailable")
                                .carrierResponse("503 Service Unavailable")
                                .build()
                ))
                .nextRetryTime(LocalDateTime.now().plusMinutes(30))
                .expirationTime(request.getScheduleTime() != null ? request.getScheduleTime().plusDays(1) : LocalDateTime.now().plusDays(1))
                .expired(false)
                .recipientId(request.getToEmail())
                .recipientChannel("EMAIL")
                .recipientDevice("UNKNOWN")
                .recipientLocation("UNKNOWN")
                .additionalInfo(java.util.Map.of(
                        "fallback_reason", "Email service unavailable",
                        "manual_processing", "true",
                        "priority", request.getPriority() != null ? request.getPriority() : "NORMAL"
                ))
                .traceId("FALLBACK_EMAIL_" + System.currentTimeMillis())
                .correlationId(request.getEmailId())
                .build();
    }

    @Override
    public NotificationResponse sendSms(SmsRequest request) {
        log.warn("SMS service unavailable. SMS to {} could not be sent: {}", 
                request.getToPhoneNumber(), request.getMessage().substring(0, Math.min(50, request.getMessage().length())));
        
        // Log critical information for manual follow-up
        log.error("MANUAL SMS REQUIRED - To: {}, Message: {}", 
                request.getToPhoneNumber(), request.getMessage());
        
        return NotificationResponse.builder()
                .notificationId(request.getSmsId())
                .messageId("FALLBACK_" + UUID.randomUUID().toString())
                .status("FAILED")
                .statusDescription("SMS service unavailable")
                .errorMessage("SMS notification service is temporarily unavailable. SMS queued for manual processing.")
                .errorCode("503")
                .requestTime(request.getRequestTime())
                .processedTime(LocalDateTime.now())
                .sentTime(null)
                .deliveredTime(null)
                .readTime(null)
                .deliveryStatus("FAILED")
                .deliveryStatusDescription("Service unavailable")
                .recipientStatus("UNKNOWN")
                .carrierResponse("SMS service offline")
                .channelUsed("SMS")
                .provider("FALLBACK")
                .providerId("SMS_FALLBACK")
                .deliveryMetrics(NotificationResponse.DeliveryMetrics.builder()
                        .processingTimeMs(50L)
                        .deliveryTimeMs(null)
                        .totalTimeMs(50L)
                        .queuePosition(null)
                        .deliveryRoute("FALLBACK")
                        .optimizationApplied("NONE")
                        .build())
                .trackingInfo(null)
                .cost("0.00")
                .currency("COP")
                .billingUnit("SMS")
                .attemptNumber(1)
                .maxAttempts(3)
                .deliveryAttempts(List.of(
                        NotificationResponse.DeliveryAttempt.builder()
                                .attemptNumber(1)
                                .attemptTime(LocalDateTime.now())
                                .status("FAILED")
                                .errorMessage("Service unavailable")
                                .provider("SMS_SERVICE")
                                .processingTimeMs(50L)
                                .failureReason("External service unavailable")
                                .carrierResponse("503 Service Unavailable")
                                .build()
                ))
                .nextRetryTime(LocalDateTime.now().plusMinutes(15))
                .expirationTime(LocalDateTime.now().plusHours(request.getValidityPeriod() / 60))
                .expired(false)
                .recipientId(request.getToPhoneNumber())
                .recipientChannel("SMS")
                .recipientDevice("MOBILE")
                .recipientLocation("UNKNOWN")
                .additionalInfo(java.util.Map.of(
                        "fallback_reason", "SMS service unavailable",
                        "manual_processing", "true",
                        "priority", request.getPriority()
                ))
                .traceId("FALLBACK_SMS_" + System.currentTimeMillis())
                .correlationId(request.getSmsId())
                .build();
    }

    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {
        log.warn("Notification service unavailable. Notification to {} could not be sent: {}", 
                request.getRecipient(), request.getSubject());
        
        // Log critical information for manual follow-up
        log.error("MANUAL NOTIFICATION REQUIRED - Type: {}, To: {}, Subject: {}, Message: {}", 
                request.getNotificationType(), request.getRecipient(), request.getSubject(),
                request.getMessage().substring(0, Math.min(100, request.getMessage().length())));
        
        return NotificationResponse.builder()
                .notificationId(request.getNotificationId())
                .messageId("FALLBACK_" + UUID.randomUUID().toString())
                .status("FAILED")
                .statusDescription("Notification service unavailable")
                .errorMessage("Notification service is temporarily unavailable. Notification queued for manual processing.")
                .errorCode("503")
                .requestTime(request.getRequestTime())
                .processedTime(LocalDateTime.now())
                .sentTime(null)
                .deliveredTime(null)
                .readTime(null)
                .deliveryStatus("FAILED")
                .deliveryStatusDescription("Service unavailable")
                .recipientStatus("UNKNOWN")
                .carrierResponse("Notification service offline")
                .channelUsed(request.getNotificationType())
                .provider("FALLBACK")
                .providerId("NOTIFICATION_FALLBACK")
                .deliveryMetrics(NotificationResponse.DeliveryMetrics.builder()
                        .processingTimeMs(75L)
                        .deliveryTimeMs(null)
                        .totalTimeMs(75L)
                        .queuePosition(null)
                        .deliveryRoute("FALLBACK")
                        .optimizationApplied("NONE")
                        .build())
                .trackingInfo(null)
                .cost("0.00")
                .currency("COP")
                .billingUnit("NOTIFICATION")
                .attemptNumber(1)
                .maxAttempts(request.getMaxRetries())
                .deliveryAttempts(List.of(
                        NotificationResponse.DeliveryAttempt.builder()
                                .attemptNumber(1)
                                .attemptTime(LocalDateTime.now())
                                .status("FAILED")
                                .errorMessage("Service unavailable")
                                .provider("NOTIFICATION_SERVICE")
                                .processingTimeMs(75L)
                                .failureReason("External service unavailable")
                                .carrierResponse("503 Service Unavailable")
                                .build()
                ))
                .nextRetryTime(LocalDateTime.now().plusSeconds(request.getRetryInterval()))
                .expirationTime(request.getExpirationTime() != null ? request.getExpirationTime() : LocalDateTime.now().plusDays(1))
                .expired(false)
                .recipientId(request.getRecipient())
                .recipientChannel(request.getNotificationType())
                .recipientDevice("UNKNOWN")
                .recipientLocation("UNKNOWN")
                .additionalInfo(java.util.Map.of(
                        "fallback_reason", "Notification service unavailable",
                        "manual_processing", "true",
                        "priority", request.getPriority(),
                        "category", request.getCategory() != null ? request.getCategory() : "UNKNOWN"
                ))
                .traceId("FALLBACK_NOTIFICATION_" + System.currentTimeMillis())
                .correlationId(request.getNotificationId())
                .build();
    }

    @Override
    public NotificationResponse[] sendBatchNotifications(NotificationRequest[] requests) {
        log.warn("Notification service unavailable. Returning fallback batch response for {} notifications", 
                requests.length);
        
        NotificationResponse[] fallbackResponses = new NotificationResponse[requests.length];
        
        for (int i = 0; i < requests.length; i++) {
            fallbackResponses[i] = sendNotification(requests[i]);
        }
        
        return fallbackResponses;
    }

    @Override
    public NotificationResponse getNotificationStatus(String notificationId) {
        log.warn("Notification service unavailable. Cannot retrieve status for notification: {}", notificationId);
        
        return NotificationResponse.builder()
                .notificationId(notificationId)
                .messageId("UNKNOWN")
                .status("UNKNOWN")
                .statusDescription("Status service unavailable")
                .errorMessage("Notification status service is temporarily unavailable.")
                .errorCode("503")
                .requestTime(null)
                .processedTime(LocalDateTime.now())
                .sentTime(null)
                .deliveredTime(null)
                .readTime(null)
                .deliveryStatus("UNKNOWN")
                .deliveryStatusDescription("Service unavailable")
                .recipientStatus("UNKNOWN")
                .carrierResponse("Status service offline")
                .channelUsed("UNKNOWN")
                .provider("FALLBACK")
                .providerId("STATUS_FALLBACK")
                .deliveryMetrics(null)
                .trackingInfo(null)
                .cost("0.00")
                .currency("COP")
                .billingUnit("QUERY")
                .attemptNumber(null)
                .maxAttempts(null)
                .deliveryAttempts(List.of())
                .nextRetryTime(null)
                .expirationTime(null)
                .expired(null)
                .recipientId("UNKNOWN")
                .recipientChannel("UNKNOWN")
                .recipientDevice("UNKNOWN")
                .recipientLocation("UNKNOWN")
                .additionalInfo(java.util.Map.of(
                        "fallback_reason", "Status service unavailable",
                        "query_time", LocalDateTime.now().toString()
                ))
                .traceId("FALLBACK_STATUS_" + System.currentTimeMillis())
                .correlationId(notificationId)
                .build();
    }

    @Override
    public String healthCheck() {
        log.debug("Notification service health check - fallback response");
        return "SERVICE_UNAVAILABLE - Notification service is currently not responding";
    }
}