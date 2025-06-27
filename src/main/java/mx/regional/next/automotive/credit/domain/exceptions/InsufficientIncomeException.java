package mx.regional.next.automotive.credit.domain.exceptions;

public class InsufficientIncomeException extends CreditDomainException {
    
    public InsufficientIncomeException(String message) {
        super(message);
    }
    
    public InsufficientIncomeException(String message, Throwable cause) {
        super(message, cause);
    }
}