package mx.regional.next.automotive.credit.domain.entities;

import mx.regional.next.automotive.credit.domain.valueobjects.DocumentNumber;
import mx.regional.next.automotive.credit.domain.valueobjects.CreditAmount;
import mx.regional.next.automotive.credit.domain.enums.DocumentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Customer {
    private final DocumentNumber documentNumber;
    private final DocumentType documentType;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final LocalDate birthDate;
    private final CreditAmount monthlyIncome;
    private final CreditAmount currentMonthlyDebts;
    private final String occupation;
    private final int workExperienceMonths;
    
    public Customer(DocumentNumber documentNumber, DocumentType documentType, 
                   String firstName, String lastName, String email, String phoneNumber,
                   LocalDate birthDate, CreditAmount monthlyIncome, 
                   CreditAmount currentMonthlyDebts, String occupation, 
                   int workExperienceMonths) {
        
        validateConstructorParameters(documentNumber, firstName, lastName, 
                                    email, birthDate, monthlyIncome, occupation);
        
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.monthlyIncome = monthlyIncome;
        this.currentMonthlyDebts = currentMonthlyDebts != null ? currentMonthlyDebts : 
                                   new CreditAmount(BigDecimal.ZERO);
        this.occupation = occupation;
        this.workExperienceMonths = workExperienceMonths;
    }
    
    private void validateConstructorParameters(DocumentNumber documentNumber, String firstName,
                                             String lastName, String email, LocalDate birthDate,
                                             CreditAmount monthlyIncome, String occupation) {
        if (documentNumber == null) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("El email debe ser válido");
        }
        if (birthDate == null || birthDate.isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("El cliente debe ser mayor de edad");
        }
        if (monthlyIncome == null) {
            throw new IllegalArgumentException("Los ingresos mensuales son obligatorios");
        }
        if (occupation == null || occupation.trim().isEmpty()) {
            throw new IllegalArgumentException("La ocupación es obligatoria");
        }
    }
    
    public boolean hasValidIncome() {
        BigDecimal minimumIncome = BigDecimal.valueOf(300000);
        return monthlyIncome.getValue().compareTo(minimumIncome) >= 0;
    }
    
    public boolean hasAcceptableDebtRatio() {
        BigDecimal debtRatio = currentMonthlyDebts.getValue()
                              .divide(monthlyIncome.getValue(), 4, BigDecimal.ROUND_HALF_UP);
        return debtRatio.compareTo(BigDecimal.valueOf(0.40)) <= 0;
    }
    
    public int getAge() {
        return LocalDate.now().getYear() - birthDate.getYear();
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public BigDecimal getAvailableMonthlyPaymentCapacity() {
        BigDecimal maxPaymentRatio = BigDecimal.valueOf(0.30); // 30% del ingreso
        BigDecimal maxPayment = monthlyIncome.getValue().multiply(maxPaymentRatio);
        return maxPayment.subtract(currentMonthlyDebts.getValue()).max(BigDecimal.ZERO);
    }
    
    // Getters
    public DocumentNumber getDocumentNumber() { return documentNumber; }
    public DocumentType getDocumentType() { return documentType; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDate getBirthDate() { return birthDate; }
    public CreditAmount getMonthlyIncome() { return monthlyIncome; }
    public CreditAmount getCurrentMonthlyDebts() { return currentMonthlyDebts; }
    public String getOccupation() { return occupation; }
    public int getWorkExperienceMonths() { return workExperienceMonths; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return Objects.equals(documentNumber, customer.documentNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(documentNumber);
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "documentNumber=" + documentNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}