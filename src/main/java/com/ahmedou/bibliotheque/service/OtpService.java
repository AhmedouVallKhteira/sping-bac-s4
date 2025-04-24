package com.ahmedou.bibliotheque.service;

import org.springframework.stereotype.Service;

import com.ahmedou.bibliotheque.model.Otp;
import com.ahmedou.bibliotheque.repository.OtpRepository;

@Service
public class OtpService {

    private final OtpRepository otpRepository;

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public int generateOtp(String email, String type) {
        Otp otp = new Otp(email, type, otpRepository);
        otpRepository.save(otp);
        return otp.getOtp();
    }


    public boolean verifyOtp(String email, String type, int otp) {
        Otp existingOtp = otpRepository.findByEmailAndType(email, type);
        if (existingOtp == null) {
            return false;
        }
        boolean isVerified = existingOtp.isValide(otp);
        if (isVerified) {
            existingOtp.setUsed(true);
            otpRepository.save(existingOtp);
        }
        return isVerified;
    }
}
