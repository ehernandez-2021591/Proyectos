
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.eduardohernandez.bean.Medicamentos;
import org.eduardohernandez.db.Conexion;
import org.eduardohernandez.report.GenerarReporte;
import org.eduardohernandez.system.Principal;


public class MedicamentosController implements Initializable{
    private Principal escenarioPrincipal;
     private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion=operaciones.NINGUNO;
    private ObservableList<Medicamentos>listaMedicamentos;
    @FXML private TextField txtcodigoMedicamento;
    @FXML private TextField txtnombreMedicamento;
    @FXML private TableView tblMedicamentos;
    @FXML private TableColumn colcodigoMedicamento;
    @FXML private TableColumn colNombreMedicamento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView ImgNuevo;
    @FXML private ImageView ImgEliminar;
    @FXML private ImageView ImgEditar;
    @FXML private ImageView ImgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblMedicamentos.setItems(getMedicamentos());
        colcodigoMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamentos,Integer>("codigoMedicamento"));
        colNombreMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamentos,String>("nombreMedicamento"));
       }
    public void seleccionarElemento(){
        if(tblMedicamentos.getSelectionModel().getSelectedItem()!=null){
            txtcodigoMedicamento.setText(String.valueOf(((Medicamentos)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
            txtnombreMedicamento.setText(((Medicamentos)tblMedicamentos.getSelectionModel().getSelectedItem()).getNombreMedicamento());
        }
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
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                ImgNuevo.setImage(new Image("/org/eduardohernandez/image/Guardar.png"));
                ImgEliminar.setImage(new Image("/org/eduardohernandez/image/Cancelar.png"));
                tipoDeOperacion=operaciones.GUARDAR;
                    break;     
            case GUARDAR:
             if("".equals(txtnombreMedicamento.getText()))
                        JOptionPane.showMessageDialog(null,"Campos incompletos");     
          else{    
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    ImgNuevo.setImage(new Image("/org/eduardohernandez/image/Nuevo.png"));
                    ImgEliminar.setImage(new Image("/org/eduardohernandez/image/Eliminar.png"));
                    tipoDeOperacion=operaciones.NINGUNO;
                    cargarDatos();
             }    break;
        } }
         public void guardar(){
            Medicamentos registro=new Medicamentos();
                registro.setNombreMedicamento(txtnombreMedicamento.getText());
                    try{
                        PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarMedicamento(?)}");
                        procedimiento.setString(1, registro.getNombreMedicamento());
                        procedimiento.execute();
                        listaMedicamentos.add(registro);
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
                        ImgNuevo.setImage(new Image("/org/eduardohernandez/image/Nuevo.png"));
                        ImgEliminar.setImage(new Image("/org/eduardohernandez/image/Eliminar.png"));
                        tipoDeOperacion=operaciones.NINGUNO;
                        break;
                    default:
                        if(tblMedicamentos.getSelectionModel().getSelectedItem()!=null){
                            int respuesta=JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","Eliminar Medicamento",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                            if(respuesta==JOptionPane.YES_OPTION){
                                try{
                                    PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");
                                    procedimiento.setInt(1,((Medicamentos)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                                    procedimiento.execute();
                                    listaMedicamentos.remove(tblMedicamentos.getSelectionModel().getSelectedIndex());
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
              if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
        btnEditar.setText("Actualizar");
        btnReporte.setText("Cancelar");
        btnNuevo.setDisable(true);
        btnEliminar.setDisable(true);
        ImgEditar.setImage(new Image("/org/eduardohernandez/image/Actualizar.png"));
        ImgReporte.setImage(new Image("/org/eduardohernandez/image/Cancelar.png"));
        activarControles();
        txtcodigoMedicamento.setEditable(false);
        tipoDeOperacion=operaciones.ACTUALIZAR;
        }
            else JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
            break; 
            case ACTUALIZAR:
                if("".equals(txtnombreMedicamento.getText()))
                    JOptionPane.showMessageDialog(null,"Campos Incompletos");
             actualizar();
            btnEditar.setText("Editar");
           btnReporte.setText("Reporte");
           btnNuevo.setDisable(false);
           btnEliminar.setDisable(false);
           ImgEditar.setImage(new Image("/org/eduardohernandez/image/Editar.png"));
           ImgReporte.setImage(new Image("/org/eduardohernandez/image/Reporte.png"));
           desactivarControles();
           limpiarControles();
           cargarDatos(); 
           tipoDeOperacion=operaciones.NINGUNO;
           break;
            }        
    }      
 public void actualizar(){
            try{
                PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamento(?,?)}");
                Medicamentos registro=(Medicamentos)tblMedicamentos.getSelectionModel().getSelectedItem();
                registro.setNombreMedicamento(txtnombreMedicamento.getText());
                procedimiento.setInt(1,registro.getCodigoMedicamento());
                procedimiento.setString(2,registro.getNombreMedicamento());
                procedimiento.execute();
            }catch(Exception e){
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
            ImgEditar.setImage(new Image("/org/eduardohernandez/image/Editar.png"));
            ImgReporte.setImage(new Image("/org/eduardohernandez/image/Reporte.png")); 
            tblMedicamentos.getSelectionModel().clearSelection();
            tipoDeOperacion = operaciones.NINGUNO;
            break;

    }   
        }
 public void imprimirReporte(){
    Map parametros=new HashMap();
    parametros.put("codigoMedicamento", null);
    parametros.put("Imagen", GenerarReporte.class.getResource("/org/eduardohernandez/image/Hoja Membretada.jpg"));            
    GenerarReporte.mostrarReportre("ReporteMedicamentos.jasper", "Reporte De Medicamentos", parametros);
    
    
}   
        public void desactivarControles(){
        txtcodigoMedicamento.setEditable(false);
        txtnombreMedicamento.setEditable(false);
    }
    public void activarControles(){
         txtcodigoMedicamento.setEditable(false);
         txtnombreMedicamento.setEditable(true);
     }
    public void limpiarControles(){
         txtcodigoMedicamento.clear();
         txtnombreMedicamento.clear();
         tblMedicamentos.getSelectionModel().clearSelection();

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

    