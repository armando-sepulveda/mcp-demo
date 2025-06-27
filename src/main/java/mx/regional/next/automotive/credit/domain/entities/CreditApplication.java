package mx.regional.next.automotive.credit.domain.entities;

import mx.regional.next.automotive.credit.domain.valueobjects.CreditAmount;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditScore;
import mx.regional.next.automotive.credit.domain.enums.CreditStatus;
import mx.regional.next.automotive.credit.domain.exceptions.InvalidCreditAmountException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class CreditApplication {
    private final String id;
    private final Customer customer;
    private final Vehicle vehicle;
    private final CreditAmount requestedAmount;
    private CreditStatus status;
    private CreditScore creditScore;
    private final LocalDateTime applicationDate;
    private LocalDateTime lastUpdateDate;
    private String rejectionReason;
    
    public CreditApplication(Customer customer, Vehicle vehicle, CreditAmount requestedAmount) {
        validateConstructorParameters(customer, vehicle, requestedAmount);
        
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.vehicle = vehicle;
        this.requestedAmount = requestedAmount;
        this.status = CreditStatus.PENDING;
        this.applicationDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
        
        validateBusinessRules();
    }
    
    private void validateConstructorParameters(Customer customer, Vehicle vehicle, 
                                             CreditAmount requestedAmount) {
        if (customer == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("El vehículo es obligatorio");
        }
        if (requestedAmount == null) {
            throw new IllegalArgumentException("El monto solicitado es obligatorio");
        }
    }
    
    private void validateBusinessRules() {
        if (requestedAmount.isGreaterThan(vehicle.getMaxCreditAmount())) {
            throw new InvalidCreditAmountException(
                "Monto solicitado excede el máximo permitido para el vehículo");
        }
    }
    
    public void approve(CreditScore creditScore) {
        if (this.status != CreditStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden aprobar aplicaciones pendientes");
        }
        if (creditScore.getValue() < 600) {
            throw new IllegalArgumentException("Score mínimo requerido: 600");
        }
        
        this.creditScore = creditScore;
        this.status = CreditStatus.APPROVED;
        this.lastUpdateDate = LocalDateTime.now();
    }
    
    public void reject(String reason) {
        if (this.status != CreditStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden rechazar aplicaciones pendientes");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo de rechazo es obligatorio");
        }
        
        this.status = CreditStatus.REJECTED;
        this.rejectionReason = reason;
        this.lastUpdateDate = LocalDateTime.now();
    }
    
    public void cancel() {
        if (this.status.isFinal()) {
            throw new IllegalStateException("No se puede cancelar una aplicación finalizada");
        }
        
        this.status = CreditStatus.CANCELLED;
        this.lastUpdateDate = LocalDateTime.now();
    }
    
    public boolean isEligibleForProcessing() {
        return vehicle.isEligibleForCredit() && 
               customer.hasValidIncome() &&
               customer.hasAcceptableDebtRatio();
    }
    
    public boolean isPending() {
        return status == CreditStatus.PENDING;
    }
    
    public boolean isApproved() {
        return status == CreditStatus.APPROVED;
    }
    
    public boolean isRejected() {
        return status == CreditStatus.REJECTED;
    }
    
    // Getters
    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() { return vehicle; }
    public CreditAmount getRequestedAmount() { return requestedAmount; }
    public CreditStatus getStatus() { return status; }
    public CreditScore getCreditScore() { return creditScore; }
    public LocalDateTime getApplicationDate() { return applicationDate; }
    public LocalDateTime getLastUpdateDate() { return lastUpdateDate; }
    public String getRejectionReason() { return rejectionReason; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CreditApplication that = (CreditApplication) obj;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "CreditApplication{" +
                "id='" + id + '\'' +
                ", customer=" + customer.getDocumentNumber() +
                ", requestedAmount=" + requestedAmount +
                ", status=" + status +
                '}';
    }
}