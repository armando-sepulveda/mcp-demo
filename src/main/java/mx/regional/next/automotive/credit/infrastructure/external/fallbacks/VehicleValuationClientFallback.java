package mx.regional.next.automotive.credit.infrastructure.external.fallbacks;

import mx.regional.next.automotive.credit.infrastructure.external.clients.VehicleValuationClient;
import mx.regional.next.automotive.credit.infrastructure.external.dto.VehicleValuationRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.VehicleValuationResponse;
import mx.regional.next.automotive.credit.infrastructure.external.dto.VehicleHistoryResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class VehicleValuationClientFallback implements VehicleValuationClient {

    private static final Logger log = LoggerFactory.getLogger(VehicleValuationClientFallback.class);
    
    // Valores de fallback basados en promedios del mercado colombiano
    private static final Map<String, BigDecimal> BRAND_MULTIPLIERS = Map.of(
        "TOYOTA", new BigDecimal("1.15"),
        "CHEVROLET", new BigDecimal("1.00"),
        "RENAULT", new BigDecimal("0.90"),
        "NISSAN", new BigDecimal("1.05"),
        "HYUNDAI", new BigDecimal("1.00"),
        "KIA", new BigDecimal("0.95"),
        "MAZDA", new BigDecimal("1.10"),
        "FORD", new BigDecimal("1.05")
    );

    @Override
    public VehicleValuationResponse getVehicleValuation(VehicleValuationRequest request) {
        log.warn("Vehicle Valuation service unavailable. Returning fallback valuation for VIN: {}", 
                request.getVin());
        
        BigDecimal estimatedValue = calculateFallbackValue(request);
        
        return VehicleValuationResponse.builder()
                .requestId(request.getRequestId())
                .vin(request.getVin())
                .valuationDate(LocalDateTime.now())
                .status("SERVICE_UNAVAILABLE")
                .errorMessage("Vehicle valuation service is temporarily unavailable. Using estimated values.")
                .brand(request.getBrand())
                .model(request.getModel())
                .version(request.getVersion())
                .year(request.getYear())
                .mileage(request.getMileage())
                .condition(request.getCondition())
                .commercialValue(estimatedValue)
                .loanValue(estimatedValue.multiply(new BigDecimal("0.80")))
                .insuranceValue(estimatedValue.multiply(new BigDecimal("1.10")))
                .quickSaleValue(estimatedValue.multiply(new BigDecimal("0.85")))
                .retailValue(estimatedValue.multiply(new BigDecimal("1.05")))
                .marketAnalysis(createFallbackMarketAnalysis())
                .depreciationAnalysis(createFallbackDepreciationAnalysis(request.getYear()))
                .liquidityAnalysis(createFallbackLiquidityAnalysis())
                .comparables(List.of())
                .comparablesFound(0)
                .averageMarketPrice(estimatedValue)
                .priceRange(estimatedValue.multiply(new BigDecimal("0.15")))
                .adjustmentFactors(List.of())
                .totalAdjustment(BigDecimal.ZERO)
                .confidenceLevel("LOW")
                .accuracyRating("ESTIMATED")
                .dataQuality("FALLBACK")
                .dataAsOf(LocalDateTime.now().toLocalDate())
                .recommendations(List.of(
                    "Estimated valuation based on general market data",
                    "Professional appraisal recommended for accurate valuation",
                    "Values may vary significantly based on actual condition"
                ))
                .marketPosition("ESTIMATED")
                .saleTimeEstimate("30-60 days (estimated)")
                .alerts(List.of("Valuation service unavailable"))
                .warnings(List.of("Using estimated values only"))
                .generalObservations("This is a fallback estimation. Professional valuation recommended.")
                .responseCode("503")
                .responseMessage("Service Unavailable - Using Estimates")
                .sourceProv
                .build();
    }

    @Override
    public VehicleHistoryResponse getVehicleHistory(String vin) {
        log.warn("Vehicle History service unavailable. Returning fallback history for VIN: {}", vin);
        
        return VehicleHistoryResponse.builder()
                .requestId("FALLBACK_" + System.currentTimeMillis())
                .vin(vin)
                .consultationDate(LocalDateTime.now())
                .status("SERVICE_UNAVAILABLE")
                .errorMessage("Vehicle history service is temporarily unavailable.")
                .numberOfOwners(null)
                .ownerHistory(List.of())
                .currentOwnerType("UNKNOWN")
                .lastOwnershipChange(null)
                .hasAccidentHistory(null)
                .numberOfAccidents(null)
                .accidentHistory(List.of())
                .accidentSeverity("UNKNOWN")
                .hasServiceHistory(null)
                .serviceHistory(List.of())
                .lastServiceDate(null)
                .totalServiceRecords(null)
                .hasLegalIssues(null)
                .legalRecords(List.of())
                .isStolen(false) // Default to false for safety
                .hasLiens(null)
                .vehicleUse("UNKNOWN")
                .operationType("UNKNOWN")
                .wasCommercialUse(null)
                .wasRentalCar(null)
                .inspectionHistory(List.of())
                .lastInspectionDate(null)
                .lastInspectionResult("UNKNOWN")
                .recalls(List.of())
                .hasOpenRecalls(null)
                .completedRecalls(null)
                .estimatedMileage(null)
                .mileageConsistency("UNKNOWN")
                .mileageHistory(List.of())
                .overallRating("UNAVAILABLE")
                .reliabilityScore("UNKNOWN")
                .riskFactors(List.of("History unavailable"))
                .positiveFactors(List.of())
                .responseCode("503")
                .responseMessage("Service Unavailable")
                .sourceDatabases("FALLBACK")
                .reportCost("0.00")
                .build();
    }

    @Override
    public VehicleValuationResponse getMarketValue(String brand, String model, Integer year, Integer mileage) {
        log.warn("Vehicle Valuation service unavailable. Returning fallback market value for: {} {} {}", 
                brand, model, year);
        
        VehicleValuationRequest fallbackRequest = VehicleValuationRequest.builder()
                .vin("UNKNOWN")
                .brand(brand)
                .model(model)
                .year(year)
                .mileage(mileage != null ? mileage : 50000)
                .condition("GOOD")
                .valuationType("COMMERCIAL")
                .build();
        
        return getVehicleValuation(fallbackRequest);
    }

    @Override
    public VehicleValuationResponse[] getBatchValuations(VehicleValuationRequest[] requests) {
        log.warn("Vehicle Valuation service unavailable. Returning fallback batch valuations for {} vehicles", 
                requests.length);
        
        VehicleValuationResponse[] fallbackResponses = new VehicleValuationResponse[requests.length];
        
        for (int i = 0; i < requests.length; i++) {
            fallbackResponses[i] = getVehicleValuation(requests[i]);
        }
        
        return fallbackResponses;
    }

    @Override
    public String healthCheck() {
        log.debug("Vehicle Valuation health check - fallback response");
        return "SERVICE_UNAVAILABLE - Vehicle Valuation service is currently not responding";
    }

    private BigDecimal calculateFallbackValue(VehicleValuationRequest request) {
        // Base value estimation using year and mileage
        int currentYear = LocalDateTime.now().getYear();
        int vehicleAge = currentYear - request.getYear();
        
        // Start with a base value (average for Colombian market)
        BigDecimal baseValue = new BigDecimal("80000000"); // 80M COP base
        
        // Apply brand multiplier
        BigDecimal brandMultiplier = BRAND_MULTIPLIERS.getOrDefault(
                request.getBrand().toUpperCase(), new BigDecimal("1.00"));
        baseValue = baseValue.multiply(brandMultiplier);
        
        // Apply age depreciation (10% per year)
        double depreciationRate = Math.pow(0.90, vehicleAge);
        baseValue = baseValue.multiply(new BigDecimal(depreciationRate));
        
        // Apply mileage depreciation
        if (request.getMileage() != null) {
            double mileageFactor = Math.max(0.5, 1.0 - (request.getMileage() / 200000.0 * 0.3));
            baseValue = baseValue.multiply(new BigDecimal(mileageFactor));
        }
        
        // Apply condition factor
        if (request.getCondition() != null) {
            BigDecimal conditionFactor = switch (request.getCondition().toUpperCase()) {
                case "EXCELLENT" -> new BigDecimal("1.15");
                case "GOOD" -> new BigDecimal("1.00");
                case "FAIR" -> new BigDecimal("0.85");
                case "POOR" -> new BigDecimal("0.65");
                default -> new BigDecimal("1.00");
            };
            baseValue = baseValue.multiply(conditionFactor);
        }
        
        return baseValue.setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    private VehicleValuationResponse.MarketAnalysis createFallbackMarketAnalysis() {
        return VehicleValuationResponse.MarketAnalysis.builder()
                .vehiclesForSale(null)
                .averagePrice(new BigDecimal("75000000"))
                .medianPrice(new BigDecimal("70000000"))
                .priceStandardDeviation(new BigDecimal("15000000"))
                .averageDaysOnMarket(45)
                .marketTrend("STABLE")
                .demandLevel("MODERATE")
                .supplyLevel("MODERATE")
                .build();
    }

    private VehicleValuationResponse.DepreciationAnalysis createFallbackDepreciationAnalysis(Integer year) {
        int currentYear = LocalDateTime.now().getYear();
        int vehicleAge = currentYear - year;
        
        return VehicleValuationResponse.DepreciationAnalysis.builder()
                .annualDepreciationRate(new BigDecimal("0.10"))
                .totalDepreciation(new BigDecimal(vehicleAge * 0.10))
                .projectedValue1Year(null)
                .projectedValue2Years(null)
                .projectedValue3Years(null)
                .depreciationCategory("STANDARD")
                .depreciationFactors(List.of("Age", "Mileage", "Market conditions"))
                .build();
    }

    private VehicleValuationResponse.LiquidityAnalysis createFallbackLiquidityAnalysis() {
        return VehicleValuationResponse.LiquidityAnalysis.builder()
                .liquidityRating("MODERATE")
                .estimatedSaleDays(45)
                .quickSaleDiscount(new BigDecimal("0.15"))
                .marketDemand("MODERATE")
                .geographicDemand("MODERATE")
                .liquidityFactors(List.of("Brand popularity", "Market size", "Economic conditions"))
                .build();
    }
}