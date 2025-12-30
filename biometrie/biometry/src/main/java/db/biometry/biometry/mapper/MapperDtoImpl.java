/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.mapper;

import db.biometry.biometry.ZenBioProperties;
import db.biometry.biometry.dto.UserDTO;
import db.biometry.biometry.dto.UserSessionDTO;
import db.biometry.biometry.dto.UtilisateurDto;
import db.biometry.biometry.entites.Employe;

import db.biometry.biometry.entites.Utilisateur;
import db.biometry.biometry.utils.JwtExpiration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author USER01
 */
@Service
public class MapperDtoImpl {

    @Autowired
    ZenBioProperties properties;

    public UserSessionDTO mapUserSessionDTOByuserDTO(Employe employe) {

        // Création de l'objet UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(employe.getUtilisateurId().getLogin());
        userDTO.setPrestataire(employe.getPrestataireId().getNom());
        userDTO.setNomcomplet((employe.getUtilisateurId().getPrenom()==null || employe.getUtilisateurId().getPrenom()=="") ? employe.getUtilisateurId().getNom(): employe.getUtilisateurId().getPrenom()+" "+employe.getUtilisateurId().getNom());
        userDTO.setProfil(employe.getProfilId().getId());
        userDTO.setProfil_name(employe.getProfilId().getCode());

        // Création de l'objet UserSessionDTO
        UserSessionDTO userSessionDTO = new UserSessionDTO();
        userSessionDTO.setUserDTO(userDTO);
        userSessionDTO.setExpiresAt(JwtExpiration.expiresAt(properties.getJwtExpirationMs()));
        System.out.println("expiration " + userSessionDTO.getExpiresAt());
        // Génération du token JWT
        userSessionDTO.setToken(
                JwtExpiration.generateJwtToken(userDTO.getUsername(), userSessionDTO.getExpiresAt())
        );

        return userSessionDTO;
    }

    public UtilisateurDto formUtilisateur(Utilisateur user) {
        UtilisateurDto userDto = new UtilisateurDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

}
