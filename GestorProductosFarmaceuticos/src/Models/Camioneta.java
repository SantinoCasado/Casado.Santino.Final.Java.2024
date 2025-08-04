package Models;

import Interfaces.IMantenible;
import Models.Enums.EstadoVehiculo;
import Models.Enums.MarcasCamioneta;
import Models.Enums.TipoCombustible;

public class Camioneta extends Vehiculo implements IMantenible{
    private MarcasCamioneta marca;
    private double campacidadCargaKg;
    
    //Constructores
    public Camioneta() {
    }

    public Camioneta(String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, MarcasCamioneta marca, double campacidadCargaKg) {
        super(patente, añoFabricacion, tipoCombustible, horasUso);
        this.marca = marca;
        this.campacidadCargaKg = campacidadCargaKg;
    }

    public Camioneta(String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estadoVehiculo) {
        super(patente, añoFabricacion, tipoCombustible, horasUso, estadoVehiculo);
        this.marca = MarcasCamioneta.JEEP;
        this.campacidadCargaKg = 1000.0;
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
    public void setCampacidadCargaKg(double campacidadCargaKg) {
        this.campacidadCargaKg = campacidadCargaKg;
    }
    
    //Override de metodos abstractos
    @Override
    public String mostrarDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append("Auto" + "\t");
        sb.append(super.getPatente() + "\t");
        sb.append(super.getAñoFabricacion() + "\t");
        sb.append(super.getTipoCombustible() + "\t");
        sb.append(super.getHorasUso() + "\t");
        sb.append(this.marca + "\t");
        sb.append(this.campacidadCargaKg + "\t");
        
        return sb.toString();
    }

    @Override
    public float calcularCostoAlquiler(int dias) {
       return dias * 80.0f;     //Declaro el numero como float
    }
    
    @Override
    public void realizarMatenimiento(){
        super.setEstadoAlquiler(EstadoVehiculo.EN_MANTENIMIENTO);
    }
    
}
