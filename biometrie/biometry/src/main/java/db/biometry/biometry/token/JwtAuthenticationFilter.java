/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.token;

/**
 *
 * @author USER01
 */

import db.biometry.biometry.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Filtre JWT pour valider automatiquement les tokens
 * S'exécute une fois par requête HTTP
 * MISE À JOUR pour Spring Boot 3.x et Java 17/21
 * 
 * @author JIATOU FRANCK
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // Récupération du token depuis le header Authorization
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // Pas de token, on continue sans authentification
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            
            // Validation du token
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);
                Integer userId = jwtUtil.getUserIdFromToken(token);

                // Création de l'authentification Spring Security
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(authority)
                        );
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Ajout de l'userId dans les détails (optionnel)
                request.setAttribute("userId", userId);
                request.setAttribute("userRole", role);

                // Définir l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("✅ Authentification réussie pour: {} (Role: {})", username, role);
            } else {
                log.warn("⚠️ Token invalide ou expiré");
            }

        } catch (Exception e) {
            log.error("❌ Erreur dans le filtre JWT: {}", e.getMessage());
            // En cas d'erreur, on continue sans authentification
        }

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Ne pas filtrer certaines URLs (endpoints publics)
        String path = request.getRequestURI();
        return path.startsWith("/auth/users/login") ||
               path.startsWith("/auth/users/refresh") ||
               path.startsWith("/auth/health") ||
               path.startsWith("/public/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs/");
    }
    
}
