package Interfaces;

public interface ISerializableCsv {
    String toCSV();        //convierte cada objeto para poder escribir o lleer una línea con formato CSV.
    ISerializableCsv fromCSV(String data);          // para deserializar una cadena CSV y convertirla en una instancia de una clase que implementa esa interfaz.
}
