package Logica;

import GestionArchivos.Configuracion;
import InterfazGrafica.ReporteErrores;
import InterfazGrafica.ReporteTokens;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class CrearReportes {

    private List<Token> listaErrores = new ArrayList<>();//Se guardan los errores
    private List<Token> listaTokens = new ArrayList<>();//Se guardan los tokens validos
    private List<String> listaUsados = new ArrayList<>();// Se guardan los tokens usados
    private List<String> listaNoUtilizados = new ArrayList<>();// Se guardan los tokens que no fueron usados
    private ReporteErrores jfErrores = new ReporteErrores();//el JFrame de Errores
    private ReporteTokens jfTokens = new ReporteTokens();// JFrame cuando no hay errores
    private Configuracion config;

    public CrearReportes(Configuracion config) {
        this.config = config;
    }

    public void filtrarLista(List<Token> tokens) {
        boolean errores = false;

        for (Token t : tokens) {

            if ("ERROR".equals(t.getTipo())) {
                errores = true;
                listaErrores.add(t);
            } else {
                listaTokens.add(t);//listado con tokens validos
            }

        }
        if (errores) {//Si hay al menos un error
            mostrarErrores(listaErrores, jfErrores.getTxtArea());

        } else {// Si no hay errores
            mostrarTokens(listaTokens, jfTokens.getTxtAreaReporte(), jfTokens.getTxtAreaRecuento());
        }

        //Muestra el reporte general
        crearReporteGeneral();
    }

    public void mostrarErrores(List<Token> errores, JTextArea areaReporte) {
        StringBuilder reporte = new StringBuilder();
        reporte.append("------------------------------------------------------------\n");
        reporte.append(String.format("| %-15s | %-7s | %-10s |\n", "Simbolo o cadena", "Fila", "Columna"));
        reporte.append("------------------------------------------------------------\n");

        for (Token e : errores) {

            reporte.append(String.format("| %15s  (%-3d , %-5d) \n",
                    e.getLexema(), e.getFila(), e.getColumna()));

        }
        reporte.append("------------------------------------------------------------\n");

        //  Mostrar en el JTextArea
        areaReporte.setText(reporte.toString());

        //  Guardar en archivo .txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("errores.txt"))) {
            writer.write(reporte.toString());
            System.out.println("Reporte de errores generado en errores.txt");
        } catch (IOException ex) {
            System.out.println("ERROR AL EXPORTAR REPORTE DE ERRORES " + ex.getMessage());
        }

        jfErrores.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jfErrores.setVisible(true);
    }

    public void mostrarTokens(List<Token> tokens, JTextArea areaReporte, JTextArea areaRecuento) {

        StringBuilder reporte = new StringBuilder();
        reporte.append("------------------------------------------------------------\n");
        reporte.append(String.format("| %-15s | %-15s | %-7s | %-10s |\n", "Token", "Lexema", "Fila", "Columna"));
        reporte.append("------------------------------------------------------------\n");

        for (Token t : tokens) {

            reporte.append(String.format("| %15s | %15s (%-3d , %-5d) \n",
                    t.getTipo(), t.getLexema(), t.getFila(), t.getColumna()));

        }
        reporte.append("------------------------------------------------------------\n");

        //  Mostrar en los JTextAreas
        StringBuilder recuento = contarTokens(tokens);
        areaRecuento.setText(recuento.toString());
        areaReporte.setText(reporte.toString());

        //  Guardar en archivo .txt los tokens y el recuento
        try (PrintWriter writer = new PrintWriter(new FileWriter("tokens.txt"))) {
            writer.write(reporte.toString());
            System.out.println("Reporte de tokens generado en tokens.txt");
        } catch (IOException e) {
            System.out.println("ERROR AL EXPORTAR REPORTE DE TOKENS " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("recuento.txt"))) {
            writer.write(recuento.toString());
            System.out.println("Reporte de recuento generado en tokens.txt");
        } catch (IOException e) {
            System.out.println("ERROR AL EXPORTAR REPORTE DE recuento " + e.getMessage());
        }

        jfTokens.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jfTokens.setVisible(true);

    }

    private StringBuilder contarTokens(List<Token> tokens) {

        StringBuilder recuento = new StringBuilder();
        recuento.append("------------------------------------------------------------\n");
        recuento.append(String.format("| %-15s | %-15s | %-15s |\n", "Token", "Lexema", "Cantidad"));
        recuento.append("------------------------------------------------------------\n");

        List<Token> copia = new ArrayList<>();
        List<Token> contados = new ArrayList<>();

        for (Token t : tokens) {//Crea una copia de la lista de tokens
            copia.add(t);
        }

        for (int i = 0; i < copia.size(); i++) {//recorre la lista copiada
            int cantidad = 0;
            for (int j = 0; j < tokens.size(); j++) {//compara la lista original con la copiada

                if (copia.get(i).getLexema().equals(tokens.get(j).getLexema())) {
                    cantidad++;

                }

            }
            contados.add(copia.get(i));

            recuento.append(String.format("| %15s | %15s | %-7d |\n",
                    copia.get(i).getTipo(), copia.get(i).getLexema(), cantidad));

        }
        recuento.append("------------------------------------------------------------\n");

        System.out.println(recuento.toString());
        return recuento;
    }

    public void guardarTextoEntrada(String texto) {

        try (PrintWriter writer = new PrintWriter(new FileWriter("textoEntrada.txt"))) {
            writer.write(texto);
            System.out.println("Texto de entrada guardado en textoEntrada.txt");
        } catch (IOException e) {
            System.out.println("ERROR AL EXPORTAR TEXTO DE ENTRADA " + e.getMessage());
        }
    }

    public void crearReporteGeneral() {
        listaUsados.clear();
        listaNoUtilizados.clear();//Limpia las listas

        int errores = listaErrores.size();
        int validos = listaTokens.size();

        int total = errores + validos;

        double porcentajeValidos = (100 * validos) / total;

        for (Token v : listaTokens) {//Recorre el listado de validos y lo compara con el de usados
            String lexema = v.getLexema();
            String tipo = v.getTipo();

            if (tipo.equals("IDENTIFICADOR") || tipo.equals("DECIMAL")
                    || tipo.equals("NUMERO") || tipo.equals("CADENA")
                    || tipo.equals("COMENTARIO_BLOQUE") || tipo.equals("COMENTARIO_LINEA")) {//Si es alguno de estos tokens los ignora

            } else {
                if (listaUsados.contains(lexema)) {

                } else {
                    listaUsados.add(lexema);
                }
            }

        }
        
        encontrarNoUsados(config.getPalabrasReservadas());
        encontrarNoUsados(config.getAgrupacion());
        encontrarNoUsados(config.getPuntuacion());
        encontrarNoUsados(config.getOperadores());//Pasa por todas las listas de tokens para encontrar las que faltan
        
        JOptionPane.showMessageDialog(null, "Cantidad de errores: "+listaErrores.size()+"\n"
                + "Porcentaje de tokens validos: " +porcentajeValidos+"% \n Tokens no utlizados: " +listaNoUtilizados,
                "Reporte General",JOptionPane.PLAIN_MESSAGE);

    }

    private void encontrarNoUsados(List<String> lista) {
        
        for (String string : lista) {
            
            if (!listaUsados.contains(string)) {
                listaNoUtilizados.add(string);
            }
        }

    }

}
