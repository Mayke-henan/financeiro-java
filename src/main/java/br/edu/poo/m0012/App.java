package br.edu.poo.m0012;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.edu.poo.m0012.config.BancoInicializador;
import br.edu.poo.m0012.view.Menu;

public class App {

    public static void main(String[] args) {
        try {
            BancoInicializador.inicializar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível preparar o banco de dados:\n" + e.getMessage()
                            + "\n\nVerifique se o MySQL/MariaDB está em execução e se"
                            + "\nDB_USER e DB_PASSWORD estão corretos no .env.");
            return;
        }
        new Menu().iniciar();
    }
}