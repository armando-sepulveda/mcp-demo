package mx.regional.next.automotive.credit.application.dto;

import mx.regional.next.automotive.credit.domain.enums.CreditStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreditApplicationResponse {
    private String applicationId;
    private CreditStatus status;
    private String customerDocument;
    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private Integer creditScore;
    private BigDecimal interestRate;
    private String rejectionReason;
    private LocalDateTime applicationDate;
    private LocalDateTime lastUpdateDate;
    
    // Constructors
    public CreditApplicationResponse() {}
    
    public CreditApplicationResponse(String applicationId, CreditStatus status, 
                                   String customerDocument, BigDecimal requestedAmount) {
        this.applicationId = applicationId;
        this.status = status;
        this.customerDocument = customerDocument;
        this.requestedAmount = requestedAmount;
    }
    
    // Factory methods
    public static CreditApplicationResponse approved(String applicationId, int creditScore, 
                                                   BigDecimal approvedAmount) {
        CreditApplicationResponse response = new CreditApplicationResponse();
        response.applicationId = applicationId;
        response.status = CreditStatus.APPROVED;
        response.creditScore = creditScore;
        response.approvedAmount = approvedAmount;
        response.lastUpdateDate = LocalDateTime.now();
        return response;
    }
    
    public static CreditApplicationResponse rejected(String applicationId, String rejectionReason) {
        CreditApplicationResponse response = new CreditApplicationResponse();
        response.applicationId = applicationId;
        response.status = CreditStatus.REJECTED;
        response.rejectionReason = rejectionReason;
        response.lastUpdateDate = LocalDateTime.now();
        return response;
    }
    
    public static CreditApplicationResponse pending(String applicationId) {
        CreditApplicationResponse response = new CreditApplicationResponse();
        response.applicationId = applicationId;
        response.status = CreditStatus.PENDING;
        response.lastUpdateDate = LocalDateTime.now();
        return response;
    }
    
    // Business methods
    public boolean isApproved() {
        return status == CreditStatus.APPROVED;
    }
    
    public boolean isRejected() {
        return status == CreditStatus.REJECTED;
    }
    
    public boolean isPending() {
        return status == CreditStatus.PENDING;
    }
    
    // Getters and Setters
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }
    
    public CreditStatus getStatus() { return status; }
    public void setStatus(CreditStatus status) { this.status = status; }
    
    public String getCustomerDocument() { return customerDocument; }
    public void setCustomerDocument(String customerDocument) { this.customerDocument = customerDocument; }
    
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
    
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    
    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }
    
    public LocalDateTime getLastUpdateDate() { return lastUpdateDate; }
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }
}