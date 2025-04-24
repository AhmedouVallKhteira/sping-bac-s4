package com.ahmedou.bibliotheque.dto;

import java.util.List;

import com.ahmedou.bibliotheque.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {
    private Long id;
    private String nom;
    private String email;
    private Role role;
    private List<AchatDTO> achats;
}
