package mx.regional.next.automotive.credit.domain.exceptions;

public class InvalidCreditAmountException extends CreditDomainException {
    
    public InvalidCreditAmountException(String message) {
        super(message);
    }
    
    public InvalidCreditAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}