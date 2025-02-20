package com.vlxtrcore.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.vlxtrcore.exception.VlxtrApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleDriveService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleDriveService.class);

    private final Drive googleDrive;

    public GoogleDriveService(Drive googleDrive) {
        this.googleDrive = googleDrive;
    }

    public List<File> fetchGoogleDriveFiles() {
        logger.info("Fetching files from Google Drive...");

        try {
            FileList result = googleDrive.files().list().execute();
            List<File> files = result.getFiles();

            if (files == null || files.isEmpty()) {
                logger.warn("No files found in Google Drive.");
                return List.of();
            }

            logger.info("Successfully fetched {} files from Google Drive.", files.size());
            return files;
        } catch (Exception e) {
            logger.error("Error fetching files from Google Drive: {}", e.getMessage(), e);
            throw new VlxtrApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Google Drive Fetch Error",
                    e.getMessage(),
                    "Verify Google Drive API credentials and permissions.");
        }
    }
}
