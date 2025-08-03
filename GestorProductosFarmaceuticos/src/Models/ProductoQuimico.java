package Models;

import Enums.Advertencia;
import Exceptions.DatoErroneoException;
import Interfaces.ISerializableCsv;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class ProductoQuimico extends ProductoLimpieza implements ISerializableCsv{
    private Advertencia tipoAdvertencia;

    public ProductoQuimico(Advertencia tipoAdvertencia, String nombreComercial, String concentración, LocalDate fechaVencimiento) {
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
        sb.append("ProductoQuimico{");
        sb.append(super.toString());
        sb.append("tipoAdvertencia=").append(tipoAdvertencia);
        sb.append('}');
        return sb.toString();
    }
    
    public String toCSV(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toCSV()).append(", ProductoQuimico");    

        sb.append(",").append(this.tipoAdvertencia);
        
        return sb.toString();
    }
    
    @Override
    public ProductoQuimico fromCSV(String line) {
        String[] result = line.split(",");

        String nombre = result[0];
        String concentracion = result[1];
        LocalDate fecha = LocalDate.parse(result[2]);
        String tipo = result[3];
        
        String advertenciaTexto = result[4].trim();
        try {
        tipoAdvertencia = Advertencia.valueOf(advertenciaTexto);
        } catch (IllegalArgumentException e) {
        throw new DatoErroneoException("Advertencia inválida: " + advertenciaTexto);
        }

        return new ProductoQuimico( tipoAdvertencia, nombre, concentracion, fecha);
    }
    
    @Override
    public Map<String, String> toMap() {
        Map<String, String> datos = new HashMap<>(super.toMap()); // Copia datos comunes de la clase padre
        datos.put("tipo", "ProuductoQuimico");       
        datos.put("Tipo de Advertencia", this.getTipoAdvertencia().name()); 
        return datos;
    }
}
