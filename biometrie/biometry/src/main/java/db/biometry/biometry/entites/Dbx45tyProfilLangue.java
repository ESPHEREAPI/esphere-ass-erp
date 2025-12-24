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
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "dbx45ty_profil_langue")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyProfilLangue.findAll", query = "SELECT d FROM Dbx45tyProfilLangue d"),
    @NamedQuery(name = "Dbx45tyProfilLangue.findById", query = "SELECT d FROM Dbx45tyProfilLangue d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyProfilLangue.findByNom", query = "SELECT d FROM Dbx45tyProfilLangue d WHERE d.nom = :nom")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder                    // Permet d'utiliser le pattern Builder
public class Dbx45tyProfilLangue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @JoinColumn(name = "langue_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyLangue langueId;
    @JoinColumn(name = "profil_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Profil profilId;

    
    
}
