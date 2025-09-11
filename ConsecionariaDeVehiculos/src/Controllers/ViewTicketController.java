package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;


public class ViewTicketController implements Initializable {
    @FXML
    private Button btnCerrar;

    @FXML
    private ListView<?> listViewTicket;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    void cerrar(ActionEvent event) {

    }    
    
}
