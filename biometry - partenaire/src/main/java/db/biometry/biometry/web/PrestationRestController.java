/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import db.biometry.biometry.dto.ApiResponse;
import db.biometry.biometry.services.LignePrestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des prestations (Ordonnances et Examens)
 * Les ordonnances et examens sont stockés dans la table LignePrestation
 * - Ordonnances: LignePrestation avec medicamentId non null
 * - Examens: LignePrestation avec examenId non null
 * 
 * @author Votre Nom
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@RestController
@RequestMapping("/ligne-prestations")
public class PrestationRestController {
     @Autowired
    private LignePrestationService lignePrestationService;

    /**
     * Compte le nombre d'ordonnances par état
     * Une ordonnance est une ligne de prestation avec medicamentId non null
     * 
     * Endpoint: GET /api/ordonnances/count-by-etat/{etat}
     * 
     * @param etat État de l'ordonnance (EN_ATTENTE, ENCAISSE, REJETE, VALIDE)
     * @return ResponseEntity contenant le nombre d'ordonnances pour cet état
     * @throws Exception en cas d'erreur lors du comptage
     */
    @GetMapping("/ordonnances/count-by-etat/{etat}")
    public ResponseEntity<ApiResponse<Long>> countOrdonnancesByEtat(
            @PathVariable String etat) {
        try {
            // Validation du paramètre état
            if (etat == null || etat.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                        false, 
                        "L'état de l'ordonnance est obligatoire", 
                        null
                    ));
            }
            
            // Normalisation de l'état
            String etatNormalise = etat.toLowerCase().trim();
            
            // Validation des états autorisés
            if (!isEtatValide(etatNormalise)) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                        false, 
                        "État invalide. États autorisés:  attente_validation, encaisse, rejet, valide", 
                        null
                    ));
            }
            
            // Appel du service pour compter les ordonnances
            // Filtre: medicamentId IS NOT NULL
            Long count = lignePrestationService.countOrdonnancesByEtat(etatNormalise);
            
            // Retour d'une réponse réussie
            return ResponseEntity.ok(
                new ApiResponse<>(
                    true, 
                    "Nombre d'ordonnances en état '" + etatNormalise + "' récupéré avec succès", 
                    count
                )
            );
            
        } catch (Exception e) {
            // Log de l'erreur
            e.printStackTrace();
            
            // Retour d'une réponse d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                    false, 
                    "Erreur lors du comptage des ordonnances: " + e.getMessage(), 
                    null
                ));
        }
    }

    /**
     * Compte le nombre d'examens par état
     * Un examen est une ligne de prestation avec examenId non null
     * 
     * Endpoint: GET /api/examens/count-by-etat/{etat}
     * 
     * @param etat État de l'examen (EN_ATTENTE, ENCAISSE, REJETE, VALIDE)
     * @return ResponseEntity contenant le nombre d'examens pour cet état
     * @throws Exception en cas d'erreur lors du comptage
     */
    @GetMapping("/examens/count-by-etat/{etat}")
    public ResponseEntity<ApiResponse<Long>> countExamensByEtat(
            @PathVariable String etat) {
        try {
            // Validation du paramètre état
            if (etat == null || etat.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                        false, 
                        "L'état de l'examen est obligatoire", 
                        null
                    ));
            }
            
            // Normalisation de l'état
            String etatNormalise = etat.toLowerCase().trim();
            
            // Validation des états autorisés
            if (!isEtatValide(etatNormalise)) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                        false, 
                        "État invalide. États autorisés: attente_validation, encaisse, rejet, valide", 
                        null
                    ));
            }
            
            // Appel du service pour compter les examens
            // Filtre: examenId IS NOT NULL
            Long count = lignePrestationService.countExamensByEtat(etatNormalise);
            
            // Retour d'une réponse réussie
            return ResponseEntity.ok(
                new ApiResponse<>(
                    true, 
                    "Nombre d'examens en état '" + etatNormalise + "' récupéré avec succès", 
                    count
                )
            );
            
        } catch (Exception e) {
            // Log de l'erreur
            e.printStackTrace();
            
            // Retour d'une réponse d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                    false, 
                    "Erreur lors du comptage des examens: " + e.getMessage(), 
                    null
                ));
        }
    }

    /**
     * Valide les états autorisés pour les prestations
     * 
     * @param etat État à valider
     * @return true si l'état est valide, false sinon
     */
    private boolean isEtatValide(String etat) {
        return etat.equals("attente_validation") || 
               etat.equals("encaisse") || 
               etat.equals("rejete") || 
               etat.equals("valide");
        
      
    }
}
