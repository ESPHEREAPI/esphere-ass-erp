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
@Table(name = "dbx45ty_ligne_prestation")
@NamedQueries({
    @NamedQuery(name = "LignePrestation.findAll", query = "SELECT d FROM LignePrestation d"),
    @NamedQuery(name = "LignePrestation.findById", query = "SELECT d FROM LignePrestation d WHERE d.id = :id"),
    @NamedQuery(name = "LignePrestation.findByTaux", query = "SELECT d FROM LignePrestation d WHERE d.taux = :taux"),
    @NamedQuery(name = "LignePrestation.findByDentsConcernees", query = "SELECT d FROM LignePrestation d WHERE d.dentsConcernees = :dentsConcernees"),
    @NamedQuery(name = "LignePrestation.findByCodification", query = "SELECT d FROM LignePrestation d WHERE d.codification = :codification"),
    @NamedQuery(name = "LignePrestation.findByNom", query = "SELECT d FROM LignePrestation d WHERE d.nom = :nom"),
    @NamedQuery(name = "LignePrestation.findByValeur", query = "SELECT d FROM LignePrestation d WHERE d.valeur = :valeur"),
    @NamedQuery(name = "LignePrestation.findByNbre", query = "SELECT d FROM LignePrestation d WHERE d.nbre = :nbre"),
    @NamedQuery(name = "LignePrestation.findByActePrelevement", query = "SELECT d FROM LignePrestation d WHERE d.actePrelevement = :actePrelevement"),
    @NamedQuery(name = "LignePrestation.findByValeurModif", query = "SELECT d FROM LignePrestation d WHERE d.valeurModif = :valeurModif"),
    @NamedQuery(name = "LignePrestation.findByNbreModif", query = "SELECT d FROM LignePrestation d WHERE d.nbreModif = :nbreModif"),
    @NamedQuery(name = "LignePrestation.findByActePrelevementModif", query = "SELECT d FROM LignePrestation d WHERE d.actePrelevementModif = :actePrelevementModif"),
    @NamedQuery(name = "LignePrestation.findByPosologie", query = "SELECT d FROM LignePrestation d WHERE d.posologie = :posologie"),
    @NamedQuery(name = "LignePrestation.findByObservations", query = "SELECT d FROM LignePrestation d WHERE d.observations = :observations"),
    @NamedQuery(name = "LignePrestation.findByObservationsActePrelevement", query = "SELECT d FROM LignePrestation d WHERE d.observationsActePrelevement = :observationsActePrelevement"),
    @NamedQuery(name = "LignePrestation.findByDate", query = "SELECT d FROM LignePrestation d WHERE d.date = :date"),
    @NamedQuery(name = "LignePrestation.findByDateValideRejete", query = "SELECT d FROM LignePrestation d WHERE d.dateValideRejete = :dateValideRejete"),
    @NamedQuery(name = "LignePrestation.findByDateEncaisse", query = "SELECT d FROM LignePrestation d WHERE d.dateEncaisse = :dateEncaisse"),
    @NamedQuery(name = "LignePrestation.findByEtat", query = "SELECT d FROM LignePrestation d WHERE d.etat = :etat"),
    @NamedQuery(name = "LignePrestation.findBySupprime", query = "SELECT d FROM LignePrestation d WHERE d.supprime = :supprime")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder 
public class LignePrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "dents_concernees")
    private String dentsConcernees;
    @Column(name = "codification")
    private String codification;
    @Column(name = "nom")
    private String nom;
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "nbre")
    private Double nbre;
    @Basic(optional = false)
    @Column(name = "acte_prelevement")
    private double actePrelevement;
    @Column(name = "valeur_modif")
    private Double valeurModif;
    @Column(name = "nbre_modif")
    private Double nbreModif;
    @Basic(optional = false)
    @Column(name = "acte_prelevement_modif")
    private double actePrelevementModif;
    @Column(name = "posologie")
    private String posologie;
    @Column(name = "observations")
    private String observations;
    @Column(name = "observations_acte_prelevement")
    private String observationsActePrelevement;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "date_valide_rejete")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValideRejete;
    @Column(name = "date_encaisse")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEncaisse;
    @Basic(optional = false)
    @Column(name = "etat")
    private String etat;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lignePrestationId")
    private List<LignePrestationAudit> dbx45tyLignePrestationAuditList;
    @JoinColumn(name = "employe_valide_rejete_id", referencedColumnName = "id")
    @ManyToOne
    private Employe employeValideRejeteId;
    @JoinColumn(name = "examen_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyExamen examenId;
    @JoinColumn(name = "medicament_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyMedicament medicamentId;
    @JoinColumn(name = "prestataire_id", referencedColumnName = "id")
    @ManyToOne
    private Prestataire prestataireId;
    @JoinColumn(name = "prestation_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyPrestation prestationId;
    @JoinColumn(name = "type_examen", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyTypePrestation typeExamen;
    @JoinColumn(name = "description_soins", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyTypePrestation descriptionSoins;

}