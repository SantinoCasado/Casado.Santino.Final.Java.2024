package Models;

import Enums.EstadoVehiculo;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;
import Interfaces.IMapAbleJson;
import Interfaces.ISerializableCsv;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public abstract class Vehiculo implements Comparable<Vehiculo>, Serializable, IMapAbleJson, ISerializableCsv {
    TipoVehiculos tipo;
    private String patente;
    private int añoFabricacion;
    private TipoCombustible tipoCombustible;
    private float kilometros;
    private EstadoVehiculo estadoVehiculo;
    private LocalDate fechaAlquiler;

// ----------------------------------- CONSTRUCTORES ------------------------------------------------------------------------------------------------------------------------------------------------------
    public Vehiculo() {
    }

    // Constructor desde Map (para deserializar JSON)
    public Vehiculo(Map<String, String> map) {
        this.tipo = TipoVehiculos.valueOf(map.get("tipo"));
        this.patente = map.get("patente");
        this.añoFabricacion = Integer.parseInt(map.get("añoFabricacion"));
        this.tipoCombustible = TipoCombustible.valueOf(map.get("tipoCombustible"));
        this.kilometros = Float.parseFloat(map.get("kilometros"));
        this.estadoVehiculo = EstadoVehiculo.valueOf(map.get("estadoVehiculo"));
        
        // Manejo seguro de fecha
        String fecha = map.get("fechaAlquiler");
        this.fechaAlquiler = (fecha == null || fecha.isEmpty()) ? null : LocalDate.parse(fecha);
        }

    // Constructor con fecha pero estado DISPONIBLE por defecto
    public Vehiculo(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float kilometros, LocalDate fechaAlquiler) {
        this.tipo = tipo;
        this.patente = patente;
        this.añoFabricacion = añoFabricacion;
        this.tipoCombustible = tipoCombustible;
        this.kilometros = kilometros;
        this.estadoVehiculo = EstadoVehiculo.DISPONIBLE; 
        this.fechaAlquiler = fechaAlquiler;
    }

    // Constructor con estado pero fecha actual por defecto
    public Vehiculo(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float kilometros, EstadoVehiculo estadoVehiculo) {
        this.tipo = tipo;
        this.patente = patente;
        this.añoFabricacion = añoFabricacion;
        this.tipoCombustible = tipoCombustible;
        this.kilometros = kilometros;
        this.estadoVehiculo = estadoVehiculo;
        this.fechaAlquiler = LocalDate.now();
    }

    // Constructor completo con estado Y fecha (EL QUE FALTABA)
    public Vehiculo(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float kilometros, EstadoVehiculo estadoVehiculo, LocalDate fechaAlquiler) {
        this.tipo = tipo;
        this.patente = patente;
        this.añoFabricacion = añoFabricacion;
        this.tipoCombustible = tipoCombustible;
        this.kilometros = kilometros;
        this.estadoVehiculo = estadoVehiculo;
        this.fechaAlquiler = fechaAlquiler;
    }

    // ----------------------------------- GETERS Y SETERS ------------------------------------------------------------------------------------------------------------------------------------------------------
    //TIPO
    public TipoVehiculos getTipo() {
        return tipo;
    }
    public void setTipo(TipoVehiculos tipo) {
        this.tipo = tipo;
    }
    
    //PATENTE
    public String getPatente() { 
        return patente; 
    }
    public void setPatente(String patente) { 
        this.patente = patente; 
    }
    
    //AÑO FABRICACION
    public int getAñoFabricacion() { 
        return añoFabricacion; 
    }
    public void setAñoFabricacion(int añoFabricacion) { 
        this.añoFabricacion = añoFabricacion; 
    }
    
    //TIPO COMBUSTIBLE
    public TipoCombustible getTipoCombustible() { 
        return tipoCombustible; 
    }
    public void setTipoCombustible(TipoCombustible tipoCombustible) { 
        this.tipoCombustible = tipoCombustible; 
    }
    
    //KILOMETROS
    public float getKilometros() { 
        return  kilometros; 
    }
    public void setKilometros(float  kilometros) { 
        this. kilometros =  kilometros; 
    }
    
    //ESTADO VEHICULO
    public EstadoVehiculo getEstadoVehiculo() { 
        return estadoVehiculo; 
    }
    public void setEstadoVehiculo(EstadoVehiculo estadoVehiculo) { 
        this.estadoVehiculo = estadoVehiculo; 
    }

    //FECHA ALQUILER
    public LocalDate getFechaAlquiler(){ 
        return fechaAlquiler; 
        }
    public void setFechaAlquiler(LocalDate fechaAlquiler) { 
        this.fechaAlquiler = fechaAlquiler; 
    }
    
    // ----------------------------------- ARCHIVOS  ------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public String toCSV() {
        return String.format("%s,%s,%d,%s,%.2f,%s,%s",
            tipo.name(),
            patente,
            añoFabricacion,
            tipoCombustible.name(),
            kilometros,
            estadoVehiculo.name(),
            fechaAlquiler != null ? fechaAlquiler.toString() : ""
        );
    }

    public static Vehiculo fromCSV(String linea) {
        return null;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("tipo", tipo.name());
        map.put("patente", patente);
        map.put("añoFabricacion", String.valueOf(añoFabricacion));
        map.put("tipoCombustible", tipoCombustible.name());
        map.put("kilometros", String.valueOf(kilometros));
        map.put("estadoVehiculo", estadoVehiculo.name());
        map.put("fechaAlquiler", fechaAlquiler != null ? fechaAlquiler.toString() : "");
        return map;
    }
    
    // ----------------------------------- METODOS ABSTRACTOS  ------------------------------------------------------------------------------------------------------------------------------------------------------
    public abstract String mostrarDetalles();
    public abstract float calcularCostoAlquiler(int dias);
    public abstract String ImprirTicker(LocalDate fechaAlquiler);
    
    // ----------------------------------- METODOS DE CLASE ------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public int compareTo(Vehiculo otro) {
        if (otro == null) {
            return 1; // Este objeto es "mayor" que null
        }
        return this.patente.compareToIgnoreCase(otro.patente); // Ignorar mayúsculas/minúsculas
    }
    
    public float obtenerHorasUso(LocalDate fechaFinUso){
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime finEstimado = fechaFinUso.atTime(LocalTime.MAX); //fin de la fecha 23:59:59.9
        
        long horasEstimadas = ChronoUnit.HOURS.between(ahora, finEstimado);
        if (horasEstimadas < 0) horasEstimadas = 0;
        
        return horasEstimadas;
    }
    
    public void registrarUso(float horas) {
        this.kilometros += horas;
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
        if (obj == null || !(obj instanceof Vehiculo)) return false; // ← CORREGIDO
        Vehiculo other = (Vehiculo) obj;
        return patente != null && patente.equals(other.patente);
    }

}