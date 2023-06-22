/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

import Administradores.UsuariosAdmin;
import Modelos.Administradores;
import Modelos.Doctores;
import Modelos.Usuarios;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 *
 * @author James Josue Molina
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    private ArrayList<Usuarios> usuarios = new ArrayList();
    private UsuariosAdmin usuariosAdmin = new UsuariosAdmin();
    public Login() {
        this.setVisible(true);
        initComponents();
        this.setLocationRelativeTo(null);
        usuarios = usuariosAdmin.obtenerUsuarios();
        usuarios.add(new Administradores("admin","admin", "Administrador"));
        
//        try {
//            Usuarios usuarioTemp = null;
//            ObjectInputStream oos = new ObjectInputStream(new FileInputStream("./log.sj"));        
//            usuarioTemp = (Usuarios)oos.readObject();
//            if(usuarioTemp != null){
//                if(usuarioTemp instanceof Doctores){
//                    Doctor doctor = new Doctor();
//                    doctor.setUsuario(usuarioTemp);
//                    doctor.setVisible(true);
//                    this.setVisible(false);
//                }else if(usuarioTemp instanceof Administradores){
//                    Administrador administrador = new Administrador();
//                    administrador.setUsuario(usuarioTemp);
//                    administrador.setVisible(true);
//                    this.setVisible(false);
//                }
//            }
//       } catch (Exception ex) {
//       }
                    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tb_usuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tb_contrasena = new javax.swing.JPasswordField();
        btn_ingresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Soluciones Dentales");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Soluciones Dentales");

        jLabel2.setText("Usuario");

        jLabel3.setText("Contraseña");

        btn_ingresar.setText("Ingresar");
        btn_ingresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ingresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(tb_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                            .addComponent(jLabel3)
                            .addComponent(tb_contrasena)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(btn_ingresar, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tb_contrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_ingresar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ingresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ingresarActionPerformed
        String usuarioStr = tb_usuario.getText();
        String contrasenaStr = String.valueOf(tb_contrasena.getPassword());
        for (Usuarios usuario : usuarios) {
            if(usuario.getUsuario().equals(usuarioStr) && usuario.getContrasena().equals(contrasenaStr)){
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./log.sj"));
                    oos.writeObject(usuario);
                } catch (Exception ex) {
                    
                }
                if(usuario instanceof Doctores){
                    Doctor doctor = new Doctor();
                    doctor.setUsuario(usuario);
                    doctor.setVisible(true);
                    this.setVisible(false);
                }else if(usuario instanceof Administradores){
                    Administrador administrador = new Administrador();
                    administrador.setUsuario(usuario);
                    administrador.setVisible(true);
                    this.setVisible(false);
                }
                return;
            }
        }
    }//GEN-LAST:event_btn_ingresarActionPerformed

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_ingresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField tb_contrasena;
    private javax.swing.JTextField tb_usuario;
    // End of variables declaration//GEN-END:variables
}
