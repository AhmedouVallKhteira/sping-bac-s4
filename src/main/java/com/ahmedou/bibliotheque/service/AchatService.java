package com.ahmedou.bibliotheque.service;

import com.ahmedou.bibliotheque.dto.*;
import com.ahmedou.bibliotheque.model.*;
import com.ahmedou.bibliotheque.repository.AchatRepository;
import com.ahmedou.bibliotheque.repository.LivreRepository;
import com.ahmedou.bibliotheque.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchatService {

    private final AchatRepository achatRepository;
    private final LivreRepository livreRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final FileStorageService fileStorageService;

    public AchatService(
            AchatRepository achatRepository,
            LivreRepository livreRepository,
            UtilisateurRepository utilisateurRepository,
            FileStorageService fileStorageService) {
        this.achatRepository = achatRepository;
        this.livreRepository = livreRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public AchatDTO faireAchat(FaireAchatRequest request) {
        Livre livre = livreRepository.findById(request.getLivreId())
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        Utilisateur client = utilisateurRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Achat achat = new Achat();
        achat.setLivre(livre);
        achat.setMontant(request.getMontant());
        achat.setBanque(request.getBanque());
        achat.setDate(LocalDate.now());
        achat.setStatus(StatutAchat.EN_ATTENTE);
        achat.setClient(client);

        Achat savedAchat = achatRepository.save(achat);

        try {
            fileStorageService.createFile("achats", savedAchat.getId().toString(), request.getPreuve());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de la preuve de paiement", e);
        }

        return toDTO(savedAchat);
    }

    @Transactional
    public AchatDTO confirmerAchat(ValiderAchatRequest request) {
        Achat achat = achatRepository.findById(request.getAchatId())
                .orElseThrow(() -> new RuntimeException("Achat introuvable"));

        Utilisateur admin = utilisateurRepository.findById(request.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));

        achat.setStatus(StatutAchat.CONFIRME);
        achat.setAdmin(admin);

        return toDTO(achatRepository.save(achat));
    }

    @Transactional
    public AchatDTO refuserAchat(ValiderAchatRequest request) {
        Achat achat = achatRepository.findById(request.getAchatId())
                .orElseThrow(() -> new RuntimeException("Achat introuvable"));

        Utilisateur admin = utilisateurRepository.findById(request.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));

        achat.setStatus(StatutAchat.REJETE);
        achat.setAdmin(admin);

        return toDTO(achatRepository.save(achat));
    }

    public List<AchatDTO> getAllAchats() {
        return achatRepository.findAllByOrderByDateDesc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AchatDTO> getAchatsByClientId(Long clientId) {
    return achatRepository.findByClientIdOrderByDateDesc(clientId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<AchatDTO> getAchatsByAdminId(Long adminId) {
        return achatRepository.findByAdminIdOrStatusOrderByDateDesc(adminId, "EN_ATTENTE")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    private AchatDTO toDTO(Achat achat) {
        return new AchatDTO(
                achat.getId(),
                new LivreSimpleDTO(achat.getLivre()),
                achat.getMontant(),
                achat.getBanque(),
                achat.getStatus(),
                achat.getDate(),
                achat.getPreuve(),
                new UtilisateurSimpleDTO(achat.getClient()),
                achat.getAdmin() != null ? new UtilisateurSimpleDTO(achat.getAdmin()) : null
        );
    }
}
