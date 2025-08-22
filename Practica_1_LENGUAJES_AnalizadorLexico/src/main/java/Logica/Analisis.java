package Logica;

import GestionArchivos.Configuracion;

public class Analisis {

    private Configuracion config;
    private String comentarioLinea, comentarioBI, comentarioBF;//para saber como inician y terminan los comentarios 

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

            // letra - posible identificador o palabra reservada
            if (Character.isLetter(c)) {

                i = analizarLetras(lexema, columna, fila, i, texto, config);

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

                comentarioBloque(lexema, columna, fila, i, texto, cerrado);

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

            } else if (c == '\n') {

                fila++;
                columna = 1;

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
            errorEncontrado(fila, startCol, lexemaInvalido + "");

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
        boolean bloqueCerrado = false;

        if (i + 1 < texto.length() || (texto.charAt(i + 1)) == ' ') {//Si al final ya no hay nada o está vacio solo es un operador

            if ((texto.charAt(i + 1)) != ' ' || ((texto.charAt(i + 1) != '/') || (texto.charAt(i + 1) != '*'))) {//es un error

                String cadena = lexema.toString();
                System.out.println("ERRO: conjunto no valido: " + cadena + " en"
                        + " (" + fila + "," + columna + ")");

            } else if (i < texto.length() && (texto.charAt(i + 1) == '/')) {//es un comentario de una linea

                while (i < texto.length()) {
                    lexema.append(texto.charAt(i));
                    i++;
                }

                String comentario = lexema.toString();
                System.out.println("TOKEN: COMENTARIO_LINEA, LEXEMA: " + comentario
                        + " (" + fila + "," + columna + ")");

            } else if (i < texto.length() && (texto.charAt(i + 1) == '*')) {//Es un bloque inicio

                while (i < texto.length() || bloqueCerrado == true) {
                    lexema.append(texto.charAt(i));
                    i++;

                    if (texto.charAt(i) == '*' && texto.charAt(i + 1) == '/') {//encuentra el bloque fin
                        bloqueCerrado = true;
                    }
                }

            }

        }

        if (bloqueCerrado) {

            String comentario = lexema.toString();
            System.out.println("TOKEN: COMENTARIO_BLOQUE, LEXEMA: " + comentario
                    + " (" + fila + "," + columna + ")");
        } else {

            System.out.println("TOKEN: OPERADOR, LEXEMA: " + "'" + simbolo + "'"
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

    private void comentarioBloque(StringBuilder lexema, int columna, int fila, int i, String texto, boolean cerrado) {

        if (cerrado) {
            System.out.println("TOKEN: COMENTARIO_BLOQUE, LEXEMA: " + "'" + lexema + "'"//Si está cerrado
                    + " (" + fila + "," + columna + ")");
        } else {
            errorEncontrado(fila, columna, lexema+"");//Si no está cerrado
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
