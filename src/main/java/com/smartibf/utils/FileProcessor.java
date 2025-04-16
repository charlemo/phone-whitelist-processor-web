// src/main/java/com/smartibf/utils/FileProcessor.java
package com.smartibf.utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Handles file processing operations for phone whitelist files
 */
public class FileProcessor {
    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1MB
    private static final String ALLOWED_EXTENSION = ".txt";
    private final PhoneNumberValidator validator;
    
    public FileProcessor() {
        this.validator = new PhoneNumberValidator();
    }
    
    /**
     * Process a whitelist file
     * @param filePath Path to the whitelist file
     * @return ProcessResult containing processing statistics and all numbers (valid and invalid with reasons)
     * @throws IOException If file operations fail
     */
    public ProcessResult processWhitelistFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        
        // Validate file extension
        if (!filePath.toLowerCase().endsWith(ALLOWED_EXTENSION)) {
            throw new IllegalArgumentException("Only .txt files are supported");
        }
        
        // Validate file size
        if (Files.size(path) > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds 1MB limit");
        }
        
        // Read all lines from the file first
        List<String> allLines = Files.readAllLines(path);
        
        Map<String, String> validNumbers = new LinkedHashMap<>(); // Maintains order with validation info
        Map<String, String> invalidNumbers = new LinkedHashMap<>(); // Invalid numbers with reasons
        int totalRecords = allLines.size();
        int invalidRecords = 0;
        int duplicates = 0;
        
        // Process each line
        Set<String> seenNumbers = new HashSet<>();
        
        for (String line : allLines) {
            String originalLine = line;
            
            // Skip empty lines
            if (line.trim().isEmpty()) {
                invalidNumbers.put(originalLine, "Empty line");
                invalidRecords++;
                continue;
            }
            
            PhoneNumberValidator.ValidationResult validationResult = validator.validateAndFormatWithReason(line);
            
            if (!validationResult.isValid()) {
                // Invalid phone number
                invalidNumbers.put(originalLine, validationResult.getReason());
                invalidRecords++;
            } else if (!seenNumbers.add(validationResult.getFormattedNumber())) {
                // Duplicate number
                invalidNumbers.put(originalLine, "Duplicate of " + validationResult.getFormattedNumber());
                duplicates++;
            } else {
                // Valid number
                validNumbers.put(validationResult.getFormattedNumber(), "Valid");
            }
        }
        
        return new ProcessResult(validNumbers, invalidNumbers, totalRecords, invalidRecords, duplicates);
    }
    
    /**
     * Process an input stream (from web upload)
     * @param inputStream The input stream containing whitelist data
     * @param fileName Original filename for extension validation
     * @return ProcessResult containing processing statistics
     * @throws IOException If stream operations fail
     */
    public ProcessResult processWhitelistStream(InputStream inputStream, String fileName) throws IOException {
        // Validate file extension
        if (!fileName.toLowerCase().endsWith(ALLOWED_EXTENSION)) {
            throw new IllegalArgumentException("Only .txt files are supported");
        }
        
        // Create a temporary file to process
        Path tempFile = Files.createTempFile("whitelist_upload_", ".txt");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        
        // Validate file size
        if (Files.size(tempFile) > MAX_FILE_SIZE) {
            Files.delete(tempFile);
            throw new IllegalArgumentException("File size exceeds 1MB limit");
        }
        
        try {
            return processWhitelistFile(tempFile.toString());
        } finally {
            // Clean up temp file
            Files.deleteIfExists(tempFile);
        }
    }
    
    /**
     * Save processed numbers to an output file with detailed format
     * @param result The processing result containing valid and invalid numbers
     * @param outputPath Path to save the output file
     * @throws IOException If file operations fail
     */
    public void saveToFile(ProcessResult result, String outputPath) throws IOException {
        // Ensure parent directories exist
        Path outputFilePath = Paths.get(outputPath);
        Files.createDirectories(outputFilePath.getParent());
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            // Header
            writer.write("# Phone Whitelist Processing Results");
            writer.newLine();
            writer.write("# Processed on: " + new Date());
            writer.newLine();
            writer.write("# Total records: " + result.getTotalRecords());
            writer.newLine();
            writer.write("# Valid records: " + result.getValidCount());
            writer.newLine();
            writer.write("# Invalid records: " + result.getInvalidRecords());
            writer.newLine();
            writer.write("# Duplicate records: " + result.getDuplicates());
            writer.newLine();
            writer.newLine();
            
            // Valid numbers section
            writer.write("## VALID NUMBERS");
            writer.newLine();
            for (String number : result.getValidNumbers()) {
                writer.write(number);
                writer.newLine();
            }
            
            writer.newLine();
            
            // Invalid numbers section
            writer.write("## INVALID NUMBERS (with reasons)");
            writer.newLine();
            for (Map.Entry<String, String> entry : result.getInvalidNumbersWithReasons().entrySet()) {
                writer.write(entry.getKey() + " # " + entry.getValue());
                writer.newLine();
            }
        }
    }
    
    /**
     * Class representing the result of processing a whitelist file
     */
    public static class ProcessResult {
        private final Map<String, String> validNumbers;
        private final Map<String, String> invalidNumbers;
        private final int totalRecords;
        private final int invalidRecords;
        private final int duplicates;
        
        public ProcessResult(Map<String, String> validNumbers, Map<String, String> invalidNumbers, 
                            int totalRecords, int invalidRecords, int duplicates) {
            this.validNumbers = validNumbers;
            this.invalidNumbers = invalidNumbers;
            this.totalRecords = totalRecords;
            this.invalidRecords = invalidRecords;
            this.duplicates = duplicates;
        }
        
        public List<String> getValidNumbers() {
            return new ArrayList<>(validNumbers.keySet());
        }
        
        public Map<String, String> getInvalidNumbersWithReasons() {
            return invalidNumbers;
        }
        
        public int getTotalRecords() {
            return totalRecords;
        }
        
        public int getInvalidRecords() {
            return invalidRecords;
        }
        
        public int getDuplicates() {
            return duplicates;
        }
        
        public int getValidCount() {
            return validNumbers.size();
        }
    }
}