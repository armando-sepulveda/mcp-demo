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
public class CreditBureauClientConfig {

    @Value("${external.services.credit-bureau.timeout.connect:5000}")
    private int connectTimeout;

    @Value("${external.services.credit-bureau.timeout.read:30000}")
    private int readTimeout;

    @Bean
    public Request.Options creditBureauRequestOptions() {
        return new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    public Encoder creditBureauEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder creditBureauDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    public ErrorDecoder creditBureauErrorDecoder() {
        return new CreditBureauErrorDecoder();
    }

    @Bean
    public Retryer creditBureauRetryer() {
        return new Retryer.Default(1000, 3000, 3);
    }

    @Bean
    public Logger.Level creditBureauLoggerLevel() {
        return Logger.Level.BASIC;
    }
}