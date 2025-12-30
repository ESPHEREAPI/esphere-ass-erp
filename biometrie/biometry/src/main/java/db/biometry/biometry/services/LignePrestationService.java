/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;


import db.biometry.biometry.entites.LignePrestation;
import db.biometry.biometry.repository.LignePrestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 *
 * @author USER01
 */
@Service
@Transactional
public class LignePrestationService {
     @Autowired
    private LignePrestationRepository lignePrestationRepository;
    
    // Constante pour le statut "non supprimé"
    private static final String NON_SUPPRIME = "-1";

    // ==================== MÉTHODES POUR LES ORDONNANCES ====================
    
    /**
     * Compte le nombre d'ordonnances par état
     * Une ordonnance est une ligne de prestation avec medicamentId non null
     * 
     * @param etat État de l'ordonnance (EN_ATTENTE, ENCAISSE, REJETE, VALIDE)
     * @return Nombre d'ordonnances dans cet état
     */
    @Transactional(readOnly = true)
    public Long countOrdonnancesByEtat(String etat) {
        try {
            // Validation du paramètre
            if (etat == null || etat.trim().isEmpty()) {
                throw new IllegalArgumentException("L'état ne peut pas être vide");
            }
            
            // Appel du repository avec filtre sur medicamentId
            Long count = lignePrestationRepository
                .countByMedicamentIdIsNotNullAndEtatAndSupprime(etat,NON_SUPPRIME);
            
            // Retourne 0 si null
            return count != null ? count : 0L;
            
        } catch (Exception e) {
            // Log de l'erreur et propagation
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du comptage des ordonnances par état", e);
        }
    }

    // ==================== MÉTHODES POUR LES EXAMENS ====================
    
    /**
     * Compte le nombre d'examens par état
     * Un examen est une ligne de prestation avec examenId non null
     * 
     * @param etat État de l'examen (EN_ATTENTE, ENCAISSE, REJETE, VALIDE)
     * @return Nombre d'examens dans cet état
     */
    @Transactional(readOnly = true)
    public Long countExamensByEtat(String etat) {
        try {
            // Validation du paramètre
            if (etat == null || etat.trim().isEmpty()) {
                throw new IllegalArgumentException("L'état ne peut pas être vide");
            }
            
            // Appel du repository avec filtre sur examenId
            Long count = lignePrestationRepository
                .countByMedicamentIdIsNullAndEtatAndSupprime(etat, NON_SUPPRIME);
            
            // Retourne 0 si null
            return count != null ? count : 0L;
            
        } catch (Exception e) {
            // Log de l'erreur et propagation
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du comptage des examens par état", e);
        }
    }

    // ==================== MÉTHODES GÉNÉRALES ====================
    
    /**
     * Récupère toutes les lignes de prestation
     * 
     * @return Liste de toutes les lignes de prestation
     */
    @Transactional(readOnly = true)
    public List<LignePrestation> findAll() {
        return lignePrestationRepository.findAll();
    }

    /**
     * Récupère une ligne de prestation par son ID
     * 
     * @param id Identifiant de la ligne de prestation
     * @return Optional contenant la ligne de prestation si trouvée
     */
    @Transactional(readOnly = true)
    public Optional<LignePrestation> findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
        return lignePrestationRepository.findById(id);
    }

    /**
     * Crée une nouvelle ligne de prestation
     * 
     * @param lignePrestation Ligne de prestation à créer
     * @return Ligne de prestation créée avec son ID généré
     */
    public LignePrestation create(LignePrestation lignePrestation) {
        try {
            // Validation des données obligatoires
            if (lignePrestation == null) {
                throw new IllegalArgumentException("La ligne de prestation ne peut pas être nulle");
            }
            
            if (lignePrestation.getPrestationId() == null) {
                throw new IllegalArgumentException("La prestation est obligatoire");
            }
            
            if (lignePrestation.getDate() == null) {
                throw new IllegalArgumentException("La date de la prestation est obligatoire");
            }
            
            // Par défaut, une nouvelle ligne est en attente
            if (lignePrestation.getEtat() == null) {
                lignePrestation.setEtat("EN_ATTENTE");
            }
            
            // Par défaut, une ligne n'est pas supprimée
            if (lignePrestation.getSupprime() == null) {
                lignePrestation.setSupprime(NON_SUPPRIME);
            }
            
            // Sauvegarde
            return lignePrestationRepository.save(lignePrestation);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la ligne de prestation", e);
        }
    }

    /**
     * Met à jour une ligne de prestation existante
     * 
     * @param id ID de la ligne de prestation à mettre à jour
     * @param lignePrestation Nouvelles données de la ligne de prestation
     * @return Ligne de prestation mise à jour
     */
    public LignePrestation update(Integer id, LignePrestation lignePrestation) {
        try {
            // Vérification de l'existence
            Optional<LignePrestation> existingLigne = lignePrestationRepository.findById(id);
            
            if (!existingLigne.isPresent()) {
                throw new RuntimeException("Ligne de prestation non trouvée avec l'ID: " + id);
            }
            
            // Mise à jour
            lignePrestation.setId(id);
            return lignePrestationRepository.save(lignePrestation);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de la ligne de prestation", e);
        }
    }

    /**
     * Suppression logique d'une ligne de prestation
     * Met le champ 'supprime' à 'OUI'
     * 
     * @param id ID de la ligne de prestation à supprimer
     */
    public void delete(Integer id) {
        try {
            Optional<LignePrestation> lignePrestation = lignePrestationRepository.findById(id);
            
            if (!lignePrestation.isPresent()) {
                throw new RuntimeException("Ligne de prestation non trouvée avec l'ID: " + id);
            }
            
            // Suppression logique
            LignePrestation ligne = lignePrestation.get();
            ligne.setSupprime("OUI");
            lignePrestationRepository.save(ligne);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression de la ligne de prestation", e);
        }
    }

    /**
     * Change l'état d'une ligne de prestation
     * 
     * @param id ID de la ligne de prestation
     * @param nouvelEtat Nouvel état (EN_ATTENTE, ENCAISSE, REJETE, VALIDE)
     * @return Ligne de prestation mise à jour
     */
    public LignePrestation changerEtat(Integer id, String nouvelEtat) {
        try {
            Optional<LignePrestation> lignePrestation = lignePrestationRepository.findById(id);
            
            if (!lignePrestation.isPresent()) {
                throw new RuntimeException("Ligne de prestation non trouvée avec l'ID: " + id);
            }
            
            LignePrestation ligne = lignePrestation.get();
            ligne.setEtat(nouvelEtat);
            
            // Met à jour la date de validation/rejet si applicable
            if (nouvelEtat.equals("valide") || nouvelEtat.equals("rejete")) {
                ligne.setDateValideRejete(new java.util.Date());
            }
            
            // Met à jour la date d'encaissement si applicable
            if (nouvelEtat.equals("ENCAISSE")) {
                ligne.setDateEncaisse(new java.util.Date());
            }
            
            return lignePrestationRepository.save(ligne);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du changement d'état de la ligne de prestation", e);
        }
    }

    /**
     * Valide plusieurs lignes de prestation en une seule transaction
     * 
     * @param ids Liste des IDs à valider
     * @return Nombre de lignes validées
     */
    public int validerMultiple(List<Integer> ids) {
        int count = 0;
        try {
            for (Integer id : ids) {
                changerEtat(id, "valide");
                count++;
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la validation multiple", e);
        }
    }

    /**
     * Encaisse plusieurs lignes de prestation en une seule transaction
     * 
     * @param ids Liste des IDs à encaisser
     * @return Nombre de lignes encaissées
     */
    public int encaisserMultiple(List<Integer> ids) {
        int count = 0;
        try {
            for (Integer id : ids) {
                changerEtat(id, "encaisse");
                count++;
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'encaissement multiple", e);
        }
    }
}
