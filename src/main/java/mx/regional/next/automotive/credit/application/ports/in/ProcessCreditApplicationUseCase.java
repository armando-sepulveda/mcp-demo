package mx.regional.next.automotive.credit.application.ports.in;

import mx.regional.next.automotive.credit.application.dto.CreditApplicationRequest;
import mx.regional.next.automotive.credit.application.dto.CreditApplicationResponse;

public interface ProcessCreditApplicationUseCase {
    CreditApplicationResponse processApplication(CreditApplicationRequest request);
}