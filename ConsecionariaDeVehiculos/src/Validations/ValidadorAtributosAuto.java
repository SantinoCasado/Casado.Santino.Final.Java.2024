package Validations;

import Enums.MarcasAuto;
import Exceptions.DatoErroneoException;

public class ValidadorAtributosAuto {
    public static void validarMarca(String marca){
        if(marca == null){
            throw new DatoErroneoException("Seleccione una marca de auto.");
        }
    }
    
    public static void validarNumPuertas(int numPuertas){
        Integer numPuertasInteger = numPuertas;
        if(numPuertasInteger == null){
            throw new DatoErroneoException("Informe del estado del vehiculo.");
        }
    }
}
