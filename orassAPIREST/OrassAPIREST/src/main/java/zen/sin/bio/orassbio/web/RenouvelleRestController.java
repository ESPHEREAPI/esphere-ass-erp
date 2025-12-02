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
import zen.sin.bio.orassbio.dtos.RenouvellementDto;
import zen.sin.bio.orassbio.entites.ZenRenouvellement;
import zen.sin.bio.orassbio.service.ConnectionOracleDataBaseService;


/**
 *
 * @author USER01
 */
@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class RenouvelleRestController implements Serializable {

    private ConnectionOracleDataBaseService connectionOracleDataBaseService;

    @GetMapping("orass/Zen-renouvellement")
    List<ZenRenouvellement> listeZenrenouvement() {
        return connectionOracleDataBaseService.listeRenouvellement();
    }

    @GetMapping("orass/Zen-renouvellement/{police}")
    public RenouvellementDto liste(@PathVariable(name = "police") String police) {
        return connectionOracleDataBaseService.listeRenouvellementByPolice(police);
    }
    
    @GetMapping("orass/Zen-renouvellement-all-police/{police}")
    public List<ZenRenouvellement> allPolice(@PathVariable(name = "police") String police) {
        return connectionOracleDataBaseService.listeRenouvellementAllPolice(police);
    }
    
    

}
