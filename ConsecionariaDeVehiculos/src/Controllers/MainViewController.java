package Controllers;

import Enums.EstadoVehiculo;
import Enums.TipoVehiculos;
import Exceptions.PatenteRepetidaException;
import Gestor.AdministradorVehiculos;
import Models.Auto;
import Models.Camioneta;
import Models.Moto;
import Models.Vehiculo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainViewController implements Initializable {

    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private TableColumn<Vehiculo, String> tipoCol;
    @FXML private TableColumn<Vehiculo, String> patenteCol;
    @FXML private TableColumn<Vehiculo, Integer> añoCol;
    @FXML private TableColumn<Vehiculo, String> combustibleCol;
    @FXML private TableColumn<Vehiculo, Float> kmCol;
    @FXML private TableColumn<Vehiculo, String> estadoCol;
    @FXML private TableColumn<Vehiculo, String> marcaCol;
    @FXML private TableColumn<Vehiculo, String> segundoAtributoCol;

    private AdministradorVehiculos administrador;

    @FXML private Button btnAgregar, btnEliminar, btnModificar, btnGuardarFiltrado, btnGuardarTodo, btnFiltrar;
    @FXML private ChoiceBox<EstadoVehiculo> cbFiltrarEstado;
    @FXML private ChoiceBox<TipoVehiculos> cbFiltrarTipo;
    @FXML private Label lblPrimerAtributo, lblSegundoAtributo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbFiltrarEstado.getItems().addAll(EstadoVehiculo.values());
        cbFiltrarEstado.setValue(EstadoVehiculo.TODOS);

        cbFiltrarTipo.getItems().addAll(TipoVehiculos.AUTO, TipoVehiculos.CAMIONETA, TipoVehiculos.MOTO, TipoVehiculos.TODOS);
        cbFiltrarTipo.setValue(TipoVehiculos.TODOS);

        administrador = new AdministradorVehiculos();

        // Configuración de columnas
        tipoCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipo().toString()));
        patenteCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPatente()));
        añoCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAñoFabricacion()).asObject());
        combustibleCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipoCombustible().toString()));
        kmCol.setCellValueFactory(cell -> new SimpleFloatProperty(cell.getValue().getKilometros()).asObject());
        estadoCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstadoVehiculo().toString()));
        
        marcaCol.setCellValueFactory(cell -> {
            Vehiculo v = cell.getValue();
            if (v instanceof Auto) {
                return new SimpleStringProperty(((Auto) v).getMarca().toString());
            } else if (v instanceof Moto) {
                return new SimpleStringProperty(((Moto) v).getMarca().toString());
            } else if (v instanceof Camioneta) {
                return new SimpleStringProperty(((Camioneta) v).getMarca().toString());
            }
            return new SimpleStringProperty("-");
        });

        segundoAtributoCol.setCellValueFactory(cell -> {
            Vehiculo v = cell.getValue();
            if (v instanceof Auto) {
                return new SimpleStringProperty(String.valueOf(((Auto) v).getNumPuertas()));
            } else if (v instanceof Moto) {
                return new SimpleStringProperty(String.valueOf(((Moto) v).getCilindrada()));
            } else if (v instanceof Camioneta) {
                return new SimpleStringProperty(String.valueOf(((Camioneta) v).getCampacidadCargaKg()));
            }
            return new SimpleStringProperty("-");
        });

        refrescarVista();
    }

    @FXML
    void agregar(ActionEvent event) {
        AbrirView(null, "Formulario");
    }

    @FXML
    void eliminar(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmar eliminación");
            alerta.setHeaderText("¿Estás seguro de eliminar este vehículo?");
            alerta.setContentText(seleccionado.toString());

            Optional<ButtonType> resultado = alerta.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                administrador.eliminar(seleccionado);
                refrescarVista();
            }
        }
    }

    @FXML
    void modificar(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            AbrirView(seleccionado, "Formulario");
        }
    }

    @FXML
    public void cambiarEstado(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            AbrirView(seleccionado, "EstadoVehiculo");
        }
    }

    @FXML
    private void guardarTodo(ActionEvent event) {
        // lógica de guardado completo
    }

    @FXML
    private void guardarFiltrado(ActionEvent event) {
        // lógica de guardado filtrado
    }

    @FXML
    public void filtrar(ActionEvent event) {
        TipoVehiculos tipoSeleccionado = cbFiltrarTipo.getValue();
        EstadoVehiculo estadoSeleccionado = cbFiltrarEstado.getValue();

        try {
            ArrayList<Vehiculo> filtrados = administrador.buscarPorTipos(tipoSeleccionado, estadoSeleccionado);
            refrescarVistaFiltrada(filtrados);
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    private void AbrirView(Vehiculo v, String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/view" + name + ".fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);

            if (name.equals("Formulario")) {
                ViewFormularioController cfc = loader.getController();
                cfc.setVehiculo(v);
                stage.showAndWait();
                Vehiculo resultado = cfc.getVehiculo();
                if (resultado != null && (v == null || !administrador.listarTodo().contains(resultado))) {
                    administrador.agregar(resultado);
                }
            }

            if (name.equals("EstadoVehiculo")) {
                ViewEstadoVehiculoController cevc = loader.getController();
                cevc.setVehiculo(v);
                stage.showAndWait();
                Vehiculo resultado = cevc.getVehiculo();
                if (resultado != null && (v == null || !administrador.listarTodo().contains(resultado))) {
                    administrador.agregar(resultado);
                }
            }

            refrescarVista();

        } catch (IOException | PatenteRepetidaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void refrescarVista() {
        tablaVehiculos.setItems(FXCollections.observableArrayList(administrador.listarTodo()));
    }

    private void refrescarVistaFiltrada(ArrayList<Vehiculo> lista) {
        tablaVehiculos.setItems(FXCollections.observableArrayList(lista));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}