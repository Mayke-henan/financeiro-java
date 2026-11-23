package br.edu.poo.m0012.view;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.poo.m0012.dao.DespesaDAO;
import br.edu.poo.m0012.model.Despesa;

public class MenuDespesa {

    private DespesaDAO dao = new DespesaDAO();

    public void iniciar() {
        while (true) {
            String opcao = JOptionPane.showInputDialog(
                    "=== DESPESAS ===\n1 - Listar\n2 - Inserir\n3 - Atualizar\n4 - Deletar\n0 - Voltar");
            if (opcao == null || opcao.equals("0")) {
                break;
            }

            switch (opcao) {
                case "1":
                    try {
                        List<Despesa> lista = dao.listar();
                        if (lista.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Nenhuma despesa cadastrada");
                            break;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (Despesa d : lista) {
                            sb.append(d).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString());
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Erro ao acessar o banco: " + e.getMessage());
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida");
            }
        }
    }
}
