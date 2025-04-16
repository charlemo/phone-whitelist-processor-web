// src/main/java/com/smartibf/actions/WhitelistDownloadAction.java
package com.smartibf.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
* Action to handle downloading processed whitelist files
*/
public class WhitelistDownloadAction extends ActionSupport {
   
   private static final long serialVersionUID = 1L;
   private static final Logger LOG = LogManager.getLogger(WhitelistDownloadAction.class);
   
   private String batchDir;
   private String fileName;
   private InputStream inputStream;
   
   /**
    * Download a processed file
    */
   public String download() {
       try {
           // Get the application's real path
           String appPath = ServletActionContext.getServletContext().getRealPath("/");
           
           // Validate and construct the file path
           String filePath = appPath + File.separator + batchDir + File.separator + fileName;
           File file = new File(filePath);
           
           // Security check to prevent directory traversal
           if (!file.getCanonicalPath().startsWith(new File(appPath).getCanonicalPath())) {
               addActionError("Invalid file path");
               return ERROR;
           }
           
           if (!file.exists()) {
               addActionError("File not found: " + fileName);
               return ERROR;
           }
           
           // Set up the file input stream for download
           inputStream = new FileInputStream(file);
           return SUCCESS;
           
       } catch (Exception e) {
           LOG.error("Error downloading file", e);
           addActionError("Error downloading file: " + e.getMessage());
           return ERROR;
       }
   }
   
   // Getters and Setters
   
   public String getBatchDir() {
       return batchDir;
   }
   
   public void setBatchDir(String batchDir) {
       this.batchDir = batchDir;
   }
   
   public String getFileName() {
       return fileName;
   }
   
   public void setFileName(String fileName) {
       this.fileName = fileName;
   }
   
   public InputStream getInputStream() {
       return inputStream;
   }
}