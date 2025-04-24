package com.ahmedou.bibliotheque.dto;

import com.ahmedou.bibliotheque.model.StatutAchat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchatDTO {
    private Long id;
    private LivreSimpleDTO livre;
    private double montant;
    private String banque;
    private StatutAchat status;
    private LocalDate date;
    private String preuve;
    private UtilisateurSimpleDTO client;
    private UtilisateurSimpleDTO admin; 
}
