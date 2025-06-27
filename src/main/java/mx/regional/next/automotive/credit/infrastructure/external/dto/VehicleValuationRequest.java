package mx.regional.next.automotive.credit.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Jacksonized
public class VehicleValuationRequest {

    @NotBlank(message = "VIN is required")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid VIN format")
    private String vin;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year is required")
    @Min(value = 1990, message = "Year must be 1990 or later")
    @Max(value = 2030, message = "Year cannot be in the future")
    private Integer year;

    @NotNull(message = "Mileage is required")
    @Min(value = 0, message = "Mileage cannot be negative")
    private Integer mileage;

    private String version;
    private String engineType;
    private String fuelType;
    private String transmission;
    private String color;
    private String bodyType;

    // Ubicación del vehículo
    private String city;
    private String department;
    private String region;

    // Condición del vehículo
    @Pattern(regexp = "^(EXCELLENT|GOOD|FAIR|POOR)$", message = "Invalid condition")
    private String condition;

    private List<String> damages;
    private List<String> modifications;
    private List<String> accessories;

    // Documentación
    private Boolean hasCompleteDocumentation;
    private Boolean hasServiceHistory;
    private Boolean hasAccidentHistory;

    // Tipo de valuación solicitada
    @NotNull(message = "Valuation type is required")
    @Pattern(regexp = "^(COMMERCIAL|INSURANCE|LOAN|SALE)$", message = "Invalid valuation type")
    private String valuationType;

    // Metadatos de la solicitud
    private String requestId;
    @Builder.Default
    private LocalDateTime requestDate = LocalDateTime.now();
    private String requestingEntity;
    private String purpose;

    // Configuraciones específicas
    @Builder.Default
    private Boolean includeMarketAnalysis = true;

    @Builder.Default
    private Boolean includeDepreciation = true;

    @Builder.Default
    private Boolean includeLiquidityAnalysis = true;

    @Builder.Default
    private Boolean includeComparables = true;

    @Builder.Default
    private Integer comparablesRadius = 50; // km

    // Datos adicionales para valuación precisa
    private String sellerType; // DEALER, PRIVATE, AUCTION
    private String saleType; // CASH, FINANCED
    private Boolean urgentSale;
    private Integer daysOnMarket;
}