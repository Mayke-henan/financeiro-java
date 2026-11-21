package com.mayke.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mayke.financeiro.config.ConexaoFactory;
import com.mayke.financeiro.model.Categoria;

public class CategoriaDAO {

    public void inserir(Categoria categoria)throws SQLException{
        String sql = "INSERT INTO categoria (nome, tipo) VALUES (?, ?)";
        try (Connection con = ConexaoFactory.getConexao();
     PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, categoria.getNome());
        ps.setString(2, categoria.getTipo().name());
        ps.executeUpdate();
}



    }
    
}
