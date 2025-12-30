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
@Table(name = "ligne_prestation")

@NamedQueries({
    @NamedQuery(name = "LignePrestations.findAll", query = "SELECT l FROM LignePrestation l"),
    @NamedQuery(name = "LignePrestations.findByIdLignePrestation", query = "SELECT l FROM LignePrestationsVues l WHERE l.idLignePrestation = :idLignePrestation"),
    @NamedQuery(name = "LignePrestations.findByIdPrestation", query = "SELECT l FROM LignePrestationsVues l WHERE l.idPrestation = :idPrestation"),
    @NamedQuery(name = "LignePrestations.findByCodePrestataire", query = "SELECT l FROM LignePrestationsVues l WHERE l.codePrestataire = :codePrestataire"),
    @NamedQuery(name = "LignePrestations.findByCodeAssurePrincipal", query = "SELECT l FROM LignePrestationsVues l WHERE l.codeAssurePrincipal = :codeAssurePrincipal"),
    @NamedQuery(name = "LignePrestations.findByCodeBeneficiaire", query = "SELECT l FROM LignePrestationsVues l WHERE l.codeBeneficiaire = :codeBeneficiaire"),
    @NamedQuery(name = "LignePrestations.findByCodeTypePrestation", query = "SELECT l FROM LignePrestationsVues l WHERE l.codeTypePrestation = :codeTypePrestation"),
    @NamedQuery(name = "LignePrestations.findByNom", query = "SELECT l FROM LignePrestationsVues l WHERE l.nom = :nom"),
    @NamedQuery(name = "LignePrestations.findByMontant", query = "SELECT l FROM LignePrestationsVues l WHERE l.montant = :montant"),
    @NamedQuery(name = "LignePrestations.findByTaux", query = "SELECT l FROM LignePrestationsVues l WHERE l.taux = :taux"),
    @NamedQuery(name = "LignePrestations.findByDate", query = "SELECT l FROM LignePrestationsVues l WHERE l.date = :date")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LignePrestationsVues implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_ligne_prestation")
    private int idLignePrestation;
    @Basic(optional = false)
    @Column(name = "id_prestation")
    private int idPrestation;
    @Column(name = "code_prestataire")
    private String codePrestataire;
    @Basic(optional = false)
    @Column(name = "code_assure_principal")
    private String codeAssurePrincipal;
    @Column(name = "code_beneficiaire")
    private String codeBeneficiaire;
    @Column(name = "code_type_prestation")
    private String codeTypePrestation;
    @Column(name = "nom")
    private String nom;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "taux")
    private Double taux;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

   
    
}
