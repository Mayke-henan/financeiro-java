package br.edu.poo.m0012.dao;

import java.sql.SQLException;
import java.util.List;

public interface Crud<T> {
    void inserir(T objeto) throws SQLException;
    List<T> listar() throws SQLException;
    T buscarPorId(int id) throws SQLException;
    void atualizar(T objeto) throws SQLException;
    void deletar(int id) throws SQLException;
}