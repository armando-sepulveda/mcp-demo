package mx.regional.next.automotive.credit.infrastructure.external.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(NotificationErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Notification API error - Method: {}, Status: {}, Reason: {}", 
                methodKey, response.status(), response.reason());

        return switch (response.status()) {
            case 400 -> new NotificationBadRequestException("Invalid notification parameters: " + response.reason());
            case 401 -> new NotificationUnauthorizedException("Authentication failed: " + response.reason());
            case 403 -> new NotificationForbiddenException("Access denied: " + response.reason());
            case 404 -> new NotificationNotFoundException("Notification not found: " + response.reason());
            case 422 -> new NotificationValidationException("Notification data validation failed: " + response.reason());
            case 429 -> new NotificationRateLimitException("Rate limit exceeded: " + response.reason());
            case 500 -> new NotificationServerException("Notification service internal error: " + response.reason());
            case 502, 503, 504 -> new NotificationServiceUnavailableException("Notification service unavailable: " + response.reason());
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }

    public static class NotificationException extends Exception {
        public NotificationException(String message) {
            super(message);
        }
    }

    public static class NotificationBadRequestException extends NotificationException {
        public NotificationBadRequestException(String message) {
            super(message);
        }
    }

    public static class NotificationUnauthorizedException extends NotificationException {
        public NotificationUnauthorizedException(String message) {
            super(message);
        }
    }

    public static class NotificationForbiddenException extends NotificationException {
        public NotificationForbiddenException(String message) {
            super(message);
        }
    }

    public static class NotificationNotFoundException extends NotificationException {
        public NotificationNotFoundException(String message) {
            super(message);
        }
    }

    public static class NotificationValidationException extends NotificationException {
        public NotificationValidationException(String message) {
            super(message);
        }
    }

    public static class NotificationRateLimitException extends NotificationException {
        public NotificationRateLimitException(String message) {
            super(message);
        }
    }

    public static class NotificationServerException extends NotificationException {
        public NotificationServerException(String message) {
            super(message);
        }
    }

    public static class NotificationServiceUnavailableException extends NotificationException {
        public NotificationServiceUnavailableException(String message) {
            super(message);
        }
    }
}