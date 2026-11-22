package br.edu.poo.m0012.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Despesa {
    private int id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private int categoriaId;
    public static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public Despesa() {
    }

    public Despesa(String descricao, BigDecimal valor, LocalDate data, int categoriaId) {

        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoriaId = categoriaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public String toString() {
        return id + " - " + descricao + " - " + "R$ " + valor + " - " + data.format(FORMATO_BR);
    }

}
