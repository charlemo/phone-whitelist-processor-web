// src/main/java/com/smartibf/actions/WhitelistUploadAction.java
package com.smartibf.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.smartibf.utils.FileProcessor;
import com.smartibf.utils.FileProcessor.ProcessResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Action to handle whitelist file uploads and processing
 */
public class WhitelistUploadAction extends ActionSupport {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(WhitelistUploadAction.class);
    private static final String OUTPUT_DIR = "processed_whitelists";
    
    private List<File> whitelistFiles;
    private List<String> whitelistFilesContentType;
    private List<String> whitelistFilesFileName;
    private String batchName;
    
    // Results storage
    private String batchDirectory;
    private List<ProcessingSummary> summaries = new ArrayList<>();
    
    private final FileProcessor fileProcessor = new FileProcessor();
    
    /**
     * Upload and process whitelist files
     */
    public String upload() {
        try {
            if (whitelistFiles == null || whitelistFiles.isEmpty()) {
                addActionError("No files were uploaded");
                return INPUT;
            }
            
            // Create unique batch directory for this upload
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String batchDirName = batchName == null || batchName.trim().isEmpty() ? 
                "batch_" + timestamp : batchName.trim() + "_" + timestamp;
            
            // Get the application's real path
            String appPath = ServletActionContext.getServletContext().getRealPath("/");
            batchDirectory = appPath + File.separator + OUTPUT_DIR + File.separator + batchDirName;
            
            // Create directory
            File batchDir = new File(batchDirectory);
            if (!batchDir.exists() && !batchDir.mkdirs()) {
                addActionError("Failed to create output directory");
                return ERROR;
            }
            
            // Process each uploaded file
            StringBuilder batchSummary = new StringBuilder();
            batchSummary.append("Batch Processing Summary\n");
            batchSummary.append("=======================\n");
            batchSummary.append("Batch: ").append(batchDirName).append("\n");
            batchSummary.append("Processed on: ").append(new Date()).append("\n\n");
            
            for (int i = 0; i < whitelistFiles.size(); i++) {
                File file = whitelistFiles.get(i);
                String fileName = whitelistFilesFileName.get(i);
                
                try {
                    // Process the file
                    ProcessResult result = fileProcessor.processWhitelistFile(file.getAbsolutePath());
                    
                    // Create output filename
                    String baseName = FilenameUtils.getBaseName(fileName);
                    String outputFileName = baseName + "_cleaned.txt";
                    String outputPath = batchDirectory + File.separator + outputFileName;
                    
                    // Save results to output file
                    fileProcessor.saveToFile(result, outputPath);
                    
                    // Add summary for this file
                    ProcessingSummary summary = new ProcessingSummary();
                    summary.setFileName(fileName);
                    summary.setOutputFileName(outputFileName);
                    summary.setTotalRecords(result.getTotalRecords());
                    summary.setValidRecords(result.getValidCount());
                    summary.setInvalidRecords(result.getInvalidRecords());
                    summary.setDuplicateRecords(result.getDuplicates());
                    summaries.add(summary);
                    
                    // Add to batch summary
                    batchSummary.append("File: ").append(fileName).append("\n");
                    batchSummary.append("  Total records: ").append(result.getTotalRecords()).append("\n");
                    batchSummary.append("  Valid records: ").append(result.getValidCount()).append("\n");
                    batchSummary.append("  Invalid records: ").append(result.getInvalidRecords()).append("\n");
                    batchSummary.append("  Duplicate records: ").append(result.getDuplicates()).append("\n");
                    batchSummary.append("  Saved to: ").append(outputFileName).append("\n\n");
                    
                } catch (Exception e) {
                    LOG.error("Error processing file: " + fileName, e);
                    
                    // Add error summary
                    ProcessingSummary summary = new ProcessingSummary();
                    summary.setFileName(fileName);
                    summary.setError(true);
                    summary.setErrorMessage(e.getMessage());
                    summaries.add(summary);
                    
                    // Add to batch summary
                    batchSummary.append("File: ").append(fileName).append("\n");
                    batchSummary.append("  Error: ").append(e.getMessage()).append("\n\n");
                }
            }
            
            // Save batch summary
            String summaryPath = batchDirectory + File.separator + "_processing_summary.txt";
            java.nio.file.Files.write(java.nio.file.Paths.get(summaryPath), 
                                     batchSummary.toString().getBytes());
            
            return SUCCESS;
            
        } catch (Exception e) {
            LOG.error("Error in upload process", e);
            addActionError("Error processing upload: " + e.getMessage());
            return ERROR;
        }
    }
    
    // Getters and Setters
    
    public List<File> getWhitelistFiles() {
        return whitelistFiles;
    }
    
    public void setWhitelistFiles(List<File> whitelistFiles) {
        this.whitelistFiles = whitelistFiles;
    }
    
    public List<String> getWhitelistFilesContentType() {
        return whitelistFilesContentType;
    }
    
    public void setWhitelistFilesContentType(List<String> whitelistFilesContentType) {
        this.whitelistFilesContentType = whitelistFilesContentType;
    }
    
    public List<String> getWhitelistFilesFileName() {
        return whitelistFilesFileName;
    }
    
    public void setWhitelistFilesFileName(List<String> whitelistFilesFileName) {
        this.whitelistFilesFileName = whitelistFilesFileName;
    }
    
    public String getBatchName() {
        return batchName;
    }
    
    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }
    
    public String getBatchDirectory() {
        return batchDirectory;
    }
    
    public List<ProcessingSummary> getSummaries() {
        return summaries;
    }
    
    /**
     * Inner class to represent file processing summary for the view
     */
    public static class ProcessingSummary {
        private String fileName;
        private String outputFileName;
        private int totalRecords;
        private int validRecords;
        private int invalidRecords;
        private int duplicateRecords;
        private boolean error;
        private String errorMessage;
        
        // Getters and Setters
        
        public String getFileName() {
            return fileName;
        }
        
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
        public String getOutputFileName() {
            return outputFileName;
        }
        
        public void setOutputFileName(String outputFileName) {
            this.outputFileName = outputFileName;
        }
        
        public int getTotalRecords() {
            return totalRecords;
        }
        
        public void setTotalRecords(int totalRecords) {
            this.totalRecords = totalRecords;
        }
        
        public int getValidRecords() {
            return validRecords;
        }
        
        public void setValidRecords(int validRecords) {
            this.validRecords = validRecords;
        }
        
        public int getInvalidRecords() {
            return invalidRecords;
        }
        
        public void setInvalidRecords(int invalidRecords) {
            this.invalidRecords = invalidRecords;
        }
        
        public int getDuplicateRecords() {
            return duplicateRecords;
        }
        
        public void setDuplicateRecords(int duplicateRecords) {
            this.duplicateRecords = duplicateRecords;
        }
        
        public boolean isError() {
            return error;
        }
        
        public void setError(boolean error) {
            this.error = error;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}