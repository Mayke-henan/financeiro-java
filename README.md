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
- Maven 3.x (dispensável se você rodar pelo IntelliJ, que já traz o Maven)
- MySQL 8 ou MariaDB em execução, por padrão em `localhost:3306`
- Usuário `root` **sem senha** (configuração padrão do projeto; veja
  [Problemas comuns](#problemas-comuns) se o seu `root` tiver senha)
- Internet (somente para a opção de cotação do dólar)
- Ambiente gráfico (o programa abre janelas)

## Como executar

Funciona em **Linux, macOS e Windows**. Os passos são os mesmos; só mudam o
comando para rodar o `schema.sql` no Windows (PowerShell) e o jeito de executar
o programa. A forma mais simples, em qualquer sistema, é abrir o projeto no
**IntelliJ IDEA** (veja o passo 4).

1. **Clone o repositório e entre na pasta:**

   ```bash
   git clone https://github.com/Mayke-henan/financeiro-java.git
   cd financeiro-java
   ```

2. **Crie o banco e as tabelas** com o `schema.sql`. O script cria o banco
   `financeiro` e usa o usuário `root` sem senha.

   **Linux e macOS (Terminal):**

   ```bash
   mysql -u root < schema.sql
   ```

   **Windows (PowerShell):** o PowerShell não aceita o `<`, então use um destes:

   ```powershell
   cmd /c "mysql -u root < schema.sql"
   ```

   ```powershell
   Get-Content schema.sql | mysql -u root
   ```

   Se o terminal disser que `mysql` não é reconhecido, abra o `schema.sql` no
   **MySQL Workbench** (ou em outra ferramenta do seu banco) e execute o script
   inteiro por lá.

   **Se o seu `root` tem senha** (comum quando o MySQL é instalado pelo
   instalador oficial no Windows ou no macOS), acrescente `-p`, digite a senha
   quando pedir e coloque a mesma senha em `DB_PASSWORD` no `.env` (passo 3):

   ```bash
   mysql -u root -p < schema.sql
   ```

   No macOS, o MySQL instalado com Homebrew (`brew install mysql` e
   `brew services start mysql`) vem com o `root` sem senha.

3. **Crie o arquivo `.env` (opcional):** o `.env.example` já vem pronto, com o
   `root` sem senha. Se o arquivo `.env` não existir, o programa usa esses
   mesmos valores por padrão. Copie para criar o seu:

   ```bash
   cp .env.example .env
   ```

   No Windows, o mesmo comando funciona no PowerShell; no Prompt de Comando
   (cmd) use `copy .env.example .env`.

   | Variável          | Descrição                                                  |
   |-------------------|------------------------------------------------------------|
   | `DB_URL`          | URL JDBC do banco (`jdbc:mysql://localhost:3306/financeiro`) |
   | `DB_USER`         | usuário do banco (`root`)                                  |
   | `DB_PASSWORD`     | senha do banco (vazia por padrão)                          |
   | `API_COTACAO_URL` | endereço da API de cotação do dólar                        |

   O arquivo `.env` não vai para o repositório (está no `.gitignore`). Só é
   preciso editá-lo se o seu banco usar outro usuário, senha ou porta.

4. **Execute o programa:**

   **Pelo IntelliJ IDEA (recomendado, em Linux, macOS e Windows):**

   1. Em **File → Open**, escolha a pasta `financeiro-java` (ou o arquivo
      `pom.xml`) e clique em **Trust Project**.
   2. Aguarde o Maven baixar as dependências (barra de progresso no canto
      inferior direito).
   3. Confira o JDK em **File → Project Structure → Project**: use o **21**. Se
      não houver nenhum, escolha **Add SDK → Download JDK** e baixe o 21.
   4. Abra `src/main/java/br/edu/poo/m0012/App.java` e clique na seta verde ao
      lado do `main` → **Run 'App.main()'**.

   Não é preciso ter o Maven instalado para rodar assim.

   **Pelo terminal** (precisa do Maven 3.x e do JDK 21 no PATH):

   ```bash
   mvn compile exec:java -Dexec.mainClass="br.edu.poo.m0012.App"
   ```

   No macOS, dá para instalar o Maven com `brew install maven`. Também funciona
   em outras IDEs (Eclipse ou VS Code): abra o projeto e execute a classe
   `br.edu.poo.m0012.App`.

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
| `Access denied for user 'root'` | O `root` do seu banco tem senha ou usa autenticação por usuário do sistema (`unix_socket`). Para deixá-lo sem senha, rode `sudo mysql` e execute `ALTER USER 'root'@'localhost' IDENTIFIED BY ''; FLUSH PRIVILEGES;`. Se preferir manter a senha (o instalador oficial do MySQL no Windows e no macOS costuma exigir uma), coloque-a em `DB_PASSWORD` no `.env` e use `-p` ao rodar o `schema.sql`. |
| `Operador '<' reservado para uso futuro` (PowerShell) | O PowerShell não aceita `<`. Use `cmd /c "mysql -u root < schema.sql"` ou `Get-Content schema.sql \| mysql -u root`. |
| `mvn` ou `mysql` não é reconhecido como comando | O programa não está instalado ou não está no PATH. Execute o projeto pelo IntelliJ (que traz o Maven) e rode o `schema.sql` pelo MySQL Workbench. |
| `Unknown database 'financeiro'` | O `schema.sql` ainda não foi executado. |
| `HeadlessException` | O programa precisa de ambiente gráfico para abrir as janelas. |
| Erro ao consultar a cotação | Sem internet ou API indisponível; o restante do sistema continua funcionando. |
| Erro ao deletar uma categoria | Ela tem despesas vinculadas. Apague ou mude a categoria dessas despesas antes. |

## Autor

Mayke Henan de Albuquerque — [@Mayke-henan](https://github.com/Mayke-henan)
