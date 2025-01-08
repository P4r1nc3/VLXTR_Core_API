package com.allegroservice.config;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive googleDrive() throws IOException {
        // Load the credentials file from the classpath
        InputStream credentialsStream = getClass().getClassLoader()
                .getResourceAsStream("allegro-service-447208-0fe7d9012130.json");

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
