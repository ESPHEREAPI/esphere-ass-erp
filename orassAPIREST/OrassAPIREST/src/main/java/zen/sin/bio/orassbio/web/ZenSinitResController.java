/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.web;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import zen.sin.bio.orassbio.entites.ZenSinistreLn;
import zen.sin.bio.orassbio.service.ConnectionOracleDataBaseService;

/**
 *
 * @author USER01
 */
@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class ZenSinitResController implements Serializable{
     private ConnectionOracleDataBaseService connectionOracleDataBaseService;
     
      @GetMapping("orass/Zen-sinistre/{number_sinistre}")
    public List<ZenSinistreLn> listes(@PathVariable(name = "number_sinistre") String number_sinistre) {
        return connectionOracleDataBaseService.listeSinistresByNumber(number_sinistre);
    }
}
