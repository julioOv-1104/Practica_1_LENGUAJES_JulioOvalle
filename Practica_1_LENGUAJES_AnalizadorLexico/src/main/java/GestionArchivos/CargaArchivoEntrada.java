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

    /*@Override
    public void run() {
        
        Path pathArchivo = Path.of(ruta);

        try {
            int fila = 1;
            FileReader fr = new FileReader(archivoentrada);
            BufferedReader br = new BufferedReader(fr);

            String linea;
            System.out.println("LEYENDO TEXTO DE ENTRADA: " + ruta);
            
            String archivoTexto = Files.readString(pathArchivo);

            while ((linea = br.readLine()) != null) {

                String texto = linea;
                System.out.println(texto);
                

            }

        } catch (IOException e) {
            System.out.println("ERROR AL LEER ARCHIVO DE ENTRADA: " + e.getMessage());
        }

    }*/
    @Override
    public void run() {

        Path pathArchivo = Path.of(ruta);

        try {

            String archivoTexto = Files.readString(pathArchivo);

            archivoTexto = archivoTexto.replace("\r\n", "\n");
            archivoTexto = archivoTexto.replace("\r", "\n");

            //System.out.println(archivoTexto);

            try {
                espacio.getEstiloDocumento().insertString(espacio.getEstiloDocumento().getLength(), archivoTexto, null);
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
