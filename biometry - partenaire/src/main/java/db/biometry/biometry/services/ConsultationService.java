/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;


import db.biometry.biometry.entites.Dbx45tyConsultation;
import db.biometry.biometry.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
/**
 *
 * @author USER01
 */
/**
 * Service de gestion des consultations
 * Contient la logique métier pour les opérations sur les consultations
 * 
 * @author Votre Nom
 * @version 1.0
 */
@Service
@Transactional
public class ConsultationService {
    @Autowired
    private ConsultationRepository consultationRepository;
    
    // Constante pour le statut "non supprimé"
    private static final String NON_SUPPRIME = "-1";

    /**
     * Compte le nombre de consultations par état
     * Exclut les consultations supprimées
     * 
     * @param etat État de la consultation (EN_ATTENTE, ENCAISSE, REJETE, VALIDE)
     * @return Nombre de consultations dans cet état
     */
    @Transactional(readOnly = true)
    public Long countByEtat(String etat) {
        try {
            // Validation du paramètre
            if (etat == null || etat.trim().isEmpty()) {
                throw new IllegalArgumentException("L'état ne peut pas être vide");
            }
            
            // Appel du repository
            Long count = consultationRepository
                .countByEtatConsultationAndSupprime(etat, NON_SUPPRIME);
            
            // Retourne 0 si null
            return count != null ? count : 0L;
            
        } catch (Exception e) {
            // Log de l'erreur et propagation
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du comptage des consultations par état", e);
        }
    }

    /**
     * Récupère toutes les consultations
     * 
     * @return Liste de toutes les consultations
     */
    @Transactional(readOnly = true)
    public List<Dbx45tyConsultation> findAll() {
        return consultationRepository.findAll();
    }

    /**
     * Récupère une consultation par son ID
     * 
     * @param id Identifiant de la consultation
     * @return Optional contenant la consultation si trouvée
     */
    @Transactional(readOnly = true)
    public Optional<Dbx45tyConsultation> findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
        return consultationRepository.findById(id);
    }

    /**
     * Crée une nouvelle consultation
     * 
     * @param consultation Consultation à créer
     * @return Consultation créée avec son ID généré
     */
    public Dbx45tyConsultation create(Dbx45tyConsultation consultation) {
        try {
            // Validation des données obligatoires
            if (consultation == null) {
                throw new IllegalArgumentException("La consultation ne peut pas être nulle");
            }
            
            if (consultation.getNatureConsultation() == null || 
                consultation.getNatureConsultation().trim().isEmpty()) {
                throw new IllegalArgumentException("La nature de la consultation est obligatoire");
            }
            
            if (consultation.getDate() == null) {
                throw new IllegalArgumentException("La date de la consultation est obligatoire");
            }
            
            // Par défaut, une nouvelle consultation est en attente
            if (consultation.getEtatConsultation() == null) {
                consultation.setEtatConsultation("EN_ATTENTE");
            }
            
            // Par défaut, une consultation n'est pas supprimée
            if (consultation.getSupprime() == null) {
                consultation.setSupprime(NON_SUPPRIME);
            }
            
            // Sauvegarde
            return consultationRepository.save(consultation);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la consultation", e);
        }
    }

    /**
     * Met à jour une consultation existante
     * 
     * @param id ID de la consultation à mettre à jour
     * @param consultation Nouvelles données de la consultation
     * @return Consultation mise à jour
     */
    public Dbx45tyConsultation update(Integer id, Dbx45tyConsultation consultation) {
        try {
            // Vérification de l'existence
            Optional<Dbx45tyConsultation> existingConsultation = consultationRepository.findById(id);
            
            if (!existingConsultation.isPresent()) {
                throw new RuntimeException("Consultation non trouvée avec l'ID: " + id);
            }
            
            // Mise à jour
            consultation.setId(id);
            return consultationRepository.save(consultation);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de la consultation", e);
        }
    }

    /**
     * Suppression logique d'une consultation
     * Met le champ 'supprime' à 'OUI'
     * 
     * @param id ID de la consultation à supprimer
     */
    public void delete(Integer id) {
        try {
            Optional<Dbx45tyConsultation> consultation = consultationRepository.findById(id);
            
            if (!consultation.isPresent()) {
                throw new RuntimeException("Consultation non trouvée avec l'ID: " + id);
            }
            
            // Suppression logique
            Dbx45tyConsultation cons = consultation.get();
            cons.setSupprime("OUI");
            consultationRepository.save(cons);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression de la consultation", e);
        }
    }

    /**
     * Change l'état d'une consultation
     * 
     * @param id ID de la consultation
     * @param nouvelEtat Nouvel état (EN_ATTENTE, ENCAISSE, REJETE, VALIDE)
     * @return Consultation mise à jour
     */
    public Dbx45tyConsultation changerEtat(Integer id, String nouvelEtat) {
        try {
            Optional<Dbx45tyConsultation> consultation = consultationRepository.findById(id);
            
            if (!consultation.isPresent()) {
                throw new RuntimeException("Consultation non trouvée avec l'ID: " + id);
            }
            
            Dbx45tyConsultation cons = consultation.get();
            cons.setEtatConsultation(nouvelEtat);
            
            // Met à jour la date de validation/rejet si applicable
            if (nouvelEtat.equals("VALIDE") || nouvelEtat.equals("REJETE") || 
                nouvelEtat.equals("ENCAISSE")) {
                cons.setDateValideRejete(new java.util.Date());
            }
            
            return consultationRepository.save(cons);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du changement d'état de la consultation", e);
        }
    }
}
