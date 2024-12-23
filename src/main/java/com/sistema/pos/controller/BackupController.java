package com.sistema.pos.controller;

import com.sistema.pos.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadBackup() {
        try {
            File backupFile = backupService.createBackup();
            InputStreamResource resource = new InputStreamResource(new FileInputStream(backupFile));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + backupFile.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(backupFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadBackup(@RequestParam("file") MultipartFile file) {
        try {
            backupService.restoreBackup(file);
            return ResponseEntity.ok("Backup restaurado exitosamente.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al restaurar el backup: " + e.getMessage());
        }
    }
}
