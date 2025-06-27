package mx.regional.next.automotive.credit.infrastructure.adapters.persistence;

import mx.regional.next.automotive.credit.application.ports.out.CreditApplicationRepositoryPort;
import mx.regional.next.automotive.credit.domain.entities.CreditApplication;
import mx.regional.next.automotive.credit.domain.entities.Customer;
import mx.regional.next.automotive.credit.domain.entities.Vehicle;
import mx.regional.next.automotive.credit.domain.valueobjects.*;
import mx.regional.next.automotive.credit.domain.enums.DocumentType;
import mx.regional.next.automotive.credit.domain.enums.VehicleType;
import mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.entities.CreditApplicationJpaEntity;
import mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.repositories.CreditApplicationJpaRepository;
import mx.regional.next.shared.common.annotations.Adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Adapter
public class CreditApplicationPersistenceAdapter implements CreditApplicationRepositoryPort {
    
    private static final Logger log = LoggerFactory.getLogger(CreditApplicationPersistenceAdapter.class);
    
    private final CreditApplicationJpaRepository jpaRepository;
    
    public CreditApplicationPersistenceAdapter(CreditApplicationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public CreditApplication save(CreditApplication application) {
        try {
            log.debug("Guardando aplicación de crédito: {}", application.getId());
            
            CreditApplicationJpaEntity entity = mapToEntity(application);
            CreditApplicationJpaEntity savedEntity = jpaRepository.save(entity);
            
            log.debug("Aplicación de crédito guardada exitosamente: {}", savedEntity.getId());
            return mapToDomain(savedEntity);
            
        } catch (Exception e) {
            log.error("Error guardando aplicación de crédito: {}", application.getId(), e);
            throw new RuntimeException("Error persistiendo aplicación de crédito", e);
        }
    }
    
    @Override
    public Optional<CreditApplication> findById(String id) {
        try {
            log.debug("Buscando aplicación de crédito por ID: {}", id);
            
            Optional<CreditApplicationJpaEntity> entity = jpaRepository.findById(id);
            
            if (entity.isPresent()) {
                log.debug("Aplicación de crédito encontrada: {}", id);
                return Optional.of(mapToDomain(entity.get()));
            } else {
                log.debug("Aplicación de crédito no encontrada: {}", id);
                return Optional.empty();
            }
            
        } catch (Exception e) {
            log.error("Error buscando aplicación de crédito por ID: {}", id, e);
            throw new RuntimeException("Error consultando aplicación de crédito", e);
        }
    }
    
    @Override
    public Optional<CreditApplication> findByCustomerDocumentNumber(String documentNumber) {
        try {
            log.debug("Buscando aplicación de crédito por documento: {}", documentNumber);
            
            Optional<CreditApplicationJpaEntity> entity = jpaRepository.findByCustomerDocument(documentNumber);
            
            if (entity.isPresent()) {
                log.debug("Aplicación de crédito encontrada para documento: {}", documentNumber);
                return Optional.of(mapToDomain(entity.get()));
            } else {
                log.debug("Aplicación de crédito no encontrada para documento: {}", documentNumber);
                return Optional.empty();
            }
            
        } catch (Exception e) {
            log.error("Error buscando aplicación de crédito por documento: {}", documentNumber, e);
            throw new RuntimeException("Error consultando aplicación de crédito", e);
        }
    }
    
    private CreditApplicationJpaEntity mapToEntity(CreditApplication application) {
        CreditApplicationJpaEntity entity = new CreditApplicationJpaEntity();
        
        entity.setId(application.getId());
        entity.setCustomerDocument(application.getCustomer().getDocumentNumber().getValue());
        entity.setVehicleVin(application.getVehicle().getVin().getValue());
        entity.setRequestedAmount(application.getRequestedAmount().getValue());
        entity.setStatus(application.getStatus());
        entity.setApplicationDate(application.getApplicationDate());
        entity.setLastUpdateDate(application.getLastUpdateDate());
        
        // Vehicle information
        entity.setVehicleBrand(application.getVehicle().getBrand());
        entity.setVehicleModel(application.getVehicle().getModel());
        entity.setVehicleYear(application.getVehicle().getYear());
        entity.setVehicleValue(application.getVehicle().getValue().getValue());
        entity.setVehicleKilometers(application.getVehicle().getKilometers());
        
        // Credit score (if available)
        if (application.getCreditScore() != null) {
            entity.setCreditScore(application.getCreditScore().getValue());
        }
        
        // Rejection reason (if available)
        entity.setRejectionReason(application.getRejectionReason());
        
        return entity;
    }
    
    private CreditApplication mapToDomain(CreditApplicationJpaEntity entity) {
        // Create Customer (simplified - in real scenario you might fetch from Customer repository)
        Customer customer = createSimplifiedCustomer(entity.getCustomerDocument());
        
        // Create Vehicle
        Vehicle vehicle = new Vehicle(
            new VehicleVIN(entity.getVehicleVin()),
            entity.getVehicleBrand(),
            entity.getVehicleModel(),
            entity.getVehicleYear(),
            VehicleType.SEDAN, // Default - in real scenario this should be stored
            new CreditAmount(entity.getVehicleValue()),
            entity.getVehicleKilometers(),
            "N/A", // color
            "N/A", // engine
            "N/A"  // transmission
        );
        
        // Create CreditApplication
        CreditApplication application = new CreditApplication(
            customer,
            vehicle,
            new CreditAmount(entity.getRequestedAmount())
        );
        
        // Set state from entity
        if (entity.getCreditScore() != null) {
            CreditScore creditScore = new CreditScore(entity.getCreditScore());
            if (entity.getStatus().name().equals("APPROVED")) {
                application.approve(creditScore);
            }
        }
        
        if (entity.getStatus().name().equals("REJECTED") && entity.getRejectionReason() != null) {
            application.reject(entity.getRejectionReason());
        }
        
        return application;
    }
    
    private Customer createSimplifiedCustomer(String documentNumber) {
        // This is a simplified version - in a real scenario you would fetch from CustomerRepository
        return new Customer(
            new DocumentNumber(documentNumber),
            DocumentType.CEDULA,
            "Customer",
            "Name",
            "customer@email.com",
            "1234567890",
            java.time.LocalDate.of(1990, 1, 1),
            new CreditAmount(java.math.BigDecimal.valueOf(3000000)),
            new CreditAmount(java.math.BigDecimal.valueOf(500000)),
            "Empleado",
            24
        );
    }
}