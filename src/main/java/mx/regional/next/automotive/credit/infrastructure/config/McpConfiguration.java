package mx.regional.next.automotive.credit.infrastructure.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "mx.regional.next.automotive.credit.infrastructure.mcp",
    "mx.regional.next.automotive.credit.application.usecases",
    "mx.regional.next.automotive.credit.domain.services",
    "mx.regional.next.automotive.credit.infrastructure.adapters"
})
public class McpConfiguration {
    
    // Esta configuración asegura que todos los componentes MCP sean detectados
    // y que Spring pueda inyectar las dependencias correctamente
}