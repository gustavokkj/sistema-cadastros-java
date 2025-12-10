-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema sistema_vendas
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sistema_vendas
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sistema_vendas` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `sistema_vendas` ;

-- -----------------------------------------------------
-- Table `sistema_vendas`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_vendas`.`categoria` (
  `id_categoria` INT NOT NULL AUTO_INCREMENT,
  `nome_categoria` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id_categoria`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sistema_vendas`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_vendas`.`cliente` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `nome_cliente` VARCHAR(150) NOT NULL,
  `email_cliente` VARCHAR(150) NULL DEFAULT NULL,
  `telefone_cliente` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`id_cliente`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sistema_vendas`.`produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_vendas`.`produto` (
  `id_produto` INT NOT NULL AUTO_INCREMENT,
  `nome_produto` VARCHAR(150) NOT NULL,
  `preco` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `quantidade` INT NOT NULL DEFAULT '0',
  `id_categoria` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_produto`),
  INDEX `fk_produto_categoria` (`id_categoria` ASC) VISIBLE,
  CONSTRAINT `fk_produto_categoria`
    FOREIGN KEY (`id_categoria`)
    REFERENCES `sistema_vendas`.`categoria` (`id_categoria`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sistema_vendas`.`venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_vendas`.`venda` (
  `id_venda` INT NOT NULL AUTO_INCREMENT,
  `id_cliente` INT NOT NULL,
  `date` DATE NOT NULL,
  `totalvenda` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id_venda`),
  INDEX `fk_venda_cliente` (`id_cliente` ASC) VISIBLE,
  CONSTRAINT `fk_venda_cliente`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `sistema_vendas`.`cliente` (`id_cliente`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sistema_vendas`.`produtovenda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_vendas`.`produtovenda` (
  `id_produtovenda` INT NOT NULL AUTO_INCREMENT,
  `id_venda` INT NOT NULL,
  `id_produto` INT NOT NULL,
  `quantidade` INT NOT NULL DEFAULT '1',
  `subtotal` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id_produtovenda`),
  INDEX `fk_produtovenda_venda` (`id_venda` ASC) VISIBLE,
  INDEX `fk_produtovenda_produto` (`id_produto` ASC) VISIBLE,
  CONSTRAINT `fk_produtovenda_produto`
    FOREIGN KEY (`id_produto`)
    REFERENCES `sistema_vendas`.`produto` (`id_produto`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_produtovenda_venda`
    FOREIGN KEY (`id_venda`)
    REFERENCES `sistema_vendas`.`venda` (`id_venda`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
