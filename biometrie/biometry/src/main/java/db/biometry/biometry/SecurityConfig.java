package db.biometry.biometry;

import db.biometry.biometry.token.JwtAuthenticationEntryPoint;
import db.biometry.biometry.token.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration de sécurité Spring Security 6.x avec JWT Compatible avec Spring
 * Boot 3.x et Java 17/21
 *
 * @author JIATOU FRANCK
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Configuration de la chaîne de filtres de sécurité MISE À JOUR pour Spring
     * Security 6.x
     */
    @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            // Désactivation de CSRF (car on utilise JWT)
            .csrf(csrf -> csrf.disable())
            // Configuration CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Gestion des exceptions d'authentification
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            // Gestion de session - STATELESS (pas de session)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Configuration des autorisations
            .authorizeHttpRequests(authorize -> authorize
                // Endpoints publics (sans authentification)
                .requestMatchers(
                        "/auth/users/login",                    // ✅ Sans préfixe
                        "/api/service-biometrie/auth/users/login",  // ✅ Avec préfixe complet
                        "/auth/users/refresh",
                        "/api/service-biometrie/auth/users/refresh",
                        "/auth/health",
                        "/public/**",
                        "/actuator/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/api-docs/**"
                ).permitAll()
                // Endpoints protégés par rôle
                .requestMatchers(HttpMethod.GET, "/auth/users/alls").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.POST, "/auth/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/auth/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/auth/users/**").hasRole("ADMIN")
                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
            );

    // Ajout du filtre JWT avant le filtre d'authentification par défaut
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
    

    /**
     * Configuration CORS pour autoriser les requêtes du frontend Compatible
     * avec Spring Security 6.x
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origines autorisées (adapter selon votre environnement)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200", // Angular Dev
                "https://localhost:4200", // Angular Dev HTTPS
                "http://localhost:8080", // Gateway
                "https://localhost:8080", // Gateway HTTPS
                "http://localhost:8081", // Backend direct
                   "https://localhost:8180", // Backend direct
                  "http://localhost:8180", // Backend direct
                "https://localhost:8081" // Backend direct HTTPS
        // En production, retirez "*" et ajoutez seulement vos domaines
        // "https://votre-domaine.com",
        // "https://app.votre-domaine.com"
        ));

        // Alternative : autoriser toutes les origines (UNIQUEMENT pour dev)
        // configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // Méthodes HTTP autorisées
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Headers autorisés
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "X-Gateway-Token"
        ));

        // Headers exposés
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Total-Count"
        ));

        // Autoriser les credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // Durée de cache de la config CORS (en secondes)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Encoder de mots de passe (BCrypt) Note: Vous utilisez déjà un système
     * custom (Crypto.loginBiometrie) Vous pouvez garder les deux ou migrer vers
     * BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
