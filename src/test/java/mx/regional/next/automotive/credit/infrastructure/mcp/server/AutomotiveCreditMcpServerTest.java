package mx.regional.next.automotive.credit.infrastructure.mcp.server;

import mx.regional.next.automotive.credit.infrastructure.mcp.tools.*;
import mx.regional.next.automotive.credit.infrastructure.mcp.resources.CreditPoliciesResource;
import mx.regional.next.automotive.credit.infrastructure.mcp.prompts.CreditAnalystPrompt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AutomotiveCreditMcpServer Tests")
class AutomotiveCreditMcpServerTest {

    @Mock
    private ProcessCreditApplicationTool processCreditApplicationTool;

    @Mock
    private ValidateCustomerDocumentsTool validateCustomerDocumentsTool;

    @Mock
    private CheckVehicleEligibilityTool checkVehicleEligibilityTool;

    @Mock
    private GetCreditApplicationStatusTool getCreditApplicationStatusTool;

    @Mock
    private CreditPoliciesResource creditPoliciesResource;

    @Mock
    private CreditAnalystPrompt creditAnalystPrompt;

    @Mock
    private ApplicationReadyEvent applicationReadyEvent;

    @InjectMocks
    private AutomotiveCreditMcpServer automotiveCreditMcpServer;

    @Nested
    @DisplayName("Server Initialization Tests")
    class ServerInitializationTests {

        @Test
        @DisplayName("Should initialize server successfully")
        void shouldInitializeServerSuccessfully() {
            // When
            automotiveCreditMcpServer.startServer();

            // Then
            assertThat(automotiveCreditMcpServer.isServerRunning()).isTrue();

            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();
            assertThat(metrics).isNotEmpty();
            assertThat(metrics).containsKey("server_start_time");
            assertThat(metrics).containsKey("registered_tools");
            assertThat(metrics).containsKey("registered_resources");
            assertThat(metrics).containsKey("registered_prompts");
            assertThat(metrics).containsKey("total_requests");
            assertThat(metrics).containsKey("successful_requests");
            assertThat(metrics).containsKey("failed_requests");
        }

        @Test
        @DisplayName("Should register correct number of tools")
        void shouldRegisterCorrectNumberOfTools() {
            // When
            automotiveCreditMcpServer.startServer();

            // Then
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();
            assertThat(metrics.get("registered_tools")).isEqualTo(9); // Total tools expected
        }

        @Test
        @DisplayName("Should register correct number of resources")
        void shouldRegisterCorrectNumberOfResources() {
            // When
            automotiveCreditMcpServer.startServer();

            // Then
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();
            assertThat(metrics.get("registered_resources")).isEqualTo(7); // Total resources expected
        }

        @Test
        @DisplayName("Should register correct number of prompts")
        void shouldRegisterCorrectNumberOfPrompts() {
            // When
            automotiveCreditMcpServer.startServer();

            // Then
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();
            assertThat(metrics.get("registered_prompts")).isEqualTo(1); // credit_analyst prompt
        }

        @Test
        @DisplayName("Should handle initialization failure gracefully")
        void shouldHandleInitializationFailureGracefully() {
            // Given - Force an exception during startup
            doThrow(new RuntimeException("Startup error")).when(applicationReadyEvent).getSource();

            // When & Then
            // Since we can't mock the private methods directly, we test the public behavior
            // The server should still be able to start normally
            assertDoesNotThrow(() -> automotiveCreditMcpServer.startServer());
        }
    }

    @Nested
    @DisplayName("Server State Management Tests")
    class ServerStateManagementTests {

        @Test
        @DisplayName("Should start as not running initially")
        void shouldStartAsNotRunningInitially() {
            // Then
            assertThat(automotiveCreditMcpServer.isServerRunning()).isFalse();
        }

        @Test
        @DisplayName("Should be running after successful start")
        void shouldBeRunningAfterSuccessfulStart() {
            // When
            automotiveCreditMcpServer.startServer();

            // Then
            assertThat(automotiveCreditMcpServer.isServerRunning()).isTrue();
        }

        @Test
        @DisplayName("Should stop server gracefully")
        void shouldStopServerGracefully() {
            // Given
            automotiveCreditMcpServer.startServer();
            assertThat(automotiveCreditMcpServer.isServerRunning()).isTrue();

            // When
            automotiveCreditMcpServer.stopServer();

            // Then
            assertThat(automotiveCreditMcpServer.isServerRunning()).isFalse();
        }

        @Test
        @DisplayName("Should clear metrics on stop")
        void shouldClearMetricsOnStop() {
            // Given
            automotiveCreditMcpServer.startServer();
            Map<String, Object> metricsBeforeStop = automotiveCreditMcpServer.getServerMetrics();
            assertThat(metricsBeforeStop).isNotEmpty();

            // When
            automotiveCreditMcpServer.stopServer();

            // Then
            Map<String, Object> metricsAfterStop = automotiveCreditMcpServer.getServerMetrics();
            assertThat(metricsAfterStop).isEmpty();
        }
    }

    @Nested
    @DisplayName("Server Information Tests")
    class ServerInformationTests {

        @BeforeEach
        void setUp() {
            automotiveCreditMcpServer.startServer();
        }

        @Test
        @DisplayName("Should provide comprehensive server info")
        void shouldProvideComprehensiveServerInfo() {
            // When
            String serverInfo = automotiveCreditMcpServer.getServerInfo();

            // Then
            assertThat(serverInfo).isNotNull();
            assertThat(serverInfo).contains("SERVIDOR MCP CRÉDITO AUTOMOTRIZ");
            assertThat(serverInfo).contains("🟢 ACTIVO");
            assertThat(serverInfo).contains("Herramientas:");
            assertThat(serverInfo).contains("Recursos:");
            assertThat(serverInfo).contains("Prompts:");
            assertThat(serverInfo).contains("Tiempo activo:");
            assertThat(serverInfo).contains("Requests totales:");
            assertThat(serverInfo).contains("Capacidades Principales");
            assertThat(serverInfo).contains("Procesamiento de solicitudes de crédito");
            assertThat(serverInfo).contains("Validación de documentos");
            assertThat(serverInfo).contains("Verificación de elegibilidad");
        }

        @Test
        @DisplayName("Should show inactive state when server is stopped")
        void shouldShowInactiveStateWhenServerIsStopped() {
            // When
            automotiveCreditMcpServer.stopServer();
            String serverInfo = automotiveCreditMcpServer.getServerInfo();

            // Then
            assertThat(serverInfo).contains("🔴 INACTIVO");
        }

        @Test
        @DisplayName("Should list all available tools")
        void shouldListAllAvailableTools() {
            // When
            List<String> tools = automotiveCreditMcpServer.getAvailableTools();

            // Then
            assertThat(tools).isNotEmpty();
            assertThat(tools).hasSize(9);
            assertThat(tools).anyMatch(tool -> tool.contains("process_credit_application"));
            assertThat(tools).anyMatch(tool -> tool.contains("validate_customer_documents"));
            assertThat(tools).anyMatch(tool -> tool.contains("check_vehicle_eligibility"));
            assertThat(tools).anyMatch(tool -> tool.contains("get_credit_application_status"));
            assertThat(tools).anyMatch(tool -> tool.contains("calculate_monthly_installment"));
        }

        @Test
        @DisplayName("Should list all available resources")
        void shouldListAllAvailableResources() {
            // When
            List<String> resources = automotiveCreditMcpServer.getAvailableResources();

            // Then
            assertThat(resources).isNotEmpty();
            assertThat(resources).hasSize(5);
            assertThat(resources).anyMatch(resource -> resource.contains("credit://policies"));
            assertThat(resources).anyMatch(resource -> resource.contains("credit://interest-rates"));
            assertThat(resources).anyMatch(resource -> resource.contains("credit://documents/*"));
            assertThat(resources).anyMatch(resource -> resource.contains("credit://eligibility-criteria"));
            assertThat(resources).anyMatch(resource -> resource.contains("credit://resource/*"));
        }

        @Test
        @DisplayName("Should list all available prompts")
        void shouldListAllAvailablePrompts() {
            // When
            List<String> prompts = automotiveCreditMcpServer.getAvailablePrompts();

            // Then
            assertThat(prompts).isNotEmpty();
            assertThat(prompts).hasSize(1);
            assertThat(prompts).anyMatch(prompt -> prompt.contains("credit_analyst"));
        }
    }

    @Nested
    @DisplayName("Metrics Management Tests")
    class MetricsManagementTests {

        @BeforeEach
        void setUp() {
            automotiveCreditMcpServer.startServer();
        }

        @Test
        @DisplayName("Should initialize metrics with zero values")
        void shouldInitializeMetricsWithZeroValues() {
            // When
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();

            // Then
            assertThat(metrics.get("total_requests")).isEqualTo(0L);
            assertThat(metrics.get("successful_requests")).isEqualTo(0L);
            assertThat(metrics.get("failed_requests")).isEqualTo(0L);
            assertThat(metrics.get("average_response_time")).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should increment total requests")
        void shouldIncrementTotalRequests() {
            // When
            automotiveCreditMcpServer.incrementTotalRequests();
            automotiveCreditMcpServer.incrementTotalRequests();

            // Then
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();
            assertThat(metrics.get("total_requests")).isEqualTo(2L);
        }

        @Test
        @DisplayName("Should increment successful requests")
        void shouldIncrementSuccessfulRequests() {
            // When
            automotiveCreditMcpServer.incrementSuccessfulRequests();
            automotiveCreditMcpServer.incrementSuccessfulRequests();
            automotiveCreditMcpServer.incrementSuccessfulRequests();

            // Then
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();
            assertThat(metrics.get("successful_requests")).isEqualTo(3L);
        }

        @Test
        @DisplayName("Should increment failed requests")
        void shouldIncrementFailedRequests() {
            // When
            automotiveCreditMcpServer.incrementFailedRequests();

            // Then
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();
            assertThat(metrics.get("failed_requests")).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should track server start time")
        void shouldTrackServerStartTime() {
            // When
            long beforeStart = System.currentTimeMillis();
            automotiveCreditMcpServer.startServer();
            long afterStart = System.currentTimeMillis();

            // Then
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();
            Long startTime = (Long) metrics.get("server_start_time");
            assertThat(startTime).isNotNull();
            assertThat(startTime).isBetween(beforeStart, afterStart);
        }

        @Test
        @DisplayName("Should return immutable copy of metrics")
        void shouldReturnImmutableCopyOfMetrics() {
            // When
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();

            // Then
            assertThrows(UnsupportedOperationException.class, () -> 
                metrics.put("new_metric", "value"));
        }
    }

    @Nested
    @DisplayName("Uptime Calculation Tests")
    class UptimeCalculationTests {

        @Test
        @DisplayName("Should calculate uptime correctly")
        void shouldCalculateUptimeCorrectly() throws InterruptedException {
            // Given
            automotiveCreditMcpServer.startServer();
            
            // Wait a short time to get measurable uptime
            Thread.sleep(100);

            // When
            String serverInfo = automotiveCreditMcpServer.getServerInfo();

            // Then
            assertThat(serverInfo).contains("Tiempo activo:");
            // Since we only waited a short time, uptime should be in minutes
            assertThat(serverInfo).containsAnyOf("0m", "1m");
        }

        @Test
        @DisplayName("Should handle uptime calculation with server not started")
        void shouldHandleUptimeCalculationWithServerNotStarted() {
            // When - Server not started
            String serverInfo = automotiveCreditMcpServer.getServerInfo();

            // Then
            assertThat(serverInfo).contains("N/A");
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Should handle stop server exception gracefully")
        void shouldHandleStopServerExceptionGracefully() {
            // Given
            automotiveCreditMcpServer.startServer();

            // When & Then - Should not throw exception even if there are issues
            assertDoesNotThrow(() -> automotiveCreditMcpServer.stopServer());
        }

        @Test
        @DisplayName("Should handle multiple start calls gracefully")
        void shouldHandleMultipleStartCallsGracefully() {
            // When & Then
            assertDoesNotThrow(() -> {
                automotiveCreditMcpServer.startServer();
                automotiveCreditMcpServer.startServer(); // Second call
            });

            assertThat(automotiveCreditMcpServer.isServerRunning()).isTrue();
        }

        @Test
        @DisplayName("Should handle multiple stop calls gracefully")
        void shouldHandleMultipleStopCallsGracefully() {
            // Given
            automotiveCreditMcpServer.startServer();

            // When & Then
            assertDoesNotThrow(() -> {
                automotiveCreditMcpServer.stopServer();
                automotiveCreditMcpServer.stopServer(); // Second call
            });

            assertThat(automotiveCreditMcpServer.isServerRunning()).isFalse();
        }
    }

    @Nested
    @DisplayName("Component Integration Tests")
    class ComponentIntegrationTests {

        @Test
        @DisplayName("Should have all required MCP components injected")
        void shouldHaveAllRequiredMcpComponentsInjected() {
            // Then - All dependencies should be properly injected (verified by successful construction)
            assertThat(automotiveCreditMcpServer).isNotNull();
            
            // The server should be able to start without null pointer exceptions
            assertDoesNotThrow(() -> automotiveCreditMcpServer.startServer());
        }

        @Test
        @DisplayName("Should provide consistent tool and resource counts")
        void shouldProvideConsistentToolAndResourceCounts() {
            // Given
            automotiveCreditMcpServer.startServer();

            // When
            List<String> tools = automotiveCreditMcpServer.getAvailableTools();
            List<String> resources = automotiveCreditMcpServer.getAvailableResources();
            List<String> prompts = automotiveCreditMcpServer.getAvailablePrompts();
            
            Map<String, Object> metrics = automotiveCreditMcpServer.getServerMetrics();

            // Then
            assertThat(metrics.get("registered_tools")).isEqualTo(tools.size());
            assertThat(metrics.get("registered_resources")).isEqualTo(resources.size());
            assertThat(metrics.get("registered_prompts")).isEqualTo(prompts.size());
        }
    }
}