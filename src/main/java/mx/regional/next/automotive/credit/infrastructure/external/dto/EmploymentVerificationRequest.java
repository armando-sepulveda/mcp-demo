package mx.regional.next.automotive.credit.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
public class EmploymentVerificationRequest {

    // Información del empleado
    @NotBlank(message = "Employee document number is required")
    @Pattern(regexp = "^[0-9]{8,11}$", message = "Invalid document number format")
    private String employeeDocumentNumber;

    @NotBlank(message = "Employee document type is required")
    @Pattern(regexp = "^(CC|CE|TI|PA)$", message = "Invalid document type")
    private String employeeDocumentType;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String middleName;
    private String secondLastName;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    // Información de la empresa
    @NotBlank(message = "Company ID is required")
    private String companyId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @Pattern(regexp = "^[0-9]{9,11}$", message = "Invalid company NIT format")
    private String companyNit;

    private String companyAddress;
    private String companyCity;
    private String companyDepartment;
    private String companyPhone;
    private String companySector;

    // Información laboral
    private String jobTitle;
    private String department;
    private LocalDate hireDate;
    private String contractType;
    private String employmentStatus;
    private BigDecimal baseSalary;
    private BigDecimal variableIncome;
    private BigDecimal totalMonthlyIncome;

    // Tipo de verificación solicitada
    @NotNull(message = "Verification type is required")
    @Pattern(regexp = "^(EMPLOYMENT|INCOME|BOTH|BASIC)$", message = "Invalid verification type")
    private String verificationType;

    // Información adicional para verificación de ingresos
    private Integer verificationPeriodMonths;
    @Builder.Default
    private Boolean includeVariableIncome = true;
    @Builder.Default
    private Boolean includeBonuses = true;
    @Builder.Default
    private Boolean includeOvertimeIncome = false;

    // Configuración de la consulta
    @Builder.Default
    private Boolean contactEmployer = true;
    @Builder.Default
    private Boolean requestPayrollHistory = false;
    @Builder.Default
    private Boolean requestTaxInformation = false;

    // Metadatos de la solicitud
    private String requestId;
    @Builder.Default
    private LocalDateTime requestDate = LocalDateTime.now();
    private String requestingEntity;
    private String requestingUser;
    private String purpose;
    private String priority;

    // Datos de contacto en la empresa
    private String hrContactName;
    private String hrContactPhone;
    private String hrContactEmail;
    private String supervisorName;
    private String supervisorPhone;
    private String supervisorEmail;

    // Autorización y consentimiento
    @Builder.Default
    private Boolean employeeConsent = false;
    private String consentDocument;
    private LocalDate consentDate;
}