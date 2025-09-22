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

        cbSave.getItems().addAll(                                                                                                   // Archivos
                    "Guardar CSV",
                    "Cargar CSV",
                    "Cargar CSV (Merge)",
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
        try {
            AbrirView(null, "Formulario");
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Error al agregar vehículo: " + e.getMessage());
        }
    }

    // ELIMINAR
    @FXML
    void eliminar(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmar eliminación");
            alerta.setHeaderText("¿Estás seguro de eliminar este vehículo? Este sera eliminado de todos los archivos tambien!");
            alerta.setContentText(seleccionado.mostrarDetalles());

            Optional<ButtonType> resultado = alerta.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    administrador.eliminar(seleccionado);
                    refrescarVista();
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Vehículo eliminado correctamente.");
                } catch (Exception e) {
                    mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar vehículo: " + e.getMessage());
                }
            }
        } else {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Debe seleccionar un vehículo para eliminar.");
        }
    }

    // MODIFICAR
    @FXML
    void modificar(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                AbrirView(seleccionado, "Formulario");
            } catch (Exception e) {
                mostrarAlerta(AlertType.ERROR, "Error", "Error al modificar vehículo: " + e.getMessage());
            }
        } else {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Debe seleccionar un vehículo para modificar.");
        }
    }

    // ------------------------------------------------ CAMBIO DE ESTADO -------------------------------
    @FXML
    public void cambiarEstado(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                AbrirView(seleccionado, "EstadoVehiculo");
            } catch (Exception e) {
                mostrarAlerta(AlertType.ERROR, "Error", "Error al cambiar estado: " + e.getMessage());
            }
        } else {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Debe seleccionar un vehículo para cambiar estado.");
        }
    }

    // ------------------------------------------------- ARCHIVOS -------------------------------------------
    @FXML
    private void aceptarAccion(ActionEvent event) {
        String accion = cbSave.getValue();
        
        try {
            switch (accion) {
                case "Guardar CSV":
                    if (administrador.listarTodo().isEmpty()) {
                        mostrarAlerta(AlertType.WARNING, "Advertencia", "No hay vehículos para guardar en CSV.");
                        return;
                    }
                    administrador.guardarCSV();
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", 
                                 "Archivo CSV guardado correctamente.\nTotal vehículos guardados: " + 
                                 administrador.listarTodo().size());
                    break;
                    
                case "Cargar CSV":
                    if (confirmarAccion("¿Desea cargar los vehículos desde CSV? Esto reemplazará todos los vehículos actuales.")) {
                        administrador.cargarCSV();
                        refrescarVista();
                        mostrarAlerta(AlertType.INFORMATION, "Éxito", 
                                     "Archivo CSV cargado correctamente.\nTotal vehículos cargados: " + 
                                     administrador.listarTodo().size());
                    }
                    break;
                    
                case "Cargar CSV (Merge)":
                    if (confirmarAccion("¿Desea cargar los vehículos desde CSV? Esto se combinará con los vehículos actuales.")) {
                        int cantidadAntes = administrador.listarTodo().size();
                        administrador.cargarYMergeCSV();
                        refrescarVista();
                        int cantidadDespues = administrador.listarTodo().size();
                        mostrarAlerta(AlertType.INFORMATION, "Éxito", 
                                     "Archivo CSV cargado y combinado correctamente.\n" +
                                     "Vehículos antes: " + cantidadAntes + "\n" +
                                     "Vehículos después: " + cantidadDespues);
                    }
                    break;
                    
                case "Guardar JSON":
                    if (administrador.listarTodo().isEmpty()) {
                        mostrarAlerta(AlertType.WARNING, "Advertencia", "No hay vehículos para guardar en JSON.");
                        return;
                    }
                    administrador.guardarJSON();
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", 
                                 "Archivo JSON guardado correctamente.\nTotal vehículos guardados: " + 
                                 administrador.listarTodo().size());
                    break;
                    
                case "Cargar JSON":
                    if (confirmarAccion("¿Desea cargar los vehículos desde JSON? Esto se combinará con los vehículos actuales.")) {
                        int cantidadAntes = administrador.listarTodo().size();
                        administrador.cargarYMergeJSON(); // Usar método que hace merge
                        refrescarVista();
                        int cantidadDespues = administrador.listarTodo().size();
                        mostrarAlerta(AlertType.INFORMATION, "Éxito", 
                                     "Archivo JSON cargado y combinado correctamente.\n" +
                                     "Vehículos antes: " + cantidadAntes + "\n" +
                                     "Vehículos después: " + cantidadDespues);
                    }
                    break;
                    
                case "Exportar TXT":
                    if (administrador.listarTodo().isEmpty()) {
                        mostrarAlerta(AlertType.WARNING, "Advertencia", "No hay vehículos para exportar.");
                        return;
                    }
                    // Exportar los vehículos actualmente mostrados (respeta filtros)
                    ArrayList<Vehiculo> vehiculosAExportar = new ArrayList<>(tablaVehiculos.getItems());
                    administrador.exportarListadoFiltradoTXT(vehiculosAExportar, "Listado de Vehículos");
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", 
                                 "Archivo TXT exportado correctamente.\nVehículos exportados: " + 
                                 vehiculosAExportar.size());
                    break;
                    
                default:
                    mostrarAlerta(AlertType.WARNING, "Advertencia", "Acción no válida seleccionada.");
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
            mostrarAlerta(AlertType.INFORMATION, "Filtro aplicado", "Se encontraron " + filtrados.size() + " vehículos que coinciden con los criterios.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.ERROR, "Error", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Error al filtrar: " + e.getMessage());
        }
    }

    // ------------------------------------------------- METODOS PARTICULARES -----------------------------------------------------------------------------------
    // Apertura de views
    private void AbrirView(Vehiculo v, String name) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/view" + name + ".fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);

            if (name.equals("Formulario")) {
                ViewFormularioController cfc = loader.getController();
                
                // Pasar el administrador y configurar índice
                cfc.setAdministrador(this.administrador);
                if (v == null) {
                    cfc.setIndiceVehiculo(-1); // Nuevo vehículo
                } else {
                    int indice = administrador.listarTodo().indexOf(v);
                    cfc.setIndiceVehiculo(indice); // Vehículo existente
                }
                
                cfc.setVehiculo(v);
                stage.showAndWait();
            }

            if (name.equals("EstadoVehiculo")) {
                ViewEstadoVehiculoController cevc = loader.getController();
                cevc.setVehiculo(v);
                stage.showAndWait();
                Vehiculo resultado = cevc.getVehiculo();
                if (resultado != null) {
                    try {
                        administrador.modificar(resultado);
                        mostrarAlerta(AlertType.INFORMATION, "Éxito", "Estado del vehículo cambiado correctamente.");
                    } catch (Exception e) {
                        mostrarAlerta(AlertType.ERROR, "Error", "Error al cambiar estado: " + e.getMessage());
                    }
                }
            }

            refrescarVista();

        } catch (IOException e) {
            throw new Exception("Error al abrir la vista: " + e.getMessage(), e);
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

    // Método para confirmar acciones
    private boolean confirmarAccion(String mensaje) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }
}