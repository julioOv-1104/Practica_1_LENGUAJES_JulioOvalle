package paquete.practica_1_lenguajes_analizadorlexico;

import GestionArchivos.Configuracion;
import Logica.Analisis;

public class ClaseMain {

    public static void main(String[] args) {

        // Texto de ejemplo
        String entrada = "8.8 int ; = ( 25 prueba123 // SI  -";

        Analisis analizador = new Analisis();

        System.out.println(entrada + "\n");
        Configuracion cargar = new Configuracion();
        cargar.cargarJSON();

        analizador.analizar(entrada, cargar);

        /*System.out.println("Palabras reservadas: " + cargar.getPalabrasReservadas());
        System.out.println("Operadores: " + cargar.getOperadores());
        System.out.println("Agrupacion: "+cargar.getAgrupacion());
        System.out.println("Puntuacion: "+cargar.getPuntuacion());
        System.out.println("Comentario de línea: " + cargar.getComentarioLinea());
        System.out.println("Comentario BloqueInicio: " + cargar.getComentarioBloqueInicio());
        System.out.println("Comentario BloqueFin: " + cargar.getComentarioBloqueFin());*/
    }

}
