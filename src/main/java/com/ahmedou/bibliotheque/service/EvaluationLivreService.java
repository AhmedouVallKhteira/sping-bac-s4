package com.ahmedou.bibliotheque.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahmedou.bibliotheque.dto.EvaluationDTO;
import com.ahmedou.bibliotheque.model.EvaluationLivre;
import com.ahmedou.bibliotheque.model.Livre;
import com.ahmedou.bibliotheque.model.Utilisateur;
import com.ahmedou.bibliotheque.repository.EvaluationLivreRepository;
import com.ahmedou.bibliotheque.repository.LivreRepository;
import com.ahmedou.bibliotheque.repository.UtilisateurRepository;

@Service
public class EvaluationLivreService {
    
    @Autowired
    private EvaluationLivreRepository evaluationLivreRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private LivreRepository livreRepository;

    public List<EvaluationDTO> findAll() {
        return evaluationLivreRepository.findAll().stream()
                .map(EvaluationDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<EvaluationDTO> findById(Long id) {
        return evaluationLivreRepository.findById(id)
                .map(EvaluationDTO::new);
    }

    public EvaluationDTO create(EvaluationLivre evaluation, Long utilisateurId, Long livreId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElseThrow();
        Livre livre = livreRepository.findById(livreId).orElseThrow();
        evaluation.setUtilisateur(utilisateur);
        evaluation.setLivre(livre);
        return new EvaluationDTO(evaluationLivreRepository.save(evaluation));
    }
    
    public EvaluationDTO update(Long id, EvaluationLivre updated) {
        EvaluationLivre existing = evaluationLivreRepository.findById(id).orElseThrow();
        existing.setNote(updated.getNote());
        existing.setCommentaire(updated.getCommentaire());
        return new EvaluationDTO(evaluationLivreRepository.save(existing));
    }

    public List<EvaluationDTO> findByUtilisateurId(Long id) {
        return evaluationLivreRepository.findByUtilisateurId(id).stream()
                .map(EvaluationDTO::new)
                .collect(Collectors.toList());
    }

    public List<EvaluationDTO> findByLivreId(Long id) {
        return evaluationLivreRepository.findByLivreId(id).stream()
                .map(EvaluationDTO::new)
                .collect(Collectors.toList());
    }

    public List<EvaluationDTO> findByUtilisateurAndLivreId(Long utilisateurId, Long livreId) {
        return evaluationLivreRepository.findByUtilisateurIdAndLivreId(utilisateurId, livreId).stream()
                .map(EvaluationDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(long id) {
        evaluationLivreRepository.deleteById(id);
    }
}
