/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Administradores;

import DBA.Dba;
import Modelos.Citas;
import Modelos.Procedimientos;
import Modelos.Registros;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author James Josue Molina
 */
public class RegistrosAdmin {
    private Dba dba = new Dba("SolucionesDentales.accdb");
    
    public RegistrosAdmin(){
        dba.conectar();
    }
    
    public ArrayList<Registros> obtenerRegistros(){
        ArrayList<Registros> registros = new ArrayList();
        try{
            String sql = "SELECT * FROM REGISTROS";
            dba.query.execute(sql);
            ResultSet rs = dba.query.getResultSet();
            while(rs.next()){
                Registros r = new Registros(
                        rs.getInt("ID"),
                        rs.getString("PACIENTE"),
                        rs.getString("CORREO"),
                        rs.getString("CELULAR"),
                        rs.getString("DOCTOR"),
                        rs.getString("PROCEDIMIENTOS"),
                        rs.getString("FECHA"),
                        rs.getString("HORA"),
                        rs.getString("ESTADO")
                );
                registros.add(r);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return registros;
    }
    
    public void registrar(ArrayList<Citas> citas){
        try{
            for (Citas cita : citas) {
                String procedimientos = "";
                for (Procedimientos procedimiento : cita.getProcedimiento())
                    procedimientos += procedimiento + ", ";
                String query = "INSERT INTO REGISTROS (PACIENTE, CORREO, CELULAR, DOCTOR, PROCEDIMIENTOS, FECHA, HORA, ESTADO) VALUES"
                    + "('" + cita.getPaciente().getPaciente() + "', "
                        + " '" + cita.getPaciente().getCorreo()+ "', "
                        + " '" + cita.getPaciente().getTelefono()+ "', "
                        + " '" + cita.getDoctor().getNombre() + "', "
                        + " '" + procedimientos + "', "
                        + " '" + cita.getFecha() + "', "
                        + " '" + cita.getHora() + "', "
                        + " '" + cita.getEstado() +  "')";
                dba.query.addBatch(query);
            }
            dba.query.executeBatch();
            dba.commit();
            dba.query.clearBatch();
            for (Citas cita : citas) {
                String query = "DELETE FROM CITAS WHERE ID = " + cita.getId();
                dba.query.addBatch(query);
            }
            dba.query.executeBatch();
            dba.commit();
            dba.query.clearBatch();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
