package org.eduardohernandez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.eduardohernandez.system.Principal;
import org.eduardohernandez.bean.Vecino;
import org.eduardohernandez.bean.Vehiculo;
import org.eduardohernandez.db.Conexion;


public class VehiculosController implements Initializable {
        private Principal escenarioPrincipal;
        private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
        private operaciones tipoDeOperacion=operaciones.NINGUNO;
        private ObservableList listaVehiculos;
        private ObservableList listaVecinos;
        @FXML private TextField txtPlaca;
        @FXML private TextField txtMarca;
        @FXML private TextField txtModelo;
        @FXML private TextField txtTipoVehiculo;
        @FXML private ComboBox cmbNIT;
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnActualizar;
        @FXML private TableColumn colPlaca;
        @FXML private TableColumn colMarca;
        @FXML private TableColumn colModelo;
        @FXML private TableColumn colNIT;
        @FXML private TableColumn colTipoVehiculo;
        @FXML private TableView tblVehiculos;
    
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbNIT.setItems(getVecino());
        cmbNIT.setDisable(true);
        desactivarControles();
    }
    public void cargarDatos(){
       tblVehiculos.setItems(getVehiculo());
       colPlaca.setCellValueFactory(new PropertyValueFactory<Vehiculo,String>("placa"));
       colMarca.setCellValueFactory(new PropertyValueFactory<Vehiculo,String>("marca"));
       colModelo.setCellValueFactory(new PropertyValueFactory<Vehiculo,String>("modelo"));
       colNIT.setCellValueFactory(new PropertyValueFactory<Vehiculo,String>("Vecinos_NIT"));
       colTipoVehiculo.setCellValueFactory(new PropertyValueFactory<Vehiculo,String>("tipoDeVehiculo"));
    }
     public void seleccionarElemento(){
        if(tblVehiculos.getSelectionModel().getSelectedItem()!=null){
            txtPlaca.setText(String.valueOf(((Vehiculo)tblVehiculos.getSelectionModel().getSelectedItem()).getPlaca()));
            txtMarca.setText(String.valueOf(((Vehiculo)tblVehiculos.getSelectionModel().getSelectedItem()).getMarca()));
            txtModelo.setText(String.valueOf(((Vehiculo)tblVehiculos.getSelectionModel().getSelectedItem()).getModelo()));
            txtTipoVehiculo.setText(((Vehiculo)tblVehiculos.getSelectionModel().getSelectedItem()).getTipoDeVehiculo());
            cmbNIT.getSelectionModel().select(buscarVecino(((Vehiculo)tblVehiculos.getSelectionModel().getSelectedItem()).getVecinos_NIT()));
            cmbNIT.setDisable(false);
        }else{
            JOptionPane.showMessageDialog(null, "No hay un elemento para seleccionar");
        }
     }
    
     public void nuevo(){
                switch (tipoDeOperacion){
                case NINGUNO:
                    limpiarControles();                  
                    activarControles();
                    btnAgregar.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnActualizar.setDisable(true);
                    btnReporte.setDisable(true);
                    cmbNIT.setDisable(false);
                    tipoDeOperacion=operaciones.GUARDAR;
                    break;
                case GUARDAR:
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnAgregar.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnActualizar.setDisable(false);
                    btnReporte.setDisable(false);
                    tipoDeOperacion=operaciones.NINGUNO;
                    cargarDatos();
                        break;
                }
            }
    
            public void guardar(){
                Vehiculo registro=new Vehiculo();
                registro.setMarca(txtMarca.getText());
                registro.setModelo(txtModelo.getText());
                registro.setPlaca(txtPlaca.getText());
                registro.setTipoDeVehiculo(txtTipoVehiculo.getText());
                registro.setVecinos_NIT(((Vecino)cmbNIT.getSelectionModel().getSelectedItem()).getNIT());
                    try{
                        PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarVehiculo(?,?,?,?,?)}");
                                procedimiento.setString(1,registro.getPlaca());
                                procedimiento.setString(2, registro.getMarca());
                                procedimiento.setString(3, registro.getModelo());
                                procedimiento.setString(4, registro.getTipoDeVehiculo());
                                procedimiento.setString(5, registro.getVecinos_NIT());
                                procedimiento.execute();
                                listaVehiculos.add(registro);
                                }catch(Exception e){
                        e.printStackTrace();
                    }
                
            }
             public void eliminar(){
                switch(tipoDeOperacion){
                    case GUARDAR:
                        desactivarControles();
                        limpiarControles();
                        btnAgregar.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnActualizar.setDisable(false);
                        btnReporte.setDisable(false);
                        tipoDeOperacion=operaciones.NINGUNO;
                        cmbNIT.setDisable(true);
                        break;
                    default:
                        if(tblVehiculos.getSelectionModel().getSelectedItem()!=null){
                            int respuesta=JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                            if(respuesta==JOptionPane.YES_OPTION){
                                try{
                                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarVehiculo(?)}");
                                    procedimiento.setString(1,((Vehiculo)tblVehiculos.getSelectionModel().getSelectedItem()).getPlaca());
                                    procedimiento.execute();
                                    listaVehiculos.remove(tblVehiculos.getSelectionModel().getSelectedIndex());
                                    limpiarControles();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }if(respuesta==JOptionPane.NO_OPTION){
                            limpiarControles();
                            desactivarControles();
                            cmbNIT.setDisable(true);}
                        }
                        else{
                        JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
                    }   break;
                
                    
            } 
             }
             public void editar(){
             switch(tipoDeOperacion){
                 case NINGUNO:
                     if(tblVehiculos.getSelectionModel().getSelectedItem()!=null){ 
                     activarControles();
                     btnAgregar.setDisable(true);
                     btnEliminar.setDisable(true);
                     btnActualizar.setText("Guardar");
                     btnReporte.setText("Cancelar");
                     cmbNIT.setDisable(true);
                     txtPlaca.setDisable(true);
                     tipoDeOperacion=operaciones.ACTUALIZAR;}
                     else{
                         JOptionPane.showMessageDialog(null,"Debe Seleccionar un Elemento");
                     }
                     break;
                 case ACTUALIZAR:
                   actualizar();
                   btnActualizar.setText("Actualizar");
                   btnReporte.setText("Reporte");
                   btnAgregar.setDisable(false);
                   btnEliminar.setDisable(false);
                   desactivarControles();
                   limpiarControles();
                   cargarDatos(); 
                   cmbNIT.setDisable(true);
                   tipoDeOperacion=operaciones.NINGUNO;
                              break;
             }
             }
             
             public void reporte(){
                 limpiarControles();
                 activarControles();
                 btnEliminar.setDisable(false);
                 btnAgregar.setDisable(false);
                 btnActualizar.setText("Actualizar");
                 btnReporte.setText("Reporte");
                 cmbNIT.setDisable(true);
                 tipoDeOperacion=operaciones.NINGUNO;
             }
             
             public void actualizar(){
             try{
                PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarVehiculo(?,?,?,?)}");
                Vehiculo registro=(Vehiculo)tblVehiculos.getSelectionModel().getSelectedItem();
                registro.setMarca(txtMarca.getText());
                registro.setModelo(txtModelo.getText());
                registro.setTipoDeVehiculo(txtTipoVehiculo.getText());
                procedimiento.setString(1,registro.getPlaca());
                procedimiento.setString(2,registro.getMarca());
                procedimiento.setString(3,registro.getModelo());
                procedimiento.setString(4,registro.getTipoDeVehiculo());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
             }
            
 public Vecino buscarVecino(String NIT){
                Vecino resultado =null;
                try{
                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarVecino(?)}");
                            procedimiento.setString(1,NIT);
                            ResultSet registro = procedimiento.executeQuery();
                            while(registro.next()){
                                resultado=new Vecino(registro.getString("NIT"),
                                                registro.getString("DPI"),
                                                registro.getString("nombres"),
                                                registro.getString("apellidos"),
                                                registro.getString("municipalidad"),
                                                registro.getString("direccion"),
                                                registro.getInt("codigoPostal"),
                                                registro.getString("telefono")
                                );
                            }
                            }catch(Exception e){
                    e.printStackTrace();
                }
            return resultado;
}
       
    
 public ObservableList<Vehiculo>getVehiculo(){
        ArrayList <Vehiculo>lista=new ArrayList<Vehiculo>();
        try{
        PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarVehiculos()}");
                ResultSet resultado=procedimiento.executeQuery();
                while(resultado.next()){
                lista.add(new Vehiculo(resultado.getString("placa"),
                        resultado.getString("marca"),  
                        resultado.getString("modelo"),              
                        resultado.getString("tipoDeVehiculo"),                            
                        resultado.getString("Vecinos_NIT")));              
                      
                } 
        }catch(Exception e){
            e.printStackTrace();
            }
         return listaVehiculos = FXCollections.observableArrayList(lista);
       }

      public ObservableList<Vecino>getVecino(){
        ArrayList <Vecino>lista=new ArrayList<Vecino>();
        try{
        PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarVecinos()}");
                ResultSet resultado=procedimiento.executeQuery();
                while(resultado.next()){
                lista.add(new Vecino(resultado.getString("NIT"),
                        resultado.getString("DPI"),  
                        resultado.getString("nombres"),              
                        resultado.getString("apellidos"),              
                        resultado.getString("direccion"),              
                        resultado.getString("municipalidad"),              
                        resultado.getInt("codigoPostal"),              
                        resultado.getString("telefono")));              
                      
                } 
        }catch(Exception e){
            e.printStackTrace();
            }
         return listaVecinos = FXCollections.observableArrayList(lista);
       }
    
    public void desactivarControles(){
        cmbNIT.setDisable(true);
        txtMarca.setEditable(true);
        txtModelo.setEditable(true);
        txtPlaca.setEditable(true);
        txtTipoVehiculo.setEditable(true);
    }
     public void activarControles(){
        cmbNIT.setDisable(false);
        txtMarca.setDisable(false);
        txtModelo.setDisable(false);
        txtPlaca.setDisable(false);
        txtTipoVehiculo.setDisable(false);
    }
     public void limpiarControles(){
        cmbNIT.setValue(null);
        txtMarca.clear();
        txtModelo.clear();
        txtPlaca.clear();
        txtTipoVehiculo.clear();
        tblVehiculos.getSelectionModel().clearSelection();
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
     public void ventanaMenu(){
        escenarioPrincipal.ventanaMenu();
    }
    
}
