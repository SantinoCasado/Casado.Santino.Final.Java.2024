package Validations;

import Enums.MarcasCamioneta;
import Exceptions.DatoErroneoException;

public class ValidadorAtributosCamioneta {
    public static void validarMarca(MarcasCamioneta marca){
        if(marca == null){
            throw new DatoErroneoException("Seleccione una marca de camioneta.");
        }
    }
    
    public static void validarCapacidadaCarga(String capacidadCargaKg){
        if( capacidadCargaKg == null || capacidadCargaKg.trim().isEmpty()){
            throw new DatoErroneoException("Informe del estado del vehiculo.");
        }else{
            try {
                float capacidadFloat = Float.parseFloat(capacidadCargaKg.trim());
                
                if (capacidadFloat < 500) {
                    throw new DatoErroneoException("El minimo de carga de una camioneta es de 500kg.");
                }
                if (capacidadFloat > 1500) {
                    throw new DatoErroneoException("El maximo de carga de una camioneta es de 1500kg.");
                }
                
            } catch (NumberFormatException e) {
                 throw new DatoErroneoException("Solo se aceptan numeros!.");
            }
          }
 
    }
}

