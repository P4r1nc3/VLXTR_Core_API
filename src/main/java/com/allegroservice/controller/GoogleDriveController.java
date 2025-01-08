package com.allegroservice.controller;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/google")
public class GoogleDriveController {

    @Value("${google.folder-id}")
    private String folderId;

    private final Drive googleDrive;


    public GoogleDriveController(Drive googleDrive) {
        this.googleDrive = googleDrive;
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> listFolderContents() throws IOException {
        FileList result = googleDrive.files().list().execute();
        List<File> files = result.getFiles();
        return ResponseEntity.ok(files);
    }
}
