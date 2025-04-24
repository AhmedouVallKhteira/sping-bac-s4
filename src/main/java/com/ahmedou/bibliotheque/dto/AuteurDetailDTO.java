package com.ahmedou.bibliotheque.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.ahmedou.bibliotheque.model.Auteur;
import com.ahmedou.bibliotheque.model.EvaluationAuteur;
import com.ahmedou.bibliotheque.model.EvaluationStats;
import com.ahmedou.bibliotheque.model.Livre;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuteurDetailDTO {
    private Long id;
    private String nom;
    private String biographie;
    private String nationalite;
    private LocalDate dateNaissance;
    private String imageUrl;
    private float evaluation;
    private List<Livre> livres;
    private List<Map<String, Object>> evaluations;
    private EvaluationStats statistiqueEvaluation;
    private List<AuteurSimpleDTO> auteursSimillers;


    public AuteurDetailDTO(Auteur auteur , List<AuteurSimpleDTO> auteursSimillers) {
        this.id = auteur.getId();
        this.nom = auteur.getNom();
        this.biographie = auteur.getBiographie();
        this.nationalite = auteur.getNationalite();
        this.dateNaissance = auteur.getDateNaissance();
        this.imageUrl = auteur.getImageUrl();
        this.evaluation = auteur.getEvaluation();
        this.livres = auteur.getLivres();
        this.evaluations = auteur.getEvaluations();
        this.statistiqueEvaluation = auteur.getStatistiqueEvaluation();
        this.auteursSimillers = auteursSimillers;
    }
}
