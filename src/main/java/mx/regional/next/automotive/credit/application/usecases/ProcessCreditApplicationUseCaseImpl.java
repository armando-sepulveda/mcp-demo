package mx.regional.next.automotive.credit.application.usecases;

import mx.regional.next.automotive.credit.application.ports.in.ProcessCreditApplicationUseCase;
import mx.regional.next.automotive.credit.application.ports.out.*;
import mx.regional.next.automotive.credit.application.dto.*;
import mx.regional.next.automotive.credit.domain.entities.*;
import mx.regional.next.automotive.credit.domain.valueobjects.*;
import mx.regional.next.automotive.credit.domain.enums.VehicleType;
import mx.regional.next.automotive.credit.domain.services.*;
import mx.regional.next.shared.common.annotations.UseCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;

@UseCase
public class ProcessCreditApplicationUseCaseImpl implements ProcessCreditApplicationUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(ProcessCreditApplicationUseCaseImpl.class);
    
    private final CustomerRepositoryPort customerRepository;
    private final CreditApplicationRepositoryPort creditApplicationRepository;
    private final CreditScoreProviderPort creditScoreProvider;
    private final VehicleValidationPort vehicleValidation;
    
    private final CreditEligibilityService creditEligibilityService;
    private final InterestRateCalculationService interestRateCalculationService;
    
    public ProcessCreditApplicationUseCaseImpl(
            CustomerRepositoryPort customerRepository,
            CreditApplicationRepositoryPort creditApplicationRepository,
            CreditScoreProviderPort creditScoreProvider,
            VehicleValidationPort vehicleValidation,
            CreditEligibilityService creditEligibilityService,
            InterestRateCalculationService interestRateCalculationService) {
        
        this.customerRepository = customerRepository;
        this.creditApplicationRepository = creditApplicationRepository;
        this.creditScoreProvider = creditScoreProvider;
        this.vehicleValidation = vehicleValidation;
        this.creditEligibilityService = creditEligibilityService;
        this.interestRateCalculationService = interestRateCalculationService;
    }
    
    @Override
    public CreditApplicationResponse processApplication(@Valid CreditApplicationRequest request) {
        log.info("Procesando solicitud de crédito para cliente: {}", request.getCustomerDocument());
        
        try {
            // 1. Validar y obtener cliente
            Customer customer = validateAndGetCustomer(request);
            
            // 2. Validar vehículo
            Vehicle vehicle = validateVehicle(request);
            
            // 3. Crear aplicación de crédito
            CreditApplication application = new CreditApplication(
                customer, 
                vehicle, 
                new CreditAmount(request.getRequestedAmount())
            );
            
            // 4. Evaluar elegibilidad
            if (!creditEligibilityService.isEligible(application)) {
                application.reject("No cumple criterios de elegibilidad");
                creditApplicationRepository.save(application);
                
                log.info("Solicitud rechazada por elegibilidad para cliente: {}", 
                        customer.getDocumentNumber().getValue());
                
                return CreditApplicationResponse.rejected(
                    application.getId(),
                    "No cumple con los criterios de elegibilidad"
                );
            }
            
            // 5. Obtener score crediticio
            CreditScore creditScore = creditScoreProvider.getCreditScore(customer.getDocumentNumber());
            
            // 6. Calcular tasa de interés
            java.math.BigDecimal interestRate = interestRateCalculationService
                .calculateInterestRate(application, creditScore);
            
            // 7. Tomar decisión
            if (creditScore.getValue() >= 600) {
                application.approve(creditScore);
                
                CreditApplicationResponse response = CreditApplicationResponse.approved(
                    application.getId(),
                    creditScore.getValue(),
                    application.getRequestedAmount().getValue()
                );
                response.setInterestRate(interestRate);
                response.setCustomerDocument(customer.getDocumentNumber().getValue());
                response.setRequestedAmount(application.getRequestedAmount().getValue());
                
                creditApplicationRepository.save(application);
                
                log.info("Solicitud aprobada para cliente: {}", customer.getDocumentNumber().getValue());
                
                return response;
                
            } else {
                String rejectionReason = String.format(
                    "Score crediticio insuficiente: %d (mínimo requerido: 600)", 
                    creditScore.getValue()
                );
                
                application.reject(rejectionReason);
                creditApplicationRepository.save(application);
                
                log.info("Solicitud rechazada por score para cliente: {}", 
                        customer.getDocumentNumber().getValue());
                
                return CreditApplicationResponse.rejected(application.getId(), rejectionReason);
            }
            
        } catch (Exception e) {
            log.error("Error procesando solicitud de crédito para cliente: {}", 
                     request.getCustomerDocument(), e);
            throw new RuntimeException("Error procesando solicitud de crédito", e);
        }
    }
    
    private Customer validateAndGetCustomer(CreditApplicationRequest request) {
        DocumentNumber documentNumber = new DocumentNumber(request.getCustomerDocument());
        
        return customerRepository.findByDocumentNumber(documentNumber)
            .orElseThrow(() -> new RuntimeException(
                "Cliente no encontrado: " + request.getCustomerDocument()));
    }
    
    private Vehicle validateVehicle(CreditApplicationRequest request) {
        VehicleValidationPort.VehicleValidationResult validation = vehicleValidation.validateVehicle(
            request.getVehicleVin(),
            request.getVehicleBrand(),
            request.getVehicleModel(),
            request.getVehicleYear()
        );
        
        if (!validation.isValid()) {
            throw new RuntimeException("Vehículo no válido: " + validation.getErrorMessage());
        }
        
        return validation.getVehicle();
    }
}