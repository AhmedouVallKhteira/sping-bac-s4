package com.ahmedou.bibliotheque.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahmedou.bibliotheque.dto.EvaluationDTO;
import com.ahmedou.bibliotheque.model.Auteur;
import com.ahmedou.bibliotheque.model.EvaluationAuteur;
import com.ahmedou.bibliotheque.model.Utilisateur;
import com.ahmedou.bibliotheque.repository.AuteurRepository;
import com.ahmedou.bibliotheque.repository.EvaluationAuteurRepository;
import com.ahmedou.bibliotheque.repository.UtilisateurRepository;

@Service
public class EvaluationAuteurService {

    @Autowired
    private EvaluationAuteurRepository evaluationAuteurRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AuteurRepository auteurRepository;

    public List<EvaluationDTO> findAll() {
        return evaluationAuteurRepository.findAll().stream()
                .map(EvaluationDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<EvaluationDTO> findById(Long id) {
        return evaluationAuteurRepository.findById(id)
                .map(EvaluationDTO::new);
    }

    public EvaluationDTO create(EvaluationAuteur evaluation, Long utilisateurId, Long auteurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElseThrow();
        Auteur auteur = auteurRepository.findById(auteurId).orElseThrow();
        evaluation.setUtilisateur(utilisateur);
        evaluation.setAuteur(auteur);
        return new EvaluationDTO(evaluationAuteurRepository.save(evaluation));
    }

    public EvaluationDTO update(Long id, EvaluationAuteur updated) {
        EvaluationAuteur existing = evaluationAuteurRepository.findById(id).orElseThrow();
        existing.setNote(updated.getNote());
        existing.setCommentaire(updated.getCommentaire());
        return new EvaluationDTO(evaluationAuteurRepository.save(existing));
    }

    public void delete(Long id) {
        evaluationAuteurRepository.deleteById(id);
    }

    public List<EvaluationDTO> findByUtilisateurId(Long id) {
        return evaluationAuteurRepository.findByUtilisateurId(id).stream()
                .map(EvaluationDTO::new)
                .collect(Collectors.toList());
    }

    public List<EvaluationDTO> findByAuteurId(Long id) {
        return evaluationAuteurRepository.findByAuteurId(id).stream()
                .map(EvaluationDTO::new)
                .collect(Collectors.toList());
    }

    public List<EvaluationDTO> findByUtilisateurAndAuteurId(Long utilisateurId, Long auteurId) {
        return evaluationAuteurRepository.findByUtilisateurIdAndAuteurId(utilisateurId, auteurId).stream()
                .map(EvaluationDTO::new)
                .collect(Collectors.toList());
    }
}
