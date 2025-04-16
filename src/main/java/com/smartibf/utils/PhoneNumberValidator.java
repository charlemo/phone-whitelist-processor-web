// src/main/java/com/smartibf/utils/PhoneNumberValidator.java
package com.smartibf.utils;

/**
 * Validates and formats phone numbers according to requirements
 */
public class PhoneNumberValidator {
    
    /**
     * Validates and formats a phone number
     * @param phoneNumber The phone number to validate
     * @return The formatted phone number or null if invalid
     */
    public String validateAndFormat(String phoneNumber) {
        ValidationResult result = validateAndFormatWithReason(phoneNumber);
        return result.isValid() ? result.getFormattedNumber() : null;
    }
    
    /**
     * Validates and formats a phone number with detailed reason
     * @param phoneNumber The phone number to validate
     * @return ValidationResult containing result and reason
     */
    public ValidationResult validateAndFormatWithReason(String phoneNumber) {
        if (phoneNumber == null) {
            return new ValidationResult(false, null, "Null input");
        }
        
        // Remove all whitespace
        String cleaned = phoneNumber.replaceAll("\\s+", "");
        
        // Skip empty strings after trimming
        if (cleaned.isEmpty()) {
            return new ValidationResult(false, null, "Empty after removing whitespace");
        }
        
        // Store original for reporting
        String original = cleaned;
        
        // Remove special characters
        cleaned = cleaned.replaceAll("[^a-zA-Z0-9]", "");
        
        // Check if the number contains any alphabetic characters
        if (cleaned.matches(".*[a-zA-Z].*")) {
            return new ValidationResult(false, null, "Contains alphabetic characters");
        }
        
        // Convert 09xxx format to 639xxx format
        if (cleaned.startsWith("09") && cleaned.length() == 11) {
            cleaned = "63" + cleaned.substring(1);
        }
        
        // Validation checks with specific reasons
        if (!cleaned.startsWith("639")) {
            return new ValidationResult(false, null, "Does not start with '639'");
        }
        
        if (cleaned.length() != 12) {
            if (cleaned.length() < 12) {
                return new ValidationResult(false, null, "Too short (should be 12 digits)");
            } else {
                return new ValidationResult(false, null, "Too long (should be 12 digits)");
            }
        }
        
        // Valid Philippine mobile number
        return new ValidationResult(true, cleaned, "Valid");
    }
    
    /**
     * Class to store validation result with reason
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String formattedNumber;
        private final String reason;
        
        public ValidationResult(boolean valid, String formattedNumber, String reason) {
            this.valid = valid;
            this.formattedNumber = formattedNumber;
            this.reason = reason;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getFormattedNumber() {
            return formattedNumber;
        }
        
        public String getReason() {
            return reason;
        }
    }
}