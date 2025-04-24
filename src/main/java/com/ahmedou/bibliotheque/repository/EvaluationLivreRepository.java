package com.ahmedou.bibliotheque.repository;

import com.ahmedou.bibliotheque.model.EvaluationLivre;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EvaluationLivreRepository extends JpaRepository<EvaluationLivre, Long> {
    List<EvaluationLivre> findByUtilisateurId(Long utilisateurId);
    List<EvaluationLivre> findByLivreId(Long id);
    List<EvaluationLivre> findByUtilisateurIdAndLivreId(Long utilisateurId, Long livreId);
}
