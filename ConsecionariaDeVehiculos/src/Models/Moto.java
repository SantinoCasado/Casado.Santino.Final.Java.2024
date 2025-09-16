package Models;

import Enums.EstadoVehiculo;
import Enums.MarcasMoto;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;
import Interfaces.ICambiarEstado;
import Interfaces.IMapAbleJson;
import Interfaces.ISerializableCsv;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Moto extends Vehiculo implements ICambiarEstado, IMapAbleJson, ISerializableCsv{
    private MarcasMoto marca;
    private int cilindrada;
    
    // ----------------------------------- CONSTRUCTORES ------------------------------------------------------------------------------------------------------------------------------------------------------
    public Moto() {
    }

    // Constructor desde Map (para deserializar JSON)
    public Moto(Map<String, String> map) {
        super(map);
        this.marca = MarcasMoto.valueOf(map.get("marca"));
        this.cilindrada = Integer.parseInt(map.get("cilindrada"));
    }

    public Moto(String patente, String marca, int añoFabricacion, float kilometros, TipoVehiculos tipo, TipoCombustible tipoCombustible, EstadoVehiculo estadoVehiculo, LocalDate fechaAlquiler, int cilindrada) {
        super(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estadoVehiculo, fechaAlquiler);
        this.cilindrada = 125;
        this.marca = MarcasMoto.HONDA;
    }


    public Moto(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float kilometros, EstadoVehiculo estado, MarcasMoto marca, int cilindrada, LocalDate fechaAlquiler) {
        super(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estado, fechaAlquiler);
        this.marca = marca;
        this.cilindrada = cilindrada;
    }
    
    //----------------------------------- GETTERS Y SETTERS ------------------------------------------------------------------------------------------------------------------------------------------------------
    //MARCA
    public MarcasMoto getMarca() {
        return marca;
    }
    public void setMarca(MarcasMoto marca) {
        this.marca = marca;
    }
    
    //CILINDRADA
    public int getCilindrada() {
        return cilindrada;
    }
    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    // ----------------------------------- ARCHIVOS ------------------------------------------------------------------------------------------------------------------------------------------------------
    // Implementación de IMapAbleJson
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = super.toMap();
        map.put("marca", marca.name());
        map.put("cilindrada", String.valueOf(cilindrada));
        return map;
    }

    public static Moto fromMap(Map<String, String> map) {
        return new Moto(map);
    }

    // Implementación de ISerializableCsv
    @Override
    public String toCSV() {
        return super.toCSV() + "," + marca.name() + "," + cilindrada;
    }
    
    public static Moto fromCSV(String linea) {
        String[] parts = linea.split(",");
        TipoVehiculos tipo = TipoVehiculos.valueOf(parts[0]);
        String patente = parts[1];
        int añoFabricacion = Integer.parseInt(parts[2]);
        TipoCombustible tipoCombustible = TipoCombustible.valueOf(parts[3]);
        float kilometros = Float.parseFloat(parts[4]);
        EstadoVehiculo estadoVehiculo = EstadoVehiculo.valueOf(parts[5]);
        LocalDate fechaAlquiler = parts[6].isEmpty() ? null : LocalDate.parse(parts[6]);
        MarcasMoto marca = MarcasMoto.valueOf(parts[7]);
        int cilindrada = Integer.parseInt(parts[8]);
        return new Moto(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estadoVehiculo, marca, cilindrada, fechaAlquiler);
    }
    
    //----------------------------------- MÉTODOS ABSTRACTOS ------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public String mostrarDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-80s %s%n", "Tipo de vehiculo: ", "Moto"));
        sb.append(String.format("%-85s %s%n", "Patente: ", super.getPatente()));
        sb.append(String.format("%-80s %d%n", "Año de Fabricacion: ", super.getAñoFabricacion()));
        sb.append(String.format("%-76s %s%n", "Tipo de Combustible: ", super.getTipoCombustible()));
        sb.append(String.format("%-80s %.2f%n", "Kilometros: ", super.getKilometros()));
        sb.append(String.format("%-75s %s%n", "Fecha de Alquiler: ", String.valueOf(super.getFechaAlquiler())));
        sb.append(String.format("%-83s %s%n", "Marca: ", this.marca));
        sb.append(String.format("%-80s %d%n", "Cilindrada: ", this.cilindrada));
        return sb.toString();
    }

    @Override
    public String ImprirTicker(LocalDate fechaAlquiler) {
        LocalDate hoy = LocalDate.now();
        int diasInt = (int) Math.abs(ChronoUnit.DAYS.between(fechaAlquiler, hoy));
        float precioTotalAlquiler = this.calcularCostoAlquiler(diasInt);

        StringBuilder sb = new StringBuilder();
        sb.append(this.mostrarDetalles());
        sb.append(String.format("%-80s %d dias x 50%n", "Precio por dia: ", diasInt));
        sb.append(String.format("%-80s %.2f%n", "Precio Total: ", precioTotalAlquiler));
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.mostrarDetalles();
    }

    @Override
    public float calcularCostoAlquiler(int dias) {
       return dias * 30.0f;     //Declaro el numero como float
    }

    @Override
    public int compareTo(Vehiculo otro) {
        return this.getPatente().compareTo(otro.getPatente());
    }

    //----------------------------------- IMPLEMENTACION DE INTERFACES ------------------------------------------------------------------------------------------------------------------------------------------------------
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
