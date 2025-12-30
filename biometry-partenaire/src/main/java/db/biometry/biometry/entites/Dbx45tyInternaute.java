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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "dbx45ty_internaute")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyInternaute.findAll", query = "SELECT d FROM Dbx45tyInternaute d"),
    @NamedQuery(name = "Dbx45tyInternaute.findById", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyInternaute.findByTokenActivation", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.tokenActivation = :tokenActivation"),
    @NamedQuery(name = "Dbx45tyInternaute.findByDateTokenActivation", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.dateTokenActivation = :dateTokenActivation"),
    @NamedQuery(name = "Dbx45tyInternaute.findByTokenMotPasseOublie", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.tokenMotPasseOublie = :tokenMotPasseOublie"),
    @NamedQuery(name = "Dbx45tyInternaute.findByLienActiverCompte", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.lienActiverCompte = :lienActiverCompte")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder  
public class Dbx45tyInternaute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "token_activation")
    private String tokenActivation;
    @Column(name = "date_token_activation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTokenActivation;
    @Column(name = "token_mot_passe_oublie")
    private String tokenMotPasseOublie;
    @Column(name = "lien_activer_compte")
    private String lienActiverCompte;
    @JoinColumn(name = "utilisateur_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Utilisateur utilisateurId;

    
}
