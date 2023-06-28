/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

import Administradores.CitasAdmin;
import Administradores.PacientesAdmin;
import Administradores.ProcedimientosAdmin;
import Administradores.UsuariosAdmin;
import Modelos.Administradores;
import Modelos.Citas;
import Modelos.Doctores;
import Modelos.Pacientes;
import Modelos.Procedimientos;
import Modelos.Usuarios;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;

/**
 *
 * @author James Josue Molina
 */
public class Administrador extends javax.swing.JFrame {

    /**
     * Creates new form Administrador
     */
    private Usuarios usuarioActual;

    private UsuariosAdmin usuariosAdmin = new UsuariosAdmin();
    private ProcedimientosAdmin procedimientosAdmin = new ProcedimientosAdmin();
    private PacientesAdmin pacientesAdmin = new PacientesAdmin();
    private CitasAdmin citasAdmin = new CitasAdmin();
    
    private ArrayList<Usuarios> usuarios = new ArrayList();
    private ArrayList<Pacientes> pacientes = new ArrayList();
    private ArrayList<Procedimientos> procedimientos = new ArrayList();
    private ArrayList<Citas> citas = new ArrayList();
    
    public Administrador() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        cargarTablaUsuarios();
        cargarTablaPacientes();
        cargarListaProcedimientos();
        cargarTablaCitas();
        
        lbl_id.setVisible(false);
        lbl_idPaciente.setVisible(false);
        lbl_idCita.setVisible(false);
        
        deshabilitarUsuario();
        deshabilitarPaciente();
        deshabilitarCitas();
    }

    public void setUsuario(Usuarios usuario){
        this.usuarioActual = usuario;
    }
    
    private void cargarTablaUsuarios(){
        usuarios = usuariosAdmin.obtenerUsuarios();
        DefaultTableModel modelo = (DefaultTableModel)jt_usuarios.getModel();
        DefaultComboBoxModel modeloCB = (DefaultComboBoxModel)cb_doctores.getModel();
        modeloCB.removeAllElements();
        int cantidadFilas = modelo.getRowCount();
        for (int i = 0; i < cantidadFilas; i++) modelo.removeRow(0);
        for (Usuarios usuario : usuarios) {
            if(usuario instanceof Doctores) modeloCB.addElement(usuario);
            String tipo = (usuario instanceof Doctores) ? "Doctor" : "Administrador";
            Object[] fila = {usuario.getId(), usuario.getUsuario(), usuario.getNombre(), tipo};
            modelo.addRow(fila);
        }
    }
    
    private void cargarTablaPacientes(){
        pacientes = pacientesAdmin.obtenerPacientes();
        DefaultTableModel modelo = (DefaultTableModel)jt_pacientes.getModel();
        DefaultComboBoxModel modeloCB = (DefaultComboBoxModel)cb_pacientes.getModel();
        modeloCB.removeAllElements();
        int cantidadFilas = modelo.getRowCount();
        for (int i = 0; i < cantidadFilas; i++) modelo.removeRow(0);
        for (Pacientes paciente : pacientes) {
            modeloCB.addElement(paciente);
            Object[] fila = {paciente.getId(), paciente.getPaciente(), paciente.getCorreo(), paciente.getTelefono()};
            modelo.addRow(fila);
        }
    }
    
    private void cargarListaProcedimientos(){
        procedimientos = procedimientosAdmin.obtenerProcedimientos();
        DefaultListModel modelo = (DefaultListModel)jl_procedimientoExistente.getModel();
        modelo.removeAllElements();
        for (Procedimientos procedimiento : procedimientos) {
            modelo.addElement(procedimiento);
        }
    }
    
    private void cargarTablaCitas(){
        citas = citasAdmin.obtenerCitas("");
        DefaultTableModel modelo = (DefaultTableModel)jt_citas.getModel();
        int cantidadFilas = modelo.getRowCount();
        for (int i = 0; i < cantidadFilas; i++) modelo.removeRow(0);
        for (Citas cita : citas) {
            String procedimientosStr = "";
            for (Procedimientos p : cita.getProcedimiento()) {
                procedimientosStr += p.getNombre() + ", ";
            }
            Object[] fila = {cita, cita.getPaciente(), cita.getDoctor(), cita.getFecha() + " " + cita.getHora(), procedimientosStr};
            modelo.addRow(fila);
        }
        cargarTablaPrincipal("WHERE ESTADO='No Realizado' OR ESTADO = 'Cancelado'");
    }
    
    private void cargarTablaPrincipal(String filtro){
        citas = citasAdmin.obtenerCitas(filtro);
        DefaultTableModel modeloPrincipal = (DefaultTableModel)jt_citasPrincipal.getModel();
        int cantidadFilas = modeloPrincipal.getRowCount();
        for (int i = 0; i < cantidadFilas; i++) modeloPrincipal.removeRow(0);
        for (Citas cita : citas) {
            String procedimientosStr = "";
            for (Procedimientos p : cita.getProcedimiento()) {
                procedimientosStr += p.getNombre() + ", ";
            }
            Object[] filaPrincipal = {cita, cita.getPaciente(), cita.getDoctor(), cita.getFecha() + " " + cita.getHora(), procedimientosStr, cita.getEstado()};
            modeloPrincipal.addRow(filaPrincipal);
        }
        
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String estado = (String) table.getValueAt(row, 5);
                String fechaCitaStr = (String) table.getValueAt(row, 3);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                java.util.Date fechaCita;
                try {
                    fechaCita = dateFormat.parse(fechaCitaStr);
                } catch (ParseException e) {
                    fechaCita = null;
                }
                java.util.Date fechaActual = new java.util.Date();

                long diffInMillis = fechaCita.getTime() - fechaActual.getTime();
                long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                if(estado.equals("Realizado")){
                    cellComponent.setBackground(Color.GREEN);
                }else if (estado.equals("Cancelado")) {
                    cellComponent.setBackground(Color.DARK_GRAY);
                    cellComponent.setForeground(Color.WHITE);
                } else if (diffInDays >= 3) {
                    cellComponent.setBackground(Color.LIGHT_GRAY);
                }else if (diffInDays > 1) {
                    cellComponent.setBackground(Color.YELLOW);
                } else if (diffInDays <= 1 && diffInDays > 0) {
                    cellComponent.setBackground(Color.ORANGE);
                } else if (diffInDays <= 0) {
                    cellComponent.setBackground(Color.RED);
                }

                return cellComponent;
            }
        };

        for (int i = 0; i < modeloPrincipal.getRowCount(); i++) {
            for (int j = 0; j < modeloPrincipal.getColumnCount(); j++) {
                jt_citasPrincipal.getColumnModel().getColumn(j).setCellRenderer(renderer);
            }
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jt_citasPrincipal = new javax.swing.JTable();
        btn_enviarRecordatorio = new javax.swing.JButton();
        btn_realizado = new javax.swing.JButton();
        btn_cancelado = new javax.swing.JButton();
        btn_reporte = new javax.swing.JButton();
        cb_realizado = new javax.swing.JCheckBox();
        btn_realizado1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cb_doctores = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jl_procedimientoExistente = new javax.swing.JList<>();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jl_procedimientoElegido = new javax.swing.JList<>();
        btn_agregarProcedimiento = new javax.swing.JButton();
        btn_quitarProcedimiento = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jt_citas = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        tb_correo = new javax.swing.JTextField();
        tb_celular = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jd_fecha = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cb_hora = new javax.swing.JComboBox<>();
        btn_guardar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        lbl_idCita = new javax.swing.JLabel();
        cb_pacientes = new javax.swing.JComboBox<>();
        btn_nuevaCita = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jt_pacientes = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        tb_pacienteAdmin = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        tb_correoAdmin = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        tb_celularAdmin = new javax.swing.JTextField();
        btn_guardarPaciente = new javax.swing.JButton();
        btn_eliminarPaciente = new javax.swing.JButton();
        lbl_idPaciente = new javax.swing.JLabel();
        btn_nuevoPaciente = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tb_usuario = new javax.swing.JTextField();
        tb_nombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tb_contrasena = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cb_tipoUsuario = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        btn_guardarUsuario = new javax.swing.JButton();
        btn_eliminarUsuario = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jt_usuarios = new javax.swing.JTable();
        lbl_id = new javax.swing.JLabel();
        btn_nuevoUsuario = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Interfaz de Admnistración de Citas y Usuarios");

        jt_citasPrincipal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Paciente", "Doctor", "Fecha", "Procedimientos", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jt_citas.setDefaultEditor(Object.class, null);
        jt_citasPrincipal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_citasPrincipalMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jt_citasPrincipal);

        btn_enviarRecordatorio.setText("Enviar Recordatorio");
        btn_enviarRecordatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_enviarRecordatorioActionPerformed(evt);
            }
        });

        btn_realizado.setText("Realizado");
        btn_realizado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_realizadoActionPerformed(evt);
            }
        });

        btn_cancelado.setText("Cancelado");
        btn_cancelado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_canceladoActionPerformed(evt);
            }
        });

        btn_reporte.setText("Reporte");

        cb_realizado.setText("Realizados");
        cb_realizado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_realizadoItemStateChanged(evt);
            }
        });

        btn_realizado1.setText("No Realizado");
        btn_realizado1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_realizado1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btn_realizado)
                        .addGap(9, 9, 9)
                        .addComponent(btn_realizado1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cancelado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_enviarRecordatorio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_reporte)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_realizado)
                        .addGap(0, 201, Short.MAX_VALUE))
                    .addComponent(jScrollPane6))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_enviarRecordatorio)
                    .addComponent(btn_realizado)
                    .addComponent(btn_cancelado)
                    .addComponent(btn_reporte)
                    .addComponent(cb_realizado)
                    .addComponent(btn_realizado1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Citas", jPanel4);

        jLabel6.setText("Paciente:");

        jLabel7.setText("Doctor Encargado:");

        cb_doctores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_doctoresActionPerformed(evt);
            }
        });

        jl_procedimientoExistente.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(jl_procedimientoExistente);

        jLabel8.setText("Procedimientos");

        jl_procedimientoElegido.setModel(new DefaultListModel());
        jScrollPane3.setViewportView(jl_procedimientoElegido);

        btn_agregarProcedimiento.setText("+");
        btn_agregarProcedimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregarProcedimientoActionPerformed(evt);
            }
        });

        btn_quitarProcedimiento.setText("-");
        btn_quitarProcedimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_quitarProcedimientoActionPerformed(evt);
            }
        });

        jButton3.setText(">");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("<");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jt_citas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Paciente", "Doctor", "Fecha", "Procedimientos"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jt_citas.setDefaultEditor(Object.class, null);
        jt_citas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_citasMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jt_citas);

        jLabel9.setText("Correo:");

        tb_correo.setEnabled(false);

        tb_celular.setEnabled(false);

        jLabel10.setText("Teléfono Celular:");

        jLabel11.setText("Fecha de Cita:");

        jLabel12.setText("Hora:");

        cb_hora.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "7:00 am", "7:30 am", "8:00 am", "8:30 am", "9:00 am", "9:30 am", "10:00 am", "10:30 am", "11:00 am", "11:30 am", "12:00 m", "12:30 pm", "1:00 pm", "1:30 pm", "2:00 pm", "2:30 pm", "3:00 pm", "3:30 pm", "4:00 pm", "4:30 pm", "5:00 pm", "5:30 pm", "6:00 pm", "6:30 pm", "7:00 pm" }));
        cb_hora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_horaActionPerformed(evt);
            }
        });

        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        btn_eliminar.setText("Eliminar");
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });

        lbl_idCita.setText("-1");

        cb_pacientes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_pacientesItemStateChanged(evt);
            }
        });
        cb_pacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_pacientesActionPerformed(evt);
            }
        });

        btn_nuevaCita.setText("Nuevo");
        btn_nuevaCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevaCitaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tb_correo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tb_celular, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_doctores, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jd_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton3)
                                    .addComponent(jButton4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn_agregarProcedimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_quitarProcedimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(cb_pacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cb_hora, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_nuevaCita, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_idCita)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_guardar)
                    .addComponent(btn_eliminar)
                    .addComponent(lbl_idCita)
                    .addComponent(cb_pacientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_nuevaCita))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tb_correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tb_celular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_doctores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jd_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4)
                                .addGap(111, 111, 111))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btn_agregarProcedimiento)
                                    .addComponent(btn_quitarProcedimiento))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Admin. Citas", jPanel1);

        jt_usuarios.setDefaultEditor(Object.class, null);
        jt_pacientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Usuario", "Correo", "Telefono"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jt_pacientes.setDefaultEditor(Object.class, null);
        jt_pacientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_pacientesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jt_pacientes);

        jLabel13.setText("Nombre de Paciente:");

        jLabel14.setText("Correo (opcional):");

        jLabel15.setText("Teléfono Celular:");

        btn_guardarPaciente.setText("Guardar");
        btn_guardarPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarPacienteActionPerformed(evt);
            }
        });

        btn_eliminarPaciente.setText("Eliminar");
        btn_eliminarPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarPacienteActionPerformed(evt);
            }
        });

        lbl_idPaciente.setText("-1");

        btn_nuevoPaciente.setText("Nuevo");
        btn_nuevoPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoPacienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tb_pacienteAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(tb_correoAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(tb_celularAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(btn_guardarPaciente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_eliminarPaciente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_idPaciente)
                    .addComponent(btn_nuevoPaciente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(btn_nuevoPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tb_pacienteAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tb_correoAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tb_celularAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_guardarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_eliminarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_idPaciente))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(143, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Admin. Pacientes", jPanel3);

        jLabel2.setText("Usuario:");

        jLabel3.setText("Nombre:");

        jLabel4.setText("Contraseña:");

        cb_tipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Doctor" }));
        cb_tipoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_tipoUsuarioActionPerformed(evt);
            }
        });

        jLabel5.setText("Tipo de Usuario");

        btn_guardarUsuario.setText("Guardar");
        btn_guardarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarUsuarioActionPerformed(evt);
            }
        });

        btn_eliminarUsuario.setText("Eliminar");
        btn_eliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarUsuarioActionPerformed(evt);
            }
        });

        jt_usuarios.setDefaultEditor(Object.class, null);
        jt_usuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Usuario", "Nombre", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jt_usuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_usuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jt_usuarios);

        lbl_id.setText("-1");

        btn_nuevoUsuario.setText("Nuevo");
        btn_nuevoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(tb_contrasena, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(tb_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(tb_usuario)
                    .addComponent(btn_guardarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_eliminarUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_nuevoUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_id)
                    .addComponent(cb_tipoUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_nuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tb_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tb_contrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_tipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_guardarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_eliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(lbl_id)))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Admin. Usuarios", jPanel2);

        btn_salir.setText("Salir");
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(btn_salir))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel1))
                    .addComponent(btn_salir))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jt_usuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_usuariosMouseClicked
        DefaultTableModel modelo = (DefaultTableModel)jt_usuarios.getModel();
        if (evt.getClickCount() == 1) {
            int filaSeleccionada = jt_usuarios.getSelectedRow();
            int id = (int)modelo.getValueAt(filaSeleccionada, 0);
            Usuarios usuario = usuariosAdmin.obtenerUsuario(id);
            if(usuario != null){
                tb_usuario.setText(usuario.getUsuario());
                tb_nombre.setText(usuario.getNombre());
                tb_contrasena.setText(usuario.getContrasena());
                cb_tipoUsuario.setSelectedIndex(((usuario instanceof Doctores) ? 1:0));
                lbl_id.setText(Integer.toString(id));
            }
            habilitarUsuario();
        }
    }//GEN-LAST:event_jt_usuariosMouseClicked

    private void btn_eliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarUsuarioActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro que quieres eliminar?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            DefaultTableModel modelo = (DefaultTableModel)jt_usuarios.getModel();
            int filaSeleccionada = jt_usuarios.getSelectedRow();
            int id = (int)modelo.getValueAt(filaSeleccionada, 0);
            usuariosAdmin.borrarUsuario(id);
            JOptionPane.showMessageDialog(this, "Usuario borrado");
            tb_nombre.setText("");
            cb_tipoUsuario.setSelectedIndex(0);
            tb_contrasena.setText("");
            tb_usuario.setText("");
            lbl_id.setText("");
            cargarTablaUsuarios();
            deshabilitarUsuario();
        } else {
        }
    }//GEN-LAST:event_btn_eliminarUsuarioActionPerformed

    private void btn_guardarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarUsuarioActionPerformed
        int id = Integer.parseInt(lbl_id.getText());
        String nombre = tb_nombre.getText();
        String tipo = cb_tipoUsuario.getSelectedItem().toString();
        String contrasena = tb_contrasena.getText();
        String usuario = tb_usuario.getText();

        if(!nombre.isEmpty() && !tipo.isEmpty() && !contrasena.isEmpty() && !usuario.isEmpty()){
            Usuarios usuarioObj;
            if(tipo.equals("Doctor"))
            usuarioObj = new Doctores(usuario, contrasena, nombre, id);
            else
            usuarioObj = new Administradores(usuario, contrasena, nombre, id);
            if( usuariosAdmin.guardarUsuario(usuarioObj) ){
                JOptionPane.showMessageDialog(this, "Usuario guardado");
                tb_nombre.setText("");
                cb_tipoUsuario.setSelectedIndex(0);
                tb_contrasena.setText("");
                tb_usuario.setText("");
                lbl_id.setText("");
                cargarTablaUsuarios();
                deshabilitarUsuario();
            }else{
                JOptionPane.showMessageDialog(this, "Ha ocurrido un error");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Llene todos los campos antes de guardar un usuario nuevo");
        }
    }//GEN-LAST:event_btn_guardarUsuarioActionPerformed

    private void cb_horaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_horaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_horaActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int index = jl_procedimientoElegido.getSelectedIndex();
        if(index != -1){
            DefaultListModel modelo = (DefaultListModel)jl_procedimientoElegido.getModel();
            modelo.removeElementAt(jl_procedimientoElegido.getSelectedIndex());
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void btn_agregarProcedimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregarProcedimientoActionPerformed
        String procedimiento = (String)JOptionPane.showInputDialog("Ingrese el procedimiento");
        if(!procedimiento.isEmpty()){
            if(procedimientosAdmin.guardarProcedimiento(procedimiento)){
                JOptionPane.showMessageDialog(this,"Procedimiento agregado exitosamente");
                cargarListaProcedimientos();
            }else{
                JOptionPane.showMessageDialog(this,"Ocurrió un error al agregar");
            }
        }
    }//GEN-LAST:event_btn_agregarProcedimientoActionPerformed

    private void cb_doctoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_doctoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_doctoresActionPerformed

    private void btn_quitarProcedimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_quitarProcedimientoActionPerformed
        int index = jl_procedimientoExistente.getSelectedIndex();
        if(index != -1){
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro que quieres eliminar?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                DefaultListModel modelo = (DefaultListModel)jl_procedimientoExistente.getModel();
                Procedimientos procedimiento = (Procedimientos)modelo.getElementAt(index);
                if(procedimientosAdmin.borrarProcedimiento(procedimiento)){
                    JOptionPane.showMessageDialog(this,"Procedimiento borrado");
                    cargarListaProcedimientos();
                }else{
                    JOptionPane.showMessageDialog(this,"Ocurrió un error al eliminar");
                }
            }
        }
    }//GEN-LAST:event_btn_quitarProcedimientoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int index = jl_procedimientoExistente.getSelectedIndex();
        if(index != -1){
            DefaultListModel modelo = (DefaultListModel)jl_procedimientoExistente.getModel();
            Procedimientos procedimiento = (Procedimientos)modelo.getElementAt(index);
            
            DefaultListModel modeloDestino = (DefaultListModel)jl_procedimientoElegido.getModel();
            boolean existe = false;
            for (int i = 0; i < modeloDestino.getSize(); i++) {
                if(procedimiento.getId() == ((Procedimientos)modeloDestino.getElementAt(i)).getId())
                    existe = true;
            }
            if(!existe)
                modeloDestino.addElement(procedimiento);
            else
                JOptionPane.showMessageDialog(this, "El procedimiento ya ha sido seleccionado");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        
        int id = Integer.parseInt(lbl_idCita.getText());
        int indexPaciente = cb_pacientes.getSelectedIndex();
        if(indexPaciente != -1){
            int indexDoctor = cb_doctores.getSelectedIndex();
            if(indexDoctor != -1){
                String hora = (String)cb_hora.getSelectedItem();
                Date fecha = jd_fecha.getDate();
                if(fecha != null){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaStr = dateFormat.format(fecha);
                    DefaultListModel modelo = (DefaultListModel)jl_procedimientoElegido.getModel();
                    if(modelo.getSize() > 0){
                        Pacientes pacienteSeleccionado = (Pacientes)cb_pacientes.getSelectedItem();
                        Doctores doctorSeleccionado = (Doctores)cb_doctores.getSelectedItem();
                        ArrayList<Procedimientos> procedimientosSeleccionados = new ArrayList();
                        for (int i = 0; i < modelo.getSize(); i++) {
                            procedimientosSeleccionados.add((Procedimientos)modelo.getElementAt(i));
                        }
                        Citas cita = new Citas(doctorSeleccionado, pacienteSeleccionado, fechaStr, hora, procedimientosSeleccionados, id);
                        if(citasAdmin.guardarCita(cita)){
                            JOptionPane.showMessageDialog(this, "Cita guardada exitosamente");
                            cargarTablaCitas();
                            deshabilitarCitas();
                            limpiarCita();
                        }else{
                            JOptionPane.showMessageDialog(this, "Ha ocurrido un error");
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, "Seleccione al menos un procedimiento");
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "Seleccione una fecha");
                }
            }else{
                JOptionPane.showMessageDialog(this, "Seleccione un doctor");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Seleccione un paciente");
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void jt_pacientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_pacientesMouseClicked
        DefaultTableModel modelo = (DefaultTableModel)jt_pacientes.getModel();
        if (evt.getClickCount() == 1) {
            int filaSeleccionada = jt_pacientes.getSelectedRow();
            int id = (int)modelo.getValueAt(filaSeleccionada, 0);
            Pacientes paciente = pacientesAdmin.obtenerPaciente(id);
            if(paciente != null){
                tb_pacienteAdmin.setText(paciente.getPaciente());
                tb_correoAdmin.setText(paciente.getCorreo());
                tb_celularAdmin.setText(paciente.getTelefono());
                lbl_idPaciente.setText(Integer.toString(id));
            }
            habilitarPaciente();
        }
    }//GEN-LAST:event_jt_pacientesMouseClicked

    private void btn_guardarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarPacienteActionPerformed
        int id = Integer.parseInt(lbl_idPaciente.getText());
        String nombre = tb_pacienteAdmin.getText();
        String correo = tb_correoAdmin.getText();
        String celular = tb_celularAdmin.getText();
        if(!nombre.isEmpty() && !celular.isEmpty()){
            if(correo.isEmpty()) correo = "";
            Pacientes paciente = new Pacientes(nombre, correo, celular, id);
            if(pacientesAdmin.guardarPaciente(paciente)){
                JOptionPane.showMessageDialog(this, "Paciente guardado exitosamente");    
                tb_pacienteAdmin.setText("");
                tb_correoAdmin.setText("");
                tb_celularAdmin.setText("");
                deshabilitarPaciente();
            }else{
                JOptionPane.showMessageDialog(this, "Ha ocurrido un error");    
            }
            cargarTablaPacientes();
        }else{
            JOptionPane.showMessageDialog(this, "Llene la información necesaria");
        }
    }//GEN-LAST:event_btn_guardarPacienteActionPerformed

    private void btn_eliminarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarPacienteActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro que quieres eliminar?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            DefaultTableModel modelo = (DefaultTableModel)jt_pacientes.getModel();
            int filaSeleccionada = jt_pacientes.getSelectedRow();
            int id = (int)modelo.getValueAt(filaSeleccionada, 0);
            if(pacientesAdmin.borrarPaciente(id)){
                JOptionPane.showMessageDialog(this, "Paciente borrado exitosamente");
                tb_pacienteAdmin.setText("");
                tb_correoAdmin.setText("");
                tb_celularAdmin.setText("");
                deshabilitarPaciente();
            }else{
                JOptionPane.showMessageDialog(this, "Ha ocurrido un error");
            }
            cargarTablaPacientes();
        } else {
        }
    }//GEN-LAST:event_btn_eliminarPacienteActionPerformed

    private void cb_pacientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_pacientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_pacientesActionPerformed

    private void cb_pacientesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_pacientesItemStateChanged
        if(cb_pacientes.getSelectedIndex() != -1){
            Pacientes paciente = (Pacientes)cb_pacientes.getSelectedItem();
            tb_correo.setText(paciente.getCorreo());
            tb_celular.setText(paciente.getTelefono());
        }
    }//GEN-LAST:event_cb_pacientesItemStateChanged

    private void cb_tipoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_tipoUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_tipoUsuarioActionPerformed

    private void btn_nuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoUsuarioActionPerformed
        habilitarUsuario();
        tb_nombre.setText("");
        cb_tipoUsuario.setSelectedIndex(0);
        tb_contrasena.setText("");
        tb_usuario.setText("");
        lbl_id.setText("-1");
    }//GEN-LAST:event_btn_nuevoUsuarioActionPerformed

    private void btn_nuevoPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoPacienteActionPerformed
        habilitarPaciente();
        tb_pacienteAdmin.setText("");
        tb_celularAdmin.setText("");
        tb_correoAdmin.setText("");
        lbl_idPaciente.setText("-1");
    }//GEN-LAST:event_btn_nuevoPacienteActionPerformed

    private void btn_nuevaCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevaCitaActionPerformed
        habilitarCitas();
        limpiarCita();
    }//GEN-LAST:event_btn_nuevaCitaActionPerformed

    private void limpiarCita(){
        jd_fecha.setDate(null);
        cb_hora.setSelectedIndex(0);
        jl_procedimientoElegido.removeAll();
        cargarListaProcedimientos();
        lbl_idCita.setText("-1");
        DefaultListModel modeloProcedimientos = (DefaultListModel)jl_procedimientoElegido.getModel();
        modeloProcedimientos.removeAllElements();
        DefaultComboBoxModel modelo = (DefaultComboBoxModel)cb_pacientes.getModel();
        if(modelo.getSize() > 0)
            cb_pacientes.setSelectedIndex(0);
        DefaultComboBoxModel modeloDr = (DefaultComboBoxModel)cb_doctores.getModel();
        if(modeloDr.getSize() > 0)
            cb_doctores.setSelectedIndex(0);
    }
    
    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro que quieres eliminar?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            DefaultTableModel modelo = (DefaultTableModel)jt_citas.getModel();
            int row = jt_citas.getSelectedRow();
            Citas cita = (Citas)modelo.getValueAt(row,0);
            if(citasAdmin.borrarCita(cita.getId())){
                JOptionPane.showMessageDialog(this, "Borrado exitosamente");
                cargarTablaCitas();
                deshabilitarCitas();
                limpiarCita();
            }else{
                JOptionPane.showMessageDialog(this, "Ha ocurrido un error");
            }
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void jt_citasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_citasMouseClicked
        DefaultTableModel modelo = (DefaultTableModel)jt_citas.getModel();
        habilitarCitas();
        if (evt.getClickCount() == 1) {
            
            int row = jt_citas.getSelectedRow();
            Citas cita = (Citas)modelo.getValueAt(row,0);
            Pacientes paciente = (Pacientes)modelo.getValueAt(row, 1);
            Doctores doctor = (Doctores)modelo.getValueAt(row,2);
            tb_correo.setText(paciente.getCorreo());
            tb_celular.setText(paciente.getTelefono());
            
            DefaultComboBoxModel modeloPacientes = (DefaultComboBoxModel)cb_pacientes.getModel();
            for (int i = 0; i < modeloPacientes.getSize(); i++) {
                if(((Pacientes)modeloPacientes.getElementAt(i)).getId() == paciente.getId()){
                    cb_pacientes.setSelectedIndex(i);
                    break;
                }
            }
            
            DefaultComboBoxModel modeloDoctores = (DefaultComboBoxModel)cb_doctores.getModel();
            for (int i = 0; i < modeloDoctores.getSize(); i++) {
                if(((Doctores)modeloDoctores.getElementAt(i)).getId() == doctor.getId()){
                    cb_doctores.setSelectedIndex(i);
                    break;
                }
            }
            
            lbl_idCita.setText(Integer.toString(cita.getId()));
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = null;
            try {
                fecha = df.parse(cita.getFecha());
            } catch (ParseException ex) {
                Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
            }
            jd_fecha.setDate(fecha);
            
            cb_hora.setSelectedItem(cita.getHora());
            
            
            DefaultListModel modeloProcedimientos = (DefaultListModel)jl_procedimientoElegido.getModel();
            modeloProcedimientos.removeAllElements();
            for (Procedimientos p : cita.getProcedimiento()) {
                modeloProcedimientos.addElement(p);
            }
        }
    }//GEN-LAST:event_jt_citasMouseClicked

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./log.sj"));
            oos.writeObject(new Usuarios());
            Login l = new Login();
            l.setVisible(true);
            this.setVisible(false);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_btn_salirActionPerformed

    private void jt_citasPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_citasPrincipalMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_citasPrincipalMouseClicked

    private void btn_realizadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_realizadoActionPerformed
        if(jt_citasPrincipal.getSelectedRow() != -1){
           DefaultTableModel modelo = (DefaultTableModel)jt_citasPrincipal.getModel();
           int row = jt_citasPrincipal.getSelectedRow();
           Citas cita = (Citas)modelo.getValueAt(row, 0);
           cita.setEstado("Realizado");
           citasAdmin.guardarCita(cita);
           cb_realizado.setSelected(false);
           cargarTablaCitas();
           JOptionPane.showMessageDialog(this, "Cita completada");
        }
    }//GEN-LAST:event_btn_realizadoActionPerformed

    private void btn_realizado1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_realizado1ActionPerformed
        if(jt_citasPrincipal.getSelectedRow() != -1){
           DefaultTableModel modelo = (DefaultTableModel)jt_citasPrincipal.getModel();
           int row = jt_citasPrincipal.getSelectedRow();
           Citas cita = (Citas)modelo.getValueAt(row, 0);
           cita.setEstado("No Realizado");
           citasAdmin.guardarCita(cita);
           cb_realizado.setSelected(false);
           cargarTablaCitas();
           JOptionPane.showMessageDialog(this, "Cita editada");
        }
    }//GEN-LAST:event_btn_realizado1ActionPerformed

    private void btn_canceladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_canceladoActionPerformed
        if(jt_citasPrincipal.getSelectedRow() != -1){
           DefaultTableModel modelo = (DefaultTableModel)jt_citasPrincipal.getModel();
           int row = jt_citasPrincipal.getSelectedRow();
           Citas cita = (Citas)modelo.getValueAt(row, 0);
           cita.setEstado("Cancelado");
           citasAdmin.guardarCita(cita);
           cb_realizado.setSelected(false);
           cargarTablaCitas();
           JOptionPane.showMessageDialog(this, "Cita editada");
        }
    }//GEN-LAST:event_btn_canceladoActionPerformed

    private void cb_realizadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_realizadoItemStateChanged
        if (cb_realizado.isSelected()) {
            cargarTablaPrincipal(" WHERE ESTADO = 'Realizado'");
        } else {
            cargarTablaPrincipal(" WHERE ESTADO = 'No Realizado' OR ESTADO = 'Cancelado'");
        }
    }//GEN-LAST:event_cb_realizadoItemStateChanged

    private void btn_enviarRecordatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_enviarRecordatorioActionPerformed
        if(jt_citasPrincipal.getSelectedRow() != -1){
            DefaultTableModel modelo = (DefaultTableModel)jt_citasPrincipal.getModel();
           int row = jt_citasPrincipal.getSelectedRow();
           Citas cita = (Citas)modelo.getValueAt(row, 0);
           if(cita.getEstado().equals("Realizado")){
               JOptionPane.showMessageDialog(this, "Este paciente ya fue atendido");
           }else if(cita.getEstado().equals("Cancelado")){
               JOptionPane.showMessageDialog(this, "Esta cita está cancelada. Realice la reprogramación manualmente");
           }else{
                try{
                    String url = "http://localhost:3000/enviar-mensaje";
                    JSONObject cuerpo = new JSONObject();
                    cuerpo.put("to", cita.getPaciente().getTelefono());
                    cuerpo.put("message", "Buenos días! Le escribimos de soluciones dentales para recordarle que tiene una cita programada para el " + cita.getFecha() + " a las " + cita.getHora() + ". Muchas gracias!");

                    String cuerpoStr = cuerpo.toString();

                    URL apiUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(cuerpoStr.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    int responseCode = connection.getResponseCode();
                    String responseMessage = connection.getResponseMessage();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        JOptionPane.showMessageDialog(null, "El mensaje se envió exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo enviar el mensaje.");
                    }

                    connection.disconnect();
                    
                }catch(Exception ex){
                   ex.printStackTrace();
                }
           }
        }
    }//GEN-LAST:event_btn_enviarRecordatorioActionPerformed

    private void habilitarUsuario(){
        tb_nombre.setEnabled(true);
        cb_tipoUsuario.setEnabled(true);
        tb_contrasena.setEnabled(true);
        tb_usuario.setEnabled(true);
    }
    
    private void deshabilitarUsuario(){
        tb_nombre.setEnabled(false);
        cb_tipoUsuario.setEnabled(false);
        tb_contrasena.setEnabled(false);
        tb_usuario.setEnabled(false);
    }
    
    private void habilitarPaciente(){
        tb_pacienteAdmin.setEnabled(true);
        tb_correoAdmin.setEnabled(true);
        tb_celularAdmin.setEnabled(true);
    }
    
    private void deshabilitarPaciente(){
        tb_pacienteAdmin.setEnabled(false);
        tb_correoAdmin.setEnabled(false);
        tb_celularAdmin.setEnabled(false);
    }
    
    private void habilitarCitas(){
        cb_pacientes.setEnabled(true);
        cb_doctores.setEnabled(true);
        jd_fecha.setEnabled(true);
        cb_hora.setEnabled(true);
        jl_procedimientoExistente.setEnabled(true);
        jButton3.setEnabled(true);
        jButton4.setEnabled(true);
        jl_procedimientoElegido.setEnabled(true);
        btn_agregarProcedimiento.setEnabled(true);
        btn_quitarProcedimiento.setEnabled(true);
    }
    
    private void deshabilitarCitas(){
        cb_pacientes.setEnabled(false);
        cb_doctores.setEnabled(false);
        jd_fecha.setEnabled(false);
        cb_hora.setEnabled(false);
        jl_procedimientoExistente.setEnabled(false);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
        jl_procedimientoElegido.setEnabled(false);
        btn_agregarProcedimiento.setEnabled(false);
        btn_quitarProcedimiento.setEnabled(false);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Administrador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_agregarProcedimiento;
    private javax.swing.JButton btn_cancelado;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_eliminarPaciente;
    private javax.swing.JButton btn_eliminarUsuario;
    private javax.swing.JButton btn_enviarRecordatorio;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_guardarPaciente;
    private javax.swing.JButton btn_guardarUsuario;
    private javax.swing.JButton btn_nuevaCita;
    private javax.swing.JButton btn_nuevoPaciente;
    private javax.swing.JButton btn_nuevoUsuario;
    private javax.swing.JButton btn_quitarProcedimiento;
    private javax.swing.JButton btn_realizado;
    private javax.swing.JButton btn_realizado1;
    private javax.swing.JButton btn_reporte;
    private javax.swing.JButton btn_salir;
    private javax.swing.JComboBox<String> cb_doctores;
    private javax.swing.JComboBox<String> cb_hora;
    private javax.swing.JComboBox<String> cb_pacientes;
    private javax.swing.JCheckBox cb_realizado;
    private javax.swing.JComboBox<String> cb_tipoUsuario;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JDateChooser jd_fecha;
    private javax.swing.JList<String> jl_procedimientoElegido;
    private javax.swing.JList<String> jl_procedimientoExistente;
    private javax.swing.JTable jt_citas;
    private javax.swing.JTable jt_citasPrincipal;
    private javax.swing.JTable jt_pacientes;
    private javax.swing.JTable jt_usuarios;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_idCita;
    private javax.swing.JLabel lbl_idPaciente;
    private javax.swing.JTextField tb_celular;
    private javax.swing.JTextField tb_celularAdmin;
    private javax.swing.JTextField tb_contrasena;
    private javax.swing.JTextField tb_correo;
    private javax.swing.JTextField tb_correoAdmin;
    private javax.swing.JTextField tb_nombre;
    private javax.swing.JTextField tb_pacienteAdmin;
    private javax.swing.JTextField tb_usuario;
    // End of variables declaration//GEN-END:variables
}
