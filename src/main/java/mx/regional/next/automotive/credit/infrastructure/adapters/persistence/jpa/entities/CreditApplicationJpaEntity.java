package mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.entities;

import mx.regional.next.automotive.credit.domain.enums.CreditStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_applications")
public class CreditApplicationJpaEntity {
    
    @Id
    private String id;
    
    @Column(name = "customer_document", nullable = false)
    private String customerDocument;
    
    @Column(name = "vehicle_vin", nullable = false)
    private String vehicleVin;
    
    @Column(name = "requested_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal requestedAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CreditStatus status;
    
    @Column(name = "credit_score")
    private Integer creditScore;
    
    @Column(name = "rejection_reason")
    private String rejectionReason;
    
    @Column(name = "application_date", nullable = false)
    private LocalDateTime applicationDate;
    
    @Column(name = "last_update_date", nullable = false)
    private LocalDateTime lastUpdateDate;
    
    @Column(name = "vehicle_brand")
    private String vehicleBrand;
    
    @Column(name = "vehicle_model")
    private String vehicleModel;
    
    @Column(name = "vehicle_year")
    private Integer vehicleYear;
    
    @Column(name = "vehicle_value", precision = 15, scale = 2)
    private BigDecimal vehicleValue;
    
    @Column(name = "vehicle_kilometers")
    private Integer vehicleKilometers;
    
    // Constructors
    public CreditApplicationJpaEntity() {}
    
    public CreditApplicationJpaEntity(String id, String customerDocument, String vehicleVin,
                                    BigDecimal requestedAmount, CreditStatus status,
                                    LocalDateTime applicationDate, LocalDateTime lastUpdateDate) {
        this.id = id;
        this.customerDocument = customerDocument;
        this.vehicleVin = vehicleVin;
        this.requestedAmount = requestedAmount;
        this.status = status;
        this.applicationDate = applicationDate;
        this.lastUpdateDate = lastUpdateDate;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCustomerDocument() { return customerDocument; }
    public void setCustomerDocument(String customerDocument) { this.customerDocument = customerDocument; }
    
    public String getVehicleVin() { return vehicleVin; }
    public void setVehicleVin(String vehicleVin) { this.vehicleVin = vehicleVin; }
    
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
    
    public CreditStatus getStatus() { return status; }
    public void setStatus(CreditStatus status) { this.status = status; }
    
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    
    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }
    
    public LocalDateTime getLastUpdateDate() { return lastUpdateDate; }
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }
    
    public String getVehicleBrand() { return vehicleBrand; }
    public void setVehicleBrand(String vehicleBrand) { this.vehicleBrand = vehicleBrand; }
    
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    
    public Integer getVehicleYear() { return vehicleYear; }
    public void setVehicleYear(Integer vehicleYear) { this.vehicleYear = vehicleYear; }
    
    public BigDecimal getVehicleValue() { return vehicleValue; }
    public void setVehicleValue(BigDecimal vehicleValue) { this.vehicleValue = vehicleValue; }
    
    public Integer getVehicleKilometers() { return vehicleKilometers; }
    public void setVehicleKilometers(Integer vehicleKilometers) { this.vehicleKilometers = vehicleKilometers; }
}