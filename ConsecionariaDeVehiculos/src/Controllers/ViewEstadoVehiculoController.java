package Controllers;

import Enums.EstadoVehiculo;
import Exceptions.DatoErroneoException;
import Interfaces.IVehiculoEditable;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewEstadoVehiculoController implements Initializable, IVehiculoEditable {
    @FXML
    private ChoiceBox<EstadoVehiculo> cbEstadoVehiculo;
    
    @FXML
    private DatePicker dpFechaAlquiler;
    
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblSegundoAtributo;
    
    @FXML
    private TextField txtAñoFabricacion,  txtKilometraje, txtMarca,  txtPatente, txtSegundoAtributo, txtTipo;    

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
            txtTipo.setText(v.getTipo().toString());
            txtAñoFabricacion.setText(String.valueOf(v.getAñoFabricacion()));
            txtPatente.setText(v.getPatente());
            txtKilometraje.setText(String.valueOf(v.getKilometros()));
            cbEstadoVehiculo.setValue(v.getEstadoVehiculo());
            if (v.getFechaAlquiler() != null) {
                dpFechaAlquiler.setValue(v.getFechaAlquiler());
            }

            // Cambia el label y el campo según el tipo de vehículo
            if (v instanceof Auto) {
                lblSegundoAtributo.setText("Cantidad de Puertas:");
                txtSegundoAtributo.setText(String.valueOf(((Auto) v).getNumPuertas()));
                txtMarca.setText(String.valueOf(((Auto) v).getMarca()));           
            } else if (v instanceof Camioneta) {
                lblSegundoAtributo.setText("Capacidad de Carga:");
                txtSegundoAtributo.setText(String.valueOf(((Camioneta) v).getCampacidadCargaKg()));
                txtMarca.setText(String.valueOf(((Camioneta) v).getMarca()));
            } else if (v instanceof Moto) {
                lblSegundoAtributo.setText("Cilindrada:");
                txtSegundoAtributo.setText(String.valueOf(((Moto) v).getCilindrada()));
                txtMarca.setText(String.valueOf(((Moto) v).getMarca()));
            } else {
                lblSegundoAtributo.setText("Segundo Atributo:");
                txtSegundoAtributo.setText("");
            }
        }
    }

    @Override
    public Vehiculo getVehiculo() {
        // Actualiza el estado y la fecha en el objeto antes de devolverlo
        if (v != null) {
            v.setEstadoVehiculo(cbEstadoVehiculo.getValue());
            try {
                LocalDate fechaAlquilerNueva = dpFechaAlquiler.getValue();
                Validations.ValidadorAtributosVehiculo.validarFechaFutura(dpFechaAlquiler);
            } catch (DatoErroneoException e) {
                mostrarAlerta("Error", e.getMessage());
            }
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
    
     private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}