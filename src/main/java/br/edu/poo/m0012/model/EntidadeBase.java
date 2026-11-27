package br.edu.poo.m0012.model;


public abstract class EntidadeBase {

    private int id;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public abstract String getResumo();

    @Override
    public String toString() {
        return id + " - " + getResumo();
    }
}