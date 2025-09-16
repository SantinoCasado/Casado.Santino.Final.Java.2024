package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TxtUtilities {
    //VARIABLES
    private static final String CARPETA = "src/Files";
    private static final String ARCHIVO_TXT = CARPETA + File.separator + "vehiculos.txt";

    //GUARDADO
    public static void guardarTexto(String contenido) throws IOException {
        File dir = new File(CARPETA);
        if (!dir.exists()) {        //Verificar si el directorio existe, si no, crearlo
            dir.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_TXT))) { //Escribir el contenido en el archivo
            writer.write(contenido);
            System.out.println("Archivo TXT guardado en: " + ARCHIVO_TXT);
        }
    }
}