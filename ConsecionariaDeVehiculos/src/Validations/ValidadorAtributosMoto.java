package Validations;

import Enums.MarcasMoto;
import Exceptions.DatoErroneoException;


public class ValidadorAtributosMoto {
     public static void validarMarca(String marca){
        if(marca == null){
            throw new DatoErroneoException("Seleccione una marca de moto.");
        }
    }
    
    public static void validarCilindrada(String cilindrada){
        if( cilindrada == null || cilindrada.trim().isEmpty()){
            throw new DatoErroneoException("Informe de su cilindrada.");
        }else{
            try {
                int cilindradaInt = Integer.parseInt(cilindrada.trim());
                
                if (cilindradaInt < 120) {
                    throw new DatoErroneoException("El minimo de cilindrada de una moto segun las normas de transito es de 120cc.");
                }
                if (cilindradaInt > 1200) {
                    throw new DatoErroneoException("El maximo de cilindrada de moto registrada es de 1200cc.");
                }
                
            } catch (NumberFormatException e) {
                 throw new DatoErroneoException("Solo se aceptan numeros!.");
            }
          }
    }
}
