package br.edu.poo.m0012.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class ConexaoFactory {

    private static Dotenv carregar() {
        return Dotenv.configure().ignoreIfMissing().load();
    }

    public static String getUrl() {
        return carregar().get("DB_URL", "jdbc:mysql://localhost:3306/financeiro");
    }

    public static String getUsuario() {
        return carregar().get("DB_USER", "root");
    }

    public static String getSenha() {
        return carregar().get("DB_PASSWORD", "");
    }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUsuario(), getSenha());
    }

}