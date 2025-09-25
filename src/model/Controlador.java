package model;

import utils.Parsing;


public class Controlador {

    private GestorUtilizadores gestorUtilizadores;
    private GestorPlaylists gestorPlaylists;
    private GestorMusicas gestorMusicas;
    private GestorAlbuns gestorAlbuns;
    private GestorEstatisticas gestorEstatisticas;

    // Construtor do Controlador
    public Controlador(Estado estado) {
        this.gestorUtilizadores = estado.getUtilizadoresEstado();
        this.gestorPlaylists = estado.getPlaylistsEstado();
        this.gestorMusicas = estado.getMusicasEstado();
        this.gestorAlbuns = estado.getAlbunsEstado();
        this.gestorEstatisticas = new GestorEstatisticas(estado, gestorPlaylists);  // Estatísticas separadas
    }

    // Função para registar um novo utilizador
    public void registarUtilizador(String dados, int tipoPlano) {
        Utilizador novoUtilizador = Parsing.parserUtilizador(dados, tipoPlano);
        if (novoUtilizador != null) {
            gestorUtilizadores.adicionarUtilizador(novoUtilizador);
            System.out.println("Utilizador " + novoUtilizador.getNome() + " registrado com sucesso!");
        } else {
            System.out.println("Erro ao registrar o utilizador.");
        }
    }

    // Função para realizar login do utilizador
    public Utilizador realizarLogin(String email, String password) {
        Utilizador u = gestorUtilizadores.getUtilizadorByEmail(email);
        if (u != null && u.getPassword().equals(password)) {
            System.out.println("Login bem-sucedido para " + u.getNome());
            return u;
        } else {
            System.out.println("Email ou senha incorretos.");
            return null;
        }
    }


    // Função para adicionar música a uma playlist
    public void adicionarMusicaAPlaylist(Utilizador utilizador, String nomePlaylist, String nomeMusica) {
        Playlist playlist = gestorPlaylists.getTodas().get(nomePlaylist);
        Musica musica = gestorMusicas.getMusicaByName(nomeMusica);
        if (playlist != null && musica != null) {
            playlist.adicionarMusica(musica);
            System.out.println("Música '" + nomeMusica + "' adicionada à playlist '" + nomePlaylist + "'.");
        } else {
            System.out.println("Erro ao adicionar música à playlist.");
        }
    }


    // Função para ver estatísticas
    public void consultarEstatisticas() {
        System.out.println("Música mais reproduzida: " + gestorEstatisticas.musicaMaisReproduzida().getNome());
        System.out.println("Intérprete mais escutado: " + gestorEstatisticas.interpreteMaisEscutado());
        System.out.println("Utilizador com mais pontos: " + gestorEstatisticas.utilizadorMaisPontos().getNome());
    }

    // Função para consultar plano atual de um utilizador
    public void verPlanoAtual(Utilizador utilizador) {

    }

    // Função para alterar o plano de um utilizador
    public void alterarPlano(Utilizador utilizador, int opcaoPlano) {
        gestorEstatisticas.alterarPlanoAtual(utilizador, opcaoPlano);
    }
}
