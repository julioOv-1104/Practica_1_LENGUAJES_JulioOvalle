package Logica;

import GestionArchivos.Configuracion;

public class Analisis {

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

    public void analizar(String texto) {

        int fila = 1;   // empieza con 1 per puede aumentar con '\n'
        int columna = 1;// inicia con 1
        int salto = 2;

        StringBuilder lexema = new StringBuilder();//aqui se va a ir armando el texto que se está identificando

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            String simbolo = String.valueOf(c);

            if (c == '\n') {

                fila++;
                columna = 1;

                // letra - posible identificador o palabra reservada
            } else if (Character.isLetter(c)) {

                i = analizarLetras(lexema, i, texto, config);

                columna = i + salto;
            } //número - entero o decimal
            else if (Character.isDigit(c)) {

                i = analizarNumeros(lexema, columna, fila, i, texto);
                columna = i + salto;

                //Si es un signo de puntuacion
            } else if (config.getPuntuacion().contains(simbolo)) {

                System.out.println("TOKEN: PUNTUACION, LEXEMA: " + "'" + simbolo + "'"
                        + " (" + fila + "," + columna + ")");
                columna++;
                //posible comentario
            } else if (texto.startsWith(comentarioLinea, i)) {// si empieza con "//"

                i = comentarioLinea(lexema, columna, fila, i, texto);
                columna = i + salto;

            } else if (texto.startsWith(comentarioBI, i)) {// si empieza con "/*"

                lexema.setLength(0);
                boolean cerrado = false;
                lexema.append(comentarioBI);

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

                comentarioBloque(lexema, columna, fila, i, cerrado);

                //Si es un operador logico
            } else if (config.getOperadores().contains(simbolo)) {

                System.out.println("TOKEN: OPERADOR, LEXEMA: " + "'" + simbolo + "'"
                        + " (" + fila + "," + columna + ")");
                columna++;

                //Si es un signo de agrupacion
            } else if (config.getAgrupacion().contains(simbolo)) {

                System.out.println("TOKEN: AGRUPACION, LEXEMA: " + "'" + simbolo + "'"
                        + " (" + fila + "," + columna + ")");
                columna++;

                //espacios o saltos de línea
            } else if (c == ' ') {
                columna++;

                //Posible caneda 
            } else if (c == '"') {
                i = analizarCadena(lexema, columna, fila, i, texto);
                columna = i + salto;

            } //cualquier otro símbolo (error)
            else {
                String error = Character.toString(c);
                errorEncontrado(fila, columna, error);
                columna++;
            }
        }
    }

    public void analizarTexto(String texto) {

        StringBuilder lexema = new StringBuilder();//aqui se va a ir armando el texto que se está identificando
        int indice = 0;

        while (indice < texto.length()) {
            char c = texto.charAt(indice);

            if (c == '\n') {
                fila++;
                columna = 1;
                // letra - posible identificador o palabra reservada
            } else if (Character.isLetter(c)) {

                indice = analizarLetras(lexema, indice, texto, config);

            }

            indice++;
        }

    }

    public int analizarLetras(StringBuilder lexema, int i, String texto, Configuracion config) {

        lexema.setLength(0); // limpiar
        int iniciarColumna = columna;

        // leer letras y números seguidos
        while (i < texto.length() && (Character.isLetterOrDigit(texto.charAt(i)))) {
            lexema.append(texto.charAt(i));
            i++;
            columna++;
            if (i < texto.length()) {
                if (texto.charAt(i) == '\n') {
                    fila++;
                    columna = 1;
                    return i;
                }
            }

        }
        String palabra = lexema.toString();

        //Si en el listado de palbras reservadas está la palabra encontrada
        if (config.getPalabrasReservadas().contains(palabra)) {
            System.out.println("TOKEN: PALABRA_RESERVADA, LEXEMA: " + lexema
                    + " (" + fila + "," + iniciarColumna + ")");
        } else {
            System.out.println("TOKEN: IDENTIFICADOR, LEXEMA: " + lexema
                    + " (" + fila + "," + iniciarColumna + ")");
        }
        columna++;
        return i; // retroceder porque el for también hace i++ y asi sigue con el proceso en el for
    }

    private int analizarNumeros(StringBuilder lexema, int columna, int fila, int i, String texto) {

        lexema.setLength(0);

        boolean esDecimal = false;
        boolean cadenaInvalida = false;

        while (i < texto.length() && texto.charAt(i) != '\n') {

            if (texto.charAt(i) == '.' || (Character.isDigit(texto.charAt(i)))) {//es un punto o un numero?

                if (texto.charAt(i) == '.') {
                    if (esDecimal) {
                        cadenaInvalida = true;
                        //Da error porque significa que la cadena de numero tiene dos puntos y eso no existe                       
                    } else {
                        esDecimal = true;//ahora es un decimal
                    }

                }

                lexema.append(texto.charAt(i));
                i++;

            } else if (texto.charAt(i) == ' ' || texto.charAt(i) == '\n') {

            } else {//No es punto ni numero
                cadenaInvalida = true;
                lexema.append(texto.charAt(i));
                i++;
            }

        }

        if (cadenaInvalida) {
            //indica que la cadena no es valida
            String lexemaInvalido = lexema.toString();
            errorEncontrado(fila, columna, lexemaInvalido + "");

        } else if (esDecimal) {
            System.out.println("TOKEN: DECIMAL, LEXEMA: " + lexema
                    + " (" + fila + "," + columna + ")");
        } else {
            System.out.println("TOKEN: NUMERO, LEXEMA: " + lexema
                    + " (" + fila + "," + columna + ")");
        }
        return i--;
    }

    private int comentarioLinea(StringBuilder lexema, int columna, int fila, int i, String texto) {

        lexema.setLength(0);
        while (i < texto.length() && texto.charAt(i) != '\n') {
            lexema.append(texto.charAt(i));
            i++;
        }

        System.out.println("TOKEN: COMENTARIO_LINEA, LEXEMA: " + "'" + lexema + "'"
                + " (" + fila + "," + columna + ")");
        return i++;
    }

    private void comentarioBloque(StringBuilder lexema, int columna, int fila, int i, boolean cerrado) {

        if (cerrado) {
            System.out.println("TOKEN: COMENTARIO_BLOQUE, LEXEMA: " + "'" + lexema + "'"//Si está cerrado
                    + " (" + fila + "," + columna + ")");
        } else {
            errorEncontrado(fila, columna, lexema + "");//Si no está cerrado
        }

    }

    private int analizarCadena(StringBuilder lexema, int columna, int fila, int i, String texto) {

        lexema.setLength(0);
        boolean cadenaCerrada = false;
        int columnaInicial = columna;
        int filaini = fila;

        lexema.append(texto.charAt(i));
        i++;//Pasa a el siguiente caracter

        while (i < texto.length() || cadenaCerrada == true) {
            lexema.append(texto.charAt(i));

            if (texto.charAt(i) == '"') {
                cadenaCerrada = true;
                break;
            }
            if (texto.charAt(i) == '\n') {
                //salto de linea
                fila++;
                columna = 1;
            }
            i++;
        }

        if (cadenaCerrada) {
            System.out.println("TOKEN: CADENA, LEXEMA: " + lexema + " en"
                    + " (" + filaini + "," + columnaInicial + ")");

        } else {
            errorEncontrado(fila, columna, lexema + "");

        }
        return i--;
    }

    private void errorEncontrado(int fila, int columna, String error) {

        System.out.println("ERROR: '" + error + "' no reconocido en ("
                + fila + "," + columna + ")");
    }

}
