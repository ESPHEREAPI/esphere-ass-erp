/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zenbio.apirest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER01
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrestationTaux {
    
    @JsonProperty("CODEINTE")
    private String codeinte;
    
    @JsonProperty("NUMEPOLI")
    private String numepoli;
    
    @JsonProperty("NUMEGROU")
    private String numegrou;
    
    @JsonProperty("AVENMODI")
    private String avenmodi;
    
    @JsonProperty("CODEPRES")
    private String codepres;
    
    @JsonProperty("TYPCONSU")
    private String typconsu;
    
    @JsonProperty("TAUXCOUV")
    private String tauxcouv;
    
    @JsonProperty("VALEPLAF")
    private String valeplaf;
}
