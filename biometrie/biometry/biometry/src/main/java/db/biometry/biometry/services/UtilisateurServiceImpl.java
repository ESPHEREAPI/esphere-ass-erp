/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dto.UserDTO;
import db.biometry.biometry.dto.UserLogin;
import db.biometry.biometry.dto.UtilisateurDto;
import db.biometry.biometry.entites.Utilisateur;
import db.biometry.biometry.exceptions.UtilisateurException;
import db.biometry.biometry.mapper.MapperDtoImpl;
import db.biometry.biometry.repository.UtilisateurRepository;
import db.biometry.biometry.utils.Crypto;

import java.util.List;
import java.util.Objects;

import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author JIATOU FRANCK
 */
@Service

@Transactional
@AllArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private MapperDtoImpl mappers;

    @Override
    public UserDTO findUserByLogin(UserLogin userLog) {
        UserDTO userdto = new UserDTO();
        Utilisateur user = utilisateurRepository.findByLogin(userLog.getLogin());
        if (user == null) {
            userdto.setEcheck_connection(true);
            userdto.setMessageEcheck("USER NOT EXISTS... PLEASE TRY AGAINST");
            return userdto;
        }
        
        if (Objects.equals(Crypto.loginBiometrie(userLog.getMotPasse()), user.getMotPasse()) == Boolean.FALSE) {
            userdto.setEcheck_connection(true);
            userdto.setMessageEcheck("LOGIN OR PASSWORD NOT CORRECT");
            return userdto;
        }
        if (Objects.equals(userLog.getMotPasse(), user.getMotPasse()) == Boolean.TRUE) {
            if (!"1".equals(user.getStatut())) {
                userdto.setEcheck_connection(true);
                userdto.setMessageEcheck("USER IS NOT ACTIVED... CHECK YOUR ADMINISTRATOR");
                return userdto;
            }

        }

        //userdto = mappers.formUtilisateur(user);
        return userdto;

    }

    @Override
    public List<UtilisateurDto> listeUtilisateur() {
        List<Utilisateur> listeUsers = utilisateurRepository.findAll();
        List<UtilisateurDto> listeUserDtos = listeUsers.stream().map(user -> mappers.formUtilisateur(user)).collect(Collectors.toList());

        return listeUserDtos;
    }

    @Override
    public UtilisateurDto getUser(int userId) throws UtilisateurException {
        Utilisateur user = utilisateurRepository.findById(userId).orElseThrow(() -> new UtilisateurException("USER NOT EXISTS... PLEASE TRY AGAINST"));
        UtilisateurDto userDto = mappers.formUtilisateur(user);
        return userDto;
    }

    @Override
    public List<UtilisateurDto> seacrhUsers(String keyword) {
        List<Utilisateur> listeUsers=utilisateurRepository.searchUser(keyword);
        List<UtilisateurDto>  listeUserDto=listeUsers.stream().map(u-> mappers.formUtilisateur(u)).collect(Collectors.toList());
       return listeUserDto;
    }

}
