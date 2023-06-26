/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Administradores;

import DBA.Dba;
import Modelos.Procedimientos;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author James Josue Molina
 */
public class ProcedimientosAdmin {
    private Dba dba = new Dba("SolucionesDentales.accdb");
    
    public ProcedimientosAdmin(){
        dba.conectar();
    }
    
    public ArrayList<Procedimientos> obtenerProcedimientos(){
        ArrayList<Procedimientos> procedimientos = new ArrayList();
        try{
            String query = "SELECT * FROM PROCEDIMIENTOS";
            dba.query.execute(query);
            ResultSet rs = dba.query.getResultSet();
            while(rs.next()){
                int id = rs.getInt("ID");
                String nombre = rs.getString("Nombre");
                procedimientos.add(new Procedimientos(nombre, id));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return procedimientos;
    }
    
    public boolean guardarProcedimiento(String procedimiento){
        try{
            String query = "INSERT INTO PROCEDIMIENTOS (NOMBRE) VALUES ('" + procedimiento + "')";
            dba.query.execute(query);
            dba.commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean borrarProcedimiento(Procedimientos procedimiento){
        try{
            String query = "DELETE FROM PROCEDIMIENTOS WHERE ID = " + procedimiento.getId();
            dba.query.execute(query);
            dba.commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
}
