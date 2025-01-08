package com.allegroservice.controller;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GoogleDriveController {

    @Value("${google.folder-id}")
    private String folderId;

    private final Drive googleDrive;


    public GoogleDriveController(Drive googleDrive) {
        this.googleDrive = googleDrive;
    }

    @GetMapping("/folder")
    public List<String> listFolderContents() {
        try {
            FileList result = googleDrive.files().list()
                    .setQ("'" + folderId + "' in parents and trashed = false")
                    .setFields("files(id, name, mimeType)")
                    .execute();

            List<File> files = result.getFiles();

            if (files == null || files.isEmpty()) {
                return List.of("Folder jest pusty.");
            } else {
                return files.stream()
                        .map(file -> "Name: " + file.getName() + ", ID: " + file.getId() + ", Type: " + file.getMimeType())
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas pobierania zawartości folderu: " + e.getMessage(), e);
        }
    }
}
