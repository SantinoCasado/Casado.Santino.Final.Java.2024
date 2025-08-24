package Models;

import Interfaces.IMantenible;
import Enums.EstadoVehiculo;
import Enums.MarcasCamioneta;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;

public class Camioneta extends Vehiculo implements IMantenible{
    private MarcasCamioneta marca;
    private float campacidadCargaKg;
    
    //Constructores
    public Camioneta() {
    }

    public Camioneta(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estado,  MarcasCamioneta marca, float campacidadCargaKg) {
        super(tipo, patente, añoFabricacion, tipoCombustible, horasUso, estado);
        this.marca = marca;
        this.campacidadCargaKg = campacidadCargaKg;
    }

    public Camioneta(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estado){
        super(tipo, patente, añoFabricacion, tipoCombustible, horasUso, estado);
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
        sb.append("Camioneta" + "\t");
        sb.append(super.getPatente() + "\t");
        sb.append(super.getAñoFabricacion() + "\t");
        sb.append(super.getTipoCombustible() + "\t");
        sb.append(super.getHorasUso() + "\t");
        sb.append(this.marca + "\t");
        sb.append(this.campacidadCargaKg + "\t");
        
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
    public void realizarMatenimiento(){
        super.setEstadoVehiculo(EstadoVehiculo.EN_MANTENIMIENTO);
    }
    
}
