package GestionArchivos;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import org.json.*;
import java.util.*;

public class Configuracion {//Clase que se encarga de cargar el archivo config.json

    private List<String> palabrasReservadas = new ArrayList<>();
    private List<String> operadores = new ArrayList<>();
    private List<String> puntuacion = new ArrayList<>();
    private List<String> agrupacion = new ArrayList<>();
    private String comentarioLinea;
    private String comentarioBloqueInicio;
    private String comentarioBloqueFin;

    public void cargarJSON() {

        //Obtiene la direccion del archivo .json situado en el proyecto
        try (InputStream is = getClass().getResourceAsStream("/JSON/config.json")) {
            if (is == null) {
                throw new RuntimeException("No se encontró config.json");
            }

            // Leer el archivo como String
            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
            String contenido = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Convertir a objeto JSON
            JSONObject json = new JSONObject(contenido);

            // Cargar los tokens del archivo
            palabrasReservadas = cargarLista(json.getJSONArray("palabrasReservadas"));
            operadores = cargarLista(json.getJSONArray("operadoresLogicos"));
            puntuacion = cargarLista(json.getJSONArray("signosPuntuacion"));
            agrupacion = cargarLista(json.getJSONArray("signosAgrupacion"));

            JSONObject comentarios = json.getJSONObject("comentarios");
            comentarioLinea = comentarios.getString("linea");
            comentarioBloqueInicio = comentarios.getString("bloqueInicio");
            comentarioBloqueFin = comentarios.getString("bloqueFin");

        } catch (Exception e) {
            System.out.println("Error leyendo el archivo config.json: " + e.getMessage());
        }
    }

    private List<String> cargarLista(JSONArray array) {
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            lista.add(array.getString(i));
        }
        return lista;
    }

    public int contarTotalDeTokens() {
        int total = 0;
        return total = palabrasReservadas.size() + operadores.size() + puntuacion.size() + agrupacion.size() + 2;
        //Se suma la cantidad de tokens que existen en el archivo .json y el "2" es por los comentarios de linea y bloque
    }

    public List<String> getPalabrasReservadas() {
        return palabrasReservadas;
    }

    public void setPalabrasReservadas(List<String> palabrasReservadas) {
        this.palabrasReservadas = palabrasReservadas;
    }

    public List<String> getOperadores() {
        return operadores;
    }

    public void setOperadores(List<String> operadores) {
        this.operadores = operadores;
    }

    public List<String> getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(List<String> puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<String> getAgrupacion() {
        return agrupacion;
    }

    public void setAgrupacion(List<String> agrupacion) {
        this.agrupacion = agrupacion;
    }

    public String getComentarioLinea() {
        return comentarioLinea;
    }

    public void setComentarioLinea(String comentarioLinea) {
        this.comentarioLinea = comentarioLinea;
    }

    public String getComentarioBloqueInicio() {
        return comentarioBloqueInicio;
    }

    public void setComentarioBloqueInicio(String comentarioBloqueInicio) {
        this.comentarioBloqueInicio = comentarioBloqueInicio;
    }

    public String getComentarioBloqueFin() {
        return comentarioBloqueFin;
    }

    public void setComentarioBloqueFin(String comentarioBloqueFin) {
        this.comentarioBloqueFin = comentarioBloqueFin;
    }

}
