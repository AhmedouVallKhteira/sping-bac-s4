package com.ahmedou.bibliotheque.dto;

import java.time.LocalDate;

import com.ahmedou.bibliotheque.model.Livre;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LivreSimpleDTO {
    private Long id;
    private String titre;
    private String description;
    private double prix;
    private String imageUrl;
    private LocalDate datePublication;
    private float evaluation;
    private AuteurSimpleDTO auteur;

    public LivreSimpleDTO(Livre livre) {
        this.id = livre.getId();
        this.titre = livre.getTitre();
        this.description = livre.getDescription();
        this.prix = livre.getPrix();
        this.imageUrl = livre.getImageUrl();
        this.datePublication = livre.getDatePublication();
        this.evaluation = livre.getEvaluation();
        this.auteur = new AuteurSimpleDTO(livre.getAuteur());
    }

}
