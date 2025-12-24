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
@Table(name = "dbx45ty_ligne_prestation_audit")
@NamedQueries({
    @NamedQuery(name = "LignePrestationAudit.findAll", query = "SELECT d FROM LignePrestationAudit d"),
    @NamedQuery(name = "LignePrestationAudit.findById", query = "SELECT d FROM LignePrestationAudit d WHERE d.id = :id"),
    @NamedQuery(name = "LignePrestationAudit.findByEtatLignePrestation", query = "SELECT d FROM LignePrestationAudit d WHERE d.etatLignePrestation = :etatLignePrestation"),
    @NamedQuery(name = "LignePrestationAudit.findByDate", query = "SELECT d FROM LignePrestationAudit d WHERE d.date = :date")})
@Data                       // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Génère constructeur vide
@AllArgsConstructor         // Génère constructeur avec tous les champs
@Builder                    // Permet d'utiliser le pattern Builder

public class LignePrestationAudit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "etat_ligne_prestation")
    private String etatLignePrestation;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "employe_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employe employeId;
    @JoinColumn(name = "ligne_prestation_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LignePrestation lignePrestationId;

}
