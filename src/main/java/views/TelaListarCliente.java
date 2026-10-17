package views;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.bean.Cliente;
import model.dao.ClienteDAO;

public class TelaListarCliente extends javax.swing.JFrame {
    private List<Cliente> registros = List.of();

    public TelaListarCliente() {
        initComponents();
        tblRegistros.setModel(new DefaultTableModel(new Object[]{"ID", "Nome", "E-mail", "Telefone"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int column) { return column == 0 ? Integer.class : String.class; }
        });
        ApoioTela.tabela(tblRegistros);
        carregar();
        pack();
        ApoioTela.configurar(this, "Clientes");
    }

    private void carregar() {
        registros = new ClienteDAO().listar();
        DefaultTableModel modelo = (DefaultTableModel) tblRegistros.getModel();
        tblRegistros.setRowSorter(null);
        tblRegistros.clearSelection();
        modelo.setRowCount(0);
        for (Cliente c : registros) modelo.addRow(new Object[]{c.getIdCliente(), c.getNomeCliente(), c.getEmailCliente(), c.getTelefoneCliente()});
        tblRegistros.setRowSorter(new javax.swing.table.TableRowSorter<>(modelo));
        lblStatus.setText(registros.size() + " registro(s) • Selecione uma linha para editar ou excluir.");
    }

    private Cliente selecionado() {
        int linha = tblRegistros.getSelectedRow();
        if (linha < 0) throw new IllegalArgumentException("Selecione um cliente na tabela.");
        return registros.get(tblRegistros.convertRowIndexToModel(linha));
    }

    private void novo(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_novo
        ApoioTela.abrir(this, TelaCadastroCliente::new);
    }//GEN-LAST:event_novo

    private void editar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editar
        try {
            Cliente c = selecionado();
            ApoioTela.abrir(this, () -> new TelaEditarCliente(c));
        } catch (RuntimeException ex) { ApoioTela.erro(this, ex); }
    }//GEN-LAST:event_editar

    private void excluir(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluir
        try {
            Cliente c = selecionado();
            if (!ApoioTela.confirmar(this, "Excluir " + c.getNomeCliente() + "?")) return;
            new ClienteDAO().excluir(c.getIdCliente());
            carregar();
            ApoioTela.sucesso(this, "Cliente excluído com sucesso.");
        } catch (RuntimeException ex) { ApoioTela.erro(this, ex); }
    }//GEN-LAST:event_excluir

    private void atualizar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atualizar
        try { carregar(); } catch (RuntimeException ex) { ApoioTela.erro(this, ex); }
    }//GEN-LAST:event_atualizar

    private void voltar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voltar
        ApoioTela.abrir(this, TelaMenuPrincipal::new);
    }//GEN-LAST:event_voltar

    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        painel = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        rolagem = new javax.swing.JScrollPane();
        tblRegistros = new javax.swing.JTable();
        rodape = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        botoes = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnAtualizar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setLayout(new java.awt.BorderLayout(12, 16));
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblTitulo.setText("CLIENTES");
        painel.add(lblTitulo, java.awt.BorderLayout.NORTH);
        rolagem.setViewportView(tblRegistros);
        painel.add(rolagem, java.awt.BorderLayout.CENTER);
        rodape.setLayout(new java.awt.BorderLayout(0, 12));
        lblStatus.setText(" ");
        rodape.add(lblStatus, java.awt.BorderLayout.NORTH);
        botoes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        btnNovo.setText("Cadastrar");
        btnNovo.addActionListener(this::novo);
        botoes.add(btnNovo);
        btnEditar.setText("Editar");
        btnEditar.addActionListener(this::editar);
        botoes.add(btnEditar);
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(this::excluir);
        botoes.add(btnExcluir);
        btnAtualizar.setText("Atualizar");
        btnAtualizar.addActionListener(this::atualizar);
        botoes.add(btnAtualizar);
        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(this::voltar);
        botoes.add(btnVoltar);
        rodape.add(botoes, java.awt.BorderLayout.CENTER);
        painel.add(rodape, java.awt.BorderLayout.SOUTH);
        getContentPane().add(painel, java.awt.BorderLayout.CENTER);
        pack();
    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel painel;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JScrollPane rolagem;
    private javax.swing.JTable tblRegistros;
    private javax.swing.JPanel rodape;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel botoes;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnVoltar;
    // End of variables declaration//GEN-END:variables
}
