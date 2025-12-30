/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.io.Serializable;

/**
 * DTO pour les statistiques mensuelles
 * Utilisé pour alimenter les graphiques du tableau de bord
 * 
 * @author Votre Nom
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesMensuellesDTO implements Serializable{
    
    
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Nom du mois (ex: "Janvier", "Février", etc.)
     */
    private String mois;
    
    /**
     * Nombre de consultations encaissées pour ce mois
     */
    private Long consultations;
    
    /**
     * Nombre d'ordonnances encaissées pour ce mois
     */
    private Long ordonnances;
    
    /**
     * Nombre d'examens encaissés pour ce mois
     */
    private Long examens;
    
    /**
     * Montant total des consultations pour ce mois (optionnel)
     */
    private Double montantConsultations;
    
    /**
     * Montant total des ordonnances pour ce mois (optionnel)
     */
    private Double montantOrdonnances;
    
    /**
     * Montant total des examens pour ce mois (optionnel)
     */
    private Double montantExamens;
    
    /**
     * Constructeur pour les statistiques de base sans montants
     * 
     * @param mois Nom du mois
     * @param consultations Nombre de consultations
     * @param ordonnances Nombre d'ordonnances
     * @param examens Nombre d'examens
     */
    public StatistiquesMensuellesDTO(String mois, Long consultations, 
                                     Long ordonnances, Long examens) {
        this.mois = mois;
        this.consultations = consultations;
        this.ordonnances = ordonnances;
        this.examens = examens;
    }
}
