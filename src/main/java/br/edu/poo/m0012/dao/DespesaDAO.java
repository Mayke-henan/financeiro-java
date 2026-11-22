package br.edu.poo.m0012.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.edu.poo.m0012.config.ConexaoFactory;
import br.edu.poo.m0012.model.Despesa;

public class DespesaDAO {

    public void inserir(Despesa despesa) throws SQLException {

        String sql = "INSERT INTO despesa (descricao, valor, data_despesa, categoria_id) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, despesa.getDescricao());
            ps.setBigDecimal(2, despesa.getValor());
            ps.setDate(3, java.sql.Date.valueOf(despesa.getData()));
            ps.setInt(4, despesa.getCategoriaId());
            ps.executeUpdate();

        }

    }
}