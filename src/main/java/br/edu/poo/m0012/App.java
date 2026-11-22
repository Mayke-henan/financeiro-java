package br.edu.poo.m0012;

import java.sql.Connection;

import br.edu.poo.m0012.config.ConexaoFactory;
import br.edu.poo.m0012.dao.CategoriaDAO;
import br.edu.poo.m0012.model.Categoria;
import br.edu.poo.m0012.model.TipoCategoria;
import br.edu.poo.m0012.view.Menu;

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
