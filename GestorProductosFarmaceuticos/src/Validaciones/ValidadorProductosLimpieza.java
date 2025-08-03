package Validaciones;

import Exceptions.*;
import java.time.LocalDate;


public class ValidadorProductosLimpieza {

    public static void validarNombre(String nombre){
        if (nombre == null || nombre.isBlank()) {
            throw new DatoErroneoException("El nombre no puede estar vacío.");
        }
    }
     public static void validarConcentracion(String concentracion){
        if (concentracion == null || concentracion.isBlank()) {
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


