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
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String biographie;
    private String nationalite;
    private LocalDate dateNaissance;

    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Livre> livres;
    
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
    private List<EvaluationAuteur> evaluations;

    public Auteur(String nom, String biographie, String nationalite, LocalDate dateNaissance) {
        this.nom = nom;
        this.biographie = biographie;
        this.nationalite = nationalite;
        this.dateNaissance = dateNaissance;
    }

    public String getImageUrl(){
        return FileStorageService.getFileUrl("auteurs", id.toString());
    }

    public float getEvaluation(){
        if (evaluations == null || evaluations.isEmpty()) {
            return 0.0f;
        }
        float somme = 0;
        for (EvaluationAuteur evaluation : evaluations) {
            somme += evaluation.getNote();
        }
        float moyenne = somme / evaluations.size();
        return Math.min(Math.max(moyenne, 0.0f), 5.0f); 
    }
    
    public EvaluationStats getStatistiqueEvaluation() {
    if (evaluations == null || evaluations.isEmpty()) {
        return new EvaluationStats(0, 0, 0, 0, 0, 0);
    }

    int total = evaluations.size();
    int[] count = new int[6];

    for (EvaluationAuteur eval : evaluations) {
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

    public List<Auteur> getAuteursSimillers(List<Auteur> allAuteurs) {
        return allAuteurs.stream()
                .filter(a -> !a.getId().equals(this.id))
                .sorted((a1, a2) -> Double.compare(
                        scoreSimilarite(a2), scoreSimilarite(a1)
                ))
                .limit(5)
                .collect(Collectors.toList());
    }

    private double scoreSimilarite(Auteur other) {
        double score = 0.0;

        if (this.nationalite.equalsIgnoreCase(other.nationalite)) score += 0.3;

        int diffLivres = Math.abs(
                (other.getLivres() != null ? other.getLivres().size() : 0) -
                        (this.getLivres() != null ? this.getLivres().size() : 0)
        );
        if (diffLivres <= 2) score += 0.2;

        boolean genreCommun = this.getLivres().stream()
                .map(Livre::getGenre)
                .anyMatch(genre ->
                        other.getLivres().stream()
                                .map(Livre::getGenre)
                                .anyMatch(g -> g.equalsIgnoreCase(genre))
                );
        if (genreCommun) score += 0.2;

        long diffAge = Math.abs(ChronoUnit.YEARS.between(this.dateNaissance, other.dateNaissance));
        if (diffAge <= 10) score += 0.2;

        float diffEval = Math.abs(this.getEvaluation() - other.getEvaluation());
        if (diffEval <= 1.0) score += 0.1;

        return score;
    }
}
