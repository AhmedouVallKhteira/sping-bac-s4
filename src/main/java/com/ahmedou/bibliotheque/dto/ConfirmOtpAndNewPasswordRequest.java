package com.ahmedou.bibliotheque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConfirmOtpAndNewPasswordRequest {
    @NotBlank
    private String email;

    private int otp;

    @NotBlank
    @Size(min = 4, max = 20)
    private String newPassword;
}