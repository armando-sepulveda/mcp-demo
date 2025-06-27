package mx.regional.next.automotive.credit.shared.utils;

import mx.regional.next.automotive.credit.shared.constants.CreditConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private ValidationUtils() {
        // Utility class
    }

    // Document validation patterns
    private static final Pattern DOCUMENT_PATTERN = Pattern.compile(CreditConstants.ValidationPatterns.DOCUMENT_NUMBER_PATTERN);
    private static final Pattern PHONE_PATTERN = Pattern.compile(CreditConstants.ValidationPatterns.PHONE_NUMBER_PATTERN);
    private static final Pattern VIN_PATTERN = Pattern.compile(CreditConstants.ValidationPatterns.VIN_PATTERN);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(CreditConstants.ValidationPatterns.EMAIL_PATTERN);
    private static final Pattern CURRENCY_PATTERN = Pattern.compile(CreditConstants.ValidationPatterns.CURRENCY_PATTERN);

    /**
     * Validates Colombian document number format
     */
    public static boolean isValidDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            return false;
        }
        return DOCUMENT_PATTERN.matcher(documentNumber.trim()).matches();
    }

    /**
     * Validates document type
     */
    public static boolean isValidDocumentType(String documentType) {
        if (documentType == null || documentType.trim().isEmpty()) {
            return false;
        }
        return CreditConstants.DocumentTypes.VALID_DOCUMENT_TYPES.contains(documentType.trim().toUpperCase());
    }

    /**
     * Validates phone number format (international format accepted)
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }

    /**
     * Validates VIN (Vehicle Identification Number)
     */
    public static boolean isValidVin(String vin) {
        if (vin == null || vin.trim().isEmpty()) {
            return false;
        }
        return VIN_PATTERN.matcher(vin.trim().toUpperCase()).matches();
    }

    /**
     * Validates email address format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim().toLowerCase()).matches();
    }

    /**
     * Validates credit amount within allowed limits
     */
    public static boolean isValidCreditAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        return amount.compareTo(CreditConstants.AmountLimits.MIN_CREDIT_AMOUNT) >= 0 &&
               amount.compareTo(CreditConstants.AmountLimits.MAX_CREDIT_AMOUNT) <= 0;
    }

    /**
     * Validates vehicle value within allowed limits
     */
    public static boolean isValidVehicleValue(BigDecimal value) {
        if (value == null) {
            return false;
        }
        return value.compareTo(CreditConstants.AmountLimits.MIN_VEHICLE_VALUE) >= 0 &&
               value.compareTo(CreditConstants.AmountLimits.MAX_VEHICLE_VALUE) <= 0;
    }

    /**
     * Validates monthly income meets minimum requirements
     */
    public static boolean isValidMonthlyIncome(BigDecimal income) {
        if (income == null) {
            return false;
        }
        return income.compareTo(CreditConstants.AmountLimits.MIN_MONTHLY_INCOME) >= 0;
    }

    /**
     * Validates credit score within valid range
     */
    public static boolean isValidCreditScore(Integer score) {
        if (score == null) {
            return false;
        }
        return score >= CreditConstants.CreditScoreRanges.MIN_SCORE &&
               score <= CreditConstants.CreditScoreRanges.MAX_SCORE;
    }

    /**
     * Validates interest rate within allowed range
     */
    public static boolean isValidInterestRate(BigDecimal rate) {
        if (rate == null) {
            return false;
        }
        return rate.compareTo(CreditConstants.InterestRates.MIN_INTEREST_RATE) >= 0 &&
               rate.compareTo(CreditConstants.InterestRates.MAX_INTEREST_RATE) <= 0;
    }

    /**
     * Validates loan term within allowed range
     */
    public static boolean isValidLoanTerm(Integer termMonths) {
        if (termMonths == null) {
            return false;
        }
        return termMonths >= CreditConstants.TermLimits.MIN_TERM_MONTHS &&
               termMonths <= CreditConstants.TermLimits.MAX_TERM_MONTHS;
    }

    /**
     * Validates vehicle brand is authorized
     */
    public static boolean isAuthorizedVehicleBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            return false;
        }
        return CreditConstants.VehicleCriteria.AUTHORIZED_BRANDS.contains(brand.trim().toUpperCase());
    }

    /**
     * Validates vehicle age criteria
     */
    public static boolean isValidVehicleAge(Integer year) {
        if (year == null) {
            return false;
        }
        int currentYear = LocalDate.now().getYear();
        int vehicleAge = currentYear - year;
        return vehicleAge <= CreditConstants.VehicleCriteria.MAX_VEHICLE_AGE_YEARS &&
               year >= CreditConstants.VehicleCriteria.MIN_VEHICLE_YEAR;
    }

    /**
     * Validates vehicle mileage criteria
     */
    public static boolean isValidVehicleMileage(Integer kilometers) {
        if (kilometers == null) {
            return false;
        }
        return kilometers >= 0 && kilometers <= CreditConstants.VehicleCriteria.MAX_VEHICLE_KILOMETERS;
    }

    /**
     * Validates applicant age requirements
     */
    public static boolean isValidApplicantAge(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return age >= CreditConstants.BusinessRules.MIN_AGE_APPLICANT &&
               age <= CreditConstants.BusinessRules.MAX_AGE_APPLICANT;
    }

    /**
     * Validates debt-to-income ratio
     */
    public static boolean isValidDebtToIncomeRatio(BigDecimal totalDebt, BigDecimal monthlyIncome) {
        if (totalDebt == null || monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal ratio = totalDebt.divide(monthlyIncome, 4, BigDecimal.ROUND_HALF_UP);
        return ratio.compareTo(CreditConstants.IncomeRequirements.MAX_DEBT_TO_INCOME) <= 0;
    }

    /**
     * Validates payment-to-income ratio
     */
    public static boolean isValidPaymentToIncomeRatio(BigDecimal monthlyPayment, BigDecimal monthlyIncome) {
        if (monthlyPayment == null || monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal ratio = monthlyPayment.divide(monthlyIncome, 4, BigDecimal.ROUND_HALF_UP);
        return ratio.compareTo(CreditConstants.IncomeRequirements.MAX_PAYMENT_TO_INCOME) <= 0;
    }

    /**
     * Validates loan-to-value ratio
     */
    public static boolean isValidLoanToValueRatio(BigDecimal loanAmount, BigDecimal vehicleValue, boolean isNewVehicle) {
        if (loanAmount == null || vehicleValue == null || vehicleValue.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal ratio = loanAmount.divide(vehicleValue, 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal maxLtv = isNewVehicle ? 
            CreditConstants.LtvRatios.MAX_LTV_NEW_VEHICLE : 
            CreditConstants.LtvRatios.MAX_LTV_USED_VEHICLE;
        return ratio.compareTo(maxLtv) <= 0;
    }

    /**
     * Validates currency amount format
     */
    public static boolean isValidCurrencyFormat(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return false;
        }
        return CURRENCY_PATTERN.matcher(amount.trim()).matches();
    }

    /**
     * Validates employment duration meets minimum requirements
     */
    public static boolean isValidEmploymentDuration(LocalDate hireDate, boolean isEmployee) {
        if (hireDate == null) {
            return false;
        }
        int monthsEmployed = Period.between(hireDate, LocalDate.now()).getMonths();
        int minMonths = isEmployee ? 
            CreditConstants.BusinessRules.MIN_EMPLOYMENT_MONTHS : 
            CreditConstants.BusinessRules.MIN_BUSINESS_MONTHS;
        return monthsEmployed >= minMonths;
    }

    /**
     * Validates that a string is not null or empty
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates that a number is positive
     */
    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Validates that a number is non-negative
     */
    public static boolean isNonNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Validates that an integer is positive
     */
    public static boolean isPositive(Integer value) {
        return value != null && value > 0;
    }

    /**
     * Validates that an integer is non-negative
     */
    public static boolean isNonNegative(Integer value) {
        return value != null && value >= 0;
    }

    /**
     * Validates date is in the past
     */
    public static boolean isInThePast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * Validates date is in the future
     */
    public static boolean isInTheFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    /**
     * Validates date is within a specific range
     */
    public static boolean isDateWithinRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Validates percentage value (0-100)
     */
    public static boolean isValidPercentage(BigDecimal percentage) {
        if (percentage == null) {
            return false;
        }
        return percentage.compareTo(BigDecimal.ZERO) >= 0 && 
               percentage.compareTo(BigDecimal.valueOf(100)) <= 0;
    }

    /**
     * Validates decimal percentage (0.0-1.0)
     */
    public static boolean isValidDecimalPercentage(BigDecimal percentage) {
        if (percentage == null) {
            return false;
        }
        return percentage.compareTo(BigDecimal.ZERO) >= 0 && 
               percentage.compareTo(BigDecimal.ONE) <= 0;
    }
}