package Controllers;

import Controllers.ViewEstadoVehiculoController;
import Controllers.ViewFormularioController;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainViewController implements Initializable {
    //------------------------------------------ ATRIBUTOS DE CLASE  --------------------------------------------------------------------------------------------------------------
    // TABLE
    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private TableColumn<Vehiculo, String> tipoCol;
    @FXML private TableColumn<Vehiculo, String> patenteCol;
    @FXML private TableColumn<Vehiculo, Integer> añoCol;
    @FXML private TableColumn<Vehiculo, String> combustibleCol;
    @FXML private TableColumn<Vehiculo, Float> kmCol;
    @FXML private TableColumn<Vehiculo, String> estadoCol;
    @FXML private TableColumn<Vehiculo, String> marcaCol;
    @FXML private TableColumn<Vehiculo, String> segundoAtributoCol;

    //BOTONES
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnFiltrar;
    @FXML private Button btnCambiarEstado;
    @FXML private Button btnAceptar;
    
    //CHOICE BOXES
    @FXML private ChoiceBox<EstadoVehiculo> cbFiltrarEstado;
    @FXML private ChoiceBox<TipoVehiculos> cbFiltrarTipo;
    @FXML private ChoiceBox<String> cbSave;
    
    //LABELS
    @FXML private Label lblPrimerAtributo;
    @FXML private Label lblSegundoAtributo;

    // ADMINISTRADOR
    private AdministradorVehiculos administrador;

    //------------------------------------------------- INICIALIZADOR -----------------------------------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //CHOICES BOXES
        cbFiltrarEstado.getItems().addAll(EstadoVehiculo.values());                                                                  //  Estados del Vehiculo
        cbFiltrarEstado.setValue(EstadoVehiculo.TODOS);

        cbFiltrarTipo.getItems().addAll(TipoVehiculos.AUTO, TipoVehiculos.CAMIONETA, TipoVehiculos.MOTO, TipoVehiculos.TODOS);      //  Tipos de vehiculo 
        cbFiltrarTipo.setValue(TipoVehiculos.TODOS);

        cbSave.getItems().addAll(                                                                                                   // Achivos
                    "Guardar CSV",
                    "Cargar CSV",
                    "Guardar JSON",
                    "Cargar JSON",
                    "Exportar TXT"
        );
        cbSave.setValue("Guardar CSV");

        administrador = new AdministradorVehiculos();                                                                               //Inicializo el administrador

        // Configuración de columnas
        tipoCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipo().toString()));                        // Tipo
        patenteCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPatente()));                             // Patente
        añoCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAñoFabricacion()).asObject());              // Año
        combustibleCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipoCombustible().toString()));      // Combustible
        kmCol.setCellValueFactory(cell -> new SimpleFloatProperty(cell.getValue().getKilometros()).asObject());                     // Kilometros
        estadoCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstadoVehiculo().toString()));            // Estado
        
        marcaCol.setCellValueFactory(cell -> {                                                                                      // Marca
            Vehiculo v = cell.getValue();   // Obtiene el valor de la celda
            // Dependiendo de su instancia
            if (v instanceof Auto) {
                return new SimpleStringProperty(((Auto) v).getMarca().toString());      // marcasAuto
            } else if (v instanceof Moto) {
                return new SimpleStringProperty(((Moto) v).getMarca().toString());      // marcasMoto
            } else if (v instanceof Camioneta) {
                return new SimpleStringProperty(((Camioneta) v).getMarca().toString()); // marcasCamioneta
            }
            return new SimpleStringProperty("-");                                       // Valor nulo
        });

        segundoAtributoCol.setCellValueFactory(cell -> {                                                                             // Segundo Atributo  
            Vehiculo v = cell.getValue();
            if (v instanceof Auto) {
                return new SimpleStringProperty(String.valueOf(((Auto) v).getNumPuertas()));    // Metodo de Auto
            } else if (v instanceof Moto) {
                return new SimpleStringProperty(String.valueOf(((Moto) v).getCilindrada()));    // Metodo de Moto
            } else if (v instanceof Camioneta) {
                return new SimpleStringProperty(String.valueOf(((Camioneta) v).getCampacidadCargaKg()));    // Metodo de Camioneta
            }
            return new SimpleStringProperty("-");
        });

        refrescarVista();       // Refresco de vista por predeterminado
    }

    // ----------------------------------------------------- ON ACTIONS -----------------------------------------------------------------------------------------------
    // AGREGAR
    @FXML
    void agregar(ActionEvent event) {
        AbrirView(null, "Formulario");
    }

    // ELIMINAR
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

    // MODIFICAR
    @FXML
    void modificar(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            AbrirView(seleccionado, "Formulario");
        }
    }

    // ------------------------------------------------ CAMBIO DE ESTADO -------------------------------
    @FXML
    public void cambiarEstado(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            AbrirView(seleccionado, "EstadoVehiculo");
        }
    }

    // ------------------------------------------------- ARCHIVOS -------------------------------------------
    @FXML
    private void aceptarAccion(ActionEvent event) throws Exception {
        String accion = cbSave.getValue();
        try{
            switch (accion) {
                case "Guardar CSV":
                    administrador.guardarCSV();
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Archivo CSV guardado correctamente.");
                    break;
                case "Cargar CSV":
                    administrador.cargarCSV();
                    refrescarVista();
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Archivo CSV cargado correctamente.");
                    break;
                case "Guardar JSON":
                    administrador.guardarJSON();
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Archivo JSON guardado correctamente.");
                    break;
                case "Cargar JSON":
                    administrador.cargarJSON();
                    refrescarVista();
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Archivo JSON cargado correctamente.");
                    break;
                case "Exportar TXT":
                    ArrayList<Vehiculo> filtrados = administrador.buscarPorTipos(TipoVehiculos.TODOS, EstadoVehiculo.TODOS);
                    administrador.exportarListadoFiltradoTXT(filtrados, "Listado de Vehículos");
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Archivo TXT exportado correctamente.");
                    break;
        }
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Ocurrió un error: " + e.getMessage());
        }
    }

    // ------------------------------------------------- FILTRADO ---------------------------------------
    @FXML
    public void filtrar(ActionEvent event) {
        TipoVehiculos tipoSeleccionado = cbFiltrarTipo.getValue();
        EstadoVehiculo estadoSeleccionado = cbFiltrarEstado.getValue();

        try {
            ArrayList<Vehiculo> filtrados = administrador.buscarPorTipos(tipoSeleccionado, estadoSeleccionado);
            refrescarVistaFiltrada(filtrados);
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.ERROR,"Error", e.getMessage());
        }
    }

    // ------------------------------------------------- METODOS PARTICULARES -----------------------------------------------------------------------------------
    // Apertura de views
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
                if (resultado != null) {
                    if (v == null || !administrador.listarTodo().contains(resultado)) {
                        administrador.agregar(resultado);
                } else {
                    administrador.modificar(resultado);
                    }
                }
            }

                if (name.equals("EstadoVehiculo")) {
                    ViewEstadoVehiculoController cevc = loader.getController();
                    cevc.setVehiculo(v);
                    stage.showAndWait();
                    Vehiculo resultado = cevc.getVehiculo();
                    if (resultado != null) {
                        administrador.modificar(resultado); 
                    }
                }

            refrescarVista();

        } catch (IOException | PatenteRepetidaException e) {
            System.out.println(e);
        }
    }

    // Refresco de la vista
    public void refrescarVista() {
        tablaVehiculos.setItems(FXCollections.observableArrayList(administrador.listarTodo()));
        tablaVehiculos.refresh();
    }

    // Refresco de la vista SOLO si se filtra
    private void refrescarVistaFiltrada(ArrayList<Vehiculo> lista) {
        tablaVehiculos.setItems(FXCollections.observableArrayList(lista));
        tablaVehiculos.refresh();
    }

    // Alert
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}