package mx.regional.next.automotive.credit.infrastructure.mcp.tools;

import mx.regional.next.automotive.credit.application.ports.in.GetCreditStatusUseCase;
import mx.regional.next.automotive.credit.application.dto.CreditStatusResponse;
import mx.regional.next.automotive.credit.domain.enums.CreditStatus;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class GetCreditApplicationStatusTool {
    
    private static final Logger log = LoggerFactory.getLogger(GetCreditApplicationStatusTool.class);
    
    private final GetCreditStatusUseCase getCreditStatusUseCase;
    
    public GetCreditApplicationStatusTool(GetCreditStatusUseCase getCreditStatusUseCase) {
        this.getCreditStatusUseCase = getCreditStatusUseCase;
    }
    
    @Tool(name = "get_credit_application_status", 
          description = "Consulta el estado actual de una solicitud de crédito automotriz por su ID.")
    public String getCreditApplicationStatus(
            @ToolParam(description = "ID de la solicitud de crédito - OBLIGATORIO", required = true) 
            String applicationId) {
        
        try {
            log.info("Consultando estado de solicitud vía MCP: {}", applicationId);
            
            // Validar formato del ID
            if (applicationId == null || applicationId.trim().isEmpty()) {
                return "❌ **Error:** ID de solicitud requerido.";
            }
            
            UUID uuid;
            try {
                uuid = UUID.fromString(applicationId.trim());
            } catch (IllegalArgumentException e) {
                return "❌ **Error:** Formato de ID inválido. Debe ser un UUID válido.";
            }
            
            CreditStatusResponse response = getCreditStatusUseCase.getCreditStatus(uuid);
            
            return formatStatusResponse(response);
            
        } catch (Exception e) {
            log.error("Error consultando estado de solicitud vía MCP", e);
            return formatErrorResponse(e.getMessage());
        }
    }
    
    @Tool(name = "get_credit_status_by_document", 
          description = "Consulta el estado de solicitudes de crédito por número de documento del cliente.")
    public String getCreditStatusByDocument(
            @ToolParam(description = "Número de documento del cliente - OBLIGATORIO", required = true) 
            String customerDocument) {
        
        try {
            log.info("Consultando estado de solicitudes por documento vía MCP: {}", customerDocument);
            
            if (customerDocument == null || customerDocument.trim().isEmpty()) {
                return "❌ **Error:** Número de documento requerido.";
            }
            
            // En una implementación real, esto consultaría múltiples solicitudes
            var applications = getCreditStatusUseCase.getCreditStatusByCustomerDocument(customerDocument.trim());
            
            return formatMultipleStatusResponse(applications, customerDocument);
            
        } catch (Exception e) {
            log.error("Error consultando solicitudes por documento vía MCP", e);
            return formatErrorResponse(e.getMessage());
        }
    }
    
    @Tool(name = "get_credit_status_summary", 
          description = "Obtiene un resumen de todos los estados posibles de solicitudes de crédito.")
    public String getCreditStatusSummary() {
        try {
            return """
                📊 **ESTADOS DE SOLICITUDES DE CRÉDITO**
                
                ## 🔄 Estados Disponibles
                
                ### 📝 **PENDIENTE**
                - Solicitud recibida y en proceso de revisión inicial
                - Documentos siendo validados
                - Tiempo estimado: 1-2 días hábiles
                
                ### 🔍 **EN_EVALUACION**
                - Documentos validados correctamente
                - Análisis crediticio en progreso
                - Verificación de información del cliente y vehículo
                - Tiempo estimado: 2-3 días hábiles
                
                ### 👥 **EN_COMITE**
                - Evaluación completada
                - Esperando decisión del comité de crédito
                - Revisión final de riesgo y condiciones
                - Tiempo estimado: 1-2 días hábiles
                
                ### ✅ **APROBADA**
                - Solicitud aprobada por el comité
                - Condiciones de crédito definidas
                - Lista para firma de contrato
                
                ### ❌ **RECHAZADA**
                - Solicitud no cumple criterios de aprobación
                - Razones específicas proporcionadas al cliente
                - Posibilidad de nueva solicitud después de 3 meses
                
                ### 📋 **DOCUMENTACION_PENDIENTE**
                - Faltan documentos o información
                - Cliente debe completar requisitos
                - Solicitud pausada hasta completar documentación
                
                ### ⏸️ **SUSPENDIDA**
                - Proceso temporalmente detenido
                - Requiere aclaración de información
                - Puede reactivarse una vez resueltos los pendientes
                
                ### 💰 **DESEMBOLSADA**
                - Crédito aprobado y dinero entregado
                - Proceso completado exitosamente
                - Inicio del período de pagos
                
                ### 🚫 **CANCELADA**
                - Solicitud cancelada por el cliente
                - Proceso terminado sin desembolso
                
                ## 📞 Consultas Adicionales
                Para consultas específicas sobre su solicitud, use:
                - `get_credit_application_status` con el ID de solicitud
                - `get_credit_status_by_document` con su número de documento
                """;
                
        } catch (Exception e) {
            log.error("Error obteniendo resumen de estados", e);
            return formatErrorResponse(e.getMessage());
        }
    }
    
    private String formatStatusResponse(CreditStatusResponse response) {
        StringBuilder result = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        result.append("📋 **ESTADO DE SOLICITUD DE CRÉDITO**\n\n");
        result.append("🆔 **ID de Solicitud:** ").append(response.getApplicationId()).append("\n");
        result.append("👤 **Cliente:** ").append(response.getCustomerDocument()).append("\n");
        result.append("📅 **Fecha de Solicitud:** ").append(response.getApplicationDate().format(formatter)).append("\n");
        
        // Estado actual con emoji
        String statusEmoji = getStatusEmoji(response.getStatus());
        result.append("📊 **Estado Actual:** ").append(statusEmoji).append(" ").append(response.getStatus().getDisplayName()).append("\n");
        
        if (response.getLastUpdate() != null) {
            result.append("🔄 **Última Actualización:** ").append(response.getLastUpdate().format(formatter)).append("\n");
        }
        
        result.append("\n");
        
        // Información específica según el estado
        switch (response.getStatus()) {
            case APROBADA:
                result.append("🎉 **¡FELICITACIONES! CRÉDITO APROBADO**\n\n");
                if (response.getApprovedAmount() != null) {
                    result.append("💰 **Monto Aprobado:** $").append(String.format("%,.0f", response.getApprovedAmount())).append("\n");
                }
                if (response.getInterestRate() != null) {
                    result.append("📈 **Tasa de Interés:** ").append(response.getInterestRate()).append("% EA\n");
                }
                result.append("📝 **Próximos Pasos:**\n");
                result.append("1. Firma del contrato de crédito\n");
                result.append("2. Presentación de documentos originales\n");
                result.append("3. Programación de desembolso\n");
                break;
                
            case RECHAZADA:
                result.append("❌ **SOLICITUD RECHAZADA**\n\n");
                if (response.getRejectionReason() != null) {
                    result.append("📋 **Motivo:** ").append(response.getRejectionReason()).append("\n");
                }
                result.append("💡 **Opciones Disponibles:**\n");
                result.append("- Mejorar perfil crediticio y volver a aplicar en 3 meses\n");
                result.append("- Considerar un codeudor\n");
                result.append("- Evaluar un monto menor\n");
                break;
                
            case EN_EVALUACION:
                result.append("🔍 **EVALUACIÓN EN PROGRESO**\n\n");
                result.append("📋 **Proceso Actual:** Análisis crediticio y verificación de información\n");
                result.append("⏰ **Tiempo Estimado:** 2-3 días hábiles adicionales\n");
                result.append("📞 **Contacto:** Nos comunicaremos si necesitamos información adicional\n");
                break;
                
            case DOCUMENTACION_PENDIENTE:
                result.append("📄 **DOCUMENTACIÓN PENDIENTE**\n\n");
                if (response.getPendingDocuments() != null && !response.getPendingDocuments().isEmpty()) {
                    result.append("📋 **Documentos Faltantes:**\n");
                    response.getPendingDocuments().forEach(doc -> 
                        result.append("• ").append(doc).append("\n"));
                }
                result.append("⚠️ **Acción Requerida:** Enviar documentos pendientes para continuar el proceso\n");
                break;
                
            default:
                result.append("📊 **Estado:** ").append(response.getStatus().getDisplayName()).append("\n");
                result.append("⏰ **Proceso en curso** - Le mantendremos informado de cualquier novedad\n");
        }
        
        if (response.getComments() != null && !response.getComments().trim().isEmpty()) {
            result.append("\n📝 **Comentarios Adicionales:**\n");
            result.append(response.getComments()).append("\n");
        }
        
        return result.toString();
    }
    
    private String formatMultipleStatusResponse(java.util.List<CreditStatusResponse> applications, String customerDocument) {
        if (applications.isEmpty()) {
            return String.format("""
                📋 **CONSULTA DE SOLICITUDES**
                
                👤 **Cliente:** %s
                
                ℹ️ **Resultado:** No se encontraron solicitudes de crédito para este documento.
                
                💡 **¿Desea iniciar una nueva solicitud?**
                Use la herramienta `process_credit_application` para crear una nueva solicitud.
                """, customerDocument);
        }
        
        StringBuilder result = new StringBuilder();
        result.append("📋 **SOLICITUDES DE CRÉDITO ENCONTRADAS**\n\n");
        result.append("👤 **Cliente:** ").append(customerDocument).append("\n");
        result.append("📊 **Total de Solicitudes:** ").append(applications.size()).append("\n\n");
        
        for (int i = 0; i < applications.size(); i++) {
            CreditStatusResponse app = applications.get(i);
            String statusEmoji = getStatusEmoji(app.getStatus());
            
            result.append("### ").append(i + 1).append(". Solicitud ").append(app.getApplicationId()).append("\n");
            result.append("📅 **Fecha:** ").append(app.getApplicationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
            result.append("📊 **Estado:** ").append(statusEmoji).append(" ").append(app.getStatus().getDisplayName()).append("\n");
            
            if (app.getApprovedAmount() != null) {
                result.append("💰 **Monto:** $").append(String.format("%,.0f", app.getApprovedAmount())).append("\n");
            }
            
            result.append("\n");
        }
        
        result.append("💡 **Para detalles específicos** use `get_credit_application_status` con el ID de la solicitud de interés.\n");
        
        return result.toString();
    }
    
    private String getStatusEmoji(CreditStatus status) {
        return switch (status) {
            case PENDIENTE -> "🟡";
            case EN_EVALUACION -> "🔍";
            case EN_COMITE -> "👥";
            case APROBADA -> "✅";
            case RECHAZADA -> "❌";
            case DOCUMENTACION_PENDIENTE -> "📄";
            case SUSPENDIDA -> "⏸️";
            case DESEMBOLSADA -> "💰";
            case CANCELADA -> "🚫";
        };
    }
    
    private String formatErrorResponse(String errorMessage) {
        return String.format("""
            ⚠️ **ERROR EN CONSULTA DE ESTADO**
            
            ❌ **Error:** %s
            
            🔧 **Acciones Sugeridas:**
            - Verificar que el ID de solicitud sea correcto
            - Asegurar que el formato del documento sea válido
            - Contactar soporte técnico si el problema persiste
            
            📞 **Soporte:** Para ayuda adicional, contacte nuestro centro de atención al cliente
            """, errorMessage);
    }
}