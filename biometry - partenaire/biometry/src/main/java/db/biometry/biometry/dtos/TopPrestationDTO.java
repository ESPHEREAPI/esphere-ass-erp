/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
public class TopPrestationDTO {
  private String  typeId;
    private String nom;
    private String categorie;
    private Long nombreConsultations;
    private BigDecimal totalDepenses;

    public TopPrestationDTO(String typeId, String nom, String categorie, Long nombreConsultations, Double totalDepenses) {
        this.typeId = typeId;
        this.nom = nom;
        this.categorie = categorie;
        this.nombreConsultations = nombreConsultations;
        this.totalDepenses = new BigDecimal(totalDepenses.doubleValue());
    }
    
    
}
