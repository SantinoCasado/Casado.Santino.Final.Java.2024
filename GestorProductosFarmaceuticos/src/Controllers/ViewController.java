package Controllers;

import Exceptions.ProductoLimpiezaRepetidoException;
import Interfaces.ISerializableCsv;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import Utilities.CsvUtilisGeneric;
import Gestor.gestorTienda;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import Models.*;


public class ViewController implements Initializable {

    @FXML
    private ListView<ProductoLimpieza> ListViewProductosFarmaceuticos;
    
     //Lista de personas con sus datos para poner en el visor despues
    private List<ProductoLimpieza> listaProductos;
    
    private gestorTienda farmacia;
    
    private CsvUtilisGeneric<ISerializableCsv> herramientaCsv;
    
    //Botones
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnFiltrar;
    
    //ChoiceBox

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         farmacia = new gestorTienda();
         herramientaCsv = new CsvUtilisGeneric<>();
         this.LeerArchivo();
        
    }    
    
    //Metodos
    @FXML
    void agregar(ActionEvent event) {
        this.AbrirFormulario(null);
    }
    
    public static void main (String[] args){
            Application.launch();
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    void eliminar(ActionEvent event) {
         //Chequeo que se haya seleccionado algo en mi listView
        ProductoLimpieza pf = this.ListViewProductosFarmaceuticos.getSelectionModel().getSelectedItem();
        
        if (pf != null) {
            //Muestro una alerta de confirmacion
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Confirmar eliminacion");
            alerta.setHeaderText("Estas seguro de eliminar estos datos?");
            alerta.setContentText(pf.toString());            
            
            Optional<ButtonType> resultado = alerta.showAndWait();
            
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {    //Verifico que alla un resultado y que sea "OK"
                farmacia.Eliminar(pf);
            }
            this.refrescarVista();
        }        
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    void modificar(ActionEvent event) {
         ProductoLimpieza producto = this.ListViewProductosFarmaceuticos.getSelectionModel().getSelectedItem();

         if(producto != null) {
             this.AbrirFormulario(producto);
         }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    void filtrar(ActionEvent event) {
        try {
            //Cargo la scena
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Filtrado.fxml"));
            Scene scene = new Scene(loader.load());
            
            //Llamo a su controlador
            FiltradoController controlador = loader.getController();
            //Listo los productos filtrados
            List<ProductoLimpieza> productosFiltrados = farmacia.FiltrarProductoFarmaceutico(60);
            controlador.setProductosFiltrados(productosFiltrados);

            // Lógica de filtrado y conversión a texto
            List<String> lineasAGuardar = new ArrayList<>();
            LocalDate hoy = LocalDate.now();

            for (ProductoLimpieza producto : productosFiltrados) {
                    //Establezco su fecha de vencimiento
                    LocalDate vencimiento = producto.getFechaVencimiento();
 
                    if (vencimiento != null && //Si no es null, no anterior a la de hoy y no es posterior a los 30 dias
                        !vencimiento.isBefore(hoy) &&
                        !vencimiento.isAfter(hoy.plusDays(60))) {       //son 60 no 30

                        lineasAGuardar.add(producto.toString());
                    }
            }


        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Productos próximos a vencer");
        stage.setScene(scene);
        stage.showAndWait();

    } catch (IOException e) {
        System.out.println("Error al cargar el formulario de filtrado: " + e.getMessage());
    }
}
    
    public void refrescarVista(){
        this.ListViewProductosFarmaceuticos.getItems().clear();
        this.ListViewProductosFarmaceuticos.getItems().addAll(farmacia.Listar());
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     private void AbrirFormulario(ProductoLimpieza producto) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Formulario.fxml"));
    
            Scene scene = new Scene(loader.load());
            FormularioController controlador = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);               
            controlador.setProducto(producto);      
            stage.showAndWait();
            ProductoLimpieza resultado = controlador.getProductoLimpieza();    
   
                if (resultado != null) {                         
                    if (producto == null) {       
                        this.farmacia.Agregar(resultado);
                    } else {
                        this.farmacia.Modificar(resultado);
                    }
                }   
               this.refrescarVista();
        }
        catch(IOException | ProductoLimpiezaRepetidoException e){
            mostrarAlertaError(e.getMessage());

        }
    }
     
     
     public void GuardarArchivo() {
        ArrayList<ISerializableCsv> data = new ArrayList<>();
        
        for (ProductoLimpieza item : this.farmacia.Listar()) {
            System.out.println("Producto encontrado: " + item);
            if (item instanceof ISerializableCsv producto) {
                System.out.println("Agregado a CSV: " + producto.toCSV());      //Uso propio
                data.add(producto);
            } else {
                System.out.println("No implementa ISerializableCsv: " + item.getClass().getSimpleName());
            }
        }
        System.out.println("Total a escribir: " + data.size());
        herramientaCsv.escribirCSV(data);
}
    
     
     public void LeerArchivo() {
        String archivoCsv = "src/DatosFormularios/datos.csv";
        ArrayList<ProductoLimpieza> productos = new ArrayList<>();
        ArrayList<String> resultado = herramientaCsv.leerCSV(archivoCsv);      // Llama a leerCSV("datos.csv"), que devuelve las líneas del archivo como String.

        for (String item : resultado ) {
            if(item == null || item.isEmpty()) {        //Si está vacía o es null, la salta.
                continue;
            }
            String[] result = item.split(",");          //La separa en campos con split(",").
            ProductoLimpieza producto = null;
            
            if (item.contains("ProductoQuimico")) {
                producto = new ProductoQuimico().fromCSV(item);
            } 
            if (item.contains("ProductoEcologico")) {
                producto = new ProductoEcologico().fromCSV(item);
            }
            
            if (producto != null) {             
                productos.add(producto);
            }
        }
        //Crea una nueva instancia de Estacionamiento (esto reinicia su estado) y le agrega todos los vehículos reconstruidos desde el archivo.
        farmacia = new gestorTienda();
        farmacia.Listar().addAll(productos);
        this.refrescarVista();
    }
     
    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Datos inválidos");
        alerta.setHeaderText("Erro al cargar producto!");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
  }
    
