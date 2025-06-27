package mx.regional.next.automotive.credit.domain.services;

import mx.regional.next.automotive.credit.domain.entities.CreditApplication;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditScore;
import mx.regional.next.shared.common.annotations.DomainService;

import java.math.BigDecimal;

@DomainService
public class InterestRateCalculationService {
    
    public BigDecimal calculateInterestRate(CreditApplication application, CreditScore creditScore) {
        BigDecimal baseRate = getBaseRateByScore(creditScore);
        BigDecimal adjustedRate = applyAdjustments(baseRate, application);
        
        return adjustedRate;
    }
    
    public BigDecimal calculateMonthlyInstallment(BigDecimal loanAmount, BigDecimal annualRate, int termInMonths) {
        if (loanAmount.compareTo(BigDecimal.ZERO) <= 0 || termInMonths <= 0) {
            throw new IllegalArgumentException("Parámetros de cálculo inválidos");
        }
        
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_UP);
        
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return loanAmount.divide(BigDecimal.valueOf(termInMonths), 2, BigDecimal.ROUND_HALF_UP);
        }
        
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(termInMonths);
        
        BigDecimal numerator = loanAmount.multiply(monthlyRate).multiply(onePlusRPowN);
        BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);
        
        return numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
    }
    
    private BigDecimal getBaseRateByScore(CreditScore creditScore) {
        if (creditScore.getValue() >= 800) {
            return BigDecimal.valueOf(0.1125); // 11.25%
        } else if (creditScore.getValue() >= 750) {
            return BigDecimal.valueOf(0.13); // 13%
        } else if (creditScore.getValue() >= 700) {
            return BigDecimal.valueOf(0.15); // 15%
        } else if (creditScore.getValue() >= 650) {
            return BigDecimal.valueOf(0.17); // 17%
        } else if (creditScore.getValue() >= 600) {
            return BigDecimal.valueOf(0.19); // 19%
        } else {
            return BigDecimal.valueOf(0.21); // 21% (casos excepcionales)
        }
    }
    
    private BigDecimal applyAdjustments(BigDecimal baseRate, CreditApplication application) {
        BigDecimal adjustedRate = baseRate;
        
        // Descuento por cliente con productos existentes (simulado)
        // adjustedRate = adjustedRate.subtract(BigDecimal.valueOf(0.005)); // -0.5%
        
        // Descuento por antigüedad laboral superior a 3 años
        if (application.getCustomer().getWorkExperienceMonths() > 36) {
            adjustedRate = adjustedRate.subtract(BigDecimal.valueOf(0.005)); // -0.5%
        }
        
        // Descuento por ingresos altos
        if (application.getCustomer().getMonthlyIncome().getValue()
                .compareTo(BigDecimal.valueOf(5000000)) > 0) {
            adjustedRate = adjustedRate.subtract(BigDecimal.valueOf(0.005)); // -0.5%
        }
        
        // Descuento por vehículo nuevo
        if (application.getVehicle().isNew()) {
            adjustedRate = adjustedRate.subtract(BigDecimal.valueOf(0.01)); // -1%
        }
        
        // Incremento por vehículo usado
        if (application.getVehicle().isUsed()) {
            adjustedRate = adjustedRate.add(BigDecimal.valueOf(0.01)); // +1%
        }
        
        // Incremento por financiación alta
        BigDecimal financingRatio = application.getRequestedAmount().getValue()
                .divide(application.getVehicle().getValue().getValue(), 4, BigDecimal.ROUND_HALF_UP);
        if (financingRatio.compareTo(BigDecimal.valueOf(0.80)) > 0) {
            adjustedRate = adjustedRate.add(BigDecimal.valueOf(0.005)); // +0.5%
        }
        
        // Asegurar que la tasa no sea menor a 10% ni mayor a 25%
        adjustedRate = adjustedRate.max(BigDecimal.valueOf(0.10));
        adjustedRate = adjustedRate.min(BigDecimal.valueOf(0.25));
        
        return adjustedRate;
    }
}