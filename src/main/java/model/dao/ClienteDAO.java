/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import conexao.Conexao;
import model.bean.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author gusta
 */
public class ClienteDAO {
	
	public boolean criar(Cliente c) {
		Connection con = Conexao.getConexao();
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(
				"INSERT INTO cliente (nome_cliente, email_cliente, telefone_cliente) VALUES (?, ?, ?)"
			);
			
			stmt.setString(1, c.getNomeCliente());
			stmt.setString(2, c.getEmailCliente());
			stmt.setString(3, c.getTelefoneCliente());
			
			stmt.executeUpdate();
			return true;
			
		} catch (SQLException ex) {
			System.out.println("Erro ao salvar cliente: " + ex);
			return false;
			
		} finally {
			Conexao.fecharConexao(con, stmt);
		}
	}
	
	public ArrayList<Cliente> listar() {
		Connection con = Conexao.getConexao();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		ArrayList<Cliente> lista = new ArrayList<>();
		
		try {
			stmt = con.prepareStatement("SELECT * FROM cliente");
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				Cliente cli = new Cliente();
				
				cli.setIdCliente(rs.getInt("id_cliente"));
				cli.setNomeCliente(rs.getString("nome_cliente"));
				cli.setEmailCliente(rs.getString("email_cliente"));
				cli.setTelefoneCliente(rs.getString("telefone_cliente"));
				
				lista.add(cli);
			}
			
		} catch (SQLException ex) {
			System.out.println("Erro ao listar clientes: " + ex);
			
		} finally {
			Conexao.fecharConexao(con, stmt, rs);
		}
		
		return lista;
	}
	
	public boolean atualizar(Cliente c) {
		Connection con = Conexao.getConexao();
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(
				"UPDATE cliente SET nome_cliente=?, email_cliente=?, telefone_cliente=? WHERE id_cliente=?"
			);
			
			stmt.setString(1, c.getNomeCliente());
			stmt.setString(2, c.getEmailCliente());
			stmt.setString(3, c.getTelefoneCliente());
			stmt.setInt(4, c.getIdCliente());
			
			stmt.executeUpdate();
			return true;
			
		} catch (SQLException ex) {
			System.out.println("Erro ao atualizar cliente: " + ex);
			return false;
			
		} finally {
			Conexao.fecharConexao(con, stmt);
		}
	}
	
	public boolean excluir(int idCliente) {
		Connection con = Conexao.getConexao();
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement("DELETE FROM cliente WHERE id_cliente = ?");
			stmt.setInt(1, idCliente);
			
			stmt.executeUpdate();
			return true;
			
		} catch (SQLException ex) {
			System.out.println("Erro ao excluir cliente: " + ex);
			return false;
			
		} finally {
			Conexao.fecharConexao(con, stmt);
		}
	}
}
