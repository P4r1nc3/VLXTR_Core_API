package com.allegroservice.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleDriveService {

    private final Drive googleDrive;


    public GoogleDriveService(Drive googleDrive) {
        this.googleDrive = googleDrive;
    }

    public List<File> fetchGoogleDriveFiles() {
        try {
            FileList result = googleDrive.files().list().execute();
            return result.getFiles();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching files from Google Drive: " + e.getMessage(), e);
        }
    }
}
