package com.ahmedou.bibliotheque.dto;

import java.time.LocalDate;

import com.ahmedou.bibliotheque.model.Auteur;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuteurSimpleDTO {
    private Long id;
    private String nom;
    private String biographie;
    private String nationalite;
    private LocalDate dateNaissance;
    private String imageUrl;
    private float evaluation;

    public AuteurSimpleDTO(Auteur auteur) {
        this.id = auteur.getId();
        this.nom = auteur.getNom();
        this.biographie = auteur.getBiographie();
        this.nationalite = auteur.getNationalite();
        this.dateNaissance = auteur.getDateNaissance();
        this.imageUrl = auteur.getImageUrl();
        this.evaluation = auteur.getEvaluation();
    }

}
