package Gestor;

import Interfaces.IGestion;
import java.time.LocalDate;
import java.util.ArrayList;
import Exceptions.ProductoLimpiezaRepetidoException;
import Interfaces.IGestion;
import Validaciones.ValidadorProductosLimpieza;
import java.time.LocalDate;
import java.util.ArrayList;
import Models.ProductoLimpieza;


public class gestorTienda implements IGestion{ 
    private ArrayList<ProductoLimpieza> productosLimpieza;

    public gestorTienda() {
        this.productosLimpieza = new ArrayList<>();
    }
    
    @Override
    public void Agregar(ProductoLimpieza producto){
        if (this.productosLimpieza.contains(producto)){
           throw new ProductoLimpiezaRepetidoException("El medicamento ya se encuentra cargado!");          //Agrege el equals y la validacion de la fecha en ProductoFarmaceutico
       }
       this.productosLimpieza.add(producto);
       //System.out.println("medicamento Agregado");              //Evitar uso de println solo para uso propio despues eliminar
    }
    
    @Override
    public void Eliminar(ProductoLimpieza producto){
        this.productosLimpieza.remove(producto);
    }

    @Override
    public void Modificar(ProductoLimpieza productoModificado) {
        for (int i = 0; i < productosLimpieza.size(); i++) {
            ProductoLimpieza actual = productosLimpieza.get(i);

            // Si encontramos el producto a modificar
            if (actual.getNombreComercial().equals(productoModificado.getNombreComercial())) {

                // Evita que haya otro producto igual (excepto el mismo que vamos a modificar)
                for (int j = 0; j < productosLimpieza.size(); j++) {
                    if (j != i && productosLimpieza.get(j).equals(productoModificado)) {
                        throw new ProductoLimpiezaRepetidoException("Ya existe un producto con esos datos.");
                    }
                }

                productosLimpieza.set(i, productoModificado);
                return;
            }
        }

    throw new IllegalArgumentException("No se encontró un producto para modificar.");
}
    
    public ArrayList<ProductoLimpieza> FiltrarProductoFarmaceutico(int limiteDiasVencimiento){                //Agrego el filtrado
        LocalDate fechaLimite = LocalDate.now().plusDays(limiteDiasVencimiento);        //Tratar de usar solo plusDays
        ArrayList productosFiltrados = new ArrayList<>();
        
        for (ProductoLimpieza producto : productosLimpieza) {
            //Verifico que este entre las fechas
            if (producto.getFechaVencimiento().isAfter(LocalDate.now()) && producto.getFechaVencimiento().isBefore(fechaLimite)) {
                productosFiltrados.add(producto);
            }
        }
        
        return productosFiltrados;
    }
    
    @Override
    public ArrayList<ProductoLimpieza> Listar(){
        return this.productosLimpieza;
    }
    
}
