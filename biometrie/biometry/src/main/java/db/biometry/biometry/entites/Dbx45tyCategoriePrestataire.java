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
@Table(name = "dbx45ty_categorie_prestataire")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyCategoriePrestataire.findAll", query = "SELECT d FROM Dbx45tyCategoriePrestataire d"),
    @NamedQuery(name = "Dbx45tyCategoriePrestataire.findById", query = "SELECT d FROM Dbx45tyCategoriePrestataire d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyCategoriePrestataire.findByNom", query = "SELECT d FROM Dbx45tyCategoriePrestataire d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyCategoriePrestataire.findByStatut", query = "SELECT d FROM Dbx45tyCategoriePrestataire d WHERE d.statut = :statut")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder                    // Permet d'utiliser le pattern Builder

public class Dbx45tyCategoriePrestataire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categorieId")
    private List<Prestataire> dbx45tyPrestataireList;

  
    
}
