package Validations;


import Enums.EstadoVehiculo;
import Enums.TipoCombustible;
import Exceptions.DatoErroneoException;
import java.time.LocalDate;

public class ValidadorAtributosVehiculo {
    public static void validarPatente(String patente){
        if (patente == null || patente.isBlank()) {
            throw new DatoErroneoException("La patente no puede estar vacia");
        }

        if(patente.length() != 7){
             throw new DatoErroneoException("La patente de una vehiculo tiene 7 caracteres");
        }else{
             //Validacion de la primer parte solo letras
            for (int i = 0; i < 2; i++) {
               if(!esLetra(patente.charAt(i))){
                    throw new DatoErroneoException("La primer parte de la patente solo tiene letras");
               }
            }

            //Validacion de la segunda parte solo numeros
           for (int i = 2; i < 5; i++) {
               if(!esLetra(patente.charAt(i))){
                    throw new DatoErroneoException("La segunda parte de la patente solo tiene numeros");
               }
            }

            //Validacion de la tercer parte solo letras
            for (int i = 5; i < 7; i++) {
                if(!esDigito(patente.charAt(i))){
                    throw new DatoErroneoException("La tercer parte de la patente solo tiene letras");
                }
            }
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
    
    public static boolean validarFecha(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        return !fecha.isBefore(hoy); // true si es hoy o en el futuro
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
