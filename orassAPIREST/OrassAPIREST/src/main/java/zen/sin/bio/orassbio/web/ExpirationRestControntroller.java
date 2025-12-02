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
import org.springframework.web.bind.annotation.RestController;
import zen.sin.bio.orassbio.entites.Expirations;
import zen.sin.bio.orassbio.entites.Expirations04;
import zen.sin.bio.orassbio.entites.Expirations30;
import zen.sin.bio.orassbio.entites.SmsProduction;
import zen.sin.bio.orassbio.service.ConnectionOracleDataBaseService;


/**
 *
 * @author USER01
 */
@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class ExpirationRestControntroller implements Serializable {

    private ConnectionOracleDataBaseService    connectionOracleDataBaseService;

    @GetMapping("orass/vue-expirations")
    List<Expirations> allExpirations() {
        return connectionOracleDataBaseService.vuesExpiration();
    }
    
     @GetMapping("orass/vue-expirations04")
    List<Expirations04> allExpirations04() {
        return connectionOracleDataBaseService.vuesExpiration04();
    }
    
       @GetMapping("orass/vue-expirations30")
    List<Expirations30> allExpirations30() {
        return connectionOracleDataBaseService.vuesExpiration30();
    }
    @GetMapping("orass/vue-sms-production")
    List<SmsProduction> allSmsProduction() {
        return connectionOracleDataBaseService.vuesSmsProduction();
    }
     

}
