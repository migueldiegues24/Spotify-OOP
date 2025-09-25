package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utilizador implements Serializable {
    private String nome;
    private String email;
    private String morada;
    private String password;
    private int pontos;
    private PlanoSubscricao plano; // Plano associado ao utilizador
    private List<Musica> musicas; // Música associada ao utilizador

    // Construtor
    public Utilizador(String nome, String morada, String email, String password, int pontos) {
        this.nome = nome;
        this.morada = morada;
        this.email = email;
        this.password = password;
        this.pontos = pontos;
        this.plano = null; // Inicialmente, sem plano
        this.musicas = new ArrayList<>();
    }

    // biblioteca pessoal
    private List<Album> bibliotecaAlbuns = new ArrayList<>();
    private List<Playlist> bibliotecaPlaylists = new ArrayList<>();

    public void adicionarAlbumABiblioteca(Album a) {
        if (!bibliotecaAlbuns.contains(a))
            bibliotecaAlbuns.add(a);
    }

    public void adicionarPlaylistABiblioteca(Playlist p) {
        if (!bibliotecaPlaylists.contains(p))
            bibliotecaPlaylists.add(p);
    }

    public List<Album> getBibliotecaAlbuns() {
        return Collections.unmodifiableList(bibliotecaAlbuns);
    }

    public List<Playlist> getBibliotecaPlaylists() {
        return Collections.unmodifiableList(bibliotecaPlaylists);
    }

    // Getters e Setters
    public String getNome() {
        return this.nome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getMorada() {
        return this.morada;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPontos() {
        return this.pontos;
    }

    public PlanoSubscricao getPlano() {
        return this.plano;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public void setPlano(PlanoSubscricao plano) {
        this.plano = plano;
    }

    public void setMusicas(List<Musica> musicas) {
        this.musicas = new ArrayList<>(musicas);
    }

    // Métodos de Lógica
    public void adicionarPontos(int pontos) {
        this.pontos += pontos;
    }


    // Método para associar uma música ao utilizador
    public void adicionarMusica(Musica musica) {
        if (!musicas.contains(musica)) {
            musicas.add(musica);
        }
    }

    public boolean podeCriarPlaylists() {
        return this.plano instanceof PlanoPremiumBase || this.plano instanceof PlanoPremiumTop;
    }

    // Adiciona este método na classe Utilizador
    public int getTotalReproducoes() {
        // Retorna o número total de reproduções de todas as músicas associadas a este
        // utilizador
        return this.musicas.stream()
                .mapToInt(Musica::getNumReproducoes)
                .sum();
    }

}
