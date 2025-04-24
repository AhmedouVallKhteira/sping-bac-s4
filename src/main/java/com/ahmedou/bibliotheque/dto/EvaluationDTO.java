package com.ahmedou.bibliotheque.dto;

import com.ahmedou.bibliotheque.model.EvaluationAuteur;
import com.ahmedou.bibliotheque.model.EvaluationLivre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTO {
    private Long id;
    private int note;
    private String commentaire;
    private Long utilisateurId;
    private Long livreId;
    private Long auteurId;

    public EvaluationDTO(EvaluationLivre evaluationLivre) {
        this.id = evaluationLivre.getId();
        this.note = evaluationLivre.getNote();
        this.commentaire = evaluationLivre.getCommentaire();
        this.utilisateurId = evaluationLivre.getUtilisateur().getId();
        this.livreId = evaluationLivre.getLivre().getId();
    }

    public EvaluationDTO(EvaluationAuteur evaluationAuteur) {
        this.id = evaluationAuteur.getId();
        this.note = evaluationAuteur.getNote();
        this.commentaire = evaluationAuteur.getCommentaire();
        this.utilisateurId = evaluationAuteur.getUtilisateur().getId();
        this.auteurId = evaluationAuteur.getAuteur().getId();
    }

}