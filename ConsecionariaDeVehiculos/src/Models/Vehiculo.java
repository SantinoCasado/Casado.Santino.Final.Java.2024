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
    private float kilometros;
    private EstadoVehiculo estadoVehiculo;
    private LocalDate fechaAlquiler;

    // Constructores 
    public Vehiculo() {
    }

    public Vehiculo(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float  kilometros, LocalDate fechaAlquiler) {
        this.tipo = tipo;
        this.patente = patente;
        this.añoFabricacion = añoFabricacion;
        this.tipoCombustible = tipoCombustible;
        this.kilometros =  kilometros;
        this.estadoVehiculo = EstadoVehiculo.DISPONIBLE; 
        this.fechaAlquiler = fechaAlquiler;
    }

    public Vehiculo(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float kilometros, EstadoVehiculo estadoVehiculo, LocalDate fechaAlquiler) {
        this.tipo = tipo;
        this.patente = patente;
        this.añoFabricacion = añoFabricacion;
        this.tipoCombustible = tipoCombustible;
        this.kilometros = kilometros;
        this.estadoVehiculo = estadoVehiculo;
        this.fechaAlquiler = fechaAlquiler;
    }

    // Método concreto
    public void registrarUso(float horas) {
        this.kilometros += horas;
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
    
    public float getKilometros() { 
        return  kilometros; 
    }
    public void setKilometros(float  kilometros) { 
        this. kilometros =  kilometros; 
    }
    
    
    public EstadoVehiculo getEstadoVehiculo() { 
        return estadoVehiculo; 
    }
    public void setEstadoVehiculo(EstadoVehiculo estadoVehiculo) { 
        this.estadoVehiculo = estadoVehiculo; 
    }

    public LocalDate getFechaAlquiler(){ 
        return fechaAlquiler; 
        }
    public void setFechaAlquiler(LocalDate fechaAlquiler) { 
        this.fechaAlquiler = fechaAlquiler; 
    }
    
    
    // Métodos abstractos
    public abstract String mostrarDetalles();
    public abstract float calcularCostoAlquiler(int dias);
    public abstract String ImprirTicker(LocalDate fechaAlquiler);
    
    
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehiculo other = (Vehiculo) obj;
        return patente != null && patente.equals(other.patente);
    }
    
}