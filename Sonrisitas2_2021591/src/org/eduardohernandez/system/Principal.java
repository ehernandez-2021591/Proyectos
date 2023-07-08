/*
Nombre:Eduardo Andre Hernandez Carranza
Codigo tecnico: IN5AM
Carne:2021591
Fecha de Creacion:05-04-2022
Modificaciones:
Nota: La receta completa se saca de Recetas al tener una seleccion.

 */
package org.eduardohernandez.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eduardohernandez.controller.CitasController;
import org.eduardohernandez.controller.DetalleRecetaController;
import org.eduardohernandez.controller.DoctorController;
import org.eduardohernandez.controller.EspecialidadController;
import org.eduardohernandez.controller.LoginController;
import org.eduardohernandez.controller.MedicamentosController;
import org.eduardohernandez.controller.MenuPrincipalController;
import org.eduardohernandez.controller.PacienteController;
import org.eduardohernandez.controller.ProgramadorController;
import org.eduardohernandez.controller.RecetasController;
import org.eduardohernandez.controller.UsuarioController;



public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/eduardohernandez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Sonrisitas");
        escenarioPrincipal.getIcons().add(new Image("/org/eduardohernandez/image/Diente.png"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/eduardohernandez/view/MenuPrincipalView.fxml"));
//        Scene escena = new Scene(root);
//        escenarioPrincipal.setScene(escena);
          ventanaLogin();
            escenarioPrincipal.show();
    }
    public void ventanaLogin(){
        try{
            LoginController Login =(LoginController)cambiarEscena("LoginView.fxml",675,400);
                Login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();}
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController ventanaUsuario=(UsuarioController)cambiarEscena("UsuarioView.fxml",977,480);
                ventanaUsuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 600, 400);
            ventanaMenu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaProgramador(){
        try{
            ProgramadorController ventanaProgramador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 643, 433);
            ventanaProgramador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPacientes(){
        try{
            PacienteController ventanaPaciente = (PacienteController)cambiarEscena("PacientesView.fxml",977,480 );
            ventanaPaciente.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaMedicamentos(){
        try{
            MedicamentosController ventanaMedicamento = (MedicamentosController)cambiarEscena("MedicamentosView.fxml",977,480 );
            ventanaMedicamento.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaEspecialidad(){
        try{
            EspecialidadController ventanaEspecialidades = (EspecialidadController)cambiarEscena("EspecialidadesView.fxml",977,480 );
            ventanaEspecialidades.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDoctor(){
        try{
            DoctorController ventanaDoctor = (DoctorController)cambiarEscena("DoctoresView.fxml",977,480 );
            ventanaDoctor.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaRecetas(){
        try{
            RecetasController ventanaRecetas = (RecetasController)cambiarEscena("RecetasView.fxml",977,480 );
            ventanaRecetas.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaDetalleRecetas(){
        try{
            DetalleRecetaController ventanaDetalleRecetas = (DetalleRecetaController)cambiarEscena("DetalleRecetasView.fxml",977,480 );
            ventanaDetalleRecetas.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     public void ventanaCitas(){
        try{
            CitasController ventanaCitas = (CitasController)cambiarEscena("CitasView.fxml",977,480 );
            ventanaCitas.setEscenarioPrincipal (this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
   
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto)throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }

}
