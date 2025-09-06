package Gestor;

import Enums.EstadoVehiculo;
import Enums.TipoVehiculos;
import Exceptions.PatenteRepetidaException;
import Interfaces.CRUD;
import Models.Vehiculo;
import java.util.ArrayList;

public class AdministradorVehiculos implements CRUD<Vehiculo>{
    private ArrayList<Vehiculo> vehiculos;
    private ArrayList <Vehiculo> vehiculosFiltrados;
    
    public AdministradorVehiculos(){
        this.vehiculos = new ArrayList<>();
        this.vehiculosFiltrados = new ArrayList<>();
    }

    @Override
    public void agregar(Vehiculo entidad) throws PatenteRepetidaException {
        if (this.vehiculos.contains(entidad)){
           throw new PatenteRepetidaException("El medicamento ya se encuentra cargado!");
       }
       this.vehiculos.add(entidad);
    }
    

    @Override
    public void modificar(Vehiculo vehiculo) {
        for (int i = 0; i < this.vehiculos.size(); i++) {
                    Vehiculo actual = this.vehiculos.get(i);

                    // Si encontramos el producto a modificar
                    if (actual.equals(vehiculo)) {

                        // Evita que haya otro producto igual (excepto el mismo que vamos a modificar)
                        for (int j = 0; j < this.vehiculos.size(); j++) {
                            if (j != i && this.vehiculos.get(j).equals(vehiculo)) {
                                throw new PatenteRepetidaException("Ya existe un vehiculo con esta patente!.");
                            }
                        }

                        this.vehiculos.set(i, vehiculo);
                        return;
                    }
                }

            throw new IllegalArgumentException("No se encontró un vehiculo para modificar.");
}

    @Override
    public void eliminar(Vehiculo vehiculo) {
        this.vehiculos.remove(vehiculo);
    }
    
    @Override
    public  ArrayList<Vehiculo> buscarPorTipos(TipoVehiculos tipoVehiculo, EstadoVehiculo estado){
        if (tipoVehiculo == null | estado == null) {
            throw new IllegalArgumentException("Tipo o estado de vehiculo invalido");
        }
        for (Vehiculo vehiculoFor : vehiculos){
            boolean coincideTipo = (tipoVehiculo == TipoVehiculos.TODOS || vehiculoFor.getTipo() == tipoVehiculo);
            boolean coincideEstado = (estado == EstadoVehiculo.TODOS || vehiculoFor.getEstadoVehiculo() == estado);
            
            if(coincideTipo && coincideEstado){
                this.vehiculosFiltrados.add(vehiculoFor);
            }
        }
        return this.vehiculosFiltrados;
    }

    @Override
    public ArrayList<Vehiculo> listarTodo() {
        return this.vehiculos;
    }
}
