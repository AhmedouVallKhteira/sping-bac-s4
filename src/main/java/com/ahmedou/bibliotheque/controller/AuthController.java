package com.ahmedou.bibliotheque.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.ahmedou.bibliotheque.model.EmailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahmedou.bibliotheque.dto.ActivateAccountRequest;
import com.ahmedou.bibliotheque.dto.CheckAuthRequest;
import com.ahmedou.bibliotheque.dto.ConfirmOtpAndNewPasswordRequest;
import com.ahmedou.bibliotheque.dto.LoginRequest;
import com.ahmedou.bibliotheque.dto.LoginResponse;
import com.ahmedou.bibliotheque.dto.RegisterRequest;
import com.ahmedou.bibliotheque.dto.ResetPasswordRequest;
import com.ahmedou.bibliotheque.dto.UtilisateurDTO;
import com.ahmedou.bibliotheque.model.Utilisateur;
import com.ahmedou.bibliotheque.security.jwt.JwtService;
import com.ahmedou.bibliotheque.service.AuthService;
import com.ahmedou.bibliotheque.service.OtpService;
import com.ahmedou.bibliotheque.service.RefreshTokenService;
import com.ahmedou.bibliotheque.utils.EmailSender;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;
    private final JwtService jwtService;


    public AuthController(AuthService authService, OtpService otpService,RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<UtilisateurDTO> register(@Valid @RequestBody RegisterRequest request) {
        String email = request.getEmail();
        String nom = request.getNom();
        String motDePasse = request.getMotDePasse();

        Utilisateur user = authService.register(nom, email, motDePasse);

        int otp = otpService.generateOtp(email, "register");
        String subject = "Confirmation d'inscription";
        String body = "Votre code de confirmation est : " + otp;
        boolean sender = EmailSender.sendEmail(email, subject, body);

        if (!sender) {
            return ResponseEntity.badRequest().body(null);
        }

        UtilisateurDTO dto = new UtilisateurDTO(user.getId(), user.getNom(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/activate-account")
    public ResponseEntity<Map<String, String>> activateAccount(@Valid @RequestBody ActivateAccountRequest request) {
        String email = request.getEmail();
        int otp = request.getOtp();
        Map<String, String> response = new HashMap<>();
        response.put("email", email);

        boolean isValid = otpService.verifyOtp(email, "register", otp);

        if (isValid) {
            authService.activateAccount(email);
            response.put("message", "Compte activé avec succès");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Le code de confirmation est incorrect");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/check-auth")
    public ResponseEntity<LoginResponse> checkAuth(@RequestBody CheckAuthRequest request) {
        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();
        boolean isAccessTokenValid = jwtService.isTokenValid(accessToken);
        if (isAccessTokenValid) {
            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, jwtService));
        }
        try {
            System.out.println("Access token is invalid, checking refresh token");
            LoginResponse newTokens = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(newTokens);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
        
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody EmailDTO emailDTO) {
        Map<String, String> response = new HashMap<>();
        String email = emailDTO.getEmail();
        System.out.println(email);
        authService.logout(email);
        response.put("message", "Déconnexion réussie.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String email = request.getEmail();
        
        Optional<Utilisateur> user = authService.findByEmail(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Si ce compte existe, un code vous a été envoyé par email.");
        response.put("email", email);
        if (user.isEmpty() || !user.get().isActive()) {
            return ResponseEntity.ok(response);
        }
        
        int otp = otpService.generateOtp(email, "reset");
        @SuppressWarnings("unused")
        boolean sent = EmailSender.sendEmail(email, "Réinitialisation du mot de passe",
                "Votre code de réinitialisation est : " + otp);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ConfirmOtpAndNewPasswordRequest request) {
        Map<String, String> response = new HashMap<>();
        boolean valid = otpService.verifyOtp(request.getEmail(), "reset", request.getOtp());
        response.put("email", request.getEmail());
        if (!valid) {
            response.put("message", "Code OTP invalide ou expiré");
            return ResponseEntity.badRequest().body(response);
        }
    
        authService.updatePassword(request.getEmail(), request.getNewPassword());
        response.put("message", "Mot de passe réinitialisé avec succès");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resent-otp")
    public ResponseEntity<Map<String, String>> resentOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String type = request.get("type");
        System.out.println("Email: " + email);
        System.out.println("Type: " + type);

        Map<String, String> response = new HashMap<>();
        if (email == null || email.isEmpty()) {
            response.put("message", "Email is required");
            return ResponseEntity.badRequest().body(response);
        }
        if (type == null || type.isEmpty()) {
            response.put("message", "Type is required");
            return ResponseEntity.badRequest().body(response);
        }
        int otp = otpService.generateOtp(email,type);
        @SuppressWarnings("unused")
        boolean sent = EmailSender.sendEmail(email, "Réinitialisation du mot de passe",
                "Votre code de réinitialisation est : " + otp);
        response.put("message", "Code OTP renvoyé avec succès");
        return ResponseEntity.ok(response);
    }


}
