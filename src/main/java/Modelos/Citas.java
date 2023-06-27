/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.util.ArrayList;

/**
 *
 * @author James Josue Molina
 */
public class Citas {
    private int id = -1;
    private Doctores doctor;
    private Pacientes paciente;
    private String fecha;
    private String hora;
    private String estado;
    private ArrayList<Procedimientos> procedimiento;

    public Citas() {
    }

    public Citas(Doctores doctor, Pacientes paciente, String fecha, String hora, ArrayList<Procedimientos> procedimiento, int id, String estado) {
        this.doctor = doctor;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.procedimiento = procedimiento;
        this.estado = estado;
        this.id = id;
    }
    
    

    public Doctores getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctores doctor) {
        this.doctor = doctor;
    }

    public Pacientes getPaciente() {
        return paciente;
    }

    public void setPaciente(Pacientes paciente) {
        this.paciente = paciente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public ArrayList<Procedimientos> getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(ArrayList<Procedimientos> procedimiento) {
        this.procedimiento = procedimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return Integer.toString(id);
    }

    
    
    
}
