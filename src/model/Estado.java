package model;

import java.io.*;
import java.util.List;
import utils.Parsing;

public class Estado implements Serializable {

    private GestorUtilizadores utilizadores;
    private GestorMusicas musicas;
    private GestorPlaylists playlists;
    private GestorAlbuns albuns;
    private GestorEstatisticas estatisticas;

    // Construtor inicializando gestores
    public Estado() {
        this.utilizadores = new GestorUtilizadores();
        this.musicas = new GestorMusicas();
        this.playlists = new GestorPlaylists();
        this.albuns = new GestorAlbuns();
        this.estatisticas = new GestorEstatisticas(this, playlists);
    }

    public GestorUtilizadores getUtilizadoresEstado() {
        return utilizadores;
    }

    public GestorPlaylists getPlaylistsEstado() {
        return playlists;
    }

    public GestorAlbuns getAlbunsEstado() {
        return albuns;
    }

    public GestorMusicas getMusicasEstado() {
        return musicas;
    }

    public GestorEstatisticas getEstatisticasEstado() {
        return estatisticas;
    }

    public void setPlaylists(GestorPlaylists playlists) {
        this.playlists = playlists;
    }

    public void setUtilizadores(GestorUtilizadores utilizadores) {
        this.utilizadores = utilizadores;
    }

    public void setMusicas(GestorMusicas musicas) {
        this.musicas = musicas;
    }

    public void setAlbuns(GestorAlbuns albuns) {
        this.albuns = albuns;
    }

    public void setEstatisticas(GestorEstatisticas estatisticas) {
        this.estatisticas = estatisticas;
    }

    // Método para carregar o estado de um arquivo
    public void loadEstado() {
        try {
            Estado e = readObjectFromFile();
            setUtilizadores(e.getUtilizadoresEstado());
            setMusicas(e.getMusicasEstado());
            setPlaylists(e.getPlaylistsEstado());
            setAlbuns(e.getAlbunsEstado());
            setEstatisticas(e.getEstatisticasEstado());
            System.out.println("Estado carregado com sucesso.");

        } catch (IOException | ClassNotFoundException e) {
            carregarDados();
            System.out.println("Nenhum estado anterior carregado.");
        }

    }

    // Função separada para carregar dados
    private void carregarDados() {
        // Carregar utilizadores, músicas, playlists e álbuns do arquivo
        carregarUtilizadores();
        carregarMusicas();
        carregarPlaylists();
        carregarAlbuns();
    }

    private void carregarUtilizadores() {
        List<Utilizador> novosUtilizadores = Parsing.parseUtilizadoresInput(this);
        if (!novosUtilizadores.isEmpty()) {
            try {
                saveObjectToFile();
                System.out.println("Novos utilizadores adicionados e estado atualizado.");
            } catch (IOException ex) {
                System.out.println("Erro ao guardar novo estado Utilizadores: " + ex.getMessage());
            }
        }
    }

    // Funções para carregar músicas, playlists e álbuns...
    private void carregarMusicas() {
        List<Musica> novasMusicas = Parsing.parseMusicasInput(this);
        if (!novasMusicas.isEmpty()) {
            try {
                saveObjectToFile();
                System.out.println("Novas músicas adicionadas e estado atualizado.");
            } catch (IOException ex) {
                System.out.println("Erro ao guardar novo estado Musicas: " + ex.getMessage());
            }
        }
    }

    private void carregarPlaylists() {
        List<Playlist> novasPlaylists = Parsing.parsePlaylistsInput(this);
        if (!novasPlaylists.isEmpty()) {
            try {
                saveObjectToFile();
                System.out.println("Novas playlists adicionadas e estado atualizado.");
            } catch (IOException ex) {
                System.out.println("Erro ao guardar novo estado Playlists: " + ex.getMessage());
            }
        }
    }

    private void carregarAlbuns() {
        List<Album> novoAlbuns = Parsing.parseAlbunsInput(this);
        if (!novoAlbuns.isEmpty()) {
            try {
                saveObjectToFile();
                System.out.println("Novas albuns adicionadas e estado atualizado.");
            } catch (IOException ex) {
                System.out.println("Erro ao guardar novo estado Albuns: " + ex.getMessage());
            }
        }
    }

    // Salvar estado em arquivo
    public void saveObjectToFile() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("estado.obj"))) {
            out.writeObject(this);
            System.out.println("Estado guardado com sucesso.");
        }
    }

    // Leitura do estado a partir de um arquivo
    public Estado readObjectFromFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("estado.obj"))) {
            return (Estado) in.readObject();
        }
    }
}