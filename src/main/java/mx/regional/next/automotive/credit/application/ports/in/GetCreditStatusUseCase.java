package mx.regional.next.automotive.credit.application.ports.in;

import mx.regional.next.automotive.credit.application.dto.CreditApplicationResponse;

public interface GetCreditStatusUseCase {
    CreditApplicationResponse getCreditStatus(String applicationId);
}