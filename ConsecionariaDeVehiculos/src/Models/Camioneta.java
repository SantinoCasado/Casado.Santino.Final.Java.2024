package Models;

import Interfaces.ICambiarEstado;
import Enums.EstadoVehiculo;
import Enums.MarcasCamioneta;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;
import Interfaces.ICambiarEstado;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Camioneta extends Vehiculo implements ICambiarEstado{
    private MarcasCamioneta marca;
    private float campacidadCargaKg;
    
    //Constructores
    public Camioneta() {
    }

    public Camioneta(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float kilometros, EstadoVehiculo estado,  MarcasCamioneta marca, float campacidadCargaKg) {
        super(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estado);
        this.marca = marca;
        this.campacidadCargaKg = campacidadCargaKg;
    }

    public Camioneta(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float kilometros, EstadoVehiculo estado){
        super(tipo, patente, añoFabricacion, tipoCombustible, kilometros, estado);
        this.marca = MarcasCamioneta.DODGE;
        this.campacidadCargaKg = 500;
    }
    
    
    //Getters y Setters
    public MarcasCamioneta getMarca() {
        return marca;
    }
    public void setMarca(MarcasCamioneta marca) {
        this.marca = marca;
    }
    

    public double getCampacidadCargaKg() {
        return campacidadCargaKg;
    }
    public void setCampacidadCargaKg(float campacidadCargaKg) {
        this.campacidadCargaKg = campacidadCargaKg;
    }
    
    //Override de metodos abstractos
    @Override
    public String mostrarDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t" + "Camioneta" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getPatente() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getAñoFabricacion() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getTipoCombustible() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getKilometros()+ "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(this.marca + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(this.campacidadCargaKg +  "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.mostrarDetalles();
    }

    @Override
    public float calcularCostoAlquiler(int dias) {
       return dias * 80.0f;     //Declaro el numero como float
    }
    
    @Override
    public String ImprirTicker(LocalDate fechaAlquiler) {
        LocalDate hoy = LocalDate.now();
        int diasInt = (int) ChronoUnit.DAYS.between(fechaAlquiler, hoy);
        
        float precioTotalAlquiler = this.calcularCostoAlquiler(diasInt);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Camioneta" + "\n" + "\n");
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
    
    
}
