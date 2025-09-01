/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service_administration_api.DTO.AuthResponseDTO;
import service_administration_api.DTO.LoginResponseDTO;
import service_administration_api.entite.PosteTravail;
import service_administration_api.mapper.MapperDtoImpl;
import service_administration_api.repository.PosteTravailRepository;
import service_administration_api.service.OracleAuthService;

/**
 *
 * @author USER01
 */
@RestController
@RequestMapping("/esphere-ass-microservice-admin/auth")

public class AuthController {

    @Autowired
    private OracleAuthService authService;
    @Autowired
    MapperDtoImpl mapper;
  @Autowired
  private  PosteTravailRepository posteTravailRepository;
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginResponseDTO request) {
        AuthResponseDTO result = authService.authenticate(request.getUsername(), request.getPassword());
        result.setUserSession(mapper.mapUserSessionDTOByuserDTO(request.getUsername()));
        
// Code HTTP : 200 = succès, 401 = échec
        int httpStatus = result.getStatus().equals("SUCCESS") ? 200 : 401;
        return ResponseEntity.status(httpStatus).body(result);
    }
    
   @GetMapping("/all-poste")
public ResponseEntity<List<PosteTravail>> allPoste() {
    List<PosteTravail> postes = posteTravailRepository.findAll();
    if (postes.isEmpty()) {
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    return ResponseEntity.ok(postes); // 200 OK
}
}
