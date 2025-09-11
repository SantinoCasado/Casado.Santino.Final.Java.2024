package Gestor;

import Enums.EstadoVehiculo;
import Enums.TipoVehiculos;
import Exceptions.PatenteRepetidaException;
import Interfaces.CRUD;
import Models.Auto;
import Models.Camioneta;
import Models.Moto;
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

            System.out.println("Comparando " + actual.getPatente() + " con " + vehiculo.getPatente());
            // Si encontramos el producto a modificar
            if (actual.equals(vehiculo)) {

                // Evita que haya otro producto igual (excepto el mismo que vamos a modificar)
                for (int j = 0; j < this.vehiculos.size(); j++) {
                    if (j != i && this.vehiculos.get(j).equals(vehiculo)) {
                        throw new PatenteRepetidaException("Ya existe un vehiculo con esta patente!.");
                    }
                }

                // Actualiza solo los atributos editables
                actual.setEstadoVehiculo(vehiculo.getEstadoVehiculo());
                actual.setFechaAlquiler(vehiculo.getFechaAlquiler());
                actual.setKilometros(vehiculo.getKilometros());
                actual.setAñoFabricacion(vehiculo.getAñoFabricacion());
                actual.setTipo(vehiculo.getTipo());


                if(vehiculo instanceof Auto auto){
                    auto.setNumPuertas(((Auto) vehiculo).getNumPuertas());
                    auto.setMarca(((Auto) vehiculo).getMarca());

                }else  if(vehiculo instanceof Moto moto){
                    moto.setCilindrada(((Moto) vehiculo).getCilindrada());
                    moto.setMarca(((Moto) vehiculo).getMarca());

                }else if(vehiculo instanceof Camioneta camioneta){
                    camioneta.setCampacidadCargaKg(((Camioneta) vehiculo).getCampacidadCargaKg());
                    camioneta.setMarca(((Camioneta) vehiculo).getMarca());
                }

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
    public ArrayList<Vehiculo> buscarPorTipos(TipoVehiculos tipoVehiculo, EstadoVehiculo estado) {
        if (tipoVehiculo == null || estado == null) {
            throw new IllegalArgumentException("Tipo o estado de vehiculo invalido");
        }
        ArrayList<Vehiculo> filtrados = new ArrayList<>();
        for (Vehiculo vehiculoFor : vehiculos) {
            boolean coincideTipo = (tipoVehiculo == TipoVehiculos.TODOS || vehiculoFor.getTipo() == tipoVehiculo);
            boolean coincideEstado = (estado == EstadoVehiculo.TODOS || vehiculoFor.getEstadoVehiculo() == estado);

            if (coincideTipo && coincideEstado) {
                filtrados.add(vehiculoFor);
            }
        }
        return filtrados;
    }

    @Override
    public ArrayList<Vehiculo> listarTodo() {
        return this.vehiculos;
    }
}
