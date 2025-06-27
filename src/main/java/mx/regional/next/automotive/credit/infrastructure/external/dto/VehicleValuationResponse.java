package mx.regional.next.automotive.credit.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Jacksonized
public class VehicleValuationResponse {

    private String requestId;
    private String vin;
    private LocalDateTime valuationDate;
    private String status;
    private String errorMessage;

    // Información del vehículo
    private String brand;
    private String model;
    private String version;
    private Integer year;
    private Integer mileage;
    private String condition;

    // Valores principales
    private BigDecimal commercialValue;
    private BigDecimal loanValue;
    private BigDecimal insuranceValue;
    private BigDecimal quickSaleValue;
    private BigDecimal retailValue;

    // Análisis de mercado
    private MarketAnalysis marketAnalysis;
    private DepreciationAnalysis depreciationAnalysis;
    private LiquidityAnalysis liquidityAnalysis;

    // Comparables utilizados
    private List<Comparable> comparables;
    private Integer comparablesFound;
    private BigDecimal averageMarketPrice;
    private BigDecimal priceRange;

    // Factores de ajuste aplicados
    private List<AdjustmentFactor> adjustmentFactors;
    private BigDecimal totalAdjustment;

    // Confiabilidad de la valuación
    private String confidenceLevel;
    private String accuracyRating;
    private String dataQuality;
    private LocalDate dataAsOf;

    // Recomendaciones
    private List<String> recommendations;
    private String marketPosition;
    private String saleTimeEstimate;

    // Alertas y observaciones
    private List<String> alerts;
    private List<String> warnings;
    private String generalObservations;

    // Metadatos
    private String responseCode;
    private String responseMessage;
    private LocalDateTime expirationDate;
    private String valuationCost;
    private String sourceProvider;

    @Data
    @Builder
    @Jacksonized
    public static class MarketAnalysis {
        private Integer vehiclesForSale;
        private BigDecimal averagePrice;
        private BigDecimal medianPrice;
        private BigDecimal priceStandardDeviation;
        private Integer averageDaysOnMarket;
        private String marketTrend;
        private String demandLevel;
        private String supplyLevel;
    }

    @Data
    @Builder
    @Jacksonized
    public static class DepreciationAnalysis {
        private BigDecimal annualDepreciationRate;
        private BigDecimal totalDepreciation;
        private BigDecimal projectedValue1Year;
        private BigDecimal projectedValue2Years;
        private BigDecimal projectedValue3Years;
        private String depreciationCategory;
        private List<String> depreciationFactors;
    }

    @Data
    @Builder
    @Jacksonized
    public static class LiquidityAnalysis {
        private String liquidityRating;
        private Integer estimatedSaleDays;
        private BigDecimal quickSaleDiscount;
        private String marketDemand;
        private String geographicDemand;
        private List<String> liquidityFactors;
    }

    @Data
    @Builder
    @Jacksonized
    public static class Comparable {
        private String source;
        private String vin;
        private String brand;
        private String model;
        private Integer year;
        private Integer mileage;
        private BigDecimal price;
        private String condition;
        private String location;
        private Integer daysOnMarket;
        private String listingType;
        private BigDecimal distanceKm;
    }

    @Data
    @Builder
    @Jacksonized
    public static class AdjustmentFactor {
        private String factorName;
        private String factorType;
        private BigDecimal adjustmentAmount;
        private BigDecimal adjustmentPercentage;
        private String description;
        private String reasoning;
    }
}