package db.biometry.biometry.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilitaire pour la gestion des JWT (JSON Web Tokens)
 * Compatible avec JJWT 0.12.x et Java 17/21
 * 
 * @author JIATOU FRANCK
 */
@Component
@Slf4j
public class JwtUtil {

    // Durées d'expiration (en millisecondes)
    private static final long ACCESS_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 heures
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 jours
    
    // Clé secrète (doit être dans application.properties en production)
    @Value("${jwt.secret:P4ssw0rd#2024!S3cur1ty@K3y$ForJWT&T0k3n*Authent1cat10n_VeryLongSecretKeyForHS512Algorithm}")
    private String jwtSecret;
    
    @Value("${jwt.access-token-expiration:86400000}") // 24 heures par défaut
    private long accessTokenExpiration;
    
    @Value("${jwt.refresh-token-expiration:604800000}") // 7 jours par défaut
    private long refreshTokenExpiration;

    /**
     * Génère la clé secrète pour signer les tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 🔐 Génère un access token pour un utilisateur
     */
    public String generateAccessToken(String username, String role, Integer userId) {
        log.debug("🔑 Génération d'un access token pour: {}", username);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);
        claims.put("type", "ACCESS");
        
        return createToken(claims, username, accessTokenExpiration);
    }

    /**
     * 🔄 Génère un refresh token pour un utilisateur
     */
    public String generateRefreshToken(String username) {
        log.debug("🔄 Génération d'un refresh token pour: {}", username);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "REFRESH");
        
        return createToken(claims, username, refreshTokenExpiration);
    }

    /**
     * 🛠️ Crée un token JWT avec les claims spécifiés
     * MISE À JOUR pour JJWT 0.12.x
     */
    private String createToken(Map<String, Object> claims, String subject, long validityInMilliseconds) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);
        
        // Nouvelle API JJWT 0.12.x
        return Jwts.builder()
                .claims(claims)  // Changé de setClaims() à claims()
                .subject(subject)  // Changé de setSubject() à subject()
                .issuedAt(now)  // Changé de setIssuedAt() à issuedAt()
                .expiration(expiryDate)  // Changé de setExpiration() à expiration()
                .signWith(getSigningKey())  // Simplifié - l'algorithme est détecté automatiquement
                .compact();
    }

    /**
     * ✅ Valide un token JWT
     * MISE À JOUR pour JJWT 0.12.x
     */
    public boolean validateToken(String token) {
        try {
            // Nouvelle API JJWT 0.12.x
            Jwts.parser()  // Changé de parserBuilder() à parser()
                    .verifyWith(getSigningKey())  // Changé de setSigningKey() à verifyWith()
                    .build()
                    .parseSignedClaims(token);  // Changé de parseClaimsJws() à parseSignedClaims()
            
            log.debug("✅ Token valide");
            return true;
            
        } catch (SignatureException e) {
            log.error("❌ Signature JWT invalide: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("❌ Token JWT mal formé: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("⏰ Token JWT expiré: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("❌ Token JWT non supporté: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("❌ JWT claims string est vide: {}", e.getMessage());
        }
        
        return false;
    }

    /**
     * 📝 Extrait le nom d'utilisateur du token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 📅 Extrait la date d'expiration du token
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 🏷️ Extrait le rôle de l'utilisateur du token
     */
    public String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    /**
     * 🆔 Extrait l'ID de l'utilisateur du token
     */
    public Integer getUserIdFromToken(String token) {
        return getAllClaimsFromToken(token).get("userId", Integer.class);
    }

    /**
     * 🔍 Extrait un claim spécifique du token
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 📋 Extrait tous les claims du token
     * MISE À JOUR pour JJWT 0.12.x
     */
    private Claims getAllClaimsFromToken(String token) {
        // Nouvelle API JJWT 0.12.x
        return Jwts.parser()  // Changé de parserBuilder() à parser()
                .verifyWith(getSigningKey())  // Changé de setSigningKey() à verifyWith()
                .build()
                .parseSignedClaims(token)  // Changé de parseClaimsJws() à parseSignedClaims()
                .getPayload();  // Changé de getBody() à getPayload()
    }

    /**
     * ⏰ Vérifie si le token est expiré
     */
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            boolean expired = expiration.before(new Date());
            
            if (expired) {
                log.warn("⏰ Token expiré depuis: {}", expiration);
            }
            
            return expired;
            
        } catch (ExpiredJwtException e) {
            log.warn("⏰ Token déjà expiré: {}", e.getMessage());
            return true;
        } catch (Exception e) {
            log.error("❌ Erreur lors de la vérification de l'expiration: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 📅 Calcule la date d'expiration pour un access token
     */
    public Date calculateAccessTokenExpiryDate() {
        return new Date(System.currentTimeMillis() + accessTokenExpiration);
    }

    /**
     * 📅 Calcule la date d'expiration pour un refresh token
     */
    public Date calculateRefreshTokenExpiryDate() {
        return new Date(System.currentTimeMillis() + refreshTokenExpiration);
    }

    /**
     * 🔄 Rafraîchit un access token à partir d'un refresh token valide
     */
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token invalide ou expiré");
        }
        
        String username = getUsernameFromToken(refreshToken);
        Claims claims = getAllClaimsFromToken(refreshToken);
        
        // Vérifier que c'est bien un refresh token
        String tokenType = claims.get("type", String.class);
        if (!"REFRESH".equals(tokenType)) {
            throw new IllegalArgumentException("Ce n'est pas un refresh token");
        }
        
        log.info("🔄 Rafraîchissement du token pour: {}", username);
        
        // On devrait récupérer le rôle depuis la base de données
        // Pour l'instant, on retourne un token simple
        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put("type", "ACCESS");
        
        return createToken(newClaims, username, accessTokenExpiration);
    }

    /**
     * 📊 Obtient des informations sur le token
     */
    public Map<String, Object> getTokenInfo(String token) {
        Map<String, Object> info = new HashMap<>();
        
        try {
            Claims claims = getAllClaimsFromToken(token);
            
            info.put("username", claims.getSubject());
            info.put("issuedAt", claims.getIssuedAt());
            info.put("expiresAt", claims.getExpiration());
            info.put("role", claims.get("role"));
            info.put("userId", claims.get("userId"));
            info.put("type", claims.get("type"));
            info.put("isExpired", isTokenExpired(token));
            
            // Calcul du temps restant
            long timeRemaining = claims.getExpiration().getTime() - System.currentTimeMillis();
            info.put("timeRemainingMs", Math.max(0, timeRemaining));
            info.put("timeRemainingMinutes", Math.max(0, timeRemaining / (60 * 1000)));
            
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'extraction des infos du token: {}", e.getMessage());
            info.put("error", e.getMessage());
        }
        
        return info;
    }

    /**
     * 🕐 Convertit une date d'expiration en format ISO
     */
    public String formatExpiryDate(Date expiryDate) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(
                expiryDate.toInstant(), 
                ZoneId.systemDefault()
        );
        return localDateTime.toString();
    }

    /**
     * 📝 Log les informations de configuration JWT
     */
    public void logConfiguration() {
        log.info("⚙️ Configuration JWT:");
        log.info("  Access Token Validity: {} ms ({} heures)", 
                accessTokenExpiration, 
                accessTokenExpiration / (60 * 60 * 1000));
        log.info("  Refresh Token Validity: {} ms ({} jours)", 
                refreshTokenExpiration, 
                refreshTokenExpiration / (24 * 60 * 60 * 1000));
        log.info("  Secret Key Length: {} caractères", jwtSecret.length());
    }
}