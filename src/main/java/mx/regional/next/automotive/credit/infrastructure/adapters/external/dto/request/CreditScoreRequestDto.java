package mx.regional.next.automotive.credit.infrastructure.adapters.external.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditScoreRequestDto {
    
    @JsonProperty("document_number")
    private String documentNumber;
    
    @JsonProperty("document_type")
    private String documentType;
    
    @JsonProperty("include_credit_history")
    private boolean includeCreditHistory;
    
    @JsonProperty("include_payment_behavior")
    private boolean includePaymentBehavior;
    
    // Constructors
    public CreditScoreRequestDto() {}
    
    public CreditScoreRequestDto(String documentNumber, String documentType) {
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.includeCreditHistory = true;
        this.includePaymentBehavior = true;
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private CreditScoreRequestDto request = new CreditScoreRequestDto();
        
        public Builder documentNumber(String documentNumber) {
            request.documentNumber = documentNumber;
            return this;
        }
        
        public Builder documentType(String documentType) {
            request.documentType = documentType;
            return this;
        }
        
        public Builder includeCreditHistory(boolean include) {
            request.includeCreditHistory = include;
            return this;
        }
        
        public Builder includePaymentBehavior(boolean include) {
            request.includePaymentBehavior = include;
            return this;
        }
        
        public CreditScoreRequestDto build() {
            return request;
        }
    }
    
    // Getters and Setters
    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    
    public boolean isIncludeCreditHistory() { return includeCreditHistory; }
    public void setIncludeCreditHistory(boolean includeCreditHistory) { this.includeCreditHistory = includeCreditHistory; }
    
    public boolean isIncludePaymentBehavior() { return includePaymentBehavior; }
    public void setIncludePaymentBehavior(boolean includePaymentBehavior) { this.includePaymentBehavior = includePaymentBehavior; }
}