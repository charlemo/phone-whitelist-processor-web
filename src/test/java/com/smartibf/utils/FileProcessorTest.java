// src/test/java/com/smartibf/utils/FileProcessorTest.java
package com.smartibf.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class FileProcessorTest {
    
    private FileProcessor processor;
    private Path testFile;
    private Path outputFile;
    
    @Before
    public void setup() throws IOException {
        processor = new FileProcessor();
        
        // Create a test file with sample phone numbers
        testFile = Files.createTempFile("test_whitelist_", ".txt");
        outputFile = Paths.get(testFile.toString().replace(".txt", "_output.txt"));
        
        // Write sample phone numbers to the test file
        String content = 
            "639123456789\n" +
            "09987654321\n" +
            "invalid\n" +
            "639123456789\n" +  // Duplicate
            "639555666777\n" +
            "123456\n" +        // Invalid
            "+639999000111\n";  // Valid with plus sign
        
        Files.write(testFile, content.getBytes());
    }
    
    @After
    public void cleanup() throws IOException {
        // Delete test files after test
        Files.deleteIfExists(testFile);
        Files.deleteIfExists(outputFile);
    }
    
    @Test
    public void testProcessFile() throws IOException {
        // Process the test file
        FileProcessor.ProcessResult result = processor.processWhitelistFile(testFile.toString());
        
        // Validate results
        assertEquals(7, result.getTotalRecords());
        assertEquals(3, result.getValidCount());
        assertEquals(3, result.getInvalidRecords());
        assertEquals(1, result.getDuplicates());
        
        // Validate valid numbers
        assertTrue(result.getValidNumbers().contains("639123456789"));
        assertTrue(result.getValidNumbers().contains("639987654321"));
        assertTrue(result.getValidNumbers().contains("639999000111"));
        
        // Validate invalid reasons
        assertTrue(result.getInvalidNumbersWithReasons().containsKey("invalid"));
        assertTrue(result.getInvalidNumbersWithReasons().containsKey("123456"));
    }
    
    @Test
    public void testProcessStream() throws IOException {
        // Test input stream processing
        String content = "639123456789\n09987654321\ninvalid";
        ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        
        FileProcessor.ProcessResult result = processor.processWhitelistStream(stream, "test.txt");
        
        assertEquals(3, result.getTotalRecords());
        assertEquals(2, result.getValidCount());
        assertEquals(1, result.getInvalidRecords());
    }
    
    @Test
    public void testSaveToFile() throws IOException {
        // Process the test file
        FileProcessor.ProcessResult result = processor.processWhitelistFile(testFile.toString());
        
        // Save results to output file
        processor.saveToFile(result, outputFile.toString());
        
        // Verify the output file exists
        assertTrue(Files.exists(outputFile));
        
        // Read the output file content
        String outputContent = new String(Files.readAllBytes(outputFile));
        
        // Verify it contains the expected headers and valid numbers
        assertTrue(outputContent.contains("# Total records: 7"));
        assertTrue(outputContent.contains("# Valid records: 3"));
        assertTrue(outputContent.contains("# Invalid records: 3"));
        assertTrue(outputContent.contains("# Duplicate records: 1"));
        assertTrue(outputContent.contains("639123456789"));
        assertTrue(outputContent.contains("639987654321"));
        assertTrue(outputContent.contains("639999000111"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFileExtension() throws IOException {
        // Create a temporary file with wrong extension
        Path wrongFile = Files.createTempFile("test_", ".csv");
        try {
            processor.processWhitelistFile(wrongFile.toString());
        } finally {
            Files.deleteIfExists(wrongFile);
        }
    }
}