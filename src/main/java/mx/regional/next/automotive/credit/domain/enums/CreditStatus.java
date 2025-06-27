package mx.regional.next.automotive.credit.domain.enums;

public enum CreditStatus {
    PENDING("PENDIENTE", "Solicitud en proceso de evaluación"),
    APPROVED("APROBADO", "Solicitud aprobada"),
    REJECTED("RECHAZADO", "Solicitud rechazada"),
    EXPIRED("EXPIRADO", "Solicitud expirada"),
    CANCELLED("CANCELADO", "Solicitud cancelada por el cliente");
    
    private final String displayName;
    private final String description;
    
    CreditStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isActive() {
        return this == PENDING || this == APPROVED;
    }
    
    public boolean isFinal() {
        return this == APPROVED || this == REJECTED || this == EXPIRED || this == CANCELLED;
    }
}