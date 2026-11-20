package com.mayke.financeiro;

import java.sql.Connection;

import com.mayke.financeiro.config.ConexaoFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       try (Connection con = ConexaoFactory.getConexao()) {
    System.out.println("Conectado!");
} catch (Exception e) {
    System.out.println("Erro: " + e.getMessage());
}
    }
}
