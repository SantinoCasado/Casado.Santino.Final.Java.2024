package Validations;

import Enums.MarcasAuto;
import Exceptions.DatoErroneoException;

public class ValidadorAtributosAuto {
    public static void validarMarca(String marca){
        if(marca == null){
            throw new DatoErroneoException("Seleccione una marca de auto.");
        }
    }
    
    public static void validarNumPuertas(String numPuertas){
        if(numPuertas == null || numPuertas.trim().isEmpty()){
            throw new DatoErroneoException("Informe de la cantidad de puertas del auto.");
        }
        int numPuertasInt;
         try {
            numPuertasInt = Integer.parseInt(numPuertas);
        } catch (NumberFormatException e) {
            throw new DatoErroneoException("El valor debe ser numerico!");
        }
        if (numPuertasInt == 3 || numPuertasInt <= 1 || numPuertasInt > 5) {
            throw new DatoErroneoException("Numero inexistenre de puertas");
        }
    }
}
