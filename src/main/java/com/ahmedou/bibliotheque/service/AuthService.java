package com.ahmedou.bibliotheque.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ahmedou.bibliotheque.dto.LoginRequest;
import com.ahmedou.bibliotheque.dto.LoginResponse;
import com.ahmedou.bibliotheque.model.RefreshToken;
import com.ahmedou.bibliotheque.model.Role;
import com.ahmedou.bibliotheque.model.Utilisateur;
import com.ahmedou.bibliotheque.repository.UtilisateurRepository;
import com.ahmedou.bibliotheque.security.jwt.JwtService;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UtilisateurRepository utilisateurRepository,
                    PasswordEncoder passwordEncoder,
                    JwtService jwtService,
                    RefreshTokenService refreshTokenService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public Utilisateur register(String nom, String email, String motDePasse) {
        if (utilisateurRepository.existsByEmailAndIsActiveTrue(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        Optional<Utilisateur> optionalUser = utilisateurRepository.findByEmail(email);
        Utilisateur user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = new Utilisateur();
        }

        user.setNom(nom);
        user.setEmail(email);
        user.setMotDePasse(passwordEncoder.encode(motDePasse));
        user.setRole(Role.SUPERADMIN);
        user.setActive(false);

        return utilisateurRepository.save(user);
    }

    public void activateAccount(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActive(true);
        utilisateurRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("Compte non activé. Veuillez vérifier votre email.");
        }

        if (!passwordEncoder.matches(request.getMotDePasse(), user.getMotDePasse())) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }

        String accessToken = jwtService.generateToken(user.getEmail(), false);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken.getToken(), jwtService);
    }

    public LoginResponse refreshToken(String token) {
        RefreshToken storedToken = refreshTokenService.findByToken(token);

        if (!refreshTokenService.isValid(storedToken)) {
            throw new RuntimeException("Le refresh token a expiré.");
        }

        String newAccessToken = jwtService.generateToken(storedToken.getUtilisateur().getEmail(), false);
        return new LoginResponse(newAccessToken, token ,jwtService);
    }

    public void logout(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        refreshTokenService.deleteByUtilisateur(user);
    }

    public void updatePassword(String email, String newPassword) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        utilisateur.setMotDePasse(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(utilisateur);
    }

    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

}
