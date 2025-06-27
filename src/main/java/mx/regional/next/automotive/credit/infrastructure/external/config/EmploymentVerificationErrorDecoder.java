package mx.regional.next.automotive.credit.infrastructure.external.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmploymentVerificationErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(EmploymentVerificationErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Employment Verification API error - Method: {}, Status: {}, Reason: {}", 
                methodKey, response.status(), response.reason());

        return switch (response.status()) {
            case 400 -> new EmploymentVerificationBadRequestException("Invalid employment verification parameters: " + response.reason());
            case 401 -> new EmploymentVerificationUnauthorizedException("Authentication failed: " + response.reason());
            case 403 -> new EmploymentVerificationForbiddenException("Access denied: " + response.reason());
            case 404 -> new EmploymentVerificationNotFoundException("Employment record not found: " + response.reason());
            case 422 -> new EmploymentVerificationValidationException("Employment data validation failed: " + response.reason());
            case 429 -> new EmploymentVerificationRateLimitException("Rate limit exceeded: " + response.reason());
            case 500 -> new EmploymentVerificationServerException("Employment Verification service internal error: " + response.reason());
            case 502, 503, 504 -> new EmploymentVerificationServiceUnavailableException("Employment Verification service unavailable: " + response.reason());
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }

    public static class EmploymentVerificationException extends Exception {
        public EmploymentVerificationException(String message) {
            super(message);
        }
    }

    public static class EmploymentVerificationBadRequestException extends EmploymentVerificationException {
        public EmploymentVerificationBadRequestException(String message) {
            super(message);
        }
    }

    public static class EmploymentVerificationUnauthorizedException extends EmploymentVerificationException {
        public EmploymentVerificationUnauthorizedException(String message) {
            super(message);
        }
    }

    public static class EmploymentVerificationForbiddenException extends EmploymentVerificationException {
        public EmploymentVerificationForbiddenException(String message) {
            super(message);
        }
    }

    public static class EmploymentVerificationNotFoundException extends EmploymentVerificationException {
        public EmploymentVerificationNotFoundException(String message) {
            super(message);
        }
    }

    public static class EmploymentVerificationValidationException extends EmploymentVerificationException {
        public EmploymentVerificationValidationException(String message) {
            super(message);
        }
    }

    public static class EmploymentVerificationRateLimitException extends EmploymentVerificationException {
        public EmploymentVerificationRateLimitException(String message) {
            super(message);
        }
    }

    public static class EmploymentVerificationServerException extends EmploymentVerificationException {
        public EmploymentVerificationServerException(String message) {
            super(message);
        }
    }

    public static class EmploymentVerificationServiceUnavailableException extends EmploymentVerificationException {
        public EmploymentVerificationServiceUnavailableException(String message) {
            super(message);
        }
    }
}