package model.bean;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidacaoTest {
    @Test void aceitaVirgulaEPontoSemPerderCentavos() {
        assertEquals(new BigDecimal("49.90"), Validacao.preco("49,90"));
        assertEquals(new BigDecimal("49.90"), Validacao.preco("49.90"));
        assertEquals(new BigDecimal("0.00"), Validacao.preco("0"));
    }

    @Test void rejeitaPrecoNegativoAmbiguoOuForaDoBanco() {
        for (String valor : new String[]{"-1", "1.000,99", "12.345", "NaN", "", "100000000", "1e3"})
            assertThrows(IllegalArgumentException.class, () -> Validacao.preco(valor), valor);
    }

    @Test void quantidadeAceitaZeroESuperiorACem() {
        assertEquals(0, Validacao.quantidade("0"));
        assertEquals(120, Validacao.quantidade("120"));
        for (String valor : new String[]{"-1", "1.5", "abc", "2147483648"})
            assertThrows(IllegalArgumentException.class, () -> Validacao.quantidade(valor));
    }

    @Test void clienteExigeNomeEValidaEmailOpcional() {
        Cliente c = new Cliente();
        c.setNomeCliente(" ");
        assertThrows(IllegalArgumentException.class, () -> Validacao.cliente(c));
        c.setNomeCliente("  Ana Lima  ");
        Validacao.cliente(c);
        assertEquals("Ana Lima", c.getNomeCliente());
        c.setEmailCliente("email-invalido");
        assertThrows(IllegalArgumentException.class, () -> Validacao.cliente(c));
        c.setEmailCliente("ana@example.com");
        Validacao.cliente(c);
    }

    @Test void categoriaRespeitaLimiteEProdutoPodeFicarSemCategoria() {
        Categoria c = new Categoria(0, "x".repeat(101));
        assertThrows(IllegalArgumentException.class, () -> Validacao.categoria(c));
        Produto p = new Produto(0, "Mouse", new BigDecimal("0.00"), 0, null);
        Validacao.produto(p);
        p.setPreco(new BigDecimal("1.001"));
        assertThrows(IllegalArgumentException.class, () -> Validacao.produto(p));
    }
}
