package br.edu.poo.m0012;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;

import br.edu.poo.m0012.config.ConexaoFactory;
import br.edu.poo.m0012.dao.CategoriaDAO;
import br.edu.poo.m0012.dao.DespesaDAO;
import br.edu.poo.m0012.model.Categoria;
import br.edu.poo.m0012.model.Despesa;
import br.edu.poo.m0012.model.TipoCategoria;
import br.edu.poo.m0012.view.Menu;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        // new Menu().iniciar();

        DespesaDAO dao = new DespesaDAO();
        try {
            for (Despesa d : dao.listar()) {
                System.out.println(d);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
