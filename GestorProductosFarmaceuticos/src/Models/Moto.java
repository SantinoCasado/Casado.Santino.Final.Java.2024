package Models;

import Interfaces.IMantenible;
import Enums.EstadoVehiculo;
import Enums.MarcasMoto;
import Enums.TipoCombustible;
import Enums.TipoVehiculos;

public class Moto extends Vehiculo implements IMantenible{
    private MarcasMoto marca;
    private int cilindrada;
    
    
    //Constructores
    public Moto() {
    }

    public Moto(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estado) {
        super(tipo, patente, añoFabricacion, tipoCombustible, horasUso, estado);
        this.marca = MarcasMoto.HONDA;
        this.cilindrada = 125;
    }

    public Moto(TipoVehiculos tipo, String patente, int añoFabricacion, TipoCombustible tipoCombustible, float horasUso, EstadoVehiculo estado, MarcasMoto marca, int cilindrada){
        super(tipo, patente, añoFabricacion, tipoCombustible, horasUso, estado);
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
        sb.append("Moto" + "\t");
        sb.append(super.getPatente() + "\t");
        sb.append(super.getAñoFabricacion() + "\t");
        sb.append(super.getTipoCombustible() + "\t");
        sb.append(super.getHorasUso() + "\t");
        sb.append(this.marca + "\t");
        sb.append(this.cilindrada + "\t");
        
        return sb.toString();
    }

    @Override
    public float calcularCostoAlquiler(int dias) {
       return dias * 30.0f;     //Declaro el numero como float
    }
    
    @Override
    public void realizarMatenimiento(){
        super.setEstadoVehiculo(EstadoVehiculo.EN_MANTENIMIENTO);
    }
}
