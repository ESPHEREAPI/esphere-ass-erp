///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package db.biometry.biometry.exceptions;
//
///**
// *
// * @author USER01
// */
//import db.biometry.biometry.dto.ApiResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//   
//    // Gestion spécifique pour les erreurs "Utilisateur"
//    @ExceptionHandler(UtilisateurException.class)
//    public ResponseEntity<ApiResponse> handleUtilisateurException(UtilisateurException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new ApiResponse(false, ex.getMessage(), null));
//    }
//
//    // Gestion pour RuntimeException génériques
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ApiResponse(false, "Unexpected error: " + ex.getMessage(), null));
//    }
//
//    // Gestion fallback pour Exception
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse> handleException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ApiResponse(false, "An error occurred", null));
//    }
//}



package db.biometry.biometry.exceptions;

import db.biometry.biometry.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour l'application
 * Capture toutes les exceptions et retourne des réponses standardisées
 * 
 * @author JIATOU FRANCK
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions métier personnalisées (UtilisateurException)
     */
    @ExceptionHandler(UtilisateurException.class)
    public ResponseEntity<ApiResponse> handleUtilisateurException(
            UtilisateurException ex, 
            WebRequest request) {
        
        log.error("❌ UtilisateurException: {} - URI: {}", 
            ex.getMessage(), 
            request.getDescription(false)
        );
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse(
                false,
                ex.getMessage(),
                null
            ));
    }

    /**
     * Gère les erreurs de validation (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        log.error("❌ Erreur de validation: {}", errors);
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse(
                false,
                "Erreur de validation des données",
                errors
            ));
    }

    /**
     * Gère les erreurs de type de paramètre
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        
        String message = String.format(
            "Le paramètre '%s' doit être de type %s", 
            ex.getName(), 
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "inconnu"
        );
        
        log.error("❌ Erreur de type de paramètre: {}", message);
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse(
                false,
                message,
                null
            ));
    }

    /**
     * Gère toutes les autres exceptions non capturées
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(
            Exception ex, 
            WebRequest request) {
        
        log.error("💥 Erreur inattendue: {} - URI: {}", 
            ex.getMessage(), 
            request.getDescription(false),
            ex
        );
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(
                false,
                "Une erreur interne est survenue. Veuillez réessayer plus tard.",
                null
            ));
    }

    /**
     * Gère les IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgument(
            IllegalArgumentException ex) {
        
        log.error("❌ Argument invalide: {}", ex.getMessage());
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse(
                false,
                ex.getMessage(),
                null
            ));
    }

    /**
     * Gère les NullPointerException
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse> handleNullPointer(
            NullPointerException ex,
            WebRequest request) {
        
        log.error("❌ NullPointerException: {} - URI: {}", 
            ex.getMessage(), 
            request.getDescription(false),
            ex
        );
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(
                false,
                "Une erreur technique est survenue",
                null
            ));
    }
}