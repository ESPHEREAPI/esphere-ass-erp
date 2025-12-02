/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dtos.DashboardStatisticsDTO;
import db.biometry.biometry.dtos.PrestationPrestataireDTO;
import db.biometry.biometry.dtos.StatistiqueTypePrestationDTO;
import db.biometry.biometry.dtos.StatistiquesGlobalesDTO;
import db.biometry.biometry.dtos.StatistiquesParPeriodeDTO;
import db.biometry.biometry.dtos.TopPrestationDTO;
import db.biometry.biometry.repositories.AdherentRepository;
import db.biometry.biometry.repositories.AyantDroitRepository;
import db.biometry.biometry.repositories.ConsultationRepository;
import db.biometry.biometry.repositories.LignePrestationRepository;
import db.biometry.biometry.repositories.TauxPrestationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service métier pour le tableau de bord souscripteur Génère les statistiques
 * et indicateurs de performance
 *
 * @author JIATOU FRANCK
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final AdherentRepository adherentRepository;
    private final AyantDroitRepository ayantDroitRepository;
    private final ConsultationRepository consultationRepository;
    private final LignePrestationRepository lignePrestationRepository;
    private final TauxPrestationRepository tauxPrestationRepository;

    /**
     * Génère les statistiques complètes du tableau de bord
     *
     * @param codeSouscripteur Code du souscripteur
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @return Statistiques du tableau de bord
     */
    public DashboardStatisticsDTO generateDashboardStatistics(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin) {

        log.info("Génération des statistiques du tableau de bord pour le souscripteur: {}", codeSouscripteur);

        return DashboardStatisticsDTO.builder()
                .consommationGlobale(getConsommationGlobale(codeSouscripteur, dateDebut, dateFin))
                .statistiquesParPeriode(getStatistiquesParPeriode(codeSouscripteur, dateDebut, dateFin, "MOIS"))
                .statistiquesParTypePrestation(getStatistiquesParTypePrestation(codeSouscripteur, dateDebut, dateFin))
                .topPrestations(getTopPrestations(codeSouscripteur, dateDebut, dateFin, 5))
                .topPrestataires(getTopPrestataires(codeSouscripteur, dateDebut, dateFin, 5))
                .tauxUtilisationPlafond(getTauxUtilisationPlafond(codeSouscripteur, dateDebut, dateFin))
                .alertes(generateAlertes(codeSouscripteur, dateDebut, dateFin))
                .dateGeneration(LocalDateTime.now())
                .codeSouscripteur(codeSouscripteur)
                .periode(DashboardStatisticsDTO.PeriodeDTO.builder()
                        .dateDebut(dateDebut)
                        .dateFin(dateFin)
                        .type("PERSONNALISE")
                        .build())
                .build();
    }

    /**
     * Calcule la consommation globale
     */
    private DashboardStatisticsDTO.ConsommationGlobaleDTO getConsommationGlobale(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin) {

        log.debug("Calcul de la consommation globale");

        // Récupération des statistiques depuis la base
        StatistiquesGlobalesDTO stats = consultationRepository.getStatistiquesGlobales(
                codeSouscripteur, dateDebut, dateFin);

//       Long nombreConsultations = stats[0] != null ? (Long.parseLong(""+stats[0]) ) : 0L;
//        BigDecimal totalDepenses = stats[1] != null ? (BigDecimal) stats[1] : BigDecimal.ZERO;
//        BigDecimal totalRembourse = stats[2] != null ? (BigDecimal) stats[2] : BigDecimal.ZERO;
//        BigDecimal moyenneDepense = stats[3] != null ? (BigDecimal) stats[3] : BigDecimal.ZERO;
       
        Long nombreConsultations = stats.getNombreConsultations() != null ? stats.getNombreConsultations() : 0L;

        BigDecimal totalDepenses = stats.getTotalDepenses() != null ? stats.getTotalDepenses() : BigDecimal.ZERO;

        BigDecimal totalRembourse = stats.getTotalRembourse() != null ? stats.getTotalRembourse()  : BigDecimal.ZERO;

        BigDecimal moyenneDepense = stats.getMoyenneDepenses()!= null ? stats.getMoyenneDepenses(): BigDecimal.ZERO;

        // Calcul du total à charge
        BigDecimal totalACharge = totalDepenses.subtract(totalRembourse);

        // Nombre de prestations
        Long nombrePrestations = lignePrestationRepository.countPrestationsBySouscripteur(
                codeSouscripteur, dateDebut, dateFin);

        // Nombre d'assurés et ayants droit actifs
        Long nombreAssuresActifs = adherentRepository.countActiveAdherentsBySouscripteur(codeSouscripteur);
        Long nombreAyantsDroitsActifs = ayantDroitRepository.countActiveBySouscripteur(codeSouscripteur);

        // Calcul de la moyenne de dépense par assuré
        BigDecimal moyenneDepenseParAssure = BigDecimal.ZERO;
        if (nombreAssuresActifs > 0) {
            moyenneDepenseParAssure = totalDepenses.divide(
                    BigDecimal.valueOf(nombreAssuresActifs), 2, RoundingMode.HALF_UP);
        }

        // Calcul du taux de remboursement moyen
        BigDecimal tauxRemboursementMoyen = BigDecimal.ZERO;
        if (totalDepenses.compareTo(BigDecimal.ZERO) > 0) {
            tauxRemboursementMoyen = totalRembourse
                    .divide(totalDepenses, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return DashboardStatisticsDTO.ConsommationGlobaleDTO.builder()
                .totalDepenses(totalDepenses)
                .totalRembourse(totalRembourse)
                .totalACharge(totalACharge)
                .nombreConsultations(nombreConsultations.intValue())
                .nombrePrestations(nombrePrestations.intValue())
                .nombreAssuresActifs(nombreAssuresActifs.intValue())
                .nombreAyantsDroitsActifs(nombreAyantsDroitsActifs.intValue())
                .moyenneDepenseParAssure(moyenneDepenseParAssure)
                .tauxRemboursementMoyen(tauxRemboursementMoyen)
                .build();
    }

    /**
     * Calcule les statistiques par période
     */
    private List<DashboardStatisticsDTO.StatistiquePeriodeDTO> getStatistiquesParPeriode(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            String typePeriode) {

        log.debug("Calcul des statistiques par période: {}", typePeriode);

        String format;
        switch (typePeriode) {
            case "JOUR":
                format = "%Y-%m-%d";
                break;
            case "SEMAINE":
                format = "%Y-%u";
                break;
            case "ANNEE":
                format = "%Y";
                break;
            default: // MOIS
                format = "%Y-%m";
        }

        List<StatistiquesParPeriodeDTO> results = consultationRepository.getStatistiquesParPeriode(
                codeSouscripteur, dateDebut, dateFin, format);

        List<DashboardStatisticsDTO.StatistiquePeriodeDTO> statistiques = new ArrayList<>();

        for (StatistiquesParPeriodeDTO result : results) {
            String periode = result.getPeriode();
            Long nombreConsultations = result.getNombreConsultations();
            BigDecimal montantTotal = result.getTotalDepenses();
            BigDecimal montantRembourse = result.getTotalRembourse();

            statistiques.add(DashboardStatisticsDTO.StatistiquePeriodeDTO.builder()
                    .periode(typePeriode)
                    .libelle(periode)
                    .montantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO)
                    .montantRembourse(montantRembourse != null ? montantRembourse : BigDecimal.ZERO)
                    .nombreConsultations(nombreConsultations.intValue())
                    .build());
        }

        return statistiques;
    }

    /**
     * Calcule les statistiques par type de prestation
     */
    private List<DashboardStatisticsDTO.StatistiqueTypePrestationDTO> getStatistiquesParTypePrestation(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin) {

        log.debug("Calcul des statistiques par type de prestation");

        List<StatistiqueTypePrestationDTO> results = consultationRepository.getStatistiquesParTypePrestation(
                codeSouscripteur, dateDebut, dateFin);

        List<DashboardStatisticsDTO.StatistiqueTypePrestationDTO> statistiques = new ArrayList<>();
        BigDecimal totalGlobal = BigDecimal.ZERO;

        // Calcul du total global d'abord
        for (StatistiqueTypePrestationDTO result : results) {
            BigDecimal montant = result.getTotalDepenses();
            totalGlobal = totalGlobal.add(montant != null ? montant : BigDecimal.ZERO);
        }

        // Construction des statistiques avec pourcentages
        for (StatistiqueTypePrestationDTO result : results) {
            String typeId = ""+result.getTypeId();
            String typeNom = result.getNom();
            String categorie = result.getCategorie();
            Long nombreUtilisations = result.getNombreConsultations();
            BigDecimal montantTotal = result.getTotalDepenses();
            BigDecimal montantRembourse = result.getTotalRembourse();

            BigDecimal pourcentageTotal = BigDecimal.ZERO;
            if (totalGlobal.compareTo(BigDecimal.ZERO) > 0) {
                pourcentageTotal = montantTotal
                        .divide(totalGlobal, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            BigDecimal tauxRemboursementMoyen = BigDecimal.ZERO;
            if (montantTotal.compareTo(BigDecimal.ZERO) > 0) {
                tauxRemboursementMoyen = montantRembourse
                        .divide(montantTotal, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            statistiques.add(DashboardStatisticsDTO.StatistiqueTypePrestationDTO.builder()
                    .typeId(typeId)
                    .typeNom(typeNom)
                    .categorie(categorie)
                    .montantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO)
                    .montantRembourse(montantRembourse != null ? montantRembourse : BigDecimal.ZERO)
                    .nombreUtilisations(nombreUtilisations.intValue())
                    .pourcentageTotal(pourcentageTotal)
                    .tauxRemboursementMoyen(tauxRemboursementMoyen)
                    .build());
        }

        return statistiques;
    }

    /**
     * Récupère le top N des prestations les plus utilisées
     */
    private List<DashboardStatisticsDTO.TopPrestationDTO> getTopPrestations(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            int limit) {

        log.debug("Récupération du top {} des prestations", limit);

        List<TopPrestationDTO> results = consultationRepository.getTopPrestations(
                codeSouscripteur, dateDebut, dateFin);

        List<DashboardStatisticsDTO.TopPrestationDTO> topPrestations = new ArrayList<>();
        Long totalUtilisations = 0L;

        // Calcul du total des utilisations
        for (TopPrestationDTO result : results) {
            Long count = result.getNombreConsultations();
            totalUtilisations += count;
        }

        // Construction du top avec limit
        int rang = 1;
        for (int i = 0; i < Math.min(limit, results.size()); i++) {
            TopPrestationDTO result = results.get(i);
            String typeId = result.getTypeId();
            String typeNom =  result.getNom();
            String categorie = result.getCategorie();
            Long nombreUtilisations = result.getNombreConsultations();
            BigDecimal montantTotal = result.getTotalDepenses();

            BigDecimal pourcentageUtilisation = BigDecimal.ZERO;
            if (totalUtilisations > 0) {
                pourcentageUtilisation = BigDecimal.valueOf(nombreUtilisations)
                        .divide(BigDecimal.valueOf(totalUtilisations), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            topPrestations.add(DashboardStatisticsDTO.TopPrestationDTO.builder()
                    .rang(rang++)
                    .typeId(typeId)
                    .typenom(typeNom)
                    .categorie(categorie)
                    .nombreUtilisations(nombreUtilisations.intValue())
                    .montantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO)
                    .pourcentageUtilisation(pourcentageUtilisation)
                    .build());
        }

        return topPrestations;
    }

    /**
     * Récupère le top N des prestataires les plus sollicités
     */
    private List<DashboardStatisticsDTO.TopPrestataireDTO> getTopPrestataires(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            int limit) {

        log.debug("Récupération du top {} des prestataires", limit);

        List<PrestationPrestataireDTO> results = lignePrestationRepository.getPrestationsParPrestataire(
                codeSouscripteur, dateDebut, dateFin);

        List<DashboardStatisticsDTO.TopPrestataireDTO> topPrestataires = new ArrayList<>();
        Long totalVisites = 0L;

        // Calcul du total des visites
        for (PrestationPrestataireDTO result : results) {
            Long count = result.getNombreLignes();
            totalVisites += count;
        }

        // Construction du top avec limit
        int rang = 1;
        for (int i = 0; i < Math.min(limit, results.size()); i++) {
           PrestationPrestataireDTO result = results.get(i);
            String prestataireId = result.getPrestataireId();
            String nomPrestataire = result.getNomPrestataire();
            String categoriePrestataire = result.getCategorie();
            Long nombreVisites = result.getNombreLignes();
            BigDecimal montantTotal = result.getTotalMontant();

            BigDecimal pourcentageUtilisation = BigDecimal.ZERO;
            if (totalVisites > 0) {
                pourcentageUtilisation = BigDecimal.valueOf(nombreVisites)
                        .divide(BigDecimal.valueOf(totalVisites), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            topPrestataires.add(DashboardStatisticsDTO.TopPrestataireDTO.builder()
                    .rang(rang++)
                    .prestataireId(prestataireId)
                    .nomPrestataire(nomPrestataire)
                    .categoriePrestataire(categoriePrestataire)
                    .nombreVisites(nombreVisites.intValue())
                    .montantTotal(montantTotal != null ? montantTotal : BigDecimal.ZERO)
                    .pourcentageUtilisation(pourcentageUtilisation)
                    .build());
        }

        return topPrestataires;
    }

    /**
     * Calcule le taux d'utilisation des plafonds
     */
    private DashboardStatisticsDTO.TauxUtilisationPlafondDTO getTauxUtilisationPlafond(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin) {

        log.debug("Calcul du taux d'utilisation des plafonds");

        // TODO: Implémenter la logique en fonction de la structure de vos polices
        // Cette méthode nécessite de récupérer les plafonds depuis TauxPrestationRepository
        // et de les comparer avec les consommations réelles
        return DashboardStatisticsDTO.TauxUtilisationPlafondDTO.builder()
                .plafondGlobal(BigDecimal.ZERO)
                .montantUtilise(BigDecimal.ZERO)
                .montantRestant(BigDecimal.ZERO)
                .pourcentageUtilisation(BigDecimal.ZERO)
                .detailsParType(new ArrayList<>())
                .build();
    }

    /**
     * Génère les alertes
     */
    private List<DashboardStatisticsDTO.AlerteDTO> generateAlertes(
            String codeSouscripteur,
            LocalDateTime dateDebut,
            LocalDateTime dateFin) {

        log.debug("Génération des alertes");

        List<DashboardStatisticsDTO.AlerteDTO> alertes = new ArrayList<>();

        // Détection des consultations répétées
        List<Object[]> consultationsRepetees = consultationRepository.detectConsultationsRepetees(
                codeSouscripteur, dateDebut, dateFin, 5);

        for (Object[] result : consultationsRepetees) {
            String codeAssure = (String) result[0];
            Long nombreConsultations = (Long) result[1];

            alertes.add(DashboardStatisticsDTO.AlerteDTO.builder()
                    .type("VISITE_REPETEE")
                    .niveau("WARNING")
                    .message("Nombre élevé de consultations détecté")
                    .codeAssure(codeAssure)
                    .dateDetection(LocalDateTime.now())
                    .details(String.format("%d consultations sur la période", nombreConsultations))
                    .build());
        }

        return alertes;
    }
}
