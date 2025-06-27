package mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.repositories;

import mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.entities.CustomerJpaEntity;
import mx.regional.next.automotive.credit.domain.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, String> {
    
    Optional<CustomerJpaEntity> findByDocumentNumber(String documentNumber);
    
    List<CustomerJpaEntity> findByDocumentType(DocumentType documentType);
    
    @Query("SELECT c FROM CustomerJpaEntity c WHERE c.email = :email")
    Optional<CustomerJpaEntity> findByEmail(@Param("email") String email);
    
    @Query("SELECT c FROM CustomerJpaEntity c WHERE UPPER(c.firstName) LIKE UPPER(:firstName) AND UPPER(c.lastName) LIKE UPPER(:lastName)")
    List<CustomerJpaEntity> findByFirstNameAndLastNameIgnoreCase(
        @Param("firstName") String firstName, 
        @Param("lastName") String lastName
    );
    
    boolean existsByDocumentNumber(String documentNumber);
    
    boolean existsByEmail(String email);
}