package Controllers;

import Controllers.ViewEstadoVehiculoController;
import Controllers.ViewFormularioController;
import Enums.EstadoVehiculo;
import Enums.TipoVehiculos;
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
import Utilities.CsvUtilities;
import Utilities.JsonUtilities;

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
            mostrarAlerta(AlertType.ERROR, "Error", "Error al agregar", "Error al agregar vehículo: " + e.getMessage());
        }
    }

    // ELIMINAR
    @FXML
    void eliminar(ActionEvent event) {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            
            // PRIMERA CONFIRMACIÓN - Solo eliminar de memoria
            Alert primeraAlerta = new Alert(Alert.AlertType.CONFIRMATION);
            primeraAlerta.setTitle("Confirmar eliminación");
            primeraAlerta.setHeaderText("¿Estás seguro de eliminar este vehículo de la aplicación?");
            primeraAlerta.setContentText("Vehículo a eliminar:\n" + seleccionado.mostrarDetalles());

            Optional<ButtonType> primerResultado = primeraAlerta.showAndWait();
            if (primerResultado.isPresent() && primerResultado.get() == ButtonType.OK) {
                
                try {
                    // Eliminar de memoria
                    administrador.eliminar(seleccionado);
                    refrescarVista();
                    
                    // SEGUNDA CONFIRMACIÓN - Eliminar de archivos
                    Alert segundaAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                    segundaAlerta.setTitle("Eliminar de archivos");
                    segundaAlerta.setHeaderText("¿Desea también eliminar este vehículo de los archivos CSV y JSON?");
                    segundaAlerta.setContentText("Patente: " + seleccionado.getPatente() + 
                                            "\n\nEsto buscará y eliminará el vehículo de:\n" +
                                            "• vehiculos.csv\n" + 
                                            "• vehiculos.json");
                    
                    Optional<ButtonType> segundoResultado = segundaAlerta.showAndWait();
                    if (segundoResultado.isPresent() && segundoResultado.get() == ButtonType.OK) {
                        
                        // Intentar eliminar de ambos archivos, capturando errores individualmente
                        boolean eliminadoCSV = false;
                        boolean eliminadoJSON = false;
                        
                        // Eliminar de CSV
                        try {
                            eliminadoCSV = CsvUtilities.eliminarVehiculoCSV(seleccionado.getPatente());
                        } catch (Exception e) {
                            System.err.println("Error al eliminar de CSV: " + e.getMessage());
                        }
                        
                        // Eliminar de JSON
                        try {
                            eliminadoJSON = JsonUtilities.eliminarVehiculoJSON(seleccionado.getPatente());
                        } catch (Exception e) {
                            System.err.println("Error al eliminar de JSON: " + e.getMessage());
                        }
                        
                        // Mostrar resultado de eliminación de archivos
                        StringBuilder mensajeResultado = new StringBuilder();
                        mensajeResultado.append("Vehículo eliminado de la aplicación correctamente.\n\n");

                        mensajeResultado.append("• CSV: ").append(eliminadoCSV ? "Eliminado" : "No encontrado o error").append("\n");
                        mensajeResultado.append("• JSON: ").append(eliminadoJSON ? "Eliminado" : "No encontrado o error");
                        
                        AlertType tipoAlerta = (eliminadoCSV || eliminadoJSON) ? AlertType.INFORMATION : AlertType.WARNING;
                        mostrarAlerta(tipoAlerta, "Eliminación completada", "Resultado de la eliminación", mensajeResultado.toString());         
                    } else {
                        // Solo se eliminó de memoria
                        mostrarAlerta(AlertType.INFORMATION, "Eliminación parcial", "Solo memoria", "Vehículo eliminado de la aplicación.\nLos archivos CSV y JSON no fueron modificados.");
                    }
                } catch (Exception e) {
                    mostrarAlerta(AlertType.ERROR, "Error", "Eliminacion de vehículo", e.getMessage());
                }
            }
        } else {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Seleccion de vehiculo" ,"Debe seleccionar un vehículo para eliminar.");
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
                mostrarAlerta(AlertType.ERROR, "Error", "Modificacion de vehículo", e.getMessage());
            }
        } else {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Seleccion de vehiculo" , "Debe seleccionar un vehículo para modificar.");
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
                mostrarAlerta(AlertType.ERROR, "Error", "Cambio de estado", e.getMessage());
            }
        } else {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Seleccion de vehiculo" , "Debe seleccionar un vehículo para cambiar estado.");
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
                        mostrarAlerta(AlertType.WARNING, "Advertencia", "Guardado de archivo CSV" , "No hay vehículos para guardar en CSV.");
                        return;
                    }
                    administrador.guardarCSV();
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Guardado de archivo CSV", "Total vehículos guardados: " + administrador.listarTodo().size());
                    break;
                    
                case "Cargar CSV":
                    if (confirmarAccion("¿Desea cargar los vehículos desde CSV? Esto se combinará con los vehículos actuales.")) {
                        int cantidadAntes = administrador.listarTodo().size();
                        administrador.cargarCSV();
                        refrescarVista();
                        int cantidadDespues = administrador.listarTodo().size();
                        mostrarAlerta(AlertType.INFORMATION, "Éxito", "Carga de datos archivo CSV",
                                    "Vehículos antes: " + cantidadAntes + "\n" +
                                    "Vehículos después: " + cantidadDespues);
                    }
                    break;
                    
                case "Guardar JSON":
                    if (administrador.listarTodo().isEmpty()) {
                        mostrarAlerta(AlertType.WARNING, "Advertencia", "Guardado de archivos JSON" , "No hay vehículos para guardar en JSON.");
                        return;
                    }
                    administrador.guardarJSON();
                    mostrarAlerta(AlertType.INFORMATION, "Éxito",  "Guardado de archivos JSON", "Total vehículos guardados: " + administrador.listarTodo().size());
                    break;
                    
                case "Cargar JSON":
                    if (confirmarAccion("¿Desea cargar los vehículos desde JSON? Esto se combinará con los vehículos actuales.")) {
                        int cantidadAntes = administrador.listarTodo().size();
                        administrador.cargarJSON();
                        refrescarVista();
                        int cantidadDespues = administrador.listarTodo().size();
                        mostrarAlerta(AlertType.INFORMATION, "Éxito", "Carga de archivo JSON", 
                                    "Vehículos antes: " + cantidadAntes + "\n" + 
                                    "Vehículos después: " + cantidadDespues);
                    }
                    break;
                    
                case "Exportar TXT":
                    if (administrador.listarTodo().isEmpty()) {
                        mostrarAlerta(AlertType.WARNING, "Advertencia", "Exportacion de datos" , "No hay vehículos para exportar.");
                        return;
                    }
                    ArrayList<Vehiculo> vehiculosAExportar = new ArrayList<>(tablaVehiculos.getItems());
                    administrador.exportarListadoFiltradoTXT(vehiculosAExportar, "Listado de Vehículos");
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Archivo TXT exportado", "Vehículos exportados: " +
                                vehiculosAExportar.size());
                    break;
                    
                default:
                    mostrarAlerta(AlertType.WARNING, "Advertencia", "Acciones de archivos" , "Acción de archivo no válida seleccionada.");
                    break;
            }
            
        } catch (ErrorEnElFiltradoException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Filtrado de datos", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Archivos de Dato",  e.getMessage());
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
                    mostrarAlerta(AlertType.INFORMATION, "Ordenamiento",  "Vehículos ordenados por patente (criterio natural)", "Total: " + ordenadosPatente.size());
                    break;
                    
                case "Ordenar por Kilómetros":
                    ArrayList<Vehiculo> ordenadosKm = administrador.ordenarPorKilometros();
                    refrescarVistaFiltrada(ordenadosKm);
                    mostrarAlerta(AlertType.INFORMATION, "Exito" , "Ordenamiento", "Vehículos ordenados por kilómetros (menor a mayor).\nTotal: " + 
                                ordenadosKm.size());
                    break;
                    
                case "Ordenar por Año":
                    ArrayList<Vehiculo> ordenadosAño = administrador.ordenarPorAño();
                    refrescarVistaFiltrada(ordenadosAño);
                    mostrarAlerta(AlertType.INFORMATION, "Exito" , "Ordenamiento", "Vehículos ordenados por año (más viejo a más nuevo).\nTotal: " + 
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
                        mostrarAlerta(AlertType.INFORMATION, "Exito" , "Modificación",  "Se agregaron 100 km a " + contador + " vehículos usando Iterator personalizado.");
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
                    mostrarAlerta(AlertType.WARNING, "Advertencia", "Ordenamiento", "Acción de ordenamiento/demostración no válida seleccionada.");
                    break;
            }
            
        } catch (ErrorEnElFiltradoException e) {
            mostrarAlerta(AlertType.ERROR,"Error" ,"Filtrado de datos", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Ordenamientos/Demostraciones", e.getMessage());
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
            mostrarAlerta(AlertType.INFORMATION, "Exito" , "Filtro aplicado", "Se encontraron " + filtrados.size() + " vehículos que coinciden con los criterios.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Datos Invalidos" ,e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Filtro de datos",e.getMessage());
        }
    }
    
    //---------------------------------------------------- METODOS DEMOSTRACION ------------------------------------------------
    // MÉTODO PARA DEMOSTRAR WILDCARDS
    private void demostrarWildcards() {
        try {
            // Filtrar solo autos usando Wildcard genérico
            ArrayList<Auto> soloAutos = administrador.filtrarPorTipo(Auto.class);
            mostrarAlerta(AlertType.INFORMATION, "Exito" ,"Wildcards - Filtrar Autos", 
                         "Se encontraron " + soloAutos.size() + " autos usando wildcards genéricos.");
            
            // Copiar autos a otra lista usando Wildcard ? super
            ArrayList<Auto> listaAutos = new ArrayList<>();
            administrador.copiarAutosSolamente(listaAutos);
            mostrarAlerta(AlertType.INFORMATION, "Exito" , "Wildcards - Copiar Autos", 
                         "Se copiaron " + listaAutos.size() + " autos usando wildcard ? super Auto.");
            
        } catch (ErrorEnElFiltradoException e) {
            mostrarAlerta(AlertType.ERROR, "Error" ,"Wildcards", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Demostración de wildcards", e.getMessage());
        }
    }

    // MÉTODO PARA DEMOSTRAR ITERATOR
    private void demostrarIterator() {
        try {
            // Usar el método que prueba el Iterator personalizado
            String resultado = administrador.probarIterator();
            mostrarAlerta(AlertType.INFORMATION, "Exito" , "Iterator Personalizado", resultado);
            
            // Exportar listado usando Iterator personalizado
            administrador.mostrarTodosConIterator();
            mostrarAlerta(AlertType.INFORMATION, "Exito" , "Iterator - Exportación", 
                         "Se exportó un archivo TXT usando el Iterator personalizado.");
            
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Demostracion de Iterator", e.getMessage());
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
                mostrarAlerta(AlertType.INFORMATION, "Exito" , "Ordenamiento por Estado", 
                             "Vehículos ordenados alfabéticamente por estado.");
            } else {
                // Ordenar por Tipo
                ArrayList<Vehiculo> porTipo = administrador.ordenarPorTipo();
                refrescarVistaFiltrada(porTipo);
                mostrarAlerta(AlertType.INFORMATION, "Exito" , "Ordenamiento por Tipo", 
                             "Vehículos ordenados alfabéticamente por tipo.");
            }
            
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Ordenamientos de datos", e.getMessage());
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
                        mostrarAlerta(AlertType.INFORMATION, "Éxito", "Cambio de estado del vehículo", "Cambio de estado aplicado correctamente.");
                    } catch (Exception e) {
                        mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Cambio de estado", e.getMessage());
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
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje, String texto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.setContentText(texto);
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