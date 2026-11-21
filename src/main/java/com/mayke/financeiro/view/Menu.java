package com.mayke.financeiro.view;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import com.mayke.financeiro.dao.CategoriaDAO;
import com.mayke.financeiro.model.Categoria;

public class Menu {

    private CategoriaDAO dao = new CategoriaDAO();

    public void iniciar() {
        while (true) {
            String opcao = JOptionPane.showInputDialog(
                    "=== CATEGORIAS ===\n1 - Listar\n0 - Sair");

            if (opcao == null || opcao.equals("0")) {
                break;
            }

            switch (opcao) {
                case "1":
                    try {
                        List<Categoria> lista = dao.listar();
                        if (lista.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Nenhuma categoria cadastrada");
                            break;
                        }
                        StringBuilder sb = new StringBuilder();
                        for (Categoria c : lista) {
                            sb.append(c).append("\n");
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