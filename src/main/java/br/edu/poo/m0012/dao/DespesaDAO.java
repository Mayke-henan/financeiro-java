package br.edu.poo.m0012.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.poo.m0012.config.ConexaoFactory;
import br.edu.poo.m0012.model.Categoria;
import br.edu.poo.m0012.model.Despesa;

public class DespesaDAO implements Crud<Despesa>{

    @Override 
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

    public List<Despesa> listar() throws SQLException {
        List<Despesa> lista = new ArrayList<>();
        String sql = "SELECT id, descricao, valor, data_despesa, categoria_id FROM despesa";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Despesa d = new Despesa();
                d.setId(rs.getInt("id"));
                d.setDescricao(rs.getString("descricao"));
                d.setValor(rs.getBigDecimal("valor"));
                d.setData(rs.getDate("data_despesa").toLocalDate());
                d.setCategoriaId(rs.getInt("categoria_id"));
                lista.add(d);
            }
        }
        return lista;
    }

    public Despesa buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, descricao, valor, data_despesa, categoria_id FROM despesa WHERE id = ?";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Despesa d = new Despesa();
                    d.setId(rs.getInt("id"));
                    d.setDescricao(rs.getString("descricao"));
                    d.setValor(rs.getBigDecimal("valor"));
                    d.setData(rs.getDate("data_despesa").toLocalDate());
                    d.setCategoriaId(rs.getInt("categoria_id"));
                    return d;
                }
            }
        }
        return null;
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM despesa WHERE id = ?";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void atualizar(Despesa despesa) throws SQLException {
        String sql = "UPDATE despesa SET descricao = ?, valor = ?, data_despesa = ?, categoria_id = ? WHERE id = ?";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, despesa.getDescricao());
            ps.setBigDecimal(2, despesa.getValor());
            ps.setDate(3, java.sql.Date.valueOf(despesa.getData()));
            ps.setInt(4, despesa.getCategoriaId());
            ps.setInt(5, despesa.getId());
            ps.executeUpdate();
        }
    }

}