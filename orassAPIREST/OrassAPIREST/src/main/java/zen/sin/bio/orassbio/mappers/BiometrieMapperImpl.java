/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.mappers;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import zen.sin.bio.orassbio.dtos.RenouvellementDto;
import zen.sin.bio.orassbio.entites.ZenGarantie;
import zen.sin.bio.orassbio.entites.ZenRenouvellement;


/**
 *
 * @author USER01
 */
@Service
@AllArgsConstructor
@Data
public class BiometrieMapperImpl  implements Serializable{

//    public ZenSinistreBio fromObjetJSON(Zen_Sinistre_BioDto zsbDTO) {
//        //ZenSinistreBioPK zenSinistreBioPK= new ZenSinistreBioPK(zsbDTO.getPrestataire(),zsbDTO.getNumepoli(),zsbDTO.getCodeinte(),zsbDTO.getCoderisq(),zsbDTO.getCodememb(),zsbDTO.getTypeexam(),IdleDate.parseString(zsbDTO.getDate_validation(), "dd/MM/yyy"));
//        ZenSinistreBio zenSinistreBio = new ZenSinistreBio(zsbDTO.getPrestataire(), zsbDTO.getNumepoli(), zsbDTO.getCodeinte(), zsbDTO.getCoderisq(), zsbDTO.getCodememb(), zsbDTO.getTypeexam(), IdleDate.parseString(zsbDTO.getDate_validation(), "dd/MM/yyy"));
//
//        zenSinistreBio.setDate_ecaissement(IdleDate.parseString(zsbDTO.getDate_ecaissement(), "dd/MM/yyy"));
//        if (zsbDTO.getMontant_valid() != null) {
//            zenSinistreBio.setMontant_valid(zsbDTO.getMontant_valid().longValue());
//        }
//        if (zsbDTO.getTaux_remb() != null) {
//            zenSinistreBio.setTaux_remb(zsbDTO.getTaux_remb().shortValue());
//        }
//        zenSinistreBio.setCode_pres_bio(zsbDTO.getCode_pres_bio());
//
//        return zenSinistreBio;
//
//    }

//    public Zen_Sinistre_BioDto fromObjetZenSinistreBio(ZenSinistreBio sin) {
//        Zen_Sinistre_BioDto sinistre = new Zen_Sinistre_BioDto();
//        BeanUtils.copyProperties(sin, sinistre);
//        sinistre.setPrestataire(sin.getPrestataire());
//        sinistre.setCodeinte(sin.getCodeinte());
//        return sinistre;
//    }

    public RenouvellementDto formOZenRenouvellement(ZenRenouvellement zr,
            List<ZenGarantie> listesGaranties) {
        RenouvellementDto renouvellementDto = new RenouvellementDto();
//        prestationDto.setCodeAssurePrincipal(lignePrestation.);
        BeanUtils.copyProperties(zr, renouvellementDto);
        renouvellementDto.setListeGaranties(listesGaranties);
        System.out.println("renouvellement: "+zr.getTYPE_AVENANT()+" "+zr.getNUMEAVEN()+" duree courant "+zr.getDUREE_COURANTE()+"duree total "+zr.getDUREETOTALE());
        return renouvellementDto;
    }

}
