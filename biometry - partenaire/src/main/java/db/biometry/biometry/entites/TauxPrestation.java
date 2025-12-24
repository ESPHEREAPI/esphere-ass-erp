///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package db.biometry.biometry.entites;
//
//import jakarta.persistence.Basic;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.NamedQueries;
//import jakarta.persistence.NamedQuery;
//import jakarta.persistence.Table;
//import java.io.Serializable;
//
///**
// *
// * @author USER01
// */
//@Entity
//@Table(name = "taux_prestation")
//@NamedQueries({
//    @NamedQuery(name = "TauxPrestation.findAll", query = "SELECT t FROM TauxPrestation t"),
//    @NamedQuery(name = "TauxPrestation.findByCodeTypePrestation", query = "SELECT t FROM TauxPrestation t WHERE t.codeTypePrestation = :codeTypePrestation"),
//    @NamedQuery(name = "TauxPrestation.findByPolice", query = "SELECT t FROM TauxPrestation t WHERE t.police = :police"),
//    @NamedQuery(name = "TauxPrestation.findByGroupe", query = "SELECT t FROM TauxPrestation t WHERE t.groupe = :groupe")})
//public class TauxPrestation implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//    @Basic(optional = false)
//    @Column(name = "code_type_prestation")
//    private String codeTypePrestation;
//    @Basic(optional = false)
//    @Column(name = "police")
//    private String police;
//    @Basic(optional = false)
//    @Column(name = "groupe")
//    private short groupe;
//
//    public TauxPrestation() {
//    }
//
//    public String getCodeTypePrestation() {
//        return codeTypePrestation;
//    }
//
//    public void setCodeTypePrestation(String codeTypePrestation) {
//        this.codeTypePrestation = codeTypePrestation;
//    }
//
//    public String getPolice() {
//        return police;
//    }
//
//    public void setPolice(String police) {
//        this.police = police;
//    }
//
//    public short getGroupe() {
//        return groupe;
//    }
//
//    public void setGroupe(short groupe) {
//        this.groupe = groupe;
//    }
//    
//}
