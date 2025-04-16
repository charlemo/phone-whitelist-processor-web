// src/test/java/com/smartibf/utils/PhoneNumberValidatorTest.java
package com.smartibf.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PhoneNumberValidatorTest {
    
    private PhoneNumberValidator validator;
    
    @Before
    public void setup() {
        validator = new PhoneNumberValidator();
    }
    
    @Test
    public void testValidPhoneNumbers() {
        // Test valid numbers
        assertTrue(validator.validateAndFormatWithReason("639123456789").isValid());
        assertTrue(validator.validateAndFormatWithReason("09123456789").isValid());
        assertTrue(validator.validateAndFormatWithReason("639 123 456 789").isValid());
        assertTrue(validator.validateAndFormatWithReason("+639123456789").isValid());
    }
    
    @Test
    public void testInvalidPhoneNumbers() {
        // Test invalid numbers
        assertFalse(validator.validateAndFormatWithReason("1234567890").isValid());
        assertFalse(validator.validateAndFormatWithReason("abc123").isValid());
        assertFalse(validator.validateAndFormatWithReason("639123").isValid());
        assertFalse(validator.validateAndFormatWithReason("6391234567890123").isValid());
        assertFalse(validator.validateAndFormatWithReason("").isValid());
        assertFalse(validator.validateAndFormatWithReason(null).isValid());
    }
    
    @Test
    public void testFormatting() {
        // Test formatting
        assertEquals("639123456789", validator.validateAndFormat("09123456789"));
        assertEquals("639123456789", validator.validateAndFormat("639123456789"));
        assertEquals("639123456789", validator.validateAndFormat("+639123456789"));
        assertEquals("639123456789", validator.validateAndFormat("639-123-456-789"));
    }
    
    @Test
    public void testInvalidReasons() {
        // Test specific error reasons
        assertEquals("Does not start with '639'", 
                    validator.validateAndFormatWithReason("1234567890").getReason());
        assertEquals("Contains alphabetic characters", 
                    validator.validateAndFormatWithReason("abc123").getReason());
        assertEquals("Too short (should be 12 digits)", 
                    validator.validateAndFormatWithReason("639123").getReason());
        assertEquals("Too long (should be 12 digits)", 
                    validator.validateAndFormatWithReason("6391234567890123").getReason());
        assertEquals("Empty after removing whitespace", 
                    validator.validateAndFormatWithReason("").getReason());
        assertEquals("Null input", 
                    validator.validateAndFormatWithReason(null).getReason());
    }
}