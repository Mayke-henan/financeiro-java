package br.edu.poo.m0012.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.poo.m0012.config.ConexaoFactory;
import br.edu.poo.m0012.model.Categoria;
import br.edu.poo.m0012.model.TipoCategoria;

public class CategoriaDAO implements Crud<Categoria> {

    public void inserir(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nome, tipo) VALUES (?, ?)";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getTipo().name());
            ps.executeUpdate();
        }
    }

    public List<Categoria> listar() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id, nome, tipo FROM categoria";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTipo(TipoCategoria.valueOf(rs.getString("tipo")));
                lista.add(c);
            }
        }
        return lista;
    }

    public Categoria buscarPorId(int id) throws SQLException {
        String sqlFiltro = "SELECT id, nome, tipo FROM categoria WHERE id = ?";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sqlFiltro)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria c = new Categoria();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    c.setTipo(TipoCategoria.valueOf(rs.getString("tipo")));
                    return c;
                }
            }
        }
        return null;
    }

    public void deletar(int id) throws SQLException {

        String sql = "DELETE FROM categoria WHERE id = ?";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

        }

    }

    public void atualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nome = ?, tipo = ? WHERE id = ?";
        try (Connection con = ConexaoFactory.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getTipo().name());
            ps.setInt(3, categoria.getId());
            ps.executeUpdate();
        }

    }
}
