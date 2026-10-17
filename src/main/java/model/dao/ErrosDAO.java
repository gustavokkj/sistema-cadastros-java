package model.dao;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

final class ErrosDAO {
    private ErrosDAO() { }

    static IllegalStateException banco(String operacao, SQLException ex) {
        Logger.getLogger(ErrosDAO.class.getName()).log(Level.WARNING, operacao, ex);
        String mensagem = ex.getErrorCode() == 1451
                ? "Este registro está vinculado a uma venda e não pode ser excluído."
                : "Não foi possível " + operacao + ". Confira a estrutura e a conexão do banco.";
        return new IllegalStateException(mensagem, ex);
    }

    static void conferir(int linhas) {
        if (linhas == 0) throw new IllegalStateException("O registro não foi encontrado. Atualize a listagem.");
    }
}
