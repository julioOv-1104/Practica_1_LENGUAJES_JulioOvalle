package Logica;

import GestionArchivos.Configuracion;
import java.util.*;

public class Analisis {

    List<Token> tokensEncontrados = new ArrayList<>();

    private Configuracion config;
    private String comentarioLinea, comentarioBI, comentarioBF;//para saber como inician y terminan los comentarios 

    private int fila = 1;
    private int columna = 1;

    public Analisis(Configuracion config) {
        this.config = config;
        this.comentarioLinea = config.getComentarioLinea();// = "//"
        this.comentarioBI = config.getComentarioBloqueInicio();// = "/*"
        this.comentarioBF = config.getComentarioBloqueFin(); // = "*/"
    }

    public List<Token> analizarTexto(String texto) {
        StringBuilder lexema = new StringBuilder();//aqui se va a ir armando el texto que se está identificando
        int indice = 0;

        while (indice < texto.length()) {
            char c = texto.charAt(indice);
            String simbolo = String.valueOf(c);

            if (c == '\n' || c == '\r') {
                lexema.setLength(0);
                lexema.append('\n');
                agregarToken("SLATO", lexema + "", fila, columna);
                fila++;
                columna = 1;

                // letra - posible identificador o palabra reservada
            } else if (c == ' ') {
                lexema.setLength(0);
                lexema.append(' ');
                agregarToken("ESPACIO", lexema + "", fila, columna);
                columna++;

                //Detecta identificadores o palabras reservadas
            } else if (Character.isLetter(c)) {
                indice = analizarLetras(lexema, indice, texto, config);

                //Determina si es numero o decimal
            } else if (Character.isDigit(c)) {
                indice = analizarNumeros(lexema, indice, texto);

                //posible cadena
            } else if (c == '"') {
                indice = analizarCadena(lexema, indice, texto);

                //Posible signo de puntuacion
            } else if (config.getPuntuacion().contains(simbolo)) {
                analizarPuntuacion(lexema, simbolo);
                columna++;

                //Posible comentario de linea
            } else if (texto.startsWith(comentarioLinea, indice)) {
                indice = comentarioLinea(lexema, indice, texto);

                //Posible comentario de bloque
            } else if (texto.startsWith(comentarioBI, indice)) {
                indice = comentarioBloque(lexema, indice, texto);

                //Posible signo de agrupacion
            } else if (config.getAgrupacion().contains(simbolo)) {
                analizarAgrupacion(lexema, simbolo);
                columna++;

                //Operador logico
            } else if (config.getOperadores().contains(simbolo)) {
                analizarOperador(lexema, simbolo);
                columna++;
            }

            indice++;
        }
        return tokensEncontrados;
    }

    public int analizarLetras(StringBuilder lexema, int i, String texto, Configuracion config) {

        lexema.setLength(0); // limpiar
        int columnaInicial = columna;//Se guardan para ser mostradas
        int filaini = fila;

        // leer letras y números seguidos
        while (i < texto.length() && (Character.isLetterOrDigit(texto.charAt(i)))) {
            lexema.append(texto.charAt(i));
            i++;
            columna++;

            if (texto.charAt(i) == '\n') {
                fila++;
                columna = 1;
                break;
            }

        }
        String palabra = lexema.toString();

        //Si en el listado de palbras reservadas está la palabra encontrada
        if (config.getPalabrasReservadas().contains(palabra)) {
            System.out.println("TOKEN: PALABRA_RESERVADA, LEXEMA: " + lexema
                    + " (" + filaini + "," + columnaInicial + ")");
            agregarToken("PALABRA_RESERVADA", lexema + "", filaini, columnaInicial);

        } else {
            System.out.println("TOKEN: IDENTIFICADOR, LEXEMA: " + lexema
                    + " (" + filaini + "," + columnaInicial + ")");
            agregarToken("IDENTIFICADOR", lexema + "", filaini, columnaInicial);
        }
        columna++;
        return i; //se retorna "i" para llevar el conteo de caracteres leidos
    }

    private int analizarNumeros(StringBuilder lexema, int i, String texto) {
        int columnaInicial = columna;//Se guardan para ser mostradas
        int filaini = fila;
        lexema.setLength(0);

        boolean esDecimal = false;
        boolean cadenaInvalida = false;

        while (i < texto.length()) {
            String simbolo = String.valueOf(texto.charAt(i));

            if (texto.charAt(i) == '.' || Character.isDigit(texto.charAt(i))) {//es un punto o un numero?

                if (texto.charAt(i) == '.') {

                    if (esDecimal) {
                        cadenaInvalida = true;
                        break;
                        //Da error porque significa que la cadena de numero tiene dos puntos y eso no existe                       
                    } else {
                        esDecimal = true;//ahora es un decimal
                    }

                }

                lexema.append(texto.charAt(i));
                i++;
                columna++;

            } else if (texto.charAt(i) == ' ') {
                i--;
                break;

            } else if (texto.charAt(i) == '\n' || texto.charAt(i) == '\r') {
                System.out.println("ES UN SALTO DE LINEA");
                i--;
                columna = 1;
                fila++;
                break;

            } else if (config.getOperadores().contains(simbolo) || config.getAgrupacion().contains(simbolo)
                    || config.getPuntuacion().contains(simbolo)) {
                i--;//retrocede para que en la siguiente iteracion pueda evaluar el simbolo
                break;

            } else {//No es punto ni numero
                cadenaInvalida = true;
                lexema.append(texto.charAt(i));
                break;

            }

        }

        if (cadenaInvalida) {
            //indica que la cadena no es valida
            String lexemaInvalido = lexema.toString();
            errorEncontrado(filaini, columnaInicial, lexemaInvalido + "");

        } else if (esDecimal) {
            System.out.println("TOKEN: DECIMAL, LEXEMA: " + lexema
                    + " (" + filaini + "," + columnaInicial + ")");
            agregarToken("DECIMAL", lexema + "", filaini, columnaInicial);
        } else {
            System.out.println("TOKEN: NUMERO, LEXEMA: " + lexema
                    + " (" + filaini + "," + columnaInicial + ")");
            agregarToken("NUMERO", lexema + "", filaini, columnaInicial);
        }

        return i;
    }

    private int comentarioLinea(StringBuilder lexema, int i, String texto) {
        int columnaInicial = columna;
        int filaini = fila;
        lexema.setLength(0);
        while (i < texto.length() && texto.charAt(i) != '\n') {
            lexema.append(texto.charAt(i));
            i++;
            columna++;

            if (texto.charAt(i) == '\n') {
                fila++;
                columna = 1;
                break;
            }
        }

        System.out.println("TOKEN: COMENTARIO_LINEA, LEXEMA: " + "'" + lexema + "'"
                + " (" + filaini + "," + columnaInicial + ")");
        agregarToken("COMENTARIO_LINEA", lexema + "", filaini, columnaInicial);
        return i;
    }

    private int comentarioBloque(StringBuilder lexema, int i, String texto) {

        lexema.setLength(0);

        boolean cerrado = false;
        lexema.append(comentarioBI);

        int columnaInicial = columna;//Se guardan para ser mostradas
        int filaini = fila;
        i += comentarioBI.length();
        columna += comentarioBI.length();//para que sepa donde seguir contando

        while (i < texto.length()) {

            if (texto.startsWith(comentarioBF, i)) {//encontró el final del comentario

                lexema.append(comentarioBF);
                i += comentarioBF.length();
                columna += comentarioBF.length();
                cerrado = true;
                break;
            }

            lexema.append(texto.charAt(i));

            if (texto.charAt(i) == '\n') {
                fila++;
                columna = 1;
            } else {
                columna++;
            }
            i++;

        }

        if (cerrado) {
            System.out.println("TOKEN: COMENTARIO_BLOQUE, LEXEMA: " + "'" + lexema + "'"//Si está cerrado
                    + " (" + filaini + "," + columnaInicial + ")");
            agregarToken("COMENTARIO_BLOQUE", lexema + "", filaini, columnaInicial);
        } else {
            errorEncontrado(filaini, columnaInicial, lexema + "");//Si no está cerrado
        }
        return i;
    }

    private int analizarCadena(StringBuilder lexema, int i, String texto) {

        lexema.setLength(0);
        boolean cadenaCerrada = false;

        int columnaInicial = columna;//Se guardan para ser mostradas
        int filaini = fila;

        lexema.append(texto.charAt(i));
        i++;//Pasa a el siguiente caracter
        columna++;

        while (i < texto.length()) {
            lexema.append(texto.charAt(i));
            columna++;
            if (texto.charAt(i) == '"') {
                cadenaCerrada = true;
                break;
            }
            if (texto.charAt(i) == ' ') {
                columna++;

            }
            if (texto.charAt(i) == '\n') {
                //salto de linea
                fila++;
                columna = 1;
                break;
            }

            i++;
        }

        if (cadenaCerrada) {
            System.out.println("TOKEN: CADENA, LEXEMA: " + lexema + " en"
                    + " (" + filaini + "," + columnaInicial + ")");
            agregarToken("CADENA", lexema + "", filaini, columnaInicial);

        } else {
            errorEncontrado(fila, columnaInicial, lexema + "");

        }
        return i;
    }

    private void analizarPuntuacion(StringBuilder lexema, String simbolo) {
        lexema.setLength(0);
        System.out.println("TOKEN: PUNTUACION, LEXEMA: " + "'" + simbolo + "'"
                + " (" + fila + "," + columna + ")");
        agregarToken("PUNTUACION", simbolo + "", fila, columna);
    }

    private void analizarOperador(StringBuilder lexema, String simbolo) {
        lexema.setLength(0);
        System.out.println("TOKEN: OPERADOR, LEXEMA: " + "'" + simbolo + "'"
                + " (" + fila + "," + columna + ")");
        agregarToken("OPERADOR", simbolo + "", fila, columna);
    }

    private void analizarAgrupacion(StringBuilder lexema, String simbolo) {
        lexema.setLength(0);
        System.out.println("TOKEN: AGRUPACION, LEXEMA: " + "'" + simbolo + "'"
                + " (" + fila + "," + columna + ")");
        agregarToken("AGRUPACION", simbolo + "", fila, columna);
    }

    private void errorEncontrado(int fila, int columna, String error) {

        System.out.println("ERROR: '" + error + "' no reconocido en ("
                + fila + "," + columna + ")");
        agregarToken("ERROR", error + "", fila, columna);
    }

    private void agregarToken(String tipo, String lexema, int fila, int columna) {

        tokensEncontrados.add(new Token(tipo, lexema, fila, columna));//Agrega el token econtrado a la lista
        
    }

}
