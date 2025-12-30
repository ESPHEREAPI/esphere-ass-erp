/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repository;

import db.biometry.biometry.entites.LignePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des lignes de prestation Gère les ordonnances
 * (medicamentId != null) et les examens (examenId != null)
 *
 * @author USER01
 */
@Repository
public interface LignePrestationRepository extends JpaRepository<LignePrestation, Integer> {

    // ==================== MÉTHODES POUR LES ORDONNANCES ====================
    /**
     * Compte les ordonnances (lignes avec medicamentId non null) par état
     *
     * @param etat État de l'ordonnance
     * @param supprime Statut de suppression
     * @return Nombre d'ordonnances correspondant aux critères
     */
    Long countByMedicamentIdIsNotNullAndEtatAndSupprime(String etat, String supprime);

    /**
     * Compte les ordonnances pour une année, un mois et un état donnés
     *
     * @param annee Année de l'ordonnance
     * @param mois Mois de l'ordonnance (1-12)
     * @param etat État de l'ordonnance
     * @return Nombre d'ordonnances pour la période spécifiée
     */
    @Query("SELECT COUNT(lp) FROM LignePrestation lp "
            + "WHERE lp.prestationId.naturePrestation= :naturePrestation "
            + "AND YEAR(lp.dateEncaisse) = :annee "
            + "AND MONTH(lp.dateEncaisse) = :mois "
            + "AND lp.etat = :etat "
            + "AND lp.supprime = :supprime")
    Long countOrdonnancesByAnneeAndMoisAndEtat(
            @Param("annee") Integer annee,
            @Param("mois") Integer mois,
            @Param("etat") String etat,
            @Param("naturePrestation") String naturePrestation,
            @Param("supprime") String supprime
    );

    /**
     * Calcule la somme des montants des ordonnances pour une période donnée
     * Utilise valeurModif si présent, sinon valeur, multiplié par nbreModif ou
     * nbre
     *
     * @param annee Année de l'ordonnance
     * @param mois Mois de l'ordonnance
     * @param etat État de l'ordonnance
     * @return Somme des montants des ordonnances
     */
    @Query("SELECT COALESCE(SUM("
            + "CASE WHEN lp.valeurModif is not null THEN lp.valeurModif ELSE lp.valeur END * "
            + "CASE WHEN lp.nbreModif is not null THEN lp.nbreModif ELSE lp.nbre END"
            + "), 0) "
            + "FROM LignePrestation lp "
            + "WHERE lp.prestationId.naturePrestation= :naturePrestation "
            + "AND YEAR(lp.dateEncaisse) = :annee "
            + "AND MONTH(lp.dateEncaisse) = :mois "
            + "AND lp.etat = :etat "
            + "AND lp.supprime = :supprime")
    Double sumMontantOrdonnancesByAnneeAndMoisAndEtat(
            @Param("annee") Integer annee,
            @Param("mois") Integer mois,
            @Param("etat") String etat,
            @Param("naturePrestation") String naturePrestation,
            @Param("supprime") String supprime
    );

    // ==================== MÉTHODES POUR LES EXAMENS ====================
    /**
     * Compte les examens (lignes avec examenId non null) par état
     *
     * @param etat État de l'examen
     * @param supprime Statut de suppression
     * @return Nombre d'examens correspondant aux critères
     */
    Long countByMedicamentIdIsNullAndEtatAndSupprime(String etat, String supprime);

    /**
     * Compte les examens pour une année, un mois et un état donnés
     *
     * @param annee Année de l'examen
     * @param mois Mois de l'examen (1-12)
     * @param etat État de l'examen
     * @return Nombre d'examens pour la période spécifiée
     */
    @Query("SELECT COUNT(lp) FROM LignePrestation lp "
            + "WHERE lp.prestationId.naturePrestation= :naturePrestation "
            + "AND lp.dateEncaisse is not null "
            + "AND YEAR(lp.dateEncaisse) = :annee "
            + "AND MONTH(lp.dateEncaisse) = :mois "
            + "AND lp.etat = :etat "
            + "AND lp.supprime = :supprime")
    Long countExamensByAnneeAndMoisAndEtat(
            @Param("annee") Integer annee,
            @Param("mois") Integer mois,
            @Param("etat") String etat,
              @Param("naturePrestation") String naturePrestation,
            @Param("supprime") String supprime
    );

    /**
     * Calcule la somme des montants des examens pour une période donnée Utilise
     * valeurModif si présent, sinon valeur, multiplié par nbreModif ou nbre
     *
     * @param annee Année de l'examen
     * @param mois Mois de l'examen
     * @param etat État de l'examen
     * @return Somme des montants des examens
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN lp.valeurModif is not null THEN lp.valeurModif ELSE lp.valeur END * "
            + "CASE WHEN lp.nbreModif is not null THEN lp.nbreModif ELSE lp.nbre END), 0) "
            + "FROM LignePrestation lp "
            + "WHERE lp.prestationId.naturePrestation= :naturePrestation "
            + "AND lp.dateEncaisse is not null "
            + // Espace ajouté ici
            "AND YEAR(lp.dateEncaisse) = :annee "
            + "AND MONTH(lp.dateEncaisse) = :mois "
            + "AND lp.etat = :etat "
            + "AND lp.supprime = :supprime")
    Double sumMontantExamensByAnneeAndMoisAndEtat(
            @Param("annee") Integer annee,
            @Param("mois") Integer mois,
            @Param("etat") String etat,
             @Param("naturePrestation") String naturePrestation,
            @Param("supprime") String supprime
    );

    // ==================== MÉTHODES GÉNÉRALES ====================
    /**
     * Compte toutes les lignes de prestation par état (Ordonnances + Examens +
     * Autres)
     *
     * @param etat État de la prestation
     * @param supprime Statut de suppression
     * @return Nombre total de prestations
     */
    Long countByEtatAndSupprime(String etat, String supprime);

    /**
     * Recherche les lignes de prestation par type d'examen
     *
     * @param typeExamenId ID du type d'examen
     * @param supprime Statut de suppression
     * @return Nombre de prestations du type spécifié
     */
    @Query("SELECT COUNT(lp) FROM LignePrestation lp "
            + "WHERE lp.typeExamen.id = :typeId "
            + "AND lp.supprime = :supprime")
    Long countByTypeExamen(
            @Param("typeId") Integer typeExamenId,
            @Param("supprime") String supprime
    );

    /**
     * Compte les ordonnances pour une année complète
     *
     * @param annee Année
     * @param etat État
     * @return Nombre d'ordonnances pour l'année
     */
    @Query("SELECT COUNT(lp) FROM LignePrestation lp "
            + "WHERE lp.prestationId.naturePrestation= :naturePrestation "
            + "AND lp.dateEncaisse is not null "
            + "AND YEAR(lp.dateEncaisse) = :annee "
            + "AND lp.etat = :etat "
            + "AND lp.supprime = :supprime")
    Long countOrdonnancesByAnneeAndEtat(
            @Param("annee") Integer annee,
            @Param("etat") String etat,
            @Param("naturePrestation") String naturePrestation,
            @Param("supprime") String supprime
    );

    /**
     * Compte les examens pour une année complète
     *
     * @param annee Année
     * @param etat État
     * @return Nombre d'examens pour l'année
     */
    @Query("SELECT COUNT(lp) FROM LignePrestation lp "
            + "WHERE lp.prestationId.naturePrestation= :naturePrestation "
            + "AND lp.dateEncaisse is not null "
            + "AND YEAR(lp.dateEncaisse) = :annee "
            + "AND lp.etat = :etat "
            + "AND lp.supprime = :supprime")
    Long countExamensByAnneeAndEtat(
            @Param("annee") Integer annee,
            @Param("etat") String etat,
              @Param("naturePrestation") String naturePrestation,
            @Param("supprime") String supprime
    );

    // ==================== MÉTHODES DE COMPTAGE SIMPLES (AUTO-GÉNÉRÉES) ====================
    /**
     * Compte tous les examens dans la base (sans filtre) Méthode générée
     * automatiquement par Spring Data JPA
     *
     * @return Nombre total d'examens (examenId IS NOT NULL)
     */
}
