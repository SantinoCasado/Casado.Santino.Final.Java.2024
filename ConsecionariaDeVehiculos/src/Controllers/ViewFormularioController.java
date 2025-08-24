package Controllers;

import Enums.*;
import static Enums.TipoVehiculos.AUTO;
import static Enums.TipoVehiculos.CAMIONETA;
import static Enums.TipoVehiculos.MOTO;
import Exceptions.DatoErroneoException;
import Exceptions.PatenteRepetidaException;
import Models.*;
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
import javafx.stage.Stage;

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
    
    public Vehiculo getVehiculo(){
        return this.v;
    }
    
   @FXML
    void cambiadoTipo(ActionEvent event) {
        // Clear the ChoiceBox regardless of its state
        this.cbMarca.getItems().clear();

        switch(cbTipoVehiculo.getValue()){
            case AUTO -> {
                lblSegundoAtributo.setText("Cantidad de Puertas: ");
                this.cbMarca.getItems().addAll("FORD", "CHEVROLET", "TOYOTA", "VOLKSWAGEN", "BMW", "FIAT", "RENAULT", "NISSAN", "PEUGEOT");
                this.cbMarca.setValue("FIAT");
            }

            case CAMIONETA -> {
                lblSegundoAtributo.setText("Capacidad de Carga: ");
                this.cbMarca.getItems().addAll("RENAULT", "NISSAN", "JEEP", "DODGE", "RAM");
                this.cbMarca.setValue("RAM");
            }
            case MOTO -> {
                lblSegundoAtributo.setText("Cilindrada: ");
                this.cbMarca.getItems().addAll("HONDA", "YAMAHA", "SUZUKI", "KAWASAKI", "BMW", "DUCATI", "MOTOMEL");
                this.cbMarca.setValue("HONDA");
            }
        }
}
    
        @FXML
        void aceptar(ActionEvent event) {
            //Inicializo los atributos particulares
            EstadoVehiculo estado = null;
            int numPuertas = 0;
            float capacidadCargaKg = 0f;
            int cilindrada = 0;

            //Seteo los valores comunes de los dos
            LocalDate fechaRenta = this.getFechaRenta();
             TipoVehiculos tipo = cbTipoVehiculo.getValue();

             String patenteParte1 = txtPatente1.getText().trim();
             String patenteParte2 = txtPatente2.getText().trim();
             String patenteCompleta = ((patenteParte1 + patenteParte2).trim().toUpperCase());

             int añoFabricacion = Integer.parseInt(txtAñoFabricacion.getText().trim());
             TipoCombustible combustible = cbTipoCombustible.getValue();
             String marca = cbMarca.getValue();

             //ESTADO
             switch(cbEstadoVehiculo.getValue()){
                 case "Disponible" -> {
                     estado = EstadoVehiculo.DISPONIBLE;
                 }
                 case "En Uso"-> {
                     estado = EstadoVehiculo.ALQUILADO;
                 }
                 case "En Mantenimiento"-> {
                     estado = EstadoVehiculo.EN_MANTENIMIENTO;
                 }
             }

              try {
                //Validaciones comunes
               // ValidadorAtributosVehiculo.validarPatenteVieja(patenteParte1, patenteParte2);
                //ValidadorAtributosVehiculo.validarAñoFabricacion(añoFabricacion);
                //ValidadorAtributosVehiculo.validarTipoCombustible(combustible);
                //ValidadorAtributosVehiculo.validarFecha(fechaRenta);

                //Si se esta editando
                if (v != null) {
                    // Actualiza atributos comunes
                    v.setPatente(patenteCompleta);
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
                            numPuertas = Integer.parseInt(txtSegundoAtributo.getText().trim());
                            ValidadorAtributosAuto.validarNumPuertas(numPuertas);
                            ValidadorAtributosAuto.validarMarca(marca); 
                        }
                        case CAMIONETA -> {
                            switch(marca){
                                case "RENAULT" -> ((Camioneta) v).setMarca(MarcasCamioneta.RENAULT);
                                case "NISSAN" -> ((Camioneta) v).setMarca(MarcasCamioneta.NISSAN);
                                case "JEEP" -> ((Camioneta) v).setMarca(MarcasCamioneta.JEEP);
                                case "DODGE"  -> ((Camioneta) v).setMarca(MarcasCamioneta.DODGE);
                                case "RAM"  -> ((Camioneta) v).setMarca(MarcasCamioneta.RAM);
                        }
                            ValidadorAtributosCamioneta.validarMarca(marca);
                            ValidadorAtributosCamioneta.validarCapacidadaCarga(txtSegundoAtributo.getText().trim());
                            capacidadCargaKg = Float.parseFloat(txtSegundoAtributo.getText().trim());
                    }
                    case MOTO -> {
                            switch(marca){
                                case "HONDA" -> ((Moto) v).setMarca(MarcasMoto.HONDA);
                                case "YAMAHA" -> ((Moto) v).setMarca(MarcasMoto.YAMAHA);
                                case "SUZUKI" -> ((Moto) v).setMarca(MarcasMoto.SUZUKI);
                                case "KAWASAKI" -> ((Moto) v).setMarca(MarcasMoto.KAWASAKI);
                                case "BMW" -> ((Moto) v).setMarca(MarcasMoto.BMW);
                                case "DUCATI" -> ((Moto) v).setMarca(MarcasMoto.DUCATI);
                                case "MOTOMEL" -> ((Moto) v).setMarca(MarcasMoto.MOTOMEL);
                        }
                            ValidadorAtributosMoto.validarMarca(marca);
                            ValidadorAtributosMoto.validarCilindrada(txtSegundoAtributo.getText().trim());
                            cilindrada = Integer.parseInt(txtSegundoAtributo.getText().trim());
                    }
                } 
                }else{
                    // Crear nuevo objeto según el tipo
                    switch (tipo) {
                        case AUTO -> this.v = new Auto(TipoVehiculos.AUTO, patenteCompleta, añoFabricacion, combustible, 0, estado, MarcasAuto.valueOf(marca), numPuertas);
                        case CAMIONETA -> this.v = new Camioneta(tipo, patenteCompleta, añoFabricacion, combustible, 0, estado, MarcasCamioneta.valueOf(marca), capacidadCargaKg);
                        case MOTO -> this.v = new Moto(tipo, patenteCompleta, añoFabricacion, combustible, 0, estado, MarcasMoto.valueOf(marca), cilindrada);
                    }
                }
                cerrar();
                float horasUsadas = this.v.obtenerHorasUso(fechaRenta);
            }catch (DatoErroneoException | PatenteRepetidaException | NumberFormatException e) {
                // Podés mostrar un Alert personalizado si querés
                System.err.println("Error: " + e.getMessage());
            }
        }
    
        @FXML
        void cancelar(ActionEvent event) {
            this.cerrar();
        }
    
        public void setVehiculo(Vehiculo v) {
    }
    
    public LocalDate getFechaRenta(){
        return dpRenta.getValue();
    }
    
        
     //Metodo que cierra el formulario
    @FXML
     private void cerrar(){
        Stage stage = (Stage)btnCancelar.getScene().getWindow();
        stage.close();
    } 
}