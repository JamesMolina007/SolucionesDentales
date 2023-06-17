/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author James Josue Molina
 */
public class Citas {
    private String doctor;
    private String paciente;
    private Date fecha;
    private ArrayList<Procedimientos> procedimiento;

    public Citas() {
    }

    public Citas(String doctor, String paciente, Date fecha, ArrayList<Procedimientos> procedimiento) {
        this.doctor = doctor;
        this.paciente = paciente;
        this.fecha = fecha;
        this.procedimiento = procedimiento;
    }
    
    public Citas(String doctor, String paciente, Date fecha) {
        this.doctor = doctor;
        this.paciente = paciente;
        this.fecha = fecha;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Procedimientos> getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(ArrayList<Procedimientos> procedimiento) {
        this.procedimiento = procedimiento;
    }
    
    
    
}
