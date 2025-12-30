package db.biometry.biometry.services;


import db.biometry.biometry.dto.AuthResponse;
import db.biometry.biometry.dto.UserLogin;
import db.biometry.biometry.dto.UserSessionDTO;
import db.biometry.biometry.entites.Employe;
import db.biometry.biometry.entites.Utilisateur;
import db.biometry.biometry.exceptions.UtilisateurException;
import db.biometry.biometry.mapper.MapperDtoImpl;
import db.biometry.biometry.repository.EmployeRepository;
import db.biometry.biometry.repository.UtilisateurRepository;
import db.biometry.biometry.utils.Crypto;
import db.biometry.biometry.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Service d'authentification JWT
 * Gère la génération et validation des tokens
 * 
 * @author JIATOU FRANCK
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationService {

    private final UtilisateurRepository utilisateurRepository;
    private final EmployeRepository employeRepository;
    private final MapperDtoImpl mappers;
    private final JwtUtil jwtUtil;

    /**
     * 🔐 Authentification complète avec génération de tokens JWT
     */
    public AuthResponse authenticate(UserLogin userLogin) {
        log.info("🔐 Authentification de l'utilisateur: {}", userLogin.getUsername());
        
        // Étape 1 : Validation des credentials
        Utilisateur user = validateCredentials(userLogin);
        
        // Étape 2 : Récupération des informations employé
        Employe employe = getEmployeByLogin(userLogin.getUsername());
        
        // Étape 3 : Création du DTO de session
        UserSessionDTO userSessionDTO = mappers.mapUserSessionDTOByuserDTO(employe);
        
        // Étape 4 : Génération des tokens JWT
        String role = employe.getProfilId() != null ? 
                employe.getProfilId().getCode() : "USER";
        
        String accessToken = jwtUtil.generateAccessToken(
                user.getLogin(), 
                role, 
                user.getId()
        );
        
        String refreshToken = jwtUtil.generateRefreshToken(user.getLogin());
        
        // Étape 5 : Calcul de la date d'expiration
        Date expiresAtDate = jwtUtil.calculateAccessTokenExpiryDate();
        String expiresAt = jwtUtil.formatExpiryDate(expiresAtDate);
        
        // Étape 6 : Mise à jour de la dernière connexion
        updateLastLogin(user);
        
        log.info("✅ Authentification réussie pour: {} (Role: {})", 
                userLogin.getUsername(), role);
        
        // Étape 7 : Construction de la réponse
        return AuthResponse.builder()
                .userSessionDTO(userSessionDTO)
                .token(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(expiresAt)
                .expiresAtDate(expiresAtDate)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.calculateAccessTokenExpiryDate().getTime() - System.currentTimeMillis())
                .build();
    }

    /**
     * 🔄 Rafraîchit un access token à partir d'un refresh token
     */
    public AuthResponse refreshToken(String refreshToken) {
        log.info("🔄 Rafraîchissement du token");
        
        try {
            // Validation du refresh token
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new UtilisateurException("Refresh token invalide ou expiré");
            }
            
            // Extraction du username
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            
            // Récupération de l'utilisateur
            Utilisateur user = utilisateurRepository.findByLogin(username);
            if (user == null) {
                throw new UtilisateurException("Utilisateur non trouvé");
            }
            
            // Vérification du statut
            if (!"1".equals(user.getStatut())) {
                throw new UtilisateurException("Compte désactivé");
            }
            
            // Récupération de l'employé
            Employe employe = getEmployeByLogin(username);
            UserSessionDTO userSessionDTO = mappers.mapUserSessionDTOByuserDTO(employe);
            
            // Génération d'un nouveau access token
            String role = employe.getProfilId() != null ? 
                    employe.getProfilId().toString() : "USER";
            
            String newAccessToken = jwtUtil.generateAccessToken(
                    username, 
                    role, 
                    user.getId()
            );
            
            // Optionnel : Générer aussi un nouveau refresh token
            String newRefreshToken = jwtUtil.generateRefreshToken(username);
            
            Date expiresAtDate = jwtUtil.calculateAccessTokenExpiryDate();
            String expiresAt = jwtUtil.formatExpiryDate(expiresAtDate);
            
            log.info("✅ Token rafraîchi avec succès pour: {}", username);
            
            return AuthResponse.builder()
                    .userSessionDTO(userSessionDTO)
                    .token(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .expiresAt(expiresAt)
                    .expiresAtDate(expiresAtDate)
                    .tokenType("Bearer")
                    .expiresIn(expiresAtDate.getTime() - System.currentTimeMillis())
                    .build();
                    
        } catch (Exception e) {
            log.error("❌ Erreur lors du rafraîchissement du token: {}", e.getMessage());
            throw new UtilisateurException("Impossible de rafraîchir le token: " + e.getMessage());
        }
    }

    /**
     * ✅ Vérifie la validité d'un token
     */
    public boolean verifyToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                log.warn("⚠️ Token vide fourni pour vérification");
                return false;
            }
            
            // Enlever "Bearer " si présent
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            boolean isValid = jwtUtil.validateToken(token);
            
            if (isValid) {
                String username = jwtUtil.getUsernameFromToken(token);
                
                // Vérifier que l'utilisateur existe toujours
                Utilisateur user = utilisateurRepository.findByLogin(username);
                if (user == null || !"1".equals(user.getStatut())) {
                    log.warn("⚠️ Utilisateur {} non trouvé ou désactivé", username);
                    return false;
                }
                
                log.debug("✅ Token valide pour: {}", username);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            log.error("❌ Erreur lors de la vérification du token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 📋 Obtient les informations du token
     */
    public UserSessionDTO getUserFromToken(String token) {
        try {
            // Enlever "Bearer " si présent
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (!jwtUtil.validateToken(token)) {
                throw new UtilisateurException("Token invalide");
            }
            
            String username = jwtUtil.getUsernameFromToken(token);
            Employe employe = getEmployeByLogin(username);
            
            return mappers.mapUserSessionDTOByuserDTO(employe);
            
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'extraction des infos utilisateur: {}", e.getMessage());
            throw new UtilisateurException("Impossible d'extraire les informations utilisateur");
        }
    }

    /**
     * 🔍 Valide les credentials de l'utilisateur
     */
    private Utilisateur validateCredentials(UserLogin userLogin) {
        // Vérification de l'existence
        Utilisateur user = utilisateurRepository.findByLogin(userLogin.getUsername());
        if (user == null) {
            log.warn("⚠️ Utilisateur non trouvé: {}", userLogin.getUsername());
            throw new UtilisateurException("Nom d'utilisateur ou mot de passe incorrect");
        }
        
        // Vérification du mot de passe
        String encryptedPassword = Crypto.loginBiometrie(userLogin.getPassword());
        if (!encryptedPassword.equals(user.getMotPasse())) {
            log.warn("⚠️ Mot de passe incorrect pour: {}", userLogin.getUsername());
            throw new UtilisateurException("Nom d'utilisateur ou mot de passe incorrect");
        }
        
        // Vérification du statut
        if (!"1".equals(user.getStatut())) {
            log.warn("⚠️ Compte désactivé: {}", userLogin.getUsername());
            throw new UtilisateurException("Votre compte est désactivé. Contactez l'administrateur.");
        }
        
        return user;
    }

    /**
     * 👤 Récupère l'employé par login
     */
    private Employe getEmployeByLogin(String login) {
        return employeRepository.findByUtilisateurLogin(login)
                .orElseThrow(() -> {
                    log.error("❌ Employé non trouvé pour le login: {}", login);
                    return new UtilisateurException("Informations employé non trouvées");
                });
    }

    /**
     * 📅 Met à jour la date de dernière connexion
     */
    private void updateLastLogin(Utilisateur user) {
        try {
            // Si vous avez un champ lastLogin dans votre entité Utilisateur
            // user.setLastLogin(LocalDateTime.now());
            // utilisateurRepository.save(user);
            
            log.debug("📅 Dernière connexion mise à jour pour: {}", user.getLogin());
        } catch (Exception e) {
            log.warn("⚠️ Impossible de mettre à jour la dernière connexion: {}", e.getMessage());
            // Ne pas bloquer la connexion pour cette erreur non critique
        }
    }
}