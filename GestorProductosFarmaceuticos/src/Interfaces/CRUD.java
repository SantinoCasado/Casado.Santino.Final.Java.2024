package Interfaces;

import Exceptions.PatenteRepetidaException;
import Models.Vehiculo;
import java.util.ArrayList;

public interface CRUD <T extends Vehiculo>{
    void agregar(T entidad) throws PatenteRepetidaException;
    void modificar(T entidad);
    void eliminar(String patente);
    
    T buscarPorPatente(String patente);
    ArrayList<T> listarTodo();
}
