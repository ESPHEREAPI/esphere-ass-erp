/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;


import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author JIATOU FRANCK
 */
@Data
public class UserLogin implements Serializable{
    private int id;
  private String userName;
  private   String passWord;
  
}
