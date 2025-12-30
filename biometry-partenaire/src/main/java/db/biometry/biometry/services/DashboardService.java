/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dto.DashboardStatsDTO;
import db.biometry.biometry.dto.StatistiquesMensuellesDTO;
import db.biometry.biometry.repository.ConsultationRepository;
import db.biometry.biometry.repository.LignePrestationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des statistiques du tableau de bord
 * Centralise la logique métier pour les calculs de statistiques
 * 
 * @author Votre Nom
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
/**
 *
 * @author USER01
 */
public class DashboardService {
    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private LignePrestationRepository lignePrestationRepository;
       // Constante pour le statut "non supprimé"
    private static final String NON_SUPPRIME = "-1";

    // Constante pour l'état "En attente de validation"
    private static final String ETAT_EN_ATTENTE = "attente_validation";
    
    // Constante pour l'état "Encaissé"
    private static final String ETAT_ENCAISSE = "encaisse";
    private static final String NATURE_PRESTATION_ORDONNANCE="ordonnance";
    private static final String NATURE_PRESTATION_EXAMEN="examen";

    // Tableau des noms de mois en français
    private static final String[] MOIS = {
        "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    };

    /**
     * Récupère les statistiques globales pour le tableau de bord
     * Compte le nombre de prestations en attente de validation
     * 
     * @return DTO contenant les statistiques globales
     */
    public DashboardStatsDTO getStatistiquesGlobales() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        try {
            // Comptage des consultations en attente
            Long nbreConsultations = consultationRepository
                .countByEtatConsultationAndSupprime(ETAT_EN_ATTENTE, NON_SUPPRIME);
            stats.setNbreConsultationsEnAttente(nbreConsultations != null ? nbreConsultations : 0L);
            
            // Comptage des ordonnances en attente
            // Filtre: medicamentId IS NOT NULL AND etat = 'EN_ATTENTE' AND supprime = 'NON'
            Long nbreOrdonnances = lignePrestationRepository
                .countByMedicamentIdIsNotNullAndEtatAndSupprime(ETAT_EN_ATTENTE, NON_SUPPRIME);
            stats.setNbreOrdonnancesEnAttente(nbreOrdonnances != null ? nbreOrdonnances : 0L);
            
            // Comptage des examens en attente
            // Filtre: examenId IS NOT NULL AND etat = 'EN_ATTENTE' AND supprime = 'NON'
            Long nbreExamens = lignePrestationRepository
                .countByMedicamentIdIsNullAndEtatAndSupprime(ETAT_EN_ATTENTE, NON_SUPPRIME);
            stats.setNbreExamensEnAttente(nbreExamens != null ? nbreExamens : 0L);
            
        } catch (Exception e) {
            // Log de l'erreur et propagation
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des statistiques globales", e);
        }
        
        return stats;
    }

    /**
     * Récupère les statistiques mensuelles pour une année donnée
     * Filtre uniquement les prestations encaissées
     * 
     * @param annee Année pour laquelle récupérer les statistiques
     * @return Liste des statistiques mensuelles (12 mois)
     */
    public List<StatistiquesMensuellesDTO> getStatistiquesMensuellesByAnnee(Integer annee) {
        List<StatistiquesMensuellesDTO> statistiques = new ArrayList<>();
        
        try {
            // Parcours des 12 mois de l'année
            for (int mois = 1; mois <= 12; mois++) {
                StatistiquesMensuellesDTO stat = new StatistiquesMensuellesDTO();
                
                // Nom du mois
                stat.setMois(MOIS[mois - 1]);
                
                // Nombre de consultations encaissées pour ce mois
                Long consultations = consultationRepository
                    .countByAnneeAndMoisAndEtat(annee, mois, ETAT_ENCAISSE,NON_SUPPRIME);
                stat.setConsultations(consultations != null ? consultations : 0L);
                
                // Nombre d'ordonnances encaissées pour ce mois
                Long ordonnances = lignePrestationRepository
                    .countOrdonnancesByAnneeAndMoisAndEtat(annee, mois, ETAT_ENCAISSE,NATURE_PRESTATION_ORDONNANCE,NON_SUPPRIME);
                stat.setOrdonnances(ordonnances != null ? ordonnances : 0L);
                
                // Nombre d'examens encaissés pour ce mois
                Long examens = lignePrestationRepository
                    .countExamensByAnneeAndMoisAndEtat(annee, mois, ETAT_ENCAISSE,NATURE_PRESTATION_EXAMEN,NON_SUPPRIME);
                System.out.println("examen:"+examens);
                stat.setExamens(examens != null ? examens : 0L);
                
                // Montants (optionnel - peut être ajouté ultérieurement)
                stat.setMontantConsultations(
                    consultationRepository.sumMontantByAnneeAndMoisAndEtat(annee, mois, ETAT_ENCAISSE,NON_SUPPRIME)
                );
                stat.setMontantOrdonnances(
                    lignePrestationRepository.sumMontantOrdonnancesByAnneeAndMoisAndEtat(annee, mois, ETAT_ENCAISSE,NATURE_PRESTATION_ORDONNANCE,NON_SUPPRIME)
                );
                stat.setMontantExamens(
                    lignePrestationRepository.sumMontantExamensByAnneeAndMoisAndEtat(annee, mois, ETAT_ENCAISSE,NATURE_PRESTATION_EXAMEN,NON_SUPPRIME)
                );
                
                statistiques.add(stat);
            }
            
        } catch (Exception e) {
            // Log de l'erreur et propagation
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des statistiques mensuelles", e);
        }
        
        return statistiques;
    }

    /**
     * Récupère les statistiques pour un mois spécifique
     * 
     * @param annee Année
     * @param mois Mois (1-12)
     * @return Liste contenant les statistiques du mois demandé
     */
    public List<StatistiquesMensuellesDTO> getStatistiquesParMois(Integer annee, Integer mois) {
        List<StatistiquesMensuellesDTO> statistiques = new ArrayList<>();
        
        try {
            // Validation du mois
            if (mois < 1 || mois > 12) {
                throw new IllegalArgumentException("Le mois doit être entre 1 et 12");
            }
            
            StatistiquesMensuellesDTO stat = new StatistiquesMensuellesDTO();
            
            // Nom du mois
            stat.setMois(MOIS[mois - 1]);
            
            // Statistiques pour le mois demandé
            Long consultations = consultationRepository
                .countByAnneeAndMoisAndEtat(annee, mois, ETAT_ENCAISSE,NON_SUPPRIME);
            stat.setConsultations(consultations != null ? consultations : 0L);
            
            Long ordonnances = lignePrestationRepository
                .countOrdonnancesByAnneeAndMoisAndEtat(annee, mois, ETAT_ENCAISSE,NATURE_PRESTATION_ORDONNANCE,NON_SUPPRIME);
            stat.setOrdonnances(ordonnances != null ? ordonnances : 0L);
            
            Long examens = lignePrestationRepository
                .countExamensByAnneeAndMoisAndEtat(annee, mois, ETAT_ENCAISSE,NATURE_PRESTATION_EXAMEN,NON_SUPPRIME);
            stat.setExamens(examens != null ? examens : 0L);
            
            statistiques.add(stat);
            
        } catch (Exception e) {
            // Log de l'erreur et propagation
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des statistiques du mois", e);
        }
        
        return statistiques;
    }
    
}
