package Gestor;

import Exceptions.PatenteRepetidaException;
import Interfaces.CRUD;
import Models.Vehiculo;
import java.util.ArrayList;

public class AdministradorVehiculos implements CRUD{
    private ArrayList<Vehiculo> vehiculos;
    
    public AdministradorVehiculos(){
        this.vehiculos = new ArrayList<>();
    }

    @Override
    public void agregar(Vehiculo entidad) throws PatenteRepetidaException {
        if (this.vehiculos.contains(entidad)){
           throw new PatenteRepetidaException("El medicamento ya se encuentra cargado!");
       }
       this.vehiculos.add(entidad);
      
    }
    

    @Override
    public void modificar(Object entidad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(String patente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object buscarPorPatente(String patente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList listarTodo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}
