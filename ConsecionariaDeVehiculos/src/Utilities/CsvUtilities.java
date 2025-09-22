package Utilities;

import Interfaces.ISerializableCsv;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class CsvUtilities<T extends ISerializableCsv> {

    //VARIABLES
    private static final String CARPETA = "src/Files";
    private static final String ARCHIVO_CSV = CARPETA + File.separator + "vehiculos.csv";

    //GUARDADO 
    public static <T extends ISerializableCsv> void guardarVehiculosCSV(ArrayList<T> lista) throws Exception {
        File dir = new File(CARPETA);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // CARGARGO datos existentes desde el archivo
        ArrayList<String> lineasExistentes = leerCSV();
        
        // Creo un Map para vehículos actuales (por patente)
        Map<String, String> vehiculosActuales = new HashMap<>();
        for (T entidad : lista) {
            String csvLine = entidad.toCSV();
            String patente = extraerPatente(csvLine);
            if (patente != null) {
                vehiculosActuales.put(patente, csvLine);
            }
        }

        // Creo el Set para controlar patentes de la lista actual
        Set<String> patentesEnMemoria = vehiculosActuales.keySet();
        
        // PROCESESA líneas existentes
        ArrayList<String> lineasFinales = new ArrayList<>();
        
        // Mantener líneas del archivo que NO están en memoria (preservar)
        for (String lineaExistente : lineasExistentes) {
            String patenteExistente = extraerPatente(lineaExistente);
            
            if (patenteExistente != null && vehiculosActuales.containsKey(patenteExistente)) {
                // Si está en memoria, usar la versión de memoria (pisar)
                lineasFinales.add(vehiculosActuales.get(patenteExistente));
                vehiculosActuales.remove(patenteExistente); // Marcar como procesado
            } else {
                // Si NO está en memoria, mantener la versión del archivo (preservar)
                lineasFinales.add(lineaExistente);
            }
        }
        
        // Agrego vehículos nuevos que no estaban en el archivo
        lineasFinales.addAll(vehiculosActuales.values());
        
        // GUARDO el resultado final (SOBRESCRIBIR archivo)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_CSV, false))) {
            for (String linea : lineasFinales) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new Exception("Error al guardar CSV: " + e.getMessage(), e);
        }
    }

    //LECTURA
    public static ArrayList<String> leerCSV() throws Exception {
        ArrayList<String> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO_CSV);
        
        // Si el archivo no existe, crearlo vacío
        if (!archivo.exists()) {
            try {
                File dir = new File(CARPETA);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                archivo.createNewFile();
            } catch (IOException e) {
                throw new Exception("Error al crear archivo CSV: " + e.getMessage(), e);
            }
            return lista; // Retornar lista vacía
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) { // Ignorar líneas vacías
                    lista.add(linea);
                }
            }
        } catch (IOException e) {
            throw new Exception("Error al leer CSV: " + e.getMessage(), e);
        }
        return lista;
    }

    //CARGA
    public static <T extends ISerializableCsv> ArrayList<T> cargarVehiculosCSV(Function<String, T> parseador) throws Exception {
        ArrayList<T> lista = new ArrayList<>();
        ArrayList<String> lineas = leerCSV();
        
        for (String linea : lineas) {
            try {
                T objeto = parseador.apply(linea);
                if (objeto != null) {
                    lista.add(objeto);
                }
            } catch (Exception e) {
                throw new Exception("Error al parsear línea CSV: " + linea + " - " + e.getMessage(), e);
            }
        }
        return lista;
    }
    
    // Extraer patente de una línea CSV
    private static String extraerPatente(String lineaCSV) {
        if (lineaCSV == null || lineaCSV.trim().isEmpty()) {
            return null;
        }
        
        String[] partes = lineaCSV.split(",");
        if (partes.length > 1) {
            return partes[1].trim(); // La patente está en la posición 1
        }
        return null;
    }
}