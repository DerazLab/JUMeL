package pseudo.main;

import java.io.IOException;

public class ProgramReader {
    public static String leerPrograma(String nombre) {
        StringBuilder entrada = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(new java.io.FileInputStream(nombre), java.nio.charset.StandardCharsets.UTF_8))) {
            int caracter;
            while ((caracter = reader.read()) != -1) {
                entrada.append((char) caracter);
            }
            return entrada.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static java.util.List<String> obtenerArchivosJava(String rutaInicial) {
        java.util.List<String> archivos = new java.util.ArrayList<>();
        java.io.File archivoOCarpeta = new java.io.File(rutaInicial);
        if (archivoOCarpeta.exists()) {
            if (archivoOCarpeta.isFile() && archivoOCarpeta.getName().endsWith(".java")) {
                archivos.add(archivoOCarpeta.getAbsolutePath());
            } else if (archivoOCarpeta.isDirectory()) {
                escanearDirectorio(archivoOCarpeta, archivos);
            }
        }
        return archivos;
    }

    private static void escanearDirectorio(java.io.File carpeta, java.util.List<String> archivos) {
        java.io.File[] lista = carpeta.listFiles();
        if (lista != null) {
            for (java.io.File f : lista) {
                if (f.isFile() && f.getName().endsWith(".java")) {
                    archivos.add(f.getAbsolutePath());
                } else if (f.isDirectory()) {
                    escanearDirectorio(f, archivos);
                }
            }
        }
    }
}
