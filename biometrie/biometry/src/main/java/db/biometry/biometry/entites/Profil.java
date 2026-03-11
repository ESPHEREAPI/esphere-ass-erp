/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entites;

import db.biometry.biometry.enums.StatutType;
import db.biometry.biometry.enums.TypeProfilUser;
import db.biometry.biometry.enums.TypeSousProfil;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "dbx45ty_profil")
@NamedQueries({
    @NamedQuery(name = "Profil.findAll", query = "SELECT d FROM Profil d"),
    @NamedQuery(name = "Profil.findById", query = "SELECT d FROM Profil d WHERE d.id = :id"),
    @NamedQuery(name = "Profil.findByTypeProfil", query = "SELECT d FROM Profil d WHERE d.typeProfil = :typeProfil"),
    @NamedQuery(name = "Profil.findByTypeSousProfil", query = "SELECT d FROM Profil d WHERE d.typeSousProfil = :typeSousProfil"),
    @NamedQuery(name = "Profil.findByCode", query = "SELECT d FROM Profil d WHERE d.code = :code"),
    @NamedQuery(name = "Profil.findByStatut", query = "SELECT d FROM Profil d WHERE d.statut = :statut"), //    @NamedQuery(name = "Profil.findBySupprime", query = "SELECT d FROM Profil d WHERE d.supprime = :supprime")
})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder
public class Profil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "type_profil")
    private TypeProfilUser typeProfil;
    @Basic(optional = false)
    @Column(name = "type_sous_profil")
    @Enumerated(EnumType.STRING)
    private TypeSousProfil typeSousProfil;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "statut")
    @Enumerated(EnumType.STRING)
    private StatutType statut;
//    @Basic(optional = false)
//    @Column(name = "supprime")
//    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profilId")
    private List<Employe> dbx45tyEmployeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profilId")
    private List<Dbx45tyProfilLangue> dbx45tyProfilLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profilId")
    private List<Dbx45tyPermission> dbx45tyPermissionList;

}
