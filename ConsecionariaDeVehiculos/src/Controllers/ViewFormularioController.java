package Controllers;

import Interfaces.IVehiculoEditable;
import Enums.*;
import static Enums.TipoVehiculos.AUTO;
import static Enums.TipoVehiculos.CAMIONETA;
import static Enums.TipoVehiculos.MOTO;
import Exceptions.DatoErroneoException;
import Exceptions.PatenteRepetidaException;
import Gestor.AdministradorVehiculos;
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
    
    // Referencias necesarias para validación
    private AdministradorVehiculos administrador;
    private int indiceVehiculo = -1; // Para saber qué vehículo se está editando
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
    
    // Métodos para setear el administrador y el índice
    public void setAdministrador(AdministradorVehiculos administrador) {
        this.administrador = administrador;
    }
    
    public void setIndiceVehiculo(int indice) {
        this.indiceVehiculo = indice;
    }
    
    @FXML
    void cambiadoTipo(ActionEvent event) {
        // Limpiar y setear el cb
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
            //Inicializar atributos particulares
            EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;
            int numPuertas = 0;
            float capacidadCargaKg = 0f;
            int cilindrada = 0;

            //Obtener valores comunes
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
                    ValidadorAtributosCamioneta.validarCapacidadCarga(txtSegundoAtributo.getText().trim());
                    capacidadCargaKg = Float.parseFloat(txtSegundoAtributo.getText().trim());
                    break;
            }
            
            float kilometraje = Float.parseFloat(valor);
            int añoFabricacion = Integer.parseInt(txtAñoFabricacion.getText().trim());

            // CREAR VEHÍCULO TEMPORAL PARA VALIDAR PATENTE
            Vehiculo vehiculoTemporal;
            switch (tipo) {
                case AUTO -> vehiculoTemporal = new Auto(TipoVehiculos.AUTO, patenteCompleta, añoFabricacion, combustible, kilometraje, estado, MarcasAuto.valueOf(marca), numPuertas, LocalDate.now());
                case CAMIONETA -> vehiculoTemporal = new Camioneta(tipo, patenteCompleta, añoFabricacion, combustible, kilometraje, estado, MarcasCamioneta.valueOf(marca), capacidadCargaKg, LocalDate.now());
                case MOTO -> vehiculoTemporal = new Moto(tipo, patenteCompleta, añoFabricacion, combustible, kilometraje, estado, MarcasMoto.valueOf(marca), cilindrada, LocalDate.now());
                default -> throw new IllegalStateException("Tipo de vehículo no válido");
            }
            
            // VALIDAR CON EL ADMINISTRADOR ANTES DE ASIGNAR
            if (administrador != null) {
                if (indiceVehiculo == -1) {
                    // Es un vehículo nuevo - validar si se puede agregar
                    administrador.agregar(vehiculoTemporal); // Esto lanza excepción si hay duplicados
            } else {
                // Es edición
                // Verificar que no haya otra patente duplicada (excluyendo el que estamos modificando)
                for (int j = 0; j < administrador.listarTodo().size(); j++) {
                    if (j != indiceVehiculo && administrador.listarTodo().get(j).getPatente().equals(patenteCompleta)) {
                        throw new PatenteRepetidaException("Ya existe un vehículo con esta patente.");
                    }
                }
                // Reemplazar directamente por índice
                administrador.listarTodo().set(indiceVehiculo, vehiculoTemporal);
            }
            }

            // SI LLEGAMOS AQUÍ, NO HAY DUPLICADOS - ASIGNAR EL VEHÍCULO
            if (v != null) {
                // Actualizar vehículo existente con los nuevos datos
                v.setEstadoVehiculo(estado);
                v.setPatente(patenteCompleta);
                v.setAñoFabricacion(añoFabricacion);
                v.setTipo(tipo);
                v.setTipoCombustible(combustible);
                v.setKilometros(kilometraje);

                switch (tipo) {
                    case AUTO -> {
                        ((Auto) v).setMarca(MarcasAuto.valueOf(marca));
                        ((Auto) v).setNumPuertas(numPuertas);
                    }
                    case CAMIONETA -> {
                        ((Camioneta) v).setMarca(MarcasCamioneta.valueOf(marca));
                        ((Camioneta) v).setCampacidadCargaKg(capacidadCargaKg);
                    }
                    case MOTO -> {
                        ((Moto) v).setMarca(MarcasMoto.valueOf(marca));
                        ((Moto) v).setCilindrada(cilindrada);
                    }
                }
            } else {
                // Asignar el vehículo temporal como el vehículo final
                this.v = vehiculoTemporal;
            }
            
            cerrar();
        } catch (DatoErroneoException | PatenteRepetidaException | NumberFormatException e) {
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
            
            cbTipoCombustible.setValue(v.getTipoCombustible());
            cbTipoVehiculo.setValue(v.getTipo());

            if (patente != null && patente.length() == 6) {
                txtPatente1.setText(patente.substring(0, 3));
                txtPatente2.setText(patente.substring(3, 6));
            }
            
            //Dependiendo del tipo de vehiculo
            if(v instanceof Auto auto){
                lblSegundoAtributo.setText("Cantidad de Puertas: ");
                txtSegundoAtributo.setText(String.valueOf(auto.getNumPuertas()));
                
                this.cbMarca.getItems().addAll("FORD", "CHEVROLET", "TOYOTA", "VOLKSWAGEN", "BMW", "FIAT", "RENAULT", "NISSAN", "PEUGEOT");
                this.cbMarca.setValue(String.valueOf(auto.getMarca()));
            }else if(v instanceof Moto moto){
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
            if (cbMarca.getItems().size() > 0) {
                cbMarca.getSelectionModel().selectFirst();
            }
            cbTipoCombustible.setValue(TipoCombustible.NAFTA);
            cbTipoVehiculo.setValue(TipoVehiculos.AUTO);
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