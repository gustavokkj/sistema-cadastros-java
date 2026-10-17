package model.dao;

import conexao.Conexao;
import model.bean.Categoria;
import model.bean.Validacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public void inserir(Categoria c) {
        Validacao.categoria(c);
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(
                     "INSERT INTO categoria (nome_categoria) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getNome_categoria());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) c.setId_categoria(keys.getInt(1));
            }
        } catch (SQLException ex) { throw ErrosDAO.banco("cadastrar a categoria", ex); }
    }

    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM categoria ORDER BY id_categoria");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria")));
        } catch (SQLException ex) { throw ErrosDAO.banco("listar as categorias", ex); }
        return lista;
    }

    public void atualizar(Categoria c) {
        Validacao.categoria(c);
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(
                     "UPDATE categoria SET nome_categoria=? WHERE id_categoria=?")) {
            stmt.setString(1, c.getNome_categoria());
            stmt.setInt(2, c.getId_categoria());
            ErrosDAO.conferir(stmt.executeUpdate());
        } catch (SQLException ex) { throw ErrosDAO.banco("atualizar a categoria", ex); }
    }

    public void excluir(int id) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement("DELETE FROM categoria WHERE id_categoria=?")) {
            stmt.setInt(1, id);
            ErrosDAO.conferir(stmt.executeUpdate());
        } catch (SQLException ex) { throw ErrosDAO.banco("excluir a categoria", ex); }
    }
}
