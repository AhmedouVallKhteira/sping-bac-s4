package com.ahmedou.bibliotheque.model;

import java.time.LocalDate;

import com.ahmedou.bibliotheque.service.FileStorageService;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "livre_id")
    private Livre livre;


    private double montant;

    private String banque;

    @Enumerated(EnumType.STRING)
    private StatutAchat status;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Utilisateur client;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Utilisateur admin;

    public String getPreuve(){
        return FileStorageService.getFileUrl("achats", id.toString());

    };
}
