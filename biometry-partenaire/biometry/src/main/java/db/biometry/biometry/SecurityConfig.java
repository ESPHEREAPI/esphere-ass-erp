///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package db.biometry.biometry;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Configuration de la sécurité Spring Security avec JWT
// * Gère l'authentification et l'autorisation
// * 
// * @author JIATOU FRANCK
// * @version 1.0
// */
///**
// *
// * @author USER01
// */
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//    // Note: JwtAuthenticationFilter et CustomUserDetailsService à implémenter
//    // private final JwtAuthenticationFilter jwtAuthFilter;
//    // private final UserDetailsService userDetailsService;
//    
//    /**
//     * Configuration de la chaîne de filtres de sécurité
//     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests(auth -> auth
//                        // Endpoints publics
//                        .requestMatchers("/api/v1/auth/**").permitAll()
//                .requestMatchers("/v3/api-docs/**",
//            "/swagger-ui.html",
//            "/swagger-ui/**").permitAll()
//                        .requestMatchers("/actuator/**").permitAll()
//                        
//                        // Endpoints du tableau de bord - Accès SOUSCRIPTEUR
//                        .requestMatchers("/api/v1/dashboard/**").hasAnyRole("SOUSCRIPTEUR", "ADMIN")
//                        
//                        // Endpoints de gestion des adhérents - Accès SOUSCRIPTEUR
//                        .requestMatchers(HttpMethod.GET, "/api/v1/adherents/**").hasAnyRole("SOUSCRIPTEUR", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/api/v1/adherents/**").hasAnyRole("SOUSCRIPTEUR", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/adherents/**").hasAnyRole("SOUSCRIPTEUR", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/adherents/**").hasRole("ADMIN")
//                        
//                        // Endpoints de reporting - Accès SOUSCRIPTEUR
//                        .requestMatchers("/api/v1/reporting/**").hasAnyRole("SOUSCRIPTEUR", "ADMIN")
//                        
//                        // Tous les autres endpoints nécessitent une authentification
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//                // .authenticationProvider(authenticationProvider())
//                // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//        
//        return http.build();
//    }
//    
//    /**
//     * Configuration CORS
//     */
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setExposedHeaders(List.of("Authorization"));
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
//        
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        
//        return source;
//    }
//    
//    /**
//     * Encodeur de mots de passe BCrypt
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    
//    /**
//     * Gestionnaire d'authentification
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//    
//    /**
//     * Fournisseur d'authentification
//     * Note: Nécessite UserDetailsService à implémenter
//     */
//    /*
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//    */
//}
