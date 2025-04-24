package com.ahmedou.bibliotheque.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahmedou.bibliotheque.dto.UserDetailDTO;
import com.ahmedou.bibliotheque.dto.UtilisateurDTO;
import com.ahmedou.bibliotheque.model.Role;
import com.ahmedou.bibliotheque.service.UtilisateurService;


@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {
        List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }

    @PutMapping("/{id}/changer-role")
    public ResponseEntity<String> changerRoleJson(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
                String roleStr = body.get("role");
                Role nouveauRole = Role.valueOf(roleStr);
                utilisateurService.changerRole(id, nouveauRole);
                return ResponseEntity.ok("Rôle mis à jour avec succès .");
            }
    
    @GetMapping("/{id}/detail")
    public ResponseEntity<UserDetailDTO> getUserDetaille(@PathVariable Long id) {
        UserDetailDTO detail = utilisateurService.getUserDetaille(id);
        return ResponseEntity.ok(detail);
    }


}
