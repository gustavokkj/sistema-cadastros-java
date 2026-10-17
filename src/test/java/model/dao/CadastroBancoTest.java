package model.dao;

import conexao.Conexao;
import java.math.BigDecimal;
import java.sql.*;
import java.util.UUID;
import model.bean.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named = "DB_TESTS", matches = "true")
class CadastroBancoTest {
    private final CategoriaDAO categorias = new CategoriaDAO();
    private final ClienteDAO clientes = new ClienteDAO();
    private final ProdutoDAO produtos = new ProdutoDAO();
    private Categoria categoria;
    private Cliente cliente;
    private Produto produto;
    private int venda;

    @BeforeEach void preparar() throws Exception {
        try (Connection con = Conexao.getConexao()) {
            assertEquals("sistema_vendas_teste", con.getCatalog(), "Use exclusivamente o banco de testes.");
        }
        categoria = new Categoria(0, "Teste " + UUID.randomUUID());
        categorias.inserir(categoria);
        cliente = new Cliente();
        cliente.setNomeCliente("Cliente de teste");
        cliente.setEmailCliente("teste@example.com");
        clientes.criar(cliente);
        produto = new Produto(0, "Mouse de teste", new BigDecimal("49.90"), 10, categoria);
        produtos.salvar(produto);
    }

    @AfterEach void limpar() throws Exception {
        try (Connection con = Conexao.getConexao()) {
            assertEquals("sistema_vendas_teste", con.getCatalog());
            if (venda != 0) {
                try (PreparedStatement stmt = con.prepareStatement("DELETE FROM venda WHERE id_venda=?")) {
                    stmt.setInt(1, venda);
                    stmt.executeUpdate();
                }
            }
        }
        if (produto != null && produtos.read(produto.getId()) != null) produtos.excluir(produto.getId());
        if (cliente != null && clientes.listar().stream().anyMatch(c -> c.getIdCliente() == cliente.getIdCliente()))
            clientes.excluir(cliente.getIdCliente());
        if (categoria != null && categorias.listar().stream().anyMatch(c -> c.getId_categoria() == categoria.getId_categoria()))
            categorias.excluir(categoria.getId_categoria());
    }

    @Test void crudCompletoEPersistenciaEmNovasConexoes() {
        assertTrue(categoria.getId_categoria() > 0);
        assertEquals(categoria.getNome_categoria(), produtos.read(produto.getId()).getCategoria().getNome_categoria());
        categoria.setNome_categoria("Categoria atualizada");
        categorias.atualizar(categoria);
        assertEquals("Categoria atualizada", produtos.read(produto.getId()).getCategoria().getNome_categoria());
        cliente.setNomeCliente("Cliente atualizado");
        clientes.atualizar(cliente);
        assertTrue(clientes.listar().stream().anyMatch(c -> c.getIdCliente() == cliente.getIdCliente()
                && c.getNomeCliente().equals("Cliente atualizado")));
        produto.setNomeProduto("Produto atualizado");
        produto.setPreco(new BigDecimal("19.99"));
        produto.setQuantidade(120);
        produto.setCategoria(null);
        produtos.atualizar(produto);
        Produto salvo = produtos.read(produto.getId());
        assertEquals(new BigDecimal("19.99"), salvo.getPreco());
        assertEquals(120, salvo.getQuantidade());
        assertNull(salvo.getCategoria());
        assertTrue(produtos.listarTodos().stream().anyMatch(p -> p.getId() == produto.getId()));
        produtos.excluir(produto.getId());
        assertNull(produtos.read(produto.getId()));
        clientes.excluir(cliente.getIdCliente());
        categorias.excluir(categoria.getId_categoria());
    }

    @Test void excluirCategoriaPreservaProdutoSemCategoria() {
        categorias.excluir(categoria.getId_categoria());
        assertNull(produtos.read(produto.getId()).getCategoria());
        assertTrue(produtos.listarTodos().stream().anyMatch(p -> p.getId() == produto.getId()));
    }

    @Test void vinculosComVendasImpedemExclusao() throws Exception {
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(
                     "INSERT INTO venda (id_cliente, date, totalvenda) VALUES (?, CURRENT_DATE, 49.90)",
                     Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cliente.getIdCliente());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) { keys.next(); venda = keys.getInt(1); }
            try (PreparedStatement item = con.prepareStatement(
                    "INSERT INTO produtovenda (id_venda,id_produto,quantidade,subtotal) VALUES (?,?,1,49.90)")) {
                item.setInt(1, venda);
                item.setInt(2, produto.getId());
                item.executeUpdate();
            }
        }
        assertTrue(assertThrows(IllegalStateException.class, () -> clientes.excluir(cliente.getIdCliente()))
                .getMessage().contains("vinculado"));
        assertThrows(IllegalStateException.class, () -> produtos.excluir(produto.getId()));
        assertNotNull(produtos.read(produto.getId()));
    }
}
