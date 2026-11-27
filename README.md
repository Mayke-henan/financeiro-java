# Financeiro

Aplicação Java com interface em janelas (JOptionPane) para organizar despesas
pessoais por categoria. Projeto do **Desafio Extra – CRUD com Integração**.

## Funcionalidades

- **Categorias:** listar, inserir, atualizar e deletar. Cada categoria tem um
  tipo: necessidade, desejo ou poupança.
- **Despesas:** listar, inserir, atualizar e deletar (descrição, valor, data e
  categoria).
- **Relacionamento 1:N:** cada despesa pertence a uma categoria (chave
  estrangeira no banco de dados).
- **API externa:** consulta a cotação do dólar na
  [AwesomeAPI](https://docs.awesomeapi.com.br/) e mostra o valor de uma despesa
  em dólar.
- **Validação dos dados:** campos obrigatórios, valor numérico maior que zero,
  data no formato `dd/mm/aaaa`, tamanho máximo dos campos e categoria existente.

## Tecnologias

- Java 21 e Maven
- MySQL ou MariaDB (acesso por JDBC, com `mysql-connector-j`)
- `dotenv-java` para ler o arquivo `.env`
- `Gson` para ler o JSON da API
- `HttpClient` (`java.net.http`) para consumir a API
- `JOptionPane` (Swing) para a interface

## Requisitos

- JDK 21
- Maven 3.x
- MySQL 8 ou MariaDB em execução, por padrão em `localhost:3306`
- Usuário `root` **sem senha** (configuração padrão do projeto; veja
  [Problemas comuns](#problemas-comuns) se o seu `root` tiver senha)
- Internet (somente para a opção de cotação do dólar)
- Ambiente gráfico (o programa abre janelas)

## Como executar

1. **Clone o repositório e entre na pasta:**

   ```bash
   git clone https://github.com/Mayke-henan/financeiro-java.git
   cd financeiro-java
   ```

2. **Crie o banco e as tabelas** com o `schema.sql`. O script cria o banco
   `financeiro` e usa o usuário `root` sem senha:

   ```bash
   mysql -u root < schema.sql
   ```

   No MariaDB mais recente o comando `mysql` foi renomeado para `mariadb`; se
   aparecer um aviso de "Deprecated program name", é só isso, e o comando
   funciona normalmente (ou use `mariadb -u root < schema.sql`).

3. **Crie o arquivo `.env`:** o `.env.example` já vem pronto, configurado para o
   `root` sem senha. Basta copiar:

   ```bash
   cp .env.example .env
   ```

   | Variável          | Descrição                                                  |
   |-------------------|------------------------------------------------------------|
   | `DB_URL`          | URL JDBC do banco (`jdbc:mysql://localhost:3306/financeiro`) |
   | `DB_USER`         | usuário do banco (`root`)                                  |
   | `DB_PASSWORD`     | senha do banco (vazia por padrão)                          |
   | `API_COTACAO_URL` | endereço da API de cotação do dólar                        |

   O arquivo `.env` não vai para o repositório (está no `.gitignore`). Só é
   preciso editá-lo se o seu banco usar outro usuário, senha ou porta.

4. **Compile e execute:**

   ```bash
   mvn compile exec:java -Dexec.mainClass="br.edu.poo.m0012.App"
   ```

   Também é possível abrir o projeto em uma IDE (Eclipse, IntelliJ ou VS Code) e
   executar a classe `br.edu.poo.m0012.App`.

## Como usar

O menu principal abre em uma janela:

```
=== FINANCEIRO ===
1 - Categorias
2 - Despesas
0 - Sair
```

Cada submenu tem as opções **Listar, Inserir, Atualizar e Deletar**, e `0` volta
ao menu anterior. No menu de despesas há ainda a opção **5 - Ver despesa em
dólar**, que escolhe uma despesa e mostra o valor convertido pela cotação atual.

Ao inserir uma despesa, o programa mostra a lista de categorias para você
escolher o ID. Clicar em **Cancelar** em qualquer janela volta ao menu sem
alterar nada.

## Banco de dados

O arquivo `schema.sql` cria o banco `financeiro` e duas tabelas:

- `categoria` (`id`, `nome`, `tipo`)
- `despesa` (`id`, `descricao`, `valor`, `data_despesa`, `categoria_id`), em que
  `categoria_id` é chave estrangeira para `categoria(id)`

O script usa `IF NOT EXISTS`, então pode ser executado mais de uma vez sem
apagar dados. Para **recomeçar do zero**:

```bash
mysql -u root -e "DROP DATABASE IF EXISTS financeiro;"
mysql -u root < schema.sql
```

A conexão é aberta em cada operação com `try-with-resources`, o que fecha
`Connection`, `PreparedStatement` e `ResultSet` automaticamente.

## Estrutura do projeto

```
src/main/java/br/edu/poo/m0012/
├── App.java                  # ponto de entrada
├── config/
│   └── ConexaoFactory.java   # lê o .env e abre a conexão com o banco
├── dao/
│   ├── Crud.java             # interface genérica com as 5 operações
│   ├── CategoriaDAO.java
│   └── DespesaDAO.java
├── model/
│   ├── EntidadeBase.java     # classe abstrata (id e getResumo())
│   ├── Categoria.java
│   ├── Despesa.java
│   └── TipoCategoria.java    # enum
├── service/
│   └── CotacaoService.java   # consumo da API externa
└── view/
    ├── Menu.java             # menu principal
    ├── MenuCategoria.java
    └── MenuDespesa.java
```

## Validações

- Nome da categoria e descrição da despesa: obrigatórios (descrição com no
  máximo 100 caracteres).
- Valor: número válido (aceita vírgula), maior que zero e até `99999999,99`.
- Data: formato `dd/mm/aaaa`, recusando datas que não existem (como 31/02).
- Categoria da despesa: precisa existir no banco.
- Tipo da categoria: apenas uma das opções do menu.
- Exclusões pedem confirmação, e erros do banco são mostrados em janelas.

## Conceitos de POO aplicados

| Conceito | Onde está |
|----------|-----------|
| Classes, objetos e construtores | `Categoria`, `Despesa` |
| Atributos privados e encapsulamento | getters e setters nas classes do `model` |
| Herança | `Categoria` e `Despesa` estendem `EntidadeBase` |
| Classe abstrata | `EntidadeBase` (método abstrato `getResumo()`) |
| Polimorfismo | cada filha implementa `getResumo()`, e o `toString()` da classe mãe usa o método da filha |
| Interface | `Crud<T>`, implementada por `CategoriaDAO` e `DespesaDAO` |
| Arrays | `TipoCategoria.values()` monta o menu de tipos em `MenuCategoria` |
| Enum | `TipoCategoria` |
| `final` | `DespesaDAO` em `MenuDespesa`, `FORMATO_BR` em `Despesa` |
| Coleções | `List` e `ArrayList` nas listagens |
| Laços e condicionais | `while`, `for`, `if` e `switch` nos menus |
| Retorno de função | métodos `pedir...` que devolvem o dado digitado, ou `null` ao cancelar |
| Tratamento de exceções | `try/catch`, `throws` e `try-with-resources` (`SQLException`, `NumberFormatException`, `DateTimeParseException`, `IOException`) |
| Entrada de dados | `JOptionPane` |

## Padrões de projeto

- **DAO:** cada tabela tem uma classe responsável pelo acesso ao banco.
- **Factory:** `ConexaoFactory` cria as conexões.
- **Camadas:** `model`, `dao`, `service` e `view` separados por responsabilidade.

## Problemas comuns

| Mensagem | Causa e solução |
|----------|-----------------|
| `Communications link failure` | O MySQL/MariaDB não está em execução ou a porta do `DB_URL` está errada. |
| `Access denied for user 'root'` | O `root` do seu banco tem senha ou usa autenticação por usuário do sistema (`unix_socket`). Para deixá-lo sem senha, rode `sudo mysql` e execute `ALTER USER 'root'@'localhost' IDENTIFIED BY ''; FLUSH PRIVILEGES;`. Se preferir manter a senha, coloque-a em `DB_PASSWORD` no `.env`. |
| `Unknown database 'financeiro'` | O `schema.sql` ainda não foi executado. |
| `API_COTACAO_URL não encontrada no .env` | Falta copiar o `.env.example` para `.env`. |
| `HeadlessException` | O programa precisa de ambiente gráfico para abrir as janelas. |
| Erro ao consultar a cotação | Sem internet ou API indisponível; o restante do sistema continua funcionando. |
| Erro ao deletar uma categoria | Ela tem despesas vinculadas. Apague ou mude a categoria dessas despesas antes. |

## Autor

Mayke Henan de Albuquerque — [@Mayke-henan](https://github.com/Mayke-henan)
