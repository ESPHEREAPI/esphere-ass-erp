/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entites;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "dbx45ty_utilisateur")
@NamedQueries({
    @NamedQuery(name = "Utilisateur.findAll", query = "SELECT d FROM Utilisateur d"),
    @NamedQuery(name = "Utilisateur.findById", query = "SELECT d FROM Utilisateur d WHERE d.id = :id"),
    @NamedQuery(name = "Utilisateur.findByGenre", query = "SELECT d FROM Utilisateur d WHERE d.genre = :genre"),
    @NamedQuery(name = "Utilisateur.findByType", query = "SELECT d FROM Utilisateur d WHERE d.type = :type"),
    @NamedQuery(name = "Utilisateur.findByNom", query = "SELECT d FROM Utilisateur d WHERE d.nom = :nom"),
    @NamedQuery(name = "Utilisateur.findByPrenom", query = "SELECT d FROM Utilisateur d WHERE d.prenom = :prenom"),
    @NamedQuery(name = "Utilisateur.findByDateNaissance", query = "SELECT d FROM Utilisateur d WHERE d.dateNaissance = :dateNaissance"),
    @NamedQuery(name = "Utilisateur.findByLieuNaissance", query = "SELECT d FROM Utilisateur d WHERE d.lieuNaissance = :lieuNaissance"),
    @NamedQuery(name = "Utilisateur.findByTelephone", query = "SELECT d FROM Utilisateur d WHERE d.telephone = :telephone"),
    @NamedQuery(name = "Utilisateur.findByTelephoneIso2", query = "SELECT d FROM Utilisateur d WHERE d.telephoneIso2 = :telephoneIso2"),
    @NamedQuery(name = "Utilisateur.findByTelephoneDialCode", query = "SELECT d FROM Utilisateur d WHERE d.telephoneDialCode = :telephoneDialCode"),
    @NamedQuery(name = "Utilisateur.findByEmail", query = "SELECT d FROM Utilisateur d WHERE d.email = :email"),
    @NamedQuery(name = "Utilisateur.findByLogin", query = "SELECT d FROM Utilisateur d WHERE d.login = :login"),
    @NamedQuery(name = "Utilisateur.findByMotPasse", query = "SELECT d FROM Utilisateur d WHERE d.motPasse = :motPasse"),
    @NamedQuery(name = "Utilisateur.findByStatut", query = "SELECT d FROM Utilisateur d WHERE d.statut = :statut"),
    @NamedQuery(name = "Utilisateur.findBySupprime", query = "SELECT d FROM Utilisateur d WHERE d.supprime = :supprime"),
    @NamedQuery(name = "Utilisateur.findByNewsletter", query = "SELECT d FROM Utilisateur d WHERE d.newsletter = :newsletter"),
    @NamedQuery(name = "Utilisateur.findByDateCreation", query = "SELECT d FROM Utilisateur d WHERE d.dateCreation = :dateCreation"),
    @NamedQuery(name = "Utilisateur.findByOauthProvider", query = "SELECT d FROM Utilisateur d WHERE d.oauthProvider = :oauthProvider"),
    @NamedQuery(name = "Utilisateur.findByOauthUid", query = "SELECT d FROM Utilisateur d WHERE d.oauthUid = :oauthUid"),
    @NamedQuery(name = "Utilisateur.findByLocalisation", query = "SELECT d FROM Utilisateur d WHERE d.localisation = :localisation"),
    @NamedQuery(name = "Utilisateur.findByActivite", query = "SELECT d FROM Utilisateur d WHERE d.activite = :activite"),
    @NamedQuery(name = "Utilisateur.findBySituationMatrimoniale", query = "SELECT d FROM Utilisateur d WHERE d.situationMatrimoniale = :situationMatrimoniale")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder  
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "genre")
    private String genre;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @Column(name = "lieu_naissance")
    private String lieuNaissance;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "telephone_iso2")
    private String telephoneIso2;
    @Column(name = "telephone_dial_code")
    private Integer telephoneDialCode;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "login")
    private String login;
    @Column(name = "mot_passe")
    private String motPasse;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @Basic(optional = false)
    @Column(name = "newsletter")
    private String newsletter;
    @Basic(optional = false)
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Column(name = "oauth_provider")
    private String oauthProvider;
    @Column(name = "oauth_uid")
    private String oauthUid;
    @Column(name = "localisation")
    private String localisation;
    @Column(name = "activite")
    private String activite;
    @Column(name = "situation_matrimoniale")
    private String situationMatrimoniale;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilisateurId")
    private List<Employe> dbx45tyEmployeList;
    @JoinColumn(name = "langue_defaut", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyLangue langueDefaut;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilisateurId")
    private List<Dbx45tyInternaute> dbx45tyInternauteList;
    private Boolean actif;

    
}
