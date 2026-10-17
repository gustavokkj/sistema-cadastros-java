-- Dados inteiramente fictícios para demonstração local.
-- Importe depois de bdvendas.sql, no banco sistema_vendas.
-- Não apaga nem sobrescreve registros. Reimportar não duplica estes exemplos.
USE sistema_vendas;
SET NAMES utf8mb4;

INSERT INTO categoria (nome_categoria)
SELECT 'Periféricos' WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome_categoria='Periféricos');
INSERT INTO categoria (nome_categoria)
SELECT 'Informática' WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome_categoria='Informática');
INSERT INTO categoria (nome_categoria)
SELECT 'Escritório' WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome_categoria='Escritório');

INSERT INTO cliente (nome_cliente, email_cliente, telefone_cliente)
SELECT 'Ana Lima', 'ana@example.com', '(00) 90000-0001'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE email_cliente='ana@example.com');
INSERT INTO cliente (nome_cliente, email_cliente, telefone_cliente)
SELECT 'Bruno Costa', 'bruno@example.com', '(00) 90000-0002'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE email_cliente='bruno@example.com');
INSERT INTO cliente (nome_cliente, email_cliente, telefone_cliente)
SELECT 'Carla Souza', 'carla@example.com', '(00) 90000-0003'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE email_cliente='carla@example.com');

INSERT INTO produto (nome_produto, preco, quantidade, id_categoria)
SELECT 'Mouse USB', 49.90, 24, (SELECT MIN(id_categoria) FROM categoria WHERE nome_categoria='Periféricos')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome_produto='Mouse USB');
INSERT INTO produto (nome_produto, preco, quantidade, id_categoria)
SELECT 'Teclado ABNT2', 89.90, 15, (SELECT MIN(id_categoria) FROM categoria WHERE nome_categoria='Periféricos')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome_produto='Teclado ABNT2');
INSERT INTO produto (nome_produto, preco, quantidade, id_categoria)
SELECT 'Monitor 24 polegadas', 799.90, 6, (SELECT MIN(id_categoria) FROM categoria WHERE nome_categoria='Informática')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome_produto='Monitor 24 polegadas');
INSERT INTO produto (nome_produto, preco, quantidade, id_categoria)
SELECT 'Headset USB', 159.90, 10, (SELECT MIN(id_categoria) FROM categoria WHERE nome_categoria='Periféricos')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome_produto='Headset USB');
INSERT INTO produto (nome_produto, preco, quantidade, id_categoria)
SELECT 'Suporte para notebook', 69.90, 12, (SELECT MIN(id_categoria) FROM categoria WHERE nome_categoria='Escritório')
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE nome_produto='Suporte para notebook');
