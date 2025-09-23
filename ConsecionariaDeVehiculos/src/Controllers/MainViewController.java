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
import Exceptions.ErrorEnElFiltradoException;

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
    @FXML private Button btnAceptarArchivos;
    @FXML private Button btnAceptarOtros;
    
    //CHOICE BOXES
    @FXML private ChoiceBox<EstadoVehiculo> cbFiltrarEstado;
    @FXML private ChoiceBox<TipoVehiculos> cbFiltrarTipo;
    @FXML private ChoiceBox<String> cbSave;           // Solo para archivos
    @FXML private ChoiceBox<String> cbOtros;          // Para ordenamientos y demostraciones
    
    //LABELS
    @FXML private Label lblPrimerAtributo;
    @FXML private Label lblSegundoAtributo;

    // ADMINISTRADOR
    private AdministradorVehiculos administrador;

    //------------------------------------------------- INICIALIZADOR -----------------------------------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //CHOICES BOXES
        cbFiltrarEstado.getItems().addAll(EstadoVehiculo.values());
        cbFiltrarEstado.setValue(EstadoVehiculo.TODOS);

        cbFiltrarTipo.getItems().addAll(TipoVehiculos.AUTO, TipoVehiculos.CAMIONETA, TipoVehiculos.MOTO, TipoVehiculos.TODOS);
        cbFiltrarTipo.setValue(TipoVehiculos.TODOS);

        // ChoiceBox solo para ARCHIVOS
        cbSave.getItems().addAll(
                    "Guardar CSV",
                    "Cargar CSV",
                    "Guardar JSON",
                    "Cargar JSON",
                    "Exportar TXT"
        );
        cbSave.setValue("Guardar CSV");

        // ChoiceBox para ORDENAMIENTOS Y DEMOSTRACIONES
        cbOtros.getItems().addAll(
                    "Ordenar por Patente",
                    "Ordenar por Kilómetros", 
                    "Ordenar por Año",
                    "Incrementar Km (+100)",
                    "Demostrar Wildcards",
                    "Demostrar Iterator",
                    "Demostrar Ordenamientos"
        );
        cbOtros.setValue("Ordenar por Patente");

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

    // ------------------------------------------------- ARCHIVOS SOLAMENTE -------------------------------------------
    @FXML
    private void aceptarAccionArchivos(ActionEvent event) {
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
                    if (confirmarAccion("¿Desea cargar los vehículos desde CSV? Esto se combinará con los vehículos actuales.")) {
                        int cantidadAntes = administrador.listarTodo().size();
                        administrador.cargarCSV();
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
                        administrador.cargarJSON();
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
                    ArrayList<Vehiculo> vehiculosAExportar = new ArrayList<>(tablaVehiculos.getItems());
                    administrador.exportarListadoFiltradoTXT(vehiculosAExportar, "Listado de Vehículos");
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", 
                                "Archivo TXT exportado correctamente.\nVehículos exportados: " + 
                                vehiculosAExportar.size());
                    break;
                    
                default:
                    mostrarAlerta(AlertType.WARNING, "Advertencia", "Acción de archivo no válida seleccionada.");
                    break;
            }
            
        } catch (ErrorEnElFiltradoException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Filtrado", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Ocurrió un error con archivos: " + e.getMessage());
        }
    }
    
    //----------------------------------------------- ORDENAMIENTO Y WILDCARDS SOLAMENTE
    @FXML
    private void aceptarAccionOtros(ActionEvent event) {
        String accion = cbOtros.getValue();
        
        try {
            switch (accion) {
                // ORDENAMIENTOS CON COMPARABLE/COMPARATOR
                case "Ordenar por Patente":
                    ArrayList<Vehiculo> ordenadosPatente = administrador.ordenarPorCriterioNatural();
                    refrescarVistaFiltrada(ordenadosPatente);
                    mostrarAlerta(AlertType.INFORMATION, "Ordenamiento", 
                                "Vehículos ordenados por patente (criterio natural).\nTotal: " + 
                                ordenadosPatente.size());
                    break;
                    
                case "Ordenar por Kilómetros":
                    ArrayList<Vehiculo> ordenadosKm = administrador.ordenarPorKilometros();
                    refrescarVistaFiltrada(ordenadosKm);
                    mostrarAlerta(AlertType.INFORMATION, "Ordenamiento", 
                                "Vehículos ordenados por kilómetros (menor a mayor).\nTotal: " + 
                                ordenadosKm.size());
                    break;
                    
                case "Ordenar por Año":
                    ArrayList<Vehiculo> ordenadosAño = administrador.ordenarPorAño();
                    refrescarVistaFiltrada(ordenadosAño);
                    mostrarAlerta(AlertType.INFORMATION, "Ordenamiento", 
                                "Vehículos ordenados por año (más viejo a más nuevo).\nTotal: " + 
                                ordenadosAño.size());
                    break;
                    
                // ITERATOR PERSONALIZADO
                case "Incrementar Km (+100)":
                    if (confirmarAccion("¿Desea incrementar 100 km a todos los vehículos usando Iterator?")) {
                        int contador = 0;
                        for (Vehiculo vehiculo : administrador) { // Usa el Iterator personalizado
                            vehiculo.setKilometros(vehiculo.getKilometros() + 100f);
                            contador++;
                        }
                        refrescarVista();
                        mostrarAlerta(AlertType.INFORMATION, "Modificación", 
                                    "Se agregaron 100 km a " + contador + " vehículos usando Iterator personalizado.");
                    }
                    break;

                // DEMOSTRACIONES
                case "Demostrar Wildcards":
                    demostrarWildcards();
                    break;
                    
                case "Demostrar Iterator":
                    demostrarIterator();
                    break;
                    
                case "Demostrar Ordenamientos":
                    demostrarOrdenamientos();
                    break;
                    
                default:
                    mostrarAlerta(AlertType.WARNING, "Advertencia", "Acción de ordenamiento/demostración no válida seleccionada.");
                    break;
            }
            
        } catch (ErrorEnElFiltradoException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Filtrado", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Ocurrió un error en ordenamientos/demostraciones: " + e.getMessage());
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
    
    //---------------------------------------------------- METODOS DEMOSTRACION ------------------------------------------------
    // MÉTODO PARA DEMOSTRAR WILDCARDS
    private void demostrarWildcards() {
        try {
            // Filtrar solo autos usando Wildcard genérico
            ArrayList<Auto> soloAutos = administrador.filtrarPorTipo(Auto.class);
            mostrarAlerta(AlertType.INFORMATION, "Wildcards - Filtrar Autos", 
                         "Se encontraron " + soloAutos.size() + " autos usando wildcards genéricos.");
            
            // Copiar autos a otra lista usando Wildcard ? super
            ArrayList<Auto> listaAutos = new ArrayList<>();
            administrador.copiarAutosSolamente(listaAutos);
            mostrarAlerta(AlertType.INFORMATION, "Wildcards - Copiar Autos", 
                         "Se copiaron " + listaAutos.size() + " autos usando wildcard ? super Auto.");
            
        } catch (ErrorEnElFiltradoException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Wildcards", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Error en demostración de wildcards: " + e.getMessage());
        }
    }

    // MÉTODO PARA DEMOSTRAR ITERATOR
    private void demostrarIterator() {
        try {
            // Usar el método que prueba el Iterator personalizado
            String resultado = administrador.probarIterator();
            mostrarAlerta(AlertType.INFORMATION, "Iterator Personalizado", resultado);
            
            // Exportar listado usando Iterator personalizado
            administrador.mostrarTodosConIterator();
            mostrarAlerta(AlertType.INFORMATION, "Iterator - Exportación", 
                         "Se exportó un archivo TXT usando el Iterator personalizado.");
            
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Error al demostrar Iterator: " + e.getMessage());
        }
    }

    // MÉTODO PARA DEMOSTRAR ORDENAMIENTOS MÚLTIPLES
    private void demostrarOrdenamientos() {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Demostrar Ordenamientos");
            alert.setHeaderText("¿Qué ordenamiento desea probar?");
            alert.setContentText("Seleccione OK para ordenar por Estado, o Cancel para ordenar por Tipo");
            
            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Ordenar por Estado
                ArrayList<Vehiculo> porEstado = administrador.ordenarPorEstado();
                refrescarVistaFiltrada(porEstado);
                mostrarAlerta(AlertType.INFORMATION, "Ordenamiento por Estado", 
                             "Vehículos ordenados alfabéticamente por estado.");
            } else {
                // Ordenar por Tipo
                ArrayList<Vehiculo> porTipo = administrador.ordenarPorTipo();
                refrescarVistaFiltrada(porTipo);
                mostrarAlerta(AlertType.INFORMATION, "Ordenamiento por Tipo", 
                             "Vehículos ordenados alfabéticamente por tipo.");
            }
            
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Error en ordenamientos: " + e.getMessage());
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