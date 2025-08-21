package Logica;

import GestionArchivos.Configuracion;

public class Analisis {

    public void analizar(String texto, Configuracion config) {

        int fila = 1;   // para el ejemplo mantenemos 1, pero puedes aumentar con '\n'
        int columna = 1;

        StringBuilder lexema = new StringBuilder();//aqui se va a ir armando el texto que se está identificando

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            String simbolo = String.valueOf(c);

            // letra - posible identificador o palabra reservada
            if (Character.isLetter(c)) {

                i = analizarLetras(lexema, columna, fila, i, texto, config);

                columna = i + 2;
            } //número - entero o decimal
            else if (Character.isDigit(c)) {

                i = analizarNumeros(lexema, columna, fila, i, texto);
                columna = +i + 2;

                //Si es un signo de puntuacion
            } else if (config.getPuntuacion().contains(simbolo)) {

                System.out.println("TOKEN: PUNTUACION, LEXEMA: " + "'" + simbolo + "'"
                        + " (" + fila + "," + columna + ")");
                columna++;

                //Si es un operador logico
            } else if (config.getOperadores().contains(simbolo)) {

                i = analizadorComentarios(lexema, columna, fila, i, texto);
                columna = i + 2;

                //Si es un signo de agrupacion
            } else if (config.getAgrupacion().contains(simbolo)) {

                System.out.println("TOKEN: AGRUPACION, LEXEMA: " + "'" + simbolo + "'"
                        + " (" + fila + "," + columna + ")");
                columna++;

                //espacios o saltos de línea
            } else if (c == ' ') {
                columna++;
            } else if (c == '\n') {
                fila++;
                columna = 1;
            } //cualquier otro símbolo (error)
            else {
                System.out.println("ERROR: simbolo '" + c + "' no reconocido en ("
                        + fila + "," + columna + ")");
                columna++;
            }
        }
    }

    public int analizarLetras(StringBuilder lexema, int columna, int fila, int i, String texto, Configuracion config) {

        lexema.setLength(0); // limpiar
        int iniciarColumna = columna;

        // leer letras y números seguidos
        while (i < texto.length() && (Character.isLetterOrDigit(texto.charAt(i)))) {
            lexema.append(texto.charAt(i));
            i++;

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

        return i--; // retroceder porque el for también hace i++ y asi sigue con el proceso en el for
    }

    private int analizarNumero(StringBuilder lexema, int columna, int fila, int i, String texto) {

        lexema.setLength(0);
        int startCol = columna;
        boolean esDecimal = false;

        while (i < texto.length() && (Character.isDigit(texto.charAt(i)) || texto.charAt(i) == '.')) {
            if (texto.charAt(i) == '.') {
                if (esDecimal) {
                    break;//Da error porque significa que la cadena de numero tiene dos puntos y eso no existe                       
                } else {
                    esDecimal = true;//ahora es un decimal
                }

            }
            lexema.append(texto.charAt(i));
            i++;

        }

        if (esDecimal) {
            System.out.println("TOKEN: DECIMAL, LEXEMA: " + lexema
                    + " (" + fila + "," + startCol + ")");
        } else {
            System.out.println("TOKEN: NUMERO, LEXEMA: " + lexema
                    + " (" + fila + "," + startCol + ")");
        }
        return i--;
    }

    private int analizarNumeros(StringBuilder lexema, int columna, int fila, int i, String texto) {

        lexema.setLength(0);
        int startCol = columna;
        boolean esDecimal = false;
        boolean cadenaInvalida = false;

        while (i < texto.length() && (texto.charAt(i) != ' ')) {

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

            } else {//No es punto ni numero
                cadenaInvalida = true;
                lexema.append(texto.charAt(i));
                i++;
            }

        }
        if (cadenaInvalida) {
            //indica que la cadena no es valida
            String lexemaInvalido = lexema.toString();
            System.out.println("ERROR: cadena " + lexemaInvalido + " invalida en"
                    + " (" + fila + "," + startCol + ")");

        } else if (esDecimal) {
            System.out.println("TOKEN: DECIMAL, LEXEMA: " + lexema
                    + " (" + fila + "," + startCol + ")");
        } else {
            System.out.println("TOKEN: NUMERO, LEXEMA: " + lexema
                    + " (" + fila + "," + startCol + ")");
        }
        return i--;
    }

    private int analizadorComentarios(StringBuilder lexema, int columna, int fila, int i, String texto) {

        lexema.setLength(0);///limpiar
        String simbolo = String.valueOf(texto.charAt(i));

        if (i + 1 < texto.length()) {//Si al final ya no hay nada solo es un operador

            if ((texto.charAt(i + 1) == ' ')) {//es un operador nada mas

                System.out.println("TOKEN: OPERADOR, LEXEMA: " + "'" + simbolo + "'"
                        + " (" + fila + "," + columna + ")");

            } else if (i < texto.length() && (texto.charAt(i) == '/')) {//es un comentario de una linea

                while (i < texto.length()) {
                    lexema.append(texto.charAt(i));
                    i++;
                }

                String comentario = lexema.toString();
                System.out.println("TOKEN: COMENTARIO_LINEA, LEXEMA: " + comentario
                        + " (" + fila + "," + columna + ")");
            }

        }
        System.out.println("TOKEN: OPERADOR, LEXEMA: " + "'" + simbolo + "'"
                + " (" + fila + "," + columna + ")");
        return i--;
    }

}
