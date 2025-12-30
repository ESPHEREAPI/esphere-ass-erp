/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.token;

/**
 *
 * @author USER01
 */


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import db.biometry.biometry.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Point d'entrée pour gérer les erreurs d'authentification
 * Retourne une réponse JSON standardisée pour les erreurs 401
 * MISE À JOUR pour Spring Boot 3.x et Java 17/21
 * 
 * @author JIATOU FRANCK
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    
    public JwtAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        // Support pour LocalDateTime
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        log.error("🚫 Erreur d'authentification: {} - URI: {}", 
                authException.getMessage(), 
                request.getRequestURI());

        // Construction de la réponse d'erreur
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(false)
                .message("Authentification requise. Token manquant ou invalide.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        // Configuration de la réponse HTTP
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // Écriture de la réponse JSON
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}