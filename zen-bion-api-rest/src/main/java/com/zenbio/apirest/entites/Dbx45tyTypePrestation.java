/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zenbio.apirest.entites;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "dbx45ty_type_prestation")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyTypePrestation.findAll", query = "SELECT d FROM Dbx45tyTypePrestation d"),
    @NamedQuery(name = "Dbx45tyTypePrestation.findById", query = "SELECT d FROM Dbx45tyTypePrestation d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyTypePrestation.findByNom", query = "SELECT d FROM Dbx45tyTypePrestation d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyTypePrestation.findByAffiche", query = "SELECT d FROM Dbx45tyTypePrestation d WHERE d.affiche = :affiche"),
    @NamedQuery(name = "Dbx45tyTypePrestation.findByCategorie", query = "SELECT d FROM Dbx45tyTypePrestation d WHERE d.categorie = :categorie")})
public class Dbx45tyTypePrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "affiche")
    private int affiche;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "categorie")
    private String categorie;

    public Dbx45tyTypePrestation() {
    }

    public Dbx45tyTypePrestation(String id) {
        this.id = id;
    }

    public Dbx45tyTypePrestation(String id, String nom, int affiche, String categorie) {
        this.id = id;
        this.nom = nom;
        this.affiche = affiche;
        this.categorie = categorie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAffiche() {
        return affiche;
    }

    public void setAffiche(int affiche) {
        this.affiche = affiche;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
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
        if (!(object instanceof Dbx45tyTypePrestation)) {
            return false;
        }
        Dbx45tyTypePrestation other = (Dbx45tyTypePrestation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zenbio.apirest.entites.Dbx45tyTypePrestation[ id=" + id + " ]";
    }
    
}
