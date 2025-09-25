package model;

import java.io.Serializable;

public class Musica implements Serializable {
    private String nome;
    private String interprete;
    private String editora;
    private String letra;
    private String[] musicaTexto;
    private String genero;
    private int duracao;
    private int numReproducoes;

    private boolean explicita;
    private String videoURL;

    // Construtor
    public Musica(String nome, String interprete, String editora, String letra,
            String[] musicaTexto, String genero, int duracao, boolean explicita, String videoURL) {
        this.nome = nome;
        this.interprete = interprete;
        this.editora = editora;
        this.letra = letra;
        this.musicaTexto = musicaTexto;
        this.genero = genero;
        this.duracao = duracao;
        this.numReproducoes = 0;
        this.explicita = explicita;
        this.videoURL = videoURL;
    }

    // Getters
    public String getNome() {
        return this.nome;
    }

    public String getInterprete() {
        return this.interprete;
    }

    public String getEditora() {
        return this.editora;
    }

    public String getLetra() {
        return this.letra;
    }

    public String[] getMusicaTexto() {
        return this.musicaTexto;
    }

    public String getGenero() {
        return this.genero;
    }

    public int getDuracao() {
        return this.duracao;
    }

    public int getNumReproducoes() {
        return this.numReproducoes;
    }

    public boolean isExplicita() {
        return this.explicita;
    }

    public String getVideoURL() {
        return this.videoURL;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setInterprete(String interprete) {
        this.interprete = interprete;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public void setMusicaTexto(String[] musicaTexto) {
        this.musicaTexto = musicaTexto;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public void setNumReproducoes(int numReproducoes) {
        this.numReproducoes = numReproducoes;
    }

    public void setExplicita(boolean explicita) {
        this.explicita = explicita;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    // Método para incrementar as reproduções
    public void incrementarReproducao() {
        this.numReproducoes++;
    }

    public void reproduzir(Utilizador u) {
        System.out.println("Reproduzindo: " + nome + " | " + interprete);
        String[] trechos = this.musicaTexto;

        for (int i = 0; i < trechos.length; i++) {
            System.out.println(trechos[i]);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        int ganhos = u.getPlano().pontosPorReproducao(u.getPontos());
        u.adicionarPontos(ganhos);
        incrementarReproducao();

        System.out.printf(
                "Ganhou %d pontos nesta reprodução. Total de pontos: %d%n",
                ganhos,
                u.getPontos());
    }

    @Override
    public String toString() {
        return "Musica{" +
                "nome='" + nome + '\'' +
                ", interprete='" + interprete + '\'' +
                ", genero='" + genero + '\'' +
                ", duracao=" + duracao +
                ", numReproducoes=" + numReproducoes +
                ", explicita=" + explicita +
                '}';
    }
}