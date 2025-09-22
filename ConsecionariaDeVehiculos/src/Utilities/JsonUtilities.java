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
        File dir = new File(CARPETA);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // CARGARGO datos existentes desde el archivo
        List<Map<String, String>> datosExistentes = cargarVehiculosJSON();
        
        // Creao Map para vehículos actuales (por patente)
        Map<String, Map<String, String>> vehiculosActuales = new HashMap<>();
        for (T entidad : lista) {
            Map<String, String> mapaVehiculo = entidad.toMap();
            String patente = mapaVehiculo.get("patente");
            vehiculosActuales.put(patente, mapaVehiculo);
        }
        
        // 3. ACTUALIZAR/AGREGAR: Recorrer datos existentes
        List<Map<String, String>> datosFinales = new ArrayList<>();
        
        //Preservo los datos que no se encuentran en memoria
        for (Map<String, String> vehiculoExistente : datosExistentes) {
            String patente = vehiculoExistente.get("patente");
            
            if (vehiculosActuales.containsKey(patente)) {
                // Si está en memoria, usar la versión de memoria (pisar)
                datosFinales.add(vehiculosActuales.get(patente));
                vehiculosActuales.remove(patente); // Marcar como procesado
            } else {
                // Si NO está en memoria, mantener la versión del archivo (preservar)
                datosFinales.add(vehiculoExistente);
            }
        }
        
        // Agrego vehículos nuevos que no estaban en el archivo
        datosFinales.addAll(vehiculosActuales.values());
        
        // GUARDARDO resultado final
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(datosFinales);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_JSON, false))) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar JSON: " + e.getMessage());
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