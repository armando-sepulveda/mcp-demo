package mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.entities;

import mx.regional.next.automotive.credit.domain.enums.DocumentType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "customers")
public class CustomerJpaEntity {
    
    @Id
    @Column(name = "document_number")
    private String documentNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    @Column(name = "monthly_income", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyIncome;
    
    @Column(name = "current_monthly_debts", precision = 15, scale = 2)
    private BigDecimal currentMonthlyDebts;
    
    @Column(name = "occupation", nullable = false)
    private String occupation;
    
    @Column(name = "work_experience_months")
    private Integer workExperienceMonths;
    
    @Column(name = "created_date")
    private LocalDate createdDate;
    
    @Column(name = "last_updated")
    private LocalDate lastUpdated;
    
    // Constructors
    public CustomerJpaEntity() {}
    
    public CustomerJpaEntity(String documentNumber, DocumentType documentType,
                           String firstName, String lastName, String email,
                           String phoneNumber, LocalDate birthDate,
                           BigDecimal monthlyIncome, BigDecimal currentMonthlyDebts,
                           String occupation, Integer workExperienceMonths) {
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.monthlyIncome = monthlyIncome;
        this.currentMonthlyDebts = currentMonthlyDebts;
        this.occupation = occupation;
        this.workExperienceMonths = workExperienceMonths;
        this.createdDate = LocalDate.now();
        this.lastUpdated = LocalDate.now();
    }
    
    // Getters and Setters
    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    
    public BigDecimal getCurrentMonthlyDebts() { return currentMonthlyDebts; }
    public void setCurrentMonthlyDebts(BigDecimal currentMonthlyDebts) { this.currentMonthlyDebts = currentMonthlyDebts; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public Integer getWorkExperienceMonths() { return workExperienceMonths; }
    public void setWorkExperienceMonths(Integer workExperienceMonths) { this.workExperienceMonths = workExperienceMonths; }
    
    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
    
    public LocalDate getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }
}