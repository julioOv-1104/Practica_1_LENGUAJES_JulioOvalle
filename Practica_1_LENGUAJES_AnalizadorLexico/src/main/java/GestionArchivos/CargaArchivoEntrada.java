package GestionArchivos;

import InterfazGrafica.AnalizadorLexicoIG;
import Logica.Analisis;
import java.io.*;
import java.nio.file.*;
import javax.swing.JTextPane;

public class CargaArchivoEntrada implements Runnable {

    private File archivoentrada;
    private String ruta;
    private Configuracion config;
    private Analisis analizador;
    private AnalizadorLexicoIG espacio;

    public CargaArchivoEntrada(String ruta, Configuracion config, Analisis analizador, AnalizadorLexicoIG espacio) {
        this.ruta = ruta;
        this.archivoentrada = new File(ruta);
        this.config = config;
        this.analizador = analizador;
        this.espacio = espacio;
    }

    @Override
    public void run() {

        Path pathArchivo = Path.of(ruta);

        try {

            String archivoTexto = Files.readString(pathArchivo);

            archivoTexto = archivoTexto.replace("\r\n", "\n");
            archivoTexto = archivoTexto.replace("\r", "\n");

            //System.out.println(archivoTexto);

            try {
                espacio.getEstiloDocumento1().insertString(espacio.getEstiloDocumento1().getLength(), archivoTexto, null);
            } catch (Exception e) {
            }

            System.out.println("\n");
            System.out.println("EL LARGO DEL ARCHIVO " + archivoTexto.length());
            System.out.println("---------------------------------LECTURA-------------------------------------");
            System.out.println("\n");
            //List<Token> tokens  = analizador.analizarTexto( archivoTexto);

        } catch (IOException e) {
            System.out.println("ERROR AL LEER ARCHIVO DE ENTRADA: " + e.getMessage());
        }

    }

}
