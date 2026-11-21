package com.mayke.financeiro;

import java.sql.Connection;

import com.mayke.financeiro.config.ConexaoFactory;
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
     Categoria c = new Categoria("Alimentacao", TipoCategoria.NECESSIDADE);
System.out.println(c);
    }
}
