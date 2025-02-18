# VLXTR Core API

## Overview
VLXTR Core API serves as the central service integrating VLXTR Allegro API and VLXTR Bambu API. It provides a unified interface for managing products, orders, and 3D print jobs, ensuring smooth communication between different microservices.

## Features
- Centralized management of products and orders
- Integration with VLXTR Allegro API for marketplace interactions
- Integration with VLXTR Bambu API for handling 3D print job execution
- MySQL database for persistent data storage
- Google Drive integration for file storage and document management

## Prerequisites
- **Java 17**
- **Maven**
- **MySQL Database** configured and running
- **Google Cloud Service Account** for Google Drive integration

## Configuration

### **1. Setting Up the Database**
VLXTR Core API requires a MySQL database for data persistence. Configure the database connection in the environment variables:

```sh
export DATABASE_URL=jdbc:mysql://your-database-host:3306/vlxtr_core_db
export DATABASE_USERNAME=your_db_username
export DATABASE_PASSWORD=your_db_password
```

Or update `application.properties`:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### **2. Configuring Google Drive Integration**
VLXTR Core API supports Google Drive for file storage. The service requires credentials to authenticate with Google Cloud.

#### **2.1 Set Up Google Credentials**
Store your Google service account JSON file in the `resources` directory and configure its access:

```sh
export GOOGLE_CREDENTIALS_FILE_NAME=service-account.json
export GOOGLE_FOLDER_ID=your_google_drive_folder_id
```

Or specify them in `application.properties`:

```properties
google.credentials-file-name=${GOOGLE_CREDENTIALS_FILE_NAME}
google.folder-id=${GOOGLE_FOLDER_ID}
```

Ensure the Google credentials JSON file is located inside `src/main/resources/` and follows this structure:

```json
{
  "type": "service_account",
  "project_id": "your-google-project-id",
  "private_key_id": "your-private-key-id",
  "private_key": "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n",
  "client_email": "your-client-email",
  "client_id": "your-client-id",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/your-client-email"
}
```

### **3. Running the Service**
Clone the repository and navigate into the project directory:

```sh
git clone https://github.com/P4r1nc3/VLXTR_Core_API.git
cd VLXTR_Core_API
```

#### **3.1 Build the Project**
```sh
mvn clean install
```

#### **3.2 Start the Application**
```sh
mvn spring-boot:run
```

The application will start on the default port **8081**.

### **4. Deploying with Docker**
To deploy the service using Docker, build and start the container:

```sh
docker build -t vlxtr-core-api .
docker run -p 8081:8081 --name vlxtr-core-api vlxtr-core-api
```

## Debugging
Enable detailed logs by modifying `application.properties`:

```properties
logging.level.root=INFO
logging.level.com.vlxtrcore=DEBUG
```

## Conclusion
VLXTR Core API acts as the central hub integrating Allegro and Bambu services while providing efficient product, order, and 3D print job management. Ensure proper configuration of database and Google Cloud credentials before running the service.

