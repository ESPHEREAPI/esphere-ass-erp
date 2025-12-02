///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package zen.sin.bio.orassbio.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import zen.sin.bio.orassbio.dtos.RenouvellementDto;
//import zen.sin.bio.orassbio.entites.ZenGarantie;
//import zen.sin.bio.orassbio.entites.ZenRenouvellement;
//import zen.sin.bio.orassbio.mappers.BiometrieMapperImpl;
//import zen.sin.bio.orassbio.repository.ZenGarantieRepository;
//import zen.sin.bio.orassbio.repository.ZenRenouvellementRepository;
//
///**
// *
// * @author USER01
// */
//@Service
//@AllArgsConstructor
//public class RenouvellementServiceImpl implements RenouvellementService {
//    
//    private ZenRenouvellementRepository zenRenouvellementRepository;
//    private BiometrieMapperImpl mappers;
//    private ZenGarantieRepository zenGarantieRepository;
//    
//    @Override
//    public List<ZenRenouvellement> listeRenouvellement() {
//        List<ZenRenouvellement> listeZenRenouvellement = new ArrayList<>();
////      listeZenRenouvellement=zenRenouvellementRepository.findAll().subList(0, 6);
//        listeZenRenouvellement = zenRenouvellementRepository.allZenRenouvellement();
//        listeZenRenouvellement.forEach(System.out::println);
////        List<RenouvellementDto> listeRenouvellement = listeZenRenouvellement.stream()
////                .map(zr -> mappers.formOZenRenouvellement(zr))
////                .collect(Collectors.toList());
//        return listeZenRenouvellement;
//    }
//    
//    @Override
//    public List<ZenRenouvellement> listeRenouvellement(int police) {
//        List<ZenRenouvellement> listeRenouvellement = zenRenouvellementRepository.findByNUMEPOLI(police);
////                .stream()
////                .map(zr -> mappers.formOZenRenouvellement(zr))
////                .collect(Collectors.toList());
//        return listeRenouvellement;
//        
//    }
//    
//    @Override
//    public RenouvellementDto listeRenouvellementByPolice(String police) {
//        if(police.contains("-")==false){
//             return new RenouvellementDto();
//        }
//        String[] data = police.split("-");
//        int agence = 0;
//        int num_police = 0;
//        ZenRenouvellement zenRenouvellement;
//        List<ZenGarantie> listeGaranties = new ArrayList<>();
//        
//        if (data.length > 0) {
//            agence = Integer.parseInt(data[0]);
//            num_police = Integer.parseInt(data[1]);
//        }
////        zenRenouvellementRepository.allZenRenouvellementPolice(num_police,agence).forEach(System.out::println);
//
//        List<ZenRenouvellement> listeRenouvellement = zenRenouvellementRepository.allZenRenouvellementPolice(num_police, agence);
//        listeRenouvellement.forEach(System.out::println);
//        if (listeRenouvellement.isEmpty()) {
//            return new RenouvellementDto();
//        }
//        zenRenouvellement = listeRenouvellement.get(0);
//        if (zenRenouvellement.getNUMEAVEN() == null) {
//            listeGaranties = zenGarantieRepository.listGarantiesByPoliceAvenantIsNull(num_police, agence);
//        } else {
//            listeGaranties = zenGarantieRepository.listGarantiesByPoliceAvenantIsNotNull(num_police, agence, zenRenouvellement.getNUMEAVEN());
//        }
//       RenouvellementDto renouvellementDto= mappers.formOZenRenouvellement(zenRenouvellement, listeGaranties);
//        //zenRenouvellement
//
////                .stream()
////                .map(zr -> mappers.formOZenRenouvellement(zr))
////                .collect(Collectors.toList());
//        return renouvellementDto;
//        
//    }
//
//    @Override
//    public List<ZenRenouvellement> listeRenouvellementAllPolice(String police) {
//     
//      if(police.contains("-")==false){
//             return new ArrayList<>();
//        }
//        String[] data = police.split("-");
//        int agence = 0;
//        int num_police = 0;
//        ZenRenouvellement zenRenouvellement;
//        List<ZenGarantie> listeGaranties = new ArrayList<>();
//        
//        if (data.length > 0) {
//            agence = Integer.parseInt(data[0]);
//            num_police = Integer.parseInt(data[1]);
//        }
//         List<ZenRenouvellement> listeRenouvellement = zenRenouvellementRepository.allZenRenouvellementPolice(num_police, agence);
//         return listeRenouvellement;
//    }
//    
//}
