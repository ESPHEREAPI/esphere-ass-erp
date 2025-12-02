/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.dtos.PrestationPrestataireDTO;
import db.biometry.biometry.entite.Dbx45tyLignePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour la gestion des lignes de prestation Requêtes pour le détail
 * des prestations médicales
 *
 * @author JIATOU FRANCK
 * @version 1.0
 */
@Repository

/**
 *
 * @author USER01
 */
public interface LignePrestationRepository extends JpaRepository<Dbx45tyLignePrestation, Integer> {

//    /*
//    lister les beneficiaires pour type de prestation examen
//     */
//    List<LignePrestation> findByCodeBeneficiaireIsNotNullAndCodeTypePrestationIsNotNull();
//
//    /*
//    lister les beneficiaires pour type de prestation Ordonance
//     */
//    List<LignePrestation> findByCodeBeneficiaireIsNotNullAndCodeTypePrestationIsNull();
//
//    /*
//    lister les adherents pour type de prestation Examen,
//     */
//    List<LignePrestation> findByCodeAssurePrincipalIsNotNullAndCodeTypePrestationIsNotNull();
//
//    /*
//    lister les adherents pour type de prestation Ordonance/paharmacie
//     */
//    List<LignePrestation> findByCodeAssurePrincipalIsNotNullAndCodeTypePrestationIsNull();
//
//    /*
//       on liste les ligne de prestation par prestataire, assure principal et date de facon distincte
//     */
//    @Query("SELECT DISTINCT l.codePrestataire,l.codeAssurePrincipal,DATE_FORMAT ( l.date , '%Y-%m-01' )  as date,l.codeTypePrestation FROM LignePrestation l ")
//    List<Object>
//            listePrestataireDistinct();
//
//    List<LignePrestation> findByCodePrestataire(String codePrestataire);
//
//    @Query("SELECT SUM (l.montant) FROM LignePrestation l WHERE l.codeAssurePrincipal= :ap and l.codePrestataire= :cp and DATE_FORMAT ( l.date , '%Y-%m-01' )= DATE_FORMAT ( :date , '%Y-%m-01' ) and l.codeTypePrestation= :tp")
//    public Double sommeLignePrestation(@Param(value = "ap") String ap, @Param(value = "cp") String cp, @Param(value = "date") Date date, @Param(value = "tp") String tp);
//
//    @Query("SELECT DISTINCT (l.idPrestation) FROM LignePrestation l")
//    public List<Integer> listesPrestionId();
//    public List<LignePrestation> findByIdPrestation(Integer idPrestation);
    /**
     * Liste des prestations par consultation
     */
    @Query("SELECT lp FROM Dbx45tyLignePrestation lp "
            + "WHERE lp.prestationId.id = :prestationId "
            + "AND lp.supprime = 'N' "
            + "ORDER BY lp.date DESC")
    List<Dbx45tyLignePrestation> findByPrestationId(@Param("prestationId") Integer prestationId);

    /**
     * Statistiques des prestations par type
     */
    @Query("SELECT "
            + "COALESCE(lp.typeExamen.id, lp.descriptionSoins.id, lp.medicamentId.nom), "
            + "COALESCE(lp.typeExamen.nom, lp.descriptionSoins.nom, lp.medicamentId.nom), "
            + "COUNT(lp), "
            + "COALESCE(SUM(lp.valeur * lp.nbre), 0), "
            + "COALESCE(SUM(CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END * CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END), 0) "
            + "FROM Dbx45tyLignePrestation lp "
            + "JOIN lp.prestationId p "
            + "JOIN p.visiteId v "
            + "JOIN v.codeAdherent benef "
            + "WHERE benef.souscripteur = :souscripteur "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY COALESCE(lp.typeExamen.id, lp.descriptionSoins.id, lp.medicamentId.nom), "
            + "COALESCE(lp.typeExamen.nom, lp.descriptionSoins.nom, lp.medicamentId.nom) "
            + "ORDER BY SUM(lp.valeur * lp.nbre) DESC")
    List<Object[]> getStatistiquesParType(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Top prestations par fréquence d'utilisation
     */
    @Query("SELECT "
            + "COALESCE(lp.typeExamen.nom, lp.descriptionSoins.nom, lp.medicamentId.nom), "
            + "COUNT(lp), "
            + "COALESCE(SUM(lp.valeur * lp.nbre), 0) "
            + "FROM Dbx45tyLignePrestation lp "
            + "JOIN lp.prestationId p "
            + "JOIN p.visiteId v "
            + "JOIN v.codeAdherent benef "
            + "WHERE benef.souscripteur = :souscripteur "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY COALESCE(lp.typeExamen.nom, lp.descriptionSoins.nom, lp.medicamentId.nom) "
            + "ORDER BY COUNT(lp) DESC")
    List<Object[]> getTopPrestationsParUtilisation(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Montant total des prestations pour un adhérent
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END * "
            + "CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END * (lp.taux / 100)), 0) "
            + "FROM Dbx45tyLignePrestation lp "
            + "JOIN lp.prestationId p "
            + "JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND lp.etat = 'encaisse' "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getMontantRemboursePourAdherent(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Nombre total de prestations pour un souscripteur
     */
    @Query("SELECT COUNT(lp) FROM Dbx45tyLignePrestation lp  where lp.prestationId.visiteId.codeAdherent.souscripteur= :souscripteur AND lp.date BETWEEN :dateDebut AND :dateFin")

    Long countPrestationsBySouscripteur(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Prestations par prestataire
     */
    @Query("SELECT new db.biometry.biometry.dtos.PrestationPrestataireDTO("
            + "lp.prestataireId.id, "
            + "lp.prestataireId.nom, "
            + "lp.prestataireId.categorieId.nom, "
            + "COUNT(lp), "
            + "COALESCE(SUM(lp.valeur * lp.nbre), 0)) "
            + "FROM Dbx45tyLignePrestation lp "
            + "WHERE lp.prestationId.visiteId.codeAdherent.souscripteur = :souscripteur "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY lp.prestataireId.id,lp.prestataireId.nom, lp.prestataireId.categorieId.nom "
            + "ORDER BY SUM(lp.valeur * lp.nbre) DESC")
    List<PrestationPrestataireDTO> getPrestationsParPrestataire(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    @Query("select lp FROM Dbx45tyLignePrestation lp WHERE lp.prestationId.visiteId.codeAdherent.codeAdherent= :codeAdherent and lp.date BETWEEN :dateDebut and  :dateFin")
    List<Dbx45tyLignePrestation> listeDeatailPrestation(@Param("codeAdherent") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
}
