package Models;

import Enums.Advertencia;
import Enums.MedidasConcentracion;
import java.time.LocalDate;


public class ProductoQuimico extends ProductoLimpieza{
    private Advertencia tipoAdvertencia;

    public ProductoQuimico(Advertencia tipoAdvertencia, String nombreComercial, MedidasConcentracion concentración, LocalDate fechaVencimiento) {
        super(nombreComercial, concentración, fechaVencimiento);
        this.tipoAdvertencia = tipoAdvertencia;
    }
    
    public ProductoQuimico() {
    }


    public Advertencia getTipoAdvertencia() {
        return tipoAdvertencia;
    }

    public void setTipoAdvertencia(Advertencia tipoAdvertencia) {
        this.tipoAdvertencia = tipoAdvertencia;
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
        final ProductoQuimico other = (ProductoQuimico) obj;
        return this.tipoAdvertencia == other.tipoAdvertencia;
    }
    
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Tipo: ").append("ProductoQuimico" + "   |   ");
        sb.append("Advertencia: ").append(tipoAdvertencia);
        sb.append(']');
        return sb.toString();
    }

}
