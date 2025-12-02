/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.zenbio.apirest.repertory;

import com.zenbio.apirest.entites.TauxPrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author USER01
 */
@Repository
public interface TauxPrestationRepository extends JpaRepository<TauxPrestation, Integer>{
    Optional<TauxPrestation> findByPoliceAndTypePrestationIdAndGroupe(String police, String typePrestationId, short groupe);
//      @Query("SELECT MAX(t.id) FROM TauxPrestation t")
//    Integer findMaxId();
    
}
