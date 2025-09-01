/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
public class UserSessionDTO {
      private UserDTO userDTO;
    private String token;
    private List<String> permissions;
    private Date expiresAt;
}
