package mx.regional.next.mcp_demo.config;

import com.logaritex.mcp.annotation.McpPrompt;
import com.logaritex.mcp.annotation.McpResource;
import org.springframework.ai.mcp.server.McpServerFeatures;
import org.springframework.ai.mcp.server.transport.MethodToolCallbackProvider;
import org.springframework.ai.mcp.server.transport.SpringAiMcpAnnotationProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class McpConfiguration {

    @Bean
    List<McpServerFeatures.SyncPromptSpecification> promptSpecs(ApplicationContext applicationContext) {
        // Obtener todos los beans de servicio que tengan al menos un método con @McpPrompt
        Map<String, Object> promptBeans = applicationContext.getBeansWithAnnotation(Service.class)
                .entrySet()
                .stream()
                .filter(entry -> Arrays.stream(entry.getValue().getClass().getMethods())
                        .anyMatch(method -> method.isAnnotationPresent(McpPrompt.class)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return SpringAiMcpAnnotationProvider.createSyncPromptSpecifications(List.of(promptBeans.values().toArray()));
    }

    @Bean
    List<McpServerFeatures.SyncResourceSpecification> syncResourceSpecifications(ApplicationContext applicationContext) {
        // Obtener todos los beans de servicio que tengan al menos un método con @McpResource
        Map<String, Object> resourceBeans = applicationContext.getBeansWithAnnotation(Service.class)
                .entrySet()
                .stream()
                .filter(entry -> Arrays.stream(entry.getValue().getClass().getMethods())
                        .anyMatch(method -> method.isAnnotationPresent(McpResource.class)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return SpringAiMcpAnnotationProvider.createSyncResourceSpecifications(List.of(resourceBeans.values().toArray()));
    }

    @Bean 
    MethodToolCallbackProvider toolCallbackProvider(ApplicationContext applicationContext) {
        // Obtener todos los beans de servicio que tengan al menos un método con @Tool
        Map<String, Object> toolBeans = applicationContext.getBeansWithAnnotation(Service.class)
                .entrySet()
                .stream()
                .filter(entry -> Arrays.stream(entry.getValue().getClass().getMethods())
                        .anyMatch(method -> method.isAnnotationPresent(com.logaritex.mcp.annotation.Tool.class)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        // Construir el provider con todos los tools
        return MethodToolCallbackProvider.builder()
                .toolObjects(toolBeans.values().toArray())
                .build();
    }
}
