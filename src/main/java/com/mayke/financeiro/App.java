package com.mayke.financeiro;

import java.sql.Connection;

import com.mayke.financeiro.config.ConexaoFactory;
import com.mayke.financeiro.dao.CategoriaDAO;
import com.mayke.financeiro.model.Categoria;
import com.mayke.financeiro.model.TipoCategoria;
import com.mayke.financeiro.view.Menu;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        new Menu().iniciar();
    }
}
