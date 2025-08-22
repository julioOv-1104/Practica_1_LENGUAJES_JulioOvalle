package paquete.practica_1_lenguajes_analizadorlexico;

import GestionArchivos.Configuracion;
import Logica.Analisis;

public class ClaseMain {

    public static void main(String[] args) {

        // Texto de ejemplo
        String entrada = "8.8.0 int ; = \"HOLA MUNDO\"  /*COMENTARIO DE BLOQUE* ( 25 prueba123 SI - //COMENTARIO DE LINEA";
        Configuracion cargar = new Configuracion();
        cargar.cargarJSON();
        
        Analisis analizador = new Analisis(cargar);

        System.out.println(entrada + "\n");

        analizador.analizar(entrada);

        /*System.out.println("Palabras reservadas: " + cargar.getPalabrasReservadas());
        System.out.println("Operadores: " + cargar.getOperadores());
        System.out.println("Agrupacion: "+cargar.getAgrupacion());
        System.out.println("Puntuacion: "+cargar.getPuntuacion());
        System.out.println("Comentario de línea: " + cargar.getComentarioLinea());
        System.out.println("Comentario BloqueInicio: " + cargar.getComentarioBloqueInicio());
        System.out.println("Comentario BloqueFin: " + cargar.getComentarioBloqueFin());*/
    }

}
