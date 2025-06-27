package mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.repositories;

import mx.regional.next.automotive.credit.infrastructure.adapters.persistence.jpa.entities.CreditApplicationJpaEntity;
import mx.regional.next.automotive.credit.domain.enums.CreditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditApplicationJpaRepository extends JpaRepository<CreditApplicationJpaEntity, String> {
    
    Optional<CreditApplicationJpaEntity> findByCustomerDocument(String customerDocument);
    
    List<CreditApplicationJpaEntity> findByStatus(CreditStatus status);
    
    List<CreditApplicationJpaEntity> findByCustomerDocumentAndStatus(String customerDocument, CreditStatus status);
    
    @Query("SELECT ca FROM CreditApplicationJpaEntity ca WHERE ca.applicationDate >= :fromDate AND ca.applicationDate <= :toDate")
    List<CreditApplicationJpaEntity> findByApplicationDateBetween(
        @Param("fromDate") LocalDateTime fromDate, 
        @Param("toDate") LocalDateTime toDate
    );
    
    @Query("SELECT COUNT(ca) FROM CreditApplicationJpaEntity ca WHERE ca.status = :status")
    long countByStatus(@Param("status") CreditStatus status);
    
    boolean existsByCustomerDocumentAndStatus(String customerDocument, CreditStatus status);
}