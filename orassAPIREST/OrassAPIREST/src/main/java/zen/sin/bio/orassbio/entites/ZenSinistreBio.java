///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package zen.sin.bio.orassbio.entites;
//
//import java.io.Serializable;
//import java.util.Date;
////import javax.persistence.Basic;
////import javax.persistence.Column;
////import javax.persistence.EmbeddedId;
////import javax.persistence.Entity;
////import javax.persistence.GeneratedValue;
////import javax.persistence.GenerationType;
////import javax.persistence.Id;
////import javax.persistence.NamedQueries;
////import javax.persistence.NamedQuery;
////import javax.persistence.Table;
////import javax.persistence.Temporal;
////import javax.persistence.TemporalType;
////import javax.persistence.UniqueConstraint;
//import lombok.Data;
//
///**
// *
// * @author USER01
// */
//@Entity
//@Table(name = "ZEN_SINISTRE_BIO",uniqueConstraints=@UniqueConstraint(columnNames={"PRESTATAIRE","NUMEPOLI","CODEINTE","CODERISQ","CODEMEMB","TYPE_EXAM","DATE_VALIDATION"}))
//@NamedQueries({
//    @NamedQuery(name = "ZenSinistreBio.findAll", query = "SELECT z FROM ZenSinistreBio z"),
//    @NamedQuery(name = "ZenSinistreBio.findByPrestataire", query = "SELECT z FROM ZenSinistreBio z WHERE z.prestataire = :prestataire"),
//    @NamedQuery(name = "ZenSinistreBio.findByNumepoli", query = "SELECT z FROM ZenSinistreBio z WHERE z.numepoli = :numepoli"),
//    @NamedQuery(name = "ZenSinistreBio.findByCodeinte", query = "SELECT z FROM ZenSinistreBio z WHERE z.codeinte = :codeinte"),
//    @NamedQuery(name = "ZenSinistreBio.findByCoderisq", query = "SELECT z FROM ZenSinistreBio z WHERE z.coderisq = :coderisq"),
//    @NamedQuery(name = "ZenSinistreBio.findByCodememb", query = "SELECT z FROM ZenSinistreBio z WHERE z.codememb = :codememb"),
//    @NamedQuery(name = "ZenSinistreBio.findByTypeExam", query = "SELECT z FROM ZenSinistreBio z WHERE z.typeexam = :typeExam"),
//    @NamedQuery(name = "ZenSinistreBio.findByDateValidation", query = "SELECT z FROM ZenSinistreBio z WHERE z.date_validation = :dateValidation"),
//    @NamedQuery(name = "ZenSinistreBio.findByDateEcaissement", query = "SELECT z FROM ZenSinistreBio z WHERE z.date_ecaissement = :dateEcaissement"),
//    @NamedQuery(name = "ZenSinistreBio.findByMontantValid\u00e9", query = "SELECT z FROM ZenSinistreBio z WHERE z.montant_valid = :montantValid"),
//    @NamedQuery(name = "ZenSinistreBio.findByTauxRemb", query = "SELECT z FROM ZenSinistreBio z WHERE z.taux_remb = :tauxRemb")})
//@Data
//public class ZenSinistreBio implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "Id")
//    private Long id;
//    @Column(name = "PRESTATAIRE")
//    private String prestataire;
//    @Column(name = "NUMEPOLI")
//    private long numepoli;
//    @Column(name = "CODEINTE")
//    private Integer codeinte;
//    @Column(name = "CODERISQ")
//    private Integer coderisq;
//    @Column(name = "CODEMEMB")
//    private Integer codememb;
//    @Column(name = "TYPE_EXAM")
//    private String typeexam;
//    @Basic(optional = false)
//    @Column(name = "DATE_VALIDATION")
//    @Temporal(TemporalType.DATE)
//    private Date date_validation;
//    @Column(name = "DATE_ECAISSEMENT")
//    @Temporal(TemporalType.DATE)
//    private Date date_ecaissement;
//    @Column(name = "MONTANT_VALIDE")
//    private Long montant_valid;
//    @Column(name = "TAUX_REMB")
//    private Short taux_remb;
//     @Column(name = "CODEPRESBIO")
//    private String code_pres_bio;
//
//    public ZenSinistreBio() {
//    }
//
//  
//    public ZenSinistreBio(String prestataire, long numepoli, int codeinte, int coderisq, Integer codememb, String typeExam, Date dateValidation) {
//       this.prestataire=prestataire;
//       this.codeinte=codeinte;
//       this.coderisq=coderisq;
//       this.codememb=codememb;
//       this.typeexam=typeExam;
//       this.date_validation=dateValidation;
//       this.numepoli=numepoli;
//    }
//
//   
//}
