package mx.regional.next.automotive.credit.infrastructure.external.clients;

import mx.regional.next.automotive.credit.infrastructure.external.dto.CreditBureauRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.CreditBureauResponse;
import mx.regional.next.automotive.credit.infrastructure.external.dto.CreditScoreResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "credit-bureau-client",
    url = "${external.services.credit-bureau.url}",
    configuration = CreditBureauClientConfig.class,
    fallback = CreditBureauClientFallback.class
)
public interface CreditBureauClient {

    @PostMapping("/api/v1/credit-report")
    CreditBureauResponse getCreditReport(@RequestBody CreditBureauRequest request);

    @GetMapping("/api/v1/credit-score/{documentNumber}")
    CreditScoreResponse getCreditScore(
            @PathVariable("documentNumber") String documentNumber,
            @RequestParam("documentType") String documentType
    );

    @PostMapping("/api/v1/batch-credit-check")
    CreditBureauResponse[] getBatchCreditReports(@RequestBody CreditBureauRequest[] requests);

    @GetMapping("/api/v1/health")
    String healthCheck();
}