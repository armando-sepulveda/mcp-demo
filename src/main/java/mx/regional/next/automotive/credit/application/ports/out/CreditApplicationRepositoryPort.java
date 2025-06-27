package mx.regional.next.automotive.credit.application.ports.out;

import mx.regional.next.automotive.credit.domain.entities.CreditApplication;

import java.util.Optional;

public interface CreditApplicationRepositoryPort {
    CreditApplication save(CreditApplication application);
    Optional<CreditApplication> findById(String id);
    Optional<CreditApplication> findByCustomerDocumentNumber(String documentNumber);
}