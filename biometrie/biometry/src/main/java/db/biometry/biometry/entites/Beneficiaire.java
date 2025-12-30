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
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "beneficiaire")
@NamedQueries({
    @NamedQuery(name = "Beneficiaire.findAll", query = "SELECT b FROM Beneficiaire b"),
    @NamedQuery(name = "Beneficiaire.findByCodeBeneficiaire", query = "SELECT b FROM Beneficiaire b WHERE b.codeBeneficiaire = :codeBeneficiaire"),
    @NamedQuery(name = "Beneficiaire.findByCodeAssurePrincipal", query = "SELECT b FROM Beneficiaire b WHERE b.codeAssurePrincipal = :codeAssurePrincipal")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "code_beneficiaire")
    private String codeBeneficiaire;
    @Column(name = "code_assure_principal")
    private String codeAssurePrincipal;

   
}
