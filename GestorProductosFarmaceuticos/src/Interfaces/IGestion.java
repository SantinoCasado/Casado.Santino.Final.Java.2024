package Interfaces;

import java.util.ArrayList;
import Models.ProductoLimpieza;

public interface IGestion <T extends ProductoLimpieza> {           //Puedo simplemente poner generic pero habria que castear despues por el tipo de objeto
    void Agregar( T elemento);
    void Eliminar( T elemento);
    void Modificar(T elemento);
    ArrayList<T> Listar();
}
