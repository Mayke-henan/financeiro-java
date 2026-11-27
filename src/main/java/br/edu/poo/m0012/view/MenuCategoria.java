package br.edu.poo.m0012.view;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.poo.m0012.dao.CategoriaDAO;
import br.edu.poo.m0012.model.Categoria;
import br.edu.poo.m0012.model.TipoCategoria;

public class MenuCategoria {

    private CategoriaDAO dao = new CategoriaDAO();

    public void iniciar() {
        while (true) {
            String opcao = JOptionPane.showInputDialog(
                    "=== CATEGORIAS ===\n1 - Listar\n2 - Inserir\n3 - Atualizar\n4 - Deletar\n0 - Voltar");
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
                case "2":
                    inserirCategoria();
                    break;

                case "3":
                    atualizarCategoria();
                    break;

                case "4":
                    deletarCategoria();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida");
            }
        }
    }

    private void inserirCategoria() {
        String nome = JOptionPane.showInputDialog("Nome da categoria:");
        if (nome == null) {
            return;
        }
        nome = nome.trim();
        if (nome.isEmpty() || nome.length() > 50) {
            JOptionPane.showMessageDialog(null, "Nome inválido (de 1 a 50 caracteres)");
            return;
        }
        TipoCategoria tipo = pedirTipo();
        if (tipo == null) {
            return;
        }

        try {
            dao.inserir(new Categoria(nome, tipo));
            JOptionPane.showMessageDialog(null, "Categoria cadastrada!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar o banco: " + e.getMessage());
        }
    }

    private void deletarCategoria() {
        String texto = JOptionPane.showInputDialog("Id da categoria a deletar:");
        if (texto == null) {
            return;
        }
        try {
            int id = Integer.parseInt(texto.trim());
            Categoria c = dao.buscarPorId(id);
            if (c == null) {
                JOptionPane.showMessageDialog(null, "Categoria não encontrada");
                return;
            }
            int confirma = JOptionPane.showConfirmDialog(null,
                    "Deletar " + c + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {
                dao.deletar(id);
                JOptionPane.showMessageDialog(null, "Categoria deletada!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Digite um número válido");
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null,
                    "Essa categoria tem despesas vinculadas. Apague ou mude essas despesas antes.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar o banco: " + e.getMessage());
        }
    }

    private void atualizarCategoria() {
        String texto = JOptionPane.showInputDialog("Id da categoria a atualizar:");
        if (texto == null) {
            return;
        }
        try {
            int id = Integer.parseInt(texto.trim());
            Categoria c = dao.buscarPorId(id);
            if (c == null) {
                JOptionPane.showMessageDialog(null, "Categoria não encontrada");
                return;
            }

            String nome = JOptionPane.showInputDialog("Novo nome:", c.getNome());
            if (nome == null) {
                return;
            }
            nome = nome.trim();
            if (nome.isEmpty() || nome.length() > 50) {
                JOptionPane.showMessageDialog(null, "Nome inválido (de 1 a 50 caracteres)");
                return;
            }

            TipoCategoria tipo = pedirTipo();
            if (tipo == null) {
                return;
            }

            c.setNome(nome);
            c.setTipo(tipo);
            dao.atualizar(c);
            JOptionPane.showMessageDialog(null, "Categoria atualizada!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Digite um número válido");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar o banco: " + e.getMessage());
        }
    }

    private TipoCategoria pedirTipo() {
        TipoCategoria[] tipos = TipoCategoria.values();

        StringBuilder sb = new StringBuilder("Tipo:\n");
        for (int i = 0; i < tipos.length; i++) {
            sb.append(i + 1).append(" - ").append(tipos[i]).append("\n");
        }

        String texto = JOptionPane.showInputDialog(sb.toString());
        if (texto == null) {
            return null;
        }

        try {
            int escolha = Integer.parseInt(texto.trim());
            if (escolha >= 1 && escolha <= tipos.length) {
                return tipos[escolha - 1];
            }
        } catch (NumberFormatException e) { // não é número: cai na mensagem de tipo inválido abaixo

        }

        JOptionPane.showMessageDialog(null, "Tipo inválido");
        return null;
    }
}