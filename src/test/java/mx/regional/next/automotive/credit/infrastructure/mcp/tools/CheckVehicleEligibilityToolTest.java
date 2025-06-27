package mx.regional.next.automotive.credit.infrastructure.mcp.tools;

import mx.regional.next.automotive.credit.domain.entities.Vehicle;
import mx.regional.next.automotive.credit.domain.valueobjects.VehicleVIN;
import mx.regional.next.automotive.credit.domain.services.CreditEligibilityService;
import mx.regional.next.automotive.credit.application.ports.out.VehicleValidationPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CheckVehicleEligibilityTool Tests")
class CheckVehicleEligibilityToolTest {

    @Mock
    private CreditEligibilityService creditEligibilityService;

    @Mock
    private VehicleValidationPort vehicleValidationPort;

    @InjectMocks
    private CheckVehicleEligibilityTool checkVehicleEligibilityTool;

    @Nested
    @DisplayName("Vehicle Eligibility Check Tests")
    class VehicleEligibilityCheckTests {

        @Test
        @DisplayName("Should approve eligible new vehicle")
        void shouldApproveEligibleNewVehicle() {
            // Given
            String validVin = "1HGBH41JXMN109186";
            String brand = "TOYOTA";
            String model = "COROLLA";
            String year = String.valueOf(Year.now().getValue());
            String value = "80000000";
            String kilometers = "5000";

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(true);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, year, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("VEHÍCULO ELEGIBLE");
            assertThat(result).contains("TOYOTA COROLLA");
            assertThat(result).contains("80,000,000");
            assertThat(result).contains("5,000 km");
            assertThat(result).contains("VIN válido");
            assertThat(result).contains("Marca autorizada");
            assertThat(result).contains("puede proceder a evaluación");

            verify(creditEligibilityService).isVehicleEligible(any(Vehicle.class));
        }

        @Test
        @DisplayName("Should reject vehicle with invalid VIN")
        void shouldRejectVehicleWithInvalidVin() {
            // Given
            String invalidVin = "INVALID_VIN"; // Less than 17 characters
            String brand = "TOYOTA";
            String model = "COROLLA";
            String year = "2023";
            String value = "80000000";
            String kilometers = "5000";

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                invalidVin, brand, model, year, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Error");
            assertThat(result).contains("VIN inválido");
            assertThat(result).contains("exactamente 17 caracteres");

            verify(creditEligibilityService, never()).isVehicleEligible(any());
        }

        @Test
        @DisplayName("Should reject vehicle with unauthorized brand")
        void shouldRejectVehicleWithUnauthorizedBrand() {
            // Given
            String validVin = "1HGBH41JXMN109186";
            String unauthorizedBrand = "FERRARI"; // Not in authorized list
            String model = "F50";
            String year = "2023";
            String value = "800000000";
            String kilometers = "1000";

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(false);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, unauthorizedBrand, model, year, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("VEHÍCULO NO ELEGIBLE");
            assertThat(result).contains("Marca no autorizada");
            assertThat(result).contains("FERRARI");
        }

        @Test
        @DisplayName("Should reject vehicle that is too old")
        void shouldRejectVehicleThatIsTooOld() {
            // Given
            String validVin = "1HGBH41JXMN109186";
            String brand = "TOYOTA";
            String model = "COROLLA";
            String oldYear = String.valueOf(Year.now().getValue() - 8); // 8 years old (max 6)
            String value = "40000000";
            String kilometers = "120000";

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(false);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, oldYear, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("VEHÍCULO NO ELEGIBLE");
            assertThat(result).contains("muy antiguo");
            assertThat(result).contains("8 años");
        }

        @Test
        @DisplayName("Should reject vehicle with excessive mileage")
        void shouldRejectVehicleWithExcessiveMileage() {
            // Given
            String validVin = "1HGBH41JXMN109186";
            String brand = "TOYOTA";
            String model = "COROLLA";
            String year = "2020";
            String value = "60000000";
            String excessiveKilometers = "150000"; // Exceeds 100,000 km limit

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(false);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, year, value, excessiveKilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("VEHÍCULO NO ELEGIBLE");
            assertThat(result).contains("excede límite");
            assertThat(result).contains("150,000 km");
        }

        @Test
        @DisplayName("Should reject vehicle with value outside range")
        void shouldRejectVehicleWithValueOutsideRange() {
            // Given
            String validVin = "1HGBH41JXMN109186";
            String brand = "TOYOTA";
            String model = "YARIS";
            String year = "2022";
            String lowValue = "30000000"; // Below minimum of 50M
            String kilometers = "20000";

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(false);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, year, lowValue, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("VEHÍCULO NO ELEGIBLE");
            assertThat(result).contains("fuera del rango");
            assertThat(result).contains("50,000,000");
        }

        @Test
        @DisplayName("Should handle invalid number format gracefully")
        void shouldHandleInvalidNumberFormatGracefully() {
            // Given
            String validVin = "1HGBH41JXMN109186";
            String brand = "TOYOTA";
            String model = "COROLLA";
            String invalidYear = "not_a_year";
            String value = "80000000";
            String kilometers = "5000";

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, invalidYear, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Error");
            assertThat(result).contains("Formato inválido");

            verify(creditEligibilityService, never()).isVehicleEligible(any());
        }

        @Test
        @DisplayName("Should handle service exception gracefully")
        void shouldHandleServiceExceptionGracefully() {
            // Given
            String validVin = "1HGBH41JXMN109186";
            String brand = "TOYOTA";
            String model = "COROLLA";
            String year = "2023";
            String value = "80000000";
            String kilometers = "5000";

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class)))
                .thenThrow(new RuntimeException("Service error"));

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, year, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("ERROR EN VERIFICACIÓN");
            assertThat(result).contains("Service error");
        }
    }

    @Nested
    @DisplayName("Authorized Brands Tests")
    class AuthorizedBrandsTests {

        @Test
        @DisplayName("Should return list of authorized brands")
        void shouldReturnListOfAuthorizedBrands() {
            // When
            String result = checkVehicleEligibilityTool.getAuthorizedBrands();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("MARCAS DE VEHÍCULOS AUTORIZADAS");
            assertThat(result).contains("TOYOTA");
            assertThat(result).contains("CHEVROLET");
            assertThat(result).contains("RENAULT");
            assertThat(result).contains("NISSAN");
            assertThat(result).contains("HYUNDAI");
            assertThat(result).contains("KIA");
            assertThat(result).contains("MAZDA");
            assertThat(result).contains("FORD");
            assertThat(result).contains("Año mínimo: 2018");
            assertThat(result).contains("Kilometraje máximo: 100,000 km");
            assertThat(result).contains("Valor mínimo: $50,000,000");
            assertThat(result).contains("Valor máximo: $300,000,000");
        }

        @Test
        @DisplayName("Should handle authorized brands request without exceptions")
        void shouldHandleAuthorizedBrandsRequestWithoutExceptions() {
            // When & Then
            assertDoesNotThrow(() -> checkVehicleEligibilityTool.getAuthorizedBrands());
        }
    }

    @Nested
    @DisplayName("Detailed Eligibility Analysis Tests")
    class DetailedEligibilityAnalysisTests {

        @Test
        @DisplayName("Should provide detailed analysis for borderline vehicle")
        void shouldProvideDetailedAnalysisForBorderlineVehicle() {
            // Given - Vehicle at the edge of eligibility
            String validVin = "1HGBH41JXMN109186";
            String brand = "HYUNDAI";
            String model = "ACCENT";
            String year = String.valueOf(Year.now().getValue() - 5); // 5 years old (within 6 year limit)
            String value = "55000000"; // Within range
            String kilometers = "95000"; // Close to 100k limit

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(true);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, year, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("VEHÍCULO ELEGIBLE");
            assertThat(result).contains("HYUNDAI ACCENT");
            assertThat(result).contains("95,000 km");
            assertThat(result).contains("5 años");
            assertThat(result).contains("dentro del límite");
            assertThat(result).contains("Valor dentro del rango");
        }

        @Test
        @DisplayName("Should show all eligibility criteria checks")
        void shouldShowAllEligibilityCriteriaChecks() {
            // Given
            String validVin = "1HGBH41JXMN109186";
            String brand = "MAZDA";
            String model = "3";
            String year = "2021";
            String value = "70000000";
            String kilometers = "30000";

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(true);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, year, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            
            // Should check all criteria
            assertThat(result).contains("VIN");
            assertThat(result).contains("Marca");
            assertThat(result).contains("Año");
            assertThat(result).contains("Valor");
            assertThat(result).contains("Kilometraje");
            assertThat(result).contains("Elegibilidad General");
            
            // Should show positive results for all
            assertThat(result).contains("✅ VIN");
            assertThat(result).contains("✅ Marca");
            assertThat(result).contains("✅ Año");
            assertThat(result).contains("✅ Valor");
            assertThat(result).contains("✅ Kilometraje");
        }

        @Test
        @DisplayName("Should identify specific failure reasons")
        void shouldIdentifySpecificFailureReasons() {
            // Given - Multiple issues
            String validVin = "1HGBH41JXMN109186";
            String brand = "SUBARU"; // Unauthorized brand
            String model = "IMPREZA";
            String year = String.valueOf(Year.now().getValue() - 8); // Too old
            String value = "25000000"; // Too low value
            String kilometers = "120000"; // Too many kilometers

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(false);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, year, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("VEHÍCULO NO ELEGIBLE");
            assertThat(result).contains("ACCIONES REQUERIDAS");
            
            // Should identify multiple issues
            long errorCount = result.lines()
                .filter(line -> line.contains("❌"))
                .count();
            assertThat(errorCount).isGreaterThan(2); // Multiple failures
        }
    }

    @Nested
    @DisplayName("Edge Cases and Validation Tests")
    class EdgeCasesAndValidationTests {

        @Test
        @DisplayName("Should handle minimum viable vehicle")
        void shouldHandleMinimumViableVehicle() {
            // Given - Minimum acceptable values
            String validVin = "1HGBH41JXMN109186";
            String brand = "KIA";
            String model = "RIO";
            String year = String.valueOf(Year.now().getValue() - 6); // Exactly 6 years (limit)
            String value = "50000000"; // Minimum value
            String kilometers = "100000"; // Maximum kilometers

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(true);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, year, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("VEHÍCULO ELEGIBLE");
            assertThat(result).contains("100,000 km (dentro del límite)");
            assertThat(result).contains("6 años");
        }

        @Test
        @DisplayName("Should handle maximum viable vehicle")
        void shouldHandleMaximumViableVehicle() {
            // Given - Maximum acceptable values
            String validVin = "1HGBH41JXMN109186";
            String brand = "FORD";
            String model = "MUSTANG";
            String year = String.valueOf(Year.now().getValue()); // New vehicle
            String value = "300000000"; // Maximum value
            String kilometers = "0"; // Brand new

            when(creditEligibilityService.isVehicleEligible(any(Vehicle.class))).thenReturn(true);

            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                validVin, brand, model, year, value, kilometers);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("VEHÍCULO ELEGIBLE");
            assertThat(result).contains("300,000,000");
            assertThat(result).contains("0 km");
            assertThat(result).contains("nuevo");
        }

        @Test
        @DisplayName("Should handle null parameters gracefully")
        void shouldHandleNullParametersGracefully() {
            // When & Then
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                null, "TOYOTA", "COROLLA", "2023", "80000000", "5000");

            assertThat(result).contains("Error");
        }

        @Test
        @DisplayName("Should handle empty parameters gracefully")
        void shouldHandleEmptyParametersGracefully() {
            // When
            String result = checkVehicleEligibilityTool.checkVehicleEligibility(
                "", "TOYOTA", "COROLLA", "2023", "80000000", "5000");

            // Then
            assertThat(result).contains("Error");
            assertThat(result).contains("VIN inválido");
        }
    }
}