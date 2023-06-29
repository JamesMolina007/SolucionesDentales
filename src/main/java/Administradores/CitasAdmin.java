/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Administradores;

import DBA.Dba;
import Modelos.Citas;
import Modelos.Doctores;
import Modelos.Pacientes;
import Modelos.Procedimientos;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author James Josue Molina
 */
public class CitasAdmin {
    private Dba dba = new Dba("SolucionesDentales.accdb");
    
    public CitasAdmin(){
        dba.conectar();
    }
    
    public ArrayList<Citas> obtenerCitas(String filtro){
        ArrayList<Citas> citas = new ArrayList();
        try{
            String query = "SELECT * FROM CITAS " + filtro;
            dba.query.execute(query);
            ResultSet rs = dba.query.getResultSet();
            while(rs.next()){
                Citas cita = new Citas();
                Pacientes paciente = new Pacientes();
                Doctores doctor = new Doctores();
                ArrayList<Procedimientos> procedimientos = new ArrayList();
                int idCitaCita = rs.getInt("ID");
                int idPacienteCita = rs.getInt("IDPACIENTE");
                int idDoctorCita = rs.getInt("IDDOCTOR");
                String fechaCita = rs.getString("FECHA");
                String horaCita = rs.getString("HORA");
                String estadoCita = rs.getString("ESTADO");
                query = "SELECT * FROM PACIENTES WHERE ID = " + idPacienteCita;
                try{
                    dba.query.execute(query);
                    ResultSet rsPaciente = dba.query.getResultSet();
                    while(rsPaciente.next()){
                        int idPaciente = rsPaciente.getInt("ID");
                        String nombrePaciente = rsPaciente.getString("NOMBRE");
                        String correoPaciente = rsPaciente.getString("CORREO");
                        String celularPaciente = rsPaciente.getString("CELULAR");
                        paciente = new Pacientes(nombrePaciente, correoPaciente, celularPaciente, idPaciente);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                
                query = "SELECT * FROM USUARIOS WHERE ID = " + idDoctorCita;
                try{
                    dba.query.execute(query);
                    ResultSet rsDoctor = dba.query.getResultSet();
                    while(rsDoctor.next()){
                        int idDoctor = rsDoctor.getInt("ID");
                        String usuarioDoctor = rsDoctor.getString("USUARIO");
                        String contrasenaDoctor = rsDoctor.getString("CONTRASENA");
                        String nombreDoctor = rsDoctor.getString("NOMBRE");
                        doctor = new Doctores(usuarioDoctor, contrasenaDoctor, nombreDoctor, idDoctor);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                
                try{
                    query = "SELECT P.* FROM PROCEDIMIENTOS AS P, PROCEDIMIENTOSPORCITA AS PPC "
                        + "WHERE P.ID = PPC.IDPROCEDIMIENTO AND PPC.IDCITA = " + idCitaCita;
                    dba.query.execute(query);
                    ResultSet rsProcedimientos = dba.query.getResultSet();
                    while(rsProcedimientos.next()){
                        int idProcedimiento = rsProcedimientos.getInt("ID");
                        String nombre = rsProcedimientos.getString("NOMBRE");
                        procedimientos.add(new Procedimientos(nombre, idProcedimiento));
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                citas.add(new Citas(doctor, paciente, fechaCita, horaCita, procedimientos, idCitaCita, estadoCita));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return citas;
    }
    
    public Citas obtenerCita(int id){
        try{
            String query = "SELECT * FROM CITAS WHERE ID = " + id;
            dba.query.execute(query);
            ResultSet rs = dba.query.getResultSet();
            while(rs.next()){
                Pacientes paciente = new Pacientes();
                Doctores doctor = new Doctores();
                ArrayList<Procedimientos> procedimientos = new ArrayList();
                
                int idCitaCita = rs.getInt("ID");
                String idPacienteCita = rs.getString("IDPACIENTE");
                String idDoctorCita = rs.getString("IDDOCTOR");
                String fechaCita = rs.getString("FECHA");
                String horaCita = rs.getString("HORA");
                String estadoCita = rs.getString("ESTADO");
                
                query = "SELECT * FROM PACIENTES WHERE ID = " + idPacienteCita;
                dba.query.execute(query);
                ResultSet rsPaciente = dba.query.getResultSet();
                int idPaciente = rsPaciente.getInt("ID");
                String nombrePaciente = rsPaciente.getString("NOMBRE");
                String correoPaciente = rsPaciente.getString("CORREO");
                String celularPaciente = rsPaciente.getString("CELULAR");
                paciente = new Pacientes(nombrePaciente, correoPaciente, celularPaciente, idPaciente);
                
                query = "SELECT * FROM USUARIOS WHERE ID = " + idDoctorCita;
                dba.query.execute(query);
                ResultSet rsDoctor = dba.query.getResultSet();
                int idDoctor = rsDoctor.getInt("ID");
                String usuarioDoctor = rsDoctor.getString("USUARIO");
                String contrasenaDoctor = rsDoctor.getString("CONTRASENA");
                String nombreDoctor = rsDoctor.getString("NOMBRE");
                doctor = new Doctores(usuarioDoctor, contrasenaDoctor, nombreDoctor, idDoctor);
                
                query = "SELECT P.* FROM PROCEDIMIENTOS AS P, PROCEDIMIENTOSPORCITA AS PPC "
                        + "WHERE P.ID = PPC.IDPROCEDIMIENTO AND PPC.IDCITA = " + idCitaCita;
                dba.query.execute(query);
                ResultSet rsProcedimientos = dba.query.getResultSet();
                while(rsProcedimientos.next()){
                    int idProcedimiento = rsProcedimientos.getInt("ID");
                    String nombre = rsProcedimientos.getString("NOMBRE");
                    procedimientos.add(new Procedimientos(nombre, idProcedimiento));
                }
                
                return new Citas(doctor, paciente, fechaCita, horaCita, procedimientos, idCitaCita, estadoCita);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public boolean guardarCita(Citas cita){
        try{
            String query = "";
            int idCita = cita.getId();
            if(cita.getId() == -1)
                query = "INSERT INTO CITAS (IDPACIENTE, IDDOCTOR, FECHA, HORA) VALUES (" + cita.getPaciente().getId() + "," + cita.getDoctor().getId() + ",'" + cita.getFecha() + "','" + cita.getHora() + "')";
            else
                query = "UPDATE CITAS SET IDPACIENTE=" + cita.getPaciente().getId() + ", IDDOCTOR=" + cita.getDoctor().getId() + ", FECHA='" + cita.getFecha() + "', HORA='" + cita.getHora() + "', ESTADO='" + cita.getEstado() + "' WHERE ID=" + cita.getId();
            
            dba.query.execute(query);
            
            if(cita.getId() == -1){
                ResultSet generatedKeys = dba.query.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idCita = generatedKeys.getInt(1);
                }
            }
            
            query = "DELETE FROM PROCEDIMIENTOSPORCITA WHERE IDCITA = " + cita.getId();
            dba.query.execute(query);
            
            for (Procedimientos procedimientos : cita.getProcedimiento()) {
                query = "INSERT INTO PROCEDIMIENTOSPORCITA (IDCITA, IDPROCEDIMIENTO) VALUES (" + idCita + "," + procedimientos.getId() + ")";
                dba.query.addBatch(query);
            }
            dba.query.executeBatch();
            dba.query.clearBatch();
            dba.commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean borrarCita(int id){
        try{
            String query = "DELETE FROM CITAS WHERE ID = " + id;
            dba.query.execute(query);
            dba.commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
