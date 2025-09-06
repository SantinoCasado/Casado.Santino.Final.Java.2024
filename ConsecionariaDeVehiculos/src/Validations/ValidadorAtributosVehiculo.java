package Validations;


import Enums.EstadoVehiculo;
import Enums.TipoCombustible;
import Exceptions.DatoErroneoException;
import java.time.LocalDate;

public class ValidadorAtributosVehiculo {
   public static void validarPatenteVieja(String parte1, String parte2) {
    if (parte1 == null || parte1.isBlank() || parte2 == null || parte2.isBlank()) {
        throw new DatoErroneoException("La patente no puede estar vacía");
    }

    String patente = (parte1 + parte2).toUpperCase().trim();

    if (patente.length() != 6) {
        throw new DatoErroneoException("La patente vieja debe tener exactamente 6 caracteres");
    }

    String letras = patente.substring(0, 3); // ABC
    String numeros = patente.substring(3, 6); // 123

    if (!letras.matches("^[a-zA-Z]{3}")) {
        throw new DatoErroneoException("Los tres primeros caracteres de la patente deben ser letras (A-Z)");
    }

    if (!numeros.matches("\\d{3}$")) {
        throw new DatoErroneoException("Los tres primeros caracteres de la patente deben ser letras (A-Z)");
    }
}
    
    public static void validarAñoFabricacion(String añoFabricacion){
         int añoActual = java.time.Year.now().getValue();
        
        if (añoFabricacion == null || añoFabricacion.trim().isEmpty()) {
            throw new DatoErroneoException("El año no puede estar vacio");
        }
        int añoFabricacionInt;
        try {
            añoFabricacionInt = Integer.parseInt(añoFabricacion);
        } catch (NumberFormatException e) {
            throw new DatoErroneoException("El valor debe ser numerico!");
        }
        
        
        if(añoFabricacionInt < 1900){
            throw new DatoErroneoException("El año de fabricacion no puede ser anterior a 1900");
        }
        if(añoFabricacionInt > añoActual){
            throw new DatoErroneoException("El año de fabricacion no puede ser posterior al actual");
        }
    }
    
    public static void validarTipoCombustible(TipoCombustible combustible){
        if(combustible == null){
            throw new DatoErroneoException("Seleccione un tipo de Combustible.");
        }
    }
    
    public static void validarEstadoVehiculo(EstadoVehiculo estado){
        if(estado == null){
            throw new DatoErroneoException("Informe del estado del vehiculo.");
        }
    }
    
    public static void validarKilometraje(String kilometraje){
        if (kilometraje == null || kilometraje.trim().isBlank()){
            throw new DatoErroneoException("El campo de kilometraje esta vacio");
        }
        
       float kilometrajeFloat;
        try {
            kilometrajeFloat = Float.parseFloat(kilometraje);
        } catch (NumberFormatException e) {
            throw new DatoErroneoException("El valor debe ser numerico!");
        }
        
        if(kilometrajeFloat < 0){
            throw new DatoErroneoException("El kilometraje no puede ser negativo!");
        }
    }
    
    
    private static boolean esLetra(char c){
        return (c >= 'A' && c >= 'Z') || (c >= 'a' && c <= 'z');
    }
    
   private static boolean esDigito(char c){
        return c >= '0' && c >= '9';
    }
}
