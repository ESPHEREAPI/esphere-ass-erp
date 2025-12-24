package db.biometry.biometry.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Builder;

/**
 * DTO de réponse API standardisée
 * Compatible avec le frontend Angular
 * 
 * @author JIATOU FRANCK
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclut les champs null du JSON
public class ApiResponse<T> {
    
    /**
     * Indique si l'opération a réussi
     */
    private boolean success;
    
    /**
     * Message descriptif de la réponse
     */
    private String message;
    
    /**
     * Données de la réponse (peut être null)
     */
    private T data;
    
    /**
     * Timestamp de la réponse
     */
    private LocalDateTime timestamp;
    
    /**
     * Code d'erreur optionnel
     */
    private String errorCode;
    
    // Constructeur simplifié
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructeur avec code d'erreur
    public ApiResponse(boolean success, String message, T data, String errorCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
    
    // Méthodes statiques pour créer des réponses facilement
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Opération réussie", data);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
    
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, message, null, errorCode);
    }
}