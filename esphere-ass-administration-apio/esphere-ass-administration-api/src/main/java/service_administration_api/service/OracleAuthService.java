/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import service_administration_api.DTO.ApiResponse;
import service_administration_api.DTO.RoleDTO;
import service_administration_api.DTO.UserDTO;
import service_administration_api.DTO.UserSessionDTO;
import service_administration_api.JwtTokenProvider;
import service_administration_api.mapper.MapperDtoImpl;
import service_administration_api.utils.JwtExpiration;
import java.util.Date;

/**
 *
 * @author USER01
 */
/**
 * Service d'authentification Oracle avec support JWT ✅ Compatible avec le
 * frontend Angular
 */
@Service
public class OracleAuthService {

    private static final Logger logger = LoggerFactory.getLogger(OracleAuthService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MapperDtoImpl mapper;

    @Value("${oracle.jdbc.url:jdbc:oracle:thin:@77.68.94.193:1521:ZINSDB}")
    private String oracleJdbcUrl;

    // Stockage en mémoire des refresh tokens actifs
    // En production, utiliser Redis ou une base de données
    private final Map<String, String> activeRefreshTokens = new ConcurrentHashMap<>();

    /**
     * Obtenir le statut du compte Oracle
     */
    private String getAccountStatus(String username) {
        String sql = "SELECT ACCOUNT_STATUS FROM DBA_USERS WHERE USERNAME = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username.toUpperCase()}, String.class);
        } catch (Exception e) {
            logger.error("Error getting account status for user: {}", username, e);
            return null;
        }
    }

    /**
     * Tenter une connexion Oracle pour valider les credentials
     */
    private boolean tryOracleLogin(String username, String password) {
        try (Connection conn = DriverManager.getConnection(oracleJdbcUrl, username, password)) {
            logger.info("Oracle authentication successful for user: {}", username);
            return true;
        } catch (SQLException e) {
            logger.warn("Oracle authentication failed for user: {}", username);
            return false;
        }
    }

    /**
     * 🔐 Authentification complète avec génération de tokens JWT ✅ Retourne
     * UserSessionDTO avec expiresAt en timestamp (long)
     */
    public ApiResponse<UserSessionDTO> authenticate(String username, String password) {
        logger.info("Authentication attempt for user: {}", username);

        // Vérifier si l'utilisateur existe
        String accountStatus = getAccountStatus(username);
        if (accountStatus == null) {
            logger.warn("User not found: {}", username);
            return ApiResponse.error("Utilisateur inexistant", "USER_NOT_FOUND");
        }

        // Vérifier le statut du compte
        switch (accountStatus) {
            case "LOCKED":
            case "LOCKED(TIMED)":
                logger.warn("Account locked for user: {}", username);
                return ApiResponse.error("Compte verrouillé", "ACCOUNT_LOCKED");

            case "EXPIRED":
            case "EXPIRED & LOCKED":
                logger.warn("Password expired for user: {}", username);
                return ApiResponse.error("Mot de passe expiré", "PASSWORD_EXPIRED");

            case "EXPIRED(GRACE)":
                logger.warn("Password in grace period for user: {}", username);
                return ApiResponse.error("Mot de passe expiré (période de grâce)", "PASSWORD_GRACE");

            /**
             * 🔐 Authentification complète avec génération de tokens JWT ✅
             */
            // ... votre code de vérification ...
            case "OPEN":
                if (!tryOracleLogin(username, password)) {
                    logger.warn("Invalid password for user: {}", username);
                    return ApiResponse.error("Mot de passe incorrect", "INVALID_PASSWORD");
                }

                try {
                    String token = jwtTokenProvider.generateToken(username);
                    String refreshToken = jwtTokenProvider.generateRefreshToken(username);

                    // ⭐ Calculer la date d'expiration en Date (pas LocalDateTime)
                    long expiresAtTimestamp = jwtTokenProvider.calculateExpirationTimestamp();
                    Date expiresAt = new Date(expiresAtTimestamp);

                    // Vérification de sécurité
                    Date now = new Date();
                    logger.info("🕐 Token expiration check:");
                    logger.info("  Now: {}", now);
                    logger.info("  ExpiresAt: {}", expiresAt);
                    logger.info("  Diff (minutes): {}", (expiresAt.getTime() - now.getTime()) / 60000);

                    if (expiresAt.before(now)) {
                        logger.error("❌ CRITICAL: expiresAt is in the past!");
                        expiresAt = new Date(System.currentTimeMillis() + 3600000); // Force 1h
                    }

                    activeRefreshTokens.put(username, refreshToken);

                    UserDTO userDTO = mapper.mapUserSessionDTOByuserDTO(username);
                    if (userDTO == null) {
                        userDTO = createDefaultUserDTO(username);
                    }

                    // ⭐ Créer la session avec Date
                    UserSessionDTO session = new UserSessionDTO(
                            userDTO,
                            token,
                            refreshToken,
                            expiresAt
                    );

                    logger.info("✅ Authentication successful for user: {}", username);
                    return ApiResponse.success("Connexion réussie", session);

                } catch (Exception e) {
                    logger.error("Error generating tokens for user: {}", username, e);
                    return ApiResponse.error("Erreur lors de la génération des tokens", "TOKEN_GENERATION_ERROR");
                }

            default:
                logger.warn("Unknown account status for user {}: {}", username, accountStatus);
                return ApiResponse.error("État du compte inconnu : " + accountStatus, "UNKNOWN_STATUS");
        }

    }

    /**
     * 🔄 Rafraîchir le token JWT
     */
   
/**
 * 🔄 Rafraîchir le token JWT
 */
public ApiResponse<UserSessionDTO> refreshToken(String refreshToken) {
    try {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            logger.warn("Invalid refresh token");
            return ApiResponse.error("Refresh token invalide", "INVALID_REFRESH_TOKEN");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String storedToken = activeRefreshTokens.get(username);
        
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            logger.warn("Refresh token not found or expired for user: {}", username);
            return ApiResponse.error("Refresh token expiré ou invalide", "REFRESH_TOKEN_EXPIRED");
        }

        String newToken = jwtTokenProvider.generateToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);
        
        // ⭐ Date au lieu de LocalDateTime
        long expiresAtTimestamp = jwtTokenProvider.calculateExpirationTimestamp();
        Date expiresAt = new Date(expiresAtTimestamp);

        activeRefreshTokens.put(username, newRefreshToken);

        UserDTO userDTO = mapper.mapUserSessionDTOByuserDTO(username);
        if (userDTO == null) {
            userDTO = createDefaultUserDTO(username);
        }

        UserSessionDTO session = new UserSessionDTO(
            userDTO,
            newToken,
            newRefreshToken,
            expiresAt
        );
        
        logger.info("Token refreshed successfully for user: {}", username);
        return ApiResponse.success("Token rafraîchi avec succès", session);
        
    } catch (Exception e) {
        logger.error("Error refreshing token", e);
        return ApiResponse.error("Erreur lors du rafraîchissement du token", "REFRESH_ERROR");
    }
    }

    /**
     * 🚪 Déconnexion
     */
    public ApiResponse<Void> logout(String token) {
        try {
            // Extraire le username du token
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // Supprimer le refresh token actif
            activeRefreshTokens.remove(username);

            logger.info("User logged out successfully: {}", username);
            return ApiResponse.success("Déconnexion réussie", null);

        } catch (Exception e) {
            logger.error("Error during logout", e);
            return ApiResponse.error("Erreur lors de la déconnexion", "LOGOUT_ERROR");
        }
    }

    /**
     * ✅ Vérifier la validité d'un token
     */
    public ApiResponse<Map<String, Object>> verifyToken(String token) {
        try {
            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                boolean isExpired = jwtTokenProvider.isTokenExpired(token);

                Map<String, Object> result = Map.of(
                        "valid", true,
                        "username", username,
                        "expired", isExpired
                );

                logger.info("Token verified for user: {}", username);
                return ApiResponse.success("Token valide", result);
            } else {
                logger.warn("Invalid token");
                return ApiResponse.error("Token invalide", "INVALID_TOKEN");
            }
        } catch (Exception e) {
            logger.error("Error verifying token", e);
            return ApiResponse.error("Erreur lors de la vérification du token", "VERIFY_ERROR");
        }
    }

    /**
     * Créer un UserDTO par défaut si le mapper ne retourne rien
     */
    private UserDTO createDefaultUserDTO(String username) {
        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .email(username + "@zenithe.com").build();

        // Extraire le nom/prénom depuis le username Oracle si possible
        String[] parts = username.split("_");
        if (parts.length >= 2) {
            userDTO.setFirstName(parts[0]);
            userDTO.setLastName(parts[1]);
        } else {
            userDTO.setFirstName(username);
            userDTO.setLastName("");
        }

        // Rôle par défaut
        RoleDTO role = new RoleDTO("USER", "Utilisateur standard");
        userDTO.setRole(role);

        return userDTO;
    }

    /**
     * Vérifier si un utilisateur est connecté
     */
    public boolean isUserLoggedIn(String username) {
        return activeRefreshTokens.containsKey(username);
    }

    /**
     * Obtenir tous les utilisateurs connectés (pour admin/monitoring)
     */
    public int getActiveUsersCount() {
        return activeRefreshTokens.size();
    }

}
