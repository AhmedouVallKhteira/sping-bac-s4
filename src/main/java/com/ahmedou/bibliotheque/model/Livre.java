package com.ahmedou.bibliotheque.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ahmedou.bibliotheque.service.FileStorageService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String genre;
    private double prix;
    private String isbn;
    private LocalDate datePublication;
    private boolean disponible;

    private Double remiseFixe;
    private boolean appliquerRemiseAutomatique;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Achat> achats;
    
    public Livre(String titre, String description, double prix, String isbn,LocalDate datePublication ,Auteur auteur) {
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.isbn = isbn;
        this.datePublication = datePublication;
        this.disponible = true;
        this.remiseFixe = 0.0;
        this.appliquerRemiseAutomatique = false;
        this.auteur = auteur;
    }
    @ManyToOne
    @JsonIgnore
    private Auteur auteur;


    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL)
    private List<EvaluationLivre> evaluations;

    public String getImageUrl() {
        return FileStorageService.getFileUrl("livres", id.toString());
    }

    public float getEvaluation() {
        if (evaluations == null || evaluations.isEmpty()) {
            return 0.0f;
        }
        float total = 0;
        for (EvaluationLivre evaluation : evaluations) {
            total += evaluation.getNote();
        }
        float average = total / evaluations.size();
        return Math.round(Math.min(5, Math.max(0, average)) * 10) / 10.0f;
    }

    public Auteur getAuteur() {
        return auteur;
    }
    
    public List<Map<String, Object>> getEvaluations() {
        if (evaluations == null) return new ArrayList<>();
        
        return evaluations.stream()
            .map(eval -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", eval.getId());
                map.put("note", eval.getNote());
                map.put("commentaire", eval.getCommentaire());
                map.put("utilisateurID", eval.getUtilisateur().getId());
                return map;
            })
            .collect(Collectors.toList());
    }

    public List<Livre> getLivresSimillers(List<Livre> livres) {
        return livres.stream()
                .filter(l -> !l.getId().equals(this.id))
                .filter(l -> l.getGenre().equals(this.genre))
                .sorted((l1, l2) -> Double.compare(
                        scoreSimilarite(l2), scoreSimilarite(l1)
                ))
                .limit(5)
                .collect(Collectors.toList());
    }

    private double scoreSimilarite(Livre other) {
        double score = 0.0;

        if (other.getGenre().equals(this.genre)) score += 0.4;
        if (other.getAuteur().equals(this.auteur)) score += 0.2;

        double diffPrix = Math.abs(other.getPrix() - this.prix) / this.prix;
        if (diffPrix <= 0.2) score += 0.2;

        long diffYears = Math.abs(
                ChronoUnit.YEARS.between(this.datePublication, other.getDatePublication())
        );
        if (diffYears <= 2) score += 0.1;

        if (other.getEvaluation() >= 4.0) score += 0.1;

        return score;
    }


    public EvaluationStats getStatistiqueEvaluation() {
        if (evaluations == null || evaluations.isEmpty()) {
            return new EvaluationStats(0, 0, 0, 0, 0, 0);
        }

        int total = evaluations.size();
        int[] count = new int[6];

        for (EvaluationLivre eval : evaluations) {
            int note = Math.round(eval.getNote());
            if (note >= 1 && note <= 5) {
                count[note]++;
            }
        }

        return new EvaluationStats(
            total,
            (count[1] * 100.0) / total,
            (count[2] * 100.0) / total,
            (count[3] * 100.0) / total,
            (count[4] * 100.0) / total,
            (count[5] * 100.0) / total
        );
    }
}
