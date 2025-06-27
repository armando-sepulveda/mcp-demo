package mx.regional.next.automotive.credit.shared.constants;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public final class CreditConstants {

    private CreditConstants() {
        // Utility class
    }

    // Credit Amount Limits
    public static final class AmountLimits {
        public static final BigDecimal MIN_CREDIT_AMOUNT = BigDecimal.valueOf(50_000);
        public static final BigDecimal MAX_CREDIT_AMOUNT = BigDecimal.valueOf(2_000_000_000);
        public static final BigDecimal MIN_VEHICLE_VALUE = BigDecimal.valueOf(50_000_000);
        public static final BigDecimal MAX_VEHICLE_VALUE = BigDecimal.valueOf(500_000_000);
        public static final BigDecimal MIN_MONTHLY_INCOME = BigDecimal.valueOf(300_000);
    }

    // Interest Rates
    public static final class InterestRates {
        public static final BigDecimal MIN_INTEREST_RATE = BigDecimal.valueOf(0.05); // 5%
        public static final BigDecimal MAX_INTEREST_RATE = BigDecimal.valueOf(0.35); // 35%
        public static final BigDecimal BASE_RATE_EXCELLENT = BigDecimal.valueOf(0.12); // 12%
        public static final BigDecimal BASE_RATE_GOOD = BigDecimal.valueOf(0.15); // 15%
        public static final BigDecimal BASE_RATE_FAIR = BigDecimal.valueOf(0.18); // 18%
        public static final BigDecimal BASE_RATE_POOR = BigDecimal.valueOf(0.22); // 22%
    }

    // Credit Score Ranges
    public static final class CreditScoreRanges {
        public static final int MIN_SCORE = 150;
        public static final int MAX_SCORE = 950;
        public static final int EXCELLENT_THRESHOLD = 750;
        public static final int VERY_GOOD_THRESHOLD = 650;
        public static final int GOOD_THRESHOLD = 550;
        public static final int FAIR_THRESHOLD = 450;
        public static final int POOR_THRESHOLD = 350;
    }

    // Term Limits (in months)
    public static final class TermLimits {
        public static final int MIN_TERM_MONTHS = 12;
        public static final int MAX_TERM_MONTHS = 84;
        public static final int STANDARD_TERM_MONTHS = 60;
    }

    // Vehicle Criteria
    public static final class VehicleCriteria {
        public static final int MAX_VEHICLE_AGE_YEARS = 6;
        public static final int MAX_VEHICLE_KILOMETERS = 100_000;
        public static final int MIN_VEHICLE_YEAR = 2018;
        
        public static final Set<String> AUTHORIZED_BRANDS = Set.of(
            "TOYOTA", "CHEVROLET", "RENAULT", "NISSAN", 
            "HYUNDAI", "KIA", "MAZDA", "FORD"
        );
    }

    // LTV (Loan to Value) Ratios
    public static final class LtvRatios {
        public static final BigDecimal MAX_LTV_NEW_VEHICLE = BigDecimal.valueOf(0.90); // 90%
        public static final BigDecimal MAX_LTV_USED_VEHICLE = BigDecimal.valueOf(0.80); // 80%
        public static final BigDecimal MIN_DOWN_PAYMENT = BigDecimal.valueOf(0.10); // 10%
    }

    // Income Requirements
    public static final class IncomeRequirements {
        public static final BigDecimal MAX_DEBT_TO_INCOME = BigDecimal.valueOf(0.50); // 50%
        public static final BigDecimal MAX_PAYMENT_TO_INCOME = BigDecimal.valueOf(0.30); // 30%
        public static final BigDecimal MIN_DEBT_SERVICE_COVERAGE = BigDecimal.valueOf(1.25); // 125%
    }

    // Document Types
    public static final class DocumentTypes {
        public static final String CEDULA_CIUDADANIA = "CC";
        public static final String CEDULA_EXTRANJERIA = "CE";
        public static final String TARJETA_IDENTIDAD = "TI";
        public static final String PASAPORTE = "PA";
        public static final String NIT = "NIT";
        
        public static final Set<String> VALID_DOCUMENT_TYPES = Set.of(
            CEDULA_CIUDADANIA, CEDULA_EXTRANJERIA, TARJETA_IDENTIDAD, PASAPORTE, NIT
        );
    }

    // Customer Types
    public static final class CustomerTypes {
        public static final String NATURAL_PERSON = "NATURAL";
        public static final String JURIDICAL_PERSON = "JURIDICAL";
        public static final String EMPLOYEE = "EMPLOYEE";
        public static final String INDEPENDENT = "INDEPENDENT";
    }

    // Employment Types
    public static final class EmploymentTypes {
        public static final String PUBLIC_EMPLOYEE = "PUBLIC";
        public static final String PRIVATE_EMPLOYEE = "PRIVATE";
        public static final String INDEPENDENT_PROFESSIONAL = "INDEPENDENT";
        public static final String BUSINESS_OWNER = "BUSINESS";
    }

    // Risk Categories
    public static final class RiskCategories {
        public static final String MINIMAL_RISK = "AAA";
        public static final String LOW_RISK = "AA";
        public static final String MEDIUM_LOW_RISK = "A";
        public static final String MEDIUM_RISK = "BBB";
        public static final String MEDIUM_HIGH_RISK = "BB";
        public static final String HIGH_RISK = "B";
        public static final String VERY_HIGH_RISK = "C";
    }

    // Application Status
    public static final class ApplicationStatus {
        public static final String PENDING = "PENDING";
        public static final String IN_EVALUATION = "IN_EVALUATION";
        public static final String IN_COMMITTEE = "IN_COMMITTEE";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
        public static final String DOCUMENTATION_PENDING = "DOCUMENTATION_PENDING";
        public static final String SUSPENDED = "SUSPENDED";
        public static final String DISBURSED = "DISBURSED";
        public static final String CANCELLED = "CANCELLED";
    }

    // Notification Types
    public static final class NotificationTypes {
        public static final String EMAIL = "EMAIL";
        public static final String SMS = "SMS";
        public static final String PUSH = "PUSH";
        public static final String IN_APP = "IN_APP";
        public static final String WEBHOOK = "WEBHOOK";
    }

    // Processing Priorities
    public static final class Priorities {
        public static final String LOW = "LOW";
        public static final String NORMAL = "NORMAL";
        public static final String HIGH = "HIGH";
        public static final String URGENT = "URGENT";
    }

    // External Service Timeouts (milliseconds)
    public static final class Timeouts {
        public static final int CREDIT_BUREAU_TIMEOUT = 30_000;
        public static final int VEHICLE_VALUATION_TIMEOUT = 25_000;
        public static final int EMPLOYMENT_VERIFICATION_TIMEOUT = 20_000;
        public static final int NOTIFICATION_TIMEOUT = 10_000;
        public static final int DEFAULT_CONNECT_TIMEOUT = 5_000;
    }

    // Validation Patterns
    public static final class ValidationPatterns {
        public static final String DOCUMENT_NUMBER_PATTERN = "^[0-9]{8,11}$";
        public static final String PHONE_NUMBER_PATTERN = "^\\+?[1-9]\\d{1,14}$";
        public static final String VIN_PATTERN = "^[A-HJ-NPR-Z0-9]{17}$";
        public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        public static final String CURRENCY_PATTERN = "^\\d+(\\.\\d{1,2})?$";
    }

    // Error Codes
    public static final class ErrorCodes {
        public static final String INVALID_DOCUMENT = "ERR_001";
        public static final String INSUFFICIENT_INCOME = "ERR_002";
        public static final String POOR_CREDIT_SCORE = "ERR_003";
        public static final String VEHICLE_NOT_ELIGIBLE = "ERR_004";
        public static final String EXCESSIVE_DEBT = "ERR_005";
        public static final String MISSING_DOCUMENTATION = "ERR_006";
        public static final String SERVICE_UNAVAILABLE = "ERR_007";
        public static final String VALIDATION_FAILED = "ERR_008";
        public static final String AUTHENTICATION_FAILED = "ERR_009";
        public static final String AUTHORIZATION_FAILED = "ERR_010";
    }

    // Business Rules
    public static final class BusinessRules {
        public static final int MIN_AGE_APPLICANT = 18;
        public static final int MAX_AGE_APPLICANT = 75;
        public static final int MIN_EMPLOYMENT_MONTHS = 6;
        public static final int MIN_BUSINESS_MONTHS = 12;
        public static final int CREDIT_HISTORY_MONTHS = 24;
        public static final int REFERENCE_COUNT_MINIMUM = 2;
    }

    // Geographic Constants
    public static final class Geography {
        public static final List<String> MAJOR_CITIES = List.of(
            "BOGOTÁ", "MEDELLÍN", "CALI", "BARRANQUILLA", "CARTAGENA",
            "CÚCUTA", "BUCARAMANGA", "PEREIRA", "IBAGUÉ", "SANTA MARTA"
        );
        
        public static final List<String> DEPARTMENTS = List.of(
            "AMAZONAS", "ANTIOQUIA", "ARAUCA", "ATLÁNTICO", "BOLÍVAR",
            "BOYACÁ", "CALDAS", "CAQUETÁ", "CASANARE", "CAUCA",
            "CESAR", "CHOCÓ", "CÓRDOBA", "CUNDINAMARCA", "GUAINÍA",
            "GUAVIARE", "HUILA", "LA GUAJIRA", "MAGDALENA", "META",
            "NARIÑO", "NORTE DE SANTANDER", "PUTUMAYO", "QUINDÍO",
            "RISARALDA", "SAN ANDRÉS Y PROVIDENCIA", "SANTANDER",
            "SUCRE", "TOLIMA", "VALLE DEL CAUCA", "VAUPÉS", "VICHADA"
        );
    }

    // Currency and Formatting
    public static final class Currency {
        public static final String CURRENCY_CODE = "COP";
        public static final String CURRENCY_SYMBOL = "$";
        public static final int DECIMAL_PLACES = 0;
        public static final String THOUSAND_SEPARATOR = ",";
    }

    // Cache Configuration
    public static final class Cache {
        public static final String CREDIT_SCORE_CACHE = "creditScoreCache";
        public static final String VEHICLE_VALUATION_CACHE = "vehicleValuationCache";
        public static final String EMPLOYMENT_VERIFICATION_CACHE = "employmentVerificationCache";
        public static final int DEFAULT_TTL_MINUTES = 60;
        public static final int CREDIT_SCORE_TTL_MINUTES = 1440; // 24 hours
        public static final int VEHICLE_VALUATION_TTL_MINUTES = 720; // 12 hours
    }

    // Audit and Compliance
    public static final class Audit {
        public static final String CREATE_ACTION = "CREATE";
        public static final String UPDATE_ACTION = "UPDATE";
        public static final String DELETE_ACTION = "DELETE";
        public static final String VIEW_ACTION = "VIEW";
        public static final String APPROVE_ACTION = "APPROVE";
        public static final String REJECT_ACTION = "REJECT";
    }

    // Feature Flags
    public static final class FeatureFlags {
        public static final String ADVANCED_RISK_SCORING = "advanced.risk.scoring";
        public static final String REAL_TIME_NOTIFICATIONS = "realtime.notifications";
        public static final String AUTOMATED_APPROVALS = "automated.approvals";
        public static final String DIGITAL_SIGNATURES = "digital.signatures";
        public static final String MOBILE_VERIFICATION = "mobile.verification";
    }
}