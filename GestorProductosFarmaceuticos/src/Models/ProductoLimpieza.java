package Models;

import Enums.MedidasConcentracion;
import Validations.ValidadorDatosProductos;
import java.time.LocalDate;
import java.util.Objects;


public abstract class ProductoLimpieza {
    protected String nombreComercial;
    protected LocalDate fechaVencimiento;
    protected MedidasConcentracion concentracion;

    public ProductoLimpieza(String nombreComercial, MedidasConcentracion concentracion , LocalDate fechaVencimiento) {
        this.nombreComercial = nombreComercial;
        this.concentracion = concentracion;
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public ProductoLimpieza() {
    }


    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        ValidadorDatosProductos.validarNombre(nombreComercial);
        this.nombreComercial = nombreComercial;
    }

    public MedidasConcentracion getConcentración() {
        return this.concentracion;
    }

    public void setConcentración(MedidasConcentracion concentración) {
        ValidadorDatosProductos.validarConcentracion(concentracion);
        this.concentracion = concentración;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        ValidadorDatosProductos.ValidarFechaVencimiento(fechaVencimiento);
        this.fechaVencimiento = fechaVencimiento;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProductoLimpieza other = (ProductoLimpieza) obj;
        if (!Objects.equals(this.nombreComercial, other.nombreComercial)) {
            return false;
        }
        if (!Objects.equals(this.concentracion, other.concentracion)) {
            return false;
        }
        return Objects.equals(this.fechaVencimiento, other.fechaVencimiento);
    }
    
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombreComercial.toUpperCase() + "\n");
        sb.append("[");
        sb.append("Concentracion: ").append(concentracion + "   |   ");
        sb.append("Fecha de Vencimiento: ").append(fechaVencimiento + "   |   ");
        return sb.toString();
    }
}
