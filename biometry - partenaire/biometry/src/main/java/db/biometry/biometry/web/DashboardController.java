/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;
import db.biometry.biometry.dtos.DashboardStatisticsDTO;
import db.biometry.biometry.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST pour le tableau de bord souscripteur
 * Expose les endpoints pour les statistiques et indicateurs
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
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Tableau de Bord", description = "APIs pour le tableau de bord souscripteur")
@CrossOrigin("*")
public class DashboardController {
     private final DashboardService dashboardService;
    
    /**
     * Récupère les statistiques complètes du tableau de bord
     * 
     * @param codeSouscripteur Code du souscripteur
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @return Statistiques du tableau de bord
     */
    @GetMapping("/statistics")
    @Operation(
            summary = "Statistiques du tableau de bord",
            description = "Récupère toutes les statistiques et indicateurs pour le tableau de bord souscripteur"
    )
    public ResponseEntity<DashboardStatisticsDTO> getStatistics(
            @Parameter(description = "Code du souscripteur", required = true)
            @RequestParam String codeSouscripteur,
            
            @Parameter(description = "Date de début (format: yyyy-MM-dd)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate dateDebut,
            
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate dateFin) {
        
        log.info("GET /api/v1/dashboard/statistics - souscripteur: {}, période: {} - {}", 
                codeSouscripteur, dateDebut, dateFin);
        
        // Dates par défaut si non spécifiées (année en cours)
        LocalDateTime startDate = dateDebut != null ? 
                dateDebut.atStartOfDay() : 
                LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        
        LocalDateTime endDate = dateFin != null ? 
                dateFin.atTime(23, 59, 59) : 
                LocalDateTime.now();
        
        DashboardStatisticsDTO statistics = dashboardService.generateDashboardStatistics(
                codeSouscripteur, startDate, endDate);
        
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Récupère les statistiques pour le mois en cours
     * 
     * @param codeSouscripteur Code du souscripteur
     * @return Statistiques du mois
     */
    @GetMapping("/statistics/current-month")
    @Operation(
            summary = "Statistiques du mois en cours",
            description = "Récupère les statistiques pour le mois en cours"
    )
    public ResponseEntity<DashboardStatisticsDTO> getCurrentMonthStatistics(
            @Parameter(description = "Code du souscripteur", required = true)
            @RequestParam String codeSouscripteur) {
        
        log.info("GET /api/v1/dashboard/statistics/current-month - souscripteur: {}", codeSouscripteur);
        
        LocalDateTime startDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now();
        
        DashboardStatisticsDTO statistics = dashboardService.generateDashboardStatistics(
                codeSouscripteur, startDate, endDate);
        
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Récupère les statistiques pour l'année en cours
     * 
     * @param codeSouscripteur Code du souscripteur
     * @return Statistiques de l'année
     */
    @GetMapping("/statistics/current-year")
    @Operation(
            summary = "Statistiques de l'année en cours",
            description = "Récupère les statistiques pour l'année en cours"
    )
    public ResponseEntity<DashboardStatisticsDTO> getCurrentYearStatistics(
            @Parameter(description = "Code du souscripteur", required = true)
            @RequestParam String codeSouscripteur) {
        
        log.info("GET /api/v1/dashboard/statistics/current-year - souscripteur: {}", codeSouscripteur);
        
        LocalDateTime startDate = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now();
        
        DashboardStatisticsDTO statistics = dashboardService.generateDashboardStatistics(
                codeSouscripteur, startDate, endDate);
        
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Récupère les statistiques pour les 7 derniers jours
     * 
     * @param codeSouscripteur Code du souscripteur
     * @return Statistiques de la semaine
     */
    @GetMapping("/statistics/last-week")
    @Operation(
            summary = "Statistiques des 7 derniers jours",
            description = "Récupère les statistiques pour les 7 derniers jours"
    )
    public ResponseEntity<DashboardStatisticsDTO> getLastWeekStatistics(
            @Parameter(description = "Code du souscripteur", required = true)
            @RequestParam String codeSouscripteur) {
        
        log.info("GET /api/v1/dashboard/statistics/last-week - souscripteur: {}", codeSouscripteur);
        
        LocalDateTime startDate = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now();
        
        DashboardStatisticsDTO statistics = dashboardService.generateDashboardStatistics(
                codeSouscripteur, startDate, endDate);
        
        return ResponseEntity.ok(statistics);
    }
}
