package br.edu.poo.m0012.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Prepara o banco de dados na primeira execução: cria o banco e as tabelas
 * quando ainda não existem. Equivale ao schema.sql e pode rodar quantas vezes
 * for preciso, pois não apaga nem duplica dados.
 */
public class BancoInicializador {

    private static final String PREFIXO = "jdbc:mysql://";

    public static void inicializar() throws SQLException {
        criarBancoSeNaoExistir();
        criarTabelasSeNaoExistirem();
    }

    private static void criarBancoSeNaoExistir() {
        String url = ConexaoFactory.getUrl();
        String nomeBanco = extrairNomeBanco(url);
        if (nomeBanco == null) {
            return;
        }

        String urlServidor = removerNomeBanco(url);
        try (Connection con = DriverManager.getConnection(
                urlServidor, ConexaoFactory.getUsuario(), ConexaoFactory.getSenha());
                Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + nomeBanco + "` CHARACTER SET utf8mb4");
        } catch (SQLException e) {
            // Sem permissão para criar bancos (ou o banco já existe): segue em frente.
            // Se o banco realmente não existir, o passo seguinte mostra o erro.
        }
    }

    private static void criarTabelasSeNaoExistirem() throws SQLException {
        try (Connection con = ConexaoFactory.getConexao();
                Statement st = con.createStatement()) {

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS categoria (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(50) NOT NULL,
                        tipo ENUM('NECESSIDADE', 'DESEJO', 'POUPANCA') NOT NULL
                    )
                    """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS despesa (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        descricao VARCHAR(100) NOT NULL,
                        valor DECIMAL(10,2) NOT NULL,
                        data_despesa DATE NOT NULL,
                        categoria_id INT NOT NULL,
                        FOREIGN KEY (categoria_id) REFERENCES categoria(id)
                    )
                    """);
        }
    }

    /** Devolve o nome do banco da URL JDBC, ou null se não houver um nome válido. */
    static String extrairNomeBanco(String url) {
        if (!url.startsWith(PREFIXO)) {
            return null;
        }
        int inicioCaminho = url.indexOf('/', PREFIXO.length());
        if (inicioCaminho < 0) {
            return null;
        }
        int inicioParametros = url.indexOf('?', inicioCaminho);
        String nome = inicioParametros < 0
                ? url.substring(inicioCaminho + 1)
                : url.substring(inicioCaminho + 1, inicioParametros);
        return nome.matches("\\w+") ? nome : null;
    }

    /** Devolve a URL do servidor, sem o nome do banco (mantém os parâmetros). */
    static String removerNomeBanco(String url) {
        int inicioCaminho = url.indexOf('/', PREFIXO.length());
        int inicioParametros = url.indexOf('?', inicioCaminho);
        String parametros = inicioParametros < 0 ? "" : url.substring(inicioParametros);
        return url.substring(0, inicioCaminho + 1) + parametros;
    }
}