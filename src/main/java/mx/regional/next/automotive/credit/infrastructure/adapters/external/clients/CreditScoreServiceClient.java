package mx.regional.next.automotive.credit.infrastructure.adapters.external.clients;

import mx.regional.next.automotive.credit.infrastructure.adapters.external.dto.request.CreditScoreRequestDto;
import mx.regional.next.automotive.credit.infrastructure.adapters.external.dto.response.CreditScoreResponseDto;
import mx.regional.next.automotive.credit.infrastructure.adapters.external.fallbacks.CreditScoreServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "credit-score-service",
    url = "${services.credit-score.url}",
    fallback = CreditScoreServiceFallback.class
)
public interface CreditScoreServiceClient {
    
    @PostMapping("/api/v1/credit-score/calculate")
    CreditScoreResponseDto getCreditScore(
        @RequestBody CreditScoreRequestDto request,
        @RequestHeader("Authorization") String authorization
    );
    
    @GetMapping("/api/v1/credit-score/{documentNumber}")
    CreditScoreResponseDto getCreditScoreByDocument(
        @PathVariable("documentNumber") String documentNumber,
        @RequestHeader("Authorization") String authorization
    );
}