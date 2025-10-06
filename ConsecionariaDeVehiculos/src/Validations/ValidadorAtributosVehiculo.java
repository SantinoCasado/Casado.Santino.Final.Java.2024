package Validations;


import Enums.EstadoVehiculo;
import Enums.TipoCombustible;
import Exceptions.DatoErroneoException;
import javafx.scene.control.DatePicker;

public class ValidadorAtributosVehiculo {

    //PATENTE
    public static void validarPatenteVieja(String parte1, String parte2) {          // Validar patente vieja ABC 123
        if (parte1 == null || parte1.isBlank() || parte2 == null || parte2.isBlank()) {     // Verificar si es nulo o vacio
            throw new DatoErroneoException("La patente no puede estar vacía");
        }

        String patente = (parte1 + parte2).toUpperCase().trim();            // Unir partes y convertir a mayusculas

        if (patente.length() != 6) {        // Verificar longitud
            throw new DatoErroneoException("La patente vieja debe tener exactamente 6 caracteres");
        }

        String letras = patente.substring(0, 3); // ABC
        String numeros = patente.substring(3, 6); // 123

        if (!letras.matches("^[a-zA-Z]{3}")) {  // Verificar que sean letras
            throw new DatoErroneoException("Los tres primeros caracteres de la patente deben ser letras (A-Z)");
        }

        if (!numeros.matches("\\d{3}$")) {      // Verificar que sean numeros
            throw new DatoErroneoException("Los tres últimos caracteres de la patente deben ser números (0-9)");
        }
    }
    
    public static void validarPatenteNueva(String parte1, String parte2, String parte3) {          // Validar patente nueva AB 123 CD
        if (parte1 == null || parte1.isBlank() || parte2 == null || parte2.isBlank() || parte3 == null || parte3.isBlank()) {     // Verificar si es nulo o vacio
            throw new DatoErroneoException("La patente no puede estar vacía");
        }

        String patente = (parte1 + parte2 + parte3).toUpperCase().trim();            // Unir partes y convertir a mayusculas

        if (patente.length() != 7) {        // Verificar longitud
            throw new DatoErroneoException("La patente nueva debe tener exactamente 7 caracteres");
        }

        String letras = patente.substring(0, 2); // AB
        String numeros = patente.substring(2, 5); // 123
        String letras2 = patente.substring(5, 7); // CD

        if (!letras.matches("^[a-zA-Z]{2}$")) {  // Verificar que sean 2 letras
            throw new DatoErroneoException("Los dos primeros caracteres de la patente deben ser letras (A-Z)");
        }
        
         if (!letras2.matches("^[a-zA-Z]{2}$")) {  // Verificar que sean 2 letras
            throw new DatoErroneoException("Los ultimos dos caracteres de la patente deben ser letras (A-Z)");
        }

        if (!numeros.matches("\\d{3}$")) {      // Verificar que sean numeros
            throw new DatoErroneoException("Los tres caracteres del medio de la patente deben ser números (0-9)");
        }
    }
    
    //AÑO FABRICACION
    public static void validarAñoFabricacion(String añoFabricacion){
        int añoActual = java.time.Year.now().getValue();        // Obtener el año actual
        
        if (añoFabricacion == null || añoFabricacion.trim().isEmpty()) {    // Verificar si es nulo o vacio
            throw new DatoErroneoException("El año no puede estar vacio");
        }
        int añoFabricacionInt;      // Variable para almacenar el año convertido
        try {
            añoFabricacionInt = Integer.parseInt(añoFabricacion);   // Intentar convertir a int
        } catch (NumberFormatException e) {
            throw new DatoErroneoException("El valor debe ser numerico!");
        }
        
        
        if(añoFabricacionInt < 1900){       // Verificar que no sea menor a 1900
            throw new DatoErroneoException("El año de fabricacion no puede ser anterior a 1900");
        }
        if(añoFabricacionInt > añoActual){  // Verificar que no sea mayor al año actual
            throw new DatoErroneoException("El año de fabricacion no puede ser posterior al actual");
        }
    }
    
    //COMBUSTIBLE
    public static void validarTipoCombustible(TipoCombustible combustible){
        if(combustible == null){    // Verificar si es nulo
            throw new DatoErroneoException("Seleccione un tipo de Combustible.");
        }
    }
    
    //ESTADO VEHICULO
    public static void validarEstadoVehiculo(EstadoVehiculo estado){
        if(estado == null){     // Verificar si es nulo
            throw new DatoErroneoException("Informe del estado del vehiculo.");
        }
    }
    
    //KILOMETRAJE
    public static void validarKilometraje(String kilometraje){
        if (kilometraje == null || kilometraje.trim().isBlank()){       // Verificar si es nulo o vacio
            throw new DatoErroneoException("El campo de kilometraje esta vacio");
        }
        
        float kilometrajeFloat;      // Variable para almacenar el km en float
        try {
            kilometrajeFloat = Float.parseFloat(kilometraje);       // Intentar convertir a float
        } catch (NumberFormatException e) {
            throw new DatoErroneoException("El valor debe ser numerico!");
        }
        
        if(kilometrajeFloat < 0){   // Verificar que no sea negativo
            throw new DatoErroneoException("El kilometraje no puede ser negativo!");
        }
    }

    //FECHA FUTURA
    public static void validarFechaFutura(DatePicker dp){   // Validar que la fecha sea futura
        if (dp.getValue() == null || !dp.getValue().isAfter(java.time.LocalDate.now())) {   // Verificar si es nulo o no es posterior a la actual
            throw new DatoErroneoException("La fecha debe ser posterior a la actual.");
        }
    }
    
    //-- METODOS AUXILIARES --//
    private static boolean esLetra(char c){
        return (c >= 'A' && c >= 'Z') || (c >= 'a' && c <= 'z');    // Verificar si es letra, mayuscula o minuscula
    }
    
   private static boolean esDigito(char c){                         // Verificar si es digito
        return c >= '0' && c >= '9';
    }
}
