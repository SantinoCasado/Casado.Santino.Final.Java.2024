package Controllers;

import Enums.Advertencia;
import Enums.EtiquetaEcologica;
import Exceptions.DatoErroneoException;
import Exceptions.ProductoVencidoException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import Models.*;
import Validaciones.ValidadorProductosLimpieza;

public class FormularioController implements Initializable {
    // Botones
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;

    // Campos de entrada
    @FXML private TextField txtNombre;
    @FXML private TextField txtConcentracion;
    @FXML private DatePicker dpFechaVencimiento;
    @FXML private TextField txtObjetivo;

    // ChoiceBox y CheckBox
    @FXML private ChoiceBox<String> cbTipoProd;
    @FXML private ChoiceBox<String> cbEtiquetaEcologic;
    @FXML private ChoiceBox<String> cbTipoAdvertencia;

    // Labels
    @FXML private Label lblEtiqueta;
    @FXML private Label lblTipoAdvertencia;

    private ProductoLimpieza producto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Tipos de producto
        cbTipoProd.getItems().addAll("QUIMICO", "ECOLOGICO");
        cbTipoProd.setValue("QUIMICO");

        // Advertencias
        cbTipoAdvertencia.getItems().clear();
        for (Advertencia adv : Advertencia.values()) {
            cbTipoAdvertencia.getItems().add(adv.name());
        }
        cbTipoAdvertencia.setValue(Advertencia.values()[0].name());

        // Etiquetas ecológicas
        cbEtiquetaEcologic.getItems().clear();
        for (EtiquetaEcologica et : EtiquetaEcologica.values()) {
            cbEtiquetaEcologic.getItems().add(et.name());
        }
        cbEtiquetaEcologic.setValue("Biodegradable");

        // Aplicar visibilidad inicial
        cambiadoTipo(null);
    }

    @FXML
    void cambiadoTipo(ActionEvent event) {
        switch(cbTipoProd.getValue()){
            case "QUIMICO" -> {
                lblEtiqueta.setVisible(false);
                cbEtiquetaEcologic.setVisible(false);
                lblEtiqueta.setManaged(false);
                cbEtiquetaEcologic.setManaged(false);

                lblTipoAdvertencia.setVisible(true);
                cbTipoAdvertencia.setVisible(true);
                lblTipoAdvertencia.setManaged(true);
                cbTipoAdvertencia.setManaged(true);

                cbTipoAdvertencia.getSelectionModel().selectFirst();
            }

            case "ECOLOGICO" -> {
                lblTipoAdvertencia.setVisible(false);
                cbTipoAdvertencia.setVisible(false);
                lblTipoAdvertencia.setManaged(false);
                cbTipoAdvertencia.setManaged(false);

                lblEtiqueta.setVisible(true);
                cbEtiquetaEcologic.setVisible(true);
                lblEtiqueta.setManaged(true);
                cbEtiquetaEcologic.setManaged(true);
            }
        }
    }

    @FXML
    void aceptar(ActionEvent event) {
        String tipo = cbTipoProd.getValue();
        String nombre = txtNombre.getText();
        String concentracion = txtConcentracion.getText();
        LocalDate fechaVencimiento = dpFechaVencimiento.getValue();

        try {
            ValidadorProductosLimpieza.validarNombre(nombre);
            ValidadorProductosLimpieza.validarConcentracion(concentracion);
            ValidadorProductosLimpieza.ValidarFechaVencimiento(fechaVencimiento);

            if (producto != null) {
                producto.setNombreComercial(nombre);
                producto.setConcentración(concentracion);
                producto.setFechaVencimiento(fechaVencimiento);

                switch (tipo) {
                    case "QUIMICO" -> {
                        String advertenciaTexto = cbTipoAdvertencia.getValue();
                        if (advertenciaTexto == null) {
                            mostrarAlerta("Debés seleccionar una advertencia para el producto químico.");
                            return;
                        }
                        Advertencia advertencia = Advertencia.valueOf(advertenciaTexto);
                        ((ProductoQuimico) producto).setTipoAdvertencia(advertencia);
                    }
                    case "ECOLOGICO" -> {
                        String etiquetaTexto = cbEtiquetaEcologic.getValue();
                        EtiquetaEcologica etiqueta = EtiquetaEcologica.valueOf(etiquetaTexto);
                        ((ProductoEcologico) producto).setEtiqueta(etiqueta);
                    }
                }
            } else {
                switch (tipo) {
                    case "QUIMICO" -> {
                        String advertenciaTexto = cbTipoAdvertencia.getValue();
                        if (advertenciaTexto == null) {
                            mostrarAlerta("Debés seleccionar una advertencia para el producto químico.");
                            return;
                        }
                        Advertencia advertencia = Advertencia.valueOf(advertenciaTexto);
                        this.producto = new ProductoQuimico(advertencia, nombre, concentracion, fechaVencimiento);
                    }
                    case "ECOLOGICO" -> {
                        String etiquetaTexto = cbEtiquetaEcologic.getValue();
                        EtiquetaEcologica etiqueta = EtiquetaEcologica.valueOf(etiquetaTexto);
                        this.producto = new ProductoEcologico(etiqueta, nombre, concentracion, fechaVencimiento);
                    }
                }
            }

            cerrar();
        } catch (DatoErroneoException | ProductoVencidoException e) {
            mostrarAlerta(e.getMessage());
        }
    }

    @FXML
    public void cancelar(ActionEvent event) {
        this.cerrar();
    }

    @FXML
    public void setProducto(ProductoLimpieza producto) {
        this.producto = producto;

        if (producto != null) {
            txtNombre.setText(producto.getNombreComercial());
            txtConcentracion.setText(producto.getConcentración());
            dpFechaVencimiento.setValue(producto.getFechaVencimiento());

            if (producto instanceof ProductoQuimico quimico) {
                cbTipoProd.setValue("QUIMICO");
                cbTipoAdvertencia.setValue(quimico.getTipoAdvertencia().name());
            } else if (producto instanceof ProductoEcologico ecologico) {
                cbTipoProd.setValue("ECOLOGICO");
                cbEtiquetaEcologic.setValue(ecologico.getEtiqueta().name());
            }
        } else {
            txtNombre.clear();
            txtConcentracion.clear();
            dpFechaVencimiento.setValue(null);
            cbTipoAdvertencia.getSelectionModel().selectFirst();
            cbTipoProd.setValue("QUIMICO");
        }

        cambiadoTipo(null);
    }

    public ProductoLimpieza getProductoLimpieza(){
        return this.producto;
    }

    @FXML
    private void cerrar(){
        Stage stage = (Stage)btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alerta.setTitle("Error de validación");
        alerta.setHeaderText("Datos inválidos");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}