package com.ahmedou.bibliotheque.dto;

import lombok.Data;

@Data
public class CreateEvaluationRequest {
    private int note;
    private String commentaire;
    private Long utilisateurId;
    private Long cibleId;
}
