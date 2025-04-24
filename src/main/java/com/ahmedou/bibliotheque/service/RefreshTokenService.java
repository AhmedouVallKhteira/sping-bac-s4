package com.ahmedou.bibliotheque.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ahmedou.bibliotheque.model.RefreshToken;
import com.ahmedou.bibliotheque.model.Utilisateur;
import com.ahmedou.bibliotheque.repository.RefreshTokenRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
    @Transactional
    public RefreshToken createRefreshToken(Utilisateur utilisateur) {
        refreshTokenRepository.deleteByUtilisateur(utilisateur);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiration(LocalDateTime.now().plusDays(7));
        refreshToken.setUtilisateur(utilisateur);

        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isValid(RefreshToken token) {
        return token.getExpiration().isAfter(LocalDateTime.now());
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token invalide"));
    }
    
    @Transactional
    public void deleteByUtilisateur(Utilisateur user) {
        refreshTokenRepository.deleteByUtilisateur(user);
    }
}
