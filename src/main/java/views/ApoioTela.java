package views;

import java.awt.Component;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

final class ApoioTela {
    static final String NOME = "Sistema de Cadastros";
    private ApoioTela() { }

    static void configurar(JFrame tela, String titulo) {
        tela.setTitle(titulo + " • " + NOME);
        java.awt.image.BufferedImage icone = new java.awt.image.BufferedImage(32, 32, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = icone.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new java.awt.Color(37, 77, 107));
        g.fillRoundRect(0, 0, 32, 32, 8, 8);
        g.setColor(java.awt.Color.WHITE);
        g.fillRoundRect(7, 7, 18, 4, 2, 2);
        g.fillRect(7, 15, 4, 3);
        g.fillRect(14, 15, 11, 3);
        g.fillRect(7, 22, 4, 3);
        g.fillRect(14, 22, 11, 3);
        g.dispose();
        tela.setIconImage(icone);
        tela.setLocationRelativeTo(null);
    }

    static void abrir(JFrame atual, Supplier<? extends JFrame> destino) {
        try {
            JFrame proxima = destino.get();
            proxima.setVisible(true);
            atual.dispose();
        } catch (RuntimeException ex) { erro(atual, ex); }
    }

    static void erro(Component tela, RuntimeException ex) {
        JOptionPane.showMessageDialog(tela, ex.getMessage(), "Não foi possível concluir",
                ex instanceof IllegalArgumentException ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    static void sucesso(Component tela, String mensagem) {
        JOptionPane.showMessageDialog(tela, mensagem, NOME, JOptionPane.INFORMATION_MESSAGE);
    }

    static boolean confirmar(Component tela, String mensagem) {
        return JOptionPane.showConfirmDialog(tela, mensagem, "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    static void tabela(JTable tabela) {
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(30);
        tabela.setFillsViewportHeight(true);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setAutoCreateRowSorter(true);
        tabela.setPreferredScrollableViewportSize(new Dimension(700, 260));
        tabela.getColumnModel().getColumn(0).setMaxWidth(65);
    }

    static void moeda(JTable tabela, int coluna) {
        tabela.getColumnModel().getColumn(coluna).setCellRenderer(new DefaultTableCellRenderer() {
            private final NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
            @Override protected void setValue(Object valor) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                setText(valor == null ? "" : formato.format(valor));
            }
        });
    }
}
