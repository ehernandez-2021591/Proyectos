
package org.eduardohernandez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
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
import org.eduardohernandez.bean.Citas;
import org.eduardohernandez.bean.Doctor;
import org.eduardohernandez.bean.Paciente;
import org.eduardohernandez.db.Conexion;
import org.eduardohernandez.report.GenerarReporte;
import org.eduardohernandez.system.Principal;


public class CitasController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion=operaciones.NINGUNO; 
    private ObservableList<Citas>listaCitas;
    private ObservableList<Doctor>listaDoctor;    
    private ObservableList<Paciente>listaPaciente;
    @FXML private DatePicker fCita;
    @FXML private TextField txtCodigoCita;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtHoraCita;
    @FXML private TextField txtCondicion;
    @FXML private GridPane grpFechas;
    @FXML private TableView tblCitas;
    @FXML private TableColumn colCodigoCita;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colHoraCita;
    @FXML private TableColumn colTratamiento;
    @FXML private TableColumn colCondicion;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private ComboBox cmbCodigoPaciente;
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
        cmbCodigoPaciente.setItems(getPaciente());
        cmbCodigoPaciente.setDisable(true);
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat( new SimpleDateFormat("yy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        fCita.getStylesheets().add("/org/eduardohernandez/resource/DatePicker.css");
        grpFechas.add(fCita,1,2);
    }
    public void cargarDatos(){
           tblCitas.setItems(getCitas());
           colCodigoCita.setCellValueFactory(new PropertyValueFactory<Citas,Integer>("codigoCita"));
           colFechaCita.setCellValueFactory(new PropertyValueFactory<Citas,Date>("FechaCita"));
           colHoraCita.setCellValueFactory(new PropertyValueFactory<Citas,Time>("horaCita"));
           colTratamiento.setCellValueFactory(new PropertyValueFactory<Citas,String>("tratamiento"));
           colCondicion.setCellValueFactory(new PropertyValueFactory<Citas,String>("descripCondActual"));
           colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Citas,Integer>("codigoPaciente"));
           colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Citas,Integer>("numeroColegiado"));
          }
    public void seleccionarElemento(){
                if(tblCitas.getSelectionModel().getSelectedItem()!=null){
                    txtCodigoCita.setText(String.valueOf(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
                    txtTratamiento.setText(String.valueOf(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento()));
                    txtHoraCita.setText(String.valueOf(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
                    txtCondicion.setText(String.valueOf(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getDescripCondActual()));
                    cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
                    cmbNumeroColegiado.setDisable(false);
                    cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
                    cmbCodigoPaciente.setDisable(false);
                    fCita.selectedDateProperty().set(((Citas)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
                }
    }
    
         public void nuevo(){
                switch (tipoDeOperacion){
                case NINGUNO:
                    limpiarControles();                  
                    activarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    imgNuevo.setImage(new Image("/org/eduardohernandez/image/Guardar.png"));
                    imgEliminar.setImage(new Image("/org/eduardohernandez/image/Cancelar.png"));
                    tipoDeOperacion=operaciones.GUARDAR;
                    break;
                case GUARDAR:
                    if("".equals(txtTratamiento.getText())|"".equals(txtHoraCita.getText())|"".equals(txtCondicion)|(fCita.getSelectedDate())==null
                            |(cmbNumeroColegiado.getValue())==null|(cmbCodigoPaciente.getValue())==null)
                        JOptionPane.showMessageDialog(null,"Campos incompletos");
                    else{
                    guardar();
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
                    }   break;
                }
            }
    
            public void guardar(){
                Citas registro=new Citas();
                //registro.setCodigoCita(Integer.parseInt(txtCodigoCita.getText()));
                registro.setFechaCita(fCita.getSelectedDate());
                registro.setHoraCita(Time.valueOf(txtHoraCita.getText()));
                registro.setTratamiento(txtTratamiento.getText());
                registro.setDescripCondActual(txtCondicion.getText());
                registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
                    try{
                        PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCita(?,?,?,?,?,?)}");
                                procedimiento.setDate(1,new java.sql.Date(registro.getFechaCita().getTime()));
                                procedimiento.setTime(2, new java.sql.Time(registro.getHoraCita().getTime()));
                                procedimiento.setString(3, registro.getTratamiento());
                                procedimiento.setString(4, registro.getDescripCondActual());
                                procedimiento.setInt(5, registro.getCodigoPaciente());
                                procedimiento.setInt(6, registro.getNumeroColegiado());
                                procedimiento.execute();
                                listaCitas.add(registro);
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
                        if(tblCitas.getSelectionModel().getSelectedItem()!=null){
                            int respuesta =JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","Eliminar Cita",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                            if(respuesta==JOptionPane.YES_OPTION){
                                try{
                                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCita(?)}");
                                    procedimiento.setInt(1,((Citas)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                                    procedimiento.execute();
                                    listaCitas.remove(tblCitas.getSelectionModel().getSelectedIndex());
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
              if(tblCitas.getSelectionModel().getSelectedItem() != null){
        btnEditar.setText("Actualizar");
        btnReporte.setText("Cancelar");
        btnNuevo.setDisable(true);
        btnEliminar.setDisable(true);
        imgEditar.setImage(new Image("/org/eduardohernandez/image/Actualizar.png"));
        imgReporte.setImage(new Image("/org/eduardohernandez/image/Cancelar.png"));
        activarControles();
        cmbNumeroColegiado.setDisable(true);
        cmbCodigoPaciente.setDisable(true);
        txtCodigoCita.setEditable(false);
        tipoDeOperacion=operaciones.ACTUALIZAR;
        }
              else JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
              break; 
            case ACTUALIZAR:
    if("".equals(txtTratamiento.getText())|"".equals(txtHoraCita.getText())|"".equals(txtCondicion)|"".equals(fCita.getSelectedDate()))                
            JOptionPane.showMessageDialog(null, "Campos Incompletos");
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
            tblCitas.getSelectionModel().clearSelection();
            tipoDeOperacion = operaciones.NINGUNO;
            break;

    }
    
    }
              
     public void imprimirReporte(){
           Map parametros=new HashMap();
           parametros.put("codigoCita", null);
           parametros.put("Imagen",GenerarReporte.class.getResource("/org/eduardohernandez/image/Hoja Membretada.jpg"));
        GenerarReporte.mostrarReportre("ReporteCitas.jasper", "Reporte De Citas", parametros);
    
       }          
          public void actualizar(){ 
         try{
         PreparedStatement procedimiento =Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCita(?,?,?,?,?)}");
            Citas registro = (Citas)tblCitas.getSelectionModel().getSelectedItem();
            registro.setFechaCita(fCita.getSelectedDate());
            registro.setHoraCita(Time.valueOf(txtHoraCita.getText()));
            registro.setTratamiento(txtTratamiento.getText());
            registro.setDescripCondActual(txtCondicion.getText());
            procedimiento.setInt(1,registro.getCodigoCita());
            procedimiento.setDate(2,new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(3, new java.sql.Time(registro.getHoraCita().getTime()));
            procedimiento.setString(4, registro.getDescripCondActual());
            procedimiento.setString(5, registro.getTratamiento());
            procedimiento.execute();
     } catch(Exception e){
    e.printStackTrace();
}
     }
             
    public Doctor buscarDoctor(int numeroColegiado){
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
      
    public Paciente buscarPaciente(int codigoCita){
                Paciente resultado =null;
                try{
                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
                            procedimiento.setInt(1, codigoCita);
                            ResultSet registro = procedimiento.executeQuery();
                            while(registro.next()){
                                resultado = new Paciente(registro.getInt("codigoPaciente"),
                                                  registro.getString("nombrePaciente"),
                                                    registro.getString("apellidosPaciente"),
                                                    registro.getString("sexo"),
                                                    registro.getDate("fechaNacimiento"),
                                                    registro.getString("direccionPaciente"),
                                                    registro.getString("telefonoPersonal"),
                                                    registro.getDate("fechaPrimeraVisita")
            );                           }
                            }catch(Exception e){
                    e.printStackTrace();
                }
            return resultado;
}   
    public ObservableList<Citas>getCitas(){
        ArrayList<Citas>lista=new ArrayList<Citas>();
        try{
                PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCitas()}");
                ResultSet resultado=procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Citas(resultado.getInt("codigoCita"),
                    resultado.getDate("fechaCita"),
                    resultado.getTime("horaCita"),
                    resultado.getString("tratamiento"),
                    resultado.getString("descripCondActual"),
                    resultado.getInt("codigoPaciente"),
                    resultado.getInt("numeroColegiado")
                   
            ));
        }
        }catch(Exception e){
                    e.printStackTrace();
                }    return listaCitas = FXCollections.observableArrayList(lista);
           
        
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
    
    
     public ObservableList<Paciente>getPaciente(){
        ArrayList<Paciente>lista=new ArrayList<Paciente>();
        try{
                PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes()}");
                ResultSet resultado=procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                        resultado.getString("nombrePaciente"),
                        resultado.getString("apellidosPaciente"),
                        resultado.getString("sexo"),
                        resultado.getDate("fechaNacimiento"),
                        resultado.getString("direccionPaciente"),
                        resultado.getString("telefonoPersonal"),
                        resultado.getDate("fechaPrimeraVisita")
            ));
        }}catch(Exception e){
                    e.printStackTrace();
                }    return listaPaciente = FXCollections.observableArrayList(lista);
            }
     
            public void activarControles(){
                txtCodigoCita.setEditable(false);
                txtTratamiento.setEditable(true);
                txtHoraCita.setEditable(true);
                txtCondicion.setEditable(true);
                cmbNumeroColegiado.setDisable(false);
                cmbCodigoPaciente.setDisable(false);
            }
            public void desactivarControles(){
                txtCodigoCita.setEditable(false);
                txtTratamiento.setEditable(false);
                txtHoraCita.setEditable(false);
                txtCondicion.setEditable(false);
                cmbNumeroColegiado.setDisable(true);
                cmbCodigoPaciente.setDisable(true);
            }
            public void limpiarControles(){
                txtCodigoCita.clear();
                txtTratamiento.clear();
                txtHoraCita.clear();
                txtCondicion.clear();
                cmbNumeroColegiado.setValue(null);
                cmbCodigoPaciente.setValue(null);
                fCita.setSelectedDate(null);
                tblCitas.getSelectionModel().clearSelection();

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
