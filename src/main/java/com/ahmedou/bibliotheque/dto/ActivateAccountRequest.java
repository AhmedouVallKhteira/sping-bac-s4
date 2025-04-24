package com.ahmedou.bibliotheque.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActivateAccountRequest {

    @NotNull(message = "Le code OTP est requis.")
    @Min(value = 100000, message = "Le code OTP doit contenir exactement 6 chiffres.")
    @Max(value = 999999, message = "Le code OTP doit contenir exactement 6 chiffres.")
    private int otp;

    @NotBlank(message = "L'adresse e-mail est requise.")
    @Email(message = "L'adresse e-mail n'est pas valide.")
    private String email;
}
