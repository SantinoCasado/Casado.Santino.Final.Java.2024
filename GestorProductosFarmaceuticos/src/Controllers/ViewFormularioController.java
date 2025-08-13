package Controllers;

import Enums.TipoCombustible;
import Enums.TipoVehiculos;
import Models.Vehiculo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class ViewFormularioController implements Initializable {
    //Botones
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    
    //Choice Boxes
    @FXML
    private ChoiceBox<String> cbEstadoVehiculo;
    @FXML
    private ChoiceBox<TipoCombustible> cbTipoCombustible;
    @FXML
    private ChoiceBox<TipoVehiculos> cbTipoVehiculo;
    @FXML
    private ChoiceBox<String> cbMarca;
    
    //Date picker
    @FXML
    private DatePicker dpRenta;
    
    //Labels
    @FXML
    private Label lblSegundoAtributo;
    @FXML
    private Label lblTiempoRenta;
    
    private Vehiculo v;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializo los ChoiceBox
        this.cbEstadoVehiculo.getItems().addAll("Disponible", "En Uso", "En Matenimiento");
        this.cbEstadoVehiculo.setValue("Disponible");
        
        this.cbTipoCombustible.getItems().addAll(TipoCombustible.DIESEL, TipoCombustible.NAFTA, TipoCombustible.ELECTRICO, TipoCombustible.HIBRIDO);
        this.cbTipoCombustible.setValue(TipoCombustible.NAFTA);
        
        this.cbTipoVehiculo.getItems().addAll(TipoVehiculos.AUTO, TipoVehiculos.CAMIONETA, TipoVehiculos.MOTO);
        this.cbTipoVehiculo.setValue(TipoVehiculos.AUTO);
    }    

    @FXML
    void cambiadoTipo(ActionEvent event) {
        //Dependiento del valor del choiseBox seteo un valor al label del datoExtra
        switch(cbTipoVehiculo.getValue()){
            
            case AUTO -> {
                // Ocultar campos de objetivo
                this.cbMarca.getItems().addAll("FORD", "CHEVROLET",  "TOYOTA", "VOLKSWAGEN", "BMW",  "FIAT", "RENAULT", "NISSAN", "PEUGEOT");
                this.cbMarca.setValue("FIAT");
                lblSegundoAtributo.setText("Cantidad de Puertas: ");
            }
            
            case CAMIONETA -> {
                // Mostrar campos de objetivo
                this.cbMarca.getItems().addAll("RENAULT", "NISSAN", "JEEP", "DODGE", "RAM");
                this.cbMarca.setValue("RAM");
                lblSegundoAtributo.setText("Capacidad de Carga: ");
                
            }
            case MOTO -> {
                // Ocultar campos de objetivo
                this.cbMarca.getItems().addAll("HONDA",  "YAMAHA", "SUZUKI", "KAWASAKI", "BMW", "DUCATI", "MOTOMEL");
                this.cbMarca.setValue("HONDA");
                lblSegundoAtributo.setText("Cilindrada: ");
            }
        }
        
        public void setProducto(Vehiculo v){
            this.producto = producto;

            if (producto != null) {
                // Campos comunes
                txtNombre.setText(producto.getNombreComercial());
                txtDosis.setText(producto.getDosis());
                dpFechaVencimiento.setValue(producto.getFechaVencimiento());

                if (producto instanceof Medicamento medicamento) {
                    cbTipoProd.setValue("MEDICAMENTO");
                    cbReceta.setSelected(medicamento.isRequerimientoRecetaMediica());
                } else if (producto instanceof Suplemento suplemento) {
                    cbTipoProd.setValue("SUPLEMENTO");
                    txtObjetivo.setText(suplemento.getObjetivo());
                }
            } else {
                // Si es nuevo, limpiar campos
                txtNombre.clear();
                txtDosis.clear();
                dpFechaVencimiento.setValue(null);
                txtObjetivo.clear();
                cbReceta.setSelected(false);
                cbTipoProd.setValue("MEDICAMENTO");
            }

        // Aplicar visibilidad según el tipo seleccionado
        cambiadoTipo(null);
    }
    
    public Vehiculo getProductoFarmaceutico(){
        return this.v;
    }
       
}
