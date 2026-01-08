package db.biometry.biometry.web;

import db.biometry.biometry.dto.ApiResponse;
import db.biometry.biometry.dto.AuthResponse;
import db.biometry.biometry.dto.UserLogin;
import db.biometry.biometry.dto.UserSessionDTO;
import db.biometry.biometry.dto.UtilisateurDto;
import db.biometry.biometry.exceptions.UtilisateurException;
import db.biometry.biometry.mapper.MapperDtoImpl;
import db.biometry.biometry.repository.EmployeRepository;
import db.biometry.biometry.services.JwtAuthenticationService;
import db.biometry.biometry.services.UtilisateurService;
import db.biometry.biometry.utils.JwtUtil;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//import javax.validation.Valid;

/**
 * Contrôleur REST optimisé pour l'authentification JWT
 * Adapté pour le frontend Angular avec AdminLTE
 * 
 * @author JIATOU FRANCK
 * @version 2.0 - Optimisé avec JWT
 */

/**
 * Contrôleur REST pour la gestion des utilisateurs et de l'authentification
 * Amélioré pour une meilleure gestion des erreurs et logging
 * 
 * @author JIATOU FRANCK
 */
@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", maxAge = 3600) // À adapter selon vos besoins
public class UtilisateurRestController  {

    private final UtilisateurService utilisateurService;
    private final MapperDtoImpl mappers;
    private final EmployeRepository employeRepository;

    private final JwtAuthenticationService jwtAuthService;
 
    private final JwtUtil jwtUtil;

    /**
     * 🔐 Endpoint de connexion avec JWT
     * POST /auth/users/login
     * 
     * @param userLogin Credentials de l'utilisateur
     * @return ApiResponse avec AuthResponse contenant les tokens
     */
    @PostMapping("/users/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody UserLogin userLogin) {
        log.info("🔐 Tentative de connexion pour: {}", userLogin.getUsername());
        
        try {
            // Validation des données d'entrée
            if (userLogin.getUsername() == null || userLogin.getUsername().trim().isEmpty()) {
                log.warn("⚠️ Tentative de connexion avec username vide");
                return ResponseEntity
                        .badRequest()
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Le nom d'utilisateur est requis")
                                .data(null)
                                .build());
            }

            if (userLogin.getPassword() == null || userLogin.getPassword().trim().isEmpty()) {
                log.warn("⚠️ Tentative de connexion avec mot de passe vide pour: {}", 
                        userLogin.getUsername());
                return ResponseEntity
                        .badRequest()
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Le mot de passe est requis")
                                .data(null)
                                .build());
            }

            // Authentification avec génération des tokens JWT
            AuthResponse authResponse = jwtAuthService.authenticate(userLogin);

            log.info("✅ Connexion réussie pour: {} - Token généré", 
                    userLogin.getUsername());

            return ResponseEntity
                    .ok()
                    .body(ApiResponse.builder()
                            .success(true)
                            .message("Connexion réussie")
                            .data(authResponse)
                            .timestamp(LocalDateTime.now())
                            .build());
            
            

        } catch (UtilisateurException e) {
            log.error("❌ Erreur d'authentification pour: {} - {}", 
                    userLogin.getUsername(), 
                    e.getMessage());
            
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
                
        } catch (Exception e) {
            log.error("💥 Erreur inattendue lors de la connexion pour: {} - {}", 
                    userLogin.getUsername(), 
                    e.getMessage(), 
                    e);
            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Une erreur interne est survenue. Veuillez réessayer.")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * 🚪 Endpoint de déconnexion
     * POST /auth/users/logout
     * 
     * @param authHeader Header Authorization avec le token
     */
    @PostMapping("/users/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        log.info("🚪 Déconnexion utilisateur");
        
        try {
            // Optionnel : Blacklister le token (nécessite Redis ou une table en BDD)
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtil.getUsernameFromToken(token);
                log.info("🚪 Déconnexion de: {}", username);
                
                // TODO: Ajouter le token à une blacklist si nécessaire
            }
            
            return ResponseEntity
                    .ok()
                    .body(ApiResponse.builder()
                            .success(true)
                            .message("Déconnexion réussie")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la déconnexion: {}", e.getMessage(), e);
            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Erreur lors de la déconnexion")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * 🔄 Endpoint de rafraîchissement du token
     * POST /auth/users/refresh
     * 
     * @param request Contient le refreshToken
     */
    @PostMapping("/users/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody Map<String, String> request) {
        log.info("🔄 Rafraîchissement du token");
        
        try {
            String refreshToken = request.get("refreshToken");
            
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                log.warn("⚠️ Refresh token manquant");
                return ResponseEntity
                        .badRequest()
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Refresh token requis")
                                .data(null)
                                .build());
            }

            // Rafraîchissement du token
            AuthResponse authResponse = jwtAuthService.refreshToken(refreshToken);

            log.info("✅ Token rafraîchi avec succès pour: {}", 
                    authResponse.getUserSessionDTO().getUserDTO());

            return ResponseEntity
                    .ok()
                    .body(ApiResponse.builder()
                            .success(true)
                            .message("Token rafraîchi avec succès")
                            .data(authResponse)
                            .timestamp(LocalDateTime.now())
                            .build());
                
        } catch (UtilisateurException e) {
            log.error("❌ Erreur lors du rafraîchissement: {}", e.getMessage());
            
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
                
        } catch (Exception e) {
            log.error("💥 Erreur inattendue lors du rafraîchissement: {}", e.getMessage(), e);
            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Impossible de rafraîchir le token")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * ✅ Endpoint de vérification du token
     * GET /auth/users/verify
     * 
     * @param authHeader Header Authorization avec le token
     */
    @GetMapping("/users/verify")
    public ResponseEntity<ApiResponse> verifyToken(
            @RequestHeader(value = "Authorization", required = true) String authHeader) {
        
        log.info("🔍 Vérification du token");
        
        try {
            if (!authHeader.startsWith("Bearer ")) {
                log.warn("⚠️ Format de token invalide");
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Format de token invalide")
                                .data(null)
                                .build());
            }

            String token = authHeader.substring(7);
            boolean isValid = jwtAuthService.verifyToken(token);

            if (isValid) {
                // Récupérer les informations utilisateur du token
                UserSessionDTO userInfo = jwtAuthService.getUserFromToken(token);
                
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("valid", true);
                responseData.put("user", userInfo);
                responseData.put("tokenInfo", jwtUtil.getTokenInfo(token));

                log.info("✅ Token valide pour: {}", userInfo.getUserDTO());

                return ResponseEntity
                        .ok()
                        .body(ApiResponse.builder()
                                .success(true)
                                .message("Token valide")
                                .data(responseData)
                                .timestamp(LocalDateTime.now())
                                .build());
            } else {
                log.warn("⚠️ Token invalide ou expiré");
                
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Token invalide ou expiré")
                                .data(Map.of("valid", false))
                                .timestamp(LocalDateTime.now())
                                .build());
            }
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la vérification du token: {}", e.getMessage(), e);
            
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Token invalide")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * 📋 Liste tous les utilisateurs (endpoint protégé)
     * GET /auth/users/alls
     */
    @GetMapping("/users/alls")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        log.info("📋 Récupération de la liste des utilisateurs");
        
        try {
            // Vérification du token si présent
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (!jwtAuthService.verifyToken(token)) {
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.builder()
                                    .success(false)
                                    .message("Token invalide ou expiré")
                                    .data(null)
                                    .build());
                }
            }

            List<UtilisateurDto> users = utilisateurService.listeUtilisateur();
            
            log.info("✅ {} utilisateurs récupérés", users.size());
            
            return ResponseEntity
                    .ok()
                    .body(ApiResponse.builder()
                            .success(true)
                            .message("Liste des utilisateurs récupérée avec succès")
                            .data(users)
                            .timestamp(LocalDateTime.now())
                            .build());
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la récupération des utilisateurs: {}", e.getMessage(), e);
            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Erreur lors de la récupération des utilisateurs")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * 🔍 Récupère un utilisateur par son ID
     * GET /auth/users/{id}
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUserById(
            @PathVariable("id") int userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        log.info("🔍 Recherche de l'utilisateur avec ID: {}", userId);
        
        try {
            UtilisateurDto userDto = utilisateurService.getUser(userId);
            
            log.info("✅ Utilisateur trouvé: {}", userDto.getLogin());
            
            return ResponseEntity
                    .ok()
                    .body(ApiResponse.builder()
                            .success(true)
                            .message("Utilisateur récupéré avec succès")
                            .data(userDto)
                            .timestamp(LocalDateTime.now())
                            .build());
                
        } catch (UtilisateurException e) {
            log.warn("⚠️ Utilisateur non trouvé avec ID: {}", userId);
            
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la récupération de l'utilisateur: {}", e.getMessage(), e);
            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Erreur lors de la récupération de l'utilisateur")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * 🔎 Recherche des utilisateurs par mot-clé
     * GET /auth/users/search?keyword=xxx
     */
    @GetMapping("/users/search")
    public ResponseEntity<ApiResponse> searchUsers(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        log.info("🔎 Recherche d'utilisateurs avec le mot-clé: '{}'", keyword);
        
        try {
            List<UtilisateurDto> users = utilisateurService.seacrhUsers(keyword);
            
            log.info("✅ {} utilisateurs trouvés", users.size());
            
            return ResponseEntity
                    .ok()
                    .body(ApiResponse.builder()
                            .success(true)
                            .message(users.isEmpty() ? 
                                    "Aucun utilisateur trouvé" : 
                                    users.size() + " utilisateur(s) trouvé(s)")
                            .data(users)
                            .timestamp(LocalDateTime.now())
                            .build());
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la recherche: {}", e.getMessage(), e);
            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Erreur lors de la recherche d'utilisateurs")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    /**
     * 🏥 Health check endpoint
     * GET /auth/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse> healthCheck() {
        Map<String, Object> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("service", "Biometry Authentication Service");
        healthData.put("version", "2.0");
        
        return ResponseEntity
                .ok()
                .body(ApiResponse.builder()
                        .success(true)
                        .message("Service opérationnel")
                        .data(healthData)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /**
     * 📊 Obtient les informations détaillées du token actuel
     * GET /auth/users/token-info
     */
    @GetMapping("/users/token-info")
    public ResponseEntity<ApiResponse> getTokenInfo(
            @RequestHeader(value = "Authorization", required = true) String authHeader) {
        
        log.info("📊 Demande d'informations sur le token");
        
        try {
            if (!authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .badRequest()
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Format de token invalide")
                                .data(null)
                                .build());
            }

            String token = authHeader.substring(7);
            Map<String, Object> tokenInfo = jwtUtil.getTokenInfo(token);

            return ResponseEntity
                    .ok()
                    .body(ApiResponse.builder()
                            .success(true)
                            .message("Informations du token récupérées")
                            .data(tokenInfo)
                            .timestamp(LocalDateTime.now())
                            .build());
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la récupération des infos: {}", e.getMessage());
            
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Token invalide")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }
}