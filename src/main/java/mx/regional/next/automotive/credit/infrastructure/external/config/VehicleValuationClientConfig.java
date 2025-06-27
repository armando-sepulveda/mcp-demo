package mx.regional.next.automotive.credit.infrastructure.external.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class VehicleValuationClientConfig {

    @Value("${external.services.vehicle-valuation.timeout.connect:5000}")
    private int connectTimeout;

    @Value("${external.services.vehicle-valuation.timeout.read:25000}")
    private int readTimeout;

    @Bean
    public Request.Options vehicleValuationRequestOptions() {
        return new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    public Encoder vehicleValuationEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder vehicleValuationDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    public ErrorDecoder vehicleValuationErrorDecoder() {
        return new VehicleValuationErrorDecoder();
    }

    @Bean
    public Retryer vehicleValuationRetryer() {
        return new Retryer.Default(1000, 2000, 2);
    }

    @Bean
    public Logger.Level vehicleValuationLoggerLevel() {
        return Logger.Level.BASIC;
    }
}