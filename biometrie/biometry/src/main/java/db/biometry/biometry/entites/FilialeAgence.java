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
@Table(name = "dbx45ty_filiale_agence")
@NamedQueries({
    @NamedQuery(name = "FilialeAgence.findAll", query = "SELECT d FROM FilialeAgence d"),
    @NamedQuery(name = "FilialeAgence.findById", query = "SELECT d FROM FilialeAgence d WHERE d.id = :id"),
    @NamedQuery(name = "FilialeAgence.findByDateCreation", query = "SELECT d FROM FilialeAgence d WHERE d.dateCreation = :dateCreation"),
    @NamedQuery(name = "FilialeAgence.findByStatut", query = "SELECT d FROM FilialeAgence d WHERE d.statut = :statut"),
    @NamedQuery(name = "FilialeAgence.findBySupprime", query = "SELECT d FROM FilialeAgence d WHERE d.supprime = :supprime")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder 

public class FilialeAgence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(mappedBy = "filialeAgenceId")
    private List<Employe> dbx45tyEmployeList;
    @JoinColumn(name = "agence_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyAgence agenceId;
    @JoinColumn(name = "ville_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyVille villeId;
    @OneToMany(mappedBy = "siegeSocialId")
    private List<Dbx45tyAgence> dbx45tyAgenceList;

}
