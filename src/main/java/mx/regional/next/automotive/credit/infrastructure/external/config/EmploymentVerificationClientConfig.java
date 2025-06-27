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
public class EmploymentVerificationClientConfig {

    @Value("${external.services.employment-verification.timeout.connect:5000}")
    private int connectTimeout;

    @Value("${external.services.employment-verification.timeout.read:20000}")
    private int readTimeout;

    @Bean
    public Request.Options employmentVerificationRequestOptions() {
        return new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    public Encoder employmentVerificationEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder employmentVerificationDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    public ErrorDecoder employmentVerificationErrorDecoder() {
        return new EmploymentVerificationErrorDecoder();
    }

    @Bean
    public Retryer employmentVerificationRetryer() {
        return new Retryer.Default(1000, 2000, 2);
    }

    @Bean
    public Logger.Level employmentVerificationLoggerLevel() {
        return Logger.Level.BASIC;
    }
}