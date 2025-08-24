package Models;

import Interfaces.IMantenible;
import Enums.EstadoVehiculo;
import Enums.MarcasAuto;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;

public class Auto extends Vehiculo implements IMantenible{
    private MarcasAuto marca;
    private int numPuertas;
    
    public Auto(){        
    }
    
    public Auto(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estadoVehiculo){
        super(tipo, patente, añoFabricacion, tipoCombustible, horasUso, estadoVehiculo);
        this.marca = MarcasAuto.FORD;
        this.numPuertas = 4;
    }
    
    public Auto(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estadoVehiculo, MarcasAuto marca, int numPuertas){
        super(tipo, patente, añoFabricacion, tipoCombustible, horasUso, estadoVehiculo);
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
        sb.append(    super.getPatente() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getAñoFabricacion() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getTipoCombustible() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(super.getHorasUso() + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
        sb.append(this.marca + "\t" + "\t" + "\t" + "\t" + "\t" + "\t");
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
    public void realizarMatenimiento(){
        super.setEstadoVehiculo(EstadoVehiculo.EN_MANTENIMIENTO);
    }
    
    
}
