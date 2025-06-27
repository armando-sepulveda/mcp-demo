package mx.regional.next.shared.common.constants;

import java.math.BigDecimal;

public final class BusinessConstants {
    
    // Credit Application Constants
    public static final BigDecimal MIN_CREDIT_AMOUNT = BigDecimal.valueOf(20_000_000);
    public static final BigDecimal MAX_CREDIT_AMOUNT = BigDecimal.valueOf(200_000_000);
    public static final BigDecimal MIN_VEHICLE_VALUE = BigDecimal.valueOf(50_000_000);
    public static final BigDecimal MAX_VEHICLE_VALUE = BigDecimal.valueOf(300_000_000);
    
    // Credit Score Constants
    public static final int MIN_CREDIT_SCORE = 300;
    public static final int MAX_CREDIT_SCORE = 850;
    public static final int MIN_ACCEPTABLE_SCORE = 600;
    
    // Financial Ratios
    public static final BigDecimal MAX_DEBT_TO_INCOME_RATIO = BigDecimal.valueOf(0.40);
    public static final BigDecimal MAX_PAYMENT_TO_INCOME_RATIO = BigDecimal.valueOf(0.30);
    public static final BigDecimal MAX_FINANCING_RATIO = BigDecimal.valueOf(0.90);
    
    // Vehicle Constants
    public static final int MIN_VEHICLE_YEAR = 2018;
    public static final int MAX_VEHICLE_KILOMETERS = 100_000;
    public static final int MIN_CUSTOMER_AGE = 18;
    public static final int MAX_CUSTOMER_AGE_AT_TERM_END = 70;
    
    // Term Limits
    public static final int MIN_TERM_MONTHS = 12;
    public static final int MAX_TERM_MONTHS = 84;
    public static final int STANDARD_TERM_MONTHS = 60;
    
    // Interest Rates
    public static final BigDecimal MIN_INTEREST_RATE = BigDecimal.valueOf(0.10);
    public static final BigDecimal MAX_INTEREST_RATE = BigDecimal.valueOf(0.25);
    
    // Minimum Income
    public static final BigDecimal MIN_MONTHLY_INCOME = BigDecimal.valueOf(2_500_000);
    
    private BusinessConstants() {
        // Utility class
    }
}