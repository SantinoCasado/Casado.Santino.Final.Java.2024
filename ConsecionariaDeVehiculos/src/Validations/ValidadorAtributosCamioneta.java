package Validations;

import Enums.MarcasCamioneta;
import Exceptions.DatoErroneoException;

public class ValidadorAtributosCamioneta {
    //MARCA
    public static void validarMarca(String marca){
        if(marca == null){
            throw new DatoErroneoException("Seleccione una marca de camioneta.");
        }
    }
    
    //CAPACIDAD DE CARGA
    public static void validarCapacidadCarga(String capacidadCargaKg){
        if( capacidadCargaKg == null || capacidadCargaKg.trim().isEmpty()){             // Verificar si es nulo o vacio
            throw new DatoErroneoException("Informe su capacidad de carga.");
        }else{
            try {
                float capacidadFloat = Float.parseFloat(capacidadCargaKg.trim());       // Intentar convertir a float
                
                if (capacidadFloat < 500) {     // Verificar que sea entre 500 y 1500
                    throw new DatoErroneoException("El minimo de carga de una camioneta es de 500kg.");
                }
                if (capacidadFloat > 1500) {    // Verificar que sea entre 500 y 1500
                    throw new DatoErroneoException("El maximo de carga de una camioneta es de 1500kg.");
                }
                
            } catch (NumberFormatException e) {
                 throw new DatoErroneoException("Solo se aceptan numeros!.");
            }
          }
 
    }
}

