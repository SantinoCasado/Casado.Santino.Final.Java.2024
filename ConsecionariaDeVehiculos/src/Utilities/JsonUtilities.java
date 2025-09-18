package Utilities;

import Interfaces.IMapAbleJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class JsonUtilities {

    //VARIABLES
    private static final String CARPETA = "src/Files";
    private static final String ARCHIVO_JSON = CARPETA + File.separator + "vehiculos.json";

    //------------------------------------ METODOS ------------------------------------------------------------------------------------------------------------------------------------------------------
    //GUARDADO
    public static <T extends IMapAbleJson> void guardarVehiculosJSON(List<T> lista) {
        File dir = new File(CARPETA);       // Crear el directorio si no existe
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<Map<String, String>> listaExportable = new ArrayList<>();      // Convertir cada entidad a un Map
        for (T entidad : lista) {
            listaExportable.add(entidad.toMap());                           // Usar el método toMap() de la interfaz
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();         // Crear el JSON con formato
        String json = gson.toJson(listaExportable);                         // Convertir la lista de Maps a JSON

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_JSON, true))) {        // Escribir el JSON en el archivo
            writer.write(json);
            System.out.println("Lista serializada en: " + ARCHIVO_JSON);
        } catch (IOException e) {
            System.err.println("Error al guardar JSON: " + e.getMessage());
        }
    }

    //LECTURA Y CARGA
    public static List<Map<String, String>> cargarVehiculosJSON() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_JSON))) {        // Leer el archivo JSON
            Gson gson = new Gson();
            Map[] entidades = gson.fromJson(br, Map[].class);       // Convertir el JSON a un array de Maps

            List<Map<String, String>> lista = new ArrayList<>();    // Convertir el array a una lista de Maps
            if (entidades != null) {                                // Verificar que no sea nulo
                for (Map entidad : entidades) {
                    lista.add((Map<String, String>) entidad);
                }
            }
            return lista;
        } catch (IOException e) {
            System.err.println("Error al leer JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}