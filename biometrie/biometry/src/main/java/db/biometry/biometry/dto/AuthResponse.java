package db.biometry.biometry.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO pour la réponse d'authentification avec tokens JWT
 * Correspond exactement aux attentes du frontend Angular
 * 
 * @author JIATOU FRANCK
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    /**
     * Données de l'utilisateur connecté
     */
    @JsonProperty("userDTO")
    private UserSessionDTO userSessionDTO;
    
    /**
     * Access token JWT (validité courte - 24h)
     */
    @JsonProperty("token")
    private String token;
    
    /**
     * Refresh token JWT (validité longue - 7 jours)
     */
    @JsonProperty("refreshToken")
    private String refreshToken;
    
    /**
     * Date d'expiration du token au format ISO
     */
    @JsonProperty("expiresAt")
    private String expiresAt;
    
    /**
     * Date d'expiration du token (Date Java)
     */
    @JsonProperty("expiresAtDate")
    private Date expiresAtDate;
    
    /**
     * Type de token (Bearer)
     */
    @JsonProperty("tokenType")
    private String tokenType = "Bearer";
    
    /**
     * Durée de validité en millisecondes
     */
    @JsonProperty("expiresIn")
    private Long expiresIn;
}