package InterfazGrafica;

import GestionArchivos.Configuracion;
import Logica.Analisis;
import Logica.CrearReportes;
import Logica.Token;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.*;

public class AnalizadorLexicoIG extends javax.swing.JFrame {

    public StyledDocument estiloDocumento1;
    private StyledDocument estiloDocumento2;
    private Analisis analizador;
    private List<Token> tokens = new ArrayList<>();
    private Configuracion config;

    public AnalizadorLexicoIG(Analisis analizador, Configuracion config) {
        initComponents();
        this.config = config;
        this.estiloDocumento1 = Editor1.getStyledDocument();
        this.estiloDocumento2 = Editor2.getStyledDocument();
        this.analizador = analizador;
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Analizador Lexico");
        definirEstilos(Editor1);
        //Editor2.setEnabled(false);

    }

    public void definirEstilos(JTextPane editor) {//define cuales son los colores para cada token
        Color cafe = new Color(111, 78, 55);
        Color verdeOscuro = new Color(28, 86, 49);
        Color morado = new Color(160, 32, 240);

        StyledDocument estiloDocumento = editor.getStyledDocument();

        Style colorReservadas = editor.addStyle("reservadas", null);
        StyleConstants.setForeground(colorReservadas, Color.BLUE);

        Style colorIdentificadores = editor.addStyle("identificadores", null);
        StyleConstants.setForeground(colorIdentificadores, cafe);

        Style colorNumeros = editor.addStyle("numeros", null);
        StyleConstants.setForeground(colorNumeros, Color.GREEN);

        Style colorCadenas = editor.addStyle("cadenas", null);
        StyleConstants.setForeground(colorCadenas, Color.orange);

        Style colorDecimales = editor.addStyle("decimales", null);
        StyleConstants.setForeground(colorDecimales, Color.BLACK);

        Style colorComentarios = editor.addStyle("comentarios", null);
        StyleConstants.setForeground(colorComentarios, verdeOscuro);

        Style colorOperadores = editor.addStyle("operadores", null);
        StyleConstants.setForeground(colorOperadores, Color.YELLOW);

        Style colorAgrupadores = editor.addStyle("agrupadores", null);
        StyleConstants.setForeground(colorAgrupadores, morado);

        Style colorErrores = editor.addStyle("errores", null);
        StyleConstants.setForeground(colorErrores, Color.RED);

    }

    private void limpiarPantalla() {

        for (JInternalFrame frame : vistaEscritorio.getAllFrames()) {
            frame.setVisible(false);//Cierra todas las ventanas que estubieran abiertas
        }

    }

    public void colorearTokens() {
        String textoIngresado = "";
        try {
            textoIngresado = estiloDocumento1.getText(0, estiloDocumento1.getLength()) + " ";
        } catch (Exception e) {
            System.out.println("ERROR AL LIMPIAR EL TEXTO " + e.getMessage());
        }

        tokens.clear();//limpia la lsta
        tokens = analizador.analizarTexto(textoIngresado + " ");

        try {

            estiloDocumento1.remove(0, estiloDocumento1.getLength());//limpia el txtPane

            for (Token t : tokens) {
                Style estilo = estiloDocumento1.getStyle("default");

                if ("PALABRA_RESERVADA".equals(t.getTipo())) {
                    estilo = estiloDocumento1.getStyle("reservadas");

                } else if ("IDENTIFICADOR".equals(t.getTipo())) {
                    estilo = estiloDocumento1.getStyle("identificadores");

                } else if ("NUMERO".equals(t.getTipo())) {
                    estilo = estiloDocumento1.getStyle("numeros");

                } else if ("CADENA".equals(t.getTipo())) {
                    estilo = estiloDocumento1.getStyle("cadenas");

                } else if ("DECIMAL".equals(t.getTipo())) {
                    estilo = estiloDocumento1.getStyle("decimales");

                } else if ("COMENTARIO_LINEA".equals(t.getTipo()) || "COMENTARIO_BLOQUE".equals(t.getTipo())) {
                    estilo = estiloDocumento1.getStyle("comentarios");

                } else if ("OPERADOR".equals(t.getTipo())) {
                    estilo = estiloDocumento1.getStyle("operadores");

                } else if ("AGRUPACION".equals(t.getTipo())) {
                    estilo = estiloDocumento1.getStyle("agrupadores");

                } else if ("ERROR".equals(t.getTipo())) {
                    estilo = estiloDocumento1.getStyle("errores");

                }

                estiloDocumento1.insertString(estiloDocumento1.getLength(), t.getLexema() + " ", estilo);

            }

        } catch (Exception e) {
            System.out.println("ERROR AL PINTAR EL TEXTO " + e.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        vistaEscritorio = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        Editor1 = new javax.swing.JTextPane();
        btnAnalizar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        txtBusqueda = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        Editor2 = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuCargar = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        menuGuardar = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(Editor1);

        btnAnalizar.setText("Analizar");
        btnAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(Editor2);

        vistaEscritorio.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        vistaEscritorio.setLayer(btnAnalizar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        vistaEscritorio.setLayer(btnBuscar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        vistaEscritorio.setLayer(txtBusqueda, javax.swing.JLayeredPane.DEFAULT_LAYER);
        vistaEscritorio.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout vistaEscritorioLayout = new javax.swing.GroupLayout(vistaEscritorio);
        vistaEscritorio.setLayout(vistaEscritorioLayout);
        vistaEscritorioLayout.setHorizontalGroup(
            vistaEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vistaEscritorioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vistaEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(vistaEscritorioLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(vistaEscritorioLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(vistaEscritorioLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 236, Short.MAX_VALUE)
                        .addComponent(btnAnalizar)
                        .addGap(15, 15, 15))))
        );
        vistaEscritorioLayout.setVerticalGroup(
            vistaEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vistaEscritorioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(vistaEscritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnalizar)
                    .addComponent(btnBuscar)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(vistaEscritorio)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(vistaEscritorio)
        );

        menuCargar.setText("Cargar Archivo");

        jMenuItem1.setText("Cargar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menuCargar.add(jMenuItem1);

        jMenuBar1.add(menuCargar);

        menuGuardar.setText("Guardar Archivo");

        jMenuItem2.setText("Guardar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        menuGuardar.add(jMenuItem2);

        jMenuBar1.add(menuGuardar);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        limpiarPantalla();
        IFCargarArchivo cargar = new IFCargarArchivo(this);
        vistaEscritorio.add(cargar);
        cargar.show();


    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarActionPerformed

        try {

            if (estiloDocumento1.getText(0, estiloDocumento1.getLength()).isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay texto", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                colorearTokens();
                enlistarResultados();
            }

        } catch (Exception e) {

        }


    }//GEN-LAST:event_btnAnalizarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

        try {
            tokens.clear();//limpia la lsta
            tokens = analizador.analizarTexto(estiloDocumento1.getText(0, estiloDocumento1.getLength()) + " ");//Analiza el texto
        } catch (Exception e) {
            System.out.println("ERROR AL CARGAR TOKENS " + e.getMessage());
        }

        if (tokens.isEmpty() || txtBusqueda.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay texto", "ERROR", JOptionPane.ERROR_MESSAGE);

        } else {
            String busqueda = txtBusqueda.getText();
            resaltarBusqueda(busqueda, tokens);

        }


    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        CrearReportes crear = new CrearReportes(config);
        try {
            crear.guardarTextoEntrada(estiloDocumento1.getText(0, estiloDocumento1.getLength()));
            JOptionPane.showMessageDialog(null, "Texto guardado", "HECHO", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            System.out.println("ERROR AL EXPORTAR TEXTO " + e.getMessage());
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void enlistarResultados() {

        String textoIngresado = Editor1.getText();
        tokens = analizador.analizarTexto(textoIngresado);

        CrearReportes crear = new CrearReportes(config);
        crear.filtrarLista(tokens);

    }

    private void resaltarBusqueda(String busqueda, List<Token> tokens) {

        try {
            estiloDocumento2.remove(0, estiloDocumento2.getLength());//Limpia el espacio
            for (Token t : tokens) {
                Style estilo = estiloDocumento2.getStyle("default");//Estilo predeterminado

                if (busqueda.equals(t.getLexema())) {//Si coincide con la busqueda
                    estilo = estiloDocumento1.getStyle("errores");//Lo pinta de color rojo
                }
                estiloDocumento2.insertString(estiloDocumento2.getLength(), t.getLexema() + " ", estilo);//Agrega el texto 
            }
        } catch (Exception e) {
        }

    }

    public JTextPane getEditor1() {
        return Editor1;
    }

    public void setEditor1(JTextPane Editor1) {
        this.Editor1 = Editor1;
    }

    public JTextPane getEditor2() {
        return Editor2;
    }

    public void setEditor2(JTextPane Editor2) {
        this.Editor2 = Editor2;
    }

    public StyledDocument getEstiloDocumento1() {
        return estiloDocumento1;
    }

    public void setEstiloDocumento1(StyledDocument estiloDocumento1) {
        this.estiloDocumento1 = estiloDocumento1;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane Editor1;
    private javax.swing.JTextPane Editor2;
    private javax.swing.JButton btnAnalizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenu menuCargar;
    private javax.swing.JMenu menuGuardar;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JDesktopPane vistaEscritorio;
    // End of variables declaration//GEN-END:variables
}
