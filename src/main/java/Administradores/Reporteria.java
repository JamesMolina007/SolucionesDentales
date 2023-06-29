/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Administradores;

import Modelos.Citas;
import Modelos.Procedimientos;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author James Josue Molina
 */
public class Reporteria implements Runnable{
    private JFrame padre;
    private JButton boton;
    private String filtro = "";
    
    public Reporteria(JFrame padre, JButton boton){
        this.padre = padre;
        this.boton = boton;
    }
    
    public void setFiltro(String filtro){
        this.filtro = filtro;
    }
  
    private boolean generarReporte(){
        CitasAdmin citas = new CitasAdmin();
        ArrayList<Citas> lista = citas.obtenerCitas(filtro + " ORDER BY FECHA DESC");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte");
        
        Row row = sheet.createRow(0);
        
        Cell paciente = row.createCell(0);
        paciente.setCellValue("Paciente");
        
        Cell correo = row.createCell(1);
        correo.setCellValue("Correo");
        
        Cell celular = row.createCell(2);
        celular.setCellValue("Celular");
        
        Cell doctor = row.createCell(3);
        doctor.setCellValue("Doctor");
        
        Cell procedimientos = row.createCell(4);
        procedimientos.setCellValue("Procedimientos");
        
        Cell fecha = row.createCell(5);
        fecha.setCellValue("Fecha");
        
        Cell hora = row.createCell(6);
        hora.setCellValue("Hora");
        
        Cell estado = row.createCell(7);
        estado.setCellValue("Estado");
        
        for (int i = 0; i < lista.size(); i++) {
            Citas cita = lista.get(i);
            Row fila = sheet.createRow(i+1);
            
            Cell pacienteVal = fila.createCell(0);
            pacienteVal.setCellValue(cita.getPaciente().getPaciente());

            Cell correoVal = fila.createCell(1);
            correoVal.setCellValue(cita.getPaciente().getCorreo());

            Cell celularVal = fila.createCell(2);
            celularVal.setCellValue(cita.getPaciente().getCorreo());

            Cell doctorVal = fila.createCell(3);
            doctorVal.setCellValue(cita.getDoctor().getNombre());

            String procedimientosStr = "";
            for (Procedimientos procedimiento : cita.getProcedimiento()) {
                procedimientosStr += procedimiento.getNombre() + ", ";
            }
            Cell procedimientosVal = fila.createCell(4);
            procedimientosVal.setCellValue(procedimientosStr);

            Cell fechaVal = fila.createCell(5);
            fechaVal.setCellValue(cita.getFecha());
        
            Cell horaVal = fila.createCell(6);
            horaVal.setCellValue(cita.getHora());
            
            Cell estadoVal = fila.createCell(7);
            estadoVal.setCellValue(cita.getEstado());
        }
        
        try{
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);
            String nombreArchivo = "Reporte_" + formattedDate + ".xlsx";
            String rutaDescargas = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + nombreArchivo;
            FileOutputStream fileOut = new FileOutputStream(rutaDescargas);
            workbook.write(fileOut);
            workbook.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void run() {
        if(generarReporte()){
            JOptionPane.showMessageDialog(padre, "Reporte generado en descargas");
        }else{
            JOptionPane.showMessageDialog(padre, "Ha ocurrido un error");
        }
        if(filtro.length() <= 0)
            boton.setText("Generar reporte");
        else
            boton.setText("Reporte mensual");
    }
}
