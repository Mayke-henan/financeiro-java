package br.edu.poo.m0012.view;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.poo.m0012.dao.CategoriaDAO;
import br.edu.poo.m0012.dao.DespesaDAO;
import br.edu.poo.m0012.model.Categoria;
import br.edu.poo.m0012.model.Despesa;
import br.edu.poo.m0012.service.CotacaoService;

public class MenuDespesa {

    private final DespesaDAO dao = new DespesaDAO();

    public void iniciar() {
        while (true) {
            String opcao = JOptionPane.showInputDialog(
                    "=== DESPESAS ===\n"
                            + "1 - Listar\n"
                            + "2 - Inserir\n"
                            + "3 - Atualizar\n"
                            + "4 - Deletar\n"
                            + "5 - Ver despesa em dólar\n"
                            + "0 - Voltar");

            if (opcao == null || opcao.equals("0")) {
                break;
            }

            switch (opcao) {
                case "1":
                    listarDespesas();
                    break;
                case "2":
                    inserirDespesa();
                    break;
                case "3":
                    atualizarDespesa();
                    break;
                case "4":
                    deletarDespesa();
                    break;
                case "5":
                    verDespesaEmDolar();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida");
            }
        }
    }

    private void listarDespesas() {
        try {
            List<Despesa> lista = dao.listar();

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhuma despesa cadastrada");
                return;
            }

            JOptionPane.showMessageDialog(null, montarLista(lista));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null, "Erro ao acessar o banco: " + e.getMessage());
        }
    }

    private void inserirDespesa() {
        String descricao = pedirDescricao();
        if (descricao == null) {
            return;
        }

        BigDecimal valor = pedirValor();
        if (valor == null) {
            return;
        }

        LocalDate data = pedirData();
        if (data == null) {
            return;
        }

        Integer categoriaId = pedirCategoriaId();
        if (categoriaId == null) {
            return;
        }

        Despesa despesa = new Despesa(descricao, valor, data, categoriaId);

        try {
            dao.inserir(despesa);
            JOptionPane.showMessageDialog(null, "Despesa cadastrada!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null, "Erro ao cadastrar despesa: " + e.getMessage());
        }
    }

    private void atualizarDespesa() {
        Despesa existente = selecionarDespesa();
        if (existente == null) {
            return;
        }

        String descricao = pedirDescricao();
        if (descricao == null) {
            return;
        }

        BigDecimal valor = pedirValor();
        if (valor == null) {
            return;
        }

        LocalDate data = pedirData();
        if (data == null) {
            return;
        }

        Integer categoriaId = pedirCategoriaId();
        if (categoriaId == null) {
            return;
        }

        Despesa atualizada = new Despesa(descricao, valor, data, categoriaId);
        atualizada.setId(existente.getId());

        try {
            dao.atualizar(atualizada);
            JOptionPane.showMessageDialog(null, "Despesa atualizada!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null, "Erro ao atualizar despesa: " + e.getMessage());
        }
    }

    private void deletarDespesa() {
        Despesa despesa = selecionarDespesa();
        if (despesa == null) {
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Deseja realmente deletar esta despesa?\n" + despesa,
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            dao.deletar(despesa.getId());
            JOptionPane.showMessageDialog(null, "Despesa deletada!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null, "Erro ao deletar despesa: " + e.getMessage());
        }
    }

    private Despesa selecionarDespesa() {
        try {
            List<Despesa> lista = dao.listar();

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhuma despesa cadastrada");
                return null;
            }

            JOptionPane.showMessageDialog(null, montarLista(lista));

            String textoId = JOptionPane.showInputDialog("Digite o ID da despesa:");
            if (textoId == null) {
                return null;
            }

            int id;
            try {
                id = Integer.parseInt(textoId.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite um ID válido");
                return null;
            }

            Despesa despesa = dao.buscarPorId(id);
            if (despesa == null) {
                JOptionPane.showMessageDialog(null, "Despesa não encontrada");
            }
            return despesa;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null, "Erro ao acessar o banco: " + e.getMessage());
            return null;
        }
    }

    private String montarLista(List<Despesa> lista) {
        StringBuilder sb = new StringBuilder("Despesas cadastradas:\n");
        for (Despesa despesa : lista) {
            sb.append(despesa).append("\n");
        }
        return sb.toString();
    }

    private String pedirDescricao() {
        while (true) {
            String descricao = JOptionPane.showInputDialog("Descrição da despesa:");

            if (descricao == null) {
                return null;
            }

            descricao = descricao.trim();
            if (descricao.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Descrição não pode ser vazia");
                continue;
            }
            if (descricao.length() > 100) {
                JOptionPane.showMessageDialog(null, "Descrição muito longa (máximo 100 caracteres)");
                continue;
            }
            return descricao;
        }
    }

    private BigDecimal pedirValor() {
        while (true) {
            String texto = JOptionPane.showInputDialog("Valor da despesa:");

            if (texto == null) {
                return null;
            }

            String normalizado = texto.trim().replace(",", ".");

            if (!normalizado.matches("\\d+(\\.\\d+)?")) {
                JOptionPane.showMessageDialog(
                        null, "Digite um valor válido, por exemplo: 25,50");
                continue;
            }

            BigDecimal valor = new BigDecimal(normalizado);

            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(null, "O valor deve ser maior que zero");
                continue;
            }
            if (valor.compareTo(new BigDecimal("99999999.99")) > 0) {
                JOptionPane.showMessageDialog(null, "Valor muito alto (máximo 99999999,99)");
                continue;
            }
            return valor.setScale(2, RoundingMode.HALF_UP);
        }
    }

    private LocalDate pedirData() {
        while (true) {
            String texto = JOptionPane.showInputDialog(
                    "Data da despesa (dd/mm/aaaa):");

            if (texto == null) {
                return null;
            }

            try {
                return LocalDate.parse(texto.trim(), Despesa.FORMATO_BR);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(
                        null, "Data inválida. Use o formato dd/mm/aaaa");
            }
        }
    }

    private Integer pedirCategoriaId() {
        CategoriaDAO categoriaDAO = new CategoriaDAO();

        try {
            List<Categoria> categorias = categoriaDAO.listar();

            if (categorias.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null, "Cadastre uma categoria antes de continuar");
                return null;
            }

            StringBuilder sb = new StringBuilder("Categorias cadastradas:\n");
            for (Categoria categoria : categorias) {
                sb.append(categoria).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());

            while (true) {
                String textoId = JOptionPane.showInputDialog(
                        "ID da categoria da despesa:");

                if (textoId == null) {
                    return null;
                }

                try {
                    int id = Integer.parseInt(textoId.trim());
                    Categoria categoria = categoriaDAO.buscarPorId(id);

                    if (categoria != null) {
                        return id;
                    }

                    JOptionPane.showMessageDialog(
                            null, "Categoria não encontrada");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            null, "Digite um ID de categoria válido");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null, "Erro ao consultar categorias: " + e.getMessage());
            return null;
        }
    }

    private void verDespesaEmDolar() {
        Despesa despesa = selecionarDespesa();
        if (despesa == null) {
            return;
        }
        try {
            BigDecimal cotacao = new CotacaoService().buscarCotacaoDolar();
            BigDecimal emDolar = despesa.getValor().divide(cotacao, 2, RoundingMode.HALF_UP);
            JOptionPane.showMessageDialog(null,
                    despesa + "\n\nCotação do dólar: R$ " + cotacao
                            + "\nValor em dólar: US$ " + emDolar);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível consultar a cotação. Verifique a internet.\n" + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(null, "Consulta interrompida");
        }
    }
}