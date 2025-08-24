package Utilities;

import Interfaces.IMapAbleJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class JsonUtilities {

    private static final String RUTA_JSON = "src/DatosFormularios/productosProximosVencer.json";
    
    public static void guardarProductos(List<IMapAbleJson> productos) {
        List<Map<String, String>> listaExportable = new ArrayList<>();

        for (IMapAbleJson producto : productos) {
            listaExportable.add(producto.toMap());
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(listaExportable);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_JSON))) {
            writer.write(json);
            System.out.println("Lista serializada en: " + RUTA_JSON);
        } catch (IOException e) {
            System.err.println("Error al guardar JSON: " + e.getMessage());
        }
    }

    public static void leerProductos() {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_JSON))) {
            Gson gson = new Gson();
            Map[] productos = gson.fromJson(br, Map[].class);

            System.out.println("Productos leídos:");
            for (Map producto : productos) {
                System.out.println(producto);
            }
        } catch (IOException e) {
            System.err.println("Error al leer JSON: " + e.getMessage());
        }
    }

}
