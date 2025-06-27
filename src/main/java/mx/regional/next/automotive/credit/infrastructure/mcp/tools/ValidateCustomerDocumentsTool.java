package mx.regional.next.automotive.credit.infrastructure.mcp.tools;

import mx.regional.next.automotive.credit.application.ports.in.ValidateDocumentsUseCase;
import mx.regional.next.automotive.credit.application.dto.DocumentValidationRequest;
import mx.regional.next.automotive.credit.application.dto.DocumentValidationResponse;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ValidateCustomerDocumentsTool {
    
    private static final Logger log = LoggerFactory.getLogger(ValidateCustomerDocumentsTool.class);
    
    private final ValidateDocumentsUseCase validateDocumentsUseCase;
    
    public ValidateCustomerDocumentsTool(ValidateDocumentsUseCase validateDocumentsUseCase) {
        this.validateDocumentsUseCase = validateDocumentsUseCase;
    }
    
    @Tool(name = "validate_customer_documents", 
          description = "Valida la autenticidad y completitud de los documentos presentados por el cliente para la solicitud de crédito.")
    public String validateDocuments(
            @ToolParam(description = "Número de documento del cliente - OBLIGATORIO", required = true) 
            String customerDocument,
            
            @ToolParam(description = "Tipo de persona (natural/juridica) - OBLIGATORIO", required = true) 
            String customerType,
            
            @ToolParam(description = "Lista de documentos en formato JSON - OBLIGATORIO", required = true) 
            String documentsJson) {
        
        try {
            log.info("Validando documentos vía MCP para cliente: {} tipo: {}", customerDocument, customerType);
            
            DocumentValidationRequest request = DocumentValidationRequest.builder()
                .customerDocument(customerDocument)
                .customerType(customerType)
                .documentsJson(documentsJson)
                .build();
            
            DocumentValidationResponse response = validateDocumentsUseCase.validateDocuments(request);
            
            return formatValidationResponse(response);
            
        } catch (Exception e) {
            log.error("Error validando documentos vía MCP", e);
            return formatErrorResponse(e.getMessage());
        }
    }
    
    private String formatValidationResponse(DocumentValidationResponse response) {
        StringBuilder result = new StringBuilder();
        
        result.append("📄 **RESULTADO DE VALIDACIÓN DE DOCUMENTOS**\n\n");
        result.append("🆔 **Cliente:** ").append(response.getCustomerDocument()).append("\n");
        result.append("📊 **Estado General:** ");
        
        if (response.isAllDocumentsValid()) {
            result.append("✅ **DOCUMENTOS VÁLIDOS**\n\n");
            result.append("🎯 **Resumen:** Todos los documentos han sido validados exitosamente.\n");
            result.append("📝 **Documentos Verificados:** ").append(response.getValidDocumentsCount()).append("\n");
            result.append("✅ **Siguiente Paso:** Los documentos están listos para evaluación crediticia.\n");
        } else {
            result.append("❌ **DOCUMENTOS INCOMPLETOS**\n\n");
            result.append("📋 **Documentos Válidos:** ").append(response.getValidDocumentsCount()).append("\n");
            result.append("⚠️ **Documentos Faltantes:** ").append(response.getMissingDocuments().size()).append("\n");
            result.append("❌ **Documentos Inválidos:** ").append(response.getInvalidDocuments().size()).append("\n\n");
            
            if (!response.getMissingDocuments().isEmpty()) {
                result.append("📋 **Documentos Faltantes:**\n");
                response.getMissingDocuments().forEach(doc -> 
                    result.append("• ").append(doc).append("\n"));
                result.append("\n");
            }
            
            if (!response.getInvalidDocuments().isEmpty()) {
                result.append("❌ **Documentos Inválidos:**\n");
                response.getInvalidDocuments().forEach(doc -> 
                    result.append("• ").append(doc).append("\n"));
                result.append("\n");
            }
            
            result.append("📝 **Acción Requerida:**\n");
            result.append("1. Proporcionar los documentos faltantes\n");
            result.append("2. Corregir los documentos inválidos\n");
            result.append("3. Solicitar nueva validación una vez completados\n");
        }
        
        return result.toString();
    }
    
    @Tool(name = "get_document_requirements", 
          description = "Obtiene la lista de documentos requeridos según el tipo de cliente.")
    public String getDocumentRequirements(
            @ToolParam(description = "Tipo de cliente (natural/juridica) - OBLIGATORIO", required = true) 
            String customerType) {
        
        try {
            log.info("Consultando requisitos de documentos para tipo: {}", customerType);
            
            return switch (customerType.toLowerCase()) {
                case "natural" -> getPersonaNaturalRequirements();
                case "juridica" -> getPersonaJuridicaRequirements();
                default -> "❌ **Error:** Tipo de cliente inválido. Use 'natural' o 'juridica'.";
            };
            
        } catch (Exception e) {
            log.error("Error consultando requisitos de documentos", e);
            return formatErrorResponse(e.getMessage());
        }
    }
    
    private String getPersonaNaturalRequirements() {
        return """
            📋 **DOCUMENTOS REQUERIDOS - PERSONA NATURAL**
            
            ## 🆔 Documentos de Identificación
            ✅ Cédula de ciudadanía (original y copia legible)
            ✅ RUT actualizado (si registrado ante DIAN)
            
            ## 💼 Documentos Laborales e Ingresos
            ✅ Certificado laboral actualizado (máximo 30 días)
            ✅ Últimas 3 nóminas o comprobantes de pago
            ✅ Extractos bancarios últimos 3 meses
            ✅ Declaración de renta (si aplica por nivel de ingresos)
            
            ## 🏠 Documentos Complementarios
            ✅ Certificado de ingresos familiares (si aplica)
            ✅ Referencias comerciales (mínimo 2)
            ✅ Referencias personales (mínimo 2)
            
            ## 📞 Información de Contacto
            ✅ Comprobante de residencia (máximo 60 días)
            ✅ Información laboral verificable
            
            💡 **Nota:** Todos los documentos deben ser originales o copias autenticadas.
            """;
    }
    
    private String getPersonaJuridicaRequirements() {
        return """
            📋 **DOCUMENTOS REQUERIDOS - PERSONA JURÍDICA**
            
            ## 🏢 Documentos Legales
            ✅ Certificado de existencia y representación legal (máximo 30 días)
            ✅ RUT actualizado de la empresa
            ✅ Cédula del representante legal
            ✅ Acta de constitución de la sociedad
            
            ## 💰 Documentos Financieros
            ✅ Estados financieros auditados últimos 2 años
            ✅ Extractos bancarios empresa últimos 6 meses
            ✅ Flujo de caja proyectado
            ✅ Declaraciones de renta últimos 2 años
            ✅ Balance general y estado de resultados
            
            ## 🏭 Documentos Operacionales
            ✅ Licencias de funcionamiento vigentes
            ✅ Autorizaciones sectoriales (si aplica)
            ✅ Referencias comerciales bancarias
            ✅ Composición accionaria actualizada
            
            ## 📊 Información Adicional
            ✅ Plan de negocio o actividad económica
            ✅ Contratos principales de la empresa
            ✅ Pólizas de seguros vigentes
            
            💡 **Nota:** Documentos societarios deben estar vigentes y actualizados.
            """;
    }
    
    private String formatErrorResponse(String errorMessage) {
        return String.format("""
            ⚠️ **ERROR EN VALIDACIÓN DE DOCUMENTOS**
            
            ❌ **Error:** %s
            
            🔧 **Acciones Sugeridas:**
            - Verificar formato de los documentos enviados
            - Asegurar que todos los campos obligatorios estén completos
            - Contactar soporte técnico si el problema persiste
            """, errorMessage);
    }
}