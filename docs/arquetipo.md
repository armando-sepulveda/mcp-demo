# Arquetipo MCP Hexagonal para Banking - Crédito Automotriz

## Estructura del Proyecto

```
automotive-credit-mcp/
├── src/main/java/com/banco/automotive/credit/
│   ├── domain/                           # 🔵 CAPA DOMINIO (Núcleo del Hexágono)
│   │   ├── entities/                     # Entidades de negocio
│   │   │   ├── CreditApplication.java
│   │   │   ├── Customer.java
│   │   │   ├── Vehicle.java
│   │   │   └── CreditDecision.java
│   │   ├── valueobjects/                 # Value Objects
│   │   │   ├── CreditAmount.java
│   │   │   ├── CreditScore.java
│   │   │   ├── VehicleVIN.java
│   │   │   └── DocumentNumber.java
│   │   ├── enums/                        # Enumeraciones de dominio
│   │   │   ├── CreditStatus.java
│   │   │   ├── VehicleType.java
│   │   │   └── DocumentType.java
│   │   ├── exceptions/                   # Excepciones de dominio
│   │   │   ├── CreditDomainException.java
│   │   │   ├── InvalidCreditAmountException.java
│   │   │   └── InsufficientIncomeException.java
│   │   └── services/                     # Servicios de dominio (reglas complejas)
│   │       ├── CreditEligibilityService.java
│   │       ├── RiskCalculationService.java
│   │       └── InterestRateCalculationService.java
│   │
│   ├── application/                      # 🟡 CAPA APLICACIÓN (Casos de Uso)
│   │   ├── ports/                        # Puertos (Interfaces)
│   │   │   ├── in/                       # Puertos de entrada (driving ports)
│   │   │   │   ├── ProcessCreditApplicationUseCase.java
│   │   │   │   ├── GetCreditStatusUseCase.java
│   │   │   │   ├── ValidateDocumentsUseCase.java
│   │   │   │   └── CalculateInstallmentUseCase.java
│   │   │   └── out/                      # Puertos de salida (driven ports)
│   │   │       ├── CustomerRepositoryPort.java
│   │   │       ├── CreditApplicationRepositoryPort.java
│   │   │       ├── CreditScoreProviderPort.java
│   │   │       ├── VehicleValidationPort.java
│   │   │       ├── DocumentValidationPort.java
│   │   │       └── NotificationPort.java
│   │   ├── usecases/                     # Implementación de casos de uso
│   │   │   ├── ProcessCreditApplicationUseCaseImpl.java
│   │   │   ├── GetCreditStatusUseCaseImpl.java
│   │   │   ├── ValidateDocumentsUseCaseImpl.java
│   │   │   └── CalculateInstallmentUseCaseImpl.java
│   │   └── dto/                          # DTOs de aplicación (no confundir con DTOs de infra)
│   │       ├── CreditApplicationRequest.java
│   │       ├── CreditApplicationResponse.java
│   │       ├── DocumentValidationRequest.java
│   │       └── InstallmentCalculationResponse.java
│   │
│   ├── infrastructure/                   # 🔴 CAPA INFRAESTRUCTURA (Adaptadores)
│   │   ├── mcp/                          # 🚀 Capa MCP (Adaptadores primarios)
│   │   │   ├── server/                   # Configuración del servidor MCP
│   │   │   │   ├── AutomotiveCreditMcpServer.java
│   │   │   │   └── McpServerConfig.java
│   │   │   ├── tools/                    # 🔧 Herramientas MCP (Actions que puede realizar el agente)
│   │   │   │   ├── ProcessCreditApplicationTool.java
│   │   │   │   ├── ValidateCustomerDocumentsTool.java
│   │   │   │   ├── CalculateMonthlyInstallmentTool.java
│   │   │   │   ├── CheckVehicleEligibilityTool.java
│   │   │   │   └── GetCreditApplicationStatusTool.java
│   │   │   ├── resources/                # 📄 Recursos MCP (Información que puede consultar el agente)
│   │   │   │   ├── CreditPoliciesResource.java
│   │   │   │   ├── VehicleCatalogResource.java
│   │   │   │   ├── InterestRatesResource.java
│   │   │   │   └── RequiredDocumentsResource.java
│   │   │   ├── prompts/                  # 💬 Prompts MCP (Instrucciones y comportamiento del agente)
│   │   │   │   ├── CreditAnalystPrompt.java
│   │   │   │   ├── DocumentValidatorPrompt.java
│   │   │   │   ├── RiskAssessmentPrompt.java
│   │   │   │   └── CustomerServicePrompt.java
│   │   │   └── mappers/                  # Mappers entre dominio y MCP
│   │   │       ├── CreditApplicationMcpMapper.java
│   │   │       └── VehicleMcpMapper.java
│   │   │
│   │   ├── adapters/                     # Adaptadores secundarios (driven adapters)
│   │   │   ├── persistence/              # Adaptadores de persistencia
│   │   │   │   ├── jpa/
│   │   │   │   │   ├── entities/
│   │   │   │   │   │   ├── CreditApplicationJpaEntity.java
│   │   │   │   │   │   └── CustomerJpaEntity.java
│   │   │   │   │   ├── repositories/
│   │   │   │   │   │   ├── CreditApplicationJpaRepository.java
│   │   │   │   │   │   └── CustomerJpaRepository.java
│   │   │   │   │   └── CreditApplicationPersistenceAdapter.java
│   │   │   │   └── CustomerPersistenceAdapter.java
│   │   │   │
│   │   │   └── external/                 # 🌐 Adaptadores para servicios externos (Feign clients)
│   │   │       ├── clients/              # Clientes Feign
│   │   │       │   ├── CustomerServiceClient.java
│   │   │       │   ├── CreditScoreServiceClient.java
│   │   │       │   ├── VehicleValidationServiceClient.java
│   │   │       │   ├── DocumentValidationServiceClient.java
│   │   │       │   └── NotificationServiceClient.java
│   │   │       ├── dto/                  # DTOs para intercambio con servicios externos
│   │   │       │   ├── request/
│   │   │       │   │   ├── CustomerValidationRequestDto.java
│   │   │       │   │   ├── CreditScoreRequestDto.java
│   │   │       │   │   ├── VehicleValidationRequestDto.java
│   │   │       │   │   └── DocumentValidationRequestDto.java
│   │   │       │   └── response/
│   │   │       │       ├── CustomerValidationResponseDto.java
│   │   │       │       ├── CreditScoreResponseDto.java
│   │   │       │       ├── VehicleValidationResponseDto.java
│   │   │       │       └── DocumentValidationResponseDto.java
│   │   │       ├── fallbacks/            # Circuit Breaker Fallbacks
│   │   │       │   ├── CustomerServiceFallback.java
│   │   │       │   ├── CreditScoreServiceFallback.java
│   │   │       │   ├── VehicleValidationServiceFallback.java
│   │   │       │   └── DocumentValidationServiceFallback.java
│   │   │       ├── adapters/             # Implementaciones de puertos
│   │   │       │   ├── CustomerServiceAdapter.java
│   │   │       │   ├── CreditScoreProviderAdapter.java
│   │   │       │   ├── VehicleValidationAdapter.java
│   │   │       │   ├── DocumentValidationAdapter.java
│   │   │       │   └── NotificationAdapter.java
│   │   │       └── mappers/              # Mappers entre dominio y DTOs externos
│   │   │           ├── CustomerExternalMapper.java
│   │   │           ├── CreditScoreExternalMapper.java
│   │   │           └── VehicleExternalMapper.java
│   │   │
│   │   └── config/                       # Configuración de infraestructura
│   │       ├── FeignConfig.java
│   │       ├── CircuitBreakerConfig.java
│   │       ├── DatabaseConfig.java
│   │       └── McpConfiguration.java
│   │
│   └── shared/                           # 🔄 CAPA COMPARTIDA
│       ├── common/                       # Utilidades comunes
│       │   ├── utils/
│       │   │   ├── ValidationUtils.java
│       │   │   ├── DateUtils.java
│       │   │   └── EncryptionUtils.java
│       │   ├── constants/
│       │   │   ├── ErrorCodes.java
│       │   │   ├── BusinessConstants.java
│       │   │   └── McpConstants.java
│       │   └── annotations/
│       │       ├── DomainService.java
│       │       ├── UseCase.java
│       │       └── Adapter.java
│       └── monitoring/                   # Observabilidad y métricas
│           ├── metrics/
│           │   ├── McpMetrics.java
│           │   └── BusinessMetrics.java
│           └── logging/
│               ├── McpLoggerAspect.java
│               └── BusinessLoggerAspect.java
│
└── src/main/resources/
    ├── application.yml                   # Configuración principal
    ├── mcp/                             # Archivos de configuración MCP
    │   ├── prompts/                     # Templates de prompts
    │   │   ├── credit-analyst.md
    │   │   ├── document-validator.md
    │   │   └── risk-assessment.md
    │   └── policies/                    # Políticas de negocio
    │       ├── credit-policies.json
    │       ├── vehicle-catalog.json
    │       └── required-documents.json
    └── database/
        └── migrations/                  # Scripts de base de datos
```

## 📖 Guía de cada Capa

### 🔵 CAPA DOMINIO (Núcleo del Hexágono)

**Intención**: Contiene la lógica de negocio pura, independiente de frameworks y tecnología.

#### Entidad de Dominio - Ejemplo

```java
// domain/entities/CreditApplication.java
package com.banco.automotive.credit.domain.entities;

import com.banco.automotive.credit.domain.valueobjects.*;
import com.banco.automotive.credit.domain.enums.CreditStatus;
import java.time.LocalDateTime;
import java.util.List;

public class CreditApplication {
    private final CreditApplicationId id;
    private final Customer customer;
    private final Vehicle vehicle;
    private final CreditAmount requestedAmount;
    private CreditStatus status;
    private CreditScore creditScore;
    private final LocalDateTime applicationDate;
    private LocalDateTime lastUpdateDate;
    private final List<Document> documents;
    
    public CreditApplication(Customer customer, Vehicle vehicle, 
                           CreditAmount requestedAmount) {
        this.id = CreditApplicationId.generate();
        this.customer = customer;
        this.vehicle = vehicle;
        this.requestedAmount = requestedAmount;
        this.status = CreditStatus.PENDING;
        this.applicationDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
        this.documents = new ArrayList<>();
        
        validateInvariants();
    }
    
    // Métodos de negocio
    public void approve(CreditScore creditScore) {
        if (this.status != CreditStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden aprobar aplicaciones pendientes");
        }
        if (creditScore.getValue() < 600) {
            throw new InsufficientCreditScoreException("Score mínimo requerido: 600");
        }
        
        this.creditScore = creditScore;
        this.status = CreditStatus.APPROVED;
        this.lastUpdateDate = LocalDateTime.now();
    }
    
    public void reject(String reason) {
        if (this.status != CreditStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden rechazar aplicaciones pendientes");
        }
        
        this.status = CreditStatus.REJECTED;
        this.lastUpdateDate = LocalDateTime.now();
    }
    
    public boolean isEligibleForProcessing() {
        return hasAllRequiredDocuments() && 
               vehicle.isEligibleForCredit() && 
               customer.hasValidIncome();
    }
    
    private void validateInvariants() {
        if (requestedAmount.getValue().compareTo(vehicle.getMaxCreditAmount()) > 0) {
            throw new InvalidCreditAmountException(
                "Monto solicitado excede el máximo permitido para el vehículo");
        }
    }
    
    // Getters inmutables...
}
```

#### Value Object - Ejemplo

```java
// domain/valueobjects/CreditAmount.java
package com.banco.automotive.credit.domain.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

public class CreditAmount {
    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(50000);
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(2000000);
    
    private final BigDecimal value;
    
    public CreditAmount(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        if (value.compareTo(MIN_AMOUNT) < 0) {
            throw new IllegalArgumentException("Monto mínimo: " + MIN_AMOUNT);
        }
        if (value.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException("Monto máximo: " + MAX_AMOUNT);
        }
        
        this.value = value;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public CreditAmount add(CreditAmount other) {
        return new CreditAmount(this.value.add(other.value));
    }
    
    public boolean isGreaterThan(CreditAmount other) {
        return this.value.compareTo(other.value) > 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CreditAmount that = (CreditAmount) obj;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
```

#### Servicio de Dominio - Ejemplo

```java
// domain/services/CreditEligibilityService.java
package com.banco.automotive.credit.domain.services;

import com.banco.automotive.credit.domain.entities.*;
import com.banco.automotive.credit.domain.valueobjects.*;
import com.banco.automotive.credit.shared.common.annotations.DomainService;

@DomainService
public class CreditEligibilityService {
    
    public boolean isEligible(CreditApplication application) {
        return hasMinimumIncome(application) &&
               hasAcceptableCreditHistory(application) &&
               vehicleQualifiesForCredit(application) &&
               debtToIncomeRatioAcceptable(application);
    }
    
    public CreditAmount calculateMaxEligibleAmount(Customer customer, Vehicle vehicle) {
        BigDecimal monthlyIncome = customer.getMonthlyIncome().getValue();
        BigDecimal maxMonthlyPayment = monthlyIncome.multiply(BigDecimal.valueOf(0.30)); // 30% del ingreso
        
        // Usar tasa de interés estándar para calcular monto máximo
        BigDecimal annualInterestRate = BigDecimal.valueOf(0.12); // 12% anual
        int termInMonths = 60; // 5 años estándar
        
        BigDecimal maxCreditAmount = calculateLoanAmount(maxMonthlyPayment, 
                                                        annualInterestRate, 
                                                        termInMonths);
        
        // No puede exceder el valor del vehículo
        BigDecimal vehicleValue = vehicle.getValue().getValue();
        BigDecimal maxByVehicle = vehicleValue.multiply(BigDecimal.valueOf(0.90)); // 90% del valor
        
        BigDecimal finalAmount = maxCreditAmount.min(maxByVehicle);
        
        return new CreditAmount(finalAmount);
    }
    
    private boolean hasMinimumIncome(CreditApplication application) {
        BigDecimal minIncome = BigDecimal.valueOf(30000); // Mínimo mensual
        return application.getCustomer().getMonthlyIncome().getValue()
                .compareTo(minIncome) >= 0;
    }
    
    private boolean hasAcceptableCreditHistory(CreditApplication application) {
        // Lógica compleja de evaluación de historial crediticio
        return application.getCustomer().getCreditHistory().isAcceptable();
    }
    
    private boolean vehicleQualifiesForCredit(CreditApplication application) {
        Vehicle vehicle = application.getVehicle();
        return vehicle.getYear() >= 2018 && // No más de 6 años
               vehicle.getKilometers() <= 100000 && // Máximo 100k km
               vehicle.isFromApprovedBrand();
    }
    
    private boolean debtToIncomeRatioAcceptable(CreditApplication application) {
        // Ratio deuda/ingreso no debe exceder 40%
        BigDecimal monthlyIncome = application.getCustomer().getMonthlyIncome().getValue();
        BigDecimal currentDebts = application.getCustomer().getCurrentMonthlyDebts().getValue();
        
        BigDecimal ratio = currentDebts.divide(monthlyIncome, 4, BigDecimal.ROUND_HALF_UP);
        return ratio.compareTo(BigDecimal.valueOf(0.40)) <= 0;
    }
    
    private BigDecimal calculateLoanAmount(BigDecimal monthlyPayment, 
                                         BigDecimal annualRate, 
                                         int termInMonths) {
        // Fórmula de cálculo de préstamo
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 6, BigDecimal.ROUND_HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(termInMonths);
        
        BigDecimal denominator = monthlyRate.multiply(onePlusRPowN);
        BigDecimal numerator = onePlusRPowN.subtract(BigDecimal.ONE);
        
        return monthlyPayment.multiply(numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP));
    }
}
```

### 🟡 CAPA APLICACIÓN (Orquestación de Casos de Uso)

**Intención**: Coordina el flujo de la aplicación y orquesta las operaciones de dominio.

#### Puerto de Entrada - Ejemplo

```java
// application/ports/in/ProcessCreditApplicationUseCase.java
package com.banco.automotive.credit.application.ports.in;

import com.banco.automotive.credit.application.dto.CreditApplicationRequest;
import com.banco.automotive.credit.application.dto.CreditApplicationResponse;

public interface ProcessCreditApplicationUseCase {
    CreditApplicationResponse processApplication(CreditApplicationRequest request);
}
```

#### Puerto de Salida - Ejemplo

```java
// application/ports/out/CustomerRepositoryPort.java
package com.banco.automotive.credit.application.ports.out;

import com.banco.automotive.credit.domain.entities.Customer;
import com.banco.automotive.credit.domain.valueobjects.DocumentNumber;
import java.util.Optional;

public interface CustomerRepositoryPort {
    Optional<Customer> findByDocumentNumber(DocumentNumber documentNumber);
    Customer save(Customer customer);
    boolean existsByDocumentNumber(DocumentNumber documentNumber);
}
```

#### Caso de Uso - Ejemplo

```java
// application/usecases/ProcessCreditApplicationUseCaseImpl.java
package com.banco.automotive.credit.application.usecases;

import com.banco.automotive.credit.application.ports.in.ProcessCreditApplicationUseCase;
import com.banco.automotive.credit.application.ports.out.*;
import com.banco.automotive.credit.application.dto.*;
import com.banco.automotive.credit.domain.entities.*;
import com.banco.automotive.credit.domain.services.*;
import com.banco.automotive.credit.shared.common.annotations.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class ProcessCreditApplicationUseCaseImpl implements ProcessCreditApplicationUseCase {
    
    private final CustomerRepositoryPort customerRepository;
    private final CreditApplicationRepositoryPort creditApplicationRepository;
    private final CreditScoreProviderPort creditScoreProvider;
    private final VehicleValidationPort vehicleValidation;
    private final DocumentValidationPort documentValidation;
    private final NotificationPort notificationPort;
    
    private final CreditEligibilityService creditEligibilityService;
    private final RiskCalculationService riskCalculationService;
    
    @Override
    public CreditApplicationResponse processApplication(CreditApplicationRequest request) {
        log.info("Procesando solicitud de crédito para cliente: {}", request.getCustomerDocument());
        
        try {
            // 1. Validar cliente
            Customer customer = validateAndGetCustomer(request);
            
            // 2. Validar vehículo
            Vehicle vehicle = validateVehicle(request);
            
            // 3. Crear aplicación de crédito
            CreditApplication application = new CreditApplication(
                customer, 
                vehicle, 
                new CreditAmount(request.getRequestedAmount())
            );
            
            // 4. Validar documentos
            validateDocuments(application, request.getDocuments());
            
            // 5. Evaluar elegibilidad
            if (!creditEligibilityService.isEligible(application)) {
                application.reject("No cumple criterios de elegibilidad");
                creditApplicationRepository.save(application);
                
                return CreditApplicationResponse.rejected(
                    application.getId().getValue(),
                    "No cumple con los criterios de elegibilidad"
                );
            }
            
            // 6. Obtener score crediticio
            CreditScore creditScore = creditScoreProvider.getCreditScore(customer.getDocumentNumber());
            
            // 7. Evaluar riesgo
            RiskAssessment riskAssessment = riskCalculationService.calculateRisk(application, creditScore);
            
            // 8. Tomar decisión
            if (riskAssessment.isApproved()) {
                application.approve(creditScore);
                
                // 9. Enviar notificación
                notificationPort.sendApprovalNotification(customer, application);
                
                log.info("Solicitud aprobada para cliente: {}", customer.getDocumentNumber().getValue());
                
                return CreditApplicationResponse.approved(
                    application.getId().getValue(),
                    creditScore.getValue(),
                    riskAssessment.getApprovedAmount()
                );
            } else {
                application.reject(riskAssessment.getRejectionReason());
                
                notificationPort.sendRejectionNotification(customer, application, riskAssessment.getRejectionReason());
                
                log.info("Solicitud rechazada para cliente: {}", customer.getDocumentNumber().getValue());
                
                return CreditApplicationResponse.rejected(
                    application.getId().getValue(),
                    riskAssessment.getRejectionReason()
                );
            }
            
        } catch (Exception e) {
            log.error("Error procesando solicitud de crédito", e);
            throw new CreditApplicationProcessingException("Error procesando solicitud", e);
        } finally {
            // Guardar aplicación
            creditApplicationRepository.save(application);
        }
    }
    
    private Customer validateAndGetCustomer(CreditApplicationRequest request) {
        DocumentNumber documentNumber = new DocumentNumber(request.getCustomerDocument());
        
        return customerRepository.findByDocumentNumber(documentNumber)
            .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado: " + request.getCustomerDocument()));
    }
    
    private Vehicle validateVehicle(CreditApplicationRequest request) {
        VehicleValidationResult validation = vehicleValidation.validateVehicle(
            request.getVehicleVin(),
            request.getVehicleBrand(),
            request.getVehicleModel(),
            request.getVehicleYear()
        );
        
        if (!validation.isValid()) {
            throw new InvalidVehicleException("Vehículo no válido: " + validation.getErrorMessage());
        }
        
        return validation.getVehicle();
    }
    
    private void validateDocuments(CreditApplication application, List<DocumentRequest> documents) {
        DocumentValidationResult result = documentValidation.validateDocuments(
            application.getCustomer().getDocumentNumber(),
            documents
        );
        
        if (!result.isValid()) {
            throw new InvalidDocumentsException("Documentos inválidos: " + result.getErrorMessage());
        }
    }
}
```

### 🔴 CAPA INFRAESTRUCTURA - Adaptadores MCP

#### Tool MCP - Ejemplo

```java
// infrastructure/mcp/tools/ProcessCreditApplicationTool.java
package com.banco.automotive.credit.infrastructure.mcp.tools;

import com.banco.automotive.credit.application.ports.in.ProcessCreditApplicationUseCase;
import com.banco.automotive.credit.application.dto.*;
import com.banco.automotive.credit.infrastructure.mcp.mappers.CreditApplicationMcpMapper;
import org.springframework.ai.mcp.server.tool.Tool;
import org.springframework.ai.mcp.server.tool.ToolParameter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessCreditApplicationTool {
    
    private final ProcessCreditApplicationUseCase processCreditApplicationUseCase;
    private final CreditApplicationMcpMapper mapper;
    
    @Tool(name = "process_credit_application", 
          description = "Procesa una nueva solicitud de crédito automotriz. Evalúa la elegibilidad del cliente, valida documentos, verifica el vehículo y toma una decisión crediticia.")
    public String processApplication(
            @ToolParameter(name = "customer_document", 
                          description = "Número de documento del cliente (cédula, pasaporte, etc.)",
                          required = true) 
            String customerDocument,
            
            @ToolParameter(name = "requested_amount", 
                          description = "Monto solicitado del crédito en pesos colombianos",
                          required = true) 
            String requestedAmount,
            
            @ToolParameter(name = "vehicle_vin", 
                          description = "Número VIN del vehículo",
                          required = true) 
            String vehicleVin,
            
            @ToolParameter(name = "vehicle_brand", 
                          description = "Marca del vehículo",
                          required = true) 
            String vehicleBrand,
            
            @ToolParameter(name = "vehicle_model", 
                          description = "Modelo del vehículo",
                          required = true) 
            String vehicleModel,
            
            @ToolParameter(name = "vehicle_year", 
                          description = "Año del vehículo",
                          required = true) 
            String vehicleYear,
            
            @ToolParameter(name = "documents", 
                          description = "Lista de documentos presentados en formato JSON",
                          required = true) 
            String documentsJson) {
        
        try {
            log.info("Procesando solicitud de crédito vía MCP para cliente: {}", customerDocument);
            
            // Mapear parámetros a DTO de aplicación
            CreditApplicationRequest request = mapper.mapToApplicationRequest(
                customerDocument, requestedAmount, vehicleVin, vehicleBrand, 
                vehicleModel, vehicleYear, documentsJson
            );
            
            // Procesar aplicación
            CreditApplicationResponse response = processCreditApplicationUseCase.processApplication(request);
            
            // Formatear respuesta para el agente de IA
            return formatResponseForAgent(response);
            
        } catch (Exception e) {
            log.error("Error procesando solicitud de crédito vía MCP", e);
            return formatErrorResponse(e.getMessage());
        }
    }
    
    private String formatResponseForAgent(CreditApplicationResponse response) {
        StringBuilder result = new StringBuilder();
        
        result.append("📋 **RESULTADO DE SOLICITUD DE CRÉDITO AUTOMOTRIZ**\n\n");
        result.append("🆔 **ID de Solicitud:** ").append(response.getApplicationId()).append("\n");
        result.append("📊 **Estado:** ").append(response.getStatus()).append("\n");
        
        if (response.isApproved()) {
            result.append("✅ **SOLICITUD APROBADA**\n\n");
            result.append("💰 **Monto Aprobado:** $").append(formatCurrency(response.getApprovedAmount())).append("\n");
            result.append("📈 **Score Crediticio:** ").append(response.getCreditScore()).append("\n");
            result.append("⏰ **Plazo Máximo:** 60 meses\n");
            result.append("🎯 **Tasa de Interés:** ").append(response.getInterestRate()).append("% anual\n");
            result.append("\n📝 **Próximos Pasos:**\n");
            result.append("1. El cliente debe firmar el contrato de crédito\n");
            result.append("2. Presentar los documentos originales para verificación\n");
            result.append("3. Programar cita para desembolso\n");
            
        } else {
            result.append("❌ **SOLICITUD RECHAZADA**\n\n");
            result.append("📋 **Motivo:** ").append(response.getRejectionReason()).append("\n");
            result.append("\n💡 **Recomendaciones:**\n");
            result.append("- Revisar historial crediticio\n");
            result.append("- Considerar un monto menor\n");
            result.append("- Añadir un codeudor si es necesario\n");
        }
        
        return result.toString();
    }
    
    private String formatErrorResponse(String errorMessage) {
        return String.format("""
            ⚠️ **ERROR EN PROCESAMIENTO**
            
            ❌ **Error:** %s
            
            🔧 **Acción Requerida:**
            - Verificar que todos los datos estén correctos
            - Contactar soporte técnico si el problema persiste
            """, errorMessage);
    }
    
    private String formatCurrency(BigDecimal amount) {
        return NumberFormat.getCurrencyInstance(new Locale("es", "CO"))
                .format(amount);
    }
}
```

#### Resource MCP - Ejemplo

```java
// infrastructure/mcp/resources/CreditPoliciesResource.java
package com.banco.automotive.credit.infrastructure.mcp.resources;

import org.springframework.ai.mcp.server.resource.Resource;
import org.springframework.ai.mcp.server.resource.ResourceContent;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreditPoliciesResource {
    
    @Resource(name = "credit_policies", 
              description = "Políticas y criterios de evaluación crediticia para créditos automotrices")
    public String getCreditPolicies() {
        return """
            # POLÍTICAS DE CRÉDITO AUTOMOTRIZ - BANCO XYZ
            
            ## 📋 CRITERIOS DE ELEGIBILIDAD
            
            ### 👤 Cliente
            - ✅ Edad mínima: 18 años, máxima: 70 años al final del crédito
            - ✅ Ingresos mínimos: $2,500,000 pesos mensuales demostrables
            - ✅ Antigüedad laboral: Mínimo 12 meses (empleados) o 24 meses (independientes)
            - ✅ Score crediticio mínimo: 600 puntos
            - ✅ Capacidad de pago: Máximo 30% de ingresos para cuota del crédito
            - ✅ Ratio deuda/ingreso total: Máximo 40%
            
            ### 🚗 Vehículo
            - ✅ Año mínimo: 2018 (máximo 6 años de antigüedad)
            - ✅ Kilometraje máximo: 100,000 km
            - ✅ Marcas autorizadas: Toyota, Chevrolet, Renault, Nissan, Hyundai, KIA, Mazda, Ford
            - ✅ Valor mínimo: $50,000,000 pesos
            - ✅ Valor máximo: $300,000,000 pesos
            - ✅ Debe tener SOAT vigente y revisión técnico-mecánica
            
            ## 💰 CONDICIONES CREDITICIAS
            
            ### Montos y Plazos
            - 💵 Monto mínimo: $20,000,000
            - 💵 Monto máximo: $200,000,000
            - 💵 Financiación máxima: 90% del valor del vehículo
            - ⏱️ Plazo mínimo: 12 meses
            - ⏱️ Plazo máximo: 84 meses
            
            ### Tasas de Interés (EA)
            - 📊 Score 750+: 12% - 14%
            - 📊 Score 700-749: 14% - 16%
            - 📊 Score 650-699: 16% - 18%
            - 📊 Score 600-649: 18% - 20%
            
            ## 📄 DOCUMENTOS REQUERIDOS
            
            ### Personas Naturales
            - 🆔 Cédula de ciudadanía (original y copia)
            - 💼 Certificado laboral (no mayor a 30 días)
            - 💰 Últimas 3 nóminas o certificado de ingresos
            - 🏦 Extractos bancarios últimos 3 meses
            - 📋 Declaración de renta (si aplica)
            - 🏠 Certificado de ingresos familiares (si aplica)
            
            ### Documentos del Vehículo
            - 📜 Tarjeta de propiedad
            - 🔍 Tecnomecánica vigente
            - 🛡️ SOAT vigente
            - 💵 Avalúo comercial (no mayor a 15 días)
            - 📋 Inspección técnica del banco
            
            ## ⚠️ CRITERIOS DE EXCLUSIÓN
            
            - ❌ Reportes negativos en centrales de riesgo por mora superior a 60 días
            - ❌ Procesos judiciales o embargo vigentes
            - ❌ Antecedentes de fraude o lavado de activos
            - ❌ Vinculación en listas de control (OFAC, ONU, etc.)
            - ❌ Vehículos con problemas legales o prendas vigentes
            - ❌ Actividades económicas de alto riesgo no autorizadas
            
            ## 📈 PROCESO DE EVALUACIÓN
            
            1. **Validación de Documentos** (1-2 días)
            2. **Evaluación Crediticia** (2-3 días)
            3. **Inspección del Vehículo** (1 día)
            4. **Comité de Crédito** (1-2 días)
            5. **Aprobación Final** (1 día)
            
            **Tiempo total estimado: 5-8 días hábiles**
            """;
    }
    
    @Resource(name = "interest_rates", 
              description = "Tabla actualizada de tasas de interés según score crediticio")
    public String getInterestRates() {
        return """
            # 📊 TABLA DE TASAS DE INTERÉS CRÉDITO AUTOMOTRIZ
            
            ## Tasas Efectivas Anuales Vigentes
            
            | Score Crediticio | Tasa Mínima | Tasa Máxima | Promedio |
            |------------------|-------------|-------------|----------|
            | 800+ (Excelente) | 10.5%       | 12.0%       | 11.25%   |
            | 750-799 (Muy Bueno) | 12.0%   | 14.0%       | 13.0%    |
            | 700-749 (Bueno)  | 14.0%      | 16.0%       | 15.0%    |
            | 650-699 (Regular) | 16.0%     | 18.0%       | 17.0%    |
            | 600-649 (Mínimo) | 18.0%      | 20.0%       | 19.0%    |
            
            ## Factores que Afectan la Tasa
            
            ### 📈 Factores que MEJORAN la tasa:
            - Cliente con productos existentes en el banco
            - Antigüedad laboral superior a 3 años
            - Ingresos superiores a $5,000,000
            - Pago de cuota inicial superior al 20%
            - Vehículo nuevo o seminuevo (menos de 2 años)
            
            ### 📉 Factores que INCREMENTAN la tasa:
            - Primer crédito con el banco
            - Ingresos variables o independientes
            - Vehículo usado (más de 3 años)
            - Financiación superior al 80% del valor
            - Score en el rango mínimo aceptable
            
            *Última actualización: $(date)*
            *Tasas sujetas a cambios según condiciones del mercado*
            """;
    }
}
```

#### Prompt MCP - Ejemplo

```java
// infrastructure/mcp/prompts/CreditAnalystPrompt.java
package com.banco.automotive.credit.infrastructure.mcp.prompts;

import org.springframework.ai.mcp.server.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class CreditAnalystPrompt {
    
    @Prompt(name = "credit_analyst", 
            description = "Prompt principal para el agente analista de crédito automotriz")
    public String getCreditAnalystPrompt() {
        return """
            # 🏦 AGENTE ANALISTA DE CRÉDITO AUTOMOTRIZ - BANCO XYZ
            
            Eres un **Analista de Crédito Senior especializado en créditos automotrices** con más de 10 años de experiencia en el sector financiero colombiano. Tu misión es ayudar a los clientes a obtener el mejor crédito automotriz posible, evaluando su capacidad de pago y guiándolos en el proceso.
            
            ## 🎯 TU PERSONALIDAD Y ESTILO
            
            - **Profesional pero cercano**: Mantén un tono experto pero amigable
            - **Orientado a soluciones**: Siempre busca alternativas cuando hay obstáculos
            - **Detallista**: Explica claramente cada paso y requisito
            - **Proactivo**: Anticipa dudas y ofrece información relevante
            - **Ético**: Siempre actúa en el mejor interés del cliente y el banco
            
            ## 📋 TUS RESPONSABILIDADES PRINCIPALES
            
            ### 1. Evaluación Crediticia
            - Analizar la capacidad de pago del cliente
            - Evaluar el perfil de riesgo según políticas del banco
            - Revisar documentación y verificar autenticidad
            - Calcular cuotas y simular diferentes escenarios
            
            ### 2. Asesoría al Cliente
            - Explicar el proceso paso a paso
            - Recomendar el mejor producto según el perfil
            - Sugerir mejoras para obtener mejores condiciones
            - Resolver dudas sobre tasas, plazos y condiciones
            
            ### 3. Gestión de Solicitudes
            - Procesar aplicaciones de crédito completas
            - Validar información de clientes y vehículos
            - Coordinar inspecciones y avalúos
            - Seguimiento hasta el desembolso
            
            ## 🛠️ HERRAMIENTAS DISPONIBLES
            
            Tienes acceso a las siguientes herramientas especializadas:
            
            - `process_credit_application`: Para procesar solicitudes completas
            - `validate_customer_documents`: Para verificar documentación
            - `calculate_monthly_installment`: Para simular cuotas
            - `check_vehicle_eligibility`: Para validar vehículos
            - `get_credit_application_status`: Para consultar estados
            
            ## 📚 RECURSOS DE CONSULTA
            
            Consulta siempre estos recursos antes de responder:
            
            - `credit_policies`: Políticas y criterios actualizados
            - `vehicle_catalog`: Marcas y modelos autorizados
            - `interest_rates`: Tasas vigentes por score
            - `required_documents`: Lista de documentos necesarios
            
            ## 🗣️ GUÍAS DE COMUNICACIÓN
            
            ### Al recibir una consulta:
            1. **Saluda cordialmente** y presenta tu rol
            2. **Comprende la necesidad** haciendo preguntas específicas
            3. **Consulta las políticas** para dar información precisa
            4. **Ofrece soluciones concretas** con pasos claros
            5. **Confirma entendimiento** antes de proceder
            
            ### Al procesar solicitudes:
            1. **Valida información completa** antes de procesar
            2. **Explica cada paso** del proceso de evaluación
            3. **Comunica resultados claramente** (aprobado/rechazado)
            4. **Ofrece alternativas** si hay problemas
            5. **Define próximos pasos** específicos
            
            ### Ejemplos de respuesta:
            
            **Para consultas generales:**
            "¡Hola! Soy tu Analista de Crédito Automotriz. Te ayudo a encontrar la mejor opción para financiar tu vehículo. Para darte una asesoría personalizada, cuéntame: ¿qué tipo de vehículo estás buscando y cuál es tu presupuesto aproximado?"
            
            **Para solicitudes:**
            "Perfecto, voy a procesar tu solicitud paso a paso. Primero validaré tu información, luego evaluaré el vehículo y finalmente calcularé las mejores condiciones para ti. ¿Tienes todos los documentos requeridos?"
            
            ## ⚠️ RESTRICCIONES IMPORTANTES
            
            - **NUNCA** apruebes créditos fuera de las políticas establecidas
            - **SIEMPRE** verifica la información antes de procesar
            - **NO** hagas promesas sobre aprobaciones sin evaluación
            - **PROTEGE** la información confidencial del cliente
            - **CUMPLE** estrictamente con las regulaciones financieras
            
            ## 🎯 OBJETIVOS DE RENDIMIENTO
            
            - **Precisión**: 95%+ en evaluaciones crediticias
            - **Satisfacción**: Respuestas claras y útiles siempre
            - **Eficiencia**: Procesar solicitudes en menos de 5 minutos
            - **Cumplimiento**: 100% adherencia a políticas bancarias
            
            Recuerda: Tu objetivo es crear una experiencia excepcional para el cliente mientras proteges los intereses del banco. ¡Cada interacción cuenta para construir confianza y relaciones duraderas!
            """;
    }
    
    @Prompt(name = "risk_assessment", 
            description = "Prompt especializado para evaluación de riesgo crediticio")
    public String getRiskAssessmentPrompt() {
        return """
            # ⚖️ EVALUADOR DE RIESGO CREDITICIO AUTOMOTRIZ
            
            Eres un especialista en **evaluación de riesgo crediticio** para créditos automotrices. Tu función es analizar exhaustivamente cada solicitud para determinar el nivel de riesgo y recomendar aprobación, condiciones especiales o rechazo.
            
            ## 📊 CRITERIOS DE EVALUACIÓN DE RIESGO
            
            ### 🔴 RIESGO ALTO (Rechazo recomendado)
            - Score crediticio < 600
            - Mora actual > 60 días en cualquier obligación
            - Ratio deuda/ingreso > 50%
            - Ingresos no demostrables o inconsistentes
            - Antecedentes de fraude o lavado
            - Vehículo con problemas legales
            
            ### 🟡 RIESGO MEDIO (Condiciones especiales)
            - Score crediticio 600-650
            - Mora histórica < 60 días (últimos 12 meses)
            - Ratio deuda/ingreso 40-50%
            - Ingresos variables pero demostrables
            - Primera vez con el banco
            - Vehículo usado > 5 años
            
            ### 🟢 RIESGO BAJO (Aprobación recomendada)
            - Score crediticio > 700
            - Sin reportes negativos últimos 24 meses
            - Ratio deuda/ingreso < 30%
            - Ingresos estables y demostrables
            - Cliente existente con buen comportamiento
            - Vehículo nuevo o seminuevo
            
            ## 🧮 METODOLOGÍA DE ANÁLISIS
            
            Al evaluar riesgo, considera:
            
            1. **Capacidad de pago** (40% del peso):
               - Ingresos vs gastos
               - Estabilidad laboral
               - Flujo de caja proyectado
            
            2. **Historial crediticio** (30% del peso):
               - Score y tendencia
               - Comportamiento de pago
               - Experiencia con créditos similares
            
            3. **Garantía del vehículo** (20% del peso):
               - Valor y depreciación
               - Liquidez en el mercado
               - Estado y antigüedad
            
            4. **Factores cualitativos** (10% del peso):
               - Perfil del cliente
               - Propósito del crédito
               - Referencias y verificaciones
            
            ## 📋 FORMATO DE EVALUACIÓN
            
            Para cada evaluación, proporciona:
            
            ```
            🎯 RESULTADO: [APROBADO/CONDICIONADO/RECHAZADO]
            📊 NIVEL DE RIESGO: [BAJO/MEDIO/ALTO]
            📈 SCORE DE RIESGO: [1-100]
            
            📋 ANÁLISIS DETALLADO:
            ✅ Fortalezas:
            - [Lista de factores positivos]
            
            ⚠️ Riesgos identificados:
            - [Lista de factores de riesgo]
            
            💡 Recomendaciones:
            - [Condiciones sugeridas o mejoras]
            
            💰 CONDICIONES PROPUESTAS:
            - Monto máximo: $XX,XXX,XXX
            - Plazo máximo: XX meses
            - Tasa sugerida: XX.X%
            - Cuota inicial mínima: XX%
            ```
            
            Mantén siempre objetividad y base tus decisiones en datos verificables y políticas establecidas.
            """;
    }
}
```

### 🌐 CAPA INFRAESTRUCTURA - Cliente Feign

#### Cliente Feign - Ejemplo

```java
// infrastructure/adapters/external/clients/CustomerServiceClient.java
package com.banco.automotive.credit.infrastructure.adapters.external.clients;

import com.banco.automotive.credit.infrastructure.adapters.external.dto.request.CustomerValidationRequestDto;
import com.banco.automotive.credit.infrastructure.adapters.external.dto.response.CustomerValidationResponseDto;
import com.banco.automotive.credit.infrastructure.adapters.external.fallbacks.CustomerServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "customer-service",
    url = "${services.customer.url}",
    fallback = CustomerServiceFallback.class,
    configuration = FeignConfig.class
)
public interface CustomerServiceClient {
    
    @PostMapping("/api/v1/customers/validate")
    CustomerValidationResponseDto validateCustomer(
        @RequestBody CustomerValidationRequestDto request,
        @RequestHeader("Authorization") String authorization
    );
    
    @GetMapping("/api/v1/customers/{documentNumber}")
    CustomerValidationResponseDto getCustomerByDocument(
        @PathVariable("documentNumber") String documentNumber,
        @RequestHeader("Authorization") String authorization
    );
    
    @GetMapping("/api/v1/customers/{documentNumber}/income-verification")
    IncomeVerificationResponseDto verifyIncome(
        @PathVariable("documentNumber") String documentNumber,
        @RequestHeader("Authorization") String authorization
    );
}
```

#### Fallback - Ejemplo

```java
// infrastructure/adapters/external/fallbacks/CustomerServiceFallback.java
package com.banco.automotive.credit.infrastructure.adapters.external.fallbacks;

import com.banco.automotive.credit.infrastructure.adapters.external.clients.CustomerServiceClient;
import com.banco.automotive.credit.infrastructure.adapters.external.dto.request.CustomerValidationRequestDto;
import com.banco.automotive.credit.infrastructure.adapters.external.dto.response.CustomerValidationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerServiceFallback implements CustomerServiceClient {
    
    @Override
    public CustomerValidationResponseDto validateCustomer(
            CustomerValidationRequestDto request, 
            String authorization) {
        
        log.warn("Circuit breaker activo para validateCustomer - documento: {}", 
                request.getDocumentNumber());
        
        return CustomerValidationResponseDto.builder()
            .documentNumber(request.getDocumentNumber())
            .valid(false)
            .errorCode("SERVICE_UNAVAILABLE")
            .errorMessage("Servicio de validación de clientes temporalmente no disponible. Intente más tarde.")
            .fallbackActivated(true)
            .build();
    }
    
    @Override
    public CustomerValidationResponseDto getCustomerByDocument(
            String documentNumber, 
            String authorization) {
        
        log.warn("Circuit breaker activo para getCustomerByDocument - documento: {}", documentNumber);
        
        return CustomerValidationResponseDto.builder()
            .documentNumber(documentNumber)
            .valid(false)
            .errorCode("SERVICE_UNAVAILABLE")
            .errorMessage("Servicio de consulta de clientes temporalmente no disponible.")
            .fallbackActivated(true)
            .build();
    }
    
    @Override
    public IncomeVerificationResponseDto verifyIncome(
            String documentNumber, 
            String authorization) {
        
        log.warn("Circuit breaker activo para verifyIncome - documento: {}", documentNumber);
        
        return IncomeVerificationResponseDto.builder()
            .documentNumber(documentNumber)
            .verified(false)
            .errorCode("SERVICE_UNAVAILABLE")
            .errorMessage("Servicio de verificación de ingresos temporalmente no disponible.")
            .fallbackActivated(true)
            .build();
    }
}
```

#### Adaptador de Infraestructura - Ejemplo

```java
// infrastructure/adapters/external/adapters/CustomerServiceAdapter.java
package com.banco.automotive.credit.infrastructure.adapters.external.adapters;

import com.banco.automotive.credit.application.ports.out.CustomerRepositoryPort;
import com.banco.automotive.credit.domain.entities.Customer;
import com.banco.automotive.credit.domain.valueobjects.DocumentNumber;
import com.banco.automotive.credit.infrastructure.adapters.external.clients.CustomerServiceClient;
import com.banco.automotive.credit.infrastructure.adapters.external.dto.request.CustomerValidationRequestDto;
import com.banco.automotive.credit.infrastructure.adapters.external.dto.response.CustomerValidationResponseDto;
import com.banco.automotive.credit.infrastructure.adapters.external.mappers.CustomerExternalMapper;
import com.banco.automotive.credit.shared.common.annotations.Adapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceAdapter implements CustomerRepositoryPort {
    
    private final CustomerServiceClient customerServiceClient;
    private final CustomerExternalMapper customerMapper;
    private final SecurityContextService securityContextService;
    
    @Override
    public Optional<Customer> findByDocumentNumber(DocumentNumber documentNumber) {
        try {
            log.info("Consultando cliente por documento: {}", documentNumber.getValue());
            
            String authToken = securityContextService.getCurrentAuthToken();
            
            CustomerValidationResponseDto response = customerServiceClient
                .getCustomerByDocument(documentNumber.getValue(), authToken);
            
            if (response.isFallbackActivated()) {
                log.warn("Fallback activado para consulta de cliente: {}", documentNumber.getValue());
                throw new ExternalServiceUnavailableException(
                    "Servicio de clientes no disponible", response.getErrorMessage());
            }
            
            if (!response.isValid()) {
                log.info("Cliente no encontrado: {}", documentNumber.getValue());
                return Optional.empty();
            }
            
            Customer customer = customerMapper.toDomain(response);
            
            log.info("Cliente encontrado exitosamente: {}", documentNumber.getValue());
            return Optional.of(customer);
            
        } catch (FeignException.NotFound e) {
            log.info("Cliente no encontrado en servicio externo: {}", documentNumber.getValue());
            return Optional.empty();
            
        } catch (FeignException e) {
            log.error("Error consultando servicio de clientes", e);
            throw new ExternalServiceException("Error consultando cliente", e);
        }
    }
    
    @Override
    public Customer save(Customer customer) {
        // En este caso, el servicio de clientes es de solo lectura
        // La creación/actualización se maneja en otro servicio
        throw new UnsupportedOperationException(
            "La creación de clientes se maneja en el servicio de onboarding");
    }
    
    @Override
    public boolean existsByDocumentNumber(DocumentNumber documentNumber) {
        try {
            CustomerValidationRequestDto request = CustomerValidationRequestDto.builder()
                .documentNumber(documentNumber.getValue())
                .validationType("EXISTENCE_CHECK")
                .build();
            
            String authToken = securityContextService.getCurrentAuthToken();
            
            CustomerValidationResponseDto response = customerServiceClient
                .validateCustomer(request, authToken);
            
            if (response.isFallbackActivated()) {
                throw new ExternalServiceUnavailableException(
                    "Servicio de validación no disponible", response.getErrorMessage());
            }
            
            return response.isValid();
            
        } catch (FeignException e) {
            log.error("Error validando existencia de cliente", e);
            throw new ExternalServiceException("Error validando cliente", e);
        }
    }
}
```

## 🔧 Configuración

### application.yml

```yaml
# application.yml
spring:
  application:
    name: automotive-credit-mcp
  
  # Configuración MCP
  ai:
    mcp:
      server:
        transport: sse
        capabilities:
          tools: true
          resources: true
          prompts: true
          completions: true
        
  # Base de datos
  datasource:
    url: jdbc:postgresql://localhost:5432/automotive_credit
    username: ${DB_USERNAME:credit_user}
    password: ${DB_PASSWORD:credit_pass}
    
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        
  # Cliente Feign
  cloud:
    openfeign:
      client:
        config:
          default:
            connectTimeout: 5000
            readTimeout: 10000
            loggerLevel: basic
          customer-service:
            connectTimeout: 3000
            readTimeout: 8000
          credit-score-service:
            connectTimeout: 2000
            readTimeout: 5000

# Configuración de servicios externos
services:
  customer:
    url: ${CUSTOMER_SERVICE_URL:http://customer-service:8080}
  credit-score:
    url: ${CREDIT_SCORE_SERVICE_URL:http://credit-score-service:8080}
  vehicle-validation:
    url: ${VEHICLE_SERVICE_URL:http://vehicle-service:8080}
  document-validation:
    url: ${DOCUMENT_SERVICE_URL:http://document-service:8080}
  notification:
    url: ${NOTIFICATION_SERVICE_URL:http://notification-service:8080}

# Circuit Breaker
resilience4j:
  circuitbreaker:
    configs:
      default:
        slidingWindowSize: 20
        minimumNumberOfCalls: 5
        waitDurationInOpenState: 30s
        failureRateThreshold: 50
        permittedNumberOfCallsInHalfOpenState: 3
    instances:
      customer-service:
        baseConfig: default
      credit-score-service:
        baseConfig: default
        failureRateThreshold: 60
        waitDurationInOpenState: 60s

# Métricas y monitoreo
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus,mcp
  endpoint:
    health:
      show-details: always
    metrics:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true

# Logging
logging:
  level:
    com.banco.automotive.credit: INFO
    org.springframework.cloud.openfeign: DEBUG
    org.springframework.ai.mcp: DEBUG
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

## 📊 Buenas Prácticas Implementadas

### 1. **Separación Clara de Responsabilidades**

- Dominio: Solo lógica de negocio pura
- Aplicación: Orquestación sin lógica de negocio
- Infraestructura: Detalles técnicos e integraciones

### 2. **Inversión de Dependencias**

- Puertos definen contratos
- Adaptadores implementan detalles
- Dominio no depende de infraestructura

### 3. **Resilencia y Fault Tolerance**

- Circuit breakers para servicios externos
- Fallbacks informativos
- Timeouts configurables

### 4. **Observabilidad**

- Logging estructurado
- Métricas de negocio y técnicas
- Trazabilidad de transacciones

### 5. **Seguridad**

- Tokens JWT para servicios externos
- Validación en cada capa
- Cifrado de datos sensibles

Este arquetipo proporciona una base sólida para implementar MCPs bancarios con arquitectura hexagonal, manteniendo la flexibilidad para crecer y adaptarse a nuevos requisitos.
