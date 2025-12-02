/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.entites;

import java.io.Serializable;
import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
import lombok.Data;

/**
 *
 * @author USER01
 */
//@Entity
@Data
//@NamedQueries({
//    @NamedQuery(name = "SmsProduction.findAll", query = "SELECT s FROM SmsProduction s")})
public class SmsProduction implements Serializable {

    private static final long serialVersionUID = 1L;
//    @Id
    private String TELEASSU;
    private Integer CODEINTE;
    private String BUREAU;
    private Long NUMEPOLI;
    private Integer NUMEAVEN;
    private String CHARGER_CLI;
    private String CHARGER_CLI_TEL;
    private String LANGUE;
    private String BRANCHE;
    private String NOMASSURE;

    private long CODECATE;
    private String LIBECATE;

//    @Temporal(TemporalType.TIMESTAMP)
    private Date DATEEMIS;
//    @Temporal(TemporalType.TIMESTAMP)
    private Date DATEEFFE;
//    @Temporal(TemporalType.TIMESTAMP)
    private Date DATEECHE;
    private Long PRIMNETT;
    private Long PRIMTOTA;

    public SmsProduction() {

    }

    public String getTELEASSU() {
        return TELEASSU;
    }

    public void setTELEASSU(String TELEASSU) {
        this.TELEASSU = TELEASSU;
    }

    public Integer getCODEINTE() {
        return CODEINTE;
    }

    public void setCODEINTE(Integer CODEINTE) {
        this.CODEINTE = CODEINTE;
    }

    public String getBUREAU() {
        return BUREAU;
    }

    public void setBUREAU(String BUREAU) {
        this.BUREAU = BUREAU;
    }

    public Long getNUMEPOLI() {
        return NUMEPOLI;
    }

    public void setNUMEPOLI(Long NUMEPOLI) {
        this.NUMEPOLI = NUMEPOLI;
    }

    public Integer getNUMEAVEN() {
        return NUMEAVEN;
    }

    public void setNUMEAVEN(Integer NUMEAVEN) {
        this.NUMEAVEN = NUMEAVEN;
    }

    public String getCHARGER_CLI() {
        return CHARGER_CLI;
    }

    public void setCHARGER_CLI(String CHARGER_CLI) {
        this.CHARGER_CLI = CHARGER_CLI;
    }

    public String getCHARGER_CLI_TEL() {
        return CHARGER_CLI_TEL;
    }

    public void setCHARGER_CLI_TEL(String CHARGER_CLI_TEL) {
        this.CHARGER_CLI_TEL = CHARGER_CLI_TEL;
    }

    public String getLANGUE() {
        return LANGUE;
    }

    public void setLANGUE(String LANGUE) {
        this.LANGUE = LANGUE;
    }

    public String getBRANCHE() {
        return BRANCHE;
    }

    public void setBRANCHE(String BRANCHE) {
        this.BRANCHE = BRANCHE;
    }

    public String getNOMASSURE() {
        return NOMASSURE;
    }

    public void setNOMASSURE(String NOMASSURE) {
        this.NOMASSURE = NOMASSURE;
    }

    public long getCODECATE() {
        return CODECATE;
    }

    public void setCODECATE(long CODECATE) {
        this.CODECATE = CODECATE;
    }

    public String getLIBECATE() {
        return LIBECATE;
    }

    public void setLIBECATE(String LIBECATE) {
        this.LIBECATE = LIBECATE;
    }

    public Date getDATEEMIS() {
        return DATEEMIS;
    }

    public void setDATEEMIS(Date DATEEMIS) {
        this.DATEEMIS = DATEEMIS;
    }

    public Date getDATEEFFE() {
        return DATEEFFE;
    }

    public void setDATEEFFE(Date DATEEFFE) {
        this.DATEEFFE = DATEEFFE;
    }

    public Date getDATEECHE() {
        return DATEECHE;
    }

    public void setDATEECHE(Date DATEECHE) {
        this.DATEECHE = DATEECHE;
    }

    public Long getPRIMNETT() {
        return PRIMNETT;
    }

    public void setPRIMNETT(Long PRIMNETT) {
        this.PRIMNETT = PRIMNETT;
    }

    public Long getPRIMTOTA() {
        return PRIMTOTA;
    }

    public void setPRIMTOTA(Long PRIMTOTA) {
        this.PRIMTOTA = PRIMTOTA;
    }

}
