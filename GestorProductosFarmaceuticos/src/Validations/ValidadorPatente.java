package Validations;

import Models.Enums.MarcasCamioneta;
import Exceptions.*;
import Models.Auto;
import Models.Camioneta;
import Models.Moto;
import Models.Vehiculo;
import java.time.LocalDate;

public class ValidadorPatente {
    public static void validarPatente(Vehiculo vehiculo){
        String patente = vehiculo.getPatente();
        
        if (!(patente == null || patente.isBlank())) {
            throw new DatoErroneoException("La patente no puede estar vacia");
        }
        if(vehiculo instanceof Auto && patente.length() != 7){
             throw new DatoErroneoException("La patente de un auto tiene 7 caracteres");
        }

        
        if(vehiculo instanceof Camioneta && patente.length() != 7){
             throw new DatoErroneoException("La patente de una camioneta tiene 7 caracteres");
        }
    }
    
    public static void validarValorPatenteMoto(Vehiculo vehiculo){
        String patente = vehiculo.getPatente();
         
         //Validacion de la patente de la moto
         if(vehiculo instanceof Moto){
            for (int i = 0; i < 3; i++) {
                if(!esLetra(patente.charAt(i))){
                    throw new DatoErroneoException("La primer parte de la patente de una moto solo tiene letras");
                }
            }
                
            for (int i = 3; i < 6; i++) {
                if(!esDigito(patente.charAt(i))){
                    throw new DatoErroneoException("La segunda parte de la patente de una moto solo tiene numeros");
                }
            }
         }
    }
    
    public static void validarValorPatenteAuto(Vehiculo vehiculo){
        String patente = vehiculo.getPatente();
            
         //Validacion de la patente de la moto
         if(vehiculo instanceof Auto){
            for (int i = 0; i < 2; i++) {
                if(!esLetra(patente.charAt(i))){
                    throw new DatoErroneoException("La primer parte de la patente de un auto solo tiene letras");
                }
            }
                
            for (int i = 2; i < 5; i++) {
                if(!esDigito(patente.charAt(i))){
                    throw new DatoErroneoException("La segunda parte de la patente de un auto solo tiene numeros");
                }
            }
            
            for (int i = 5; i < 7; i++) {
                if(!esDigito(patente.charAt(i))){
                    throw new DatoErroneoException("La tercer parte de la patente de un auto solo tiene letras");
                }
            }
         }
    }
    
   public static void validarValorPatenteCamioneta(Vehiculo vehiculo){
        String patente = vehiculo.getPatente();
            
         //Validacion de la patente de la moto
         if(vehiculo instanceof Camioneta){
            for (int i = 0; i < 2; i++) {
                if(!esLetra(patente.charAt(i))){
                    throw new DatoErroneoException("La primer parte de la patente de una camioneta solo tiene letras");
                }
            }
                
            for (int i = 2; i < 5; i++) {
                if(!esDigito(patente.charAt(i))){
                    throw new DatoErroneoException("La segunda parte de la patente de una cmioneta solo tiene numeros");
                }
            }
            
            for (int i = 5; i < 7; i++) {
                if(!esDigito(patente.charAt(i))){
                    throw new DatoErroneoException("La tercer parte de la patente de una camioneta solo tiene letras");
                }
            }
         }
    }
    
    
    private static boolean esLetra(char c){
        return (c >= 'A' && c >= 'Z') || (c >= 'a' && c <= 'z');
    }
    
   private static boolean esDigito(char c){
        return c >= '0' && c >= '9';
    }
}
