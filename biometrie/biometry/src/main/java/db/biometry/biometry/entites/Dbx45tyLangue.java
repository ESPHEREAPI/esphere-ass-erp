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
@Table(name = "dbx45ty_langue")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyLangue.findAll", query = "SELECT d FROM Dbx45tyLangue d"),
    @NamedQuery(name = "Dbx45tyLangue.findById", query = "SELECT d FROM Dbx45tyLangue d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyLangue.findByNom", query = "SELECT d FROM Dbx45tyLangue d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyLangue.findByCodeIso", query = "SELECT d FROM Dbx45tyLangue d WHERE d.codeIso = :codeIso"),
    @NamedQuery(name = "Dbx45tyLangue.findByCodeFin", query = "SELECT d FROM Dbx45tyLangue d WHERE d.codeFin = :codeFin"),
    @NamedQuery(name = "Dbx45tyLangue.findByCode", query = "SELECT d FROM Dbx45tyLangue d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyLangue.findByFormatDate", query = "SELECT d FROM Dbx45tyLangue d WHERE d.formatDate = :formatDate"),
    @NamedQuery(name = "Dbx45tyLangue.findByStatut", query = "SELECT d FROM Dbx45tyLangue d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyLangue.findBySupprime", query = "SELECT d FROM Dbx45tyLangue d WHERE d.supprime = :supprime")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder  
public class Dbx45tyLangue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "code_iso")
    private String codeIso;
    @Basic(optional = false)
    @Column(name = "code_fin")
    private String codeFin;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "format_date")
    private String formatDate;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyMenuLangue> dbx45tyMenuLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueDefaut")
    private List<Utilisateur> dbx45tyUtilisateurList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyProfilLangue> dbx45tyProfilLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyRegionLangue> dbx45tyRegionLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyPaysLangue> dbx45tyPaysLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyLangueHasPays> dbx45tyLangueHasPaysList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyAgenceLangue> dbx45tyAgenceLangueList;

    
}
