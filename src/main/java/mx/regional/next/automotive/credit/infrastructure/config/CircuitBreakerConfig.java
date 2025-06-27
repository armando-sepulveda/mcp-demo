package mx.regional.next.automotive.credit.infrastructure.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "resilience4j")
public class CircuitBreakerConfig {
    
    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerConfig.class);
    
    // Configuración para servicio de score crediticio
    @Bean
    public CircuitBreaker creditScoreServiceCircuitBreaker() {
        io.github.resilience4j.circuitbreaker.CircuitBreakerConfig config = 
            io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(60.0f)                    // 60% de errores para abrir
                .slowCallRateThreshold(80.0f)                   // 80% de llamadas lentas
                .slowCallDurationThreshold(Duration.ofSeconds(3)) // Llamada lenta > 3s
                .slidingWindowType(SlidingWindowType.COUNT_BASED) // Ventana basada en conteo
                .slidingWindowSize(10)                          // Ventana de 10 llamadas
                .minimumNumberOfCalls(5)                        // Mínimo 5 llamadas para evaluar
                .waitDurationInOpenState(Duration.ofSeconds(30)) // Esperar 30s antes de half-open
                .permittedNumberOfCallsInHalfOpenState(3)       // 3 llamadas en half-open
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(
                    org.springframework.web.client.ResourceAccessException.class,
                    java.net.SocketTimeoutException.class,
                    java.net.ConnectException.class,
                    feign.FeignException.class
                )
                .ignoreExceptions(
                    IllegalArgumentException.class,
                    javax.validation.ValidationException.class
                )
                .build();
        
        CircuitBreaker circuitBreaker = CircuitBreaker.of("creditScoreService", config);
        
        // Listeners para logging
        circuitBreaker.getEventPublisher().onStateTransition(event -> 
            log.info("Circuit breaker creditScoreService cambió de {} a {}", 
                    event.getStateTransition().getFromState(), 
                    event.getStateTransition().getToState()));
        
        circuitBreaker.getEventPublisher().onCallNotPermitted(event -> 
            log.warn("Llamada no permitida por circuit breaker creditScoreService"));
        
        return circuitBreaker;
    }
    
    // Configuración para servicio de validación de vehículos
    @Bean
    public CircuitBreaker vehicleValidationServiceCircuitBreaker() {
        io.github.resilience4j.circuitbreaker.CircuitBreakerConfig config = 
            io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(70.0f)                    // 70% de errores para abrir
                .slowCallRateThreshold(85.0f)                   // 85% de llamadas lentas
                .slowCallDurationThreshold(Duration.ofSeconds(5)) // Llamada lenta > 5s
                .slidingWindowType(SlidingWindowType.TIME_BASED) // Ventana basada en tiempo
                .slidingWindowSize(60)                          // Ventana de 60 segundos
                .minimumNumberOfCalls(3)                        // Mínimo 3 llamadas para evaluar
                .waitDurationInOpenState(Duration.ofSeconds(45)) // Esperar 45s antes de half-open
                .permittedNumberOfCallsInHalfOpenState(2)       // 2 llamadas en half-open
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(
                    org.springframework.web.client.ResourceAccessException.class,
                    java.net.SocketTimeoutException.class,
                    feign.FeignException.class
                )
                .build();
        
        CircuitBreaker circuitBreaker = CircuitBreaker.of("vehicleValidationService", config);
        
        circuitBreaker.getEventPublisher().onStateTransition(event -> 
            log.info("Circuit breaker vehicleValidationService cambió de {} a {}", 
                    event.getStateTransition().getFromState(), 
                    event.getStateTransition().getToState()));
        
        return circuitBreaker;
    }
    
    // Configuración para servicio de validación de documentos
    @Bean
    public CircuitBreaker documentValidationServiceCircuitBreaker() {
        io.github.resilience4j.circuitbreaker.CircuitBreakerConfig config = 
            io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(50.0f)                    // 50% de errores para abrir
                .slowCallRateThreshold(75.0f)                   // 75% de llamadas lentas
                .slowCallDurationThreshold(Duration.ofSeconds(10)) // Llamada lenta > 10s
                .slidingWindowType(SlidingWindowType.COUNT_BASED) // Ventana basada en conteo
                .slidingWindowSize(8)                           // Ventana de 8 llamadas
                .minimumNumberOfCalls(4)                        // Mínimo 4 llamadas para evaluar
                .waitDurationInOpenState(Duration.ofMinutes(1)) // Esperar 1 minuto antes de half-open
                .permittedNumberOfCallsInHalfOpenState(2)       // 2 llamadas en half-open
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(
                    org.springframework.web.client.ResourceAccessException.class,
                    java.net.SocketTimeoutException.class,
                    feign.FeignException.class,
                    java.io.IOException.class
                )
                .build();
        
        CircuitBreaker circuitBreaker = CircuitBreaker.of("documentValidationService", config);
        
        circuitBreaker.getEventPublisher().onStateTransition(event -> 
            log.info("Circuit breaker documentValidationService cambió de {} a {}", 
                    event.getStateTransition().getFromState(), 
                    event.getStateTransition().getToState()));
        
        return circuitBreaker;
    }
    
    // Configuración para servicio de notificaciones
    @Bean
    public CircuitBreaker notificationServiceCircuitBreaker() {
        io.github.resilience4j.circuitbreaker.CircuitBreakerConfig config = 
            io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(80.0f)                    // 80% de errores para abrir (más tolerante)
                .slowCallRateThreshold(90.0f)                   // 90% de llamadas lentas
                .slowCallDurationThreshold(Duration.ofSeconds(15)) // Llamada lenta > 15s
                .slidingWindowType(SlidingWindowType.COUNT_BASED) // Ventana basada en conteo
                .slidingWindowSize(6)                           // Ventana de 6 llamadas
                .minimumNumberOfCalls(3)                        // Mínimo 3 llamadas para evaluar
                .waitDurationInOpenState(Duration.ofSeconds(20)) // Esperar 20s antes de half-open
                .permittedNumberOfCallsInHalfOpenState(2)       // 2 llamadas en half-open
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(
                    org.springframework.web.client.ResourceAccessException.class,
                    feign.FeignException.class
                )
                .build();
        
        CircuitBreaker circuitBreaker = CircuitBreaker.of("notificationService", config);
        
        circuitBreaker.getEventPublisher().onStateTransition(event -> 
            log.info("Circuit breaker notificationService cambió de {} a {}", 
                    event.getStateTransition().getFromState(), 
                    event.getStateTransition().getToState()));
        
        return circuitBreaker;
    }
    
    // Configuración de Retry para servicios críticos
    @Bean
    public Retry creditScoreServiceRetry() {
        RetryConfig config = RetryConfig.custom()
            .maxAttempts(3)                                     // Máximo 3 intentos
            .waitDuration(Duration.ofSeconds(2))                // Esperar 2s entre intentos
            .exponentialBackoffMultiplier(1.5)                  // Backoff exponencial 1.5x
            .retryOnException(throwable -> 
                throwable instanceof org.springframework.web.client.ResourceAccessException ||
                throwable instanceof java.net.SocketTimeoutException ||
                (throwable instanceof feign.FeignException && 
                 ((feign.FeignException) throwable).status() >= 500))
            .build();
        
        Retry retry = Retry.of("creditScoreService", config);
        
        retry.getEventPublisher().onRetry(event -> 
            log.warn("Reintentando llamada a creditScoreService, intento {} de {}", 
                    event.getNumberOfRetryAttempts(), config.getMaxAttempts()));
        
        return retry;
    }
    
    @Bean
    public Retry vehicleValidationServiceRetry() {
        RetryConfig config = RetryConfig.custom()
            .maxAttempts(2)                                     // Máximo 2 intentos
            .waitDuration(Duration.ofSeconds(3))                // Esperar 3s entre intentos
            .retryOnException(throwable -> 
                throwable instanceof org.springframework.web.client.ResourceAccessException ||
                (throwable instanceof feign.FeignException && 
                 ((feign.FeignException) throwable).status() >= 500))
            .build();
        
        Retry retry = Retry.of("vehicleValidationService", config);
        
        retry.getEventPublisher().onRetry(event -> 
            log.warn("Reintentando llamada a vehicleValidationService, intento {} de {}", 
                    event.getNumberOfRetryAttempts(), config.getMaxAttempts()));
        
        return retry;
    }
    
    // Configuración de TimeLimiter
    @Bean
    public TimeLimiter creditScoreServiceTimeLimiter() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(8))             // Timeout de 8 segundos
            .cancelRunningFuture(true)                          // Cancelar future al timeout
            .build();
        
        TimeLimiter timeLimiter = TimeLimiter.of("creditScoreService", config);
        
        timeLimiter.getEventPublisher().onTimeout(event -> 
            log.error("Timeout en llamada a creditScoreService después de {}ms", 
                     event.getDuration().toMillis()));
        
        return timeLimiter;
    }
    
    @Bean
    public TimeLimiter vehicleValidationServiceTimeLimiter() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(10))            // Timeout de 10 segundos
            .cancelRunningFuture(true)
            .build();
        
        TimeLimiter timeLimiter = TimeLimiter.of("vehicleValidationService", config);
        
        timeLimiter.getEventPublisher().onTimeout(event -> 
            log.error("Timeout en llamada a vehicleValidationService después de {}ms", 
                     event.getDuration().toMillis()));
        
        return timeLimiter;
    }
    
    @Bean
    public TimeLimiter documentValidationServiceTimeLimiter() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(15))            // Timeout de 15 segundos
            .cancelRunningFuture(true)
            .build();
        
        TimeLimiter timeLimiter = TimeLimiter.of("documentValidationService", config);
        
        timeLimiter.getEventPublisher().onTimeout(event -> 
            log.error("Timeout en llamada a documentValidationService después de {}ms", 
                     event.getDuration().toMillis()));
        
        return timeLimiter;
    }
    
    @Bean
    public TimeLimiter notificationServiceTimeLimiter() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(20))            // Timeout de 20 segundos
            .cancelRunningFuture(false)                         // No cancelar notificaciones
            .build();
        
        return TimeLimiter.of("notificationService", config);
    }
}