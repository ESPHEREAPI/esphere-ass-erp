/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entites;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "type_prestation")
@NamedQueries({
    @NamedQuery(name = "TypePrestation.findAll", query = "SELECT t FROM TypePrestation t"),
    @NamedQuery(name = "TypePrestation.findByCodeTypePrestation", query = "SELECT t FROM TypePrestation t WHERE t.codeTypePrestation = :codeTypePrestation"),
    @NamedQuery(name = "TypePrestation.findByNom", query = "SELECT t FROM TypePrestation t WHERE t.nom = :nom")})
public class TypePrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "code_type_prestation")
    private String codeTypePrestation;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;

    public TypePrestation() {
    }

    public String getCodeTypePrestation() {
        return codeTypePrestation;
    }

    public void setCodeTypePrestation(String codeTypePrestation) {
        this.codeTypePrestation = codeTypePrestation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
}
