/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;

import db.biometry.biometry.dtos.UserDTO;
import db.biometry.biometry.dtos.UserLogin;
import db.biometry.biometry.dtos.UserSessionDTO;
import db.biometry.biometry.exceptions.UtilisateurException;
import db.biometry.biometry.mappers.BiometrieMapperImpl;
import db.biometry.biometry.services.UtilisateurService;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author JIATOU FRANCK
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/service-biometrie-partenaire")
//@CrossOrigin("*")
public class UtlisateurRestController {

    private UtilisateurService utilisateurService;
      private BiometrieMapperImpl mappers;

    @GetMapping("users/all-users")
    public List<UserDTO> listeusers() {
        return utilisateurService.listeUtilisateur();
    }

    @GetMapping("users/{id}")
    public UserDTO getUser(@PathVariable(name = "id") int userId) {
        UserDTO userDto = new UserDTO();
        try {

            userDto = utilisateurService.getUser(userId);
        } catch (UtilisateurException u) {
            userDto = new UserDTO();
            userDto.setEcheck_connection(true);
            userDto.setMessageEcheck(u.getMessage());
        }
        return userDto;
    }
    
  @PostMapping("users/login")  
   public ResponseEntity<?> connect(@RequestBody UserLogin userLogin){
      UserDTO user=utilisateurService.findUserByLogin(userLogin);
       UserSessionDTO userSessionDTO = mappers.mapUserSessionDTOByuserDTO(user);
        
        return ResponseEntity.ok(userSessionDTO);
      
  }
  
  @GetMapping("users/search")
  public List<UserDTO> searchUsers(@RequestParam(name = "keyword", defaultValue = "")String keyword){
      return utilisateurService.seacrhUsers(keyword);
  }
  

   
}
