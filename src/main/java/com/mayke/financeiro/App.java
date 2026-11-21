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
    Categoria c = dao.buscarPorId(1);
    c.setNome("Mercado");
    dao.atualizar(c);
    System.out.println(dao.buscarPorId(1));
} catch (Exception e) {
    System.out.println("Erro: " + e.getMessage());
}
    }
}
