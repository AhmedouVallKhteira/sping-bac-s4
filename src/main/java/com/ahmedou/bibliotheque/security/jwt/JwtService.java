package com.ahmedou.bibliotheque.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.ahmedou.bibliotheque.model.Utilisateur;
import com.ahmedou.bibliotheque.repository.UtilisateurRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "ahmedou_valle_23010_supnum_project_secure_jwt_key".getBytes(StandardCharsets.UTF_8)
    );

    private final UtilisateurRepository utilisateurRepository;

    public JwtService(UtilisateurRepository utilisateurRepository) {
    this.utilisateurRepository = utilisateurRepository;
}



    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; 
    private final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; 

    private Key getSignKey() {
        return SECRET_KEY;
    }

    public String generateToken(String email, boolean isRefresh) {
    Utilisateur user = utilisateurRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

    long expiration = isRefresh ? REFRESH_TOKEN_EXPIRATION : ACCESS_TOKEN_EXPIRATION;

    Map<String, Object> claims = new HashMap<>();
    
    claims.put("id", user.getId());
    claims.put("nom", user.getNom());
    claims.put("email", user.getEmail());
    claims.put("role", user.getRole().name());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
}

    public String extractNom(String token) {
        return extractClaim(token, claims -> claims.get("nom", String.class));
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractId(String token) {
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

}
