/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 *
 * @author USER01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO  implements Serializable{
     private static final long serialVersionUID = 1L;
    
    /**
     * Nombre de consultations en attente de validation
     */
    private Long nbreConsultationsEnAttente;
    
    /**
     * Nombre d'ordonnances en attente de validation
     */
    private Long nbreOrdonnancesEnAttente;
    
    /**
     * Nombre d'examens en attente de validation
     */
    private Long nbreExamensEnAttente;
    
    /**
     * Montant total des consultations en attente (optionnel)
     */
    private Double montantTotalConsultations;
    
    /**
     * Montant total des ordonnances en attente (optionnel)
     */
    private Double montantTotalOrdonnances;
    
    /**
     * Montant total des examens en attente (optionnel)
     */
    private Double montantTotalExamens;
}
