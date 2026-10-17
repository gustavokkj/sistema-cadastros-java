package model.bean;

import java.math.BigDecimal;

public final class Validacao {
    private Validacao() { }

    public static String texto(String valor, String campo, int limite, boolean obrigatorio) {
        String texto = valor == null ? "" : valor.trim();
        if (obrigatorio && texto.isEmpty()) throw new IllegalArgumentException("Preencha " + campo + ".");
        if (texto.length() > limite) throw new IllegalArgumentException(campo + ": máximo de " + limite + " caracteres.");
        return texto;
    }

    public static void cliente(Cliente c) {
        c.setNomeCliente(texto(c.getNomeCliente(), "o nome do cliente", 150, true));
        String email = texto(c.getEmailCliente(), "o e-mail", 150, false);
        if (!email.isEmpty() && !email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
            throw new IllegalArgumentException("Informe um e-mail válido ou deixe o campo vazio.");
        c.setEmailCliente(email);
        c.setTelefoneCliente(texto(c.getTelefoneCliente(), "o telefone", 30, false));
    }

    public static void categoria(Categoria c) {
        c.setNome_categoria(texto(c.getNome_categoria(), "o nome da categoria", 100, true));
    }

    public static BigDecimal preco(String texto) {
        String valor = texto == null ? "" : texto.trim();
        if (!valor.matches("[0-9]+([.,][0-9]{1,2})?"))
            throw new IllegalArgumentException("Informe um preço válido, como 49,90, sem separador de milhar.");
        BigDecimal preco = new BigDecimal(valor.replace(',', '.'));
        validarPreco(preco);
        return preco.setScale(2);
    }

    public static int quantidade(String texto) {
        try {
            int valor = Integer.parseInt(texto.trim());
            if (valor < 0) throw new NumberFormatException();
            return valor;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("A quantidade deve ser um número inteiro maior ou igual a zero.");
        }
    }

    public static void produto(Produto p) {
        p.setNomeProduto(texto(p.getNomeProduto(), "o nome do produto", 150, true));
        validarPreco(p.getPreco());
        p.setPreco(p.getPreco().setScale(2));
        if (p.getQuantidade() < 0) throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        if (p.getCategoria() != null && p.getCategoria().getId_categoria() <= 0)
            throw new IllegalArgumentException("Selecione uma categoria válida.");
    }

    private static void validarPreco(BigDecimal preco) {
        if (preco == null || preco.signum() < 0 || preco.compareTo(new BigDecimal("99999999.99")) > 0
                || preco.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("O preço deve estar entre 0,00 e 99.999.999,99, com até duas casas decimais.");
    }
}
