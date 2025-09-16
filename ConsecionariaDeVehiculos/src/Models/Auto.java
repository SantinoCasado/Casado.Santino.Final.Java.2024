package Models;

import Interfaces.ICambiarEstado;
import Enums.EstadoVehiculo;
import Enums.MarcasAuto;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;
import Interfaces.IMapAbleJson;
import Interfaces.ISerializableCsv;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.HashMap;

public class Auto extends Vehiculo implements ICambiarEstado, IMapAbleJson, ISerializableCsv {
    private MarcasAuto marca;
    private int numPuertas;
    
    // ----------------------------------- CONSTRUCTORES ------------------------------------------------------------------------------------------------------------------------------------------------------
    public Auto() {
    }

    // Constructor desde Map (para deserializar JSON)
    public Auto(Map<String, String> map) {
        super(map);
        this.marca = MarcasAuto.valueOf(map.get("marca"));
        this.numPuertas = Integer.parseInt(map.get("numPuertas"));
    }
    
    public Auto(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float  kilometros, EstadoVehiculo estadoVehiculo, LocalDate fechaAlquiler){
        super(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estadoVehiculo, fechaAlquiler);
        this.marca = MarcasAuto.FORD;
        this.numPuertas = 4;
    }
    
    public Auto(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float  kilometros, EstadoVehiculo estadoVehiculo, MarcasAuto marca, int numPuertas, LocalDate fechaAlquiler){
        super(tipo, patente, añoFabricacion, tipoCombustible,  kilometros, estadoVehiculo, fechaAlquiler);
        this.marca = marca;
        this.numPuertas = numPuertas;
    }
    
    
    // ----------------------------------- GETERS Y SETERS ------------------------------------------------------------------------------------------------------------------------------------------------------
    //MARCA
    public MarcasAuto getMarca() {
        return marca;
    }
    public void setMarca(MarcasAuto marca) {
        this.marca = marca;
    }
    

    //PUERTAS
    public int getNumPuertas() {
        return numPuertas;
    }
    public void setNumPuertas(int numPuertas) {
        this.numPuertas = numPuertas;
    }
    
    // ----------------------------------- ARCHIVOS  ------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public String toCSV() {
        return super.toCSV() + "," + marca.name() + "," + numPuertas;
    }
    
    public static Auto fromCSV(String linea) {
        String[] parts = linea.split(",");
        // Heredados
        TipoVehiculos tipo = TipoVehiculos.valueOf(parts[0]);
        String patente = parts[1];
        int añoFabricacion = Integer.parseInt(parts[2]);
        TipoCombustible tipoCombustible = TipoCombustible.valueOf(parts[3]);
        float kilometros = Float.parseFloat(parts[4]);
        EstadoVehiculo estadoVehiculo = EstadoVehiculo.valueOf(parts[5]);
        LocalDate fechaAlquiler = parts[6].isEmpty() ? null : LocalDate.parse(parts[6]);

        // Propios
        MarcasAuto marca = MarcasAuto.valueOf(parts[7]);
        int numPuertas = Integer.parseInt(parts[8]);

        // Constructor completo
        Auto auto = new Auto(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estadoVehiculo, fechaAlquiler);
        auto.setMarca(marca);
        auto.setNumPuertas(numPuertas);

        return auto;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = (Map<String, String>) super.toMap();
        map.put("marca", marca.name());
        map.put("numPuertas", String.valueOf(numPuertas));
        return map;
    }

    public static Auto fromMap(Map<String, String> map) {
        return new Auto(map);
    }
    
    
    // ----------------------------------- METODOS ABSTRACTOS  ------------------------------------------------------------------------------------------------------------------------------------------------------  
    @Override
    public float calcularCostoAlquiler(int dias) {
       return dias * 50.0f;
    }

    @Override
    public String mostrarDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-80s %s%n", "Tipo de vehiculo:", "Auto"));
        sb.append(String.format("%-85s %s%n", "Patente:", super.getPatente()));
        sb.append(String.format("%-80s %d%n", "Año de Fabricacion:", super.getAñoFabricacion()));
        sb.append(String.format("%-76s %s%n", "Tipo de Combustible:", super.getTipoCombustible()));
        sb.append(String.format("%-80s %.2f%n", "Kilometros:", super.getKilometros()));
        sb.append(String.format("%-75s %s%n", "Fecha de Alquiler:", String.valueOf(super.getFechaAlquiler())));
        sb.append(String.format("%-83s %s%n", "Marca:", this.marca));
        sb.append(String.format("%-80s %d%n", "Numero de Puertas:", this.numPuertas));
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
    public int compareTo(Vehiculo otro) {
        return this.getPatente().compareTo(otro.getPatente());
    }

    // ----------------------------------- METODOS DE INTERFAZ ------------------------------------------------------------------------------------------------------------------------------------------------------
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
