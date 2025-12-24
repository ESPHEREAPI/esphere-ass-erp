/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entites;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "dbx45ty_prestataire")
@NamedQueries({
    @NamedQuery(name = "Prestataire.findAll", query = "SELECT d FROM Prestataire d"),
    @NamedQuery(name = "Prestataire.findById", query = "SELECT d FROM Prestataire d WHERE d.id = :id"),
    @NamedQuery(name = "Prestataire.findByNom", query = "SELECT d FROM Prestataire d WHERE d.nom = :nom"),
    @NamedQuery(name = "Prestataire.findByAdresse", query = "SELECT d FROM Prestataire d WHERE d.adresse = :adresse"),
    @NamedQuery(name = "Prestataire.findByEmail", query = "SELECT d FROM Prestataire d WHERE d.email = :email"),
    @NamedQuery(name = "Prestataire.findByTelephone", query = "SELECT d FROM Prestataire d WHERE d.telephone = :telephone"),
    @NamedQuery(name = "Prestataire.findByRegistre", query = "SELECT d FROM Prestataire d WHERE d.registre = :registre"),
    @NamedQuery(name = "Prestataire.findByLogo", query = "SELECT d FROM Prestataire d WHERE d.logo = :logo"),
    @NamedQuery(name = "Prestataire.findByStatut", query = "SELECT d FROM Prestataire d WHERE d.statut = :statut"),
    @NamedQuery(name = "Prestataire.findBySupprime", query = "SELECT d FROM Prestataire d WHERE d.supprime = :supprime")})

@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder                    // Permet d'utiliser le pattern Builder
public class Prestataire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "nom")
    private String nom;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "email")
    private String email;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "registre")
    private String registre;
    @Column(name = "logo")
    private String logo;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prestataireId")
    private List<Employe> dbx45tyEmployeList;
    @OneToMany(mappedBy = "prestataireId")
    private List<Dbx45tyMedicament> dbx45tyMedicamentList;
    @JoinColumn(name = "categorie_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyCategoriePrestataire categorieId;
    @JoinColumn(name = "ville_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyVille villeId;
    @OneToMany(mappedBy = "prestataireId")
    private List<Dbx45tyPrestation> dbx45tyPrestationList;
    @OneToMany(mappedBy = "prestataireId")
    private List<LignePrestation> dbx45tyLignePrestationList;
    
}
