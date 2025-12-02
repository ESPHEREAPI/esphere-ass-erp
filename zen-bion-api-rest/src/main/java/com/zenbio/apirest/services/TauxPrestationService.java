/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zenbio.apirest.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.zenbio.apirest.CustomProperties;
import com.zenbio.apirest.dto.PrestationTaux;
import com.zenbio.apirest.dto.TauxPrestationResponse;
import com.zenbio.apirest.entites.Dbx45tyTypePrestation;
import com.zenbio.apirest.entites.TauxPrestation;
import com.zenbio.apirest.repertory.TauxPrestationRepository;
import com.zenbio.apirest.repertory.TypePrestationRepository;
import com.zenbio.apirest.utiles.WebServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.dao.DataIntegrityViolationException;

/**
 *
 * @author USER01
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TauxPrestationService {

    private final CustomProperties property;
    private final WebServices urlService;
    private final TauxPrestationRepository tauxPrestationRepository;
    private final TypePrestationRepository typePrestationRepository;

  @Transactional
public List<TauxPrestation> chargerTauxPrestation(String agencePolice) {
    List<TauxPrestation> listTauxPrestation = new ArrayList<>();

    try {
        if (agencePolice == null || !agencePolice.contains("-")) {
            throw new IllegalArgumentException("Format invalide : attendu 'agence-police'");
        }

        String[] parties = agencePolice.split("-");
        String agence = parties[0].trim();
        String police = parties[1].trim();
        
        log.info("🔍 Chargement des taux pour police: {}", agencePolice);

        InputStream responseStream = urlService.urlBiometries(property.getTaux_couvertures_prestations());
        if (responseStream == null) {
            log.error("Flux de réponse nul");
            return listTauxPrestation;
        }

        String jsonResponse;
        try (responseStream) {
            jsonResponse = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        //objectMapper.configure(DeserializationFeature.COERCE_SCALARS, true);

        TauxPrestationResponse response = objectMapper.readValue(jsonResponse, TauxPrestationResponse.class);

        if (response.getError() != null && !response.getError().isEmpty()) {
            log.error("Erreur service distant: {}", response.getError());
            return listTauxPrestation;
        }

        if (response.getTabTauxPrestation() == null || response.getTabTauxPrestation().isEmpty()) {
            log.warn("Aucune prestation reçue");
            return listTauxPrestation;
        }

        log.info("📦 Total prestations reçues: {}", response.getTabTauxPrestation().size());

        // ✅ Filtrer et valider AVANT de créer
        List<PrestationTaux> prestationsValides = response.getTabTauxPrestation().stream()
                .filter(tp -> tp.getCodeinte() != null && tp.getNumepoli() != null)
                .filter(tp -> tp.getTypconsu() != null && !tp.getTypconsu().trim().isEmpty()) // ✅ Filtrer les NULL
                .filter(tp -> {
                    String tpCodeinte = tp.getCodeinte().trim();
                    String tpNumepoli = tp.getNumepoli().trim();
                    return agence.equals(tpCodeinte) && police.equals(tpNumepoli);
                })
                .collect(Collectors.toList());

        log.info("✓ Prestations valides filtrées: {}", prestationsValides.size());

        // ✅ Traiter chaque prestation individuellement
        for (PrestationTaux tp : prestationsValides) {
            try {
                TauxPrestation created = creerTauxPrestation(tp);
                if (created != null) {
                    listTauxPrestation.add(created);
                }
            } catch (Exception e) {
                log.error("❌ Erreur traitement prestation: {}", tp, e);
                // Continuer avec les autres
            }
        }

        log.info("✅ {} taux de prestation créés/récupérés", listTauxPrestation.size());

    } catch (Exception e) {
        log.error("❌ Erreur lors du chargement des taux de prestation", e);
    }

    return listTauxPrestation;
}
   private TauxPrestation creerTauxPrestation(PrestationTaux tpr) {
    try {
        // ✅ Valider les données obligatoires d'abord
        if (tpr.getTypconsu() == null || tpr.getTypconsu().trim().isEmpty()) {
            log.warn("⚠️ Type prestation NULL ou vide pour: {}", tpr);
            return null; // Ignorer cet enregistrement
        }
        
        short groupe = Short.parseShort(tpr.getNumegrou());
        float plafond = Float.parseFloat(tpr.getValeplaf() == null ? "0" : tpr.getValeplaf());
        int taux = Integer.parseInt(tpr.getTauxcouv());
        String police = tpr.getCodeinte() + "-" + tpr.getNumepoli();
        String typePrestation = normalizeString(tpr.getCodepres());
        
        // ✅ Double vérification après normalisation
        if (typePrestation == null || typePrestation.trim().isEmpty()) {
            log.warn("⚠️ Type prestation vide après normalisation pour: {}", tpr);
            return null;
        }

        // Vérifier si le taux existe déjà
        Optional<TauxPrestation> existing = tauxPrestationRepository
                .findByPoliceAndTypePrestationIdAndGroupe(police, typePrestation, groupe);
  
        if (existing.isPresent()) {
            log.debug("✓ TauxPrestation existant: id={} police={} type={} groupe={}",
                    existing.get().getId(), police, typePrestation, groupe);
            return existing.get();
        }

        // ✅ Créer SANS assigner d'ID (laisser la BDD le faire)
        TauxPrestation nouveau = TauxPrestation.builder()
                // .id(idmax+1)  ❌ NE JAMAIS FAIRE ÇA avec IDENTITY
                .groupe(groupe)
                .plafond(plafond)
                .police(police)
                .taux(taux)
                .typePrestationId(typePrestation)
                .build();

        TauxPrestation saved = tauxPrestationRepository.save(nouveau);
        log.info("✅ TauxPrestation créé: id={} police={} type={} groupe={}",
                saved.getId(), police, typePrestation, groupe);
        
        return saved;

    } catch (DataIntegrityViolationException e) {
        log.warn("⚠️ Conflit détecté, récupération depuis la base");
        try {
            String police = tpr.getCodeinte() + "-" + tpr.getNumepoli();
            String typePrestation = normalizeString(tpr.getTypconsu());
            short groupe = Short.parseShort(tpr.getNumegrou());
            
            if (typePrestation != null && !typePrestation.trim().isEmpty()) {
                return tauxPrestationRepository
                        .findByPoliceAndTypePrestationIdAndGroupe(police, typePrestation, groupe)
                        .orElse(null);
            }
        } catch (Exception ex) {
            log.error("❌ Erreur lors de la récupération: {}", ex.getMessage());
        }
        return null;
        
    } catch (NumberFormatException e) {
        log.error("❌ Erreur de conversion numérique: {}", tpr, e);
        return null;
        
    } catch (Exception e) {
        log.error("❌ Erreur inattendue: {}", tpr, e);
        return null;
    }
}

// ✅ Améliorer la méthode de normalisation
private String normalizeString(String input) {
    if (input == null || input.trim().isEmpty()) {
        return null;
    }
    String normalized = java.text.Normalizer.normalize(input.trim(), java.text.Normalizer.Form.NFC);
    return normalized.isEmpty() ? null : normalized;
}

}
