package com.ahmedou.bibliotheque.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddLivreRequest {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    @NotBlank(message = "La description est obligatoire")
    private String description;
    
    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private double prix;
    
    @NotBlank(message = "L'ISBN est obligatoire")
    @Pattern(regexp = "^\\d{13}$", message = "L'ISBN doit contenir exactement 13 chiffres")
    private String isbn;

    @NotNull(message = "La date de publication est obligatoire")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate datePublication;

    @NotNull(message = "L'image est obligatoire")
    private MultipartFile image;

    @NotNull(message = "L'auteur est obligatoire")
    private Long auteurId;
}
