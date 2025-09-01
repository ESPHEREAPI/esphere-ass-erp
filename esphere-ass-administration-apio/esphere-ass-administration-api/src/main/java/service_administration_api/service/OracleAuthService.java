/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import service_administration_api.DTO.AuthResponseDTO;

/**
 *
 * @author USER01
 */
@Service
public class OracleAuthService {
   @Autowired
    private JdbcTemplate jdbcTemplate;

    private String getAccountStatus(String username) {
        String sql = "SELECT ACCOUNT_STATUS FROM DBA_USERS WHERE USERNAME = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username.toUpperCase()}, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean tryLogin(String username, String password) {
        String jdbcUrl = "jdbc:oracle:thin:@//localhost:1521/ORCLASS"; // adapter si nécessaire
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            return true; // connexion OK puis fermée automatiquement
        } catch (SQLException e) {
            return false; // mot de passe incorrect ou autre erreur
        }
    }

    public AuthResponseDTO authenticate(String username, String password) {
        String status = getAccountStatus(username);
        if (status == null) {
            return new AuthResponseDTO("ERROR", "Utilisateur inexistant");
        }

        switch (status) {
            case "OPEN":
                if (tryLogin(username, password)) {
                    return new AuthResponseDTO("SUCCESS", "Connexion réussie");
                } else {
                    return new AuthResponseDTO("ERROR", "Mot de passe incorrect");
                }
            case "LOCKED":
            case "LOCKED(TIMED)":
                return new AuthResponseDTO("ERROR", "Compte verrouillé");
            case "EXPIRED":
            case "EXPIRED & LOCKED":
                return new AuthResponseDTO("ERROR", "Mot de passe expiré");
            case "EXPIRED(GRACE)":
                return new AuthResponseDTO("ERROR", "Mot de passe expiré (grâce)");
            default:
                return new AuthResponseDTO("ERROR", "État inconnu : " + status);
        }
    }

}
