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

    if (!letras.matches("[A-Z]{3}")) {
        throw new DatoErroneoException("Los tres primeros caracteres deben ser letras (A-Z)");
    }

    if (!numeros.matches("\\d{3}")) {
        throw new DatoErroneoException("Los tres últimos caracteres deben ser números (0-9)");
    }
}
    
    public static void validarAñoFabricacion(int añoFabricacion){
         Integer añoInteger = añoFabricacion;
         int añoActual = java.time.Year.now().getValue();
        
        if (añoInteger == null) {
            throw new DatoErroneoException("El año no puede estar vacio");
        }
        
        if(añoFabricacion < 1900){
            throw new DatoErroneoException("El año de fabricacion no puede ser anterior a 1900");
        }
        if(añoFabricacion < añoActual){
            throw new DatoErroneoException("El año de fabricacion no puede ser posterior al actual");
        }
    }
    
    public static void validarFecha(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        if (fecha.isBefore(hoy)) {
             throw new DatoErroneoException("La fecha no puede ser anterior a hoy!.");
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
    
    
    private static boolean esLetra(char c){
        return (c >= 'A' && c >= 'Z') || (c >= 'a' && c <= 'z');
    }
    
   private static boolean esDigito(char c){
        return c >= '0' && c >= '9';
    }
}
