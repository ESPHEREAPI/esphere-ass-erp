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

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "dbx45ty_pays_langue")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyPaysLangue.findAll", query = "SELECT d FROM Dbx45tyPaysLangue d"),
    @NamedQuery(name = "Dbx45tyPaysLangue.findById", query = "SELECT d FROM Dbx45tyPaysLangue d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyPaysLangue.findByNom", query = "SELECT d FROM Dbx45tyPaysLangue d WHERE d.nom = :nom")})
public class Dbx45tyPaysLangue implements Serializable {

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
    @JoinColumn(name = "pays_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyPays paysId;

    public Dbx45tyPaysLangue() {
    }

    public Dbx45tyPaysLangue(Integer id) {
        this.id = id;
    }

    public Dbx45tyPaysLangue(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Dbx45tyLangue getLangueId() {
        return langueId;
    }

    public void setLangueId(Dbx45tyLangue langueId) {
        this.langueId = langueId;
    }

    public Dbx45tyPays getPaysId() {
        return paysId;
    }

    public void setPaysId(Dbx45tyPays paysId) {
        this.paysId = paysId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dbx45tyPaysLangue)) {
            return false;
        }
        Dbx45tyPaysLangue other = (Dbx45tyPaysLangue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entites.Dbx45tyPaysLangue[ id=" + id + " ]";
    }
    
}
