package Validations;

import Enums.MarcasMoto;
import Exceptions.DatoErroneoException;


public class ValidadorAtributosMoto {
    //MARCA
     public static void validarMarca(String marca){
        if(marca == null){
            throw new DatoErroneoException("Seleccione una marca de moto.");
        }
    }
    
    //CILINDRADA
    public static void validarCilindrada(String cilindrada){
        if( cilindrada == null || cilindrada.trim().isEmpty()){             // Verificar si es nulo o vacio
            throw new DatoErroneoException("Informe de su cilindrada.");
        }else{
            try {
                int cilindradaInt = Integer.parseInt(cilindrada.trim());    // Intentar convertir a int
                
                if (cilindradaInt < 120) {          // Verificar que sea entre 120 y 1200
                    throw new DatoErroneoException("El minimo de cilindrada de una moto segun las normas de transito es de 120cc.");
                }
                if (cilindradaInt > 1200) {         // Verificar que sea entre 120 y 1200
                    throw new DatoErroneoException("El maximo de cilindrada de moto registrada es de 1200cc.");
                }
                
            } catch (NumberFormatException e) {
                 throw new DatoErroneoException("Solo se aceptan numeros!.");
            }
          }
    }
}
