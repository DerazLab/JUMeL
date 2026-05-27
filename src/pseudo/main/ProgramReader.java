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
}
