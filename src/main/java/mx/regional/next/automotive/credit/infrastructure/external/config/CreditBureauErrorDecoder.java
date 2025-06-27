package mx.regional.next.automotive.credit.infrastructure.external.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreditBureauErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(CreditBureauErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Credit Bureau API error - Method: {}, Status: {}, Reason: {}", 
                methodKey, response.status(), response.reason());

        return switch (response.status()) {
            case 400 -> new CreditBureauBadRequestException("Invalid request parameters: " + response.reason());
            case 401 -> new CreditBureauUnauthorizedException("Authentication failed: " + response.reason());
            case 403 -> new CreditBureauForbiddenException("Access denied: " + response.reason());
            case 404 -> new CreditBureauNotFoundException("Credit record not found: " + response.reason());
            case 429 -> new CreditBureauRateLimitException("Rate limit exceeded: " + response.reason());
            case 500 -> new CreditBureauServerException("Credit Bureau internal error: " + response.reason());
            case 502, 503, 504 -> new CreditBureauServiceUnavailableException("Credit Bureau service unavailable: " + response.reason());
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }

    public static class CreditBureauException extends Exception {
        public CreditBureauException(String message) {
            super(message);
        }
    }

    public static class CreditBureauBadRequestException extends CreditBureauException {
        public CreditBureauBadRequestException(String message) {
            super(message);
        }
    }

    public static class CreditBureauUnauthorizedException extends CreditBureauException {
        public CreditBureauUnauthorizedException(String message) {
            super(message);
        }
    }

    public static class CreditBureauForbiddenException extends CreditBureauException {
        public CreditBureauForbiddenException(String message) {
            super(message);
        }
    }

    public static class CreditBureauNotFoundException extends CreditBureauException {
        public CreditBureauNotFoundException(String message) {
            super(message);
        }
    }

    public static class CreditBureauRateLimitException extends CreditBureauException {
        public CreditBureauRateLimitException(String message) {
            super(message);
        }
    }

    public static class CreditBureauServerException extends CreditBureauException {
        public CreditBureauServerException(String message) {
            super(message);
        }
    }

    public static class CreditBureauServiceUnavailableException extends CreditBureauException {
        public CreditBureauServiceUnavailableException(String message) {
            super(message);
        }
    }
}