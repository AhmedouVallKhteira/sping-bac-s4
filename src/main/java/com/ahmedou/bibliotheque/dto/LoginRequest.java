package com.ahmedou.bibliotheque.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Email invalide")
    @NotBlank(message = "Email est requis")
    private String email;

    @NotBlank(message = "Mot de passe est requis")
    private String motDePasse;
}
