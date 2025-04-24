package com.ahmedou.bibliotheque.validation.validators;

import org.springframework.stereotype.Component;

import com.ahmedou.bibliotheque.repository.UtilisateurRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<com.ahmedou.bibliotheque.validation.annotations.UniqueEmail, String> {

    private final UtilisateurRepository utilisateurRepository;

    public UniqueEmailValidator(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) return true;
        return !utilisateurRepository.existsByEmailAndIsActiveTrue(email);
    }
}
