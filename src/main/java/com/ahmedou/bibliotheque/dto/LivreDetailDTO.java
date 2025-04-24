package com.ahmedou.bibliotheque.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.ahmedou.bibliotheque.model.EvaluationLivre;
import com.ahmedou.bibliotheque.model.EvaluationStats;
import com.ahmedou.bibliotheque.model.Livre;

import com.ahmedou.bibliotheque.repository.LivreRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LivreDetailDTO {
    private Long id;
    private String titre;
    private String description;
    private String genre;
    private double prix;
    private String isbn;
    private LocalDate datePublication;
    private boolean disponible;
    private String imageUrl;
    private float evaluation;
    private AuteurSimpleDTO auteur;
    private List<Map<String, Object>> evaluations;
    private EvaluationStats statistiqueEvaluation;
    private List<LivreSimpleDTO> livresSimillers;

    public LivreDetailDTO(Livre livre ,List<LivreSimpleDTO> livreSimmillaire) {
        this.id = livre.getId();
        this.titre = livre.getTitre();
        this.description = livre.getDescription();
        this.genre = livre.getGenre();
        this.prix = livre.getPrix();
        this.isbn = livre.getIsbn();
        this.datePublication = livre.getDatePublication();
        this.disponible = livre.isDisponible();
        this.imageUrl = livre.getImageUrl();
        this.evaluation = livre.getEvaluation();
        this.auteur = new AuteurSimpleDTO(livre.getAuteur());
        this.evaluations = livre.getEvaluations();
        this.statistiqueEvaluation = livre.getStatistiqueEvaluation();
        this.livresSimillers = livreSimmillaire;
    }
}
