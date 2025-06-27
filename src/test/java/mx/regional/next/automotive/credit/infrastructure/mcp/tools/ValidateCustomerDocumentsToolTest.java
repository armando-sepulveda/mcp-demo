package mx.regional.next.automotive.credit.infrastructure.mcp.tools;

import mx.regional.next.automotive.credit.application.ports.in.ValidateDocumentsUseCase;
import mx.regional.next.automotive.credit.application.dto.DocumentValidationRequest;
import mx.regional.next.automotive.credit.application.dto.DocumentValidationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidateCustomerDocumentsTool Tests")
class ValidateCustomerDocumentsToolTest {

    @Mock
    private ValidateDocumentsUseCase validateDocumentsUseCase;

    @InjectMocks
    private ValidateCustomerDocumentsTool validateCustomerDocumentsTool;

    private DocumentValidationResponse validResponse;
    private DocumentValidationResponse invalidResponse;

    @BeforeEach
    void setUp() {
        validResponse = DocumentValidationResponse.builder()
            .customerDocument("12345678901")
            .allDocumentsValid(true)
            .validDocumentsCount(5)
            .missingDocuments(List.of())
            .invalidDocuments(List.of())
            .build();

        invalidResponse = DocumentValidationResponse.builder()
            .customerDocument("98765432109")
            .allDocumentsValid(false)
            .validDocumentsCount(3)
            .missingDocuments(List.of("Extractos bancarios", "Certificado laboral"))
            .invalidDocuments(List.of("Cédula vencida"))
            .build();
    }

    @Nested
    @DisplayName("Document Validation Tests")
    class DocumentValidationTests {

        @Test
        @DisplayName("Should validate documents successfully for natural person")
        void shouldValidateDocumentsSuccessfullyForNaturalPerson() {
            // Given
            String customerDocument = "12345678901";
            String customerType = "natural";
            String documentsJson = "{\"cedula\": \"cedula.pdf\", \"extractos\": \"extractos.pdf\"}";

            when(validateDocumentsUseCase.validateDocuments(any(DocumentValidationRequest.class)))
                .thenReturn(validResponse);

            // When
            String result = validateCustomerDocumentsTool.validateDocuments(
                customerDocument, customerType, documentsJson);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("DOCUMENTOS VÁLIDOS");
            assertThat(result).contains("12345678901");
            assertThat(result).contains("5");
            assertThat(result).contains("listos para evaluación crediticia");

            verify(validateDocumentsUseCase).validateDocuments(any(DocumentValidationRequest.class));
        }

        @Test
        @DisplayName("Should handle incomplete documents for natural person")
        void shouldHandleIncompleteDocumentsForNaturalPerson() {
            // Given
            String customerDocument = "98765432109";
            String customerType = "natural";
            String documentsJson = "{\"cedula\": \"cedula_vencida.pdf\"}";

            when(validateDocumentsUseCase.validateDocuments(any(DocumentValidationRequest.class)))
                .thenReturn(invalidResponse);

            // When
            String result = validateCustomerDocumentsTool.validateDocuments(
                customerDocument, customerType, documentsJson);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("DOCUMENTOS INCOMPLETOS");
            assertThat(result).contains("98765432109");
            assertThat(result).contains("Documentos Válidos: 3");
            assertThat(result).contains("Documentos Faltantes: 2");
            assertThat(result).contains("Documentos Inválidos: 1");
            assertThat(result).contains("Extractos bancarios");
            assertThat(result).contains("Certificado laboral");
            assertThat(result).contains("Cédula vencida");
            assertThat(result).contains("Proporcionar los documentos faltantes");

            verify(validateDocumentsUseCase).validateDocuments(any(DocumentValidationRequest.class));
        }

        @Test
        @DisplayName("Should handle validation service exception")
        void shouldHandleValidationServiceException() {
            // Given
            String customerDocument = "12345678901";
            String customerType = "natural";
            String documentsJson = "{\"cedula\": \"cedula.pdf\"}";

            when(validateDocumentsUseCase.validateDocuments(any(DocumentValidationRequest.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

            // When
            String result = validateCustomerDocumentsTool.validateDocuments(
                customerDocument, customerType, documentsJson);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("ERROR EN VALIDACIÓN DE DOCUMENTOS");
            assertThat(result).contains("Service unavailable");
            assertThat(result).contains("Verificar formato de los documentos");

            verify(validateDocumentsUseCase).validateDocuments(any(DocumentValidationRequest.class));
        }
    }

    @Nested
    @DisplayName("Document Requirements Tests")
    class DocumentRequirementsTests {

        @Test
        @DisplayName("Should return natural person requirements")
        void shouldReturnNaturalPersonRequirements() {
            // When
            String result = validateCustomerDocumentsTool.getDocumentRequirements("natural");

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("DOCUMENTOS REQUERIDOS - PERSONA NATURAL");
            assertThat(result).contains("Cédula de ciudadanía");
            assertThat(result).contains("Certificado laboral");
            assertThat(result).contains("Últimas 3 nóminas");
            assertThat(result).contains("Extractos bancarios");
            assertThat(result).contains("Referencias comerciales");
        }

        @Test
        @DisplayName("Should return juridica person requirements")
        void shouldReturnJuridicaPersonRequirements() {
            // When
            String result = validateCustomerDocumentsTool.getDocumentRequirements("juridica");

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("DOCUMENTOS REQUERIDOS - PERSONA JURÍDICA");
            assertThat(result).contains("Certificado de existencia y representación legal");
            assertThat(result).contains("Estados financieros últimos 2 años");
            assertThat(result).contains("Extractos bancarios últimos 6 meses");
        }

        @Test
        @DisplayName("Should handle invalid customer type gracefully")
        void shouldHandleInvalidCustomerTypeGracefully() {
            // When
            String result = validateCustomerDocumentsTool.getDocumentRequirements("invalid_type");

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Error");
            assertThat(result).contains("Tipo de cliente inválido");
            assertThat(result).contains("'natural' o 'juridica'");
        }
    }
}