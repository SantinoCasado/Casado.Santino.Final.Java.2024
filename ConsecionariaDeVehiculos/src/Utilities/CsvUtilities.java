package Utilities;

import Interfaces.ISerializableCsv;
import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

public class CsvUtilities<T extends ISerializableCsv> {

    //VARIABLES
    private static final String CARPETA = "src/Files";
    private static final String ARCHIVO_CSV = CARPETA + File.separator + "vehiculos.csv";

    //------------------------------------ METODOS ------------------------------------------------------------------------------------------------------------------------------------------------------
    //GUARDADO
    public static <T extends ISerializableCsv> void guardarVehiculosCSV(ArrayList<T> lista) {
        File dir = new File(CARPETA);
        if (!dir.exists()) {    // Crear el directorio si no existe
            dir.mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_CSV))) { // Sobrescribe el archivo
            // Escribir cada objeto en una línea
            for (T item : lista) {
                bw.write(item.toCSV());
                bw.newLine();
            }
            System.out.println("Archivo CSV guardado en: " + ARCHIVO_CSV);
        } catch (IOException e) {
            System.err.println("Error al escribir el CSV: " + e.getMessage());
        }
    }

    //LECTURA
    public static ArrayList<String> leerCSV() {
        ArrayList<String> lista = new ArrayList<>();    // Lista para almacenar las líneas del CSV
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CSV))) { // Lee el archivo
            String linea;
            while ((linea = br.readLine()) != null) {       // Lee línea por línea
                lista.add(linea);                           // Agrega la línea a la lista
            }
        } catch (IOException e) {
            System.err.println("Error al leer el CSV: " + e.getMessage());
        }
        return lista;
    }

    //CARGA
    public static <T extends ISerializableCsv> ArrayList<T> cargarVehiculosCSV(Function<String, T> parseador) { // Recibe una función para parsear las líneas
        ArrayList<T> lista = new ArrayList<>();     // Lista para almacenar los objetos creados
        ArrayList<String> lineas = leerCSV();       // Lee las líneas del CSV
        for (String linea : lineas) {
            T objeto = parseador.apply(linea);      // Usa la función de parseo para crear el objeto
            if (objeto != null) {                   // Si el objeto es válido, lo agrega a la lista
                lista.add(objeto);
            }
        }
        return lista;
    }
}