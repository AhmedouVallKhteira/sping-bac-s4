package com.ahmedou.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahmedou.bibliotheque.model.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByEmailAndType(String email, String type);
}