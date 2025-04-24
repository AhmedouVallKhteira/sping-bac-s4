package com.ahmedou.bibliotheque.dto;

import com.ahmedou.bibliotheque.model.Role;
import com.ahmedou.bibliotheque.security.jwt.JwtService;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";

    private Long id;
    private String nom;
    private String email;
    private Role role;

    public LoginResponse(String accessToken, String refreshToken, JwtService jwtService) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

        this.email = jwtService.extractEmail(accessToken);
        this.nom = jwtService.extractNom(accessToken);
        this.id = jwtService.extractId(accessToken);
        this.role = Role.valueOf(jwtService.extractRole(accessToken));
    }
}
