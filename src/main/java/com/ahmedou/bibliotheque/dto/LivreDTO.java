package com.ahmedou.bibliotheque.dto;

import com.ahmedou.bibliotheque.model.Livre;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LivreDTO {
    private Long id;
    private String titre;
    private String description;
    private double prix;
    private String imageUrl;
    private LocalDate datePublication;
    private float evaluation;

    public LivreDTO(Livre livre) {
        this.id = livre.getId() ;
        this.titre = livre.getTitre();
        this.description = livre.getDescription();
        this.prix = livre.getPrix();
        this.imageUrl = livre.getImageUrl();
        this.datePublication = livre.getDatePublication();
        this.evaluation = livre.getEvaluation();
    }
}
