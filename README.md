Here's an updated README.md that includes comprehensive sections on building, running locally, and development:

# Phone Whitelist Processor Web Application

A web application for validating and processing phone number whitelist files, specifically designed for Philippine mobile numbers.

## Features

- Web-based interface for uploading whitelist files
- Multiple file upload support
- Batch processing with custom batch naming
- Detailed processing reports
- Individual and batch download options

## User Flow

1. **Navigate to Home Page**
   - User accesses the application's home page.

2. **Enter Batch Name (Optional)**
   - User provides an optional batch name for the processing session.

3. **Upload Files**
   - User selects one or multiple `.txt` files containing phone numbers.
   - User clicks the "Upload and Process" button.

4. **File Validation**
   - System validates each file:
     1. Checks file extension (`.txt` only).
     2. Verifies file size (must be under 1MB).

5. **File Processing**
   - System processes each valid file:
     1. Validates each phone number according to requirements.
     2. Converts `09xxx` format to `639xxx` format.
     3. Removes whitespaces and special characters.
     4. Filters out invalid or duplicate numbers.

6. **Output Generation**
   - System generates:
     1. A cleaned file for each input file containing valid numbers.
     2. A detailed report with processing statistics.

7. **View Results**
   - User views the processing summary on the results page.

8. **Download Options**
   - User can download:
     1. Individual processed files.
     2. A complete summary report.

9. **Repeat Process**
   - User can choose to process more files.

## Technical Requirements

- Java 17+
- Servlet container (GlassFish v3 or compatible)
- Maven 3.8+ (for building)

## Phone Number Validation Rules

The application validates Philippine mobile numbers according to these rules:
- Valid numbers must start with "639" or be convertible from "09xxx" format
- Numbers must be exactly 12 digits long after formatting
- Whitespaces and special characters are automatically removed
- Alphanumeric or alphabetic values are rejected
- Duplicate numbers are identified and filtered

## File Requirements

- File must be in .txt format
- Maximum file size is 1MB
- Each line should contain a single phone number

## Development Environment Setup

### Prerequisites Installation

1. **Install JDK 17**
   - Download JDK 17 from Oracle or use OpenJDK
   - Set JAVA_HOME environment variable
   - Add %JAVA_HOME%\bin to PATH

2. **Install Maven**
   - Download Maven from https://maven.apache.org/download.cgi
   - Set MAVEN_HOME environment variable
   - Add %MAVEN_HOME%\bin to PATH

3. **Install VSCode**
   - Download from https://code.visualstudio.com/
   - Install the following extensions:
     - Extension Pack for Java
     - Maven for Java
     - XML
     - Tomcat for Java

### Creating a New Project in VSCode

1. Open Command Palette (Ctrl+Shift+P)
2. Select "Maven: Create Maven Project"
3. Choose "maven-archetype-webapp"
4. Enter Group Id: `com.smartibf`
5. Enter Artifact Id: `phone-whitelist-processor-web`

### Project Structure Setup

Create the following directory structure:

```bash
# Main Java source directories
mkdir -p src/main/java/com/smartibf/actions
mkdir -p src/main/java/com/smartibf/utils

# Main resources directory
mkdir -p src/main/resources

# Test directories
mkdir -p src/test/java/com/smartibf/utils
mkdir -p src/test/resources

# Web application directories
mkdir -p src/main/webapp/WEB-INF/classes
mkdir -p src/main/webapp/pages
mkdir -p src/main/webapp/css
mkdir -p src/main/webapp/js
```

## Building

### Building with Maven

```bash
# Clean and build the project
mvn clean package

# Skip tests while building
mvn clean package -DskipTests
```

### Building in VSCode

1. Open the project in VSCode
2. Press Ctrl+Shift+P and type "Maven: Execute Commands..."
3. Select "clean package"

### Output

The build process generates the following artifacts:

- WAR file: `target/phone-whitelist-processor.war`
- Compiled classes: `target/classes/`
- Test results: `target/surefire-reports/`

## Running Locally

### Using Maven Tomcat Plugin

The project includes the Tomcat Maven plugin for easy local deployment:

```bash
# Start embedded Tomcat server
mvn tomcat7:run

# Access the application at:
# http://localhost:8080/phone-whitelist
```

### Using Standalone Tomcat

1. Install Tomcat 9+ (compatible with Servlet 4.0)
2. Copy `target/phone-whitelist-processor.war` to Tomcat's `webapps` directory
3. Start Tomcat server
4. Access the application at:
   - `http://localhost:8080/phone-whitelist-processor/`

### Using VSCode Tomcat Extension

1. Click on the Tomcat icon in the sidebar
2. Right-click on your server and select "Add War"
3. Select the WAR file from your target directory
4. Right-click on the deployed app and select "Run"

## Deployment to GlassFish

### Prerequisites

- GlassFish v3 installed and running
- JDK 17 configured with GlassFish

### Deployment Steps

1. Start the GlassFish server:
   ```bash
   asadmin start-domain
   ```

2. Deploy the application:
   ```bash
   asadmin deploy target/phone-whitelist-processor.war
   ```

3. Access the application:
   - `http://localhost:8080/phone-whitelist-processor/`

### Undeployment

```bash
asadmin undeploy phone-whitelist-processor
```

## Development Guidelines

### Code Organization

- **Actions Package**: Contains Struts action classes
- **Utils Package**: Contains core business logic
- **JSP Pages**: Contains the presentation layer
- **WEB-INF**: Contains configuration files

### Making Changes

1. **Modify Java Files**:
   - Edit files in `src/main/java`
   - Use `mvn compile` to verify compilation

2. **Modify JSP Files**:
   - Edit files in `src/main/webapp/pages`
   - Changes are visible on refresh (when using embedded Tomcat)

3. **Modify Configurations**:
   - Edit `src/main/webapp/WEB-INF/web.xml` for servlet config
   - Edit `src/main/webapp/WEB-INF/classes/struts.xml` for Struts config
   - Restart the application after config changes

### Adding New Features

1. Create new action classes in `src/main/java/com/smartibf/actions`
2. Add new utility classes in `src/main/java/com/smartibf/utils`
3. Create new JSP pages in `src/main/webapp/pages`
4. Update `struts.xml` to map new actions

### Testing

Run tests with Maven:

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=ClassName
```

Create test cases in `src/test/java` following the same package structure as the main code.

## Troubleshooting

### Common Issues

1. **Compilation Errors**:
   - Check Java version compatibility
   - Verify dependencies in pom.xml

2. **Runtime Errors**:
   - Check Tomcat/GlassFish logs
   - Verify class paths and deployment descriptors

3. **File Upload Issues**:
   - Check file size limits in `struts.xml`
   - Verify multipart configuration

4. **File Processing Issues**:
   - Check output directory permissions
   - Verify file extension validation

### Logging

The application uses Log4j2 for logging:

- Configuration: `src/main/resources/log4j2.xml`
- Log files: `logs/app.log`

Adjust log levels for debugging:
- Change `<Root level="info">` to `<Root level="debug">` for more detailed logs

## Project Tree

```
phone-whitelist-processor-web
├─ pom.xml
├─ README.md
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ smartibf
   │  │        ├─ actions
   │  │        │  ├─ WhitelistDownloadAction.java
   │  │        │  └─ WhitelistUploadAction.java
   │  │        └─ utils
   │  │           ├─ FileProcessor.java
   │  │           └─ PhoneNumberValidator.java
   │  ├─ resources
   │  │  ├─ log4j2.xml
   │  │  └─ struts.xml
   │  └─ webapp
   │     ├─ index.jsp
   │     ├─ pages
   │     │  ├─ index.jsp
   │     │  ├─ process.jsp
   │     │  └─ result.jsp
   │     └─ WEB-INF
   │        └─ web.xml
   └─ test
      └─ java
         └─ com
            └─ smartibf
               ├─ actions
               ├─ resources
               │  ├─ invalid_whitelist.txt
               │  ├─ oversized_whitelist.txt
               │  └─ sample_whitelist.txt
               └─ utils
                  ├─ FileProcessorTest.java
                  └─ PhoneNumberValidatorTest.java

```

## Performance Considerations

### Memory Usage

- File size limit (1MB) prevents excessive memory usage
- Stream-based processing for large files
- Proper resource cleanup with try-with-resources

### Optimizations

- Cached validation results for duplicate detection
- Efficient data structures (HashSet for duplicate detection)
- Buffered I/O operations

## Security Considerations

- Input validation for all file uploads
- File size restrictions to prevent DoS attacks
- Path traversal protection in file download action
- Content type validation

## Maintenance

### Updating Dependencies

Check for dependency updates with:
```bash
mvn versions:display-dependency-updates
```

Update to newer versions in pom.xml as needed.

### Version History

#### v1.0.0 (Initial Release)
- Basic phone whitelist processing functionality
- Web interface for file upload and download
- Multiple file processing support
- Detailed reporting
