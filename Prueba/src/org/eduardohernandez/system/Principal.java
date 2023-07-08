/*
Nombre:Eduardo Andre Hernandez Carranza
Carnet:2021591
Codigo Tecnico: IN5AM 
Fecha de Creacion:26/09/2022
Fecha de Modificacion:27/09/2022
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
import org.eduardohernandez.controller.MenuPrincipalController;
import org.eduardohernandez.controller.VecinosController;
import org.eduardohernandez.controller.VehiculosController;

public class Principal extends Application {
        private final String PAQUETE_VISTA ="/org/eduardohernandez/view/";
        private Stage escenarioPrincipal;
        private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal)throws Exception {
        this.escenarioPrincipal=escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Emetra");
        escenarioPrincipal.getIcons().add(new Image("/org/eduardohernandez/image/logo.png"));
        ventanaMenu();
        escenarioPrincipal.show();
    }
    
    public void ventanaMenu(){
        try{
        MenuPrincipalController menu=(MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",910,497);  
        menu.setEscenarioPrincipal(this);
      }catch(Exception e){
        e.printStackTrace();
         }
    }
    public void ventanaVecinos(){
        try{
            VecinosController vecinos=(VecinosController)cambiarEscena("VecinosView.fxml",1277,537);
            vecinos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaVehiculos(){
        try{
            VehiculosController vehiculos=(VehiculosController)cambiarEscena("VehiculoView.fxml",1277,537);
            vehiculos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   
    public static void main(String[] args) {
        launch(args);
    }
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();        
        return resultado;
    }
}