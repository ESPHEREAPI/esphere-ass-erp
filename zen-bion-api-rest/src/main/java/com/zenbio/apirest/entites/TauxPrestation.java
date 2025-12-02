/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zenbio.apirest.entites;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "dbx45ty_taux_prestation")
@NamedQueries({
    @NamedQuery(name = "TauxPrestation.findAll", query = "SELECT d FROM TauxPrestation d"),
    @NamedQuery(name = "TauxPrestation.findById", query = "SELECT d FROM TauxPrestation d WHERE d.id = :id"),
    @NamedQuery(name = "TauxPrestation.findByTypePrestationId", query = "SELECT d FROM TauxPrestation d WHERE d.typePrestationId = :typePrestationId"),
    @NamedQuery(name = "TauxPrestation.findByPolice", query = "SELECT d FROM TauxPrestation d WHERE d.police = :police"),
    @NamedQuery(name = "TauxPrestation.findByGroupe", query = "SELECT d FROM TauxPrestation d WHERE d.groupe = :groupe"),
    @NamedQuery(name = "TauxPrestation.findByTaux", query = "SELECT d FROM TauxPrestation d WHERE d.taux = :taux"),
    @NamedQuery(name = "TauxPrestation.findByPlafond", query = "SELECT d FROM TauxPrestation d WHERE d.plafond = :plafond")})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TauxPrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "type_prestation_id")
    private String typePrestationId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "police")
    private String police;
    @Basic(optional = false)
    @NotNull
    @Column(name = "groupe")
    private short groupe;
    @Column(name = "taux")
    private Integer taux;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "plafond")
    private Float plafond;

   
    
}
