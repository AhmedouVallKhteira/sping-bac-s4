package com.ahmedou.bibliotheque.controller;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
    
    private final String uploadDir = "uploads"; 

    @GetMapping("/uploads/{type}/{filename}")
    public ResponseEntity<?> getFile(@PathVariable String type, @PathVariable String filename) {
        Path filePath = Paths.get(uploadDir, type, filename);
        Resource resource;

        try {
            if (Files.exists(filePath)) {
                resource = new UrlResource(filePath.toUri());
            } else {
                Path defaultImg = Paths.get(uploadDir, "default.png");
                resource = new UrlResource(defaultImg.toUri());
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().body("Une erreur s'est produite : " + e.getMessage());
        }
    }
}