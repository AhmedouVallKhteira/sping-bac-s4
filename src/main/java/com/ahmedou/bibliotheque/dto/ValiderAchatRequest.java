package com.ahmedou.bibliotheque.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValiderAchatRequest {
    @NotNull(message = "L'ID de l'achat est obligatoire")
    private Long achatId;
    
    @NotNull(message = "L'ID de l'admin est obligatoire")
    private Long adminId;
}