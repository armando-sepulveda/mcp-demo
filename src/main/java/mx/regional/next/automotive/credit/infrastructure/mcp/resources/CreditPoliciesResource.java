package mx.regional.next.automotive.credit.infrastructure.mcp.resources;

import com.logaritex.mcp.annotation.McpResource;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class CreditPoliciesResource {
    
    private static final Logger log = LoggerFactory.getLogger(CreditPoliciesResource.class);
    
    @McpResource(
        uri = "credit://policies",
        name = "Credit Policies", 
        description = "Políticas y criterios de evaluación crediticia para créditos automotrices"
    )
    public String getCreditPolicies() {
        return """
            # POLÍTICAS DE CRÉDITO AUTOMOTRIZ - BANCO XYZ
            
            ## 📋 CRITERIOS DE ELEGIBILIDAD
            
            ### 👤 Cliente
            - ✅ Edad mínima: 18 años, máxima: 70 años al final del crédito
            - ✅ Ingresos mínimos: $2,500,000 pesos mensuales demostrables
            - ✅ Antigüedad laboral: Mínimo 12 meses (empleados) o 24 meses (independientes)
            - ✅ Score crediticio mínimo: 600 puntos
            - ✅ Capacidad de pago: Máximo 30% de ingresos para cuota del crédito
            - ✅ Ratio deuda/ingreso total: Máximo 40%
            
            ### 🚗 Vehículo
            - ✅ Año mínimo: 2018 (máximo 6 años de antigüedad)
            - ✅ Kilometraje máximo: 100,000 km
            - ✅ Marcas autorizadas: Toyota, Chevrolet, Renault, Nissan, Hyundai, KIA, Mazda, Ford
            - ✅ Valor mínimo: $50,000,000 pesos
            - ✅ Valor máximo: $300,000,000 pesos
            - ✅ Debe tener SOAT vigente y revisión técnico-mecánica
            
            ## 💰 CONDICIONES CREDITICIAS
            
            ### Montos y Plazos
            - 💵 Monto mínimo: $20,000,000
            - 💵 Monto máximo: $200,000,000
            - 💵 Financiación máxima: 90% del valor del vehículo
            - ⏱️ Plazo mínimo: 12 meses
            - ⏱️ Plazo máximo: 84 meses
            
            ### Tasas de Interés (EA)
            - 📊 Score 750+: 12% - 14%
            - 📊 Score 700-749: 14% - 16%
            - 📊 Score 650-699: 16% - 18%
            - 📊 Score 600-649: 18% - 20%
            
            ## 📄 DOCUMENTOS REQUERIDOS
            
            ### Personas Naturales
            - 🆔 Cédula de ciudadanía (original y copia)
            - 💼 Certificado laboral (no mayor a 30 días)
            - 💰 Últimas 3 nóminas o certificado de ingresos
            - 🏦 Extractos bancarios últimos 3 meses
            - 📋 Declaración de renta (si aplica)
            - 🏠 Certificado de ingresos familiares (si aplica)
            
            ### Documentos del Vehículo
            - 📜 Tarjeta de propiedad
            - 🔍 Tecnomecánica vigente
            - 🛡️ SOAT vigente
            - 💵 Avalúo comercial (no mayor a 15 días)
            - 📋 Inspección técnica del banco
            
            ## ⚠️ CRITERIOS DE EXCLUSIÓN
            
            - ❌ Reportes negativos en centrales de riesgo por mora superior a 60 días
            - ❌ Procesos judiciales o embargo vigentes
            - ❌ Antecedentes de fraude o lavado de activos
            - ❌ Vinculación en listas de control (OFAC, ONU, etc.)
            - ❌ Vehículos con problemas legales o prendas vigentes
            - ❌ Actividades económicas de alto riesgo no autorizadas
            
            ## 📈 PROCESO DE EVALUACIÓN
            
            1. **Validación de Documentos** (1-2 días)
            2. **Evaluación Crediticia** (2-3 días)
            3. **Inspección del Vehículo** (1 día)
            4. **Comité de Crédito** (1-2 días)
            5. **Aprobación Final** (1 día)
            
            **Tiempo total estimado: 5-8 días hábiles**
            """;
    }
    
    @McpResource(
        uri = "credit://interest-rates",
        name = "Interest Rates", 
        description = "Tabla actualizada de tasas de interés según score crediticio"
    )
    public String getInterestRates() {
        return """
            # 📊 TABLA DE TASAS DE INTERÉS CRÉDITO AUTOMOTRIZ
            
            ## Tasas Efectivas Anuales Vigentes
            
            | Score Crediticio | Tasa Mínima | Tasa Máxima | Promedio |
            |------------------|-------------|-------------|----------|
            | 800+ (Excelente) | 10.5%       | 12.0%       | 11.25%   |
            | 750-799 (Muy Bueno) | 12.0%   | 14.0%       | 13.0%    |
            | 700-749 (Bueno)  | 14.0%      | 16.0%       | 15.0%    |
            | 650-699 (Regular) | 16.0%     | 18.0%       | 17.0%    |
            | 600-649 (Mínimo) | 18.0%      | 20.0%       | 19.0%    |
            
            ## Factores que Afectan la Tasa
            
            ### 📈 Factores que MEJORAN la tasa:
            - Cliente con productos existentes en el banco
            - Antigüedad laboral superior a 3 años
            - Ingresos superiores a $5,000,000
            - Pago de cuota inicial superior al 20%
            - Vehículo nuevo o seminuevo (menos de 2 años)
            
            ### 📉 Factores que INCREMENTAN la tasa:
            - Primer crédito con el banco
            - Ingresos variables o independientes
            - Vehículo usado (más de 3 años)
            - Financiación superior al 80% del valor
            - Score en el rango mínimo aceptable
            
            *Última actualización: Enero 2024*
            *Tasas sujetas a cambios según condiciones del mercado*
            """;
    }
    
    @McpResource(
        uri = "credit://documents/{document_type}",
        name = "Required Documents", 
        description = "Lista de documentos requeridos por tipo de cliente"
    )
    public String getRequiredDocuments(String documentType) {
        log.debug("Obteniendo documentos requeridos para tipo: {}", documentType);
        
        return switch (documentType.toLowerCase()) {
            case "natural" -> getPersonaNaturalDocuments();
            case "juridica" -> getPersonaJuridicaDocuments();
            case "vehiculo" -> getVehicleDocuments();
            default -> getAllRequiredDocuments();
        };
    }
    
    @McpResource(
        uri = "credit://eligibility-criteria",
        name = "Eligibility Criteria", 
        description = "Criterios detallados de elegibilidad crediticia"
    )
    public List<String> getEligibilityCriteria() {
        return List.of(
            "Edad mínima: 18 años, máxima: 70 años al final del crédito",
            "Ingresos mínimos: $300,000 pesos mensuales demostrables",
            "Antigüedad laboral: Mínimo 12 meses (empleados) o 24 meses (independientes)",
            "Score crediticio mínimo: 600 puntos",
            "Capacidad de pago: Máximo 30% de ingresos para cuota del crédito",
            "Ratio deuda/ingreso total: Máximo 40%",
            "Vehículo año mínimo: 2018 (máximo 6 años de antigüedad)",
            "Kilometraje máximo del vehículo: 100,000 km",
            "Marcas autorizadas: Toyota, Chevrolet, Renault, Nissan, Hyundai, KIA, Mazda, Ford"
        );
    }
    
    @McpResource(
        uri = "credit://resource/{resource_name}",
        name = "Dynamic Credit Resource", 
        description = "Acceso dinámico a recursos de crédito específicos"
    )
    public String getDynamicResource(String resourceName) {
        log.info("Accediendo al recurso dinámico: {} desde exchange", resourceName);
        
        return switch (resourceName.toLowerCase()) {
            case "policies" -> getCreditPolicies();
            case "rates" -> getInterestRates();
            case "documents" -> getAllRequiredDocuments();
            case "criteria" -> String.join("\n• ", getEligibilityCriteria());
            default -> "Recurso no encontrado: " + resourceName + 
                      "\nRecursos disponibles: policies, rates, documents, criteria";
        };
    }
    
    private String getPersonaNaturalDocuments() {
        return """
            # 📄 DOCUMENTOS REQUERIDOS - PERSONA NATURAL
            
            ## Documentos de Identificación
            - 🆔 Cédula de ciudadanía (original y copia)
            - 📋 RUT actualizado (si aplica)
            
            ## Documentos Laborales e Ingresos
            - 💼 Certificado laboral (no mayor a 30 días)
            - 💰 Últimas 3 nóminas o certificado de ingresos
            - 🏦 Extractos bancarios últimos 3 meses
            - 📋 Declaración de renta (si aplica)
            
            ## Documentos Adicionales
            - 🏠 Certificado de ingresos familiares (si aplica)
            - 📞 Referencias comerciales y personales
            """;
    }
    
    private String getPersonaJuridicaDocuments() {
        return """
            # 📄 DOCUMENTOS REQUERIDOS - PERSONA JURÍDICA
            
            ## Documentos Legales
            - 🏢 Certificado de existencia y representación legal (no mayor a 30 días)
            - 📋 RUT actualizado
            - 🆔 Cédula del representante legal
            
            ## Documentos Financieros
            - 💰 Estados financieros últimos 2 años
            - 🏦 Extractos bancarios últimos 6 meses
            - 📊 Flujo de caja proyectado
            - 📋 Declaración de renta últimos 2 años
            
            ## Documentos Adicionales
            - 📞 Referencias comerciales
            - 🏭 Autorizaciones y licencias de funcionamiento
            """;
    }
    
    private String getVehicleDocuments() {
        return """
            # 📄 DOCUMENTOS DEL VEHÍCULO
            
            ## Documentos Legales
            - 📜 Tarjeta de propiedad original
            - 🔍 Tecnomecánica vigente
            - 🛡️ SOAT vigente
            - 📋 Certificado de tradición del vehículo
            
            ## Documentos de Evaluación
            - 💵 Avalúo comercial (no mayor a 15 días)
            - 🔧 Inspección técnica del banco
            - 📸 Fotografías del vehículo (exterior e interior)
            
            ## Verificaciones
            - ✅ Verificación de no reporte por robo
            - ✅ Verificación de prendas vigentes
            """;
    }
    
    private String getAllRequiredDocuments() {
        return """
            # 📄 DOCUMENTOS REQUERIDOS COMPLETOS
            
            Para obtener documentos específicos use:
            - credit://documents/natural - Persona Natural
            - credit://documents/juridica - Persona Jurídica  
            - credit://documents/vehiculo - Documentos del Vehículo
            
            O consulte cada categoría individualmente.
            """;
    }
}