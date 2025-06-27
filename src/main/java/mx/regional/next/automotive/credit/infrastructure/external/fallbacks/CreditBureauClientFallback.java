package mx.regional.next.automotive.credit.infrastructure.external.fallbacks;

import mx.regional.next.automotive.credit.infrastructure.external.clients.CreditBureauClient;
import mx.regional.next.automotive.credit.infrastructure.external.dto.CreditBureauRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.CreditBureauResponse;
import mx.regional.next.automotive.credit.infrastructure.external.dto.CreditScoreResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CreditBureauClientFallback implements CreditBureauClient {

    private static final Logger log = LoggerFactory.getLogger(CreditBureauClientFallback.class);

    @Override
    public CreditBureauResponse getCreditReport(CreditBureauRequest request) {
        log.warn("Credit Bureau service unavailable. Returning fallback response for document: {}", 
                request.getDocumentNumber());
        
        return CreditBureauResponse.builder()
                .requestId(request.getRequestId())
                .documentNumber(request.getDocumentNumber())
                .documentType(request.getDocumentType())
                .fullName(request.getFirstName() + " " + request.getLastName())
                .consultationDate(LocalDateTime.now())
                .status("SERVICE_UNAVAILABLE")
                .errorMessage("Credit Bureau service is temporarily unavailable. Please try again later.")
                .creditScore(null)
                .scoreCategory("UNAVAILABLE")
                .riskLevel("UNKNOWN")
                .totalDebt(null)
                .availableCredit(null)
                .usedCredit(null)
                .monthlyPayments(null)
                .activeAccounts(null)
                .closedAccounts(null)
                .daysInDefault(null)
                .timesInDefault(null)
                .paymentBehavior("UNKNOWN")
                .consultationsLast6Months(null)
                .consultationsLast12Months(null)
                .creditProducts(List.of())
                .recentInquiries(List.of())
                .publicRecords(List.of())
                .commercialReferences(List.of())
                .alerts(List.of("Service temporarily unavailable"))
                .warnings(List.of("Credit verification could not be completed"))
                .generalObservations("External credit bureau service is currently unavailable. " +
                        "Manual verification may be required for credit approval.")
                .responseCode("503")
                .responseMessage("Service Unavailable")
                .sourceBureau("FALLBACK")
                .build();
    }

    @Override
    public CreditScoreResponse getCreditScore(String documentNumber, String documentType) {
        log.warn("Credit Bureau service unavailable. Returning fallback score response for document: {}", 
                documentNumber);
        
        return CreditScoreResponse.builder()
                .requestId("FALLBACK_" + System.currentTimeMillis())
                .documentNumber(documentNumber)
                .documentType(documentType)
                .consultationDate(LocalDateTime.now())
                .status("SERVICE_UNAVAILABLE")
                .errorMessage("Credit scoring service is temporarily unavailable. Please try again later.")
                .creditScore(null)
                .scoreCategory("UNAVAILABLE")
                .riskLevel("UNKNOWN")
                .scoreModel("UNAVAILABLE")
                .scoreCalculationDate(null)
                .paymentHistory(null)
                .creditUtilization(null)
                .creditHistory(null)
                .creditMix(null)
                .newCredit(null)
                .positiveFactors(List.of())
                .negativeFactors(List.of("Service unavailable"))
                .improvementRecommendations(List.of("Retry credit score consultation when service is available"))
                .scoreHistory(List.of())
                .percentileRanking("UNKNOWN")
                .averageScoreForAge(null)
                .comparisonWithPeers("UNAVAILABLE")
                .defaultProbability12Months(null)
                .defaultProbability24Months(null)
                .riskProfile("UNKNOWN")
                .riskAlerts(List.of("Credit score unavailable"))
                .fraudAlerts(List.of())
                .identityVerified(null)
                .responseCode("503")
                .responseMessage("Service Unavailable")
                .consultationCost("0.00")
                .build();
    }

    @Override
    public CreditBureauResponse[] getBatchCreditReports(CreditBureauRequest[] requests) {
        log.warn("Credit Bureau service unavailable. Returning fallback batch response for {} requests", 
                requests.length);
        
        CreditBureauResponse[] fallbackResponses = new CreditBureauResponse[requests.length];
        
        for (int i = 0; i < requests.length; i++) {
            fallbackResponses[i] = getCreditReport(requests[i]);
        }
        
        return fallbackResponses;
    }

    @Override
    public String healthCheck() {
        log.debug("Credit Bureau health check - fallback response");
        return "SERVICE_UNAVAILABLE - Credit Bureau service is currently not responding";
    }
}