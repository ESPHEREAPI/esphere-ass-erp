/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.entites;

import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import zen.sin.bio.orassbio.exception.GlobalException;

/**
 *
 * @author USER01
 */
//@Entity

@Table(name = "ZEN_SINISTRE_LN")
@NamedQueries({
    @NamedQuery(name = "ZenSinistreLn.findAll", query = "SELECT z FROM ZenSinistreLn z"),
    @NamedQuery(name = "ZenSinistreLn.findByAssure", query = "SELECT z FROM ZenSinistreLn z WHERE z.assure = :assure"),
    @NamedQuery(name = "ZenSinistreLn.findByDateSurvenance", query = "SELECT z FROM ZenSinistreLn z WHERE z.dateSurvenance = :dateSurvenance"),
    @NamedQuery(name = "ZenSinistreLn.findByDateDeclaration", query = "SELECT z FROM ZenSinistreLn z WHERE z.dateDeclaration = :dateDeclaration"),
    @NamedQuery(name = "ZenSinistreLn.findByNumeimma", query = "SELECT z FROM ZenSinistreLn z WHERE z.numeimma = :numeimma"),
    @NamedQuery(name = "ZenSinistreLn.findByMarque", query = "SELECT z FROM ZenSinistreLn z WHERE z.marque = :marque"),
    @NamedQuery(name = "ZenSinistreLn.findBySort", query = "SELECT z FROM ZenSinistreLn z WHERE z.sort = :sort")})
public class ZenSinistreLn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "CODEINTE")
    private int codeinte;

    @Column(name = "EXERSINI")
    private short exersini;

    @Column(name = "NUMESINI")
    private int numesini;
    @Column(name = "INTEPOLI")
    private int intepoli;
    @Column(name = "NUMEPOLI")
    private Long numepoli;
    @Size(max = 50)
    @Column(name = "ASSURE")
    private String assure;
    @Column(name = "DATE_SURVENANCE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSurvenance;
    @Column(name = "DATE_DECLARATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDeclaration;
    @Size(max = 15)
    @Column(name = "NUMEIMMA")
    private String numeimma;
    @Size(max = 20)
    @Column(name = "MARQUE")
    private String marque;
    @Size(max = 30)
    @Column(name = "SORT")
    private String sort;
   private List<ZenEvenement> listeEvenement=new ArrayList<>();
    private GlobalException error;

    public ZenSinistreLn() {
//        error=new GlobalException();
    }

    public String getAssure() {
        return assure;
    }

    public void setAssure(String assure) {
        this.assure = assure;
    }

    public Date getDateSurvenance() {
        return dateSurvenance;
    }

    public void setDateSurvenance(Date dateSurvenance) {
        this.dateSurvenance = dateSurvenance;
    }

    public Date getDateDeclaration() {
        return dateDeclaration;
    }

    public void setDateDeclaration(Date dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public String getNumeimma() {
        return numeimma;
    }

    public void setNumeimma(String numeimma) {
        this.numeimma = numeimma;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public int getIntepoli() {
        return intepoli;
    }

    public void setIntepoli(int intepoli) {
        this.intepoli = intepoli;
    }

    public Long getNumepoli() {
        return numepoli;
    }

    public void setNumepoli(long numepoli) {
        this.numepoli = numepoli;
    }

    public GlobalException getError() {
        return error;
    }

    public void setError(GlobalException error) {
        this.error = error;
    }

    public List<ZenEvenement> getListeEvenement() {
        return listeEvenement;
    }

    public void setListeEvenement(List<ZenEvenement> listeEvenement) {
        this.listeEvenement = listeEvenement;
    }
    
    

}
