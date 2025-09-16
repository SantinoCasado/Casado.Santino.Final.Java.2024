package Validations;

import Enums.MarcasAuto;
import Exceptions.DatoErroneoException;

public class ValidadorAtributosAuto {

    //MARCA
    public static void validarMarca(String marca){
        if(marca == null){
            throw new DatoErroneoException("Seleccione una marca de auto.");
        }
    }
    
    //NUMERO DE PUERTAS
    public static void validarNumPuertas(String numPuertas){
        if(numPuertas == null || numPuertas.trim().isEmpty()){                                    // Verificar si es nulo o vacio
            throw new DatoErroneoException("Informe de la cantidad de puertas del auto.");
        }
        int numPuertasInt;
         try {
            numPuertasInt = Integer.parseInt(numPuertas);                                         // Intentar convertir a entero
        } catch (NumberFormatException e) {
            throw new DatoErroneoException("El valor debe ser numerico!");
        }
        if (numPuertasInt == 3 || numPuertasInt <= 1 || numPuertasInt > 5) {                    // Verificar que sea 2, 4 o 5
            throw new DatoErroneoException("Numero inexistente de puertas");
        }
    }
}
