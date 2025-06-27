package mx.regional.next.automotive.credit.domain.services;

import mx.regional.next.automotive.credit.domain.entities.CreditApplication;
import mx.regional.next.automotive.credit.domain.entities.Customer;
import mx.regional.next.automotive.credit.domain.entities.Vehicle;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditAmount;
import mx.regional.next.shared.common.annotations.DomainService;

import java.math.BigDecimal;

@DomainService
public class CreditEligibilityService {
    
    public boolean isEligible(CreditApplication application) {
        return hasMinimumIncome(application) &&
               hasAcceptableCreditHistory(application) &&
               vehicleQualifiesForCredit(application) &&
               debtToIncomeRatioAcceptable(application);
    }
    
    public CreditAmount calculateMaxEligibleAmount(Customer customer, Vehicle vehicle) {
        BigDecimal monthlyIncome = customer.getMonthlyIncome().getValue();
        BigDecimal maxMonthlyPayment = monthlyIncome.multiply(BigDecimal.valueOf(0.30)); // 30% del ingreso
        
        // Usar tasa de interés estándar para calcular monto máximo
        BigDecimal annualInterestRate = BigDecimal.valueOf(0.12); // 12% anual
        int termInMonths = 60; // 5 años estándar
        
        BigDecimal maxCreditAmount = calculateLoanAmount(maxMonthlyPayment, 
                                                        annualInterestRate, 
                                                        termInMonths);
        
        // No puede exceder el valor del vehículo
        BigDecimal vehicleValue = vehicle.getValue().getValue();
        BigDecimal maxByVehicle = vehicleValue.multiply(BigDecimal.valueOf(0.90)); // 90% del valor
        
        BigDecimal finalAmount = maxCreditAmount.min(maxByVehicle);
        
        return new CreditAmount(finalAmount);
    }
    
    private boolean hasMinimumIncome(CreditApplication application) {
        BigDecimal minIncome = BigDecimal.valueOf(300000); // Mínimo mensual
        return application.getCustomer().getMonthlyIncome().getValue()
                .compareTo(minIncome) >= 0;
    }
    
    private boolean hasAcceptableCreditHistory(CreditApplication application) {
        // En un escenario real, esto consultaría centrales de riesgo
        // Por ahora, asumimos que es aceptable si cumple otros criterios
        return application.getCustomer().hasAcceptableDebtRatio();
    }
    
    private boolean vehicleQualifiesForCredit(CreditApplication application) {
        Vehicle vehicle = application.getVehicle();
        return vehicle.getYear() >= 2018 && // No más de 6 años
               vehicle.getKilometers() <= 100000 && // Máximo 100k km
               vehicle.isFromApprovedBrand();
    }
    
    private boolean debtToIncomeRatioAcceptable(CreditApplication application) {
        // Ratio deuda/ingreso no debe exceder 40%
        BigDecimal monthlyIncome = application.getCustomer().getMonthlyIncome().getValue();
        BigDecimal currentDebts = application.getCustomer().getCurrentMonthlyDebts().getValue();
        
        BigDecimal ratio = currentDebts.divide(monthlyIncome, 4, BigDecimal.ROUND_HALF_UP);
        return ratio.compareTo(BigDecimal.valueOf(0.40)) <= 0;
    }
    
    private BigDecimal calculateLoanAmount(BigDecimal monthlyPayment, 
                                         BigDecimal annualRate, 
                                         int termInMonths) {
        // Fórmula de cálculo de préstamo
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(termInMonths);
        
        BigDecimal denominator = monthlyRate.multiply(onePlusRPowN);
        BigDecimal numerator = onePlusRPowN.subtract(BigDecimal.ONE);
        
        return monthlyPayment.multiply(numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP));
    }
}