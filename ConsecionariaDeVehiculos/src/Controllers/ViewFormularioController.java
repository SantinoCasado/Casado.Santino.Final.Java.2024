package Controllers;

import Interfaces.IVehiculoEditable;
import Enums.*;
import static Enums.TipoVehiculos.AUTO;
import static Enums.TipoVehiculos.CAMIONETA;
import static Enums.TipoVehiculos.MOTO;
import Exceptions.DatoErroneoException;
import Exceptions.PatenteRepetidaException;
import Models.*;
import Validations.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewFormularioController implements Initializable, IVehiculoEditable {
    //Botones
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    
    //Choice Boxes
    @FXML
    private ChoiceBox<TipoCombustible> cbTipoCombustible;
    @FXML
    private ChoiceBox<TipoVehiculos> cbTipoVehiculo;
    @FXML
    private ChoiceBox<String> cbMarca;
    
    //Labels
    @FXML
    private Label lblSegundoAtributo;
    
    //Text Fields
    @FXML
    private TextField txtPatente1;
    @FXML
    private TextField txtPatente2;
    @FXML
    private TextField txtAñoFabricacion;
    @FXML
    private TextField txtSegundoAtributo;
    @FXML
    private TextField txtKilometraje;        
    
    private Vehiculo v;
    
    
   @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializo los ChoiceBox
        this.cbTipoCombustible.getItems().addAll(TipoCombustible.DIESEL, TipoCombustible.NAFTA, TipoCombustible.ELECTRICO, TipoCombustible.HIBRIDO);
        this.cbTipoCombustible.setValue(TipoCombustible.NAFTA);
        
        this.cbTipoVehiculo.getItems().addAll(TipoVehiculos.AUTO, TipoVehiculos.CAMIONETA, TipoVehiculos.MOTO);
        this.cbTipoVehiculo.setValue(TipoVehiculos.AUTO);
    }    
    
    @Override
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
              try {
                //Inicializo los atributos particulares
               EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;
               int numPuertas = 0;
               float capacidadCargaKg = 0f;
               int cilindrada = 0;

               //Obtengo los valores comunes de los dos
                TipoVehiculos tipo = cbTipoVehiculo.getValue();
                String valor = txtKilometraje.getText().trim();
                String añoStr = txtAñoFabricacion.getText().trim();
                String patenteParte1 = txtPatente1.getText().trim();
                String patenteParte2 = txtPatente2.getText().trim();
                String patenteCompleta = ((patenteParte1 + patenteParte2).trim().toUpperCase());

                TipoCombustible combustible = cbTipoCombustible.getValue();
                String marca = cbMarca.getValue();
                  
                //Validaciones comunes
                ValidadorAtributosVehiculo.validarPatenteVieja(patenteParte1, patenteParte2);
                ValidadorAtributosVehiculo.validarAñoFabricacion(añoStr);
                ValidadorAtributosVehiculo.validarTipoCombustible(combustible);
                ValidadorAtributosVehiculo.validarKilometraje(valor);
                  switch (tipo) {
                      case AUTO:
                          ValidadorAtributosAuto.validarNumPuertas(txtSegundoAtributo.getText().trim());
                          ValidadorAtributosAuto.validarMarca(marca);
                          numPuertas = Integer.parseInt(txtSegundoAtributo.getText().trim());
                          break;
                      case MOTO:
                          ValidadorAtributosMoto.validarMarca(marca);
                          ValidadorAtributosMoto.validarCilindrada(txtSegundoAtributo.getText().trim());
                          cilindrada = Integer.parseInt(txtSegundoAtributo.getText().trim());
                          break;
                      case CAMIONETA:
                          ValidadorAtributosCamioneta.validarMarca(marca);
                          ValidadorAtributosCamioneta.validarCapacidadaCarga(txtSegundoAtributo.getText().trim());
                          capacidadCargaKg = Float.parseFloat(txtSegundoAtributo.getText().trim());
                          break;
                  }
                
                float kilometraje = Float.parseFloat(valor);
                int añoFabricacion = Integer.parseInt(txtAñoFabricacion.getText().trim());

                //Si se esta editando
                if (v != null) {
                    // Actualiza atributos comunes
                    v.setEstadoVehiculo(EstadoVehiculo.DISPONIBLE);
                    v.setPatente(patenteCompleta);
                    v.setAñoFabricacion(añoFabricacion);
                    v.setTipo(tipo);
                    v.setTipoCombustible(combustible);
                    v.setKilometros(kilometraje);

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
                        }
                        case CAMIONETA -> {
                            switch(marca){
                                case "RENAULT" -> ((Camioneta) v).setMarca(MarcasCamioneta.RENAULT);
                                case "NISSAN" -> ((Camioneta) v).setMarca(MarcasCamioneta.NISSAN);
                                case "JEEP" -> ((Camioneta) v).setMarca(MarcasCamioneta.JEEP);
                                case "DODGE"  -> ((Camioneta) v).setMarca(MarcasCamioneta.DODGE);
                                case "RAM"  -> ((Camioneta) v).setMarca(MarcasCamioneta.RAM);
                        }
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
                    }
                } 
                }else{
                    // Crear nuevo objeto según el tipo
                    switch (tipo) {
                        case AUTO -> this.v = new Auto(TipoVehiculos.AUTO, patenteCompleta, añoFabricacion, combustible, kilometraje, estado, MarcasAuto.valueOf(marca), numPuertas, LocalDate.now());
                        case CAMIONETA -> this.v = new Camioneta(tipo, patenteCompleta, añoFabricacion, combustible, kilometraje, estado, MarcasCamioneta.valueOf(marca), capacidadCargaKg, LocalDate.now());
                        case MOTO -> this.v = new Moto(tipo, patenteCompleta, añoFabricacion, combustible, kilometraje, estado, MarcasMoto.valueOf(marca), cilindrada, LocalDate.now());
                    }
                }
                cerrar();
            }catch (DatoErroneoException | PatenteRepetidaException | NumberFormatException e) {
                mostrarAlerta(e.getMessage());
            }
        }
     
    private void mostrarAlerta(String mensaje) {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alerta.setTitle("Error de validación");
        alerta.setHeaderText("Datos inválidos");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
     
     @FXML
     void cancelar(ActionEvent event) {
            this.cerrar();
    }
    
     @Override
    public void setVehiculo(Vehiculo v) {
        this.v = v;
         if (v != null) {
             String patente = v.getPatente();
             txtAñoFabricacion.setText(String.valueOf(v.getAñoFabricacion()));
             txtKilometraje.setText(String.valueOf(v.getKilometros()));
             
             cbTipoCombustible.getItems().addAll(TipoCombustible.DIESEL, TipoCombustible.NAFTA, TipoCombustible.ELECTRICO, TipoCombustible.HIBRIDO);
             cbTipoCombustible.setValue(v.getTipoCombustible());
             
             cbTipoVehiculo.getItems().addAll(TipoVehiculos.AUTO, TipoVehiculos.CAMIONETA, TipoVehiculos.MOTO);
             cbTipoVehiculo.setValue(v.getTipo());

             if (patente != null && patente.length() == 6) {
                txtPatente1.setText(patente.substring(0, 3));
             }
             if (patente != null && patente.length() == 6) {
                  txtPatente2.setText(patente.substring(3, 6));
            }
             
            //Dependiendo del tipo de vehiculo
            if(v instanceof Auto auto){
                lblSegundoAtributo.setText("Cantidad de Puertas: ");
                txtSegundoAtributo.setText(String.valueOf(auto.getNumPuertas()));
                
                this.cbMarca.getItems().addAll("FORD", "CHEVROLET", "TOYOTA", "VOLKSWAGEN", "BMW", "FIAT", "RENAULT", "NISSAN", "PEUGEOT");
                this.cbMarca.setValue(String.valueOf(auto.getMarca()));
            }else  if(v instanceof Moto moto){
                lblSegundoAtributo.setText("Cilindrada: ");
                txtSegundoAtributo.setText(String.valueOf(moto.getCilindrada()));
                
                this.cbMarca.getItems().addAll("HONDA", "YAMAHA", "SUZUKI", "KAWASAKI", "BMW", "DUCATI", "MOTOMEL");
                this.cbMarca.setValue(String.valueOf(moto.getMarca()));
            }else if(v instanceof Camioneta camioneta){
                lblSegundoAtributo.setText("Capacidad de Carga: ");
                txtSegundoAtributo.setText(String.valueOf(camioneta.getCampacidadCargaKg()));
                
                this.cbMarca.getItems().addAll("RENAULT", "NISSAN", "JEEP", "DODGE", "RAM");
                this.cbMarca.setValue(String.valueOf(camioneta.getMarca()));
            }
         }else{
             txtAñoFabricacion.clear();
             txtKilometraje.clear();
             txtPatente1.clear();
             txtPatente2.clear();
             txtSegundoAtributo.clear();
             cbMarca.getSelectionModel().selectFirst();
             cbTipoCombustible.getSelectionModel().selectFirst();
             cbTipoVehiculo.getSelectionModel().selectFirst();
         }
         cambiadoTipo(null);
    }
        
     //Metodo que cierra el formulario
    @FXML
     private void cerrar(){
        Stage stage = (Stage)btnCancelar.getScene().getWindow();
        stage.close();
    } 
}