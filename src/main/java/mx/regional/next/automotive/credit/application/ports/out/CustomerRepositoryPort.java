package mx.regional.next.automotive.credit.application.ports.out;

import mx.regional.next.automotive.credit.domain.entities.Customer;
import mx.regional.next.automotive.credit.domain.valueobjects.DocumentNumber;

import java.util.Optional;

public interface CustomerRepositoryPort {
    Optional<Customer> findByDocumentNumber(DocumentNumber documentNumber);
    Customer save(Customer customer);
    boolean existsByDocumentNumber(DocumentNumber documentNumber);
}