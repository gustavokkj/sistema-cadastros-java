/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import conexao.Conexao;
import model.bean.Produto;
import model.bean.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gusta
 */
public class ProdutoDAO {

    public void salvar(Produto p) {
        Connection con = Conexao.getConexao();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(
                "INSERT INTO produto (nome_produto, preco, quantidade, id_categoria) VALUES (?, ?, ?, ?)"
            );
            
            stmt.setString(1, p.getNomeProduto());
            stmt.setDouble(2, p.getPreco());
            stmt.setInt(3, p.getQuantidade());
            stmt.setInt(4, p.getCategoria().getId_categoria()); 

            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao salvar produto: " + ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, stmt);
        }
    }

    public List<Produto> listarTodos() {
        Connection con = Conexao.getConexao();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Produto> lista = new ArrayList<>();

        
        try {
            stmt = con.prepareStatement("SELECT id_produto, nome_produto, preco, quantidade, id_categoria FROM produto");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id_produto"));
                p.setNomeProduto(rs.getString("nome_produto"));
                p.setPreco(rs.getDouble("preco"));
                p.setQuantidade(rs.getInt("quantidade"));
                
                Categoria c = new Categoria();
                c.setId_categoria(rs.getInt("id_categoria"));
                
                p.setCategoria(c);
                lista.add(p);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao listar produtos: " + ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, stmt, rs);
        }

        return lista;
    }
     public Produto read(int id_produto) {
        Connection con = Conexao.getConexao();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Produto> lista = new ArrayList<>();

        
        try {
            stmt = con.prepareStatement("SELECT id_produto, nome_produto, preco, quantidade, id_categoria FROM produto "
                    + "Where id_produto = ?");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id_produto"));
                p.setNomeProduto(rs.getString("nome_produto"));
                p.setPreco(rs.getDouble("preco"));
                p.setQuantidade(rs.getInt("quantidade"));
                
                Categoria c = new Categoria();
                c.setId_categoria(rs.getInt("id_categoria"));
                
                p.setCategoria(c);
                return p;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao listar produtos: " + ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, stmt, rs);
        }

        return null;
    }
     
    public void atualizar(Produto p) {
        Connection con = Conexao.getConexao();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(
                "UPDATE produto SET nome_produto = ?, preco = ?, quantidade = ? id_categoria = ? WHERE id_produto = ?"
            );

            stmt.setString(1, p.getNomeProduto());
            stmt.setDouble(2, p.getPreco());
            stmt.setInt(3, p.getQuantidade());
            stmt.setInt(4, p.getId());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao atualizar produto: " + ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, stmt);
        }
    }

    public void excluir(int id) {
        Connection con = Conexao.getConexao();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM produto WHERE id_produto = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao excluir produto: " + ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, stmt);
        }
    }
}