package Models;

import Interfaces.ICambiarEstado;
import Enums.EstadoVehiculo;
import Enums.MarcasMoto;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;
import Interfaces.ICambiarEstado;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Moto extends Vehiculo implements ICambiarEstado{
    private MarcasMoto marca;
    private int cilindrada;
    
    
    //Constructores
    public Moto() {
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
    
    //Getters y Setters
    public MarcasMoto getMarca() {
        return marca;
    }
    public void setMarca(MarcasMoto marca) {
        this.marca = marca;
    }
    

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }
    
    //Override de metodos abstractos
    @Override
    public String mostrarDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append( "\t" + "Moto" +  "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getPatente() +  "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getAñoFabricacion() +  "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getTipoCombustible() +  "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getKilometros()+  "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(this.marca + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(this.cilindrada +  "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        
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
    public String ImprirTicker(LocalDate fechaAlquiler) {
        LocalDate hoy = LocalDate.now();
        int diasInt = (int) ChronoUnit.DAYS.between(fechaAlquiler, hoy);
        
        float precioTotalAlquiler = this.calcularCostoAlquiler(diasInt);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Moto" + "\n" + "\n");
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
