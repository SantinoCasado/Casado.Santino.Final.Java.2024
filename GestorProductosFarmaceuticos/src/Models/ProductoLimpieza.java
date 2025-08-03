package Models;

import Interfaces.IMapAbleJson;
import Interfaces.ISerializableCsv;
import Validaciones.ValidadorProductosLimpieza;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class ProductoLimpieza implements ISerializableCsv, IMapAbleJson{
    protected String nombreComercial;
    protected String concentracion;
    protected LocalDate fechaVencimiento;

    public ProductoLimpieza(String nombreComercial, String concentracion, LocalDate fechaVencimiento) {
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
        ValidadorProductosLimpieza.validarNombre(nombreComercial);
        this.nombreComercial = nombreComercial;
    }

    public String getConcentración() {
        return concentracion;
    }

    public void setConcentración(String concentración) {
        ValidadorProductosLimpieza.validarConcentracion(concentracion);
        this.concentracion = concentración;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        ValidadorProductosLimpieza.ValidarFechaVencimiento(fechaVencimiento);
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
        sb.append("nombreComercial=").append(nombreComercial);
        sb.append(", concentraci\u00f3n=").append(concentracion);
        sb.append(", fechaVencimiento=").append(fechaVencimiento);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String toCSV(){
        return nombreComercial + "," + concentracion + "," + fechaVencimiento;
    }
    
    @Override
    public Map<String, String> toMap() {
        Map<String,String> datos = new HashMap<>();
        datos.put("nombreComercial", nombreComercial);
        datos.put("concentracion", concentracion);
        datos.put("fechaVencimiento", fechaVencimiento.toString());
        return datos;
    }

}
