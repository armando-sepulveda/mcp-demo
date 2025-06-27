package mx.regional.next.automotive.credit.infrastructure.adapters.external.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class CreditScoreResponseDto {
    
    @JsonProperty("document_number")
    private String documentNumber;
    
    @JsonProperty("credit_score")
    private Integer creditScore;
    
    @JsonProperty("score_category")
    private String scoreCategory;
    
    @JsonProperty("calculation_date")
    private LocalDateTime calculationDate;
    
    @JsonProperty("valid")
    private boolean valid;
    
    @JsonProperty("error_code")
    private String errorCode;
    
    @JsonProperty("error_message")
    private String errorMessage;
    
    @JsonProperty("fallback_activated")
    private boolean fallbackActivated;
    
    @JsonProperty("data_sources")
    private String[] dataSources;
    
    @JsonProperty("additional_info")
    private AdditionalInfo additionalInfo;
    
    // Constructors
    public CreditScoreResponseDto() {}
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private CreditScoreResponseDto response = new CreditScoreResponseDto();
        
        public Builder documentNumber(String documentNumber) {
            response.documentNumber = documentNumber;
            return this;
        }
        
        public Builder creditScore(Integer creditScore) {
            response.creditScore = creditScore;
            return this;
        }
        
        public Builder scoreCategory(String scoreCategory) {
            response.scoreCategory = scoreCategory;
            return this;
        }
        
        public Builder valid(boolean valid) {
            response.valid = valid;
            return this;
        }
        
        public Builder errorCode(String errorCode) {
            response.errorCode = errorCode;
            return this;
        }
        
        public Builder errorMessage(String errorMessage) {
            response.errorMessage = errorMessage;
            return this;
        }
        
        public Builder fallbackActivated(boolean fallbackActivated) {
            response.fallbackActivated = fallbackActivated;
            return this;
        }
        
        public CreditScoreResponseDto build() {
            response.calculationDate = LocalDateTime.now();
            return response;
        }
    }
    
    // Nested class for additional information
    public static class AdditionalInfo {
        @JsonProperty("payment_history_months")
        private Integer paymentHistoryMonths;
        
        @JsonProperty("active_accounts")
        private Integer activeAccounts;
        
        @JsonProperty("total_debt_amount")
        private java.math.BigDecimal totalDebtAmount;
        
        @JsonProperty("credit_utilization_ratio")
        private java.math.BigDecimal creditUtilizationRatio;
        
        // Getters and setters
        public Integer getPaymentHistoryMonths() { return paymentHistoryMonths; }
        public void setPaymentHistoryMonths(Integer paymentHistoryMonths) { this.paymentHistoryMonths = paymentHistoryMonths; }
        
        public Integer getActiveAccounts() { return activeAccounts; }
        public void setActiveAccounts(Integer activeAccounts) { this.activeAccounts = activeAccounts; }
        
        public java.math.BigDecimal getTotalDebtAmount() { return totalDebtAmount; }
        public void setTotalDebtAmount(java.math.BigDecimal totalDebtAmount) { this.totalDebtAmount = totalDebtAmount; }
        
        public java.math.BigDecimal getCreditUtilizationRatio() { return creditUtilizationRatio; }
        public void setCreditUtilizationRatio(java.math.BigDecimal creditUtilizationRatio) { this.creditUtilizationRatio = creditUtilizationRatio; }
    }
    
    // Getters and Setters
    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    
    public String getScoreCategory() { return scoreCategory; }
    public void setScoreCategory(String scoreCategory) { this.scoreCategory = scoreCategory; }
    
    public LocalDateTime getCalculationDate() { return calculationDate; }
    public void setCalculationDate(LocalDateTime calculationDate) { this.calculationDate = calculationDate; }
    
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public boolean isFallbackActivated() { return fallbackActivated; }
    public void setFallbackActivated(boolean fallbackActivated) { this.fallbackActivated = fallbackActivated; }
    
    public String[] getDataSources() { return dataSources; }
    public void setDataSources(String[] dataSources) { this.dataSources = dataSources; }
    
    public AdditionalInfo getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(AdditionalInfo additionalInfo) { this.additionalInfo = additionalInfo; }
}