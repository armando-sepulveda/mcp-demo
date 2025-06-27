package mx.regional.next.automotive.credit.shared.utils;

import mx.regional.next.automotive.credit.shared.constants.CreditConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class FormatUtils {

    private FormatUtils() {
        // Utility class
    }

    // Date formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter ISO_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Number formatters
    private static final DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,##0");
    private static final DecimalFormat PERCENTAGE_FORMATTER = new DecimalFormat("#0.00%");
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#,##0.00");
    private static final NumberFormat COLOMBIAN_CURRENCY = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    /**
     * Formats a BigDecimal amount as Colombian currency (without decimals)
     */
    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0";
        }
        return CreditConstants.Currency.CURRENCY_SYMBOL + CURRENCY_FORMATTER.format(amount);
    }

    /**
     * Formats a BigDecimal amount as Colombian currency with symbol
     */
    public static String formatCurrencyWithSymbol(BigDecimal amount) {
        if (amount == null) {
            return "COP $0";
        }
        return "COP " + CreditConstants.Currency.CURRENCY_SYMBOL + CURRENCY_FORMATTER.format(amount);
    }

    /**
     * Formats a BigDecimal as percentage (e.g., 0.15 -> 15.00%)
     */
    public static String formatPercentage(BigDecimal decimal) {
        if (decimal == null) {
            return "0.00%";
        }
        return PERCENTAGE_FORMATTER.format(decimal);
    }

    /**
     * Formats a BigDecimal as percentage with custom decimals
     */
    public static String formatPercentage(BigDecimal decimal, int decimalPlaces) {
        if (decimal == null) {
            return "0" + ".".repeat(Math.max(0, decimalPlaces)) + "%";
        }
        DecimalFormat customFormatter = new DecimalFormat("#0." + "0".repeat(decimalPlaces) + "%");
        return customFormatter.format(decimal);
    }

    /**
     * Formats a decimal number with thousand separators
     */
    public static String formatDecimal(BigDecimal decimal) {
        if (decimal == null) {
            return "0.00";
        }
        return DECIMAL_FORMATTER.format(decimal);
    }

    /**
     * Formats a decimal number with custom decimal places
     */
    public static String formatDecimal(BigDecimal decimal, int decimalPlaces) {
        if (decimal == null) {
            return "0" + (decimalPlaces > 0 ? "." + "0".repeat(decimalPlaces) : "");
        }
        DecimalFormat customFormatter = new DecimalFormat("#,##0" + (decimalPlaces > 0 ? "." + "0".repeat(decimalPlaces) : ""));
        return customFormatter.format(decimal);
    }

    /**
     * Formats a LocalDate as dd/MM/yyyy
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * Formats a LocalDateTime as dd/MM/yyyy HH:mm:ss
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * Formats a LocalDateTime as HH:mm:ss
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(TIME_FORMATTER);
    }

    /**
     * Formats a LocalDate as ISO format (yyyy-MM-dd)
     */
    public static String formatIsoDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(ISO_DATE_FORMATTER);
    }

    /**
     * Formats a LocalDateTime as ISO format (yyyy-MM-ddTHH:mm:ss)
     */
    public static String formatIsoDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(ISO_DATETIME_FORMATTER);
    }

    /**
     * Formats document number with dots for readability
     */
    public static String formatDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            return "";
        }
        
        String cleaned = documentNumber.replaceAll("[^0-9]", "");
        if (cleaned.length() <= 3) {
            return cleaned;
        }
        
        StringBuilder formatted = new StringBuilder();
        int length = cleaned.length();
        
        for (int i = 0; i < length; i++) {
            if (i > 0 && (length - i) % 3 == 0) {
                formatted.append(".");
            }
            formatted.append(cleaned.charAt(i));
        }
        
        return formatted.toString();
    }

    /**
     * Formats phone number for display
     */
    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "";
        }
        
        String cleaned = phoneNumber.replaceAll("[^0-9+]", "");
        
        if (cleaned.startsWith("+57")) {
            // Colombian number with country code
            if (cleaned.length() == 13) {
                return String.format("+57 %s %s %s", 
                    cleaned.substring(3, 6), 
                    cleaned.substring(6, 9), 
                    cleaned.substring(9));
            }
        } else if (cleaned.length() == 10) {
            // Colombian mobile number
            return String.format("%s %s %s", 
                cleaned.substring(0, 3), 
                cleaned.substring(3, 6), 
                cleaned.substring(6));
        } else if (cleaned.length() == 7) {
            // Colombian landline
            return String.format("%s %s", 
                cleaned.substring(0, 3), 
                cleaned.substring(3));
        }
        
        return cleaned;
    }

    /**
     * Formats VIN for display
     */
    public static String formatVin(String vin) {
        if (vin == null || vin.trim().isEmpty()) {
            return "";
        }
        
        String cleaned = vin.toUpperCase().replaceAll("[^A-HJ-NPR-Z0-9]", "");
        if (cleaned.length() == 17) {
            return String.format("%s-%s-%s-%s", 
                cleaned.substring(0, 3), 
                cleaned.substring(3, 9), 
                cleaned.substring(9, 10), 
                cleaned.substring(10));
        }
        
        return cleaned;
    }

    /**
     * Formats credit score with category
     */
    public static String formatCreditScore(Integer score) {
        if (score == null) {
            return "N/A";
        }
        
        String category = getCreditScoreCategory(score);
        return String.format("%d (%s)", score, category);
    }

    /**
     * Gets credit score category
     */
    public static String getCreditScoreCategory(Integer score) {
        if (score == null) {
            return "N/A";
        }
        
        if (score >= CreditConstants.CreditScoreRanges.EXCELLENT_THRESHOLD) {
            return "Excelente";
        } else if (score >= CreditConstants.CreditScoreRanges.VERY_GOOD_THRESHOLD) {
            return "Muy Bueno";
        } else if (score >= CreditConstants.CreditScoreRanges.GOOD_THRESHOLD) {
            return "Bueno";
        } else if (score >= CreditConstants.CreditScoreRanges.FAIR_THRESHOLD) {
            return "Regular";
        } else {
            return "Deficiente";
        }
    }

    /**
     * Formats loan term in months to years and months
     */
    public static String formatLoanTerm(Integer months) {
        if (months == null || months <= 0) {
            return "0 meses";
        }
        
        int years = months / 12;
        int remainingMonths = months % 12;
        
        if (years == 0) {
            return String.format("%d %s", months, months == 1 ? "mes" : "meses");
        } else if (remainingMonths == 0) {
            return String.format("%d %s", years, years == 1 ? "año" : "años");
        } else {
            return String.format("%d %s y %d %s", 
                years, years == 1 ? "año" : "años",
                remainingMonths, remainingMonths == 1 ? "mes" : "meses");
        }
    }

    /**
     * Formats vehicle age
     */
    public static String formatVehicleAge(Integer year) {
        if (year == null) {
            return "N/A";
        }
        
        int currentYear = LocalDate.now().getYear();
        int age = currentYear - year;
        
        if (age == 0) {
            return "Nuevo";
        } else if (age == 1) {
            return "1 año";
        } else {
            return String.format("%d años", age);
        }
    }

    /**
     * Formats mileage with units
     */
    public static String formatMileage(Integer kilometers) {
        if (kilometers == null) {
            return "N/A";
        }
        
        return String.format("%s km", CURRENCY_FORMATTER.format(kilometers));
    }

    /**
     * Formats file size in human readable format
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        
        String[] units = {"KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = bytes;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.1f %s", size, units[unitIndex]);
    }

    /**
     * Formats duration in milliseconds to readable format
     */
    public static String formatDuration(long milliseconds) {
        if (milliseconds < 1000) {
            return milliseconds + " ms";
        }
        
        long seconds = milliseconds / 1000;
        if (seconds < 60) {
            return seconds + " s";
        }
        
        long minutes = seconds / 60;
        seconds = seconds % 60;
        if (minutes < 60) {
            return String.format("%d min %d s", minutes, seconds);
        }
        
        long hours = minutes / 60;
        minutes = minutes % 60;
        return String.format("%d h %d min", hours, minutes);
    }

    /**
     * Truncates text to specified length with ellipsis
     */
    public static String truncateText(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Masks sensitive information (like document numbers)
     */
    public static String maskDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.length() < 4) {
            return "****";
        }
        
        int visibleChars = 3;
        String visible = documentNumber.substring(documentNumber.length() - visibleChars);
        String masked = "*".repeat(documentNumber.length() - visibleChars);
        return masked + visible;
    }

    /**
     * Masks email address
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "****@****.***";
        }
        
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        String maskedUsername = username.length() > 2 ? 
            username.substring(0, 2) + "*".repeat(username.length() - 2) : 
            "*".repeat(username.length());
            
        return maskedUsername + "@" + domain;
    }

    /**
     * Rounds BigDecimal to specified decimal places
     */
    public static BigDecimal roundToDecimalPlaces(BigDecimal value, int decimalPlaces) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(decimalPlaces, RoundingMode.HALF_UP);
    }

    /**
     * Converts string to title case
     */
    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }
            titleCase.append(c);
        }
        
        return titleCase.toString();
    }
}