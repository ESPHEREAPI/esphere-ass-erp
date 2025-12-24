/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.repository;


import db.biometry.biometry.entites.Utilisateur;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author JIATOU FRANCK
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Integer>{
    Utilisateur findByLogin(String login);
    @Query("SELECT u FROM Utilisateur u WHERE u.nom  like :kw or u.login like :kw")
     public List<Utilisateur> searchUser(@Param(value ="kw")String keyword);
    
    
    
}
