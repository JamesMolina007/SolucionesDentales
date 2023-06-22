/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.util.ArrayList;
import java.io.Serializable;
/**
 *
 * @author James Josue Molina
 */
public class Doctores extends Usuarios implements Serializable{
    private ArrayList<Citas> citas = new ArrayList();

    public Doctores() {
    }

    public Doctores(String usuario, String contrasena, String nombre) {
        super(usuario, contrasena, nombre);
    }
    
    public Doctores(String usuario, String contrasena, String nombre, int id) {
        super(usuario, contrasena, nombre, id);
    }

    public ArrayList<Citas> getCitas() {
        return citas;
    }

    public void setCitas(ArrayList<Citas> citas) {
        this.citas = citas;
    }
    
    
    
}
