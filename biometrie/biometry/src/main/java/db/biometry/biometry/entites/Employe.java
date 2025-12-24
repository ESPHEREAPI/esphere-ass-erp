/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entites;

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
import java.io.Serializable;
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
@Table(name = "dbx45ty_employe")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyEmploye.findAll", query = "SELECT d FROM Employe d"),
    @NamedQuery(name = "Dbx45tyEmploye.findById", query = "SELECT d FROM Employe d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyEmploye.findByConnexionAppli", query = "SELECT d FROM Employe d WHERE d.connexionAppli = :connexionAppli")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder                    // Permet d'utiliser le pattern Builder

public class Employe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "connexion_appli", nullable = false)
    private String connexionAppli;

    @ManyToOne
    @JoinColumn(name = "filiale_agence_id", referencedColumnName = "id")
    private FilialeAgence filialeAgenceId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prestataire_id", referencedColumnName = "id")
    private Prestataire prestataireId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profil_id", referencedColumnName = "id")
    private Profil profilId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "utilisateur_id", referencedColumnName = "id")
    private Utilisateur utilisateurId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeId")
    private List<LignePrestationAudit> dbx45tyLignePrestationAuditList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeId")
    private List<Dbx45tyConsultationAudit> dbx45tyConsultationAuditList;

    @OneToMany(mappedBy = "employeId")
    private List<Dbx45tyVisite> dbx45tyVisiteList;

    @OneToMany(mappedBy = "employeValideRejeteId")
    private List<Dbx45tyConsultation> dbx45tyConsultationList;

    @OneToMany(mappedBy = "employeValideRejeteId")
    private List<LignePrestation> dbx45tyLignePrestationList;
}


  

    

