///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package zen.sin.bio.orassbio.web;
//
//import java.io.Serializable;
//import java.util.List;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import zen.sin.bio.orassbio.dtos.SinistreBioDto;
//import zen.sin.bio.orassbio.dtos.Zen_Sinistre_BioDto;
//import zen.sin.bio.orassbio.entites.ZenSinistreBio;
//import zen.sin.bio.orassbio.service.ZenSinistreBioServive;
//
///**
// *
// * @author USER01
// */
//@RestController
//@AllArgsConstructor
//@Slf4j
//@CrossOrigin("*")
//public class SinistreRestController  implements Serializable{
//    
//    private ZenSinistreBioServive zenSinistreBioServive;
//    
//    @GetMapping("orass/sin-prestations")
//    SinistreBioDto listesPrestationSave(){
//      return zenSinistreBioServive.saveAllDataPrestations();
//    }
//    
//    @GetMapping("orass/sin-consultations")
//     SinistreBioDto listesConsultationSave(){
//      return zenSinistreBioServive.saveAllDataConsultations();
//    }
//     
//     @GetMapping("orass/sin-biometry")
//     List<ZenSinistreBio>  listeAllSinistreBiometry(){
//         return zenSinistreBioServive.listesAllData();
//     }
//}
