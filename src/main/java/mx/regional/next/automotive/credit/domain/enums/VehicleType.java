package mx.regional.next.automotive.credit.domain.enums;

public enum VehicleType {
    SEDAN("SEDÁN", "Automóvil sedán"),
    HATCHBACK("HATCHBACK", "Automóvil hatchback"),
    SUV("SUV", "Vehículo utilitario deportivo"),
    PICKUP("CAMIONETA", "Camioneta pickup"),
    COUPE("COUPÉ", "Automóvil coupé"),
    CONVERTIBLE("CONVERTIBLE", "Automóvil convertible"),
    WAGON("FAMILIAR", "Automóvil familiar");
    
    private final String displayName;
    private final String description;
    
    VehicleType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}