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
@Table(name = "dbx45ty_prestation")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyPrestation.findAll", query = "SELECT d FROM Dbx45tyPrestation d"),
    @NamedQuery(name = "Dbx45tyPrestation.findById", query = "SELECT d FROM Dbx45tyPrestation d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyPrestation.findByNaturePrestation", query = "SELECT d FROM Dbx45tyPrestation d WHERE d.naturePrestation = :naturePrestation"),
    @NamedQuery(name = "Dbx45tyPrestation.findByDate", query = "SELECT d FROM Dbx45tyPrestation d WHERE d.date = :date"),
    @NamedQuery(name = "Dbx45tyPrestation.findBySupprime", query = "SELECT d FROM Dbx45tyPrestation d WHERE d.supprime = :supprime")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder                    // Permet d'utiliser le pattern Builder
public class Dbx45tyPrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nature_prestation")
    private String naturePrestation;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @JoinColumn(name = "prestataire_id", referencedColumnName = "id")
    @ManyToOne
    private Prestataire prestataireId;
    @JoinColumn(name = "visite_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyVisite visiteId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prestationId")
    private List<LignePrestation> dbx45tyLignePrestationList;

    
}
