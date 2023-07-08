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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.eduardohernandez.system.Principal;
import org.eduardohernandez.bean.Vecino;
import org.eduardohernandez.db.Conexion;


public class VecinosController implements Initializable {
    private Principal escenarioPrincipal;
    private ObservableList listaVecinos;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion=operaciones.NINGUNO;
    @FXML private TextField txtDPI;
    @FXML private TextField txtNIT;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCodigoPostal;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtMunicipalidad;
    @FXML private TextField txtTelefono;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private Button btnActualizar;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colNIT;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colCodigoPostal;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colMunicipalidad;
    @FXML private TableColumn colTelefono;
    @FXML private TableView tblVecinos;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblVecinos.setItems(getVecino());
        colDPI.setCellValueFactory(new PropertyValueFactory<Vecino,String>("DPI"));
        colNIT.setCellValueFactory(new PropertyValueFactory<Vecino, String>("NIT"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Vecino,String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Vecino,String>("apellidos"));
        colCodigoPostal.setCellValueFactory(new PropertyValueFactory<Vecino,Integer>("codigoPostal"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Vecino,String>("direccion"));
        colMunicipalidad.setCellValueFactory(new PropertyValueFactory<Vecino,String>("municipalidad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Vecino,String>("telefono"));
    }
    public void seleccionarElemento(){
        if(tblVecinos.getSelectionModel().getSelectedItem()!=null){
            txtDPI.setText(String.valueOf(((Vecino)tblVecinos.getSelectionModel().getSelectedItem()).getDPI()));
            txtNIT.setText(String.valueOf(((Vecino)tblVecinos.getSelectionModel().getSelectedItem()).getNIT()));
            txtNombres.setText(String.valueOf(((Vecino)tblVecinos.getSelectionModel().getSelectedItem()).getNombres()));
            txtApellidos.setText(String.valueOf(((Vecino)tblVecinos.getSelectionModel().getSelectedItem()).getApellidos()));
            txtCodigoPostal.setText(String.valueOf(((Vecino)tblVecinos.getSelectionModel().getSelectedItem()).getCodigoPostal()));
            txtDireccion.setText(String.valueOf(((Vecino)tblVecinos.getSelectionModel().getSelectedItem()).getDireccion()));
            txtMunicipalidad.setText(String.valueOf(((Vecino)tblVecinos.getSelectionModel().getSelectedItem()).getMunicipalidad()));
            txtTelefono.setText(String.valueOf(((Vecino)tblVecinos.getSelectionModel().getSelectedItem()).getTelefono()));
        }
        else{
            JOptionPane.showMessageDialog(null, "No hay un elemento para seleccionar");
        }
    } public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnActualizar.setDisable(true);
                btnReporte.setDisable(true);
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
            Vecino registro=new Vecino();
                registro.setNIT(txtNIT.getText());
                registro.setDPI(txtDPI.getText());
                registro.setNombres(txtNombres.getText());
                registro.setApellidos(txtApellidos.getText());
                registro.setTelefono(txtTelefono.getText());
                registro.setMunicipalidad(txtMunicipalidad.getText());
                registro.setDireccion(txtDireccion.getText());
                registro.setCodigoPostal(Integer.parseInt(txtCodigoPostal.getText()));
                        try{      
                        PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarVecino(?,?,?,?,?,?,?,?)}");
                        procedimiento.setString(1, registro.getNIT());
                        procedimiento.setString(2, registro.getDPI());
                        procedimiento.setString(3, registro.getNombres());
                        procedimiento.setString(4, registro.getApellidos());
                        procedimiento.setString(5, registro.getDireccion());
                        procedimiento.setString(6, registro.getMunicipalidad());
                        procedimiento.setInt(7, registro.getCodigoPostal());
                        procedimiento.setString(8, registro.getTelefono());
                        procedimiento.execute();
                        listaVecinos.add(registro);
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
                        break;
                    default:
                        if(tblVecinos.getSelectionModel().getSelectedItem()!=null){
                            int respuesta=JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                              if(respuesta==JOptionPane.YES_OPTION){
                                try{
                                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarVecino(?)}");
                                    procedimiento.setString(1,((Vecino)tblVecinos.getSelectionModel().getSelectedItem()).getNIT());
                                    procedimiento.execute();
                                    listaVecinos.remove(tblVecinos.getSelectionModel().getSelectedIndex());
                                    limpiarControles();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }if(respuesta==JOptionPane.NO_OPTION){
                            limpiarControles();}
                        }
                        else{
                        JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
                    }   break;
                
                }
          }
          
          
        public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
              if(tblVecinos.getSelectionModel().getSelectedItem() != null){
        btnActualizar.setText("Guardar");
        btnReporte.setText("Cancelar");
        btnAgregar.setDisable(true);
        btnEliminar.setDisable(true);
        activarControles();
        txtNIT.setEditable(false);
        tipoDeOperacion=operaciones.ACTUALIZAR;
        }
            else JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
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
           tipoDeOperacion=operaciones.NINGUNO;
           break;
    }        
    }
        public void reporte(){
            switch(tipoDeOperacion){
                  case ACTUALIZAR:
            desactivarControles();
            limpiarControles();
            btnActualizar.setText("Actualizar");
            btnReporte.setText("Reporte");
            btnAgregar.setDisable(false);
            btnEliminar.setDisable(false);
            tblVecinos.getSelectionModel().clearSelection();
            tipoDeOperacion = operaciones.NINGUNO;
            break;
    }   
        }
        
        public void actualizar(){
            try{
                PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarVecino(?,?,?,?,?,?,?,?)}");
                Vecino registro=(Vecino)tblVecinos.getSelectionModel().getSelectedItem();
                registro.setDPI(txtDPI.getText());
                registro.setCodigoPostal(Integer.parseInt(txtCodigoPostal.getText()));
                registro.setApellidos(txtApellidos.getText());
                registro.setNombres(txtNombres.getText());
                registro.setDireccion(txtDireccion.getText());
                registro.setMunicipalidad(txtMunicipalidad.getText());
                registro.setTelefono(txtTelefono.getText());
                procedimiento.setString(1,registro.getNIT());
                procedimiento.setString(2,registro.getDPI());
                procedimiento.setString(3,registro.getNombres());
                procedimiento.setString(4,registro.getApellidos());
                procedimiento.setString(5,registro.getDireccion());
                procedimiento.setString(6,registro.getMunicipalidad());
                procedimiento.setInt(7,registro.getCodigoPostal());
                procedimiento.setString(8,registro.getTelefono());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
         public void desactivarControles(){
        txtDPI.setEditable(false);
        txtNIT.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        txtMunicipalidad.setEditable(false);
        txtCodigoPostal.setEditable(false);

         }
    public void activarControles(){
        txtDPI.setEditable(true);
        txtNIT.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtMunicipalidad.setEditable(true);
        txtCodigoPostal.setEditable(true);
     }
    public void limpiarControles(){
         txtDPI.clear();
        txtNIT.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtMunicipalidad.clear();
        txtCodigoPostal.clear();
        tblVecinos.getSelectionModel().clearSelection();
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
    
     
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }  
     public void ventanaMenu(){
        escenarioPrincipal.ventanaMenu();
    }
}