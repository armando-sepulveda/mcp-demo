package mx.regional.next.automotive.credit.application.ports.out;

import mx.regional.next.automotive.credit.domain.entities.Vehicle;

public interface VehicleValidationPort {
    VehicleValidationResult validateVehicle(String vin, String brand, String model, int year);
    
    class VehicleValidationResult {
        private final boolean valid;
        private final String errorMessage;
        private final Vehicle vehicle;
        
        public VehicleValidationResult(boolean valid, String errorMessage, Vehicle vehicle) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.vehicle = vehicle;
        }
        
        public static VehicleValidationResult valid(Vehicle vehicle) {
            return new VehicleValidationResult(true, null, vehicle);
        }
        
        public static VehicleValidationResult invalid(String errorMessage) {
            return new VehicleValidationResult(false, errorMessage, null);
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
        public Vehicle getVehicle() { return vehicle; }
    }
}