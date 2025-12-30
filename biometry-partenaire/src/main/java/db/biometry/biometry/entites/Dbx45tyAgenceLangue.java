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
@Table(name = "dbx45ty_agence_langue")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyAgenceLangue.findAll", query = "SELECT d FROM Dbx45tyAgenceLangue d"),
    @NamedQuery(name = "Dbx45tyAgenceLangue.findById", query = "SELECT d FROM Dbx45tyAgenceLangue d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyAgenceLangue.findByDescriptionCourte", query = "SELECT d FROM Dbx45tyAgenceLangue d WHERE d.descriptionCourte = :descriptionCourte"),
    @NamedQuery(name = "Dbx45tyAgenceLangue.findByDescription", query = "SELECT d FROM Dbx45tyAgenceLangue d WHERE d.description = :description")})
public class Dbx45tyAgenceLangue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description_courte")
    private String descriptionCourte;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "agence_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyAgence agenceId;
    @JoinColumn(name = "langue_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyLangue langueId;

    public Dbx45tyAgenceLangue() {
    }

    public Dbx45tyAgenceLangue(Integer id) {
        this.id = id;
    }

    public Dbx45tyAgenceLangue(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescriptionCourte() {
        return descriptionCourte;
    }

    public void setDescriptionCourte(String descriptionCourte) {
        this.descriptionCourte = descriptionCourte;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Dbx45tyAgence getAgenceId() {
        return agenceId;
    }

    public void setAgenceId(Dbx45tyAgence agenceId) {
        this.agenceId = agenceId;
    }

    public Dbx45tyLangue getLangueId() {
        return langueId;
    }

    public void setLangueId(Dbx45tyLangue langueId) {
        this.langueId = langueId;
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
        if (!(object instanceof Dbx45tyAgenceLangue)) {
            return false;
        }
        Dbx45tyAgenceLangue other = (Dbx45tyAgenceLangue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entites.Dbx45tyAgenceLangue[ id=" + id + " ]";
    }
    
}
