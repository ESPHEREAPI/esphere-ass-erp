/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zenbio.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 *
 * @author USER01
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TauxPrestationResponse {
private String error;
private List<PrestationTaux>tabTauxPrestation;
}
