package Exceptions;


public class DatoErroneoException extends RuntimeException{
        public DatoErroneoException(String mensaje) {
            super(mensaje);
    }
}
