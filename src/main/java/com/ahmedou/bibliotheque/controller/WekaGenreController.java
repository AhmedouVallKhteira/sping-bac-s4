package com.ahmedou.bibliotheque.controller;

import com.ahmedou.bibliotheque.service.WekaGenreClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/genre")
public class WekaGenreController {

    @Autowired
    private WekaGenreClassifier wekaClassifier;

    @PostMapping("/predict")
    public ResponseEntity<String> predictWithWeka(@RequestBody Map<String, String> request) {
        try {
            String titre = request.getOrDefault("titre", "");
            String description = request.getOrDefault("description", "");
            String genre = wekaClassifier.predict(titre, description);
            return ResponseEntity.ok(genre);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Erreur de prédiction : " + e.getMessage());
        }
    }

    @PostMapping("/reload")
    public ResponseEntity<String> reloadCsv(@RequestParam(defaultValue = "livres.csv") String file) {
        try {
            wekaClassifier.reloadModelFromCsv(file);
            return ResponseEntity.ok("✅ Modèle rechargé depuis : " + file);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Erreur lors du rechargement : " + e.getMessage());
        }
    }
}
