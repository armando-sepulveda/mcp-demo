package mx.regional.next.automotive.credit.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.repositories")
@EntityScan(basePackages = "mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.entities")
@EnableTransactionManagement
public class DatabaseConfig {
    // Configuration for JPA and database access
}