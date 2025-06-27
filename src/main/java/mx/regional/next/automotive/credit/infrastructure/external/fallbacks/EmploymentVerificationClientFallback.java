package mx.regional.next.automotive.credit.infrastructure.external.fallbacks;

import mx.regional.next.automotive.credit.infrastructure.external.clients.EmploymentVerificationClient;
import mx.regional.next.automotive.credit.infrastructure.external.dto.EmploymentVerificationRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.EmploymentVerificationResponse;
import mx.regional.next.automotive.credit.infrastructure.external.dto.IncomeVerificationResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EmploymentVerificationClientFallback implements EmploymentVerificationClient {

    private static final Logger log = LoggerFactory.getLogger(EmploymentVerificationClientFallback.class);

    @Override
    public EmploymentVerificationResponse verifyEmployment(EmploymentVerificationRequest request) {
        log.warn("Employment Verification service unavailable. Returning fallback response for document: {}", 
                request.getEmployeeDocumentNumber());
        
        return EmploymentVerificationResponse.builder()
                .requestId(request.getRequestId())
                .employeeDocumentNumber(request.getEmployeeDocumentNumber())
                .verificationDate(LocalDateTime.now())
                .status("SERVICE_UNAVAILABLE")
                .errorMessage("Employment verification service is temporarily unavailable. Manual verification required.")
                .employmentVerified(null)
                .employmentStatus("UNKNOWN")
                .verificationMethod("UNAVAILABLE")
                .verificationSource("FALLBACK")
                .companyName(request.getCompanyName())
                .companyNit(request.getCompanyNit())
                .jobTitle(request.getJobTitle())
                .department(request.getDepartment())
                .hireDate(request.getHireDate())
                .contractType(request.getContractType())
                .workSchedule("UNKNOWN")
                .employmentDuration("UNKNOWN")
                .incomeVerified(null)
                .verifiedBaseSalary(null)
                .verifiedVariableIncome(null)
                .verifiedTotalIncome(null)
                .averageMonthlyIncome(null)
                .incomeStability("UNKNOWN")
                .incomeHistory(List.of())
                .verificationPeriodMonths(null)
                .incomeGrowthRate(null)
                .companyInfo(createFallbackCompanyInfo(request))
                .contactsVerified(List.of())
                .stabilityRating("UNKNOWN")
                .jobSecurityAssessment("UNKNOWN")
                .riskFactors(List.of("Verification service unavailable"))
                .positiveFactors(List.of())
                .documentsVerified(List.of())
                .observations(List.of("Employment verification could not be completed automatically"))
                .warnings(List.of("Manual verification required"))
                .recommendations(List.of(
                    "Contact employer directly for verification",
                    "Request additional employment documentation",
                    "Consider alternative verification methods"
                ))
                .confidenceLevel("NONE")
                .dataQuality("UNAVAILABLE")
                .reliabilityScore("0")
                .responseCode("503")
                .responseMessage("Service Unavailable")
                .verificationCost("0.00")
                .provider("FALLBACK")
                .build();
    }

    @Override
    public IncomeVerificationResponse verifyIncome(EmploymentVerificationRequest request) {
        log.warn("Income Verification service unavailable. Returning fallback response for document: {}", 
                request.getEmployeeDocumentNumber());
        
        return IncomeVerificationResponse.builder()
                .requestId(request.getRequestId())
                .employeeDocumentNumber(request.getEmployeeDocumentNumber())
                .verificationDate(LocalDateTime.now())
                .status("SERVICE_UNAVAILABLE")
                .errorMessage("Income verification service is temporarily unavailable. Manual verification required.")
                .incomeVerified(null)
                .declaredIncome(request.getTotalMonthlyIncome())
                .verifiedIncome(null)
                .incomeVariance(null)
                .incomeVariancePercentage(null)
                .incomeBreakdown(null)
                .incomeStability("UNKNOWN")
                .incomeVolatility(null)
                .incomePattern("UNKNOWN")
                .seasonalityImpact("UNKNOWN")
                .monthlyIncomeHistory(List.of())
                .verificationPeriodMonths(null)
                .incomeGrowthRate(null)
                .incomeGrowthTrend("UNKNOWN")
                .industryAverageIncome(null)
                .incomePercentile("UNKNOWN")
                .incomeCompetitiveness("UNKNOWN")
                .verificationSources(List.of())
                .primaryVerificationMethod("UNAVAILABLE")
                .secondaryVerificationMethod("UNAVAILABLE")
                .incomeRiskRating("UNKNOWN")
                .incomeRiskFactors(List.of("Verification service unavailable"))
                .incomeStabilityFactors(List.of())
                .projectedIncome12Months(null)
                .projectedIncome24Months(null)
                .projectionConfidence("NONE")
                .observations(List.of("Income verification could not be completed automatically"))
                .warnings(List.of("Manual income verification required"))
                .recommendations(List.of(
                    "Request recent pay stubs for income verification",
                    "Obtain employment certificate with salary details",
                    "Consider bank statement analysis for income validation"
                ))
                .confidenceLevel("NONE")
                .verificationQuality("UNAVAILABLE")
                .reliabilityScore("0")
                .responseCode("503")
                .responseMessage("Service Unavailable")
                .verificationCost("0.00")
                .build();
    }

    @Override
    public EmploymentVerificationResponse getCompanyInfo(String companyId) {
        log.warn("Company Information service unavailable. Returning fallback response for company: {}", companyId);
        
        return EmploymentVerificationResponse.builder()
                .requestId("FALLBACK_" + System.currentTimeMillis())
                .verificationDate(LocalDateTime.now())
                .status("SERVICE_UNAVAILABLE")
                .errorMessage("Company information service is temporarily unavailable.")
                .employmentVerified(null)
                .employmentStatus("UNKNOWN")
                .verificationMethod("UNAVAILABLE")
                .verificationSource("FALLBACK")
                .companyName("UNKNOWN")
                .companyNit(companyId)
                .companyInfo(EmploymentVerificationResponse.CompanyInformation.builder()
                        .companyName("UNKNOWN")
                        .companyNit(companyId)
                        .sector("UNKNOWN")
                        .industry("UNKNOWN")
                        .companySize("UNKNOWN")
                        .numberOfEmployees(null)
                        .creditRating("UNKNOWN")
                        .financialStability("UNKNOWN")
                        .businessRegistration("UNKNOWN")
                        .operatingLicense(null)
                        .foundedDate(null)
                        .legalStructure("UNKNOWN")
                        .build())
                .observations(List.of("Company information could not be retrieved"))
                .warnings(List.of("Manual company verification required"))
                .recommendations(List.of(
                    "Verify company registration manually",
                    "Request company documentation directly",
                    "Use alternative company verification sources"
                ))
                .confidenceLevel("NONE")
                .dataQuality("UNAVAILABLE")
                .reliabilityScore("0")
                .responseCode("503")
                .responseMessage("Service Unavailable")
                .verificationCost("0.00")
                .provider("FALLBACK")
                .build();
    }

    @Override
    public EmploymentVerificationResponse[] getBatchEmploymentVerifications(EmploymentVerificationRequest[] requests) {
        log.warn("Employment Verification service unavailable. Returning fallback batch response for {} requests", 
                requests.length);
        
        EmploymentVerificationResponse[] fallbackResponses = new EmploymentVerificationResponse[requests.length];
        
        for (int i = 0; i < requests.length; i++) {
            fallbackResponses[i] = verifyEmployment(requests[i]);
        }
        
        return fallbackResponses;
    }

    @Override
    public String healthCheck() {
        log.debug("Employment Verification health check - fallback response");
        return "SERVICE_UNAVAILABLE - Employment Verification service is currently not responding";
    }

    private EmploymentVerificationResponse.CompanyInformation createFallbackCompanyInfo(EmploymentVerificationRequest request) {
        return EmploymentVerificationResponse.CompanyInformation.builder()
                .companyName(request.getCompanyName())
                .companyNit(request.getCompanyNit())
                .sector(request.getCompanySector())
                .industry("UNKNOWN")
                .companySize("UNKNOWN")
                .numberOfEmployees(null)
                .creditRating("UNKNOWN")
                .financialStability("UNKNOWN")
                .businessRegistration("UNKNOWN")
                .operatingLicense(null)
                .foundedDate(null)
                .legalStructure("UNKNOWN")
                .build();
    }
}