/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;

import db.biometry.biometry.dto.UserDTO;
import db.biometry.biometry.dto.UserLogin;
import db.biometry.biometry.dto.UserSessionDTO;
import db.biometry.biometry.dto.UtilisateurDto;
import db.biometry.biometry.entites.Employe;
import db.biometry.biometry.exceptions.UtilisateurException;
import db.biometry.biometry.mapper.MapperDtoImpl;
import db.biometry.biometry.repository.EmployeRepository;
import db.biometry.biometry.services.UtilisateurService;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("auth/users")
public class UtlisateurRestController {

    private UtilisateurService utilisateurService;
     private EmployeRepository employeRepository;
    private MapperDtoImpl mappers;

    @GetMapping("/alls")
    public List<UtilisateurDto> listeusers() {
        return utilisateurService.listeUtilisateur();
    }

    @GetMapping("/{id}")
    public UtilisateurDto getUser(@PathVariable(name = "id") int userId) {
        UtilisateurDto userDto = new UtilisateurDto();
        try {

            userDto = utilisateurService.getUser(userId);
        } catch (UtilisateurException u) {
            userDto = new UtilisateurDto();
            userDto.setEcheck_connection(true);
            userDto.setMessageEcheck(u.getMessage());
        }
        return userDto;
    }
    
  @PostMapping("/login")  
  public UserSessionDTO connect(@RequestBody UserLogin userLogin){
      UserDTO user=utilisateurService.findUserByLogin(userLogin);
      Employe employe=getEmployeByLogin(userLogin.getLogin());
      UserSessionDTO userSessionDTO=mappers.mapUserSessionDTOByuserDTO(employe);
      return userSessionDTO;
  }
  
  @GetMapping("/search")
  public List<UtilisateurDto> searchUsers(@RequestParam(name = "keyword", defaultValue = "")String keyword){
      return utilisateurService.seacrhUsers(keyword);
  }
  

   
    
    public Employe getEmployeByLogin(String login) {
        return employeRepository.findByUtilisateurLogin(login)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé pour le login: " + login));
    }
  

}
