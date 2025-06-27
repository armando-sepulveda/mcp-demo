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
public class NotificationClientConfig {

    @Value("${external.services.notification.timeout.connect:3000}")
    private int connectTimeout;

    @Value("${external.services.notification.timeout.read:10000}")
    private int readTimeout;

    @Bean
    public Request.Options notificationRequestOptions() {
        return new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    public Encoder notificationEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder notificationDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    public ErrorDecoder notificationErrorDecoder() {
        return new NotificationErrorDecoder();
    }

    @Bean
    public Retryer notificationRetryer() {
        return new Retryer.Default(500, 1500, 3);
    }

    @Bean
    public Logger.Level notificationLoggerLevel() {
        return Logger.Level.HEADERS;
    }
}