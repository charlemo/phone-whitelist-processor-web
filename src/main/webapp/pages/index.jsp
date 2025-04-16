<!-- src/main/webapp/pages/index.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Security-Policy" content="default-src 'self'; script-src 'self' https://code.jquery.com https://cdn.jsdelivr.net https://stackpath.bootstrapcdn.com; style-src 'self' https://stackpath.bootstrapcdn.com;">
    <title>Phone Whitelist Processor</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .container { margin-top: 30px; }
        .header { margin-bottom: 30px; }
        .upload-form { background-color: #f8f9fa; padding: 20px; border-radius: 5px; }
        .btn-file-upload { position: relative; overflow: hidden; }
        .btn-file-upload input[type=file] { position: absolute; top: 0; right: 0; min-width: 100%; min-height: 100%; opacity: 0; cursor: pointer; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header text-center">
            <h1>Phone Whitelist Processor</h1>
            <p class="lead">Upload and process phone number whitelist files</p>
        </div>
        
        <!-- Display errors if any -->
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger">
                <s:actionerror/>
            </div>
        </s:if>
        
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h4>Upload Files</h4>
            </div>
            <div class="card-body">
                <s:form action="uploadWhitelist" method="post" enctype="multipart/form-data" theme="simple" cssClass="upload-form">
                    <div class="form-group">
                        <label for="batchName">Batch Name (optional):</label>
                        <s:textfield name="batchName" cssClass="form-control" placeholder="Enter a name for this batch of files"/>
                    </div>
                    
                    <div class="form-group">
                        <label for="fileUpload">Select Whitelist Files:</label>
                        <div class="input-group">
                            <div class="custom-file">
                                <s:file name="whitelistFiles" multiple="true" cssClass="custom-file-input" id="fileUpload"/>
                                <label class="custom-file-label" for="fileUpload">Choose files...</label>
                            </div>
                        </div>
                        <small class="form-text text-muted">
                            Only .txt files up to 1MB are supported. You can select multiple files.
                        </small>
                    </div>
                    
                    <div class="form-group">
                        <s:submit value="Upload and Process" cssClass="btn btn-primary"/>
                    </div>
                </s:form>
            </div>
        </div>
        
        <div class="card mt-4">
            <div class="card-header bg-info text-white">
                <h4>Instructions</h4>
            </div>
            <div class="card-body">
                <h5>Expected File Format:</h5>
                <p>The processor expects text files with one phone number per line. Valid Philippine mobile numbers should:</p>
                <ul>
                    <li>Start with 639 (international format) or 09 (local format)</li>
                    <li>Have exactly 12 digits when in international format (639XXXXXXXXX)</li>
                    <li>Or 11 digits when in local format (09XXXXXXXXX)</li>
                </ul>
                
                <h5>Processing Features:</h5>
                <ul>
                    <li>Validates each phone number according to PH mobile format standards</li>
                    <li>Converts local format (09XX) to international format (639XX)</li>
                    <li>Identifies and removes duplicates</li>
                    <li>Provides detailed error reasons for invalid numbers</li>
                    <li>Generates downloadable processed files and summary reports</li>
                </ul>
            </div>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        // Update file input label with selected file names
        $('.custom-file-input').on('change', function() {
            var fileNames = Array.from(this.files).map(file => file.name).join(', ');
            $(this).next('.custom-file-label').html(fileNames || 'Choose files...');
        });
    </script>
</body>
</html>