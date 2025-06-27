package mx.regional.next.automotive.credit.application.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class CreditApplicationRequest {
    
    @NotBlank(message = "El documento del cliente es obligatorio")
    private String customerDocument;
    
    @NotNull(message = "El monto solicitado es obligatorio")
    @DecimalMin(value = "50000", message = "El monto mínimo es $50,000")
    @DecimalMax(value = "2000000", message = "El monto máximo es $2,000,000")
    private BigDecimal requestedAmount;
    
    @NotBlank(message = "El VIN del vehículo es obligatorio")
    private String vehicleVin;
    
    @NotBlank(message = "La marca del vehículo es obligatoria")
    private String vehicleBrand;
    
    @NotBlank(message = "El modelo del vehículo es obligatorio")
    private String vehicleModel;
    
    @Min(value = 2010, message = "El año mínimo del vehículo es 2010")
    @Max(value = 2024, message = "El año máximo del vehículo es el año actual")
    private int vehicleYear;
    
    @NotNull(message = "El valor del vehículo es obligatorio")
    @DecimalMin(value = "1000000", message = "El valor mínimo del vehículo es $1,000,000")
    private BigDecimal vehicleValue;
    
    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    private int vehicleKilometers;
    
    private String vehicleColor;
    private String vehicleEngine;
    private String vehicleTransmission;
    
    @NotEmpty(message = "Se requiere al menos un documento")
    private List<DocumentRequest> documents;
    
    // Constructors
    public CreditApplicationRequest() {}
    
    public CreditApplicationRequest(String customerDocument, BigDecimal requestedAmount,
                                  String vehicleVin, String vehicleBrand, String vehicleModel,
                                  int vehicleYear, BigDecimal vehicleValue, int vehicleKilometers,
                                  String vehicleColor, String vehicleEngine, String vehicleTransmission,
                                  List<DocumentRequest> documents) {
        this.customerDocument = customerDocument;
        this.requestedAmount = requestedAmount;
        this.vehicleVin = vehicleVin;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.vehicleValue = vehicleValue;
        this.vehicleKilometers = vehicleKilometers;
        this.vehicleColor = vehicleColor;
        this.vehicleEngine = vehicleEngine;
        this.vehicleTransmission = vehicleTransmission;
        this.documents = documents;
    }
    
    // Getters and Setters
    public String getCustomerDocument() { return customerDocument; }
    public void setCustomerDocument(String customerDocument) { this.customerDocument = customerDocument; }
    
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
    
    public String getVehicleVin() { return vehicleVin; }
    public void setVehicleVin(String vehicleVin) { this.vehicleVin = vehicleVin; }
    
    public String getVehicleBrand() { return vehicleBrand; }
    public void setVehicleBrand(String vehicleBrand) { this.vehicleBrand = vehicleBrand; }
    
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    
    public int getVehicleYear() { return vehicleYear; }
    public void setVehicleYear(int vehicleYear) { this.vehicleYear = vehicleYear; }
    
    public BigDecimal getVehicleValue() { return vehicleValue; }
    public void setVehicleValue(BigDecimal vehicleValue) { this.vehicleValue = vehicleValue; }
    
    public int getVehicleKilometers() { return vehicleKilometers; }
    public void setVehicleKilometers(int vehicleKilometers) { this.vehicleKilometers = vehicleKilometers; }
    
    public String getVehicleColor() { return vehicleColor; }
    public void setVehicleColor(String vehicleColor) { this.vehicleColor = vehicleColor; }
    
    public String getVehicleEngine() { return vehicleEngine; }
    public void setVehicleEngine(String vehicleEngine) { this.vehicleEngine = vehicleEngine; }
    
    public String getVehicleTransmission() { return vehicleTransmission; }
    public void setVehicleTransmission(String vehicleTransmission) { this.vehicleTransmission = vehicleTransmission; }
    
    public List<DocumentRequest> getDocuments() { return documents; }
    public void setDocuments(List<DocumentRequest> documents) { this.documents = documents; }
    
    public static class DocumentRequest {
        @NotBlank(message = "El tipo de documento es obligatorio")
        private String type;
        
        @NotBlank(message = "El contenido del documento es obligatorio")
        private String content;
        
        public DocumentRequest() {}
        
        public DocumentRequest(String type, String content) {
            this.type = type;
            this.content = content;
        }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}