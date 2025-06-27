package mx.regional.next.automotive.credit.infrastructure.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients(basePackages = "mx.regional.next.automotive.credit.infrastructure.adapters.external.clients")
public class FeignConfig {
    
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
    
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
            5000, TimeUnit.MILLISECONDS, // connect timeout
            10000, TimeUnit.MILLISECONDS, // read timeout
            true // follow redirects
        );
    }
    
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
            100, // initial interval
            1000, // max interval
            3 // max attempts
        );
    }
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }
    
    private static class CustomFeignErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();
        
        @Override
        public Exception decode(String methodKey, feign.Response response) {
            switch (response.status()) {
                case 400:
                    return new RuntimeException("Bad Request: " + methodKey);
                case 401:
                    return new RuntimeException("Unauthorized: " + methodKey);
                case 404:
                    return new RuntimeException("Not Found: " + methodKey);
                case 503:
                    return new RuntimeException("Service Unavailable: " + methodKey);
                default:
                    return defaultErrorDecoder.decode(methodKey, response);
            }
        }
    }
}