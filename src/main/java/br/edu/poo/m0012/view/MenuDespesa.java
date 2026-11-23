package br.edu.poo.m0012.view;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.poo.m0012.dao.CategoriaDAO;
import br.edu.poo.m0012.dao.DespesaDAO;
import br.edu.poo.m0012.model.Categoria;
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
                case "2":
                    inserirDespesa();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida");
            }
        }
    }

    private void inserirDespesa() {
        String descricao = JOptionPane.showInputDialog("Descrição da despesa:");
        if (descricao == null) {
            return;
        }
        descricao = descricao.trim();
        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Descrição não pode ser vazia");
            return;
        }

        String textoValor = JOptionPane.showInputDialog("Valor da despesa:");
        if (textoValor == null) {
            return;
        }
        BigDecimal valor;
        try {
            valor = new BigDecimal(textoValor.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Digite um valor válido, por exemplo: 25,50");
            return;
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(null, "O valor deve ser maior que zero");
            return;
        }

        String textoData = JOptionPane.showInputDialog("Data da despesa (dd/mm/aaaa):");
        if (textoData == null) {
            return;
        }
        LocalDate data;
        try {
            data = LocalDate.parse(textoData.trim(), Despesa.FORMATO_BR);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Data inválida. Use o formato dd/mm/aaaa");
            return;
        }

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        try {
            List<Categoria> categorias = categoriaDAO.listar();
            if (categorias.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Cadastre uma categoria antes de inserir uma despesa");
                return;
            }

            StringBuilder sb = new StringBuilder("Categorias cadastradas:\n");
            for (Categoria categoria : categorias) {
                sb.append(categoria).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());

            String textoCategoriaId = JOptionPane.showInputDialog("ID da categoria da despesa:");
            if (textoCategoriaId == null) {
                return;
            }

            int categoriaId;
            try {
                categoriaId = Integer.parseInt(textoCategoriaId.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite um ID de categoria válido");
                return;
            }

            Categoria categoria = categoriaDAO.buscarPorId(categoriaId);
            if (categoria == null) {
                JOptionPane.showMessageDialog(null, "Categoria não encontrada");
                return;
            }

            Despesa despesa = new Despesa(descricao, valor, data, categoriaId);
            dao.inserirDespesa(despesa);
            JOptionPane.showMessageDialog(null, "Despesa cadastrada!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar o banco: " + e.getMessage());
        }
    }

}
