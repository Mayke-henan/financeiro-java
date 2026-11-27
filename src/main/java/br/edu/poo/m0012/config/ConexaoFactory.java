package br.edu.poo.m0012.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class ConexaoFactory {

    public static Connection getConexao() throws SQLException {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String url = dotenv.get("DB_URL", "jdbc:mysql://localhost:3306/financeiro");
        String user = dotenv.get("DB_USER", "root");
        String password = dotenv.get("DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }

}