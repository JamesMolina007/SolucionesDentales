/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Administradores;

import DBA.Dba;
import Modelos.Administradores;
import Modelos.Doctores;
import Modelos.Usuarios;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author James Josue Molina
 */
public class UsuariosAdmin {
    private Dba dba = new Dba("SolucionesDentales.accdb");
    private Usuarios usuarioActual;
    private Usuarios usuarioAnterior;
    
    public UsuariosAdmin(){
        dba.conectar();
    }
    
    public ArrayList<Usuarios> obtenerUsuarios(){
        ArrayList<Usuarios> usuarios = new ArrayList();
        try{
            String query = "SELECT * FROM Usuarios";
            dba.query.execute(query);
            ResultSet rs = dba.query.getResultSet();
            while (rs.next()) {
                String usuario = rs.getString("usuario");
                String contrasena = rs.getString("contrasena");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo");
                int id = rs.getInt("id");
                if(tipo.equals("Doctor"))
                    usuarios.add(new Doctores(usuario,contrasena,nombre,id));
                else
                    usuarios.add(new Administradores(usuario,contrasena,nombre,id));
            }
        }catch(Exception ex){
            
        }
        
        return usuarios;
    }
    
    public Usuarios obtenerUsuario(int id){
        Usuarios usuarioObj = null;
        try{
            String query = "SELECT * FROM Usuarios WHERE ID = " + id;
            dba.query.execute(query);
            ResultSet rs = dba.query.getResultSet();
            while (rs.next()) {
                String usuario = rs.getString("usuario");
                String contrasena = rs.getString("contrasena");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo");
                int idDB = rs.getInt("id");
                if(tipo.equals("Doctor"))
                    usuarioObj = new Doctores(usuario,contrasena,nombre,idDB);
                else
                    usuarioObj = new Administradores(usuario,contrasena,nombre,idDB);
            }
        }catch(Exception ex){
            
        }
        
        return usuarioObj;
    }
    
    public boolean guardarUsuario(Usuarios usuario){
        try{
            String query;
            String tipo = (usuario instanceof Doctores) ? "Doctor" : "Administrador";
            if(usuario.getId() == -1)
                query = "INSERT INTO Usuarios (Usuario, Contrasena, Nombre, Tipo) VALUES ('" + usuario.getUsuario() + "','" + usuario.getContrasena() + "','" + usuario.getNombre() + "','" + tipo + "')";
            else
                query = "UPDATE Usuarios SET Usuario = '" + usuario.getUsuario() + "', Contrasena='" + usuario.getContrasena() + "', Nombre='" + usuario.getNombre() + "', Tipo='" + tipo + "' WHERE ID = " + usuario.getId();
            dba.query.execute(query);
            dba.commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean borrarUsuario(int id){
        try{
            String query = "DELETE FROM Usuarios WHERE ID = " + id;
            dba.query.execute(query);
            dba.commit();
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    
}
