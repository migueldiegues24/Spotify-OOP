package model;

import java.io.Serializable;

public abstract class PlanoSubscricao implements Serializable {
    private String nome;
    private int pontosPorMusica;

    public PlanoSubscricao(String nome, int pontosPorMusica) {
        this.nome = nome;
        this.pontosPorMusica = pontosPorMusica;
    }

    public String getNome() {
        return this.nome;
    }

    public int getPontosPorMusica() {
        return this.pontosPorMusica;
    }

    public abstract boolean podeCriarPlaylists();

    public abstract int pontosPorReproducao(int pontosAtuais);

    @Override
    public abstract PlanoSubscricao clone();
}
