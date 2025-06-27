package mx.regional.next.automotive.credit.infrastructure.mcp.tools;

import mx.regional.next.automotive.credit.application.ports.in.ProcessCreditApplicationUseCase;
import mx.regional.next.automotive.credit.application.dto.*;
import mx.regional.next.automotive.credit.infrastructure.mcp.mappers.CreditApplicationMcpMapper;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class ProcessCreditApplicationTool {
    
    private static final Logger log = LoggerFactory.getLogger(ProcessCreditApplicationTool.class);
    
    private final ProcessCreditApplicationUseCase processCreditApplicationUseCase;
    private final CreditApplicationMcpMapper mapper;
    
    public ProcessCreditApplicationTool(
            ProcessCreditApplicationUseCase processCreditApplicationUseCase,
            CreditApplicationMcpMapper mapper) {
        this.processCreditApplicationUseCase = processCreditApplicationUseCase;
        this.mapper = mapper;
    }
    
    @Tool(name = "process_credit_application", 
          description = "Procesa una nueva solicitud de crédito automotriz. Evalúa la elegibilidad del cliente, valida documentos, verifica el vehículo y toma una decisión crediticia.")
    public String processApplication(
            @ToolParam(description = "Número de documento del cliente (cédula, NIT, etc.) - OBLIGATORIO", required = true) 
            String customerDocument,
            
            @ToolParam(description = "Monto del crédito solicitado en pesos colombianos - OBLIGATORIO", required = true) 
            String requestedAmount,
            
            @ToolParam(description = "VIN del vehículo (17 caracteres) - OBLIGATORIO", required = true) 
            String vehicleVin,
            
            @ToolParam(description = "Marca del vehículo (Toyota, Chevrolet, etc.) - OBLIGATORIO", required = true) 
            String vehicleBrand,
            
            @ToolParam(description = "Modelo del vehículo - OBLIGATORIO", required = true) 
            String vehicleModel,
            
            @ToolParam(description = "Año del vehículo (2018 o posterior) - OBLIGATORIO", required = true) 
            String vehicleYear,
            
            @ToolParam(description = "Valor comercial del vehículo en pesos colombianos - OBLIGATORIO", required = true) 
            String vehicleValue,
            
            @ToolParam(description = "Kilometraje del vehículo (máximo 100,000 km) - OBLIGATORIO", required = true) 
            String vehicleKilometers,
            
            @ToolParam(description = "Documentos del cliente en formato JSON (opcional para simulación inicial)", required = false) 
            String documentsJson) {
        
        try {
            log.info("Procesando solicitud de crédito vía MCP para cliente: {}", customerDocument);
            
            // Mapear parámetros a DTO de aplicación
            CreditApplicationRequest request = mapper.mapToApplicationRequest(
                customerDocument, requestedAmount, vehicleVin, vehicleBrand, 
                vehicleModel, vehicleYear, vehicleValue, vehicleKilometers, documentsJson
            );
            
            // Procesar aplicación
            CreditApplicationResponse response = processCreditApplicationUseCase.processApplication(request);
            
            // Formatear respuesta para el agente de IA
            return formatResponseForAgent(response);
            
        } catch (Exception e) {
            log.error("Error procesando solicitud de crédito vía MCP", e);
            return formatErrorResponse(e.getMessage());
        }
    }
    
    private String formatResponseForAgent(CreditApplicationResponse response) {
        StringBuilder result = new StringBuilder();
        
        result.append("📋 **RESULTADO DE SOLICITUD DE CRÉDITO AUTOMOTRIZ**\\n\\n");
        result.append("🆔 **ID de Solicitud:** ").append(response.getApplicationId()).append("\\n");
        result.append("📊 **Estado:** ").append(response.getStatus().getDisplayName()).append("\\n");
        
        if (response.isApproved()) {
            result.append("✅ **SOLICITUD APROBADA**\\n\\n");
            result.append("💰 **Monto Aprobado:** $").append(formatCurrency(response.getApprovedAmount())).append("\\n");
            result.append("📈 **Score Crediticio:** ").append(response.getCreditScore()).append("\\n");
            result.append("⏰ **Plazo Máximo:** 60 meses\\n");
            result.append("🎯 **Tasa de Interés:** ").append(formatPercentage(response.getInterestRate())).append("% anual\\n");
            result.append("\\n📝 **Próximos Pasos:**\\n");
            result.append("1. El cliente debe firmar el contrato de crédito\\n");
            result.append("2. Presentar los documentos originales para verificación\\n");
            result.append("3. Programar cita para desembolso\\n");
            
        } else {
            result.append("❌ **SOLICITUD RECHAZADA**\\n\\n");
            result.append("📋 **Motivo:** ").append(response.getRejectionReason()).append("\\n");
            result.append("\\n💡 **Recomendaciones:**\\n");
            result.append("- Revisar historial crediticio\\n");
            result.append("- Considerar un monto menor\\n");
            result.append("- Añadir un codeudor si es necesario\\n");
        }
        
        return result.toString();
    }
    
    @Tool(name = "calculate_monthly_installment", 
          description = "Calcula la cuota mensual estimada de un crédito automotriz basado en monto, tasa y plazo.")
    public String calculateMonthlyInstallment(
            @ToolParam(description = "Monto del crédito en pesos colombianos - OBLIGATORIO", required = true) 
            String loanAmount,
            
            @ToolParam(description = "Tasa de interés anual en porcentaje (ej: 15.5) - OBLIGATORIO", required = true) 
            String annualInterestRate,
            
            @ToolParam(description = "Plazo en meses (12-84) - OBLIGATORIO", required = true) 
            String termInMonths,
            
            @ToolParam(description = "Tipo de cliente para aplicar descuentos (natural/juridica)", required = false) 
            String customerType) {
        
        try {
            log.info("Calculando cuota mensual vía MCP: monto={}, tasa={}, plazo={}, tipo={}", 
                     loanAmount, annualInterestRate, termInMonths, customerType);
            
            // Convertir parámetros
            BigDecimal amount = new BigDecimal(loanAmount);
            BigDecimal rate = new BigDecimal(annualInterestRate).divide(BigDecimal.valueOf(100), 6, BigDecimal.ROUND_HALF_UP);
            int months = Integer.parseInt(termInMonths);
            
            // Validaciones básicas
            if (amount.compareTo(BigDecimal.valueOf(50000)) < 0) {
                return "❌ **Error:** El monto mínimo del crédito es $50,000";
            }
            if (amount.compareTo(BigDecimal.valueOf(2000000000)) > 0) {
                return "❌ **Error:** El monto máximo del crédito es $2,000,000,000";
            }
            if (months < 12 || months > 84) {
                return "❌ **Error:** El plazo debe estar entre 12 y 84 meses";
            }
            if (rate.compareTo(BigDecimal.valueOf(0.05)) < 0 || rate.compareTo(BigDecimal.valueOf(0.35)) > 0) {
                return "❌ **Error:** La tasa de interés debe estar entre 5% y 35% anual";
            }
            
            // Aplicar descuento por tipo de cliente (opcional)
            BigDecimal finalRate = rate;
            String discountInfo = "";
            if (customerType != null && !customerType.trim().isEmpty()) {
                if ("juridica".equalsIgnoreCase(customerType.trim())) {
                    finalRate = rate.multiply(BigDecimal.valueOf(0.95)); // 5% descuento para personas jurídicas
                    discountInfo = "\n🎁 **Descuento aplicado:** 5% para persona jurídica";
                }
            }
            
            // Cálculo de cuota mensual usando fórmula de amortización
            BigDecimal monthlyRate = finalRate.divide(BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_UP);
            BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
            BigDecimal onePlusRPowN = onePlusR.pow(months);
            
            BigDecimal numerator = amount.multiply(monthlyRate).multiply(onePlusRPowN);
            BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);
            BigDecimal monthlyPayment = numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
            
            BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(months));
            BigDecimal totalInterest = totalPayment.subtract(amount);
            
            return String.format("""
                💰 **SIMULACIÓN DE CUOTA MENSUAL**
                
                📊 **Datos del Crédito:**
                • Monto solicitado: %s
                • Tasa de interés: %.2f%% EA%s
                • Plazo: %d meses (%.1f años)
                
                💵 **Resultado de Simulación:**
                • **Cuota mensual: %s**
                • Total a pagar: %s
                • Total intereses: %s
                
                📈 **Información Adicional:**
                • Costo financiero: %.1f%% del monto
                • Capacidad de pago requerida: %s mensual mínimo
                
                ⚠️ *Simulación referencial. Tasas sujetas a aprobación crediticia final.*
                """,
                formatCurrency(amount),
                finalRate.multiply(BigDecimal.valueOf(100)),
                discountInfo,
                months,
                months / 12.0,
                formatCurrency(monthlyPayment),
                formatCurrency(totalPayment),
                formatCurrency(totalInterest),
                totalInterest.divide(amount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)),
                formatCurrency(monthlyPayment.divide(BigDecimal.valueOf(0.30), 0, BigDecimal.ROUND_UP))
            );
            
        } catch (NumberFormatException e) {
            log.error("Error de formato en parámetros numéricos", e);
            return "❌ **Error:** Formato inválido en los números. Verifique que el monto, tasa y plazo sean números válidos.";
        } catch (Exception e) {
            log.error("Error calculando cuota mensual vía MCP", e);
            return "❌ **Error inesperado:** " + e.getMessage();
        }
    }
    
    private String formatErrorResponse(String errorMessage) {
        return String.format("""
            ⚠️ **ERROR EN PROCESAMIENTO**
            
            ❌ **Error:** %s
            
            🔧 **Acción Requerida:**
            - Verificar que todos los datos estén correctos
            - Contactar soporte técnico si el problema persiste
            """, errorMessage);
    }
    
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "N/A";
        return NumberFormat.getCurrencyInstance(new Locale("es", "CO")).format(amount);
    }
    
    private String formatPercentage(BigDecimal percentage) {
        if (percentage == null) return "N/A";
        return percentage.multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }
}