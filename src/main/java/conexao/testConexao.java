package conexao;
import java.sql.Connection;

public class testConexao {
    public static void main(String[] args) {
        try (Connection c = Conexao.getConexao()) {
            System.out.println("Conexão OK!");
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException("Erro ao fechar conexão.", ex);
        }
    }
}
