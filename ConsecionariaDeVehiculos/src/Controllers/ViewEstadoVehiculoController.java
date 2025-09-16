package Controllers;

import Enums.EstadoVehiculo;
import Exceptions.DatoErroneoException;
import Interfaces.IVehiculoEditable;
import Models.Auto;
import Models.Camioneta;
import Models.Moto;
import Models.Vehiculo;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewEstadoVehiculoController implements Initializable, IVehiculoEditable {
    //------------------------------------------ ATRIBUTOS DE CLASE  --------------------------------------------------------------------------------------------------------------
    // CHOICE BOX
    @FXML private ChoiceBox<EstadoVehiculo> cbEstadoVehiculo;

    // DATE PICKER
    @FXML private DatePicker dpFechaAlquiler;

    // LABELS
    @FXML private Label lblFecha;
    @FXML private Label lblSegundoAtributo;
    @FXML private Label lblKmExtra;

    //TEXT FIELDS
    @FXML private TextField txtTipo;
    @FXML private TextField txtMarca;
    @FXML private TextField txtAnioFabricacion;
    @FXML private TextField txtSegundoAtributo;
    @FXML private TextField txtPatente;
    @FXML private TextField txtKilometraje;
    @FXML private TextField txtKmExtra;

    // BUTTONS
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;

    // OBEJTO
    private Vehiculo v;

    //------------------------------------------------- INICIALIZADOR -----------------------------------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //CHOICE BOX OPTIONS
        cbEstadoVehiculo.getItems().addAll(EstadoVehiculo.DISPONIBLE, EstadoVehiculo.ALQUILADO, EstadoVehiculo.EN_MANTENIMIENTO);

        // DATE PICKER
        dpFechaAlquiler.setValue(LocalDate.now());
    }

    // ---------------------------------------------------- ON ACTIONS --------------------------------------------------------------------------------------
    @FXML
    void aceptar(ActionEvent event) {
        if (v != null) {
            try {
                Validations.ValidadorAtributosVehiculo.validarFechaFutura(dpFechaAlquiler);

                EstadoVehiculo estadoAnterior = v.getEstadoVehiculo();
                EstadoVehiculo nuevoEstado = cbEstadoVehiculo.getValue();
                
                v.setEstadoVehiculo(nuevoEstado);
                v.setFechaAlquiler(dpFechaAlquiler.getValue());
                
                // Si pasa de ALQUILADO a DISPONIBLE, pide el kilometraje extra
                if (estadoAnterior == EstadoVehiculo.ALQUILADO && nuevoEstado == EstadoVehiculo.DISPONIBLE) {
                    lblKmExtra.setVisible(true);
                    txtKmExtra.setVisible(true);

                    // Si el campo está vacío, no cerrar y pedir al usuario que lo complete
                    if (txtKmExtra.getText() == null || txtKmExtra.getText().isEmpty()) {
                        mostrarAlerta("Falta dato", "Ingrese el kilometraje extra antes de continuar.");
                        return;
                    }
                    try {
                        float kmNuevo = Float.parseFloat(txtKmExtra.getText());
                        v.setKilometros(v.getKilometros() + kmNuevo);
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error", "Ingrese un valor numérico válido para el kilometraje.");
                        return;
                    }
                    // Oculta los campos después de sumar el kilometraje
                    lblKmExtra.setVisible(false);
                    txtKmExtra.setVisible(false);
                    txtKmExtra.clear();
                }
                
                if(nuevoEstado == EstadoVehiculo.ALQUILADO){
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Imprimir Ticket");
                    alerta.setHeaderText("¿Desea imprimir el ticekt de Alquiler?");
                    alerta.setContentText("Seleccione una opcion");
                    
                    ButtonType btnSi = new ButtonType("Si");
                    ButtonType btnNo = new ButtonType("No");
                    alerta.getButtonTypes().setAll(btnSi, btnNo);
                    
                    alerta.showAndWait().ifPresent(respuesta -> {
                        if (respuesta == btnSi){
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/viewTicket.fxml"));
    
                                Scene scene = new Scene(loader.load());
                                ViewTicketController controlador = loader.getController();
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setScene(scene);               
                                controlador.setVehiculo(v);      
                                stage.showAndWait();
                            } catch (Exception e) {
                                mostrarAlerta("Error", e.getMessage());
                            }
                        }
                        cerrar();
                    });
                }
                cerrar();
                
            } catch (DatoErroneoException e) {
                mostrarAlerta("Error", e.getMessage());
            }
        }
    }
    
    // CANCELAR ACCION
    @FXML
    void cancelar(ActionEvent event) {
        this.cerrar();
    }

    // Método para guardar el kilometraje ingresado
    public void onAceptarKilometraje(Vehiculo vehiculo) {
        try {
            float kmNuevo = Float.parseFloat(txtKilometraje.getText());
            vehiculo.setKilometros(vehiculo.getKilometros() + kmNuevo);
            lblKmExtra.setVisible(false);
            txtKmExtra.setVisible(false);
            txtKmExtra.clear();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese un valor numérico válido para el kilometraje.");
        }
    }

    // CERRAR
    @FXML
    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    // ------------------------------------------------- METODOS PARTICULARES ---------------------------------------------------------------------------------------------
    @Override
    public void setVehiculo(Vehiculo v) {
        this.v = v;
        // Que no sea nuevo
        if (v != null) {
            // ATRIBUTOS COMUNES
            txtTipo.setText(v.getTipo().toString());
            txtTipo.setDisable(true);

            txtAnioFabricacion.setText(String.valueOf(v.getAñoFabricacion()));
            txtAnioFabricacion.setDisable(true);

            txtPatente.setText(v.getPatente());
            txtPatente.setDisable(true);

            txtKilometraje.setText(String.valueOf(v.getKilometros()));
            txtKilometraje.setDisable(true);

            cbEstadoVehiculo.setValue(v.getEstadoVehiculo());

            if (v.getFechaAlquiler() != null) {
                dpFechaAlquiler.setValue(v.getFechaAlquiler());
            }

            // Cambia el label y el campo según el tipo de vehículo
            if (v instanceof Auto) {
                lblSegundoAtributo.setText("Cantidad de Puertas:");
                txtSegundoAtributo.setText(String.valueOf(((Auto) v).getNumPuertas()));
                txtSegundoAtributo.setDisable(true);

                txtMarca.setText(String.valueOf(((Auto) v).getMarca()));
                txtMarca.setDisable(true);
            } else if (v instanceof Camioneta) {
                lblSegundoAtributo.setText("Capacidad de Carga:");
                txtSegundoAtributo.setText(String.valueOf(((Camioneta) v).getCampacidadCargaKg()));
                txtSegundoAtributo.setDisable(true);

                txtMarca.setText(String.valueOf(((Camioneta) v).getMarca()));
                txtMarca.setDisable(true);
            } else if (v instanceof Moto) {
                lblSegundoAtributo.setText("Cilindrada:");
                txtSegundoAtributo.setText(String.valueOf(((Moto) v).getCilindrada()));
                txtSegundoAtributo.setDisable(true);

                txtMarca.setText(String.valueOf(((Moto) v).getMarca()));
                txtMarca.setDisable(true);
            } else {
                lblSegundoAtributo.setText("Segundo Atributo:");
                txtSegundoAtributo.setText("");
                txtSegundoAtributo.setDisable(true);

                txtMarca.setDisable(true);
            }
            
            // Manejo de visibilidad del campo de kilometraje extra
            EstadoVehiculo estadoAnterior = v.getEstadoVehiculo();
            EstadoVehiculo nuevoEstado = cbEstadoVehiculo.getValue();
            if (estadoAnterior == EstadoVehiculo.DISPONIBLE || estadoAnterior == EstadoVehiculo.EN_MANTENIMIENTO) {
                lblKmExtra.setVisible(false);
                txtKmExtra.setVisible(false);
            }

            if (estadoAnterior == EstadoVehiculo.ALQUILADO && nuevoEstado == EstadoVehiculo.DISPONIBLE) {
                lblKmExtra.setVisible(true);
                txtKmExtra.setVisible(true);
            } 
        }
    }

    @Override
    public Vehiculo getVehiculo() {
        // Actualiza el estado y la fecha en el objeto antes de devolverlo
        if (v != null) {
            v.setEstadoVehiculo(cbEstadoVehiculo.getValue());
            try {
                Validations.ValidadorAtributosVehiculo.validarFechaFutura(dpFechaAlquiler);
            } catch (DatoErroneoException e) {
                mostrarAlerta("Error", e.getMessage());
            }
            v.setFechaAlquiler(dpFechaAlquiler.getValue());
        }
        return v;
    }

    // ALERT
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}