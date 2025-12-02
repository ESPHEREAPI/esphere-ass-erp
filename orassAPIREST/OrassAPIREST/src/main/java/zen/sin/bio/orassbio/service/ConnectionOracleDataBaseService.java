/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package zen.sin.bio.orassbio.service;

import java.util.List;
import zen.sin.bio.orassbio.dtos.RenouvellementDto;
import zen.sin.bio.orassbio.entites.Expirations;
import zen.sin.bio.orassbio.entites.Expirations04;
import zen.sin.bio.orassbio.entites.Expirations30;
import zen.sin.bio.orassbio.entites.SmsProduction;
import zen.sin.bio.orassbio.entites.ZenRenouvellement;
import zen.sin.bio.orassbio.entites.ZenSinistreLn;

/**
 *
 * @author USER01
 */
public interface ConnectionOracleDataBaseService {

    java.sql.Connection connectionOracle();

    void deconect();

    public List<Expirations> vuesExpiration();

    public List<Expirations30> vuesExpiration30();

    public List<Expirations04> vuesExpiration04();

    public List<SmsProduction> vuesSmsProduction();

    public List<ZenRenouvellement> listeRenouvellement();

    public List<ZenRenouvellement> listeRenouvellement(int police);

    public RenouvellementDto listeRenouvellementByPolice(String police);

    public List<ZenRenouvellement> listeRenouvellementAllPolice(String police);
    public List<ZenSinistreLn> listeSinistresByNumber(String numberSinistre);
    public List<ZenSinistreLn> listesSinistreByPolice(String police);
    public List<ZenSinistreLn> listesAllSinistre();

}
