package com.vlxtrcore.config;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.vlxtrcore.exception.VlxtrApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleDriveConfig {

    private static final Logger logger = LoggerFactory.getLogger(GoogleDriveConfig.class);

    @Value("${google.credentials-file-name}")
    private String credentialsFileName;

    @Bean
    public Drive googleDrive() {

        try {
            InputStream credentialsStream = getClass().getClassLoader()
                    .getResourceAsStream(credentialsFileName);

            if (credentialsStream == null) {
                logger.error("Google Drive credentials file '{}' not found in resources.", credentialsFileName);
                throw new VlxtrApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Google Drive Configuration Error",
                        "Could not find the credentials file in resources.",
                        "Ensure the credentials file is placed correctly in the resources folder.");
            }

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(credentialsStream)
                    .createScoped(DriveScopes.DRIVE);

            Drive drive = new Drive.Builder(
                    new com.google.api.client.http.javanet.NetHttpTransport(),
                    new com.google.api.client.json.jackson2.JacksonFactory(),
                    new HttpCredentialsAdapter(credentials)
            ).setApplicationName("Allegro Service").build();

            return drive;
        } catch (IOException e) {
            logger.error("Failed to initialize Google Drive API client: {}", e.getMessage(), e);
            throw new VlxtrApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Google Drive Initialization Failed",
                    e.getMessage(),
                    "Check the credentials file and permissions.");
        }
    }
}
