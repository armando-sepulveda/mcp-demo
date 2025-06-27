package mx.regional.next.automotive.credit.infrastructure.external.clients;

import mx.regional.next.automotive.credit.infrastructure.external.dto.NotificationRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.NotificationResponse;
import mx.regional.next.automotive.credit.infrastructure.external.dto.EmailRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.SmsRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "notification-client",
    url = "${external.services.notification.url}",
    configuration = NotificationClientConfig.class,
    fallback = NotificationClientFallback.class
)
public interface NotificationClient {

    @PostMapping("/api/v1/send-email")
    NotificationResponse sendEmail(@RequestBody EmailRequest request);

    @PostMapping("/api/v1/send-sms")
    NotificationResponse sendSms(@RequestBody SmsRequest request);

    @PostMapping("/api/v1/send-notification")
    NotificationResponse sendNotification(@RequestBody NotificationRequest request);

    @PostMapping("/api/v1/send-batch-notifications")
    NotificationResponse[] sendBatchNotifications(@RequestBody NotificationRequest[] requests);

    @GetMapping("/api/v1/notification-status/{notificationId}")
    NotificationResponse getNotificationStatus(@PathVariable("notificationId") String notificationId);

    @GetMapping("/api/v1/health")
    String healthCheck();
}