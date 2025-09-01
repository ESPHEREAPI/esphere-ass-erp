/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Entity
@Data
@Table(name = "DETAIL_POSTE_TRAVAIL", schema = "ORASSADM",
       uniqueConstraints = {
           @UniqueConstraint(name = "UNI_DETAIL_POSTE_TRAVAIL", 
                           columnNames = {"NOM_UTIL", "CODEINTE", "CODEBRAN", "CODECATE"})
       })
public class DetailPosteTravail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Clé primaire technique auto-générée
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detail_poste_seq")
    @SequenceGenerator(name = "detail_poste_seq", 
                      sequenceName = "SEQ_DETAIL_POSTE_TRAVAIL", 
                      schema = "ORASSADM",
                      allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    
    /**
     * Nom de l'utilisateur - Partie de la contrainte d'unicité métier
     */
    @Column(name = "NOM_UTIL", length = 30, nullable = false)
    private String nomUtil;

    /**
     * Code interne - Partie de la contrainte d'unicité métier
     */
    @Column(name = "CODEINTE", precision = 5)
    private Integer codeInte;

    /**
     * Code branche - Partie de la contrainte d'unicité métier
     */
    @Column(name = "CODEBRAN", precision = 2, nullable = false)
    private Integer codeBran;

    /**
     * Code catégorie - Partie de la contrainte d'unicité métier
     */
    @Column(name = "CODECATE", precision = 5)
    private Integer codeCate;

    /**
     * Validation AN - Valeur par défaut 'O'
     */
    @Column(name = "VALI__AN", length = 1, columnDefinition = "VARCHAR2(1) DEFAULT 'O'")
    private String valiAn = "O";

    /**
     * Validation AV - Valeur par défaut 'O'
     */
    @Column(name = "VALI__AV", length = 1, columnDefinition = "VARCHAR2(1) DEFAULT 'O'")
    private String valiAv = "O";

    /**
     * Stock attendu
     */
    @Column(name = "STOCATTE", length = 1)
    private String stocAtte;

    // Relations avec les autres entités (optionnelles)
    
    /**
     * Relation vers la table BRANCHE
     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "CODEBRAN", referencedColumnName = "CODEBRAN")
//    private Branche branche;
//
//    /**
//     * Relation vers la table CATEGORIE
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "CODECATE", referencedColumnName = "CODECATE")
//    private Categorie categorie;
//
//    /**
//     * Relation vers la table POSTE_TRAVAIL
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "NOM_UTIL", referencedColumnName = "NOM_UTIL")
//    private PosteTravail posteTravail;

    // Constructeurs
    public DetailPosteTravail() {
        super();
    }

    public DetailPosteTravail(String nomUtil, Integer codeInte, Integer codeBran, Integer codeCate) {
        this.nomUtil = nomUtil;
        this.codeInte = codeInte;
        this.codeBran = codeBran;
        this.codeCate = codeCate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailPosteTravail)) {
            return false;
        }
        DetailPosteTravail other = (DetailPosteTravail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.DetailPosteTravail[ id=" + id + " ]";
    }
    
}
