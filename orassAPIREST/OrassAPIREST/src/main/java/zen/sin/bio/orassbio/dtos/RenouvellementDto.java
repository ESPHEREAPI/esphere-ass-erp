/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import zen.sin.bio.orassbio.entites.ZenGarantie;

/**
 *
 * @author USER01
 */

public class RenouvellementDto implements Serializable {

    private String POLICEAV;
    private String NUMEIMMA;
    private Integer CODEINTE;
    private Long NUMEPOLI;
    private Integer NUMEAVEN;
    private String TYPE_AVENANT;
    // @Temporal(TemporalType.TIMESTAMP)
    private Date DATEEFFE;
    //  @Temporal(TemporalType.TIMESTAMP)
    private Date DATEECHE;
    private Integer CODECATE;
    private String LIBECATE;
    private Integer CATEFILLE;
    private String NOM;
    private String TELEASSU;
    private String VILLE;
    private String ENERGIE;
    private Integer PUISSANCE_FISC;
    private String MERQUEVEHI;
    private String TYPEVEHI;
    private Integer POIDS;
    private Integer NBREPLACE;
    private String NBRECARTE;
    private String ANNEPERCOND;
    // @Temporal(TemporalType.DATE)
    private Date DATENAISCOND;
    //  @Temporal(TemporalType.DATE)
    private Date DATEMISECIRC;
    private String POSSEDEVIGNETTE;
    private Integer DUREE_COURANTE;
    private Integer DUREETOTALE;
    private String AVECREMORQ;
    private String LIQUIDINFLAM;
    private String USAGE;
    private String DOUBLECOMMANDE;
    private Long PRIMNETT;
    private Long PRIMTOTA;
    private String REF_DIGITAL;
    private Integer CYLINDREE;
    private String GENRE;
    private String ZONE_;
    private String SOUS_CATEGORIE;
    private String AVEC_RC_ELEVE;
    private String NOM_BUREAU;
    List<ZenGarantie> listeGaranties = new ArrayList<>();

    public String getPOLICEAV() {
        return POLICEAV;
    }

    public void setPOLICEAV(String POLICEAV) {
        this.POLICEAV = POLICEAV;
    }

    public String getNUMEIMMA() {
        return NUMEIMMA;
    }

    public void setNUMEIMMA(String NUMEIMMA) {
        this.NUMEIMMA = NUMEIMMA;
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

    public Integer getNUMEAVEN() {
        return NUMEAVEN;
    }

    public void setNUMEAVEN(Integer NUMEAVEN) {
        this.NUMEAVEN = NUMEAVEN;
    }

    public String getTYPE_AVENANT() {
        return TYPE_AVENANT;
    }

    public void setTYPE_AVENANT(String TYPE_AVENANT) {
        this.TYPE_AVENANT = TYPE_AVENANT;
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

    public Integer getCODECATE() {
        return CODECATE;
    }

    public void setCODECATE(Integer CODECATE) {
        this.CODECATE = CODECATE;
    }

    public String getLIBECATE() {
        return LIBECATE;
    }

    public void setLIBECATE(String LIBECATE) {
        this.LIBECATE = LIBECATE;
    }

    public Integer getCATEFILLE() {
        return CATEFILLE;
    }

    public void setCATEFILLE(Integer CATEFILLE) {
        this.CATEFILLE = CATEFILLE;
    }

    public String getNOM() {
        return NOM;
    }

    public void setNOM(String NOM) {
        this.NOM = NOM;
    }

    public String getTELEASSU() {
        return TELEASSU;
    }

    public void setTELEASSU(String TELEASSU) {
        this.TELEASSU = TELEASSU;
    }

    public String getVILLE() {
        return VILLE;
    }

    public void setVILLE(String VILLE) {
        this.VILLE = VILLE;
    }

    public String getENERGIE() {
        return ENERGIE;
    }

    public void setENERGIE(String ENERGIE) {
        this.ENERGIE = ENERGIE;
    }

    public Integer getPUISSANCE_FISC() {
        return PUISSANCE_FISC;
    }

    public void setPUISSANCE_FISC(Integer PUISSANCE_FISC) {
        this.PUISSANCE_FISC = PUISSANCE_FISC;
    }

    public String getMERQUEVEHI() {
        return MERQUEVEHI;
    }

    public void setMERQUEVEHI(String MERQUEVEHI) {
        this.MERQUEVEHI = MERQUEVEHI;
    }

    public String getTYPEVEHI() {
        return TYPEVEHI;
    }

    public void setTYPEVEHI(String TYPEVEHI) {
        this.TYPEVEHI = TYPEVEHI;
    }

    public Integer getPOIDS() {
        return POIDS;
    }

    public void setPOIDS(Integer POIDS) {
        this.POIDS = POIDS;
    }

    public Integer getNBREPLACE() {
        return NBREPLACE;
    }

    public void setNBREPLACE(Integer NBREPLACE) {
        this.NBREPLACE = NBREPLACE;
    }

    public String getNBRECARTE() {
        return NBRECARTE;
    }

    public void setNBRECARTE(String NBRECARTE) {
        this.NBRECARTE = NBRECARTE;
    }

    public String getANNEPERCOND() {
        return ANNEPERCOND;
    }

    public void setANNEPERCOND(String ANNEPERCOND) {
        this.ANNEPERCOND = ANNEPERCOND;
    }

    public Date getDATENAISCOND() {
        return DATENAISCOND;
    }

    public void setDATENAISCOND(Date DATENAISCOND) {
        this.DATENAISCOND = DATENAISCOND;
    }

    public Date getDATEMISECIRC() {
        return DATEMISECIRC;
    }

    public void setDATEMISECIRC(Date DATEMISECIRC) {
        this.DATEMISECIRC = DATEMISECIRC;
    }

    public String getPOSSEDEVIGNETTE() {
        return POSSEDEVIGNETTE;
    }

    public void setPOSSEDEVIGNETTE(String POSSEDEVIGNETTE) {
        this.POSSEDEVIGNETTE = POSSEDEVIGNETTE;
    }

    public Integer getDUREE_COURANTE() {
        return DUREE_COURANTE;
    }

    public void setDUREE_COURANTE(Integer DUREE_COURANTE) {
        this.DUREE_COURANTE = DUREE_COURANTE;
    }

    public Integer getDUREETOTALE() {
        return DUREETOTALE;
    }

    public void setDUREETOTALE(Integer DUREETOTALE) {
        this.DUREETOTALE = DUREETOTALE;
    }

    public String getAVECREMORQ() {
        return AVECREMORQ;
    }

    public void setAVECREMORQ(String AVECREMORQ) {
        this.AVECREMORQ = AVECREMORQ;
    }

    public String getLIQUIDINFLAM() {
        return LIQUIDINFLAM;
    }

    public void setLIQUIDINFLAM(String LIQUIDINFLAM) {
        this.LIQUIDINFLAM = LIQUIDINFLAM;
    }

    public String getUSAGE() {
        return USAGE;
    }

    public void setUSAGE(String USAGE) {
        this.USAGE = USAGE;
    }

    public String getDOUBLECOMMANDE() {
        return DOUBLECOMMANDE;
    }

    public void setDOUBLECOMMANDE(String DOUBLECOMMANDE) {
        this.DOUBLECOMMANDE = DOUBLECOMMANDE;
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

    public String getREF_DIGITAL() {
        return REF_DIGITAL;
    }

    public void setREF_DIGITAL(String REF_DIGITAL) {
        this.REF_DIGITAL = REF_DIGITAL;
    }

    public Integer getCYLINDREE() {
        return CYLINDREE;
    }

    public void setCYLINDREE(Integer CYLINDREE) {
        this.CYLINDREE = CYLINDREE;
    }

    public String getGENRE() {
        return GENRE;
    }

    public void setGENRE(String GENRE) {
        this.GENRE = GENRE;
    }

    public String getZONE_() {
        return ZONE_;
    }

    public void setZONE_(String ZONE_) {
        this.ZONE_ = ZONE_;
    }

    public String getSOUS_CATEGORIE() {
        return SOUS_CATEGORIE;
    }

    public void setSOUS_CATEGORIE(String SOUS_CATEGORIE) {
        this.SOUS_CATEGORIE = SOUS_CATEGORIE;
    }

    public String getAVEC_RC_ELEVE() {
        return AVEC_RC_ELEVE;
    }

    public void setAVEC_RC_ELEVE(String AVEC_RC_ELEVE) {
        this.AVEC_RC_ELEVE = AVEC_RC_ELEVE;
    }

    public List<ZenGarantie> getListeGaranties() {
        return listeGaranties;
    }

    public void setListeGaranties(List<ZenGarantie> listeGaranties) {
        this.listeGaranties = listeGaranties;
    }

    public String getNOM_BUREAU() {
        return NOM_BUREAU;
    }

    public void setNOM_BUREAU(String NOM_BUREAU) {
        this.NOM_BUREAU = NOM_BUREAU;
    }

}
