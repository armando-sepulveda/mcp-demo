package mx.regional.next.automotive.credit.application.ports.out;

import mx.regional.next.automotive.credit.domain.valueobjects.CreditScore;
import mx.regional.next.automotive.credit.domain.valueobjects.DocumentNumber;

public interface CreditScoreProviderPort {
    CreditScore getCreditScore(DocumentNumber documentNumber);
}