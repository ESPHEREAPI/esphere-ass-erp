/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.zenbio.apirest.repertory;

import com.zenbio.apirest.entites.Dbx45tyTypePrestation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author USER01
 */
public interface TypePrestationRepository extends JpaRepository<Dbx45tyTypePrestation, String>{
    
    Optional<Dbx45tyTypePrestation> findByNom(String nom);
    
}
