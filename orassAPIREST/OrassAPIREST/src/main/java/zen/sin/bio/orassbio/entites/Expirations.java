/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.entites;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 *
 * @author USER01
 */
//@Entity
@Data
//@NamedQueries({
//    @NamedQuery(name = "Expirations.findAll", query = "SELECT e FROM Expirations e")})
public class Expirations implements Serializable {

    private static final long serialVersionUID = 1L;
   // @Id
    private String TELEASSU;
    private Integer CODEINTE;
    private Long NUMEPOLI;
    private String NUMEIMMA;
    private Integer NUMEAVEN;
    private String NOMASSURE;
   // @Column(name = "DATEECHE")
   // @Temporal(TemporalType.TIMESTAMP)
    private Date DATEECHE;

    private String LANGUE;

    public Expirations() {
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

    public Long getNUMEPOLI() {
        return NUMEPOLI;
    }

    public void setNUMEPOLI(Long NUMEPOLI) {
        this.NUMEPOLI = NUMEPOLI;
    }

    public String getNUMEIMMA() {
        return NUMEIMMA;
    }

    public void setNUMEIMMA(String NUMEIMMA) {
        this.NUMEIMMA = NUMEIMMA;
    }

    public Integer getNUMEAVEN() {
        return NUMEAVEN;
    }

    public void setNUMEAVEN(Integer NUMEAVEN) {
        this.NUMEAVEN = NUMEAVEN;
    }

    public String getNOMASSURE() {
        return NOMASSURE;
    }

    public void setNOMASSURE(String NOMASSURE) {
        this.NOMASSURE = NOMASSURE;
    }

    public Date getDATEECHE() {
        return DATEECHE;
    }

    public void setDATEECHE(Date DATEECHE) {
        this.DATEECHE = DATEECHE;
    }

    public String getLANGUE() {
        return LANGUE;
    }

    public void setLANGUE(String LANGUE) {
        this.LANGUE = LANGUE;
    }

}
