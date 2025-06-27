package mx.regional.next.automotive.credit.application.services;

import com.logaritex.mcp.annotation.McpPrompt;
import com.logaritex.mcp.annotation.McpResource;
import com.logaritex.mcp.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CreditAnalysisService {

    @McpPrompt(
        name = "credit-analysis",
        description = "Analiza la viabilidad crediticia de un solicitante basado en sus datos financieros",
        arguments = {
            @McpPrompt.Argument(name = "income", type = "number", description = "Ingresos mensuales del solicitante"),
            @McpPrompt.Argument(name = "expenses", type = "number", description = "Gastos mensuales del solicitante"),
            @McpPrompt.Argument(name = "credit_history", type = "string", description = "Historial crediticio (excellent, good, fair, poor)"),
            @McpPrompt.Argument(name = "employment_years", type = "number", description = "Años en el empleo actual")
        }
    )
    public String analyzeCreditViability(double income, double expenses, String creditHistory, int employmentYears) {
        // Lógica de análisis crediticio
        double debtToIncomeRatio = expenses / income;
        
        StringBuilder analysis = new StringBuilder();
        analysis.append("=== ANÁLISIS DE VIABILIDAD CREDITICIA ===\n\n");
        analysis.append(String.format("Ingresos mensuales: $%.2f\n", income));
        analysis.append(String.format("Gastos mensuales: $%.2f\n", expenses));
        analysis.append(String.format("Ratio deuda/ingreso: %.2f%%\n", debtToIncomeRatio * 100));
        analysis.append(String.format("Historial crediticio: %s\n", creditHistory));
        analysis.append(String.format("Años de empleo: %d\n\n", employmentYears));
        
        // Evaluación de riesgo
        String riskLevel = evaluateRisk(debtToIncomeRatio, creditHistory, employmentYears);
        analysis.append(String.format("Nivel de riesgo: %s\n", riskLevel));
        
        // Recomendación
        String recommendation = generateRecommendation(riskLevel, debtToIncomeRatio);
        analysis.append(String.format("Recomendación: %s\n", recommendation));
        
        return analysis.toString();
    }

    @McpResource(
        uri = "credit://policies/automotive",
        name = "Políticas de Crédito Automotriz",
        description = "Políticas y criterios para otorgamiento de créditos automotrices"
    )
    public String getCreditPolicies() {
        return """
            === POLÍTICAS DE CRÉDITO AUTOMOTRIZ ===
            
            CRITERIOS BÁSICOS:
            - Ingresos mínimos: $15,000 MXN mensuales
            - Antigüedad laboral mínima: 12 meses
            - Ratio deuda/ingreso máximo: 35%
            - Score crediticio mínimo: 650
            
            DOCUMENTACIÓN REQUERIDA:
            - Comprobantes de ingresos (3 meses)
            - Referencias comerciales
            - Identificación oficial
            - Comprobante de domicilio
            
            TASAS DE INTERÉS:
            - Score excelente (750+): 8.5% - 10.5%
            - Score bueno (700-749): 10.5% - 12.5%
            - Score regular (650-699): 12.5% - 15.5%
            
            PLAZOS:
            - Vehículos nuevos: 12-72 meses
            - Vehículos usados: 12-60 meses
            """;
    }

    @Tool(
        name = "calculate_monthly_payment",
        description = "Calcula el pago mensual de un crédito automotriz"
    )
    public Map<String, Object> calculateMonthlyPayment(
            double loanAmount,
            double annualInterestRate,
            int termInMonths) {
        
        double monthlyRate = annualInterestRate / 100 / 12;
        double monthlyPayment = (loanAmount * monthlyRate * Math.pow(1 + monthlyRate, termInMonths)) /
                               (Math.pow(1 + monthlyRate, termInMonths) - 1);
        
        double totalPayment = monthlyPayment * termInMonths;
        double totalInterest = totalPayment - loanAmount;
        
        return Map.of(
            "monthlyPayment", Math.round(monthlyPayment * 100.0) / 100.0,
            "totalPayment", Math.round(totalPayment * 100.0) / 100.0,
            "totalInterest", Math.round(totalInterest * 100.0) / 100.0,
            "loanAmount", loanAmount,
            "interestRate", annualInterestRate,
            "termMonths", termInMonths
        );
    }

    private String evaluateRisk(double debtToIncomeRatio, String creditHistory, int employmentYears) {
        int riskScore = 0;
        
        // Evaluar ratio deuda/ingreso
        if (debtToIncomeRatio > 0.5) riskScore += 3;
        else if (debtToIncomeRatio > 0.35) riskScore += 2;
        else if (debtToIncomeRatio > 0.25) riskScore += 1;
        
        // Evaluar historial crediticio
        switch (creditHistory.toLowerCase()) {
            case "poor" -> riskScore += 3;
            case "fair" -> riskScore += 2;
            case "good" -> riskScore += 1;
            case "excellent" -> riskScore += 0;
        }
        
        // Evaluar años de empleo
        if (employmentYears < 1) riskScore += 2;
        else if (employmentYears < 2) riskScore += 1;
        
        // Determinar nivel de riesgo
        if (riskScore >= 6) return "ALTO";
        else if (riskScore >= 3) return "MEDIO";
        else return "BAJO";
    }

    private String generateRecommendation(String riskLevel, double debtToIncomeRatio) {
        return switch (riskLevel) {
            case "BAJO" -> "APROBADO - Cliente cumple con todos los criterios de bajo riesgo";
            case "MEDIO" -> "CONDICIONAL - Revisar garantías adicionales o ajustar términos del crédito";
            case "ALTO" -> "RECHAZADO - Cliente no cumple con los criterios mínimos de riesgo";
            default -> "PENDIENTE - Requiere análisis adicional";
        };
    }
}
