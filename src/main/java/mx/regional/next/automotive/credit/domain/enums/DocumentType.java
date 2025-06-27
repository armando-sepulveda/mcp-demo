package mx.regional.next.automotive.credit.domain.enums;

public enum DocumentType {
    CEDULA("CÉDULA", "Cédula de ciudadanía"),
    PASSPORT("PASAPORTE", "Pasaporte"),
    FOREIGN_ID("CEDULA_EXTRANJERA", "Cédula de extranjería");
    
    private final String displayName;
    private final String description;
    
    DocumentType(String displayName, String description) {
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