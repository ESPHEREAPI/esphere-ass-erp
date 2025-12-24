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
@Table(name = "dbx45ty_agence")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyAgence.findAll", query = "SELECT d FROM Dbx45tyAgence d"),
    @NamedQuery(name = "Dbx45tyAgence.findById", query = "SELECT d FROM Dbx45tyAgence d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyAgence.findByNom", query = "SELECT d FROM Dbx45tyAgence d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyAgence.findByCode", query = "SELECT d FROM Dbx45tyAgence d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyAgence.findByDateCreation", query = "SELECT d FROM Dbx45tyAgence d WHERE d.dateCreation = :dateCreation"),
    @NamedQuery(name = "Dbx45tyAgence.findBySiteWeb", query = "SELECT d FROM Dbx45tyAgence d WHERE d.siteWeb = :siteWeb"),
    @NamedQuery(name = "Dbx45tyAgence.findByStatut", query = "SELECT d FROM Dbx45tyAgence d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyAgence.findBySupprime", query = "SELECT d FROM Dbx45tyAgence d WHERE d.supprime = :supprime")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder 
public class Dbx45tyAgence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    @Column(name = "site_web")
    private String siteWeb;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agenceId")
    private List<FilialeAgence> dbx45tyFilialeAgenceList;
    @JoinColumn(name = "siege_social_id", referencedColumnName = "id")
    @ManyToOne
    private FilialeAgence siegeSocialId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agenceId")
    private List<Dbx45tyAgenceLangue> dbx45tyAgenceLangueList;

}
