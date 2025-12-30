/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;
import db.biometry.biometry.dto.ApiResponse;
import db.biometry.biometry.dto.DashboardStatsDTO;
import db.biometry.biometry.dto.StatistiquesMensuellesDTO;
import db.biometry.biometry.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion du tableau de bord
 * Expose les endpoints API pour récupérer les statistiques des prestations médicales
 * 
 * @author Votre Nom
 * @version 1.0
 */
@RestController
/**
 *
 * @author USER01
 */
@RequestMapping("/dashboard")
public class DashboardRestController {
    @Autowired
    private DashboardService dashboardService;

    /**
     * Récupère les statistiques générales du tableau de bord
     * Compte le nombre de prestations en attente de validation pour chaque type
     * 
     * Endpoint: GET /api/dashboard/stats
     * 
     * @return ResponseEntity contenant les statistiques dans un wrapper ApiResponse
     * @throws Exception en cas d'erreur lors de la récupération des données
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {
        try {
            // Appel du service pour récupérer les statistiques
            DashboardStatsDTO stats = dashboardService.getStatistiquesGlobales();
            
            // Retour d'une réponse réussie avec les données
            return ResponseEntity.ok(
                new ApiResponse<>(
                    true, 
                    "Statistiques récupérées avec succès", 
                    stats
                )
            );
            
        } catch (Exception e) {
            // Log de l'erreur (utilisez un logger en production)
            e.printStackTrace();
            
            // Retour d'une réponse d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                    false, 
                    "Erreur lors de la récupération des statistiques: " + e.getMessage(), 
                    null
                ));
        }
    }

    /**
     * Récupère les statistiques mensuelles pour une année donnée
     * Filtre uniquement les prestations avec l'état "ENCAISSE"
     * 
     * Endpoint: GET /api/dashboard/statistiques-mensuelles/{annee}
     * 
     * @param annee Année pour laquelle récupérer les statistiques (ex: 2024)
     * @return ResponseEntity contenant la liste des statistiques mensuelles
     * @throws Exception en cas d'erreur lors de la récupération des données
     */
    @GetMapping("/statistiques-mensuelles/{annee}")
    public ResponseEntity<ApiResponse<List<StatistiquesMensuellesDTO>>> getStatistiquesMensuelles(
            @PathVariable Integer annee) {
        try {
            // Validation du paramètre année
            if (annee == null || annee < 2000 || annee > 2100) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                        false, 
                        "Année invalide. Veuillez fournir une année entre 2000 et 2100", 
                        null
                    ));
            }
            
            // Appel du service pour récupérer les statistiques mensuelles
            List<StatistiquesMensuellesDTO> statistiques = 
                dashboardService.getStatistiquesMensuellesByAnnee(annee);
            
            // Vérification si des données ont été trouvées
            if (statistiques == null || statistiques.isEmpty()) {
                return ResponseEntity.ok(
                    new ApiResponse<>(
                        true, 
                        "Aucune statistique trouvée pour l'année " + annee, 
                        statistiques
                    )
                );
            }
            
            // Retour d'une réponse réussie avec les données
            return ResponseEntity.ok(
                new ApiResponse<>(
                    true, 
                    "Statistiques mensuelles récupérées avec succès pour l'année " + annee, 
                    statistiques
                )
            );
            
        } catch (Exception e) {
            // Log de l'erreur
            e.printStackTrace();
            
            // Retour d'une réponse d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                    false, 
                    "Erreur lors de la récupération des statistiques mensuelles: " + e.getMessage(), 
                    null
                ));
        }
    }

    /**
     * Récupère les statistiques détaillées avec filtres optionnels
     * Permet de filtrer par année et optionnellement par mois
     * 
     * Endpoint: GET /api/dashboard/statistiques-detaillees
     * 
     * @param annee Année obligatoire pour le filtrage
     * @param mois Mois optionnel pour un filtrage plus précis (1-12)
     * @return ResponseEntity contenant les statistiques détaillées
     * @throws Exception en cas d'erreur lors de la récupération des données
     */
    @GetMapping("/statistiques-detaillees")
    public ResponseEntity<ApiResponse<List<StatistiquesMensuellesDTO>>> getStatistiquesDetaillees(
            @RequestParam Integer annee,
            @RequestParam(required = false) Integer mois) {
        try {
            // Validation des paramètres
            if (annee == null || annee < 2000 || annee > 2100) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                        false, 
                        "Année invalide", 
                        null
                    ));
            }
            
            if (mois != null && (mois < 1 || mois > 12)) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                        false, 
                        "Mois invalide. Veuillez fournir un mois entre 1 et 12", 
                        null
                    ));
            }
            
            // Appel du service avec ou sans filtre de mois
            List<StatistiquesMensuellesDTO> statistiques;
            if (mois != null) {
                statistiques = dashboardService.getStatistiquesParMois(annee, mois);
            } else {
                statistiques = dashboardService.getStatistiquesMensuellesByAnnee(annee);
            }
            
            // Retour d'une réponse réussie
            return ResponseEntity.ok(
                new ApiResponse<>(
                    true, 
                    "Statistiques détaillées récupérées avec succès", 
                    statistiques
                )
            );
            
        } catch (Exception e) {
            // Log de l'erreur
            e.printStackTrace();
            
            // Retour d'une réponse d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                    false, 
                    "Erreur lors de la récupération des statistiques détaillées: " + e.getMessage(), 
                    null
                ));
        }
    }
}
