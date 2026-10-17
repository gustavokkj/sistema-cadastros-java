package conexao;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Conexao {
    private Conexao() { }

    public static Connection getConexao() {
        Properties local = new Properties();
        Path arquivo = Path.of("config", "database.properties");
        if (Files.exists(arquivo)) {
            try (Reader reader = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
                local.load(reader);
            } catch (IOException ex) {
                throw new IllegalStateException("Não foi possível ler config/database.properties.", ex);
            }
        }
        String url = valor("DB_URL", "db.url", local,
                "jdbc:mariadb://127.0.0.1:3306/sistema_vendas?connectTimeout=3000&socketTimeout=5000");
        try {
            return DriverManager.getConnection(url, valor("DB_USER", "db.user", local, "root"),
                    valor("DB_PASSWORD", "db.password", local, ""));
        } catch (SQLException ex) {
            throw new IllegalStateException("Não foi possível conectar ao banco. Confira o serviço MySQL/MariaDB, "
                    + "a importação de bdvendas.sql e config/database.properties.", ex);
        }
    }

    private static String valor(String ambiente, String chave, Properties local, String padrao) {
        String valor = System.getenv(ambiente);
        return valor != null ? valor : local.getProperty(chave, padrao);
    }
}
