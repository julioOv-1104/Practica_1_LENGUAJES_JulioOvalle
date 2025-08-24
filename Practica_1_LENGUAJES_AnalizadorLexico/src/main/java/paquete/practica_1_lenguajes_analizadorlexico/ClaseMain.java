package paquete.practica_1_lenguajes_analizadorlexico;

import GestionArchivos.CargaArchivoEntrada;
import GestionArchivos.Configuracion;
import InterfazGrafica.AnalizadorLexicoIG;
import Logica.Analisis;

public class ClaseMain {

    public static void main(String[] args) {

        Configuracion cargar = new Configuracion();
        cargar.cargarJSON();
        String ruta = "C:\\Users\\Usuario\\OneDrive\\Documents\\Practica_1_LENGUAJES_JulioOvalle\\Archivo de entrada de prueba.txt";

        Analisis analizador = new Analisis(cargar);

        /*CargaArchivoEntrada iniciarArchivo = new CargaArchivoEntrada(ruta, cargar,analizador);
        Thread hilo = new Thread(iniciarArchivo);
        hilo.start();*/
        
        AnalizadorLexicoIG ig = new AnalizadorLexicoIG(analizador);
        ig.setVisible(true);
        
        
        
        //System.out.println(txt + "\n");
        //System.out.println("txt largo = " + txt.length());
        //StringBuilder lexema = new StringBuilder();
        //lexema.setLength(0);
        //analizador.analizarTexto(lexema, txt);

        /*System.out.println("Palabras reservadas: " + cargar.getPalabrasReservadas());
        System.out.println("Operadores: " + cargar.getOperadores());
        System.out.println("Agrupacion: "+cargar.getAgrupacion());
        System.out.println("Puntuacion: "+cargar.getPuntuacion());
        System.out.println("Comentario de línea: " + cargar.getComentarioLinea());
        System.out.println("Comentario BloqueInicio: " + cargar.getComentarioBloqueInicio());
        System.out.println("Comentario BloqueFin: " + cargar.getComentarioBloqueFin());*/
    }

}
