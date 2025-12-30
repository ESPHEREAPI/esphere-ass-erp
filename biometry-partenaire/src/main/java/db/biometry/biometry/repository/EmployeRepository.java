/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repository;


import db.biometry.biometry.entites.Employe;
import db.biometry.biometry.entites.FilialeAgence;
import db.biometry.biometry.entites.Prestataire;
import db.biometry.biometry.entites.Profil;
import db.biometry.biometry.entites.Utilisateur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER01
 */
@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer>{
   // ==================== REQUETES DERIVEES ====================
    
    /**
     * Recherche un employé par son identifiant de connexion application
     */
    Optional<Employe> findByConnexionAppli(String connexionAppli);
    
    /**
     * Vérifie si un employé existe avec cet identifiant de connexion
     */
    boolean existsByConnexionAppli(String connexionAppli);
    
    /**
     * Recherche les employés par prestataire
     */
    List<Employe> findByPrestataireId(Prestataire prestataire);
    
    /**
     * Recherche les employés par filiale/agence
     */
    List<Employe> findByFilialeAgenceId(FilialeAgence filialeAgence);
    
    /**
     * Recherche les employés par profil
     */
    List<Employe> findByProfilId(Profil profil);
    
    /**
     * Recherche un employé par utilisateur
     */
    Optional<Employe> findByUtilisateurId(Utilisateur utilisateur);

    // ==================== REQUETES JPQL PERSONNALISEES ====================
    
    /**
     * Recherche les employés par nom de prestataire
     */
    @Query("SELECT e FROM Employe e WHERE e.prestataireId.nom = :nomPrestataire")
    List<Employe> findByPrestataireNom(@Param("nomPrestataire") String nomPrestataire);
    
    /**
     * Recherche les employés par nom de profil
     */
    @Query("SELECT e FROM Employe e WHERE e.profilId.typeProfil = :libelleProfil")
    List<Employe> findByProfilLibelle(@Param("libelleProfil") String libelleProfil);
    
    /**
     * Recherche les employés actifs (ayant un utilisateur actif)
     */
    @Query("SELECT e FROM Employe e WHERE e.utilisateurId.actif = true")
    List<Employe> findEmployesActifs();
    
    /**
     * Recherche les employés inactifs
     */
    @Query("SELECT e FROM Employe e WHERE e.utilisateurId.actif = false")
    List<Employe> findEmployesInactifs();
    
    /**
     * Recherche un employé par le login de l'utilisateur
     * @param login
     * @param login
     * @param login
     * @return 
     */
    @Query("SELECT e FROM Employe e WHERE e.utilisateurId.login = :login")
    Optional<Employe> findByUtilisateurLogin(@Param("login") String login);
    
    /**
     * Recherche les employés par email de l'utilisateur
     */
    @Query("SELECT e FROM Employe e WHERE e.utilisateurId.email = :email")
    Optional<Employe> findByUtilisateurEmail(@Param("email") String email);
    
    /**
     * Recherche les employés par nom et prénom de l'utilisateur
     */
    @Query("SELECT e FROM Employe e WHERE e.utilisateurId.nom = :nom AND e.utilisateurId.prenom = :prenom")
    List<Employe> findByUtilisateurNomAndPrenom(@Param("nom") String nom, @Param("prenom") String prenom);
    
    /**
     * Compte les employés par prestataire
     */
    @Query("SELECT COUNT(e) FROM Employe e WHERE e.prestataireId = :prestataire")
    Long countByPrestataire(@Param("prestataire") Prestataire prestataire);
    
    /**
     * Compte les employés par profil
     */
    @Query("SELECT COUNT(e) FROM Employe e WHERE e.profilId = :profil")
    Long countByProfil(@Param("profil") Profil profil);
    
    /**
     * Recherche les employés ayant effectué des consultations
     */
//    @Query("SELECT DISTINCT e FROM Employe e WHERE SIZE(e.ConsultationList) > 0")
//    List<Employe> findEmployesAvecConsultations();
    
    /**
     * Recherche les employés ayant effectué des visites
     */
//    @Query("SELECT DISTINCT e FROM Employe e WHERE SIZE(e.VisiteList) > 0")
//    List<Employe> findEmployesAvecVisites();
    
    /**
     * Recherche les employés par région/ville de la filiale
     */
    @Query("SELECT e FROM Employe e WHERE e.filialeAgenceId.villeId = :ville")
    List<Employe> findByFilialeVille(@Param("ville") long ville);
    
    /**
     * Recherche les employés d'une filiale avec un profil spécifique
     */
    @Query("SELECT e FROM Employe e WHERE e.filialeAgenceId = :filiale AND e.profilId = :profil")
    List<Employe> findByFilialeAndProfil(@Param("filiale") FilialeAgence filiale, 
                                        @Param("profil") Profil profil);

    // ==================== REQUETES NATIVES SQL ====================
    
    /**
     * Statistiques des employés par prestataire (requête native)
     */
    @Query(value = "SELECT p.nom as prestataire_nom, COUNT(e.id) as nombre_employes " +
                   "FROM Examen_employe e " +
                   "JOIN Examen_prestataire p ON e.prestataire_id = p.id " +
                   "GROUP BY p.id, p.nom " +
                   "ORDER BY nombre_employes DESC", 
           nativeQuery = true)
    List<Object[]> getStatistiquesEmployesParPrestataire();
    
    /**
     * Recherche avancée avec filtres multiples
     * @param prestataireId
     * @param profilId
     * @param filialeId
     * @param actif
     * @return 
     */
    @Query("SELECT e FROM Employe e WHERE " +
           "(:prestataireId IS NULL OR e.prestataireId.id = :prestataireId) AND " +
           "(:profilId IS NULL OR e.profilId.id = :profilId) AND " +
           "(:filialeId IS NULL OR e.filialeAgenceId.id = :filialeId) AND " +
           "(:actif IS NULL OR e.utilisateurId.actif = :actif)")
    List<Employe> findWithFilters(@Param("prestataireId") Integer prestataireId,
                                 @Param("profilId") Integer profilId,
                                 @Param("filialeId") Integer filialeId,
                                 @Param("actif") Boolean actif);
    
    /**
     * Recherche textuelle dans les informations de l'employé
     * @param searchTerm
     * @return 
     */
    @Query("SELECT e FROM Employe e WHERE " +
           "LOWER(e.connexionAppli) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.utilisateurId.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.utilisateurId.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.utilisateurId.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employe> searchEmployes(@Param("searchTerm") String searchTerm);
    
    /**
     * Employés ayant validé ou rejeté des consultations récemment
     * @param dateDebut
     * @return 
     */
//    @Query("SELECT DISTINCT e FROM Employe e " +
//           "JOIN e.ConsultationList c " +
//           "WHERE c.dateValidationRejet >= :dateDebut")
//    List<Employe> findEmployesValidateursRecents(@Param("dateDebut") java.time.LocalDateTime dateDebut);
//  
    
}
