package mx.regional.next.automotive.credit.domain.exceptions;

public class CreditDomainException extends RuntimeException {
    
    public CreditDomainException(String message) {
        super(message);
    }
    
    public CreditDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}