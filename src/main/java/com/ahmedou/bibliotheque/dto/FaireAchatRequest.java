package com.ahmedou.bibliotheque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaireAchatRequest {
    @NotNull(message = "L'ID du livre est obligatoire")
    private Long livreId;
    
    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;
    
    @NotNull(message = "Le montant est obligatoire")
    private double montant;
    
    @NotNull(message = "La banque est obligatoire")
    @NotBlank(message = "La banque ne peut pas être vide")
    private String banque;
    
    @NotNull(message = "La preuve de paiement est obligatoire")
    private MultipartFile preuve;
    
    @NotBlank(message = "Le message ne peut pas être vide")
    private String message;
}