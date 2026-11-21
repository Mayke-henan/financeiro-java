package com.mayke.financeiro;

import java.sql.Connection;

import com.mayke.financeiro.config.ConexaoFactory;
import com.mayke.financeiro.dao.CategoriaDAO;
import com.mayke.financeiro.model.Categoria;
import com.mayke.financeiro.model.TipoCategoria;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
CategoriaDAO dao = new CategoriaDAO();
try {
    for (Categoria c : dao.listar()) {
        System.out.println(c);
    }
} catch (Exception e) {
    System.out.println("Erro: " + e.getMessage());
}
    }
}
