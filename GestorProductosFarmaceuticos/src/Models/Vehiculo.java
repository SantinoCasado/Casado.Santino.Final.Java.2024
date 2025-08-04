package Models;

import Models.Enums.EstadoVehiculo;
import Models.Enums.TipoCombustible;

import java.io.Serializable;
import java.util.Objects;

public abstract class Vehiculo implements Comparable<Vehiculo>, Serializable {
    private String patente;
    private int añoFabricacion;
    private TipoCombustible tipoCombustible;
    private float horasUso;
    private EstadoVehiculo estadoVehiculo;

    // Constructores 
    public Vehiculo() {
    }

    public Vehiculo(String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso) {
        this.patente = patente;
        this.añoFabricacion = añoFabricacion;
        this.tipoCombustible = tipoCombustible;
        this.horasUso = horasUso;
        this.estadoVehiculo = EstadoVehiculo.DISPONIBLE; // Valor por defecto
    }

    public Vehiculo(String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estadoVehiculo) {
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
    
    
    public EstadoVehiculo getEstadoAlquiler() { 
        return estadoVehiculo; 
    }
    public void setEstadoAlquiler(EstadoVehiculo estadoVehiculo) { 
        this.estadoVehiculo = estadoVehiculo; 
    }
    
    // Métodos abstractos
    public abstract String mostrarDetalles();
    public abstract float calcularCostoAlquiler(int dias);

    @Override
    public int compareTo(Vehiculo otroVehiculo) {
        return Integer.compare(this.añoFabricacion, otroVehiculo.añoFabricacion);
    }

    //Hash code y equals
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.patente);
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