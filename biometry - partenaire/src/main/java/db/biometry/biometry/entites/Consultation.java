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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "consultation")
@NamedQueries({
    @NamedQuery(name = "Consultation.findAll", query = "SELECT c FROM Consultation c"),
    @NamedQuery(name = "Consultation.findByIdConsultation", query = "SELECT c FROM Consultation c WHERE c.idConsultation = :idConsultation"),
    @NamedQuery(name = "Consultation.findByCodePrestataire", query = "SELECT c FROM Consultation c WHERE c.codePrestataire = :codePrestataire"),
    @NamedQuery(name = "Consultation.findByCodeAssurePrincipal", query = "SELECT c FROM Consultation c WHERE c.codeAssurePrincipal = :codeAssurePrincipal"),
    @NamedQuery(name = "Consultation.findByCodeBeneficiaire", query = "SELECT c FROM Consultation c WHERE c.codeBeneficiaire = :codeBeneficiaire"),
    @NamedQuery(name = "Consultation.findByCodeTypePrestation", query = "SELECT c FROM Consultation c WHERE c.codeTypePrestation = :codeTypePrestation"),
    @NamedQuery(name = "Consultation.findByMontant", query = "SELECT c FROM Consultation c WHERE c.montant = :montant"),
    @NamedQuery(name = "Consultation.findByTauxCouverture", query = "SELECT c FROM Consultation c WHERE c.tauxCouverture = :tauxCouverture"),
    @NamedQuery(name = "Consultation.findByDate", query = "SELECT c FROM Consultation c WHERE c.date = :date")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_consultation")
    private int idConsultation;
    @Basic(optional = false)
    @Column(name = "code_prestataire")
    private String codePrestataire;
    @Basic(optional = false)
    @Column(name = "code_assure_principal")
    private String codeAssurePrincipal;
    @Column(name = "code_beneficiaire")
    private String codeBeneficiaire;
    @Basic(optional = false)
    @Column(name = "code_type_prestation")
    private String codeTypePrestation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "taux_couverture")
    private Double tauxCouverture;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

  
    
}
