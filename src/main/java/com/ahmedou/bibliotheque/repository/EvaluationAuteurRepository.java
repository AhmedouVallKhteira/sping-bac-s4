package com.ahmedou.bibliotheque.repository;

import com.ahmedou.bibliotheque.model.EvaluationAuteur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EvaluationAuteurRepository extends JpaRepository<EvaluationAuteur, Long> {
    List<EvaluationAuteur> findByUtilisateurId(Long utilisateurId);
    List<EvaluationAuteur> findByAuteurId(Long auteurId);
    List<EvaluationAuteur> findByUtilisateurIdAndAuteurId(Long utilisateurId, Long auteurId);
}
