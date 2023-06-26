/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author James Josue Molina
 */
public class Pacientes {
    private int id = -1;
    private String paciente;
    private String correo;
    private String telefono;

    public Pacientes() {
    }

    
    
    public Pacientes(String paciente, String correo, String telefono) {
        this.paciente = paciente;
        this.correo = correo;
        this.telefono = telefono;
    }
    
    public Pacientes(String paciente, String correo, String telefono, int id) {
        this.paciente = paciente;
        this.correo = correo;
        this.telefono = telefono;
        this.id = id;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return paciente;
    }
    
}
