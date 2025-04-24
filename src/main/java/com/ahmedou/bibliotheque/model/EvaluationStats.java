package com.ahmedou.bibliotheque.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EvaluationStats {
    private int totalEvaluations;
    private double pourcentage1;
    private double pourcentage2;
    private double pourcentage3;
    private double pourcentage4;
    private double pourcentage5;
}
