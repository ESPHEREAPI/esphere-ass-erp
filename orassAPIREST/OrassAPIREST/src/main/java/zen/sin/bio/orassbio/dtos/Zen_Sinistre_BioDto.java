/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.dtos;

import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
public class Zen_Sinistre_BioDto {

    private String prestataire;
    private Integer numepoli;
    private Integer codeinte;
    private Integer coderisq;
    private Integer codememb;
    private String typeexam;
    private String date_validation;
    private String date_ecaissement;
    private Double montant_valid;
    private Double taux_remb;
    private String code_pres_bio;

}
