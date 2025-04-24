package com.ahmedou.bibliotheque.controller;

import java.util.List;

import com.ahmedou.bibliotheque.dto.*;
import com.ahmedou.bibliotheque.model.Auteur;
import com.ahmedou.bibliotheque.service.AuteurService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auteurs")
public class AuteurController {

    private final AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Auteur> ajouterAuteur(@ModelAttribute AddAuteurRequest request) {
        Auteur auteur = new Auteur(
            request.getNom(),
            request.getBiographie(),
            request.getNationalite(),
            request.getDateNaissance()
        );
        Auteur saved = auteurService.ajouterAuteur(auteur, request.getPhoto());
        return ResponseEntity.ok(saved);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Auteur> updateAuteur(@PathVariable Long id, @ModelAttribute AddAuteurRequest request) {
        Auteur data = new Auteur(
            request.getNom(),
            request.getBiographie(),
            request.getNationalite(),
            request.getDateNaissance()
        );
        Auteur updated = auteurService.updateAuteur(id, data, request.getPhoto());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAuteur(@PathVariable Long id) {
        boolean deleted = auteurService.supprimerAuteur(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AuteurSimpleDTO>> getAllAuteurs() {
        List<AuteurSimpleDTO> auteurs = auteurService.getAllAuteurs().stream()
            .map(AuteurSimpleDTO::new)
            .toList();
        return ResponseEntity.ok(auteurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuteurDetailDTO> getAuteurDetail(@PathVariable Long id) {
        AuteurDetailDTO auteur = auteurService.getAuteurById(id);
        return auteur != null
            ? ResponseEntity.ok(auteur)
            : ResponseEntity.notFound().build();
    }
}
