package Utilities;

import Interfaces.IMapAbleJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.*;
import java.lang.reflect.Type;
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

        // CARGO datos existentes desde el archivo
        List<Map<String, String>> datosExistentes = cargarVehiculosJSON();
        
        // Creo Map para vehículos actuales (por patente)
        Map<String, Map<String, String>> vehiculosActuales = new HashMap<>();
        for (T entidad : lista) {
            Map<String, String> mapaVehiculo = entidad.toMap();
            String patente = mapaVehiculo.get("patente");
            vehiculosActuales.put(patente, mapaVehiculo);
        }
        
        // ACTUALIZAR/AGREGAR: Recorrer datos existentes
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
        
        // GUARDO resultado final
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(datosFinales);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_JSON, false))) {
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Error al guardar JSON: " + e.getMessage());
        }
    }

    //LECTURA Y CARGA
    public static List<Map<String, String>> cargarVehiculosJSON() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_JSON))) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Map<String, String>>>(){}.getType();
            List<Map<String, String>> lista = gson.fromJson(br, tipoLista);
            
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ELIMINAR VEHICULO DE JSON 
    public static boolean eliminarVehiculoJSON(String patente) {
        try {
            File archivoJSON = new File(ARCHIVO_JSON);
            if (!archivoJSON.exists()) {
                return false;
            }
            
            // Cargar todos los vehículos usando Gson
            List<Map<String, String>> vehiculos = cargarVehiculosJSON();
            boolean vehiculoEncontrado = false;
            
            // Buscar y eliminar el vehículo con la patente especificada
            Iterator<Map<String, String>> iterator = vehiculos.iterator();
            while (iterator.hasNext()) {
                Map<String, String> vehiculo = iterator.next();
                if (patente.equals(vehiculo.get("patente"))) {
                    iterator.remove();
                    vehiculoEncontrado = true;
                    break;
                }
            }
            
            // Reescribir el archivo JSON sin el vehículo eliminado
            if (vehiculoEncontrado) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(vehiculos);
                
                try (FileWriter writer = new FileWriter(archivoJSON)) {
                    writer.write(json);
                }
            }
            
            return vehiculoEncontrado;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar del archivo JSON: " + e.getMessage(), e);
        }
    }
    
    //-------------------------------------------------------------------- METODOS AUXILIARES ---------------------------------------------------------------------------
    // VERIFICAR EXISTENCIA
    public static boolean existeVehiculoEnJSON(String patente) {
        try {
            File archivoJSON = new File(ARCHIVO_JSON);
            if (!archivoJSON.exists()) {
                return false;
            }
            
            // Cargar todos los vehículos usando Gson
            List<Map<String, String>> vehiculos = cargarVehiculosJSON();
            
            // Buscar el vehículo con la patente especificada
            for (Map<String, String> vehiculo : vehiculos) {
                if (patente.equals(vehiculo.get("patente"))) {
                    return true;
                }
            }
            
            return false;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar en archivo JSON: " + e.getMessage(), e);
        }
    }
}