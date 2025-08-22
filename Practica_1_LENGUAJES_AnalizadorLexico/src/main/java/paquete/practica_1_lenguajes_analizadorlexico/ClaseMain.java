package paquete.practica_1_lenguajes_analizadorlexico;

import GestionArchivos.CargaArchivoEntrada;
import GestionArchivos.Configuracion;
import Logica.Analisis;

public class ClaseMain {

    public static void main(String[] args) {

        // Texto de ejemplo
        //String entrada = "8.8.0 int ; = \"HOLA MUNDO\"  /*COMENTARIO DE BLOQUE* ( 25 prueba123 SI - //COMENTARIO DE LINEA";
        //String txt = "palapala ad ENTONCES /*abc123 ojo 1104 2.0)*/ \njulio si  12;  \n\"allan ovalle\"\n ; { + // comentario si  ";

        Configuracion cargar = new Configuracion();
        cargar.cargarJSON();
        String ruta = "C:\\Users\\Usuario\\OneDrive\\Documents\\Practica_1_LENGUAJES_JulioOvalle\\Archivo de entrada de prueba.txt";

        Analisis analizador = new Analisis(cargar);

        CargaArchivoEntrada iniciarArchivo = new CargaArchivoEntrada(ruta, cargar,analizador);
        Thread hilo = new Thread(iniciarArchivo);
        hilo.start();
        
        
        
        
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
