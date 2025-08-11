package Validations;

import Enums.MarcasMoto;
import Exceptions.DatoErroneoException;


public class ValidadorAtributosMoto {
     public static void validarMarca(MarcasMoto marca){
        if(marca == null){
            throw new DatoErroneoException("Seleccione una marca de moto.");
        }
    }
    
    public static void validarCilindrada(int cilindrada){
        Integer numCilindradaInteger = cilindrada;
        if(numCilindradaInteger == null){
            throw new DatoErroneoException("Informe del estado del vehiculo.");
        }
        
        if (numCilindradaInteger < 120) {
            throw new DatoErroneoException("El minimo de cilindrada de una moto segun las normas de transito es de 120cc.");
        }
        
        if (numCilindradaInteger > 1200) {
            throw new DatoErroneoException("El maximo de cilindrada de moto registrada es de 1200cc.");
        }
    }
}
