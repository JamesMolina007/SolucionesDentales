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
public class Administradores extends Usuarios implements Serializable{

    public Administradores() {
    }

    public Administradores(String usuario, String contrasena, String nombre) {
        super(usuario, contrasena, nombre);
    }
    
    public Administradores(String usuario, String contrasena, String nombre, int id) {
        super(usuario, contrasena, nombre, id);
    }
    
}
