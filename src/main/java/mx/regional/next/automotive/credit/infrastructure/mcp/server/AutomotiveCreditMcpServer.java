package mx.regional.next.automotive.credit.infrastructure.mcp.server;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Servidor MCP principal para crédito automotriz
 * Coordina todas las herramientas, recursos y prompts MCP
 */
@Component
@Slf4j
@Builder
public class AutomotiveCreditMcpServer {

    private final String serverName;
    private final String serverVersion;
    private final String serverDescription;
    private final String protocolVersion;
    private final ApplicationContext applicationContext;

    @PostConstruct
    public void initialize() {
        log.info("Initializing {} v{}", serverName, serverVersion);
        log.info("Description: {}", serverDescription);
        log.info("Protocol Version: {}", protocolVersion);
        
        // Descobrir y registrar capacidades MCP
        discoverMcpCapabilities();
        
        log.info("Automotive Credit MCP Server ready");
    }

    /**
     * Descubre automáticamente todas las capacidades MCP disponibles
     */
    private void discoverMcpCapabilities() {
        int toolCount = discoverTools();
        int resourceCount = discoverResources();
        int promptCount = discoverPrompts();
        
        log.info("Discovered MCP capabilities:");
        log.info("- Tools: {} available", toolCount);
        log.info("- Resources: {} available", resourceCount);
        log.info("- Prompts: {} available", promptCount);
    }

    /**
     * Descubre herramientas MCP disponibles
     */
    private int discoverTools() {
        Map<String, Object> allBeans = applicationContext.getBeansOfType(Object.class);
        int count = 0;
        
        for (Object bean : allBeans.values()) {
            Class<?> beanClass = getBeanClass(bean);
            Method[] methods = beanClass.getDeclaredMethods();
            
            for (Method method : methods) {
                if (hasAnnotation(method, "McpTool")) {
                    String toolName = getAnnotationValue(method, "McpTool", "name");
                    String description = getAnnotationValue(method, "McpTool", "description");
                    
                    log.debug("Discovered MCP Tool: {} - {}", toolName, description);
                    count++;
                }
            }
        }
        
        return count;
    }

    /**
     * Descubre recursos MCP disponibles
     */
    private int discoverResources() {
        Map<String, Object> allBeans = applicationContext.getBeansOfType(Object.class);
        int count = 0;
        
        for (Object bean : allBeans.values()) {
            Class<?> beanClass = getBeanClass(bean);
            Method[] methods = beanClass.getDeclaredMethods();
            
            for (Method method : methods) {
                if (hasAnnotation(method, "McpResource")) {
                    String resourceUri = getAnnotationValue(method, "McpResource", "uri");
                    String name = getAnnotationValue(method, "McpResource", "name");
                    
                    log.debug("Discovered MCP Resource: {} ({})", name, resourceUri);
                    count++;
                }
            }
        }
        
        return count;
    }

    /**
     * Descubre prompts MCP disponibles
     */
    private int discoverPrompts() {
        Map<String, Object> allBeans = applicationContext.getBeansOfType(Object.class);
        int count = 0;
        
        for (Object bean : allBeans.values()) {
            Class<?> beanClass = getBeanClass(bean);
            Method[] methods = beanClass.getDeclaredMethods();
            
            for (Method method : methods) {
                if (hasAnnotation(method, "McpPrompt")) {
                    String promptName = getAnnotationValue(method, "McpPrompt", "name");
                    String description = getAnnotationValue(method, "McpPrompt", "description");
                    
                    log.debug("Discovered MCP Prompt: {} - {}", promptName, description);
                    count++;
                }
            }
        }
        
        return count;
    }

    /**
     * Verifica si un método tiene una anotación específica
     */
    private boolean hasAnnotation(Method method, String annotationSimpleName) {
        return Arrays.stream(method.getAnnotations())
            .anyMatch(annotation -> 
                annotation.annotationType().getSimpleName().equals(annotationSimpleName)
            );
    }

    /**
     * Obtiene el valor de un atributo de una anotación
     */
    private String getAnnotationValue(Method method, String annotationSimpleName, String attributeName) {
        return Arrays.stream(method.getAnnotations())
            .filter(annotation -> 
                annotation.annotationType().getSimpleName().equals(annotationSimpleName)
            )
            .findFirst()
            .map(annotation -> {
                try {
                    Method valueMethod = annotation.annotationType().getMethod(attributeName);
                    Object value = valueMethod.invoke(annotation);
                    return value != null ? value.toString() : "";
                } catch (Exception e) {
                    log.warn("Could not get {} from {}: {}", attributeName, annotationSimpleName, e.getMessage());
                    return "";
                }
            })
            .orElse("");
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

    /**
     * Información del servidor MCP
     */
    public McpServerInfo getServerInfo() {
        return McpServerInfo.builder()
            .name(serverName)
            .version(serverVersion)
            .description(serverDescription)
            .protocolVersion(protocolVersion)
            .capabilities(getMcpCapabilities())
            .build();
    }

    /**
     * Obtiene las capacidades MCP del servidor
     */
    private McpCapabilities getMcpCapabilities() {
        return McpCapabilities.builder()
            .tools(true)
            .resources(true)
            .prompts(true)
            .logging(true)
            .build();
    }

    /**
     * Información del servidor MCP
     */
    @Builder
    public static class McpServerInfo {
        private final String name;
        private final String version;
        private final String description;
        private final String protocolVersion;
        private final McpCapabilities capabilities;
    }

    /**
     * Capacidades del servidor MCP
     */
    @Builder
    public static class McpCapabilities {
        private final boolean tools;
        private final boolean resources;
        private final boolean prompts;
        private final boolean logging;
    }
}