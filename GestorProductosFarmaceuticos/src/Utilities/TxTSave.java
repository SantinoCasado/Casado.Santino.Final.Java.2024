package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TxTSave {
    public static void guardar(List<String> lineas) {
        // Ruta relativa al proyecto
        String rutaCarpeta = "src" + File.separator + "DatosFormularios";
        File carpeta = new File(rutaCarpeta);

        // Crear la carpeta si no existe
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Archivo dentro de esa carpeta
        File archivo = new File(carpeta, "medicamentos_proximos_a_vencer.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
            System.out.println("Archivo guardado en: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ocurrió un error al escribir el archivo: " + e.getMessage());
        }
    }
}