package Validations;

import Enums.MedidasConcentracion;
import Exceptions.*;
import java.time.LocalDate;

public class ValidadorDatosProductos {
    public static void validarNombre(String nombre){
        if (nombre == null || nombre.isBlank()) {
            throw new DatoErroneoException("El nombre no puede estar vacío.");
        }
    }
     public static void validarConcentracion(MedidasConcentracion concentracion){
        if (concentracion == null) {
            throw new DatoErroneoException("La concentracion no puede estar vacía.");
        }
      }
      
    public static void ValidarFechaVencimiento(LocalDate fechaVencimiento){
        if (fechaVencimiento == null) {
            throw new DatoErroneoException("La fecha de vencimiento no puede ser anterior a hoy.");
        }
        if (fechaVencimiento.isBefore(LocalDate.now())) {
            throw new ProductoVencidoException("No se puede agregar productos vencidos!!");
        }
    }
}
