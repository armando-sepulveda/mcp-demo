package mx.regional.next.automotive.credit.infrastructure.external.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleValuationErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(VehicleValuationErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Vehicle Valuation API error - Method: {}, Status: {}, Reason: {}", 
                methodKey, response.status(), response.reason());

        return switch (response.status()) {
            case 400 -> new VehicleValuationBadRequestException("Invalid vehicle parameters: " + response.reason());
            case 401 -> new VehicleValuationUnauthorizedException("Authentication failed: " + response.reason());
            case 403 -> new VehicleValuationForbiddenException("Access denied: " + response.reason());
            case 404 -> new VehicleValuationNotFoundException("Vehicle not found in database: " + response.reason());
            case 422 -> new VehicleValuationValidationException("Vehicle data validation failed: " + response.reason());
            case 429 -> new VehicleValuationRateLimitException("Rate limit exceeded: " + response.reason());
            case 500 -> new VehicleValuationServerException("Vehicle Valuation service internal error: " + response.reason());
            case 502, 503, 504 -> new VehicleValuationServiceUnavailableException("Vehicle Valuation service unavailable: " + response.reason());
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }

    public static class VehicleValuationException extends Exception {
        public VehicleValuationException(String message) {
            super(message);
        }
    }

    public static class VehicleValuationBadRequestException extends VehicleValuationException {
        public VehicleValuationBadRequestException(String message) {
            super(message);
        }
    }

    public static class VehicleValuationUnauthorizedException extends VehicleValuationException {
        public VehicleValuationUnauthorizedException(String message) {
            super(message);
        }
    }

    public static class VehicleValuationForbiddenException extends VehicleValuationException {
        public VehicleValuationForbiddenException(String message) {
            super(message);
        }
    }

    public static class VehicleValuationNotFoundException extends VehicleValuationException {
        public VehicleValuationNotFoundException(String message) {
            super(message);
        }
    }

    public static class VehicleValuationValidationException extends VehicleValuationException {
        public VehicleValuationValidationException(String message) {
            super(message);
        }
    }

    public static class VehicleValuationRateLimitException extends VehicleValuationException {
        public VehicleValuationRateLimitException(String message) {
            super(message);
        }
    }

    public static class VehicleValuationServerException extends VehicleValuationException {
        public VehicleValuationServerException(String message) {
            super(message);
        }
    }

    public static class VehicleValuationServiceUnavailableException extends VehicleValuationException {
        public VehicleValuationServiceUnavailableException(String message) {
            super(message);
        }
    }
}