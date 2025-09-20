# Estrutura do Banco de Dados


# Estrutura do Banco de Dados

O banco de dados relacional é projetado para ser normalizado e garantir integridade referencial.

## Principais Entidades

### Entidades Base

#### Pessoa
Entidade central que armazena dados comuns:
- **id:** Identificador único
- **nome:** Nome completo
- **telefone:** Contato telefônico
- **endereco:** Relacionamento com endereço

#### Endereco
Informações de localização:
- **logradouro, numero, complemento**
- **bairro, cep**
- **cidade_id:** FK para Cidade

### Entidades de Negócio

#### Cliente
Especialização de Pessoa para clientes:
- Relacionamento com OrdemServico
- Relacionamento com Projeto
- Relacionamento com Venda

#### Produto
Catálogo de produtos:
- **nome, precoCusto, precoVenda**
- **estoque, unidadeDeMedida**
- **grupo_id:** FK para Grupo
- **fornecedor_id:** FK para Fornecedor

#### OrdemServico
Gestão de trabalhos:
- **numero:** Identificador único
- **status:** Enum de status
- **dataInicio, dataConclusao**
- **cliente_id, projeto_id**

## Relacionamentos Principais

### Um para Um (1:1)
- Pessoa ↔ Endereco
- Projeto ↔ OrdemServico

### Um para Muitos (1:N)
- Cliente → OrdemServico
- Cliente → Projeto
- Fornecedor → Produto
- Venda → ItemVenda

### Muitos para Muitos (N:N)
- OrdemServico ↔ Produto (via ItemOrdemServico)
- OrdemServico ↔ Servico (via ItemOrdemServico)

## Scripts de Criação

### Tabela Pessoa
```sql
CREATE TABLE pessoa (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Tabela Cliente
```sql
CREATE TABLE cliente (
    cli_id SERIAL PRIMARY KEY,
    pessoa_id INTEGER NOT NULL,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
        ON DELETE CASCADE
);
```

### Tabela Produto
```sql
CREATE TABLE produto (
    prod_id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    preco_custo DECIMAL(10,2) DEFAULT 0,
    preco_venda DECIMAL(10,2) DEFAULT 0,
    estoque DECIMAL(10,3) DEFAULT 0,
    unidade_de_medida VARCHAR(10),
    ativo BOOLEAN DEFAULT true,
    grupo_id INTEGER,
    fornecedor_id INTEGER,
    FOREIGN KEY (grupo_id) REFERENCES grupo(id),
    FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id)
);
```

## Índices e Performance

### Índices Recomendados
```sql
-- Índices para busca por nome
CREATE INDEX idx_pessoa_nome ON pessoa(nome);
CREATE INDEX idx_produto_nome ON produto(nome);

-- Índices para relacionamentos
CREATE INDEX idx_cliente_pessoa ON cliente(pessoa_id);
CREATE INDEX idx_produto_grupo ON produto(grupo_id);
CREATE INDEX idx_produto_fornecedor ON produto(fornecedor_id);

-- Índices para datas
CREATE INDEX idx_venda_data ON venda(data_venda);
CREATE INDEX idx_compra_data ON compra(data_compra);
```

## Constraints e Validações

### Check Constraints
```sql
-- Validação de preços
ALTER TABLE produto ADD CONSTRAINT chk_preco_positivo 
    CHECK (preco_custo >= 0 AND preco_venda >= 0);

-- Validação de estoque
ALTER TABLE produto ADD CONSTRAINT chk_estoque_positivo 
    CHECK (estoque >= 0);

-- Validação de datas
ALTER TABLE ordem_servico ADD CONSTRAINT chk_datas_validas 
    CHECK (data_inicio <= data_conclusao);
```

## Triggers e Procedures

### Trigger para Atualização de Estoque
```sql
CREATE OR REPLACE FUNCTION atualizar_estoque()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE produto 
        SET estoque = estoque - NEW.quantidade
        WHERE prod_id = NEW.produto_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_estoque
    AFTER INSERT ON item_venda
    FOR EACH ROW
    EXECUTE FUNCTION atualizar_estoque();
```

## Backup e Manutenção

### Script de Backup
```bash
#!/bin/bash
pg_dump -h localhost -U usuario -d os_marmoraria > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Manutenção Regular
```sql
-- Análise de estatísticas
ANALYZE;

-- Reindexação
REINDEX DATABASE os_marmoraria;
```
                