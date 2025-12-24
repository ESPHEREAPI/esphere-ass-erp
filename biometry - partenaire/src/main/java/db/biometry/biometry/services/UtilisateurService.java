/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.services;


import db.biometry.biometry.dto.UserDTO;
import db.biometry.biometry.dto.UserLogin;
import db.biometry.biometry.dto.UtilisateurDto;
import db.biometry.biometry.exceptions.UtilisateurException;
import java.util.List;

/**
 *
 * @author JIATOU FRANCK
 */
public interface UtilisateurService {
    UserDTO findUserByLogin(UserLogin userLog);
    List<UtilisateurDto>listeUtilisateur();
    UtilisateurDto getUser(int userId)throws UtilisateurException;
    List<UtilisateurDto>seacrhUsers(String keyword);
}
