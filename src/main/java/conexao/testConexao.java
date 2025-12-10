package conexao;
import java.sql.Connection;

public class testConexao {
    public static void main(String[] args) {
        Connection c = Conexao.getConexao();
        if (c != null) {
            System.out.println("Conexão OK!");
            Conexao.fecharConexao(c);
        }
    }
}
