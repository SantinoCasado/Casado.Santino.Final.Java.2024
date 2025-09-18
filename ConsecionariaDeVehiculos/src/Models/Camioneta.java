package Models;

import Enums.EstadoVehiculo;
import Enums.MarcasCamioneta;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;
import Interfaces.ICambiarEstado;
import Interfaces.IMapAbleJson;
import Interfaces.ISerializableCsv;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Camioneta extends Vehiculo implements ICambiarEstado, IMapAbleJson, ISerializableCsv{
    private MarcasCamioneta marca;
    private float campacidadCargaKg;
    
    // ----------------------------------- CONSTRUCTORES ------------------------------------------------------------------------------------------------------------------------------------------------------
    public Camioneta() {
    }

    // Constructor desde Map (para deserializar JSON)
    public Camioneta(Map<String, String> map) {
        super(map);
        this.marca = MarcasCamioneta.valueOf(map.get("marca"));
        this.campacidadCargaKg = Float.parseFloat(map.get("campacidadCargaKg"));
    }

    public Camioneta(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float kilometros, EstadoVehiculo estado,  MarcasCamioneta marca, float campacidadCargaKg, LocalDate fechaAlquiler) {
        super(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estado, fechaAlquiler);
        this.marca = marca;
        this.campacidadCargaKg = campacidadCargaKg;
    }

    public Camioneta(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float kilometros, EstadoVehiculo estado, LocalDate fechaAlquiler) {
        super(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estado, fechaAlquiler);
        this.marca = MarcasCamioneta.DODGE;
        this.campacidadCargaKg = 500.0f;
    }
    
    
    // ----------------------------------- GETTERS Y SETTERS ------------------------------------------------------------------------------------------------------------------------------------------------------
    //MARCA
    public MarcasCamioneta getMarca() {
        return marca;
    }
    public void setMarca(MarcasCamioneta marca) {
        this.marca = marca;
    }
    
    //CAPACIDAD DE CARGA
    public float getCampacidadCargaKg() {
        return campacidadCargaKg;
    }
    public void setCampacidadCargaKg(float campacidadCargaKg) {
        this.campacidadCargaKg = campacidadCargaKg;
    }

    // ----------------------------------- ARCHIVOS ------------------------------------------------------------------------------------------------------------------------------------------------------
    // Implementación de IMapAbleJson
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = (Map<String, String>) super.toMap();
        map.put("marca", marca.name());
        map.put("campacidadCargaKg", String.valueOf(campacidadCargaKg));
        return map;
    }

    public static Camioneta fromMap(Map<String, String> map) {
        return new Camioneta(map);
    }

    // Implementación de ISerializableCsv
    @Override
    public String toCSV() { 
        return super.toCSV() + "," + marca.name() + "," + campacidadCargaKg;
    }
    
    public static Camioneta fromCSV(String linea) {
    System.out.println("Línea recibida: " + linea);
    String[] partes = linea.split(",");
    if (partes.length < 9) {
        throw new IllegalArgumentException("Línea CSV inválida para Camioneta: " + linea);
    }
    TipoVehiculos tipo = TipoVehiculos.valueOf(partes[0]);
    String patente = partes[1];
    int añoFabricacion = Integer.parseInt(partes[2]);
    TipoCombustible tipoCombustible = TipoCombustible.valueOf(partes[3]);
    float kilometros = Float.parseFloat(partes[4]);
    EstadoVehiculo estado = EstadoVehiculo.valueOf(partes[5]);
    LocalDate fechaAlquiler = partes[6].isEmpty() ? null : LocalDate.parse(partes[6]);
    MarcasCamioneta marca = MarcasCamioneta.valueOf(partes[7]);
        System.out.println("Valor a parsear: " + partes[8]);
    float campacidadCargaKg = Float.parseFloat(partes[8]);
    
    Camioneta camioneta =  new Camioneta(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estado, marca, campacidadCargaKg, fechaAlquiler);
    
    return camioneta;
    }
    // ----------------------------------- MÉTODOS ABSTRACTOS ------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public String mostrarDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-80s %s%n", "Tipo de vehiculo:", "Camioneta"));
        sb.append(String.format("%-85s %s%n", "Patente:", super.getPatente()));
        sb.append(String.format("%-80s %d%n", "Año de Fabricacion:", super.getAñoFabricacion()));
        sb.append(String.format("%-76s %s%n", "Tipo de Combustible:", super.getTipoCombustible()));
        sb.append(String.format("%-80s %.2f%n", "Kilometros:", super.getKilometros()));
        sb.append(String.format("%-75s %s%n", "Fecha de Alquiler:", String.valueOf(super.getFechaAlquiler())));
        sb.append(String.format("%-83s %s%n", "Marca:", this.marca));
        sb.append(String.format("%-77s %.2f kg%n", "Capacidad de Carga:", this.campacidadCargaKg));
        return sb.toString();
    }

    @Override
    public String ImprirTicker(LocalDate fechaAlquiler) {
        LocalDate hoy = LocalDate.now();
        int diasInt = (int) Math.abs(ChronoUnit.DAYS.between(fechaAlquiler, hoy));
        float precioTotalAlquiler = this.calcularCostoAlquiler(diasInt);

        StringBuilder sb = new StringBuilder();
        sb.append(this.mostrarDetalles());
        sb.append(String.format("%-80s %d dias x 50%n", "Precio por dia:", diasInt));
        sb.append(String.format("%-80s %.2f%n", "Precio Total:", precioTotalAlquiler));
        return sb.toString();
    }

    @Override
    public float calcularCostoAlquiler(int dias) {
       return dias * 80.0f;     //Declaro el numero como float
    }

    @Override
    public int compareTo(Vehiculo otro) {
        return this.getPatente().compareTo(otro.getPatente());
    }

    // ----------------------------------- IMPLEMENTACION DE INTERFACES ------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void realizarMatenimiento() {
        super.setEstadoVehiculo(EstadoVehiculo.EN_MANTENIMIENTO);
    }

    @Override
    public void alquilarVehiculo() {
        super.setEstadoVehiculo(EstadoVehiculo.ALQUILADO);
    }

    @Override
    public void disponerVehiculo() {
        super.setEstadoVehiculo(EstadoVehiculo.DISPONIBLE);
    }
}
