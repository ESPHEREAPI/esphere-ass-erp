package db.biometry.biometry.services;

import db.biometry.biometry.dto.UserDTO;
import db.biometry.biometry.dto.UserLogin;
import db.biometry.biometry.dto.UtilisateurDto;
import db.biometry.biometry.entites.Utilisateur;
import db.biometry.biometry.exceptions.UtilisateurException;

import db.biometry.biometry.mapper.MapperDtoImpl;
import db.biometry.biometry.repository.UtilisateurRepository;
import db.biometry.biometry.utils.Crypto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion des utilisateurs
 * Amélioré avec logging détaillé et gestion d'erreurs
 * 
 * @author JIATOU FRANCK
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final MapperDtoImpl mappers;

    @Override
    public UserDTO findUserByLogin(UserLogin userLog) {
        log.info("🔍 Recherche de l'utilisateur: {}", userLog.getUsername());
        
        UserDTO userdto = new UserDTO();
        
        try {
            // Étape 1 : Vérification de l'existence de l'utilisateur
            Utilisateur user = utilisateurRepository.findByLogin(userLog.getUsername());

            if (user == null) {
                log.warn("⚠️ Utilisateur '{}' non trouvé dans la base de données", userLog.getUsername());
                userdto.setEcheck_connection(true);
                userdto.setMessageEcheck("Utilisateur non trouvé. Veuillez vérifier votre nom d'utilisateur.");
                return userdto;
            }

            log.debug("✅ Utilisateur '{}' trouvé (ID: {})", userLog.getUsername(), user.getId());

            // Étape 2 : Vérification du mot de passe
            String encryptedPassword = Crypto.loginBiometrie(userLog.getPassword());
            
            if (!encryptedPassword.equals(user.getMotPasse())) {
                log.warn("⚠️ Mot de passe incorrect pour l'utilisateur: {}", userLog.getUsername());
                userdto.setEcheck_connection(true);
                userdto.setMessageEcheck("Mot de passe incorrect. Veuillez réessayer.");
                return userdto;
            }

            log.debug("✅ Mot de passe correct pour: {}", userLog.getUsername());

            // Étape 3 : Vérification du statut de l'utilisateur
            if (!"1".equals(user.getStatut())) {
                log.warn("⚠️ Compte désactivé pour l'utilisateur: {} (Statut: {})", 
                    userLog.getUsername(), 
                    user.getStatut()
                );
                userdto.setEcheck_connection(true);
                userdto.setMessageEcheck(
                    "Votre compte est désactivé. Veuillez contacter l'administrateur."
                );
                return userdto;
            }

            log.debug("✅ Compte actif pour: {}", userLog.getUsername());

            // Étape 4 : Connexion réussie
            userdto.setUsername(user.getLogin());
            userdto.setEcheck_connection(false);
            
            log.info("✅ Authentification réussie pour: {}", userLog.getUsername());
            
            return userdto;
            
        } catch (Exception e) {
            log.error("💥 Erreur inattendue lors de l'authentification de '{}': {}", 
                userLog.getUsername(), 
                e.getMessage(), 
                e
            );
            
            userdto.setEcheck_connection(true);
            userdto.setMessageEcheck(
                "Une erreur technique est survenue. Veuillez réessayer plus tard."
            );
            return userdto;
        }
    }

    @Override
    public List<UtilisateurDto> listeUtilisateur() {
        log.info("📋 Récupération de la liste complète des utilisateurs");
        
        try {
            List<UtilisateurDto> users = utilisateurRepository.findAll()
                .stream()
                .map(mappers::formUtilisateur)
                .collect(Collectors.toList());
            
            log.info("✅ {} utilisateurs récupérés", users.size());
            
            return users;
            
        } catch (Exception e) {
            log.error("❌ Erreur lors de la récupération de la liste des utilisateurs: {}", 
                e.getMessage(), 
                e
            );
            throw new UtilisateurException(
                "Erreur lors de la récupération de la liste des utilisateurs"
            );
        }
    }

    @Override
    public UtilisateurDto getUser(int userId) {
        log.info("🔍 Recherche de l'utilisateur avec ID: {}", userId);
        
        try {
            Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("⚠️ Aucun utilisateur trouvé avec l'ID: {}", userId);
                    return new UtilisateurException(
                        "Utilisateur non trouvé avec l'ID: " + userId
                    );
                });
            
            log.info("✅ Utilisateur trouvé: {} (Login: {})", userId, user.getLogin());
            
            return mappers.formUtilisateur(user);
            
        } catch (UtilisateurException e) {
            throw e;
            
        } catch (Exception e) {
            log.error("❌ Erreur lors de la récupération de l'utilisateur {}: {}", 
                userId, 
                e.getMessage(), 
                e
            );
            throw new UtilisateurException(
                "Erreur lors de la récupération de l'utilisateur"
            );
        }
    }

    @Override
    public List<UtilisateurDto> seacrhUsers(String keyword) {
        log.info("🔎 Recherche d'utilisateurs avec le mot-clé: '{}'", keyword);
        
        try {
            List<UtilisateurDto> users = utilisateurRepository.searchUser(keyword)
                .stream()
                .map(mappers::formUtilisateur)
                .collect(Collectors.toList());
            
            log.info("✅ {} utilisateurs trouvés pour '{}'", users.size(), keyword);
            
            return users;
            
        } catch (Exception e) {
            log.error("❌ Erreur lors de la recherche d'utilisateurs avec '{}': {}", 
                keyword, 
                e.getMessage(), 
                e
            );
            throw new UtilisateurException(
                "Erreur lors de la recherche d'utilisateurs"
            );
        }
    }
}