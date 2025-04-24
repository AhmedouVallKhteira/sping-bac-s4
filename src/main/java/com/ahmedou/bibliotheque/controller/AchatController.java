package com.ahmedou.bibliotheque.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahmedou.bibliotheque.dto.AchatDTO;
import com.ahmedou.bibliotheque.dto.FaireAchatRequest;
import com.ahmedou.bibliotheque.dto.ValiderAchatRequest;
import com.ahmedou.bibliotheque.service.AchatService;

@RestController
@RequestMapping("/api/achats")
public class AchatController {

    private final AchatService achatService;

    public AchatController(AchatService achatService) {
        this.achatService = achatService;
    }

    @PostMapping(value = "/faire", consumes = "multipart/form-data")
    public ResponseEntity<AchatDTO> faireAchat(@ModelAttribute FaireAchatRequest request) {
        AchatDTO achat = achatService.faireAchat(request);
        return ResponseEntity.ok(achat);
    }

    @PostMapping("/confirmer")
    public ResponseEntity<AchatDTO> confirmerAchat(@RequestBody ValiderAchatRequest request) {
        AchatDTO achat = achatService.confirmerAchat(request);
        return ResponseEntity.ok(achat);
    }

    @PostMapping("/refuser")
    public ResponseEntity<AchatDTO> refuserAchat(@RequestBody ValiderAchatRequest request) {
        AchatDTO achat = achatService.refuserAchat(request);
        return ResponseEntity.ok(achat);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AchatDTO>> getAllAchats() {
        List<AchatDTO> achats = achatService.getAllAchats();
        return ResponseEntity.ok(achats);
    }
    @GetMapping("/by-client/{id}")
    public ResponseEntity<List<AchatDTO>> getAchatsByClient(@PathVariable Long id) {
        List<AchatDTO> achats = achatService.getAchatsByClientId(id);
        return ResponseEntity.ok(achats);
    }

    @GetMapping("/by-admin/{id}")
    public ResponseEntity<List<AchatDTO>> getAchatsByAdmin(@PathVariable Long id) {
        List<AchatDTO> achats = achatService.getAchatsByAdminId(id);
        return ResponseEntity.ok(achats);
    }

}
