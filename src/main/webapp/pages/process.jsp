<!-- src/main/webapp/pages/process.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Security-Policy" content="default-src 'self'; script-src 'self' https://code.jquery.com https://cdn.jsdelivr.net https://stackpath.bootstrapcdn.com 'unsafe-inline'; style-src 'self' https://stackpath.bootstrapcdn.com 'unsafe-inline'; img-src 'self';">
    <title>Processing Files</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .container { margin-top: 30px; }
        .header { margin-bottom: 30px; }
        .processing-indicator { margin: 50px 0; text-align: center; }
        .spinner-border { width: 3rem; height: 3rem; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header text-center">
            <h1>Processing Files</h1>
            <p class="lead">Please wait while your files are being processed...</p>
        </div>
        
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h4>Processing Status</h4>
            </div>
            <div class="card-body">
                <div class="processing-indicator">
                    <div class="spinner-border text-primary" role="status">
                        <span class="sr-only">Processing...</span>
                    </div>
                    <h4 class="mt-3">Processing your whitelist files</h4>
                    <p>This may take a few moments depending on the file size and number of records.</p>
                </div>
                
                <!-- Auto-redirect to results page after processing -->
                <script>
                    // Redirect to results page after a short delay
                    setTimeout(function() {
                        window.location.href = '<s:url action="processingComplete"/>';
                    }, 3000);
                </script>
            </div>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>