
package br.edu.poo.m0012.view;

import javax.swing.JOptionPane;

public class Menu {

    public void iniciar() {
        while (true) {
            String opcao = JOptionPane.showInputDialog(
                    "=== FINANCEIRO ===\n1 - Categorias\n2 - Despesas\n0 - Sair");

            if (opcao == null || opcao.equals("0")) {
                break;
            }

            switch (opcao) {
                case "1":
                    new MenuCategoria().iniciar();
                    break;
                case "2":
                    new MenuDespesa().iniciar();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida");
            }
        }
    }
}
