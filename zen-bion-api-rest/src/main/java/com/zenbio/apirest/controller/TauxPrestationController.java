/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zenbio.apirest.controller;


import com.zenbio.apirest.dto.ApiResponse;
import com.zenbio.apirest.entites.TauxPrestation;
import com.zenbio.apirest.services.TauxPrestationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taux-prestations")
@RequiredArgsConstructor
/**
 *
 * @author USER01
 */
public class TauxPrestationController {
    private final TauxPrestationService tauxPrestationService;

    @GetMapping("/{agencePolice}")
    public ResponseEntity<ApiResponse<List<TauxPrestation>>> getTauxPrestations(
            @PathVariable String agencePolice) {
        try {
            List<TauxPrestation> tauxList = tauxPrestationService.chargerTauxPrestation(agencePolice);

            if (tauxList == null || tauxList.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success(tauxList, "Aucun taux trouvé"));
            }

            return ResponseEntity.ok(ApiResponse.success(tauxList, "Taux de prestation chargés avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Erreur lors du chargement des taux de prestation : " + e.getMessage(), 500));
        }
    }
}
