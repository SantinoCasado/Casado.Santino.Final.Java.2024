package Controllers;

import Enums.EstadoVehiculo;
import Interfaces.IVehiculoEditable;
import Models.Vehiculo;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class ViewEstadoVehiculoController implements Initializable, IVehiculoEditable {
    @FXML
    private ChoiceBox<EstadoVehiculo> cbEstadoVehiculo;
    @FXML
    private DatePicker dpFechaAlquiler;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblSegundoAtributo;

    private Vehiculo v;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbEstadoVehiculo.getItems().addAll(EstadoVehiculo.values());
        cbEstadoVehiculo.setValue(EstadoVehiculo.DISPONIBLE); // Valor por defecto
        dpFechaAlquiler.setValue(LocalDate.now());
    }

    @Override
    public void setVehiculo(Vehiculo v) {
        this.v = v;
        if (v != null) {
            cbEstadoVehiculo.setValue(v.getEstadoVehiculo());
            // Si el vehículo tiene fecha de alquiler, mostrarla
            if (v.getFechaAlquiler() != null) {
                dpFechaAlquiler.setValue(v.getFechaAlquiler());
            }
        }
    }

    @Override
    public Vehiculo getVehiculo() {
        // Actualiza el estado y la fecha en el objeto antes de devolverlo
        if (v != null) {
            v.setEstadoVehiculo(cbEstadoVehiculo.getValue());
            v.setFechaAlquiler(dpFechaAlquiler.getValue());
        }
        return v;
    }

    @FXML
    void imprimirTicket(ActionEvent event) {
        // Aquí podrías mostrar un mensaje, abrir una ventana o generar un PDF
        // Ejemplo simple:
        if (v != null) {
            System.out.println("Ticket generado para: " + v.getPatente() +
                " - Estado: " + v.getEstadoVehiculo() +
                " - Fecha alquiler: " + dpFechaAlquiler.getValue());
        }
    }
}