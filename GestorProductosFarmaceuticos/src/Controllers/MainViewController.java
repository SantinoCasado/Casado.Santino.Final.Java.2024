package Controllers;

import Enums.TipoVehiculos;
import Exceptions.PatenteRepetidaException;
import Gestor.AdministradorVehiculos;
import Models.Vehiculo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.jfr.Label;

public class MainViewController implements Initializable {
    @FXML
    private ListView<Vehiculo> ListViewVehiculos;
    
    private ArrayList<Vehiculo> listaVehiculos;
    private AdministradorVehiculos administrador;

    //Botones
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnGuardarFiltrado;
    @FXML
    private Button btnGuardarTodo;
    
    //Choice Box
    @FXML
    private ChoiceBox<String> cbFiltrarEstado;
    @FXML
    private ChoiceBox<TipoVehiculos> cbFiltrarTipo;
    
    //Label
    @FXML
    private Label lblPrimerAtributo;
    @FXML
    private Label lblSegundoAtributo;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializo los Choice Boxes
        this.cbFiltrarEstado.getItems().addAll("Disponible", "En Uso", "En Mantenimiento", "TODOS");
        this.cbFiltrarEstado.setValue("TODOS");
        
        this.cbFiltrarTipo.getItems().addAll(TipoVehiculos.AUTO, TipoVehiculos.CAMIONETA, TipoVehiculos.MOTO, TipoVehiculos.TODOS);
        this.cbFiltrarTipo.setValue(TipoVehiculos.TODOS);
        
       administrador = new AdministradorVehiculos();
       
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CRUD
    @FXML
    void agregar(ActionEvent event) {
        this.AbrirFormulario(null);
    }
    
    @FXML
    void eliminar(ActionEvent event) {
         //Chequeo que se haya seleccionado algo en mi listView
        Vehiculo pf = this.ListViewVehiculos.getSelectionModel().getSelectedItem();
        
        if (pf != null) {
            //Muestro una alerta de confirmacion
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmar eliminacion");
            alerta.setHeaderText("Estas seguro de eliminar estos datos?");
            alerta.setContentText(pf.toString());            
            
            Optional<ButtonType> resultado = alerta.showAndWait();
            
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {    //Verifico que alla un resultado y que sea "OK"
                administrador.eliminar(pf);
            }
            this.refrescarVista();
        }        
    }
    
    @FXML
    void modificar(ActionEvent event) {
         Vehiculo v = this.ListViewVehiculos.getSelectionModel().getSelectedItem();

         if(v != null) {
             this.AbrirFormulario(v);
         }
    }
    
    
 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    //Metodos especiales
    
    //Abir viewForm
    private void AbrirFormulario(Vehiculo v ) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/viewFormulario.fxml"));
    
            Scene scene = new Scene(loader.load());
            ViewFormularioController controlador = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);               
            controlador.setVehiculo(v);      
            stage.showAndWait();
            Vehiculo resultado = controlador.getVehiculo();    
   

                if(resultado != null) {                        
                    if(v == null){       
                        if (!administrador.listarTodo().contains(resultado)){       
                            this.administrador.agregar(resultado);
                        }
                    }
                }
                this.refrescarVista();
        }
        catch(IOException | PatenteRepetidaException e){
            System.out.println("Error:" + e.getMessage());
        }
    }
    
    
    
    //Actualiza la vista
    public void refrescarVista(){
        this.ListViewVehiculos.getItems().clear();
        this.ListViewVehiculos.getItems().addAll(administrador.listarTodo());
    }
}
