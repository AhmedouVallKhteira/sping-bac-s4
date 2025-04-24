package com.ahmedou.bibliotheque.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahmedou.bibliotheque.dto.CreateEvaluationRequest;
import com.ahmedou.bibliotheque.dto.EvaluationDTO;
import com.ahmedou.bibliotheque.model.EvaluationAuteur;
import com.ahmedou.bibliotheque.model.EvaluationLivre;
import com.ahmedou.bibliotheque.service.EvaluationAuteurService;
import com.ahmedou.bibliotheque.service.EvaluationLivreService;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationAuteurService evaluationAuteurService;

    @Autowired
    private EvaluationLivreService evaluationLivreService;

    @GetMapping("/{type}")
    public ResponseEntity<List<EvaluationDTO>> getAll(@PathVariable String type) {
        if (type.equals("livre")) {
            return ResponseEntity.ok(evaluationLivreService.findAll());
        } else if (type.equals("auteur")) {
            return ResponseEntity.ok(evaluationAuteurService.findAll());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<EvaluationDTO> getById(@PathVariable String type, @PathVariable Long id) {
        if (type.equals("livre")) {
            return evaluationLivreService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else if (type.equals("auteur")) {
            return evaluationAuteurService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{type}")
    public ResponseEntity<EvaluationDTO> create(
            @PathVariable String type,
            @RequestBody CreateEvaluationRequest request) {

        if (type.equals("livre")) {
            EvaluationLivre evaluation = new EvaluationLivre();
            evaluation.setNote(request.getNote());
            evaluation.setCommentaire(request.getCommentaire());
            return ResponseEntity.ok(
                evaluationLivreService.create(evaluation, request.getUtilisateurId(), request.getCibleId())
            );
        } else if (type.equals("auteur")) {
            EvaluationAuteur evaluation = new EvaluationAuteur();
            evaluation.setNote(request.getNote());
            evaluation.setCommentaire(request.getCommentaire());
            return ResponseEntity.ok(
                evaluationAuteurService.create(evaluation, request.getUtilisateurId(), request.getCibleId())
            );
        }

        return ResponseEntity.badRequest().build();
    }


    @PutMapping("/{type}/{id}")
    public ResponseEntity<EvaluationDTO> update(@PathVariable String type,
                                                @PathVariable Long id,
                                                @RequestBody EvaluationDTO evaluation) {
        if (type.equals("livre")) {
            EvaluationLivre updated = new EvaluationLivre();
            updated.setNote(evaluation.getNote());
            updated.setCommentaire(evaluation.getCommentaire());
            return ResponseEntity.ok(evaluationLivreService.update(id, updated));
        } else if (type.equals("auteur")) {
            EvaluationAuteur updated = new EvaluationAuteur();
            updated.setNote(evaluation.getNote());
            updated.setCommentaire(evaluation.getCommentaire());
            return ResponseEntity.ok(evaluationAuteurService.update(id, updated));
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<Void> delete(@PathVariable String type, @PathVariable Long id) {
        if (type.equals("livre")) {
            evaluationLivreService.delete(id);
            return ResponseEntity.ok().build();
        } else if (type.equals("auteur")) {
            evaluationAuteurService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{type}/utilisateur/{utilisateurId}")
    public ResponseEntity<List<EvaluationDTO>> getByUtilisateurId(@PathVariable String type, @PathVariable Long utilisateurId) {
        if (type.equals("livre")) {
            return ResponseEntity.ok(evaluationLivreService.findByUtilisateurId(utilisateurId));
        } else if (type.equals("auteur")) {
            return ResponseEntity.ok(evaluationAuteurService.findByUtilisateurId(utilisateurId));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{type}/cible/{cibleId}")
    public ResponseEntity<List<EvaluationDTO>> getByCibleId(@PathVariable String type, @PathVariable Long cibleId) {
        if (type.equals("livre")) {
            return ResponseEntity.ok(evaluationLivreService.findByLivreId(cibleId));
        } else if (type.equals("auteur")) {
            return ResponseEntity.ok(evaluationAuteurService.findByAuteurId(cibleId));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{type}/utilisateur/{utilisateurId}/cible/{cibleId}")
    public ResponseEntity<List<EvaluationDTO>> getByUtilisateurAndCibleId(@PathVariable String type,
                                                                          @PathVariable Long utilisateurId,
                                                                          @PathVariable Long cibleId) {
        if (type.equals("livre")) {
            return ResponseEntity.ok(evaluationLivreService.findByUtilisateurAndLivreId(utilisateurId, cibleId));
        } else if (type.equals("auteur")) {
            return ResponseEntity.ok(evaluationAuteurService.findByUtilisateurAndAuteurId(utilisateurId, cibleId));
        }
        return ResponseEntity.badRequest().build();
    }
}