package paquete.practica_1_lenguajes_analizadorlexico;

import GestionArchivos.CargaArchivoEntrada;
import GestionArchivos.Configuracion;
import InterfazGrafica.AnalizadorLexicoIG;
import Logica.Analisis;

public class ClaseMain {

    public static void main(String[] args) {

        Configuracion cargar = new Configuracion();
        cargar.cargarJSON();
        Analisis analizador = new Analisis(cargar);

        
        AnalizadorLexicoIG ig = new AnalizadorLexicoIG(analizador,cargar);
        ig.setVisible(true);
        

    }

}
