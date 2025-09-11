package Models;

import Interfaces.ICambiarEstado;
import Enums.EstadoVehiculo;
import Enums.MarcasAuto;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Auto extends Vehiculo implements ICambiarEstado{
    private MarcasAuto marca;
    private int numPuertas;
    
    public Auto(){        
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
    
    
    //Getters y Setters
    public MarcasAuto getMarca() {
        return marca;
    }
    public void setMarca(MarcasAuto marca) {
        this.marca = marca;
    }
    

    public int getNumPuertas() {
        return numPuertas;
    }

    public void setNumPuertas(int numPuertas) {
        this.numPuertas = numPuertas;
    }
    
    
    //Override de motodos abstractos
    @Override
    public String mostrarDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t" + "  Auto" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getPatente() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getAñoFabricacion() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getTipoCombustible() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getKilometros()+ "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(this.marca + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(this.numPuertas + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.mostrarDetalles();
    }
    
    
    @Override
    public float calcularCostoAlquiler(int dias) {
       return dias * 50.0f;     //Declaro el numero como float
    }

    @Override
    public String ImprirTicker(LocalDate fechaAlquiler) {
        LocalDate hoy = LocalDate.now();
        int diasInt = (int) ChronoUnit.DAYS.between(fechaAlquiler, hoy);
        
        float precioTotalAlquiler = this.calcularCostoAlquiler(diasInt);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Auto" + "\n" + "\n");
        sb.append(fechaAlquiler + "\n" + "\n");
        sb.append(diasInt);
        sb.append(" x ");
        sb.append(diasInt + "\n" + "\n");
        sb.append(precioTotalAlquiler + "\n");
        
        return sb.toString();
    }

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

    @Override
    public int compareTo(Vehiculo o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
