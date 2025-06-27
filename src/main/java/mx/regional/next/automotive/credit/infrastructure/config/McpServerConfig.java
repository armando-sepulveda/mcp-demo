package mx.regional.next.automotive.credit.infrastructure.config;

import mx.regional.next.automotive.credit.infrastructure.mcp.server.AutomotiveCreditMcpServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuración del servidor MCP para crédito automotriz
 * Registra automáticamente todas las herramientas, recursos y prompts
 */
@Configuration
@Profile("!test") // No cargar en tests para evitar conflictos
public class McpServerConfig {

    @Value("${mcp.server.name:Automotive Credit MCP Server}")
    private String serverName;

    @Value("${mcp.server.version:1.0.0}")
    private String serverVersion;

    @Value("${mcp.server.description:MCP Server for automotive credit processing}")
    private String serverDescription;

    @Value("${mcp.server.protocol-version:2024-11-05}")
    private String protocolVersion;

    @Bean
    public AutomotiveCreditMcpServer mcpServer(ApplicationContext applicationContext) {
        return AutomotiveCreditMcpServer.builder()
            .serverName(serverName)
            .serverVersion(serverVersion)
            .serverDescription(serverDescription)
            .protocolVersion(protocolVersion)
            .applicationContext(applicationContext)
            .build();
    }

    /**
     * Configuración para herramientas MCP
     * Busca todos los componentes que tengan métodos anotados con @McpTool
     */
    @Bean
    public List<Object> mcpToolProviders(ApplicationContext applicationContext) {
        return findBeansWithMcpAnnotation(applicationContext, "McpTool");
    }

    /**
     * Configuración para recursos MCP
     * Busca todos los componentes que tengan métodos anotados con @McpResource
     */
    @Bean
    public List<Object> mcpResourceProviders(ApplicationContext applicationContext) {
        return findBeansWithMcpAnnotation(applicationContext, "McpResource");
    }

    /**
     * Configuración para prompts MCP
     * Busca todos los componentes que tengan métodos anotados con @McpPrompt
     */
    @Bean
    public List<Object> mcpPromptProviders(ApplicationContext applicationContext) {
        return findBeansWithMcpAnnotation(applicationContext, "McpPrompt");
    }

    /**
     * Encuentra todos los beans que tienen métodos con una anotación MCP específica
     */
    private List<Object> findBeansWithMcpAnnotation(ApplicationContext applicationContext, String annotationSimpleName) {
        Map<String, Object> allBeans = applicationContext.getBeansOfType(Object.class);
        
        return allBeans.values().stream()
            .filter(bean -> {
                // Obtener la clase real (sin proxies de Spring)
                Class<?> beanClass = getBeanClass(bean);
                
                // Buscar métodos con la anotación MCP específica
                return Arrays.stream(beanClass.getDeclaredMethods())
                    .anyMatch(method -> 
                        Arrays.stream(method.getAnnotations())
                            .anyMatch(annotation -> 
                                annotation.annotationType().getSimpleName().equals(annotationSimpleName)
                            )
                    );
            })
            .collect(Collectors.toList());
    }

    /**
     * Obtiene la clase real del bean, manejando proxies de Spring
     */
    private Class<?> getBeanClass(Object bean) {
        if (bean.getClass().getName().contains("$$SpringCGLIB$$") || 
            bean.getClass().getName().contains("$$EnhancerBySpringCGLIB$$")) {
            return bean.getClass().getSuperclass();
        }
        return bean.getClass();
    }
}