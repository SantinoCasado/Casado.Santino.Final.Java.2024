package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import jdk.jfr.Label;

public class MainViewController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
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
    private ChoiceBox<?> cbFiltrarEstado;
    @FXML
    private ChoiceBox<?> cbFiltrarTipo;
    
    //Label
    @FXML
    private Label lblPrimerAtributo;
    @FXML
    private Label lblSegundoAtributo;
    
    
}
