package com.ahmedou.bibliotheque.controller;

import com.ahmedou.bibliotheque.model.Livre;
import com.ahmedou.bibliotheque.service.LivreService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ahmedou.bibliotheque.dto.*;

import java.util.List;
import java.util.stream.Collectors;

import com.ahmedou.bibliotheque.model.Auteur;
import com.ahmedou.bibliotheque.service.AuteurService;

@RestController
@RequestMapping("/api/livres")
public class LivreController {

    private final LivreService livreService;
    private final AuteurService auteurService;

    public LivreController(LivreService livreService ,AuteurService auteurService) {
        this.livreService = livreService;
        this.auteurService = auteurService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Livre> ajouterLivre(@ModelAttribute AddLivreRequest request) throws Exception {
        long auteurId = request.getAuteurId();
        Auteur auteur = auteurService.getauteurById(auteurId);
        if (auteur == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Livre livre = new Livre(
            request.getTitre(),
            request.getDescription(),
            request.getPrix(),
            request.getIsbn(),
            request.getDatePublication(),
            auteur
        );

        Livre saved = livreService.ajouterLivre(livre, request.getImage());
        return ResponseEntity.ok(saved);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Livre> modifierLivre(
            @PathVariable Long id,
            @ModelAttribute AddLivreRequest request) throws Exception {
        
        long auteurId = request.getAuteurId();
        Auteur auteur = auteurService.getauteurById(auteurId);
        if (auteur == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Livre updatedData = new Livre(
            request.getTitre(),
            request.getDescription(),
            request.getPrix(),
            request.getIsbn(),
            request.getDatePublication(),
            auteur
        );

        Livre updated = livreService.modifierLivre(id, updatedData, request.getImage());
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<LivreSimpleDTO>> getAllLivres() {
        List<LivreSimpleDTO> livres = livreService.getAllLivres().stream()
            .map(LivreSimpleDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(livres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreDetailDTO> getLivre(@PathVariable Long id) {
        LivreDetailDTO livre = livreService.getLivreById(id);
        if (livre == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(livre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerLivre(@PathVariable Long id) {
        boolean deleted = livreService.supprimerLivre(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/remise-fixe")
    public ResponseEntity<Void> appliquerRemiseFixe(@RequestBody RemiseRequest request) throws Exception {
        Livre livre = livreService.getlivreById(request.getIdLivre());
        if (livre == null) {
            return ResponseEntity.notFound().build();
        }
        
        livre.setRemiseFixe(request.getRemisePourcentage());
        livreService.modifierLivre(livre.getId(), livre, null);
        
        return ResponseEntity.ok().build();
    }
    
}
