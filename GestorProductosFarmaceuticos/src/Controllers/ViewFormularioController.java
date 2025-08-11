package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class ViewFormularioController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    //Botones
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    
    //Choice Boxes
    @FXML
    private ChoiceBox<?> cbEstadoVehiculo;
    @FXML
    private ChoiceBox<?> cbTipoCombustible;
    @FXML
    private ChoiceBox<?> cbTipoVehiculo;
    
    //Date picker
    @FXML
    private DatePicker dpRenta;
    
    //Labels
    @FXML
    private Label lblPrimerAtributo;
    @FXML
    private Label lblSegundoAtributo;
    @FXML
    private Label lblTiempoRenta;



}
