package mx.regional.next.automotive.credit.application.ports.in;

import mx.regional.next.automotive.credit.application.dto.InstallmentCalculationResponse;

import java.math.BigDecimal;

public interface CalculateInstallmentUseCase {
    InstallmentCalculationResponse calculateInstallment(BigDecimal loanAmount, 
                                                      BigDecimal annualRate, 
                                                      int termInMonths);
}