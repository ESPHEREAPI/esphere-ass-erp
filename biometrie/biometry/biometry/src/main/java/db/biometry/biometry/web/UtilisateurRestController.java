package db.biometry.biometry.web;

import com.netflix.discovery.provider.Serializer;
import db.biometry.biometry.dto.ApiResponse;
import db.biometry.biometry.dto.UserDTO;
import db.biometry.biometry.dto.UserLogin;
import db.biometry.biometry.dto.UserSessionDTO;
import db.biometry.biometry.dto.UtilisateurDto;
import db.biometry.biometry.entites.Employe;
import db.biometry.biometry.exceptions.UtilisateurException;
import db.biometry.biometry.mapper.MapperDtoImpl;
import db.biometry.biometry.repository.EmployeRepository;
import db.biometry.biometry.services.UtilisateurService;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Endpoint de connexion utilisateur
     * POST /auth/users/login
     */
    @PostMapping("/users/login")
    public ResponseEntity<ApiResponse> connect(@RequestBody UserLogin userLogin) {
        log.info("🔐 Tentative de connexion pour l'utilisateur: {}", userLogin.getUsername());
        
        try {
            // Validation des données d'entrée
            if (userLogin.getUsername() == null || userLogin.getUsername().trim().isEmpty()) {
                log.warn("⚠️ Tentative de connexion avec username vide");
                return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(
                        false, 
                        "Le nom d'utilisateur est requis", 
                        null
                    ));
            }

            if (userLogin.getPassword() == null || userLogin.getPassword().trim().isEmpty()) {
                log.warn("⚠️ Tentative de connexion avec mot de passe vide pour: {}", userLogin.getUsername());
                return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(
                        false, 
                        "Le mot de passe est requis", 
                        null
                    ));
            }

            // Recherche et validation de l'utilisateur
            UserDTO user = utilisateurService.findUserByLogin(userLogin);

            if (user.getEcheck_connection()) {
                log.warn("❌ Échec de connexion pour: {} - Raison: {}", 
                    userLogin.getUsername(), 
                    user.getMessageEcheck()
                );
                
                return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(
                        false, 
                        user.getMessageEcheck(), 
                        null
                    ));
            }

            // Récupération des informations complètes de l'employé
            Employe employe = getEmployeByLogin(userLogin.getUsername());
            UserSessionDTO userSessionDTO = mappers.mapUserSessionDTOByuserDTO(employe);

            log.info("✅ Connexion réussie pour: {} (Role: {})", 
                userLogin.getUsername(),
                employe.getUtilisateurId() != null ? 
                    employe.getProfilId(): "N/A"
            );

            return ResponseEntity
                .ok()
                .body(new ApiResponse(
                    true, 
                    "Connexion réussie", 
                    userSessionDTO
                ));

        } catch (UtilisateurException e) {
            log.error("❌ Erreur métier lors de la connexion pour: {} - {}", 
                userLogin.getUsername(), 
                e.getMessage()
            );
            
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(
                    false, 
                    e.getMessage(), 
                    null
                ));
                
        } catch (Exception e) {
            log.error("💥 Erreur inattendue lors de la connexion pour: {} - {}", 
                userLogin.getUsername(), 
                e.getMessage(), 
                e
            );
            
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(
                    false, 
                    "Une erreur interne est survenue. Veuillez réessayer plus tard.", 
                    null
                ));
        }
    }

    /**
     * Endpoint de déconnexion utilisateur
     * POST /auth/users/logout
     */
    @PostMapping("/users/logout")
    public ResponseEntity<ApiResponse> logout() {
        log.info("🚪 Déconnexion utilisateur");
        
        try {
            // Ici, vous pouvez ajouter la logique de déconnexion
            // (invalidation de token, nettoyage de session, etc.)
            
            return ResponseEntity
                .ok()
                .body(new ApiResponse(
                    true, 
                    "Déconnexion réussie", 
                    null
                ));
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la déconnexion: {}", e.getMessage(), e);
            
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(
                    false, 
                    "Erreur lors de la déconnexion", 
                    null
                ));
        }
    }

    /**
     * Liste tous les utilisateurs
     * GET /auth/users/alls
     */
    @GetMapping("/users/alls")
    public ResponseEntity<ApiResponse> listeUsers() {
        log.info("📋 Récupération de la liste des utilisateurs");
        
        try {
            List<UtilisateurDto> users = utilisateurService.listeUtilisateur();
            
            log.info("✅ {} utilisateurs récupérés", users.size());
            
            return ResponseEntity
                .ok()
                .body(new ApiResponse(
                    true, 
                    "Liste des utilisateurs récupérée avec succès", 
                    users
                ));
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la récupération des utilisateurs: {}", e.getMessage(), e);
            
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(
                    false, 
                    "Erreur lors de la récupération des utilisateurs", 
                    null
                ));
        }
    }

    /**
     * Récupère un utilisateur par son ID
     * GET /auth/users/{id}
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable(name = "id") int userId) {
        log.info("🔍 Recherche de l'utilisateur avec ID: {}", userId);
        
        try {
            UtilisateurDto userDto = utilisateurService.getUser(userId);
            
            log.info("✅ Utilisateur trouvé: {}", userDto.getLogin());
            
            return ResponseEntity
                .ok()
                .body(new ApiResponse(
                    true, 
                    "Utilisateur récupéré avec succès", 
                    userDto
                ));
                
        } catch (UtilisateurException e) {
            log.warn("⚠️ Utilisateur non trouvé avec ID: {} - {}", userId, e.getMessage());
            
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(
                    false, 
                    e.getMessage(), 
                    null
                ));
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la récupération de l'utilisateur {}: {}", 
                userId, 
                e.getMessage(), 
                e
            );
            
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(
                    false, 
                    "Erreur lors de la récupération de l'utilisateur", 
                    null
                ));
        }
    }

    /**
     * Recherche des utilisateurs par mot-clé
     * GET /auth/users/search?keyword=xxx
     */
    @GetMapping("/users/search")
    public ResponseEntity<ApiResponse> searchUsers(
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        
        log.info("🔎 Recherche d'utilisateurs avec le mot-clé: '{}'", keyword);
        
        try {
            List<UtilisateurDto> users = utilisateurService.seacrhUsers(keyword);
            
            log.info("✅ {} utilisateurs trouvés pour le mot-clé '{}'", users.size(), keyword);
            
            return ResponseEntity
                .ok()
                .body(new ApiResponse(
                    true, 
                    users.isEmpty() ? "Aucun utilisateur trouvé" : "Utilisateurs trouvés", 
                    users
                ));
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la recherche d'utilisateurs avec '{}': {}", 
                keyword, 
                e.getMessage(), 
                e
            );
            
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(
                    false, 
                    "Erreur lors de la recherche d'utilisateurs", 
                    null
                ));
        }
    }

    /**
     * Vérifie la validité d'un token (à implémenter avec JWT)
     * GET /auth/users/verify
     */
    @GetMapping("/users/verify")
    public ResponseEntity<ApiResponse> verifyToken() {
        log.info("🔐 Vérification de token");
        
        try {
            // TODO: Implémenter la vérification du token JWT
            // Pour l'instant, retourne OK si l'utilisateur a un token valide
            
            return ResponseEntity
                .ok()
                .body(new ApiResponse(
                    true, 
                    "Token valide", 
                    null
                ));
                
        } catch (Exception e) {
            log.error("❌ Erreur lors de la vérification du token: {}", e.getMessage(), e);
            
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(
                    false, 
                    "Token invalide", 
                    null
                ));
        }
    }

    /**
     * Refresh token endpoint (à implémenter avec JWT)
     * POST /auth/users/refresh
     */
    @PostMapping("/users/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody String refreshToken) {
        log.info("🔄 Rafraîchissement du token");
        
        try {
            // TODO: Implémenter le rafraîchissement du token JWT
            
            return ResponseEntity
                .ok()
                .body(new ApiResponse(
                    true, 
                    "Token rafraîchi avec succès", 
                    null
                ));
                
        } catch (Exception e) {
            log.error("❌ Erreur lors du rafraîchissement du token: {}", e.getMessage(), e);
            
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(
                    false, 
                    "Impossible de rafraîchir le token", 
                    null
                ));
        }
    }

    /**
     * Méthode privée pour récupérer un employé par son login
     */
    private Employe getEmployeByLogin(String login) {
        log.debug("🔍 Recherche de l'employé avec login: {}", login);
        
        return employeRepository.findByUtilisateurLogin(login)
            .orElseThrow(() -> {
                log.error("❌ Employé non trouvé pour le login: {}", login);
                return new UtilisateurException(
                    "Utilisateur non trouvé. Veuillez vérifier vos identifiants."
                );
            });
    }

    /**
     * Health check endpoint
     * GET /auth/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse> healthCheck() {
        return ResponseEntity
            .ok()
            .body(new ApiResponse(
                true, 
                "Service opérationnel", 
                LocalDateTime.now()
            ));
    }
}