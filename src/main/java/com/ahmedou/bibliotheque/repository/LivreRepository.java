package com.ahmedou.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahmedou.bibliotheque.model.Livre;

import java.util.List;


public interface LivreRepository extends JpaRepository<Livre, Long> {
    List<Livre> findByAuteurId(Long auteurId);
}
