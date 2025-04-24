package com.ahmedou.bibliotheque.model;

import com.ahmedou.bibliotheque.repository.OtpRepository;
import jakarta.persistence.*;
import lombok.Data;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Entity
@Table(name = "otps")
@Data
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private int otp;
    private String type;
    private boolean isUsed;
    private LocalDateTime createdAt;

    public Otp() {}

    public Otp(String email, String type, OtpRepository otpRepository) {
        Otp existingOtp = otpRepository.findByEmailAndType(email, type);
        if (existingOtp != null) {
            this.id = existingOtp.getId();
        }
        this.email = email;
        this.type = type;
        this.otp = generateOtp();
        this.isUsed = false;
        this.createdAt = LocalDateTime.now();
    }
    public boolean isValide(int otp){
    
        if (this.isUsed) {
            System.out.println("OTP already used");
            return false;
        }
        LocalDateTime expiresAt = this.createdAt.plusMinutes(5);
        return LocalDateTime.now().isBefore(expiresAt) && this.otp == otp;
    }

    private int generateOtp() {
        SecureRandom random = new SecureRandom();
        return 100000 + random.nextInt(900000);
    }
}
