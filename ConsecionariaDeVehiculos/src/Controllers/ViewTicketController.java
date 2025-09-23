package Controllers;

import Models.Auto;
import Models.Camioneta;
import Models.Moto;
import Models.Vehiculo;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


public class ViewTicketController implements Initializable {
    @FXML private Button btnCerrar;
    @FXML private ListView<String> listViewTicket;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setVehiculo(Vehiculo v) {
        LocalDate fechaAlquiler = v.getFechaAlquiler();
        if (v != null) {
            String ticket = "";
            if (v instanceof Auto auto) {
                ticket = auto.ImprirTicker(fechaAlquiler);
            } else if (v instanceof Moto moto) {
                ticket = moto.ImprirTicker(fechaAlquiler);
            } else if (v instanceof Camioneta camioneta) {
                ticket = camioneta.ImprirTicker(fechaAlquiler);
            } else {
                ticket = "Tipo de vehículo no soportado.";
            }
            listViewTicket.getItems().setAll(ticket.split("\n"));
        }
    }

    @FXML
    void cerrar(ActionEvent event) {
        listViewTicket.getItems().clear();
        Stage stage = (Stage)btnCerrar.getScene().getWindow();
        stage.close();
    }
}
