package com.ahmedou.bibliotheque.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ahmedou.bibliotheque.dto.AchatDTO;
import com.ahmedou.bibliotheque.dto.UserDetailDTO;
import com.ahmedou.bibliotheque.dto.UtilisateurDTO;
import com.ahmedou.bibliotheque.model.Role;
import com.ahmedou.bibliotheque.model.Utilisateur;
import com.ahmedou.bibliotheque.repository.AchatRepository;
import com.ahmedou.bibliotheque.repository.UtilisateurRepository;
import com.ahmedou.bibliotheque.dto.UtilisateurSimpleDTO;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final AchatRepository achatRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, AchatRepository achatRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.achatRepository = achatRepository;
    }

    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurRepository.findAll()
                .stream()
                .filter(u -> u.getRole() != Role.SUPERADMIN)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UtilisateurDTO convertToDTO(Utilisateur utilisateur) {
        return new UtilisateurDTO(
            utilisateur.getId(),
            utilisateur.getNom(),
            utilisateur.getEmail(),
            utilisateur.getRole()
        );
    }

    public void changerRole(Long id, Role nouveauRole) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec ID : " + id));

        utilisateur.setRole(nouveauRole);
        utilisateurRepository.save(utilisateur);
    }

    public UserDetailDTO getUserDetaille(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId).orElseThrow();

        List<AchatDTO> achats;

        achats = switch (user.getRole()) {
            case USER -> achatRepository.findByClientIdOrderByDateDesc(userId)
                    .stream().map(this::toDTO).collect(Collectors.toList());
            case ADMIN -> achatRepository.findByAdminIdOrderByDateDesc(userId)
                    .stream().map(this::toDTO).collect(Collectors.toList());
            default -> achatRepository.findAll()
                    .stream().map(this::toDTO).collect(Collectors.toList());
        };

        return new UserDetailDTO(user.getId(), user.getNom(), user.getEmail(),user.getRole(), achats);
    }

    private AchatDTO toDTO(com.ahmedou.bibliotheque.model.Achat achat) {
        return new AchatDTO(
            achat.getId(),
            new com.ahmedou.bibliotheque.dto.LivreSimpleDTO(achat.getLivre()),
            achat.getMontant(),
            achat.getBanque(),
            achat.getStatus(),
            achat.getDate(),
            achat.getPreuve(),
            new UtilisateurSimpleDTO(achat.getClient()),
            achat.getAdmin() != null ? new com.ahmedou.bibliotheque.dto.UtilisateurSimpleDTO(achat.getAdmin()) : null
        );
    }
} 
