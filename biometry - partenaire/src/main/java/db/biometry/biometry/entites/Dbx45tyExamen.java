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
@Table(name = "dbx45ty_examen")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyExamen.findAll", query = "SELECT d FROM Dbx45tyExamen d"),
    @NamedQuery(name = "Dbx45tyExamen.findById", query = "SELECT d FROM Dbx45tyExamen d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyExamen.findByCode", query = "SELECT d FROM Dbx45tyExamen d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyExamen.findByNom", query = "SELECT d FROM Dbx45tyExamen d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyExamen.findByCotation", query = "SELECT d FROM Dbx45tyExamen d WHERE d.cotation = :cotation"),
    @NamedQuery(name = "Dbx45tyExamen.findByPrix", query = "SELECT d FROM Dbx45tyExamen d WHERE d.prix = :prix"),
    @NamedQuery(name = "Dbx45tyExamen.findByStatut", query = "SELECT d FROM Dbx45tyExamen d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyExamen.findBySupprime", query = "SELECT d FROM Dbx45tyExamen d WHERE d.supprime = :supprime")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder                    // Permet d'utiliser le pattern Builder

public class Dbx45tyExamen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "cotation")
    private short cotation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix")
    private Double prix;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(mappedBy = "examenId")
    private List<LignePrestation> dbx45tyLignePrestationList;

    
    
}
