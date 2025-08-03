package Models;

import Enums.EtiquetaEcologica;
import Enums.MedidasConcentracion;
import java.time.LocalDate;

public class ProductoEcologico extends ProductoLimpieza{
    private EtiquetaEcologica etiqueta;

    public ProductoEcologico(EtiquetaEcologica etiqueta, String nombreComercial, MedidasConcentracion concentración, LocalDate fechaVencimiento) {
        super(nombreComercial, concentración, fechaVencimiento);
        this.etiqueta = etiqueta;
    }
    
    public ProductoEcologico(){
    }

    public EtiquetaEcologica getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(EtiquetaEcologica etiqueta) {
        this.etiqueta = etiqueta;
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
        final ProductoEcologico other = (ProductoEcologico) obj;
        return this.etiqueta == other.etiqueta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Tipo: ").append("ProductoEcologico" + "   |   ");
        sb.append("Etiqueta Ecologica: ").append(etiqueta);
        sb.append(']');
        return sb.toString();
    }
    
}
