<!-- src/main/webapp/pages/result.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Processing Results</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .container { margin-top: 30px; }
        .header { margin-bottom: 30px; }
        .summary-card { margin-bottom: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header text-center">
            <h1>Processing Results</h1>
            <p class="lead">Summary of processed whitelist files</p>
        </div>
        
        <!-- Display errors if any -->
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger">
                <s:actionerror/>
            </div>
        </s:if>
        
        <!-- Summary table -->
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h4>Processing Summary</h4>
            </div>
            <div class="card-body">
                <s:if test="summaries.size() > 0">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>File Name</th>
                                <th>Total Records</th>
                                <th>Valid</th>
                                <th>Invalid</th>
                                <th>Duplicates</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:iterator value="summaries">
                                <tr>
                                    <td><s:property value="fileName" /></td>
                                    <s:if test="error">
                                        <td colspan="4" class="text-danger">
                                            Error: <s:property value="errorMessage" />
                                        </td>
                                    </s:if>
                                    <s:else>
                                        <td><s:property value="totalRecords" /></td>
                                        <td><s:property value="validRecords" /></td>
                                        <td><s:property value="invalidRecords" /></td>
                                        <td><s:property value="duplicateRecords" /></td>
                                    </s:else>
                                    <td>
                                        <s:if test="!error">
                                            <s:url var="downloadUrl" action="downloadProcessed">
                                                <s:param name="batchDir" value="batchDirectory" />
                                                <s:param name="fileName" value="outputFileName" />
                                            </s:url>
                                            <s:a href="%{downloadUrl}" cssClass="btn btn-sm btn-success">
                                                Download
                                            </s:a>
                                        </s:if>
                                    </td>
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>
                    
                    <!-- Download summary link -->
                    <div class="mt-3">
                        <s:url var="summaryUrl" action="downloadProcessed">
                            <s:param name="batchDir" value="batchDirectory" />
                            <s:param name="fileName" value="'_processing_summary.txt'" />
                        </s:url>
                        <s:a href="%{summaryUrl}" cssClass="btn btn-info">
                            Download Complete Summary
                        </s:a>
                    </div>
                </s:if>
                <s:else>
                    <div class="alert alert-warning">No files were processed.</div>
                </s:else>
            </div>
        </div>
        
        <!-- Back to upload form button -->
        <div class="text-center mb-4">
            <s:a action="index" cssClass="btn btn-primary">Process More Files</s:a>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>