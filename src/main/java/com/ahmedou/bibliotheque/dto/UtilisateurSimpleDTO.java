package com.ahmedou.bibliotheque.dto;

import com.ahmedou.bibliotheque.model.Utilisateur;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurSimpleDTO {
    private Long id;
    private String nom;
    private String email;
    private String role;

    public UtilisateurSimpleDTO(Utilisateur utilisateur) {
    this.id = utilisateur.getId();
    this.nom = utilisateur.getNom();
    this.email = utilisateur.getEmail();
}

}
