/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;
import db.biometry.biometry.dtos.AdherentDTO;
import db.biometry.biometry.dtos.AdherentFilterDTO;
import db.biometry.biometry.dtos.DashboardStatisticsDTO;
import db.biometry.biometry.dtos.DetailConsomationAdherentDTO;
import db.biometry.biometry.services.AdherentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST pour la gestion des adhérents
 * Expose les endpoints CRUD pour les adhérents
 * 
 * @author JIATOU FRANCK
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/adherents")
@RequiredArgsConstructor
@Tag(name = "Gestion des Adhérents", description = "APIs pour la gestion des adhérents/assurés")
@CrossOrigin("*")
public class AdherentController {
    private final AdherentService adherentService;
    
    /**
     * Recherche des adhérents avec filtres et pagination
     * 
     * @param filter Critères de recherche
     * @return Page d'adhérents
     */
    @PostMapping("/search")
    @Operation(
            summary = "Recherche d'adhérents",
            description = "Recherche des adhérents avec filtres multiples et pagination"
    )
    public ResponseEntity<Page<AdherentDTO>> searchAdherents(
            @Valid @RequestBody AdherentFilterDTO filter) {
        
        log.info("POST /api/v1/adherents/search - filtres: {}", filter);
        
        Page<AdherentDTO> adherents = adherentService.searchAdherents(filter);
        
        return ResponseEntity.ok(adherents);
    }
    
    /**
     * Récupère le profil détaillé d'un adhérent
     * 
     * @param codeAdherent Code de l'adhérent
     * @return Profil détaillé
     */
    @GetMapping("/{codeAdherent}")
    @Operation(
            summary = "Détails d'un adhérent",
            description = "Récupère le profil complet d'un adhérent incluant ses ayants droit et sa consommation"
    )
    public ResponseEntity<AdherentDTO> getAdherent(
            @Parameter(description = "Code de l'adhérent", required = true)
            @PathVariable String codeAdherent) {
        
        log.info("GET /api/v1/adherents/{} - récupération du profil", codeAdherent);
        
        AdherentDTO adherent = adherentService.getAdherentProfile(codeAdherent);
        
        return ResponseEntity.ok(adherent);
    }
    
    /**
     * Crée un nouvel adhérent
     * 
     * @param adherentDTO Données de l'adhérent
     * @return Adhérent créé
     */
    @PostMapping
    @Operation(
            summary = "Créer un adhérent",
            description = "Crée un nouvel adhérent dans le système"
    )
    public ResponseEntity<AdherentDTO> createAdherent(
            @Valid @RequestBody AdherentDTO adherentDTO) {
        
        log.info("POST /api/v1/adherents - création: {}", adherentDTO.getAssurePrincipal());
        
        AdherentDTO created = adherentService.createAdherent(adherentDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Met à jour un adhérent existant
     * 
     * @param codeAdherent Code de l'adhérent
     * @param adherentDTO Nouvelles données
     * @return Adhérent mis à jour
     */
    @PutMapping("/{codeAdherent}")
    @Operation(
            summary = "Modifier un adhérent",
            description = "Met à jour les informations d'un adhérent existant"
    )
    public ResponseEntity<AdherentDTO> updateAdherent(
            @Parameter(description = "Code de l'adhérent", required = true)
            @PathVariable String codeAdherent,
            @Valid @RequestBody AdherentDTO adherentDTO) {
        
        log.info("PUT /api/v1/adherents/{} - mise à jour", codeAdherent);
        
        AdherentDTO updated = adherentService.updateAdherent(codeAdherent, adherentDTO);
        
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Supprime un adhérent
     * 
     * @param codeAdherent Code de l'adhérent
     * @return Confirmation de suppression
     */
    @DeleteMapping("/{codeAdherent}")
    @Operation(
            summary = "Supprimer un adhérent",
            description = "Supprime logiquement un adhérent (changement de statut)"
    )
    public ResponseEntity<Void> deleteAdherent(
            @Parameter(description = "Code de l'adhérent", required = true)
            @PathVariable String codeAdherent) {
        
        log.info("DELETE /api/v1/adherents/{} - suppression", codeAdherent);
        
        adherentService.deleteAdherent(codeAdherent);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Change le statut d'un adhérent
     * 
     * @param codeAdherent Code de l'adhérent
     * @param statut Nouveau statut
     * @return Adhérent mis à jour
     */
    @PatchMapping("/{codeAdherent}/statut")
    @Operation(
            summary = "Changer le statut",
            description = "Modifie le statut d'un adhérent (ACTIF, SUSPENDU, RESILIE)"
    )
    public ResponseEntity<AdherentDTO> changeStatut(
            @Parameter(description = "Code de l'adhérent", required = true)
            @PathVariable String codeAdherent,
            @Parameter(description = "Nouveau statut", required = true)
            @RequestParam String statut) {
        
        log.info("PATCH /api/v1/adherents/{}/statut - changement vers: {}", codeAdherent, statut);
        
        AdherentDTO adherent = adherentService.getAdherentProfile(codeAdherent);
        adherent.setStatut(statut);
        AdherentDTO updated = adherentService.updateAdherent(codeAdherent, adherent);
        
        return ResponseEntity.ok(updated);
    }
    
       public ResponseEntity<List<DetailConsomationAdherentDTO>> getDetailConsomationAdherent(
            @Parameter(description = "Code du souscripteur", required = true)
            @RequestParam String codeSouscripteur,
            
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDateTime dateDebut,
            
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDateTime dateFin){
           
           
           List<DetailConsomationAdherentDTO>consomation=adherentService.detailConsomationAdherent(dateDebut, dateFin, codeSouscripteur);
             return ResponseEntity.ok(consomation);
       }
       
         @GetMapping("/all/{souscripteur}")
    @Operation(
            summary = "Détails d'un adhérent",
            description = "Récupère le profil complet d'un adhérent incluant ses ayants droit et sa consommation"
    )
    public ResponseEntity<List<AdherentDTO>> allAdherent(
            
            @PathVariable String souscripteur) {
        
        //log.info("GET /api/v1/adherents/{} - récupération du profil", codeAdherent);
        
        
        
        return ResponseEntity.ok(adherentService.allAdherentBySoucripteur("CAMTEL"));
    }
}
