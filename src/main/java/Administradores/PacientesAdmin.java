/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Administradores;

import DBA.Dba;
import Modelos.Pacientes;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author James Josue Molina
 */
public class PacientesAdmin {
    private Dba dba = new Dba("SolucionesDentales.accdb");
    
    public PacientesAdmin(){
        dba.conectar();
    }
    
    public ArrayList<Pacientes> obtenerPacientes(){
        ArrayList<Pacientes> pacientes = new ArrayList();
        try{
            String query = "SELECT * FROM PACIENTES";
            dba.query.execute(query);
            ResultSet rs = dba.query.getResultSet();
            while(rs.next()){
                int id = rs.getInt("ID");
                String nombre = rs.getString("Nombre");
                String correo = rs.getString("Correo");
                String celular = rs.getString("Celular");
                pacientes.add(new Pacientes(nombre, correo, celular, id));
            }
        }catch(Exception ex){
        }
        return pacientes;
    }
    
    public Pacientes obtenerPaciente(int id){
        try{
            String query = "SELECT * FROM PACIENTES WHERE ID = " + id;
            dba.query.execute(query);
            ResultSet rs = dba.query.getResultSet();
            while(rs.next()){
                id = rs.getInt("ID");
                String nombre = rs.getString("Nombre");
                String correo = rs.getString("Correo");
                String celular = rs.getString("Celular");
                return new Pacientes(nombre, correo, celular, id);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public boolean guardarPaciente(Pacientes paciente){
        try{
            String query = "";
            if(paciente.getId() == -1)
                query = "INSERT INTO PACIENTES (NOMBRE, CORREO, CELULAR) VALUES ('" + paciente.getPaciente() + "','" + paciente.getCorreo() + "','" + paciente.getTelefono() + "')";
            else
                query = "UPDATE PACIENTES SET NOMBRE = '" + paciente.getPaciente() + "', CORREO='" + paciente.getCorreo() + "', CELULAR='" + paciente.getTelefono() + "' WHERE ID = " + paciente.getId();
            dba.query.execute(query);
            dba.commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean borrarPaciente(int id){
        try{
            String query = "DELETE FROM PACIENTES WHERE ID = " + id;
            dba.query.execute(query);
            dba.commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    
}
