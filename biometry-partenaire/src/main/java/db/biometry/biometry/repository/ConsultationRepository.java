/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repository;

import db.biometry.biometry.entites.Dbx45tyConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER01
 */
@Repository
public interface ConsultationRepository extends JpaRepository<Dbx45tyConsultation, Integer>{
    /**
     * Compte le nombre de consultations par état et statut de suppression
     * 
     * @param etatConsultation État de la consultation (EN_ATTENTE, ENCAISSE, etc.)
     * @param supprime Statut de suppression ("OUI" ou "NON")
     * @return Nombre de consultations correspondant aux critères
     */
    Long countByEtatConsultationAndSupprime(String etatConsultation, String supprime);

    /**
     * Compte le nombre de consultations pour une année, un mois et un état donnés
     * Utilise YEAR() et MONTH() pour extraire l'année et le mois de la date
     * 
     * @param annee Année de la consultation
     * @param mois Mois de la consultation (1-12)
     * @param etat État de la consultation
     * @return Nombre de consultations pour la période et l'état spécifiés
     */
    @Query("SELECT COUNT(c) FROM Dbx45tyConsultation c " +
           "WHERE YEAR(c.date) = :annee " +
           "AND MONTH(c.date) = :mois " +
           "AND c.etatConsultation = :etat " +
           "AND c.supprime = :supprime")
    Long countByAnneeAndMoisAndEtat(
        @Param("annee") Integer annee,
        @Param("mois") Integer mois,
        @Param("etat") String etat,
        @Param("supprime") String supprime
    );

    /**
     * Calcule la somme des montants des consultations pour une période et un état donnés
     * Utilise le champ montantModif si présent, sinon montant
     * 
     * @param annee Année de la consultation
     * @param mois Mois de la consultation (1-12)
     * @param etat État de la consultation
     * @return Somme des montants ou 0 si aucune consultation
     */
    @Query("SELECT COALESCE(SUM(CASE " +
           "WHEN c.montantModif IS NOT NULL THEN c.montantModif " +
           "ELSE c.montant END), 0) " +
           "FROM Dbx45tyConsultation c " +
           "WHERE YEAR(c.date) = :annee " +
           "AND MONTH(c.date) = :mois " +
           "AND c.etatConsultation = :etat " +
           "AND c.supprime = :supprime")
    Double sumMontantByAnneeAndMoisAndEtat(
        @Param("annee") Integer annee,
        @Param("mois") Integer mois,
        @Param("etat") String etat,
          @Param("supprime") String supprime
    );

    /**
     * Compte le nombre de consultations pour une année et un état donnés
     * Utile pour les statistiques annuelles
     * 
     * @param annee Année de la consultation
     * @param etat État de la consultation
     * @return Nombre de consultations pour l'année et l'état spécifiés
     */
    @Query("SELECT COUNT(c) FROM Dbx45tyConsultation c " +
           "WHERE YEAR(c.date) = :annee " +
           "AND c.etatConsultation = :etat " +
           "AND c.supprime = :supprime")
    Long countByAnneeAndEtat(
        @Param("annee") Integer annee,
        @Param("etat") String etat,
        @Param("supprime") String supprime
    );

    /**
     * Recherche les consultations par type de prestation
     * 
     * @param typeConsultationId ID du type de consultation
     * @param supprime Statut de suppression
     * @return Nombre de consultations du type spécifié
     */
    @Query("SELECT COUNT(c) FROM Dbx45tyConsultation c " +
           "WHERE c.typeConsultation.id = :typeId " +
           "AND c.supprime = :supprime")
    Long countByTypeConsultation(
        @Param("typeId") Integer typeConsultationId,
        @Param("supprime") String supprime
    );
    
}
