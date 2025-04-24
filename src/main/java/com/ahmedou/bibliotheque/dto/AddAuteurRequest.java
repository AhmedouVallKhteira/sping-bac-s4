package com.ahmedou.bibliotheque.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddAuteurRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "La biographie est obligatoire")
    private String biographie;

    @NotBlank(message = "La nationalité est obligatoire")
    private String nationalite;

    @NotNull(message = "La date de naissance est obligatoire")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateNaissance;

    @NotNull(message = "La photo est obligatoire")
    private MultipartFile photo;
    
}
