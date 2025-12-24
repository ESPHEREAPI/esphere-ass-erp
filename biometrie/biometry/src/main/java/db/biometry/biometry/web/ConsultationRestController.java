/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;
import db.biometry.biometry.dto.ApiResponse;
import db.biometry.biometry.services.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des consultations
 * Expose les endpoints API pour les opérations CRUD et les statistiques des consultations
 * 
 * @author Votre Nom
 * @version 1.0
 */
@RestController
@RequestMapping("/consultations")
/**
 *
 * @author USER01
 */
public class ConsultationRestController {
     @Autowired
    private ConsultationService consultationService;

    /**
     * Compte le nombre de consultations par état
     * Utilisé pour afficher les statistiques dans les cards du dashboard
     * 
     * Endpoint: GET /api/consultations/count-by-etat/{etat}
     * 
     * @param etat État de la consultation (EN_ATTENTE, ENCAISSE, REJETE, VALIDE)
     * @return ResponseEntity contenant le nombre de consultations pour cet état
     * @throws Exception en cas d'erreur lors du comptage
     */
    @GetMapping("/count-by-etat/{etat}")
    public ResponseEntity<ApiResponse<Long>> countConsultationsByEtat(
            @PathVariable String etat) {
        try {
            // Validation du paramètre état
            if (etat == null || etat.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                        false, 
                        "L'état de la consultation est obligatoire", 
                        null
                    ));
            }
            
            // Normalisation de l'état (en majuscules)
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
            
            // Appel du service pour compter les consultations
            Long count = consultationService.countByEtat(etatNormalise);
            
            // Retour d'une réponse réussie
            return ResponseEntity.ok(
                new ApiResponse<>(
                    true, 
                    "Nombre de consultations en état '" + etatNormalise + "' récupéré avec succès", 
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
                    "Erreur lors du comptage des consultations: " + e.getMessage(), 
                    null
                ));
        }
    }

    /**
     * Valide les états autorisés pour les consultations
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
