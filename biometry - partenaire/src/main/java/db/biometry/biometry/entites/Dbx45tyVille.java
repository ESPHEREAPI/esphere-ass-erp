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
@Table(name = "dbx45ty_ville")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyVille.findAll", query = "SELECT d FROM Dbx45tyVille d"),
    @NamedQuery(name = "Dbx45tyVille.findById", query = "SELECT d FROM Dbx45tyVille d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyVille.findByRegionId", query = "SELECT d FROM Dbx45tyVille d WHERE d.regionId = :regionId"),
    @NamedQuery(name = "Dbx45tyVille.findByCode", query = "SELECT d FROM Dbx45tyVille d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyVille.findByCodeZone", query = "SELECT d FROM Dbx45tyVille d WHERE d.codeZone = :codeZone"),
    @NamedQuery(name = "Dbx45tyVille.findByStatut", query = "SELECT d FROM Dbx45tyVille d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyVille.findBySupprime", query = "SELECT d FROM Dbx45tyVille d WHERE d.supprime = :supprime")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder 
public class Dbx45tyVille implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "region_id")
    private int regionId;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "code_zone")
    private String codeZone;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(mappedBy = "villeId")
    private List<Prestataire> dbx45tyPrestataireList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "villeId")
    private List<FilialeAgence> dbx45tyFilialeAgenceList;
    @OneToMany(mappedBy = "chefLieuId")
    private List<Dbx45tyRegion> dbx45tyRegionList;
    @OneToMany(mappedBy = "capitaleId")
    private List<Dbx45tyPays> dbx45tyPaysList;

    
}
