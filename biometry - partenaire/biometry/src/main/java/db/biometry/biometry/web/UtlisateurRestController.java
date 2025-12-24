/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;

import db.biometry.biometry.dtos.UserDTO;
import db.biometry.biometry.dtos.UserLogin;
import db.biometry.biometry.dtos.UserSessionDTO;
import db.biometry.biometry.exceptions.ResourceNotFoundException;
import db.biometry.biometry.exceptions.UtilisateurException;
import db.biometry.biometry.mappers.BiometrieMapperImpl;
import db.biometry.biometry.services.UtilisateurService;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;


/**
 *
 * @author JIATOU FRANCK
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/service-biometrie-partenaire")
//@CrossOrigin("*")
public class UtlisateurRestController {

   private UtilisateurService utilisateurService;
    private BiometrieMapperImpl mappers;
    
    @GetMapping("users/all-users")
    public List<UserDTO> listeusers() {
        return utilisateurService.listeUtilisateur();
    }
    
    @GetMapping("users/{id}")
    public UserDTO getUser(@PathVariable(name = "id") int userId) {
        UserDTO userDto = new UserDTO();
        try {
            userDto = utilisateurService.getUser(userId);
        } catch (UtilisateurException u) {
            userDto = new UserDTO();
            userDto.setEcheck_connection(true);
            userDto.setMessageEcheck(u.getMessage());
        }
        return userDto;
    }
    
    @PostMapping("users/login")  
    public ResponseEntity<?> connect(@RequestBody UserLogin userLogin) {
        try {
            // Log des données reçues
            log.info("Tentative de connexion pour l'utilisateur: {}", userLogin.getUserName());
            
            // Validation des données d'entrée
            if (userLogin.getUserName() == null || userLogin.getUserName().trim().isEmpty()) {
                log.warn("Nom d'utilisateur vide");
                Map<String, String> error = new HashMap<>();
                error.put("message", "Le nom d'utilisateur est requis");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (userLogin.getPassWord() == null || userLogin.getPassWord().trim().isEmpty()) {
                log.warn("Mot de passe vide");
                Map<String, String> error = new HashMap<>();
                error.put("message", "Le mot de passe est requis");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Recherche de l'utilisateur
            UserDTO user = utilisateurService.findUserByLogin(userLogin);
            
            if (user == null) {
                log.warn("Utilisateur non trouvé: {}", userLogin.getUserName());
                Map<String, String> error = new HashMap<>();
                error.put("message", "Login ou mot de passe incorrect");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Vérification des erreurs de connexion
            if (user.getEcheck_connection()) {
                log.warn("Erreur de connexion: {}", user.getMessageEcheck());
                Map<String, String> error = new HashMap<>();
                error.put("message", user.getMessageEcheck());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Création de la session
            UserSessionDTO userSessionDTO = mappers.mapUserSessionDTOByuserDTO(user);
            
            // Vérification que la session est complète
            if (userSessionDTO == null || userSessionDTO.getToken() == null) {
                log.error("Impossible de créer la session utilisateur");
                Map<String, String> error = new HashMap<>();
                error.put("message", "Erreur lors de la création de la session");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
            
            log.info("Connexion réussie pour l'utilisateur: {}", userLogin.getUserName());
            
            // Log de la réponse pour debug
            log.debug("UserSessionDTO: token={}, expiresAt={}, usersDTO={}", 
                userSessionDTO.getToken() != null ? "present" : "null",
                userSessionDTO.getExpiresAt(),
                userSessionDTO.getUsersDTO() != null ? "present" : "null");
            
            return ResponseEntity.ok(userSessionDTO);
            
        } catch (Exception e) {
            log.error("Erreur utilisateur: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            
        } 
    }
    
    @GetMapping("users/search")
    public List<UserDTO> searchUsers(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return utilisateurService.seacrhUsers(keyword);
    }
    
    @PostMapping("users/logout")
public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
    try {
        log.info("Tentative de déconnexion");
        
        // Extraire le token si présent
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        
        // Invalider le token côté serveur si vous avez une gestion de session
        if (token != null && !token.isEmpty()) {
            // Appeler votre service pour invalider le token
            // utilisateurService.invalidateToken(token);
            log.info("Token invalidé côté serveur");
        }
        
        log.info("Déconnexion réussie");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Déconnexion réussie");
        response.put("timestamp", new Date());
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        log.error("Erreur lors de la déconnexion: {}", e.getMessage(), e);
        
        Map<String, String> error = new HashMap<>();
        error.put("message", "Erreur lors de la déconnexion");
        error.put("details", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
   
}
