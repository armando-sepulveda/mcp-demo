package mx.regional.next.automotive.credit.domain.services;

import mx.regional.next.automotive.credit.domain.entities.CreditApplication;
import mx.regional.next.automotive.credit.domain.entities.Customer;
import mx.regional.next.automotive.credit.domain.entities.Vehicle;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditAmount;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditScore;
import mx.regional.next.automotive.credit.domain.enums.CreditStatus;
import mx.regional.next.automotive.credit.shared.common.annotations.DomainService;

import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;

@DomainService
@Slf4j
public class RiskCalculationService {
    
    private static final BigDecimal MINIMUM_SCORE = BigDecimal.valueOf(600);
    private static final BigDecimal EXCELLENT_SCORE = BigDecimal.valueOf(750);
    private static final BigDecimal MAX_DEBT_TO_INCOME_RATIO = BigDecimal.valueOf(0.40);
    private static final BigDecimal MAX_INSTALLMENT_TO_INCOME_RATIO = BigDecimal.valueOf(0.30);
    private static final int MAX_VEHICLE_AGE_YEARS = 6;
    private static final int MAX_VEHICLE_KILOMETERS = 100000;
    
    public RiskAssessment calculateRisk(CreditApplication application, CreditScore creditScore) {
        log.info("Calculando riesgo para solicitud: {}", application.getId().getValue());
        
        RiskAssessment assessment = new RiskAssessment();
        
        // Evaluación de score crediticio
        assessment.addFactor(evaluateCreditScore(creditScore));
        
        // Evaluación de capacidad de pago
        assessment.addFactor(evaluatePaymentCapacity(application));
        
        // Evaluación del vehículo
        assessment.addFactor(evaluateVehicleRisk(application.getVehicle()));
        
        // Evaluación de relación deuda/ingreso
        assessment.addFactor(evaluateDebtToIncomeRatio(application.getCustomer()));
        
        // Evaluación de antigüedad laboral
        assessment.addFactor(evaluateEmploymentStability(application.getCustomer()));
        
        // Evaluación de historial crediticio
        assessment.addFactor(evaluateCreditHistory(application.getCustomer()));
        
        // Calcular score de riesgo final
        assessment.calculateFinalRisk();
        
        log.info("Riesgo calculado para solicitud {}: Score={}, Decisión={}", 
                 application.getId().getValue(), 
                 assessment.getRiskScore(), 
                 assessment.isApproved() ? "APROBADO" : "RECHAZADO");
        
        return assessment;
    }
    
    public BigDecimal calculateRecommendedInterestRate(RiskAssessment riskAssessment, CreditScore creditScore) {
        log.debug("Calculando tasa de interés recomendada para score: {}", creditScore.getValue());
        
        BigDecimal baseRate = getBaseRateByScore(creditScore);
        BigDecimal riskAdjustment = calculateRiskAdjustment(riskAssessment);
        
        BigDecimal finalRate = baseRate.add(riskAdjustment);
        
        // Aplicar límites mínimos y máximos
        BigDecimal minRate = BigDecimal.valueOf(0.12); // 12% mínimo
        BigDecimal maxRate = BigDecimal.valueOf(0.25); // 25% máximo
        
        if (finalRate.compareTo(minRate) < 0) {
            finalRate = minRate;
        } else if (finalRate.compareTo(maxRate) > 0) {
            finalRate = maxRate;
        }
        
        return finalRate.setScale(4, RoundingMode.HALF_UP);
    }
    
    private RiskFactor evaluateCreditScore(CreditScore creditScore) {
        BigDecimal score = BigDecimal.valueOf(creditScore.getValue());
        
        if (score.compareTo(EXCELLENT_SCORE) >= 0) {
            return new RiskFactor("CREDIT_SCORE", RiskLevel.LOW, 85, 
                "Score crediticio excelente (≥750)");
        } else if (score.compareTo(BigDecimal.valueOf(700)) >= 0) {
            return new RiskFactor("CREDIT_SCORE", RiskLevel.LOW, 75, 
                "Score crediticio muy bueno (700-749)");
        } else if (score.compareTo(BigDecimal.valueOf(650)) >= 0) {
            return new RiskFactor("CREDIT_SCORE", RiskLevel.MEDIUM, 60, 
                "Score crediticio bueno (650-699)");
        } else if (score.compareTo(MINIMUM_SCORE) >= 0) {
            return new RiskFactor("CREDIT_SCORE", RiskLevel.MEDIUM, 45, 
                "Score crediticio aceptable (600-649)");
        } else {
            return new RiskFactor("CREDIT_SCORE", RiskLevel.HIGH, 20, 
                "Score crediticio insuficiente (<600)");
        }
    }
    
    private RiskFactor evaluatePaymentCapacity(CreditApplication application) {
        Customer customer = application.getCustomer();
        BigDecimal monthlyIncome = customer.getMonthlyIncome().getValue();
        
        // Calcular cuota estimada (usando 60 meses y tasa promedio del 18%)
        BigDecimal estimatedRate = BigDecimal.valueOf(0.18);
        int termMonths = 60;
        BigDecimal loanAmount = application.getRequestedAmount().getValue();
        
        BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, estimatedRate, termMonths);
        BigDecimal paymentToIncomeRatio = monthlyPayment.divide(monthlyIncome, 4, RoundingMode.HALF_UP);
        
        if (paymentToIncomeRatio.compareTo(BigDecimal.valueOf(0.20)) <= 0) {
            return new RiskFactor("PAYMENT_CAPACITY", RiskLevel.LOW, 90, 
                String.format("Excelente capacidad de pago (%.1f%% del ingreso)", 
                             paymentToIncomeRatio.multiply(BigDecimal.valueOf(100))));
        } else if (paymentToIncomeRatio.compareTo(BigDecimal.valueOf(0.25)) <= 0) {
            return new RiskFactor("PAYMENT_CAPACITY", RiskLevel.LOW, 80, 
                String.format("Buena capacidad de pago (%.1f%% del ingreso)", 
                             paymentToIncomeRatio.multiply(BigDecimal.valueOf(100))));
        } else if (paymentToIncomeRatio.compareTo(MAX_INSTALLMENT_TO_INCOME_RATIO) <= 0) {
            return new RiskFactor("PAYMENT_CAPACITY", RiskLevel.MEDIUM, 65, 
                String.format("Capacidad de pago aceptable (%.1f%% del ingreso)", 
                             paymentToIncomeRatio.multiply(BigDecimal.valueOf(100))));
        } else {
            return new RiskFactor("PAYMENT_CAPACITY", RiskLevel.HIGH, 30, 
                String.format("Capacidad de pago insuficiente (%.1f%% del ingreso)", 
                             paymentToIncomeRatio.multiply(BigDecimal.valueOf(100))));
        }
    }
    
    private RiskFactor evaluateVehicleRisk(Vehicle vehicle) {
        int currentYear = Year.now().getValue();
        int vehicleAge = currentYear - vehicle.getYear();
        
        int score = 70; // Base score
        StringBuilder details = new StringBuilder();
        
        // Evaluar antigüedad
        if (vehicleAge <= 2) {
            score += 15;
            details.append("Vehículo nuevo/seminuevo. ");
        } else if (vehicleAge <= 4) {
            score += 5;
            details.append("Vehículo reciente. ");
        } else if (vehicleAge <= MAX_VEHICLE_AGE_YEARS) {
            score -= 5;
            details.append("Vehículo usado. ");
        } else {
            score -= 20;
            details.append("Vehículo muy antiguo. ");
        }
        
        // Evaluar kilometraje
        if (vehicle.getKilometers() <= 50000) {
            score += 10;
            details.append("Bajo kilometraje. ");
        } else if (vehicle.getKilometers() <= 80000) {
            score += 5;
            details.append("Kilometraje moderado. ");
        } else if (vehicle.getKilometers() <= MAX_VEHICLE_KILOMETERS) {
            score -= 5;
            details.append("Alto kilometraje. ");
        } else {
            score -= 15;
            details.append("Kilometraje excesivo. ");
        }
        
        // Evaluar valor vs préstamo
        BigDecimal loanToValueRatio = calculateLoanToValueRatio(vehicle);
        if (loanToValueRatio.compareTo(BigDecimal.valueOf(0.70)) <= 0) {
            score += 10;
            details.append("Baja relación préstamo/valor.");
        } else if (loanToValueRatio.compareTo(BigDecimal.valueOf(0.85)) <= 0) {
            score += 5;
            details.append("Relación préstamo/valor moderada.");
        } else {
            score -= 10;
            details.append("Alta relación préstamo/valor.");
        }
        
        RiskLevel level = score >= 80 ? RiskLevel.LOW : 
                         score >= 60 ? RiskLevel.MEDIUM : RiskLevel.HIGH;
        
        return new RiskFactor("VEHICLE_RISK", level, score, details.toString());
    }
    
    private RiskFactor evaluateDebtToIncomeRatio(Customer customer) {
        BigDecimal monthlyIncome = customer.getMonthlyIncome().getValue();
        BigDecimal currentDebts = BigDecimal.ZERO; // TODO: Implementar getCurrentMonthlyDebts()
        
        BigDecimal debtToIncomeRatio = currentDebts.divide(monthlyIncome, 4, RoundingMode.HALF_UP);
        
        if (debtToIncomeRatio.compareTo(BigDecimal.valueOf(0.20)) <= 0) {
            return new RiskFactor("DEBT_TO_INCOME", RiskLevel.LOW, 90, 
                String.format("Baja relación deuda/ingreso (%.1f%%)", 
                             debtToIncomeRatio.multiply(BigDecimal.valueOf(100))));
        } else if (debtToIncomeRatio.compareTo(BigDecimal.valueOf(0.30)) <= 0) {
            return new RiskFactor("DEBT_TO_INCOME", RiskLevel.MEDIUM, 70, 
                String.format("Relación deuda/ingreso moderada (%.1f%%)", 
                             debtToIncomeRatio.multiply(BigDecimal.valueOf(100))));
        } else if (debtToIncomeRatio.compareTo(MAX_DEBT_TO_INCOME_RATIO) <= 0) {
            return new RiskFactor("DEBT_TO_INCOME", RiskLevel.MEDIUM, 50, 
                String.format("Relación deuda/ingreso alta (%.1f%%)", 
                             debtToIncomeRatio.multiply(BigDecimal.valueOf(100))));
        } else {
            return new RiskFactor("DEBT_TO_INCOME", RiskLevel.HIGH, 25, 
                String.format("Relación deuda/ingreso excesiva (%.1f%%)", 
                             debtToIncomeRatio.multiply(BigDecimal.valueOf(100))));
        }
    }
    
    private RiskFactor evaluateEmploymentStability(Customer customer) {
        // TODO: Implementar evaluación de estabilidad laboral
        return new RiskFactor("EMPLOYMENT_STABILITY", RiskLevel.MEDIUM, 70, 
            "Estabilidad laboral a evaluar con documentación");
    }
    
    private RiskFactor evaluateCreditHistory(Customer customer) {
        // TODO: Implementar evaluación de historial crediticio
        return new RiskFactor("CREDIT_HISTORY", RiskLevel.MEDIUM, 70, 
            "Historial crediticio a evaluar con centrales de riesgo");
    }
    
    private BigDecimal getBaseRateByScore(CreditScore creditScore) {
        int score = creditScore.getValue();
        
        if (score >= 750) {
            return BigDecimal.valueOf(0.12); // 12%
        } else if (score >= 700) {
            return BigDecimal.valueOf(0.14); // 14%
        } else if (score >= 650) {
            return BigDecimal.valueOf(0.16); // 16%
        } else if (score >= 600) {
            return BigDecimal.valueOf(0.18); // 18%
        } else {
            return BigDecimal.valueOf(0.22); // 22%
        }
    }
    
    private BigDecimal calculateRiskAdjustment(RiskAssessment riskAssessment) {
        BigDecimal adjustment = BigDecimal.ZERO;
        
        switch (riskAssessment.getOverallRiskLevel()) {
            case LOW:
                adjustment = BigDecimal.valueOf(-0.01); // -1% por bajo riesgo
                break;
            case MEDIUM:
                adjustment = BigDecimal.ZERO; // Sin ajuste
                break;
            case HIGH:
                adjustment = BigDecimal.valueOf(0.02); // +2% por alto riesgo
                break;
        }
        
        return adjustment;
    }
    
    private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal annualRate, int termMonths) {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(termMonths);
        
        BigDecimal numerator = loanAmount.multiply(monthlyRate).multiply(onePlusRPowN);
        BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateLoanToValueRatio(Vehicle vehicle) {
        // TODO: Implementar cálculo basado en el monto solicitado vs valor del vehículo
        return BigDecimal.valueOf(0.80); // Placeholder
    }
    
    // Clases internas para el assessment de riesgo
    public static class RiskAssessment {
        private final java.util.List<RiskFactor> factors = new java.util.ArrayList<>();
        private BigDecimal riskScore;
        private RiskLevel overallRiskLevel;
        private boolean approved;
        
        public void addFactor(RiskFactor factor) {
            factors.add(factor);
        }
        
        public void calculateFinalRisk() {
            if (factors.isEmpty()) {
                riskScore = BigDecimal.ZERO;
                overallRiskLevel = RiskLevel.HIGH;
                approved = false;
                return;
            }
            
            // Calcular score promedio ponderado
            BigDecimal totalScore = factors.stream()
                .map(factor -> BigDecimal.valueOf(factor.score))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            riskScore = totalScore.divide(BigDecimal.valueOf(factors.size()), 2, RoundingMode.HALF_UP);
            
            // Determinar nivel de riesgo general
            if (riskScore.compareTo(BigDecimal.valueOf(75)) >= 0) {
                overallRiskLevel = RiskLevel.LOW;
                approved = true;
            } else if (riskScore.compareTo(BigDecimal.valueOf(60)) >= 0) {
                overallRiskLevel = RiskLevel.MEDIUM;
                approved = true;
            } else {
                overallRiskLevel = RiskLevel.HIGH;
                approved = false;
            }
            
            // Verificar que no haya factores críticos de alto riesgo
            long highRiskFactors = factors.stream()
                .filter(factor -> factor.level == RiskLevel.HIGH)
                .count();
            
            if (highRiskFactors >= 2) {
                approved = false;
                overallRiskLevel = RiskLevel.HIGH;
            }
        }
        
        // Getters
        public java.util.List<RiskFactor> getFactors() { return factors; }
        public BigDecimal getRiskScore() { return riskScore; }
        public RiskLevel getOverallRiskLevel() { return overallRiskLevel; }
        public boolean isApproved() { return approved; }
    }
    
    public static class RiskFactor {
        private final String category;
        private final RiskLevel level;
        private final int score;
        private final String details;
        
        public RiskFactor(String category, RiskLevel level, int score, String details) {
            this.category = category;
            this.level = level;
            this.score = score;
            this.details = details;
        }
        
        // Getters
        public String getCategory() { return category; }
        public RiskLevel getLevel() { return level; }
        public int getScore() { return score; }
        public String getDetails() { return details; }
    }
    
    public enum RiskLevel {
        LOW, MEDIUM, HIGH
    }
}