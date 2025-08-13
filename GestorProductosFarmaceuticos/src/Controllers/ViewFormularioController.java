package Controllers;

import Enums.*;
import static Enums.TipoVehiculos.AUTO;
import static Enums.TipoVehiculos.CAMIONETA;
import static Enums.TipoVehiculos.MOTO;
import Exceptions.DatoErroneoException;
import Models.Auto;
import Models.Vehiculo;
import Validations.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    
    //Text Fields
    @FXML
    private TextField txtPatente1;
    @FXML
    private TextField txtPatente2;
    @FXML
    private TextField txtAñoFabricacion;
    @FXML
    private TextField txtSegundoAtributo;    
    
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
    }
    
    @FXML
    void aceptar(ActionEvent event) {
        LocalDate fechaRenta = dpRenta.getValue();
        //Seteo los valores comunes de los dos
         TipoVehiculos tipo = cbTipoVehiculo.getValue();
         String patente = (txtPatente1.getText() + txtPatente2.getText()).toUpperCase().trim();
         int añoFabricacion = Integer.parseInt(txtAñoFabricacion.getText().trim());
         TipoCombustible combustible = cbTipoCombustible.getValue();
         float horasUsadas = this.v.obtenerHorasUso(fechaRenta);
         String marca = cbMarca.getValue();
         
         switch(cbEstadoVehiculo.getValue()){
             case "Disponible" -> {
                 EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;
             }
             case "En Uso"-> {
                 EstadoVehiculo estado = EstadoVehiculo.ALQUILADO;
             }
             case "En Mantenimiento"-> {
                 EstadoVehiculo estado = EstadoVehiculo.EN_MANTENIMIENTO;
             }
         }

          try {
            ValidadorAtributosVehiculo.validarPatente(patente);
            ValidadorAtributosVehiculo.validarAñoFabricacion(añoFabricacion);
            ValidadorAtributosVehiculo.validarTipoCombustible(combustible);
            ValidadorAtributosVehiculo.validarFecha(fechaRenta);
            //Si se esta editando
            if (v != null) {
                // Actualiza atributos comunes
                v.setPatente(patente);
                v.setAñoFabricacion(añoFabricacion);
                v.setTipo(tipo);
                v.setTipoCombustible(combustible);
                dpRenta.setValue(fechaRenta);
                
                //seteo y pareceo segun cada caso
                switch (tipo) {
                    case AUTO -> {
                        switch(marca){
                            case "FORD" -> ((Auto) v).setMarca(MarcasAuto.FORD);
                            case "CHEVROLET" -> ((Auto) v).setMarca(MarcasAuto.CHEVROLET);
                            case "TOYOTA" -> ((Auto) v).setMarca(MarcasAuto.TOYOTA);
                            case "VOLKSWAGEN"  -> ((Auto) v).setMarca(MarcasAuto.VOLKSWAGEN);
                            case "BMW"  -> ((Auto) v).setMarca(MarcasAuto.BMW);
                            case "FIAT"  -> ((Auto) v).setMarca(MarcasAuto.FIAT);
                            case "RENAULT"  -> ((Auto) v).setMarca(MarcasAuto.RENAULT);
                            case "NISSAN"  -> ((Auto) v).setMarca(MarcasAuto.NISSAN);
                            case "PEUGEOT"  -> ((Auto) v).setMarca(MarcasAuto.PEUGEOT);
                        }
                        int numPuertas = Integer.parseInt(txtSegundoAtributo.getText().trim());
                        ValidadorAtributosAuto.validarNumPuertas(numPuertas);
                        ValidadorAtributosAuto.validarMarca(marca);
                    }
                    case "SUPLEMENTO" -> {
                        String objetivo = txtObjetivo.getText().trim();
                        ValidadorProductosFarmaceuticos.validarObjetivo(objetivo);
                        ((Suplemento) producto).setObjetivo(objetivo);
                    }
                }
            } else {
                // Crear nuevo objeto según el tipo
                switch (tipo) {
                    case "MEDICAMENTO" -> {
                        boolean requiereReceta = cbReceta.isSelected();
                        this.producto = new Medicamento(requiereReceta, nombre, dosis, fechaVencimiento);
                    }
                    case "SUPLEMENTO" -> {
                        String objetivo = txtObjetivo.getText().trim();
                        this.producto = new Suplemento(objetivo, nombre, dosis, fechaVencimiento);
                    }
                }
            }
            cerrar();
        } catch (DatoErroneoException | ProductFarmaVencidoException e) {
            // Podés mostrar un Alert personalizado si querés
            System.err.println("Error: " + e.getMessage());
        }
    }
    
}