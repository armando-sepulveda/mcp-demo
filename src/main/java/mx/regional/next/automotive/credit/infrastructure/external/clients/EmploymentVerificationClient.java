package mx.regional.next.automotive.credit.infrastructure.external.clients;

import mx.regional.next.automotive.credit.infrastructure.external.dto.EmploymentVerificationRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.EmploymentVerificationResponse;
import mx.regional.next.automotive.credit.infrastructure.external.dto.IncomeVerificationResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "employment-verification-client",
    url = "${external.services.employment-verification.url}",
    configuration = EmploymentVerificationClientConfig.class,
    fallback = EmploymentVerificationClientFallback.class
)
public interface EmploymentVerificationClient {

    @PostMapping("/api/v1/employment-verification")
    EmploymentVerificationResponse verifyEmployment(@RequestBody EmploymentVerificationRequest request);

    @PostMapping("/api/v1/income-verification")
    IncomeVerificationResponse verifyIncome(@RequestBody EmploymentVerificationRequest request);

    @GetMapping("/api/v1/company-info/{companyId}")
    EmploymentVerificationResponse getCompanyInfo(@PathVariable("companyId") String companyId);

    @PostMapping("/api/v1/batch-employment-verification")
    EmploymentVerificationResponse[] getBatchEmploymentVerifications(@RequestBody EmploymentVerificationRequest[] requests);

    @GetMapping("/api/v1/health")
    String healthCheck();
}