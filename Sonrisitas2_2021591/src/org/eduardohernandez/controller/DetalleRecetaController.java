
package org.eduardohernandez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.swing.JOptionPane;
import org.eduardohernandez.bean.DetalleReceta;
import org.eduardohernandez.bean.Medicamentos;
import org.eduardohernandez.bean.Receta;
import org.eduardohernandez.db.Conexion;
import org.eduardohernandez.report.GenerarReporte;
import org.eduardohernandez.system.Principal;


public class DetalleRecetaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion=operaciones.NINGUNO;
    private ObservableList<DetalleReceta>listaDetalleReceta;
    private ObservableList<Receta>listaReceta;
    private ObservableList<Medicamentos>listaMedicamentos;
    @FXML private TextField txtcodigoDetalleReceta;
    @FXML private TextField txtDosis;
    @FXML private ComboBox  cmbCodigoMedicamento;
    @FXML private ComboBox  cmbCodigoReceta; 
    @FXML private TableView tblDetalleReceta;
    @FXML private TableColumn colCodigoDetalleReceta;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colCodigoMedicamento;
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
        cmbCodigoMedicamento.setItems(getMedicamentos());
        cmbCodigoReceta.setItems(getReceta());
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
        
    }
    public void cargarDatos(){
        tblDetalleReceta.setItems(getDetalleReceta());
        colCodigoDetalleReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoDetalleReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta,String>("Dosis"));
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoReceta"));
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoMedicamento"));
    }
        public void seleccionarElemento(){
           if(tblDetalleReceta.getSelectionModel().getSelectedItem()!=null){
               txtcodigoDetalleReceta.setText(String.valueOf(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta()));
               txtDosis.setText(String.valueOf(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getDosis()));
               cmbCodigoReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoReceta()));
               cmbCodigoMedicamento.getSelectionModel().select(buscarMedicamentos(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
                cmbCodigoReceta.setDisable(false);
                cmbCodigoMedicamento.setDisable(false);
           }
        
        } 
        
         public Receta buscarReceta(int codigoReceta){
                Receta resultado =null;
                try{
                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarReceta(?)}");
                            procedimiento.setInt(1, codigoReceta);
                            ResultSet registro = procedimiento.executeQuery();
                            while(registro.next()){
                                resultado=new Receta(registro.getInt("codigoReceta"),
                                                registro.getDate("fechaReceta"),
                                                registro.getInt("numeroColegiado")
                                );
                            }
                            }catch(Exception e){
                    e.printStackTrace();
                }
            return resultado;
}
            public Medicamentos buscarMedicamentos(int codigoMedicamento){
                Medicamentos resultado =null;
                try{
                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarMedicamento(?)}");
                            procedimiento.setInt(1, codigoMedicamento);
                            ResultSet registro = procedimiento.executeQuery();
                            while(registro.next()){
                                resultado=new Medicamentos(registro.getInt("codigoMedicamento"),
                                                registro.getString("nombreMedicamento")
                                );
                            }
                            }catch(Exception e){
                    e.printStackTrace();
                }
            return resultado;
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
                    cmbCodigoReceta.setDisable(false);
                    cmbCodigoMedicamento.setDisable(false);
                    break;
                case GUARDAR:
                    if("".equals(txtDosis.getText())|(cmbCodigoMedicamento.getValue()==null)
                            |(cmbCodigoReceta.getValue())==null)
                        JOptionPane.showMessageDialog(null, "Campos Incompletos");
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
                    }break;
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
                        if(tblDetalleReceta.getSelectionModel().getSelectedItem()!=null){
                            int respuesta =JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","Eliminar Detalle Receta",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                            if(respuesta==JOptionPane.YES_OPTION){
                                try{
                                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDetalleReceta(?)}");
                                    procedimiento.setInt(1,((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta());
                                    procedimiento.execute();
                                    listaDetalleReceta.remove(tblDetalleReceta.getSelectionModel().getSelectedIndex());
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
         public void guardar(){
                DetalleReceta registro=new DetalleReceta();
                registro.setDosis(txtDosis.getText());
                registro.setCodigoMedicamento(((Medicamentos)cmbCodigoMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                registro.setCodigoReceta(((Receta)cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
                
                try{
                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarDetalleReceta(?,?,?)}");
                    procedimiento.setString(1,registro.getDosis());
                    procedimiento.setInt(2,registro.getCodigoReceta());
                    procedimiento.setInt(3, registro.getCodigoMedicamento());
                    procedimiento.execute();
                    listaDetalleReceta.add(registro);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }     
            public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
              if(tblDetalleReceta.getSelectionModel().getSelectedItem() != null){
        btnEditar.setText("Actualizar");
        btnReporte.setText("Cancelar");
        btnNuevo.setDisable(true);
        btnEliminar.setDisable(true);
        imgEditar.setImage(new Image("/org/eduardohernandez/image/Actualizar.png"));
        imgReporte.setImage(new Image("/org/eduardohernandez/image/Cancelar.png"));
        activarControles();
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
        txtcodigoDetalleReceta.setEditable(false);
        tipoDeOperacion=operaciones.ACTUALIZAR;
        }
              else JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
              break; 
            case ACTUALIZAR:
                if("".equals(txtDosis.getText()))
                    JOptionPane.showMessageDialog(null, "Campos Incompletos");
                else{
                actualizar();
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
                }break; 
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
            tblDetalleReceta.getSelectionModel().clearSelection();
            tipoDeOperacion = operaciones.NINGUNO;
            break;

    }   
        }
            
     public void imprimirReporte(){
    Map parametros=new HashMap();
    parametros.put("codigoDetalleReceta", null);
    parametros.put("Imagen", GenerarReporte.class.getResource("/org/eduardohernandez/image/Hoja Membretada.jpg"));    
    GenerarReporte.mostrarReportre("ReporteDetalleReceta.jasper", "Reporte De DetalleReceta", parametros);
    parametros.put("Imagen", GenerarReporte.class.getResource("/org/eduardohernandez/image/Hoja Membretada.jpg"));

    
}          
    public void actualizar(){ 
         try{
         PreparedStatement procedimiento =Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDetalleReceta(?,?)}");
            DetalleReceta registro = (DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem();
            registro.setDosis(txtDosis.getText());
            procedimiento.setInt(1,registro.getCodigoDetalleReceta());
             procedimiento.setString(2,registro.getDosis());
            procedimiento.execute();
     } catch(Exception e){
    e.printStackTrace();
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
        }
        }catch(Exception e){
                    e.printStackTrace();
                }    return listaReceta = FXCollections.observableArrayList(lista);
           
        
    }
      public ObservableList<DetalleReceta>getDetalleReceta(){
        ArrayList<DetalleReceta>lista=new ArrayList<DetalleReceta>();
        try{
                PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalleRecetas()}");
                ResultSet resultado=procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new DetalleReceta(resultado.getInt("codigoDetalleReceta"),
                    resultado.getString("dosis"),
                    resultado.getInt("codigoReceta"),
                    resultado.getInt("codigoMedicamento")
            ));
        }}catch(Exception e){
                    e.printStackTrace();
                }    return listaDetalleReceta = FXCollections.observableArrayList(lista);
           
        
    }
    
    public ObservableList<Medicamentos>getMedicamentos(){
        ArrayList <Medicamentos>lista=new ArrayList<Medicamentos>();
        try{
        PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos()}");
                ResultSet resultado=procedimiento.executeQuery();
                while(resultado.next()){
                lista.add(new Medicamentos(resultado.getInt("codigoMedicamento" ),
                        resultado.getString("nombreMedicamento")));              
                } 
        }catch(Exception e){
            e.printStackTrace();
            }
         return listaMedicamentos = FXCollections.observableArrayList(lista);
       }
    public void desactivarControles(){
        txtDosis.setEditable(true);
        txtcodigoDetalleReceta.setEditable(true);
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
        
    }
    public void activarControles(){
        txtDosis.setEditable(true);
        txtcodigoDetalleReceta.setEditable(false);
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
    }
    public void limpiarControles(){
         txtDosis.clear();
         txtcodigoDetalleReceta.clear();
         cmbCodigoReceta.setValue(null);
         cmbCodigoMedicamento.setValue(null);
         tblDetalleReceta.getSelectionModel().clearSelection();

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