package Utilities;

import Interfaces.ISerializableCsv;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvUtilisGeneric<T extends ISerializableCsv> { 
    
    public void escribirCSV(ArrayList<T> lista) {
        // Ruta relativa a la carpeta del proyecto
        String archivoCsv = "src/DatosFormularios/datos.csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCsv))) {
            for (T item : lista) {
                bw.write(item.toCSV());
                bw.newLine();
            }
            System.out.println("Archivo CSV guardado en: " + archivoCsv);
        } catch (IOException e) {
            System.err.println("Error al escribir el CSV: " + e.getMessage());
        }
    }

    public ArrayList<String> leerCSV(String ruta){
        // Leer el CSV
        ArrayList<String> lista = new ArrayList<>(); 

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {       // Abre un archivo CSV ubicado en la ruta que le pasás como parámetro (ruta).
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(linea);       // Cada línea leída (que representa una fila del CSV) se agrega a una lista de tipo ArrayList<String>.
            }
        } catch (IOException e) {
            System.err.println("Error al leer el CSV: " + e.getMessage());      // Si ocurre un error al leer el archivo, lo captura y muestra un mensaje de error.
        }
        return lista;
    }
    
}

