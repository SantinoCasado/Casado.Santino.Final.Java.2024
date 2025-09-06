package Interfaces;

import Enums.EstadoVehiculo;
import Enums.TipoVehiculos;
import Exceptions.PatenteRepetidaException;
import Models.Vehiculo;
import java.util.ArrayList;

public interface CRUD <T extends Vehiculo>{
    void agregar(T entidad) throws PatenteRepetidaException;
    void modificar(T entidad);
    void eliminar(T entidad);
    
    ArrayList<T> buscarPorTipos(TipoVehiculos tipoVehiculo, EstadoVehiculo estado);
    ArrayList<T> listarTodo();
}
