
package org.eduardohernandez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.eduardohernandez.bean.Doctor;
import org.eduardohernandez.bean.Receta;
import org.eduardohernandez.db.Conexion;
import org.eduardohernandez.report.GenerarReporte;
import org.eduardohernandez.system.Principal;


public class RecetasController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion=operaciones.NINGUNO; 
    private ObservableList<Receta>listaReceta;
    private ObservableList<Doctor>listaDoctor;    
    private DatePicker fReceta;    
    @FXML private TextField txtCodigoReceta;
    @FXML private ComboBox  cmbNumeroColegiado;
    @FXML private GridPane grpFechas;    
    @FXML private TableView tblRecetas;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colFechaReceta;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(true);
        fReceta = new DatePicker(Locale.ENGLISH);
        fReceta.setDateFormat( new SimpleDateFormat("yy-MM-dd"));
        fReceta.getCalendarView().todayButtonTextProperty().set("Today");
        fReceta.getCalendarView().setShowWeeks(false);
        grpFechas.add(fReceta,3,1);
        fReceta.getStylesheets().add("/org/eduardohernandez/resource/DatePicker.css");
        
    }
    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Receta,Integer>("numeroColegiado"));
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<Receta,Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory<Receta,Date>("fechaReceta"));
        
    }
     public void seleccionarElemento(){
        if(tblRecetas.getSelectionModel().getSelectedItem()!=null){
            txtCodigoReceta.setText(String.valueOf(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            fReceta.selectedDateProperty().set(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getFechaReceta());
            cmbNumeroColegiado.getSelectionModel().select(buscarDoctores(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
            cmbNumeroColegiado.setDisable(false);
            btnReporte.setText("Receta");
        }
           
     }
  public Doctor buscarDoctores(int numeroColegiado){
                Doctor resultado =null;
                try{
                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
                            procedimiento.setInt(1, numeroColegiado);
                            ResultSet registro = procedimiento.executeQuery();
                            while(registro.next()){
                                resultado = new Doctor(registro.getInt("numeroColegiado"),
                                                    registro.getString("nombresDoctor"),
                                        registro.getString("ApellidosDoctor"),
                                        registro.getString("telefonoContacto"),
                                        registro.getInt("codigoEspecialidad")
            );                           }
                            }catch(Exception e){
                    e.printStackTrace();
                }
            return resultado;
}   
     public void nuevo(){
                switch (tipoDeOperacion){
                case NINGUNO:
                    btnReporte.setText("Reporte");
                    activarControles();
                    limpiarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    imgNuevo.setImage(new Image("/org/eduardohernandez/image/Guardar.png"));
                    imgEliminar.setImage(new Image("/org/eduardohernandez/image/Cancelar.png"));
                    tipoDeOperacion=operaciones.GUARDAR;
                    break;
                case GUARDAR:
                    if((fReceta.getSelectedDate())==null|
                        (cmbNumeroColegiado.getValue())==null)
                        JOptionPane.showMessageDialog(null,"Campos Incompletos");
                    else{guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/eduardohernandez/image/Nuevo.png"));
                    imgEliminar.setImage(new Image("/org/eduardohernandez/image/Eliminar.png"));
                    tipoDeOperacion=operaciones.NINGUNO;
                    cargarDatos();
                    } break;
                }
            }
     public void guardar(){
                Receta registro=new Receta();
                registro.setFechaReceta(fReceta.getSelectedDate());
                registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
                try{
                   PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarReceta(?,?)}");
                    procedimiento.setDate(1,new java.sql.Date (registro.getFechaReceta().getTime()));
                    procedimiento.setInt(2,registro.getNumeroColegiado());
                   // procedimiento.setInt(2,registro.getNumeroColegiado());
                    procedimiento.execute();                                                                                                                                                                                                                                                                                                                                                                          
                    listaReceta.add(registro);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
     
             public void eliminar(){
                switch(tipoDeOperacion){
                    case GUARDAR:
                        desactivarControles();
                        limpiarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/eduardohernandez/image/Nuevo.png"));
                        imgEliminar.setImage(new Image("/org/eduardohernandez/image/Eliminar.png"));
                        tipoDeOperacion=operaciones.NINGUNO;
                        break;
                    default:
                        btnReporte.setText("Reporte");
                        if(tblRecetas.getSelectionModel().getSelectedItem()!=null){
                            int respuesta =JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","Eliminar Receta",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                            if(respuesta==JOptionPane.YES_OPTION){
                                try{
                                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
                                    procedimiento.setInt(1,((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                                    procedimiento.execute();
                                    listaReceta.remove(tblRecetas.getSelectionModel().getSelectedIndex());
                                    limpiarControles();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }if(respuesta==JOptionPane.NO_OPTION){
                            limpiarControles();
                        }
                } else{
                        JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
                    } break;
            } }
            public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                btnReporte.setText("Reporte");
              if(tblRecetas.getSelectionModel().getSelectedItem() != null){
        btnEditar.setText("Actualizar");
        btnReporte.setText("Cancelar");
        btnNuevo.setDisable(true);
        btnEliminar.setDisable(true);
        imgEditar.setImage(new Image("/org/eduardohernandez/image/Actualizar.png"));
        imgReporte.setImage(new Image("/org/eduardohernandez/image/Cancelar.png"));
        activarControles();
        cmbNumeroColegiado.setDisable(true);
        txtCodigoReceta.setDisable(true);
        tipoDeOperacion=operaciones.ACTUALIZAR;
        }
              else JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
              break; 
            case ACTUALIZAR:
             if("".equals(fReceta.getSelectedDate())|"".equals(txtCodigoReceta.getText()))
                     JOptionPane.showMessageDialog(null,"Campos Incompletos");                             
             else{actualizar();
            btnEditar.setText("Editar");
           btnReporte.setText("Reporte");
           btnNuevo.setDisable(false);
           btnEliminar.setDisable(false);
           imgEditar.setImage(new Image("/org/eduardohernandez/image/Editar.png"));
           imgReporte.setImage(new Image("/org/eduardohernandez/image/Reporte.png"));
           desactivarControles();
           limpiarControles();
           cargarDatos(); 
           tipoDeOperacion=operaciones.NINGUNO;
             } break;
    }   
            }
       public void actualizar(){ 
         try{
         PreparedStatement procedimiento =Conexion.getInstance().getConexion().prepareCall("{call sp_EditarReceta(?,?)}");
            Receta registro = (Receta)tblRecetas.getSelectionModel().getSelectedItem();
            registro.setFechaReceta(fReceta.getSelectedDate());
            procedimiento.setInt(1,registro.getCodigoReceta());
            procedimiento.setDate(2,new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.execute();
     } catch(Exception e){
    e.printStackTrace();
}
     
    }
       public void reporte(){
    switch(tipoDeOperacion){
        case NINGUNO:
            imprimirReporte();
            break;
        case ACTUALIZAR:
            desactivarControles();
            limpiarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setDisable(false);
            btnEliminar.setDisable(false);
            imgEditar.setImage(new Image("/org/eduardohernandez/image/Editar.png"));
            imgReporte.setImage(new Image("/org/eduardohernandez/image/Reporte.png")); 
            tblRecetas.getSelectionModel().clearSelection();
            tipoDeOperacion = operaciones.NINGUNO;
            break;

    }
    
    }
    
       public void imprimirReporte(){
        if(tblRecetas.getSelectionModel().getSelectedItem()==null){
            Map parametros=new HashMap();
            parametros.put("codigoReceta", null);
            parametros.put("Imagen", GenerarReporte.class.getResource("/org/eduardohernandez/image/Hoja Membretada.jpg"));
            GenerarReporte.mostrarReportre("ReporteReceta.jasper", "Reporte De Doctores", parametros);}        
           if(tblRecetas.getSelectionModel().getSelectedItem()!=null){       
           Map parametros=new HashMap();
         int codReceta =((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta();           
         parametros.put("codReceta",codReceta);
         parametros.put("Image1", GenerarReporte.class.getResource("/org/eduardohernandez/image/Hoja Membretada2.png"));
         parametros.put("Image2", GenerarReporte.class.getResource("/org/eduardohernandez/image/SimboloMedico.png"));
         parametros.put("Image3", GenerarReporte.class.getResource("/org/eduardohernandez/image/Firma.jpg"));
         GenerarReporte.mostrarReportre("RecetaMedica.jasper", "Reporte De Recetas", parametros);
     }
       }
    public ObservableList<Receta>getReceta(){
        ArrayList<Receta>lista=new ArrayList<Receta>();
        try{
                PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas()}");
                ResultSet resultado=procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Receta(resultado.getInt("codigoReceta"),
                    resultado.getDate("fechaReceta"),
                    resultado.getInt("numeroColegiado")
                   
            ));
        }}catch(Exception e){
                    e.printStackTrace();
                }    return listaReceta = FXCollections.observableArrayList(lista);
           
        
    }
    public ObservableList<Doctor>getDoctor(){
        ArrayList<Doctor>lista=new ArrayList<Doctor>();
        try{
                PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores()}");
                ResultSet resultado=procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                    resultado.getString("nombresDoctor"),
                   resultado.getString("apellidosDoctor"),
                  resultado.getString("telefonoContacto"),
                    resultado.getInt("codigoEspecialidad")
            ));
        }}catch(Exception e){
                    e.printStackTrace();
                }    return listaDoctor = FXCollections.observableArrayList(lista);
           
        
    }
         public void desactivarControles(){
        txtCodigoReceta.setEditable(false);
        cmbNumeroColegiado.setDisable(true);
}
     public void activarControles(){
         txtCodigoReceta.setEditable(false);
         cmbNumeroColegiado.setDisable(false);
         
     }
     public void limpiarControles(){
         txtCodigoReceta.clear();
         cmbNumeroColegiado.getSelectionModel().clearSelection();
         fReceta.setSelectedDate(null);
         cmbNumeroColegiado.setValue(null);
         tblRecetas.getSelectionModel().clearSelection();

     }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();}
   
    }