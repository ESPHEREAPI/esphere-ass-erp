/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import lombok.Data;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import lombok.Data;

/**
 *
 * @author USER01
 */
//@Entity
//@Data
//@NamedQueries({
//    @NamedQuery(name = "ZenGarantie.findAll", query = "SELECT z FROM ZenGarantie z")})
public class ZenGarantie implements Serializable {

    private static final long serialVersionUID = 1L;
//    @Id

//    @JsonIgnore
//    private Integer CODEINTE;
//    @JsonIgnore
//    private Long NUMEPOLI;
//    @JsonIgnore
//    private Integer AVENMODI;
    private String CODEGARA;
    private String LIBEGARA;
    private Long MONTGARA;

    public ZenGarantie() {
    }


//
//    public Integer getCODEINTE() {
//        return CODEINTE;
//    }
//
//    public void setCODEINTE(Integer CODEINTE) {
//        this.CODEINTE = CODEINTE;
//    }
//
//    public Long getNUMEPOLI() {
//        return NUMEPOLI;
//    }
//
//    public void setNUMEPOLI(Long NUMEPOLI) {
//        this.NUMEPOLI = NUMEPOLI;
//    }
//
//    public Integer getAVENMODI() {
//        return AVENMODI;
//    }
//
//    public void setAVENMODI(Integer AVENMODI) {
//        this.AVENMODI = AVENMODI;
//    }

    public String getCODEGARA() {
        return CODEGARA;
    }

    public void setCODEGARA(String CODEGARA) {
        this.CODEGARA = CODEGARA;
    }

    public String getLIBEGARA() {
        return LIBEGARA;
    }

    public void setLIBEGARA(String LIBEGARA) {
        this.LIBEGARA = LIBEGARA;
    }

    public Long getMONTGARA() {
        return MONTGARA;
    }

    public void setMONTGARA(Long MONTGARA) {
        this.MONTGARA = MONTGARA;
    }

}
