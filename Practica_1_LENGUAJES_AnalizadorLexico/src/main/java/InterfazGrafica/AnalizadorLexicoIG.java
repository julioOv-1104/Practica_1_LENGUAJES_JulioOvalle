package InterfazGrafica;

import Logica.Analisis;
import Logica.Token;
import java.awt.Color;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

public class AnalizadorLexicoIG extends javax.swing.JFrame {

    private StyledDocument estiloDocumento;
    private Analisis analizador;
    private boolean repintando = false;

    public AnalizadorLexicoIG(Analisis analizador) {
        initComponents();
        this.estiloDocumento = Editor1.getStyledDocument();
        this.analizador = analizador;
        definirEstilos(Editor1);

        Editor1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                analizarYColorear();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                analizarYColorear();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                analizarYColorear();
            }

            private void analizarYColorear() {

                if (repintando) {
                    return;
                }
                
                    repintando = true;

                    try {
                        String textoIngresado = Editor1.getText() + " ";
                        List<Token> tokensEncontrados = analizador.analizarTexto(textoIngresado);
                        color(tokensEncontrados, Editor1);
                    } catch (Exception e) {
                        System.out.println("ERROR " + e.getMessage());
                    }
                    repintando = false;

                
            }

        });

    }

    public void guardar() {

        Editor1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                analizarYColorear();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                analizarYColorear();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                analizarYColorear();
            }

            private void analizarYColorear() {

                if (repintando) {
                    return;
                }
                repintando = true;

                try {
                    String textoIngresado = Editor1.getText();
                    List<Token> tokensEncontrados = analizador.analizarTexto(textoIngresado);
                    color(tokensEncontrados, Editor1);
                } catch (Exception e) {
                    System.out.println("ERROR " + e.getMessage());
                }
                repintando = false;
            }

        });

    }

    public void definirEstilos(JTextPane editor) {
        StyledDocument estiloDocumento = editor.getStyledDocument();

        Style colorReservadas = Editor1.addStyle("reservadas", null);
        StyleConstants.setForeground(colorReservadas, Color.blue);

    }

    public void color(List<Token> tokens, JTextPane editor) {

        try {

            estiloDocumento.remove(0, estiloDocumento.getLength());

            for (Token t : tokens) {
                Style estilo = editor.getStyle("default");

                if ("PALABRA_RESERVADA".equals(t.getTipo())) {
                    estilo = editor.getStyle("reservadas");
                }

                estiloDocumento.insertString(estiloDocumento.getLength(), t.getLexema() + " ", estilo);
            }
        } catch (Exception ex) {
            System.out.println("ERROR EN COLOREAR " + ex.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Editor1 = new javax.swing.JTextPane();
        btnAnalizar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        Editor2 = new javax.swing.JTextPane();
        txtBusqueda = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(Editor1);

        btnAnalizar.setText("Analizar");
        btnAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(Editor2);

        txtBusqueda.setText("Busqueda...");

        btnBuscar.setText("Buscar");

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 292, Short.MAX_VALUE)
                        .addComponent(btnAnalizar))
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap())
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(btnAnalizar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenu1.setText("Cargar Archivo");

        jMenuItem1.setText("Cargar");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Guardar Archivo");

        jMenuItem2.setText("Guardar");
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Importar Reporte");

        jMenuItem3.setText("Importar");
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarActionPerformed

        String textoIngresado = Editor1.getText();
        List<Token> tokens = analizador.analizarTexto(textoIngresado + " ");

        try {
            estiloDocumento.remove(0, estiloDocumento.getLength());

            for (Token t : tokens) {
                Style estilo = estiloDocumento.getStyle("default");

                if ("PALABRA_RESERVADA".equals(t.getTipo())) {
                    estilo = estiloDocumento.getStyle("reservadas");
                }

                estiloDocumento.insertString(estiloDocumento.getLength(), t.getLexema() + " ", estilo);
            }

        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnAnalizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane Editor1;
    private javax.swing.JTextPane Editor2;
    private javax.swing.JPanel Panel1;
    private javax.swing.JButton btnAnalizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables
}
