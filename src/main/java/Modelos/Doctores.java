/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.io.Serializable;
/**
 *
 * @author James Josue Molina
 */
public class Doctores extends Usuarios implements Serializable{
   
    public Doctores(){
        
    }
    
    public Doctores(String usuario, String contrasena, String nombre) {
        super(usuario, contrasena, nombre);
    }
    
    public Doctores(String usuario, String contrasena, String nombre, int id) {
        super(usuario, contrasena, nombre, id);
    }    
    
}
