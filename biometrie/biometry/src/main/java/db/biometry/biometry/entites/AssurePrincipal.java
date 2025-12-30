/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entites;



import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "assure_principal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQueries({
    @NamedQuery(name = "AssurePrincipal.findAll", query = "SELECT a FROM AssurePrincipal a"),
    @NamedQuery(name = "AssurePrincipal.findByCodeAssurePrincipal", query = "SELECT a FROM AssurePrincipal a WHERE a.id.codeAssurePrincipal = :codeAssurePrincipal"),
    @NamedQuery(name = "AssurePrincipal.findByPolice", query = "SELECT a FROM AssurePrincipal a WHERE a.id.police = :police"),
    @NamedQuery(name = "AssurePrincipal.findByGroupe", query = "SELECT a FROM AssurePrincipal a WHERE a.id.groupe = :groupe")
})
public class AssurePrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AssurePrincipalId id; // clé composite

    @Column(name = "effet_police")
    @Temporal(TemporalType.DATE)
    private Date effetPolice;

    @Column(name = "echeance_police")
    @Temporal(TemporalType.DATE)
    private Date echeancePolice;
}
