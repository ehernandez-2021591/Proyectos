
package org.eduardohernandez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.eduardohernandez.bean.Usuario;
import org.eduardohernandez.db.Conexion;
import org.eduardohernandez.system.Principal;


public class UsuarioController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO,GUARDAR};
    private operaciones tipoDeOperacion=operaciones.NINGUNO;
    @FXML TextField txtCodigoUsuario;
    @FXML TextField txtnombreUsuario;
    @FXML TextField txtApellidoUsuario;
    @FXML TextField txtUsuario;
    @FXML TextField txtPassword;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView ImgNuevo;
    @FXML private ImageView ImgEliminar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEliminar.setDisable(true);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
        public void nuevo(){
            switch (tipoDeOperacion){
                case NINGUNO:
                    limpiarControles();
                    activarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    ImgNuevo.setImage(new Image("/org/eduardohernandez/image/Guardar.png"));
                     ImgEliminar.setImage(new Image("/org/eduardohernandez/image/Cancelar.png"));
                    tipoDeOperacion=operaciones.GUARDAR;
                    break;
                case GUARDAR:
                    if("".equals(txtUsuario.getText())|"".equals(txtPassword.getText())|"".equals(txtnombreUsuario.getText())|"".equals(txtApellidoUsuario.getText()))
                        JOptionPane.showMessageDialog(null,"Campos incompletos");
                    else{
                    guardar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    ImgNuevo.setImage(new Image("/org/eduardohernandez/image/Nuevo.png"));
                    ImgEliminar.setImage(new Image("/org/eduardohernandez/image/Eliminar.png"));
                    tipoDeOperacion=operaciones.NINGUNO;
                    }break;
            }
        }
        public void eliminar(){
                switch(tipoDeOperacion){
                    case GUARDAR:
                        btnEliminar.setDisable(true);
                        desactivarControles();
                        limpiarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        ImgNuevo.setImage(new Image("/org/eduardohernandez/image/Nuevo.png"));
                        ImgEliminar.setImage(new Image("/org/eduardohernandez/image/Eliminar.png"));
                        tipoDeOperacion=operaciones.NINGUNO;
                        break;}
        
        }
    public void guardar(){
        Usuario registro=new Usuario();
        registro.setNombreUsuario(txtnombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtPassword.getText());
        try{
                        PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
                procedimiento.setString(1, registro.getNombreUsuario());
                procedimiento.setString(2,registro.getApellidoUsuario());
                procedimiento.setString(3,registro.getUsuarioLogin());
                procedimiento.setString(4, registro.getContrasena());
                procedimiento.execute();
                JOptionPane.showMessageDialog(null, "Usuario Creado Con exito");
                ventanaLogin();
        }catch(Exception e){
            e.printStackTrace();
        }
        }
    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtnombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
    }
    public void activarControles(){
        txtCodigoUsuario.setEditable(false);
        txtnombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);   
        btnEliminar.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoUsuario.clear();
        txtnombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();  
    }
      public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
}
