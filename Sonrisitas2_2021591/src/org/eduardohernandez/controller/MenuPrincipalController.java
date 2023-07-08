package org.eduardohernandez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.eduardohernandez.system.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
   }
    public void ventanaMedicamentos(){
        escenarioPrincipal.ventanaMedicamentos();
   }
    public void ventanaEspecialidad(){
        escenarioPrincipal.ventanaEspecialidad();
    }
    public void ventanaDoctor(){
        escenarioPrincipal.ventanaDoctor();
    }    
    public void ventanaRecetas(){
        escenarioPrincipal.ventanaRecetas();
    }
    public void ventanaDetalleRecetas(){
        escenarioPrincipal.ventanaDetalleRecetas();
    }
    public void ventanaCitas(){
        escenarioPrincipal.ventanaCitas();
    }
        public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
        public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    }