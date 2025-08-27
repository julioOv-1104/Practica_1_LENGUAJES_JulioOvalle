package Logica;

import InterfazGrafica.ReporteErrores;
import InterfazGrafica.ReporteTokens;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class CrearReportes {

    private List<Token> listaErrores = new ArrayList<>();
    private List<Token> listaTokens = new ArrayList<>();
    private ReporteErrores jfErrores = new ReporteErrores();//el JFrame de Errores
    private ReporteTokens jfTokens = new ReporteTokens();

    public void filtrarLista(List<Token> tokens) {
        boolean errores = false;

        for (Token t : tokens) {

            if ("ERROR".equals(t.getTipo())) {
                errores = true;
                listaErrores.add(t);
            }

            listaTokens.add(t);
        }
        if (errores) {//Si hay al menos un error

            mostrarErrores(listaErrores, jfErrores.getTxtArea());

        } else {// Si no hay errores
            mostrarTokens(listaTokens, jfTokens.getTxtAreaReporte(), jfTokens.getTxtAreaRecuento());
        }
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

}
