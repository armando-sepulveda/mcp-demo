package mx.regional.next.automotive.credit.application.dto;

import java.math.BigDecimal;

public class InstallmentCalculationResponse {
    private BigDecimal loanAmount;
    private BigDecimal annualInterestRate;
    private int termInMonths;
    private BigDecimal monthlyInstallment;
    private BigDecimal totalInterest;
    private BigDecimal totalAmount;
    
    public InstallmentCalculationResponse() {}
    
    public InstallmentCalculationResponse(BigDecimal loanAmount, BigDecimal annualInterestRate,
                                        int termInMonths, BigDecimal monthlyInstallment) {
        this.loanAmount = loanAmount;
        this.annualInterestRate = annualInterestRate;
        this.termInMonths = termInMonths;
        this.monthlyInstallment = monthlyInstallment;
        this.totalAmount = monthlyInstallment.multiply(BigDecimal.valueOf(termInMonths));
        this.totalInterest = totalAmount.subtract(loanAmount);
    }
    
    // Getters and Setters
    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
    
    public BigDecimal getAnnualInterestRate() { return annualInterestRate; }
    public void setAnnualInterestRate(BigDecimal annualInterestRate) { this.annualInterestRate = annualInterestRate; }
    
    public int getTermInMonths() { return termInMonths; }
    public void setTermInMonths(int termInMonths) { this.termInMonths = termInMonths; }
    
    public BigDecimal getMonthlyInstallment() { return monthlyInstallment; }
    public void setMonthlyInstallment(BigDecimal monthlyInstallment) { this.monthlyInstallment = monthlyInstallment; }
    
    public BigDecimal getTotalInterest() { return totalInterest; }
    public void setTotalInterest(BigDecimal totalInterest) { this.totalInterest = totalInterest; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}