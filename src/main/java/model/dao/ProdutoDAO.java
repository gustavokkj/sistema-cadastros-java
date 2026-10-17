package model.dao;

import conexao.Conexao;
import model.bean.Produto;
import model.bean.Categoria;
import model.bean.Validacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private static final String CONSULTA = "SELECT p.id_produto, p.nome_produto, p.preco, p.quantidade, "
            + "p.id_categoria, c.nome_categoria FROM produto p LEFT JOIN categoria c ON c.id_categoria=p.id_categoria";

    public void salvar(Produto p) {
        Validacao.produto(p);
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(
                     "INSERT INTO produto (nome_produto, preco, quantidade, id_categoria) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            preencher(stmt, p);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getInt(1));
            }
        } catch (SQLException ex) { throw ErrosDAO.banco("cadastrar o produto", ex); }
    }

    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(CONSULTA + " ORDER BY p.id_produto");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(ler(rs));
        } catch (SQLException ex) { throw ErrosDAO.banco("listar os produtos", ex); }
        return lista;
    }

    public Produto read(int id) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(CONSULTA + " WHERE p.id_produto=?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? ler(rs) : null;
            }
        } catch (SQLException ex) { throw ErrosDAO.banco("consultar o produto", ex); }
    }

    public void atualizar(Produto p) {
        Validacao.produto(p);
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(
                     "UPDATE produto SET nome_produto=?, preco=?, quantidade=?, id_categoria=? WHERE id_produto=?")) {
            preencher(stmt, p);
            stmt.setInt(5, p.getId());
            ErrosDAO.conferir(stmt.executeUpdate());
        } catch (SQLException ex) { throw ErrosDAO.banco("atualizar o produto", ex); }
    }

    public void excluir(int id) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement("DELETE FROM produto WHERE id_produto=?")) {
            stmt.setInt(1, id);
            ErrosDAO.conferir(stmt.executeUpdate());
        } catch (SQLException ex) { throw ErrosDAO.banco("excluir o produto", ex); }
    }

    private void preencher(PreparedStatement stmt, Produto p) throws SQLException {
        stmt.setString(1, p.getNomeProduto());
        stmt.setBigDecimal(2, p.getPreco());
        stmt.setInt(3, p.getQuantidade());
        if (p.getCategoria() == null) stmt.setNull(4, Types.INTEGER);
        else stmt.setInt(4, p.getCategoria().getId_categoria());
    }

    private Produto ler(ResultSet rs) throws SQLException {
        Integer categoriaId = (Integer) rs.getObject("id_categoria");
        Categoria categoria = categoriaId == null ? null : new Categoria(categoriaId, rs.getString("nome_categoria"));
        return new Produto(rs.getInt("id_produto"), rs.getString("nome_produto"), rs.getBigDecimal("preco"),
                rs.getInt("quantidade"), categoria);
    }
}
