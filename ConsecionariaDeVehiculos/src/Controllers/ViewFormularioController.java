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
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    
    //Choice Boxes
    @FXML private ChoiceBox<TipoCombustible> cbTipoCombustible;
    @FXML private ChoiceBox<TipoVehiculos> cbTipoVehiculo;
    @FXML private ChoiceBox<String> cbMarca;
    @FXML private ChoiceBox<Integer> cbAñoFabricacion;

    //Labels
    @FXML private Label lblSegundoAtributo;

    //Text Fields
    @FXML private TextField txtPatente1;
    @FXML private TextField txtPatente2;
    @FXML private TextField txtSegundoAtributo;
    @FXML private TextField txtKilometraje;        

    // Referencias necesarias para validación
    private AdministradorVehiculos administrador;
    private int indiceVehiculo = -1; // Para saber qué vehículo se está editando
    private Vehiculo v;
    
    //------------------------------------------ INICIALIZADOR -------------------------------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializo los ChoiceBox
        this.cbTipoVehiculo.getItems().addAll(TipoVehiculos.AUTO, TipoVehiculos.CAMIONETA, TipoVehiculos.MOTO);
        this.cbTipoVehiculo.setValue(TipoVehiculos.AUTO);
        
        this.cbAñoFabricacion.getItems().addAll(2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025);
        this.cbAñoFabricacion.setValue(2025);
    }    
    
    //------------------------------------------ SETTERS & GETTERS ---------------------------------------------------------------------------------------------
    // Método para obtener el vehículo creado o modificado
    @Override
    public Vehiculo getVehiculo(){
        return this.v;
    }
    // Método para setear el vehículo a modificar
    @Override
    public void setVehiculo(Vehiculo v) {
        this.v = v;
        if (v != null) {    // Si es distinto de null, cargo los datos del vehículo en los campos
            //Datos comunes
            String patente = v.getPatente();
            cbAñoFabricacion.setValue(v.getAñoFabricacion());
            txtKilometraje.setText(String.valueOf(v.getKilometros()));
            
            cbTipoCombustible.setValue(v.getTipoCombustible());
            cbTipoVehiculo.setValue(v.getTipo());

            //Divido la patente en dos partes
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
        }else{  // Si es null, limpio los campos para un nuevo vehículo
            cbAñoFabricacion.setValue(2025);
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
        cambiandoTipo(null);
    }
    
    // Métodos para setear el administrador y el índice
    public void setAdministrador(AdministradorVehiculos administrador) {
        this.administrador = administrador;
    }
    
    // Índice del vehículo en la lista del administrador (si es edición)
    public void setIndiceVehiculo(int indice) {
        this.indiceVehiculo = indice;
    }
    
    //------------------------------------------ MÉTODOS DE LOS BOTONES ----------------------------------------------------------------------------------------
    //Método que se ejecuta al cambiar el tipo de vehículo en el ChoiceBox
    @FXML
    void cambiandoTipo(ActionEvent event) {
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
        
    //Método que se ejecuta al cambiar la marca en el ChoiceBox
    @FXML
    void cambiandoMarca(ActionEvent event) {
        String marcaSeleccionada = cbMarca.getValue();
        if (marcaSeleccionada == null) return;
        
        // Limpiar combustibles actuales
        cbTipoCombustible.getItems().clear();
        
        // Configurar combustibles según la marca
        switch (marcaSeleccionada) {
            // Marcas premium - todos los combustibles
            case "BMW", "AUDI", "MERCEDES", "DUCATI" -> {
                cbTipoCombustible.getItems().addAll(
                    TipoCombustible.NAFTA, 
                    TipoCombustible.DIESEL, 
                    TipoCombustible.ELECTRICO, 
                    TipoCombustible.HIBRIDO
                );
                cbTipoCombustible.setValue(TipoCombustible.NAFTA);
            }
            
            // Marcas eco-friendly
            case "TOYOTA", "NISSAN", "HONDA" -> {
                cbTipoCombustible.getItems().addAll(
                    TipoCombustible.NAFTA, 
                    TipoCombustible.HIBRIDO, 
                    TipoCombustible.ELECTRICO
                );
                cbTipoCombustible.setValue(TipoCombustible.HIBRIDO);
            }
            
            // Marcas comerciales/trabajo
            case "FORD", "CHEVROLET", "RENAULT", "RAM", "DODGE", "JEEP" -> {
                cbTipoCombustible.getItems().addAll(
                    TipoCombustible.NAFTA, 
                    TipoCombustible.DIESEL
                );
                cbTipoCombustible.setValue(TipoCombustible.DIESEL);
            }
            
            // Motos básicas - solo nafta
            case "MOTOMEL", "SUZUKI", "KAWASAKI", "YAMAHA" -> {
                cbTipoCombustible.getItems().add(TipoCombustible.NAFTA);
                cbTipoCombustible.setValue(TipoCombustible.NAFTA);
            }
            
            // Marcas económicas
            case "FIAT", "VOLKSWAGEN", "PEUGEOT" -> {
                cbTipoCombustible.getItems().addAll(
                    TipoCombustible.NAFTA, 
                    TipoCombustible.DIESEL
                );
                cbTipoCombustible.setValue(TipoCombustible.NAFTA);
            }
            
            // Por defecto - todos los combustibles
            default -> {
                cbTipoCombustible.getItems().addAll(
                    TipoCombustible.NAFTA, 
                    TipoCombustible.DIESEL, 
                    TipoCombustible.ELECTRICO, 
                    TipoCombustible.HIBRIDO
                );
                cbTipoCombustible.setValue(TipoCombustible.NAFTA);
            }
        }
    }
    
    //Método que se ejecuta al presionar el botón Aceptar
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
            String añoStr = String.valueOf(cbAñoFabricacion.getValue());
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

            // Validaciones específicas según el tipo
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
            
            // Parseos comunes
            float kilometraje = Float.parseFloat(valor);
            int añoFabricacion = cbAñoFabricacion.getValue();

            // CREAR VEHÍCULO TEMPORAL PARA VALIDAR PATENTE
            Vehiculo vehiculoTemporal;
            switch (tipo) {
                case AUTO -> vehiculoTemporal = new Auto(TipoVehiculos.AUTO, patenteCompleta, añoFabricacion, combustible, kilometraje, estado, MarcasAuto.valueOf(marca), numPuertas, LocalDate.now());
                case CAMIONETA -> vehiculoTemporal = new Camioneta(tipo, patenteCompleta, añoFabricacion, combustible, kilometraje, estado, MarcasCamioneta.valueOf(marca), capacidadCargaKg, LocalDate.now());
                case MOTO -> vehiculoTemporal = new Moto(tipo, patenteCompleta, añoFabricacion, combustible, kilometraje, estado, MarcasMoto.valueOf(marca), cilindrada, LocalDate.now());
                default -> throw new IllegalStateException("Tipo de vehículo no válido");
            }
            
            // VALIDAR CON EL ADMINISTRADOR ANTES DE ASIGNAR
            if (administrador != null) {// Puede ser null en pruebas unitarias
                if (indiceVehiculo == -1) { // Si es un vehículo nuevo
                    // Es un vehículo nuevo - validar si se puede agregar
                    administrador.agregar(vehiculoTemporal); // Esto lanza excepción si hay duplicados
            } else {    // Si es un vehículo existente
                // Es edición
                // Verificar que no haya otra patente duplicada (excluyendo el que estamos modificando)
                for (int j = 0; j < administrador.listarTodo().size(); j++) {   // Recorro toda la lista
                    if (j != indiceVehiculo && administrador.listarTodo().get(j).getPatente().equals(patenteCompleta)) {    // Si encuentro una patente igual en otro vehículo
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

                // Actualizar atributos específicos
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

    @FXML
    void cancelar(ActionEvent event) {
        this.cerrar();
    }

    //------------------------------------------ MÉTODOS AUXILIARES -------------------------------------------------------------------------------------------
    // Método para mostrar alertas de error
    private void mostrarAlerta(String mensaje) {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alerta.setTitle("Error de validación");
        alerta.setHeaderText("Datos inválidos");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Método que cierra el formulario
    @FXML
    private void cerrar(){
        Stage stage = (Stage)btnCancelar.getScene().getWindow();
        stage.close();
    } 
}