package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Playlist implements Serializable {
    protected String nome;
    protected List<Musica> musicas;
    protected Utilizador criador;
    public boolean publica;

    public Playlist(String nome, Utilizador criador, boolean publica) {
        this.nome = nome;
        this.musicas = new ArrayList<>();
        this.criador = criador;
        this.publica = publica;
    }

    public boolean isPublica() {
        return publica;
    }

    public void setPublica(boolean publica) {
        this.publica = publica;
    }

    // Método para adicionar uma música
    public void adicionarMusica(Musica m) {
        musicas.add(m);
    }

    // Método para remover uma música
    public void removerMusica(Musica m) {
        musicas.remove(m);
    }

    // Método para obter o criador da playlist
    public Utilizador getCriador() {
        return criador;
    }

    // Método para obter o nome da playlist
    public String getNome() {
        return nome;
    }

    // Método para obter todas as músicas
    public List<Musica> getMusicas() {
        return musicas;
    }

    // Método abstrato para exibir a playlist, será implementado nas subclasses
    public abstract void exibirPlaylist();

    // Método para reproduzir a playlist, implementado nas subclasses
    public abstract void reproduzirPlaylist(Utilizador u);

    public String toString() {
        return "Playlist{" +
            "nome='" + nome + '\'' +
            ", numMusicas=" + musicas.size() +
            '}';
    }
}
