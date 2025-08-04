package Models;

import Interfaces.IMantenible;
import Models.Enums.EstadoVehiculo;
import Models.Enums.MarcasAuto;
import Models.Enums.TipoCombustible;

public class Auto extends Vehiculo implements IMantenible{
    private MarcasAuto marca;
    private int numPuertas;
    
    public Auto(){        
    }
    
    public Auto(String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estadoVehiculo){
        super(patente, añoFabricacion, tipoCombustible, horasUso);
        this.marca = MarcasAuto.FORD;
        this.numPuertas = 4;
    }
    
    public Auto(String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estadoVehiculo, MarcasAuto marca, int numPuertas){
        super(patente, añoFabricacion, tipoCombustible, horasUso);
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
        sb.append("Auto" + "\t");
        sb.append(super.getPatente() + "\t");
        sb.append(super.getAñoFabricacion() + "\t");
        sb.append(super.getTipoCombustible() + "\t");
        sb.append(super.getHorasUso() + "\t");
        sb.append(this.marca + "\t");
        sb.append(this.numPuertas + "\t");
        
        return sb.toString();
    }

    @Override
    public float calcularCostoAlquiler(int dias) {
       return dias * 50.0f;     //Declaro el numero como float
    }
    
    @Override
    public void realizarMatenimiento(){
        super.setEstadoAlquiler(EstadoVehiculo.EN_MANTENIMIENTO);
    }
    
    
}
