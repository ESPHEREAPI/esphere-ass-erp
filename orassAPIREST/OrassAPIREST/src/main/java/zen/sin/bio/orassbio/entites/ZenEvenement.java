/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author USER01
 */
//@Entity
@Table(name = "ZEN_EVENEMENT")
//@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZenEvenement.findAll", query = "SELECT z FROM ZenEvenement z"),
    @NamedQuery(name = "ZenEvenement.findByCodeinte", query = "SELECT z FROM ZenEvenement z WHERE z.codeinte = :codeinte"),
    @NamedQuery(name = "ZenEvenement.findByExersini", query = "SELECT z FROM ZenEvenement z WHERE z.exersini = :exersini"),
    @NamedQuery(name = "ZenEvenement.findByNumesini", query = "SELECT z FROM ZenEvenement z WHERE z.numesini = :numesini"),
    @NamedQuery(name = "ZenEvenement.findByLibeeven", query = "SELECT z FROM ZenEvenement z WHERE z.libeeven = :libeeven"),
    @NamedQuery(name = "ZenEvenement.findByAssure", query = "SELECT z FROM ZenEvenement z WHERE z.assure = :assure"),
    @NamedQuery(name = "ZenEvenement.findByNumeimma", query = "SELECT z FROM ZenEvenement z WHERE z.numeimma = :numeimma"),
    @NamedQuery(name = "ZenEvenement.findByMarqvehi", query = "SELECT z FROM ZenEvenement z WHERE z.marqvehi = :marqvehi"),
    @NamedQuery(name = "ZenEvenement.findByTypevehi", query = "SELECT z FROM ZenEvenement z WHERE z.typevehi = :typevehi"),
    @NamedQuery(name = "ZenEvenement.findByObseeven", query = "SELECT z FROM ZenEvenement z WHERE z.obseeven = :obseeven"),
    @NamedQuery(name = "ZenEvenement.findByDateeven", query = "SELECT z FROM ZenEvenement z WHERE z.dateeven = :dateeven")})
public class ZenEvenement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @JsonIgnore
    @Column(name = "CODEINTE")
    private int codeinte;
    @Basic(optional = false)
    //@NotNull
    @JsonIgnore
    @Column(name = "EXERSINI")
    private short exersini;
    @Basic(optional = false)
   // @NotNull
    @JsonIgnore
    @Column(name = "NUMESINI")
    private int numesini;
    @Size(max = 150)
    @Column(name = "LIBEEVEN")
    private String libeeven;
    @Size(max = 50)
    @JsonIgnore
    @Column(name = "ASSURE")
    private String assure;
    @Size(max = 15)
    @JsonIgnore
    @Column(name = "NUMEIMMA")
    private String numeimma;
    @Size(max = 20)
    @JsonIgnore
    @Column(name = "MARQVEHI")
    private String marqvehi;
    @Size(max = 20)
    @Column(name = "TYPEVEHI")
    private String typevehi;
    @Size(max = 1000)
    @Column(name = "OBSEEVEN")
    private String obseeven;
    @Column(name = "DATEEVEN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateeven;
    private Integer MONTREGL=0;

    public ZenEvenement() {
    }

    public int getCodeinte() {
        return codeinte;
    }

    public void setCodeinte(int codeinte) {
        this.codeinte = codeinte;
    }

    public short getExersini() {
        return exersini;
    }

    public void setExersini(short exersini) {
        this.exersini = exersini;
    }

    public int getNumesini() {
        return numesini;
    }

    public void setNumesini(int numesini) {
        this.numesini = numesini;
    }

    public String getLibeeven() {
        return libeeven;
    }

    public void setLibeeven(String libeeven) {
        this.libeeven = libeeven;
    }

    public String getAssure() {
        return assure;
    }

    public void setAssure(String assure) {
        this.assure = assure;
    }

    public String getNumeimma() {
        return numeimma;
    }

    public void setNumeimma(String numeimma) {
        this.numeimma = numeimma;
    }

    public String getMarqvehi() {
        return marqvehi;
    }

    public void setMarqvehi(String marqvehi) {
        this.marqvehi = marqvehi;
    }

    public String getTypevehi() {
        return typevehi;
    }

    public void setTypevehi(String typevehi) {
        this.typevehi = typevehi;
    }

    public String getObseeven() {
        return obseeven;
    }

    public void setObseeven(String obseeven) {
        this.obseeven = obseeven;
    }

    public Date getDateeven() {
        return dateeven;
    }

    public void setDateeven(Date dateeven) {
        this.dateeven = dateeven;
    }

    public Integer getMONTREGL() {
        return MONTREGL;
    }

    public void setMONTREGL(Integer MONTREGL) {
        this.MONTREGL = MONTREGL;
    }
    

}
