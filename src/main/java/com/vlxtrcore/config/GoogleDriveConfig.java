package com.vlxtrcore.config;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleDriveConfig {

    @Value("${google.credentials-file-name}")
    private String credentialsFileName;

    @Bean
    public Drive googleDrive() throws IOException {
        InputStream credentialsStream = getClass().getClassLoader()
                .getResourceAsStream(credentialsFileName);

        if (credentialsStream == null) {
            throw new IOException("Could not find the credentials file in resources.");
        }

        // Load credentials from the input stream
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(credentialsStream)
                .createScoped(DriveScopes.DRIVE);

        return new Drive.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                new com.google.api.client.json.jackson2.JacksonFactory(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Allegro Service").build();
    }
}
