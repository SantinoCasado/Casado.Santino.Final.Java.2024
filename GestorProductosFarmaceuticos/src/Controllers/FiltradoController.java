package Controllers;

import Interfaces.IMapAbleJson;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import Models.ProductoLimpieza;
import Utilities.JsonSave;
import java.util.ArrayList;

public class FiltradoController implements Initializable {
    @FXML
    private ListView<ProductoLimpieza> listViewFiltrados;

    @FXML
    private Button btnGuardar;

    private List<ProductoLimpieza> productosFiltrados;



    public void setProductosFiltrados(List<ProductoLimpieza> productos) {
        this.productosFiltrados = productos;
        listViewFiltrados.getItems().setAll(productos);
    }

    @FXML
    public void guardarFiltrados() {
        if (productosFiltrados == null || productosFiltrados.isEmpty()) {
            mostrarAlerta("Sin productos", "No hay productos filtrados para guardar.");
            return;
        }
        
        List<IMapAbleJson> lista = new ArrayList<>();
        lista.addAll(productosFiltrados);
        
        JsonSave.guardarProductos(lista);
       
        mostrarAlerta("Guardado exitoso", "Productos guardados en formato JSON.");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}