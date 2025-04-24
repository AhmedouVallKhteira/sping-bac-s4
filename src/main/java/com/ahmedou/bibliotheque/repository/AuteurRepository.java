package com.ahmedou.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahmedou.bibliotheque.model.Auteur;

public interface AuteurRepository extends JpaRepository<Auteur, Long> {
}
