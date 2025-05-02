package com.ahmedou.bibliotheque.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ahmedou.bibliotheque.dto.AuteurDetailDTO;
import com.ahmedou.bibliotheque.dto.AuteurSimpleDTO;
import com.ahmedou.bibliotheque.dto.LivreDetailDTO;
import com.ahmedou.bibliotheque.dto.LivreSimpleDTO;
import com.ahmedou.bibliotheque.model.Livre;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ahmedou.bibliotheque.model.Auteur;
import com.ahmedou.bibliotheque.repository.AuteurRepository;

@Service
public class AuteurService {

    private final AuteurRepository auteurRepository;
    private final FileStorageService fileStorageService;


    public AuteurService(AuteurRepository auteurRepository, FileStorageService fileStorageService) {
        this.auteurRepository = auteurRepository;
        this.fileStorageService = fileStorageService;
    }

    public Auteur ajouterAuteur(Auteur auteur, MultipartFile photo) {
        Auteur savedAuteur = auteurRepository.save(auteur);

        if (photo != null && !photo.isEmpty()) {
            try {
                fileStorageService.createFile("auteurs", savedAuteur.getId().toString(), photo);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de l'enregistrement de la photo", e);
            }
        }

        return savedAuteur;
    }

    public Auteur updateAuteur(Long id, Auteur details, MultipartFile photo) {
        Auteur auteur = auteurRepository.findById(id).orElse(null);
        if (auteur == null) throw new RuntimeException("Auteur non trouvé");

        auteur.setNom(details.getNom());
        auteur.setBiographie(details.getBiographie());
        auteur.setNationalite(details.getNationalite());
        auteur.setDateNaissance(details.getDateNaissance());

        Auteur updated = auteurRepository.save(auteur);

        if (photo != null && !photo.isEmpty()) {
            try {
                fileStorageService.updateFile("auteurs", updated.getId().toString(), photo);
            } catch (IOException e) {
                throw new RuntimeException("Erreur mise à jour photo", e);
            }
        }

        return updated;
    }

    public boolean supprimerAuteur(Long id) {
    Auteur auteur = auteurRepository.findById(id).orElse(null);
    if (auteur == null) return false;

    try {
        fileStorageService.deleteFile("auteurs", id.toString());
    } catch (IOException e) {
        throw new RuntimeException("Erreur lors de la suppression de la photo", e);
    }

    auteurRepository.deleteById(id);
    return true;
}


    public List<Auteur> getAllAuteurs() {
        return auteurRepository.findAll();
    }

    public AuteurDetailDTO getAuteurById(Long id) {
        Auteur auteur = auteurRepository.findById(id).orElse(null);
        if (auteur == null)
            return null;
        List<Auteur> auteurs = auteur.getAuteursSimillers(auteurRepository.findAll());

        List<AuteurSimpleDTO> auteursSimlaires = new ArrayList<>();
        for (Auteur a : auteurs) {
            auteursSimlaires.add(new AuteurSimpleDTO(a));
        }

        return new AuteurDetailDTO(auteur, auteursSimlaires);
    }
    public Auteur getauteurById(Long id){
        return auteurRepository.findById(id).orElse(null);
    }
}
