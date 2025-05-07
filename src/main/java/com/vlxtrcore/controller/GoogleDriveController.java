package com.vlxtrcore.controller;

import com.vlxtrcore.service.GoogleDriveService;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/google")
public class GoogleDriveController {

    @Value("${google.folder-id}")
    private String folderId;

    private final GoogleDriveService googleDriveService;


    public GoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> listFolderContents() {
        List<File> files = googleDriveService.fetchGoogleDriveFiles();
        return ResponseEntity.ok(files);
    }
}
