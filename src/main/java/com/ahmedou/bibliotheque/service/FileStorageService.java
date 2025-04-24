package com.ahmedou.bibliotheque.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String createFile(String type, String id, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Le nom de fichier original est nul");
        }

        String extension = StringUtils.getFilenameExtension(originalFilename);
        String filename = type + "-" + id + (extension != null ? "." + extension : "");
        
        Path uploadPath = Paths.get(uploadDir, type);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    public String updateFile(String type, String id, MultipartFile file) throws IOException {
        deleteFile(type, id);
        return createFile(type, id, file);
    }

    public boolean deleteFile(String type, String id) throws IOException {
        Path uploadPath = Paths.get(uploadDir, type);
        if (!Files.exists(uploadPath)) return false;

        try (Stream<Path> files = Files.list(uploadPath)) {
            Optional<Path> fileToDelete = files
                .filter(path -> path.getFileName().toString().startsWith(type + "-" + id))
                .findFirst();

            if (fileToDelete.isPresent()) {
                Files.delete(fileToDelete.get());
                return true;
            }
        }
        return false;
    }

    public static String getFileUrl(String type, String id) {
        String uploadDir = "uploads";
        Path uploadPath = Paths.get(uploadDir, type);
        try (Stream<Path> files = Files.list(uploadPath)) {
            Optional<String> filename = files
                .filter(path -> path.getFileName().toString().startsWith(type + "-" + id))
                .map(path -> path.getFileName().toString())
                .findFirst();

            if (filename.isPresent()) {
                return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/uploads/")
                    .path(type + "/")
                    .path(filename.get())
                    .toUriString();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la recherche du fichier pour l'id: " + id, e);
        }

        // retourne une image par défaut si aucune image trouvée
        return ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/uploads/")
            .path(type + "/default.png")
            .toUriString();
    }
}
