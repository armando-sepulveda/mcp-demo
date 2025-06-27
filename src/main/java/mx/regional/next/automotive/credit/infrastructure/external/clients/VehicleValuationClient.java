package mx.regional.next.automotive.credit.infrastructure.external.clients;

import mx.regional.next.automotive.credit.infrastructure.external.dto.VehicleValuationRequest;
import mx.regional.next.automotive.credit.infrastructure.external.dto.VehicleValuationResponse;
import mx.regional.next.automotive.credit.infrastructure.external.dto.VehicleHistoryResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "vehicle-valuation-client",
    url = "${external.services.vehicle-valuation.url}",
    configuration = VehicleValuationClientConfig.class,
    fallback = VehicleValuationClientFallback.class
)
public interface VehicleValuationClient {

    @PostMapping("/api/v1/vehicle-valuation")
    VehicleValuationResponse getVehicleValuation(@RequestBody VehicleValuationRequest request);

    @GetMapping("/api/v1/vehicle-history/{vin}")
    VehicleHistoryResponse getVehicleHistory(@PathVariable("vin") String vin);

    @GetMapping("/api/v1/market-value/{brand}/{model}/{year}")
    VehicleValuationResponse getMarketValue(
            @PathVariable("brand") String brand,
            @PathVariable("model") String model,
            @PathVariable("year") Integer year,
            @RequestParam(value = "mileage", required = false) Integer mileage
    );

    @PostMapping("/api/v1/batch-valuation")
    VehicleValuationResponse[] getBatchValuations(@RequestBody VehicleValuationRequest[] requests);

    @GetMapping("/api/v1/health")
    String healthCheck();
}