/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entites;

import jakarta.persistence.Basic;
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
@Table(name = "dbx45ty_medicament")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyMedicament.findAll", query = "SELECT d FROM Dbx45tyMedicament d"),
    @NamedQuery(name = "Dbx45tyMedicament.findById", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyMedicament.findByCode", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyMedicament.findByNom", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyMedicament.findByOrigine", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.origine = :origine"),
    @NamedQuery(name = "Dbx45tyMedicament.findByPrix", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.prix = :prix"),
    @NamedQuery(name = "Dbx45tyMedicament.findByQuantite", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.quantite = :quantite"),
    @NamedQuery(name = "Dbx45tyMedicament.findByCategorie", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.categorie = :categorie"),
    @NamedQuery(name = "Dbx45tyMedicament.findByStatut", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyMedicament.findBySupprime", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.supprime = :supprime")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder                    // Permet d'utiliser le pattern Builder
public class Dbx45tyMedicament implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Column(name = "origine")
    private String origine;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix")
    private Double prix;
    @Column(name = "quantite")
    private Double quantite;
    @Basic(optional = false)
    @Column(name = "categorie")
    private String categorie;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @JoinColumn(name = "prestataire_id", referencedColumnName = "id")
    @ManyToOne
    private Prestataire prestataireId;
    @OneToMany(mappedBy = "medicamentId")
    private List<LignePrestation> dbx45tyLignePrestationList;

  
    
}
