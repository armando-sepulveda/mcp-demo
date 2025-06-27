package mx.regional.next.automotive.credit.domain.services;

import mx.regional.next.automotive.credit.domain.entities.CreditApplication;
import mx.regional.next.automotive.credit.domain.entities.Customer;
import mx.regional.next.automotive.credit.domain.entities.Vehicle;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditAmount;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditScore;
import mx.regional.next.automotive.credit.shared.common.annotations.DomainService;

import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.Map;
import java.util.HashMap;

@DomainService
@Slf4j
public class InterestRateCalculationService {
    
    // Tasas base por rango de score crediticio
    private static final Map<String, BigDecimal> BASE_RATES = Map.of(
        "EXCELLENT", BigDecimal.valueOf(0.115),  // 11.5% para score >= 750
        "VERY_GOOD", BigDecimal.valueOf(0.135),  // 13.5% para score 700-749
        "GOOD", BigDecimal.valueOf(0.155),       // 15.5% para score 650-699
        "FAIR", BigDecimal.valueOf(0.175),       // 17.5% para score 600-649
        "POOR", BigDecimal.valueOf(0.215)        // 21.5% para score < 600
    );
    
    // Límites de tasas
    private static final BigDecimal MIN_RATE = BigDecimal.valueOf(0.10);  // 10% mínimo
    private static final BigDecimal MAX_RATE = BigDecimal.valueOf(0.28);  // 28% máximo
    
    // Factores de ajuste
    private static final BigDecimal VEHICLE_NEW_DISCOUNT = BigDecimal.valueOf(-0.005);    // -0.5% vehículo nuevo
    private static final BigDecimal VEHICLE_OLD_SURCHARGE = BigDecimal.valueOf(0.01);     // +1% vehículo antiguo
    private static final BigDecimal HIGH_AMOUNT_DISCOUNT = BigDecimal.valueOf(-0.005);    // -0.5% montos altos
    private static final BigDecimal LONG_TERM_SURCHARGE = BigDecimal.valueOf(0.005);      // +0.5% plazos largos
    private static final BigDecimal HIGH_INCOME_DISCOUNT = BigDecimal.valueOf(-0.005);    // -0.5% ingresos altos
    
    public InterestRateCalculation calculateInterestRate(
            CreditScore creditScore, 
            Customer customer, 
            Vehicle vehicle, 
            CreditAmount requestedAmount, 
            int termInMonths) {
        
        log.info("Calculando tasa de interés para score: {}, monto: {}, plazo: {} meses", 
                 creditScore.getValue(), requestedAmount.getValue(), termInMonths);
        
        InterestRateCalculation calculation = new InterestRateCalculation();
        
        // 1. Obtener tasa base según score crediticio
        BigDecimal baseRate = getBaseRateByScore(creditScore);
        calculation.setBaseRate(baseRate);
        calculation.addDetail("Tasa base por score " + creditScore.getValue() + ": " + 
                             formatPercentage(baseRate));
        
        // 2. Aplicar ajustes por vehículo
        BigDecimal vehicleAdjustment = calculateVehicleAdjustment(vehicle);
        calculation.addAdjustment("VEHICLE", vehicleAdjustment);
        
        // 3. Aplicar ajustes por monto
        BigDecimal amountAdjustment = calculateAmountAdjustment(requestedAmount);
        calculation.addAdjustment("AMOUNT", amountAdjustment);
        
        // 4. Aplicar ajustes por plazo
        BigDecimal termAdjustment = calculateTermAdjustment(termInMonths);
        calculation.addAdjustment("TERM", termAdjustment);
        
        // 5. Aplicar ajustes por perfil del cliente
        BigDecimal customerAdjustment = calculateCustomerAdjustment(customer);
        calculation.addAdjustment("CUSTOMER", customerAdjustment);
        
        // 6. Calcular tasa final
        BigDecimal finalRate = baseRate
            .add(vehicleAdjustment)
            .add(amountAdjustment)
            .add(termAdjustment)
            .add(customerAdjustment);
        
        // 7. Aplicar límites mínimos y máximos
        finalRate = applyRateLimits(finalRate);
        calculation.setFinalRate(finalRate);
        
        log.info("Tasa de interés calculada: {}% para solicitud", formatPercentage(finalRate));
        
        return calculation;
    }
    
    // Método legacy mantenido para compatibilidad
    public BigDecimal calculateInterestRate(CreditApplication application, CreditScore creditScore) {
        InterestRateCalculation calculation = calculateInterestRate(
            creditScore, 
            application.getCustomer(), 
            application.getVehicle(), 
            application.getRequestedAmount(), 
            60 // plazo por defecto
        );
        return calculation.getFinalRate();
    }
    
    public BigDecimal calculateMonthlyInstallment(CreditAmount loanAmount, 
                                                 BigDecimal annualInterestRate, 
                                                 int termInMonths) {
        log.debug("Calculando cuota mensual: monto={}, tasa={}, plazo={}", 
                  loanAmount.getValue(), annualInterestRate, termInMonths);
        
        if (termInMonths <= 0) {
            throw new IllegalArgumentException("El plazo debe ser mayor a 0 meses");
        }
        
        if (annualInterestRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La tasa de interés debe ser mayor a 0");
        }
        
        BigDecimal monthlyRate = annualInterestRate.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);
        BigDecimal principal = loanAmount.getValue();
        
        // Fórmula de amortización: PMT = P * [r(1+r)^n] / [(1+r)^n - 1]
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(termInMonths);
        
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRPowN);
        BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);
        
        BigDecimal monthlyPayment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        
        log.debug("Cuota mensual calculada: {}", monthlyPayment);
        return monthlyPayment;
    }
    
    // Método legacy mantenido para compatibilidad
    public BigDecimal calculateMonthlyInstallment(BigDecimal loanAmount, BigDecimal annualRate, int termInMonths) {
        if (loanAmount.compareTo(BigDecimal.ZERO) <= 0 || termInMonths <= 0) {
            throw new IllegalArgumentException("Parámetros de cálculo inválidos");
        }
        
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);
        
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return loanAmount.divide(BigDecimal.valueOf(termInMonths), 2, RoundingMode.HALF_UP);
        }
        
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(termInMonths);
        
        BigDecimal numerator = loanAmount.multiply(monthlyRate).multiply(onePlusRPowN);
        BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal getBaseRateByScore(CreditScore creditScore) {
        int score = creditScore.getValue();
        
        if (score >= 750) {
            return BASE_RATES.get("EXCELLENT");
        } else if (score >= 700) {
            return BASE_RATES.get("VERY_GOOD");
        } else if (score >= 650) {
            return BASE_RATES.get("GOOD");
        } else if (score >= 600) {
            return BASE_RATES.get("FAIR");
        } else {
            return BASE_RATES.get("POOR");
        }
    }
    
    private BigDecimal calculateVehicleAdjustment(Vehicle vehicle) {
        int currentYear = Year.now().getValue();
        int vehicleAge = currentYear - vehicle.getYear();
        
        BigDecimal adjustment = BigDecimal.ZERO;
        
        if (vehicleAge <= 1) {
            // Vehículo nuevo - descuento
            adjustment = VEHICLE_NEW_DISCOUNT;
        } else if (vehicleAge >= 5) {
            // Vehículo antiguo - sobrecargo
            adjustment = VEHICLE_OLD_SURCHARGE;
        }
        
        return adjustment;
    }
    
    private BigDecimal calculateAmountAdjustment(CreditAmount amount) {
        BigDecimal value = amount.getValue();
        
        // Descuento para montos altos (mayor a 150 millones)
        if (value.compareTo(BigDecimal.valueOf(150_000_000)) > 0) {
            return HIGH_AMOUNT_DISCOUNT;
        }
        
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateTermAdjustment(int termInMonths) {
        // Sobrecargo para plazos largos (más de 60 meses)
        if (termInMonths > 60) {
            return LONG_TERM_SURCHARGE;
        }
        
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateCustomerAdjustment(Customer customer) {
        BigDecimal adjustment = BigDecimal.ZERO;
        
        // Descuento por ingresos altos (mayor a 8 millones mensuales)
        if (customer.getMonthlyIncome().getValue().compareTo(BigDecimal.valueOf(8_000_000)) > 0) {
            adjustment = adjustment.add(HIGH_INCOME_DISCOUNT);
        }
        
        return adjustment;
    }
    
    private BigDecimal applyRateLimits(BigDecimal rate) {
        if (rate.compareTo(MIN_RATE) < 0) {
            return MIN_RATE;
        } else if (rate.compareTo(MAX_RATE) > 0) {
            return MAX_RATE;
        }
        return rate;
    }
    
    private String formatPercentage(BigDecimal rate) {
        return rate.multiply(BigDecimal.valueOf(100))
                  .setScale(2, RoundingMode.HALF_UP)
                  .toString() + "%";
    }
    
    // Clase interna para el cálculo de tasas
    public static class InterestRateCalculation {
        private BigDecimal baseRate;
        private BigDecimal finalRate;
        private final Map<String, BigDecimal> adjustments = new HashMap<>();
        private final java.util.List<String> details = new java.util.ArrayList<>();
        
        public void setBaseRate(BigDecimal baseRate) {
            this.baseRate = baseRate;
        }
        
        public void setFinalRate(BigDecimal finalRate) {
            this.finalRate = finalRate;
        }
        
        public void addAdjustment(String category, BigDecimal adjustment) {
            if (adjustment.compareTo(BigDecimal.ZERO) != 0) {
                adjustments.put(category, adjustment);
                String sign = adjustment.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
                details.add(String.format("Ajuste %s: %s%.3f%%", 
                    category.toLowerCase(), sign, adjustment.multiply(BigDecimal.valueOf(100))));
            }
        }
        
        public void addDetail(String detail) {
            details.add(detail);
        }
        
        // Getters
        public BigDecimal getBaseRate() { return baseRate; }
        public BigDecimal getFinalRate() { return finalRate; }
        public Map<String, BigDecimal> getAdjustments() { return adjustments; }
        public java.util.List<String> getDetails() { return details; }
        
        public BigDecimal getTotalAdjustments() {
            return adjustments.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}