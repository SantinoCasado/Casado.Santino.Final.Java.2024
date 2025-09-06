package Controllers;

import Controllers.ViewFormularioController;
import Enums.EstadoVehiculo;
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
import javafx.scene.control.Label;

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
    @FXML
    private Button btnFiltrar;
    
    //Choice Box
    @FXML
    private ChoiceBox<EstadoVehiculo> cbFiltrarEstado;
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
        cbFiltrarEstado.getItems().addAll(EstadoVehiculo.values());
        this.cbFiltrarEstado.setValue(EstadoVehiculo.TODOS);
        
        this.cbFiltrarTipo.getItems().addAll(TipoVehiculos.AUTO, TipoVehiculos.CAMIONETA, TipoVehiculos.MOTO, TipoVehiculos.TODOS);
        this.cbFiltrarTipo.setValue(TipoVehiculos.TODOS);
        
       administrador = new AdministradorVehiculos();
       
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CRUD
    @FXML
    void agregar(ActionEvent event) {
        this.AbrirView(null, "Formulario");
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
             this.AbrirView(v, "Formulario");
         }
    }
    
    @FXML
    public void filtrar(ActionEvent event) {
    // Lógica de filtrado
    }
    
    @FXML
    public void cambiarEstado(ActionEvent event) {
        Vehiculo v = this.ListViewVehiculos.getSelectionModel().getSelectedItem();
        this.AbrirView(v, "EstadoVehiculo");
    }

    
    @FXML
    private void guardarTodo(ActionEvent event) {
        // tu lógica acá
    }
    
    @FXML
    private void guardarFiltrado(ActionEvent event) {
        // lógica de guardado filtrado
    }

 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    //Metodos especiales
    
    //Abir viewForm
    private void AbrirView(Vehiculo v, String name) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/view"+name+".fxml"));
            Scene scene = new Scene(loader.load());
            
            if (name.equals("Formulario")) {
                ViewFormularioController cfc = (ViewFormularioController) loader.getController();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);               
                cfc.setVehiculo(v);      
                stage.showAndWait();
                Vehiculo resultado = cfc.getVehiculo();    


                    if(resultado != null) {                        
                        if(v == null){       
                            if (!administrador.listarTodo().contains(resultado)){       
                                this.administrador.agregar(resultado);
                            }
                        }
                    }
                    this.refrescarVista();
                }
            if (name.equals("EstadoVehiculo")) {
                ViewEstadoVehiculoController cevc = loader.getController();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);               
                cevc.setVehiculo(v);      
                stage.showAndWait();
                Vehiculo resultado = cevc.getVehiculo();    


                 if(resultado != null) {                        
                    if(v == null){       
                        if (!administrador.listarTodo().contains(resultado)){       
                            this.administrador.agregar(resultado);
                        }
                    }
                 }
                 this.refrescarVista();
            } 
        }catch(IOException | PatenteRepetidaException e){
            System.out.println("Error:" + e.getMessage());
        }
        
    }
 
    //Actualiza la vista
    public void refrescarVista(){
        this.ListViewVehiculos.getItems().clear();
        this.ListViewVehiculos.getItems().addAll(administrador.listarTodo());
    }
}
