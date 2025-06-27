package mx.regional.next.automotive.credit.infrastructure.mcp.tools;

import mx.regional.next.automotive.credit.application.ports.in.ProcessCreditApplicationUseCase;
import mx.regional.next.automotive.credit.application.dto.CreditApplicationRequest;
import mx.regional.next.automotive.credit.application.dto.CreditApplicationResponse;
import mx.regional.next.automotive.credit.infrastructure.mcp.mappers.CreditApplicationMcpMapper;
import mx.regional.next.automotive.credit.domain.enums.CreditStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProcessCreditApplicationTool Tests")
class ProcessCreditApplicationToolTest {

    @Mock
    private ProcessCreditApplicationUseCase processCreditApplicationUseCase;

    @Mock
    private CreditApplicationMcpMapper mapper;

    @InjectMocks
    private ProcessCreditApplicationTool processCreditApplicationTool;

    private CreditApplicationRequest validRequest;
    private CreditApplicationResponse approvedResponse;
    private CreditApplicationResponse rejectedResponse;

    @BeforeEach
    void setUp() {
        validRequest = CreditApplicationRequest.builder()
            .customerDocument("12345678901")
            .requestedAmount(BigDecimal.valueOf(50_000_000))
            .vehicleVin("1HGBH41JXMN109186")
            .vehicleBrand("TOYOTA")
            .vehicleModel("COROLLA")
            .vehicleYear(2023)
            .vehicleValue(BigDecimal.valueOf(80_000_000))
            .vehicleKilometers(5_000)
            .build();

        approvedResponse = CreditApplicationResponse.builder()
            .applicationId(UUID.randomUUID())
            .status(CreditStatus.APROBADA)
            .approved(true)
            .approvedAmount(BigDecimal.valueOf(50_000_000))
            .creditScore(750)
            .interestRate(BigDecimal.valueOf(0.14))
            .build();

        rejectedResponse = CreditApplicationResponse.builder()
            .applicationId(UUID.randomUUID())
            .status(CreditStatus.RECHAZADA)
            .approved(false)
            .rejectionReason("Score crediticio insuficiente")
            .build();
    }

    @Nested
    @DisplayName("Process Application Tests")
    class ProcessApplicationTests {

        @Test
        @DisplayName("Should process approved application successfully")
        void shouldProcessApprovedApplicationSuccessfully() {
            // Given
            when(mapper.mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(validRequest);
            when(processCreditApplicationUseCase.processApplication(any(CreditApplicationRequest.class)))
                .thenReturn(approvedResponse);

            // When
            String result = processCreditApplicationTool.processApplication(
                "12345678901", 
                "50000000", 
                "1HGBH41JXMN109186", 
                "TOYOTA", 
                "COROLLA", 
                "2023", 
                "80000000", 
                "5000", 
                null
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("SOLICITUD APROBADA");
            assertThat(result).contains("50,000,000");
            assertThat(result).contains("750");
            assertThat(result).contains("14.00%");
            assertThat(result).contains("firma del contrato");

            verify(mapper).mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString());
            verify(processCreditApplicationUseCase).processApplication(any(CreditApplicationRequest.class));
        }

        @Test
        @DisplayName("Should process rejected application correctly")
        void shouldProcessRejectedApplicationCorrectly() {
            // Given
            when(mapper.mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(validRequest);
            when(processCreditApplicationUseCase.processApplication(any(CreditApplicationRequest.class)))
                .thenReturn(rejectedResponse);

            // When
            String result = processCreditApplicationTool.processApplication(
                "12345678901", 
                "50000000", 
                "1HGBH41JXMN109186", 
                "TOYOTA", 
                "COROLLA", 
                "2023", 
                "80000000", 
                "5000", 
                null
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("SOLICITUD RECHAZADA");
            assertThat(result).contains("Score crediticio insuficiente");
            assertThat(result).contains("Recomendaciones");
            assertThat(result).contains("historial crediticio");

            verify(mapper).mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString());
            verify(processCreditApplicationUseCase).processApplication(any(CreditApplicationRequest.class));
        }

        @Test
        @DisplayName("Should handle processing exception gracefully")
        void shouldHandleProcessingExceptionGracefully() {
            // Given
            when(mapper.mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(validRequest);
            when(processCreditApplicationUseCase.processApplication(any(CreditApplicationRequest.class)))
                .thenThrow(new RuntimeException("Error de sistema"));

            // When
            String result = processCreditApplicationTool.processApplication(
                "12345678901", 
                "50000000", 
                "1HGBH41JXMN109186", 
                "TOYOTA", 
                "COROLLA", 
                "2023", 
                "80000000", 
                "5000", 
                null
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("ERROR EN PROCESAMIENTO");
            assertThat(result).contains("Error de sistema");
            assertThat(result).contains("Verificar que todos los datos");

            verify(mapper).mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString());
            verify(processCreditApplicationUseCase).processApplication(any(CreditApplicationRequest.class));
        }

        @Test
        @DisplayName("Should handle mapper exception gracefully")
        void shouldHandleMapperExceptionGracefully() {
            // Given
            when(mapper.mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Datos inválidos"));

            // When
            String result = processCreditApplicationTool.processApplication(
                "invalid", 
                "invalid", 
                "invalid", 
                "TOYOTA", 
                "COROLLA", 
                "2023", 
                "80000000", 
                "5000", 
                null
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("ERROR EN PROCESAMIENTO");
            assertThat(result).contains("Datos inválidos");

            verify(mapper).mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString());
            verify(processCreditApplicationUseCase, never()).processApplication(any());
        }
    }

    @Nested
    @DisplayName("Calculate Monthly Installment Tests")
    class CalculateMonthlyInstallmentTests {

        @Test
        @DisplayName("Should calculate monthly installment correctly")
        void shouldCalculateMonthlyInstallmentCorrectly() {
            // When
            String result = processCreditApplicationTool.calculateMonthlyInstallment(
                "50000000", // 50M
                "15.5",     // 15.5%
                "60",       // 60 meses
                null        // Sin tipo especial
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("SIMULACIÓN DE CUOTA MENSUAL");
            assertThat(result).contains("50,000,000");
            assertThat(result).contains("15.50% EA");
            assertThat(result).contains("60 meses");
            assertThat(result).contains("5.0 años");
            assertThat(result).contains("Cuota mensual:");
            assertThat(result).contains("Total a pagar:");
            assertThat(result).contains("Total intereses:");
        }

        @Test
        @DisplayName("Should apply discount for juridica customer type")
        void shouldApplyDiscountForJuridicaCustomerType() {
            // When
            String result = processCreditApplicationTool.calculateMonthlyInstallment(
                "50000000", 
                "15.5", 
                "60", 
                "juridica"
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Descuento aplicado");
            assertThat(result).contains("5% para persona jurídica");
            // La tasa efectiva debería ser menor (15.5% * 0.95 = 14.725%)
            assertThat(result).contains("14.73% EA") || assertThat(result).contains("14.72% EA");
        }

        @Test
        @DisplayName("Should validate minimum amount")
        void shouldValidateMinimumAmount() {
            // When
            String result = processCreditApplicationTool.calculateMonthlyInstallment(
                "30000", // Menos del mínimo (50,000)
                "15.5", 
                "60", 
                null
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Error");
            assertThat(result).contains("monto mínimo del crédito es $50,000");
        }

        @Test
        @DisplayName("Should validate maximum amount")
        void shouldValidateMaximumAmount() {
            // When
            String result = processCreditApplicationTool.calculateMonthlyInstallment(
                "3000000000", // Más del máximo (2,000,000,000)
                "15.5", 
                "60", 
                null
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Error");
            assertThat(result).contains("monto máximo del crédito es $2,000,000,000");
        }

        @Test
        @DisplayName("Should validate term range")
        void shouldValidateTermRange() {
            // Test minimum term
            String resultMin = processCreditApplicationTool.calculateMonthlyInstallment(
                "50000000", 
                "15.5", 
                "6", // Menos del mínimo (12)
                null
            );

            assertThat(resultMin).contains("Error");
            assertThat(resultMin).contains("plazo debe estar entre 12 y 84 meses");

            // Test maximum term
            String resultMax = processCreditApplicationTool.calculateMonthlyInstallment(
                "50000000", 
                "15.5", 
                "96", // Más del máximo (84)
                null
            );

            assertThat(resultMax).contains("Error");
            assertThat(resultMax).contains("plazo debe estar entre 12 y 84 meses");
        }

        @Test
        @DisplayName("Should validate interest rate range")
        void shouldValidateInterestRateRange() {
            // Test minimum rate
            String resultMin = processCreditApplicationTool.calculateMonthlyInstallment(
                "50000000", 
                "3.0", // Menos del mínimo (5%)
                "60", 
                null
            );

            assertThat(resultMin).contains("Error");
            assertThat(resultMin).contains("tasa de interés debe estar entre 5% y 35%");

            // Test maximum rate
            String resultMax = processCreditApplicationTool.calculateMonthlyInstallment(
                "50000000", 
                "40.0", // Más del máximo (35%)
                "60", 
                null
            );

            assertThat(resultMax).contains("Error");
            assertThat(resultMax).contains("tasa de interés debe estar entre 5% y 35%");
        }

        @Test
        @DisplayName("Should handle invalid number format gracefully")
        void shouldHandleInvalidNumberFormatGracefully() {
            // When
            String result = processCreditApplicationTool.calculateMonthlyInstallment(
                "invalid_amount", 
                "15.5", 
                "60", 
                null
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Error");
            assertThat(result).contains("Formato inválido en los números");
        }

        @Test
        @DisplayName("Should show capacity requirements")
        void shouldShowCapacityRequirements() {
            // When
            String result = processCreditApplicationTool.calculateMonthlyInstallment(
                "50000000", 
                "15.5", 
                "60", 
                null
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Capacidad de pago requerida:");
            assertThat(result).contains("mensual mínimo");
            assertThat(result).contains("Costo financiero:");
        }
    }

    @Nested
    @DisplayName("Response Formatting Tests")
    class ResponseFormattingTests {

        @Test
        @DisplayName("Should format approved response with all details")
        void shouldFormatApprovedResponseWithAllDetails() {
            // Given
            CreditApplicationResponse detailedApprovedResponse = CreditApplicationResponse.builder()
                .applicationId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .status(CreditStatus.APROBADA)
                .approved(true)
                .approvedAmount(BigDecimal.valueOf(45_000_000))
                .creditScore(720)
                .interestRate(BigDecimal.valueOf(0.155))
                .build();

            when(mapper.mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(validRequest);
            when(processCreditApplicationUseCase.processApplication(any(CreditApplicationRequest.class)))
                .thenReturn(detailedApprovedResponse);

            // When
            String result = processCreditApplicationTool.processApplication(
                "12345678901", "45000000", "1HGBH41JXMN109186", "TOYOTA", 
                "COROLLA", "2023", "80000000", "5000", null
            );

            // Then
            assertThat(result).contains("123e4567-e89b-12d3-a456-426614174000");
            assertThat(result).contains("APROBADA");
            assertThat(result).contains("45,000,000");
            assertThat(result).contains("720");
            assertThat(result).contains("15.50%");
            assertThat(result).contains("60 meses");
            assertThat(result).contains("próximos pasos");
        }

        @Test
        @DisplayName("Should format rejected response with recommendation")
        void shouldFormatRejectedResponseWithRecommendation() {
            // Given
            CreditApplicationResponse detailedRejectedResponse = CreditApplicationResponse.builder()
                .applicationId(UUID.fromString("987fcdeb-51d2-43e1-b456-426614174001"))
                .status(CreditStatus.RECHAZADA)
                .approved(false)
                .rejectionReason("Ingresos insuficientes para el monto solicitado")
                .build();

            when(mapper.mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(validRequest);
            when(processCreditApplicationUseCase.processApplication(any(CreditApplicationRequest.class)))
                .thenReturn(detailedRejectedResponse);

            // When
            String result = processCreditApplicationTool.processApplication(
                "98765432109", "60000000", "1HGBH41JXMN109187", "CHEVROLET", 
                "AVEO", "2020", "40000000", "50000", null
            );

            // Then
            assertThat(result).contains("987fcdeb-51d2-43e1-b456-426614174001");
            assertThat(result).contains("RECHAZADA");
            assertThat(result).contains("Ingresos insuficientes");
            assertThat(result).contains("Recomendaciones");
            assertThat(result).contains("Revisar historial crediticio");
            assertThat(result).contains("monto menor");
            assertThat(result).contains("codeudor");
        }
    }

    @Nested
    @DisplayName("Tool Integration Tests")
    class ToolIntegrationTests {

        @Test
        @DisplayName("Should work with all required parameters")
        void shouldWorkWithAllRequiredParameters() {
            // Given
            when(mapper.mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(validRequest);
            when(processCreditApplicationUseCase.processApplication(any(CreditApplicationRequest.class)))
                .thenReturn(approvedResponse);

            // When
            String result = processCreditApplicationTool.processApplication(
                "12345678901",           // customerDocument
                "50000000",              // requestedAmount  
                "1HGBH41JXMN109186",    // vehicleVin
                "TOYOTA",                // vehicleBrand
                "COROLLA",               // vehicleModel
                "2023",                  // vehicleYear
                "80000000",              // vehicleValue
                "5000",                  // vehicleKilometers
                "{\"cedula\": \"scan.pdf\"}" // documentsJson
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("SOLICITUD APROBADA");
            
            verify(mapper).mapToApplicationRequest(
                eq("12345678901"),
                eq("50000000"),
                eq("1HGBH41JXMN109186"),
                eq("TOYOTA"),
                eq("COROLLA"),
                eq("2023"),
                eq("80000000"),
                eq("5000"),
                eq("{\"cedula\": \"scan.pdf\"}")
            );
        }

        @Test
        @DisplayName("Should work with optional documents parameter as null")
        void shouldWorkWithOptionalDocumentsParameterAsNull() {
            // Given
            when(mapper.mapToApplicationRequest(anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(validRequest);
            when(processCreditApplicationUseCase.processApplication(any(CreditApplicationRequest.class)))
                .thenReturn(approvedResponse);

            // When
            String result = processCreditApplicationTool.processApplication(
                "12345678901", "50000000", "1HGBH41JXMN109186", "TOYOTA", 
                "COROLLA", "2023", "80000000", "5000", null
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("SOLICITUD APROBADA");
            
            verify(mapper).mapToApplicationRequest(
                eq("12345678901"), eq("50000000"), eq("1HGBH41JXMN109186"), 
                eq("TOYOTA"), eq("COROLLA"), eq("2023"), eq("80000000"), 
                eq("5000"), eq(null)
            );
        }
    }
}