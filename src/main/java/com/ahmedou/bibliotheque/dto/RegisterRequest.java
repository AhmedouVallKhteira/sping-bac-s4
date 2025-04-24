package com.ahmedou.bibliotheque.dto;

import com.ahmedou.bibliotheque.validation.annotations.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "nom d'utilisateur est requis")
    private String nom;

    @NotBlank(message = "email est requis")
    @Email(message = "email invalide")
    @UniqueEmail
    private String email;

    @NotBlank(message = "mot de passe est requis")
    @Size(min = 4, max = 20, message = "le mot de passe doit contenir entre 4 et 20 caractères")
    private String motDePasse;
}
