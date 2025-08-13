package Models;

import Enums.EstadoVehiculo;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public abstract class Vehiculo implements Comparable<Vehiculo>, Serializable {
    TipoVehiculos tipo;
    private String patente;
    private int añoFabricacion;
    private TipoCombustible tipoCombustible;
    private float horasUso;
    private EstadoVehiculo estadoVehiculo;

    // Constructores 
    public Vehiculo() {
    }

    public Vehiculo(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso) {
        this.tipo = tipo;
        this.patente = patente;
        this.añoFabricacion = añoFabricacion;
        this.tipoCombustible = tipoCombustible;
        this.horasUso = horasUso;
        this.estadoVehiculo = EstadoVehiculo.DISPONIBLE; // Valor por defecto
    }

    public Vehiculo(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estadoVehiculo) {
        this.tipo = tipo;
        this.patente = patente;
        this.añoFabricacion = añoFabricacion;
        this.tipoCombustible = tipoCombustible;
        this.horasUso = horasUso;
        this.estadoVehiculo = estadoVehiculo;
    }

    // Método concreto
    public void registrarUso(float horas) {
        this.horasUso += horas;
    }

    // Getters y Setters    
    public TipoVehiculos getTipo() {
        return tipo;
    }
    public void setTipo(TipoVehiculos tipo) {
        this.tipo = tipo;
    }
    
    
    public String getPatente() { 
        return patente; 
    }
    public void setPatente(String patente) { 
        this.patente = patente; 
    }
    
    
    public int getAñoFabricacion() { 
        return añoFabricacion; 
    }
    public void setAñoFabricacion(int añoFabricacion) { 
        this.añoFabricacion = añoFabricacion; 
    }
    
    
    public TipoCombustible getTipoCombustible() { 
        return tipoCombustible; 
    }
    public void setTipoCombustible(TipoCombustible tipoCombustible) { 
        this.tipoCombustible = tipoCombustible; 
    }
    
    public float getHorasUso() { 
        return horasUso; 
    }
    public void setHorasUso(float horasUso) { 
        this.horasUso = horasUso; 
    }
    
    
    public EstadoVehiculo getEstadoVehiculo() { 
        return estadoVehiculo; 
    }
    public void setEstadoVehiculo(EstadoVehiculo estadoVehiculo) { 
        this.estadoVehiculo = estadoVehiculo; 
    }
    
    
    // Métodos abstractos
    public abstract String mostrarDetalles();
    public abstract float calcularCostoAlquiler(int dias);

    @Override
    public int compareTo(Vehiculo otroVehiculo) {
        return Integer.compare(this.añoFabricacion, otroVehiculo.añoFabricacion);
    }
    
    public float obtenerHorasUso(LocalDate fechaFinUso){
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime finEstimado = fechaFinUso.atTime(LocalTime.MAX); //fin de la fecha 23:59:59.9
        
        long horasEstimadas = ChronoUnit.HOURS.between(ahora, finEstimado);
        if (horasEstimadas < 0) horasEstimadas = 0;
        
        return horasEstimadas;
    }

    //Hash code y equals
    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
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
        final Vehiculo other = (Vehiculo) obj;
        return Objects.equals(this.patente, other.patente);
    }
    
}