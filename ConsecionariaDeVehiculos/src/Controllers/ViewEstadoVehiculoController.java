package Controllers;

import Interfaces.IVehiculoEditable;
import Models.Vehiculo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class ViewEstadoVehiculoController implements Initializable, IVehiculoEditable{
    @FXML
    private ChoiceBox<?> cbEstadoVehiculo;

    @FXML
    private DatePicker dpFechaAlquiler;

    @FXML
    private Label lblFecha;
    @FXML
    private Label lblSegundoAtributo;

     private Vehiculo v;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
    
    @FXML
    void imprimirTicket(ActionEvent event) {

    }

    @Override
    public void setVehiculo(Vehiculo v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vehiculo getVehiculo() {
        return this.v;
    }

}

    

