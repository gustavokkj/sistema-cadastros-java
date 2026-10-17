package model.dao;

import conexao.Conexao;
import model.bean.Cliente;
import model.bean.Validacao;
import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO {
    public boolean criar(Cliente c) {
        Validacao.cliente(c);
        String sql = "INSERT INTO cliente (nome_cliente, email_cliente, telefone_cliente) VALUES (?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(stmt, c);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) c.setIdCliente(keys.getInt(1));
            }
            return true;
        } catch (SQLException ex) { throw ErrosDAO.banco("cadastrar o cliente", ex); }
    }

    public ArrayList<Cliente> listar() {
        ArrayList<Cliente> lista = new ArrayList<>();
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM cliente ORDER BY id_cliente");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setNomeCliente(rs.getString("nome_cliente"));
                c.setEmailCliente(rs.getString("email_cliente"));
                c.setTelefoneCliente(rs.getString("telefone_cliente"));
                lista.add(c);
            }
        } catch (SQLException ex) { throw ErrosDAO.banco("listar os clientes", ex); }
        return lista;
    }

    public boolean atualizar(Cliente c) {
        Validacao.cliente(c);
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(
                     "UPDATE cliente SET nome_cliente=?, email_cliente=?, telefone_cliente=? WHERE id_cliente=?")) {
            preencher(stmt, c);
            stmt.setInt(4, c.getIdCliente());
            ErrosDAO.conferir(stmt.executeUpdate());
            return true;
        } catch (SQLException ex) { throw ErrosDAO.banco("atualizar o cliente", ex); }
    }

    public boolean excluir(int idCliente) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement("DELETE FROM cliente WHERE id_cliente=?")) {
            stmt.setInt(1, idCliente);
            ErrosDAO.conferir(stmt.executeUpdate());
            return true;
        } catch (SQLException ex) { throw ErrosDAO.banco("excluir o cliente", ex); }
    }

    private void preencher(PreparedStatement stmt, Cliente c) throws SQLException {
        stmt.setString(1, c.getNomeCliente());
        stmt.setString(2, c.getEmailCliente());
        stmt.setString(3, c.getTelefoneCliente());
    }
}
