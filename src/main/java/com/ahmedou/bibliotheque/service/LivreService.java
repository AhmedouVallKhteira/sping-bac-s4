package com.ahmedou.bibliotheque.service;

import java.io.IOException;
import java.util.List;

import com.ahmedou.bibliotheque.dto.LivreDetailDTO;
import com.ahmedou.bibliotheque.dto.LivreSimpleDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ahmedou.bibliotheque.model.Livre;
import com.ahmedou.bibliotheque.repository.LivreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivreService {

    private final LivreRepository livreRepository;
    private final FileStorageService fileStorageService;

    public Livre ajouterLivre(Livre livre, MultipartFile image) throws Exception {
        String titre = livre.getTitre();
        String description = livre.getDescription();
        WekaGenreClassifier wekaGenreClassifier= new WekaGenreClassifier();
        String genre= wekaGenreClassifier.predict(titre, description);
        livre.setGenre(genre);
        Livre savedLivre = livreRepository.save(livre);
        if (image != null && !image.isEmpty()) {
            try {
                fileStorageService.createFile("livres", savedLivre.getId().toString(), image);
            } catch (IOException e) {
                throw new RuntimeException("Erreur d’enregistrement de l’image", e);
            }
        }

        return savedLivre;
    }

    public Livre modifierLivre(Long id, Livre updatedData, MultipartFile image) throws Exception {
        Livre existing = livreRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setTitre(updatedData.getTitre());
        existing.setDescription(updatedData.getDescription());
        existing.setPrix(updatedData.getPrix());
        existing.setIsbn(updatedData.getIsbn());
        existing.setDatePublication(updatedData.getDatePublication());

        String titre = existing.getTitre();
        String description = existing.getDescription();
        WekaGenreClassifier wekaGenreClassifier= new WekaGenreClassifier();
        String genre= wekaGenreClassifier.predict(titre, description);
        existing.setGenre(genre);

        Livre updated = livreRepository.save(existing);

        if (image != null && !image.isEmpty()) {
            try {
                fileStorageService.updateFile("livres", updated.getId().toString(), image);
            } catch (IOException e) {
                throw new RuntimeException("Erreur mise à jour image", e);
            }
        }

        return updated;
    }

    public boolean supprimerLivre(Long id) {
        Livre livre = livreRepository.findById(id).orElse(null);
        if (livre == null) return false;

        try {
            fileStorageService.deleteFile("livres", id.toString());
        } catch (IOException e) {
            throw new RuntimeException("Erreur suppression image", e);
        }

        livreRepository.deleteById(id);
        return true;
    }

    public LivreDetailDTO getLivreById(Long id) {

        Livre livre = livreRepository.findById(id).orElse(null);
        if(livre == null)
            return null;
        List<Livre> livres = livre.getLivresSimillers(livreRepository.findAll());

        List<LivreSimpleDTO> livresSimlaires = List.of();
        for(Livre l : livres){
            livresSimlaires.add(new LivreSimpleDTO(l));
        }
        return new LivreDetailDTO(livre,livresSimlaires);
    }

    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    public Livre getlivreById(Long id) {
        return livreRepository.findById(id).orElse(null);
    }
}
