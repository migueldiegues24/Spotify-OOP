package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    private String titulo;
    private String artista;
    private List<Musica> musicas;

    public Album(String titulo, String artista) {
        this.titulo = titulo;
        this.artista = artista;
        this.musicas = new ArrayList<>();
    }

    // Métodos para adicionar/remover músicas
    public void adicionarMusica(Musica musica) {
        if (!musicas.contains(musica)) {
            musicas.add(musica);
        }
    }

    public void removerMusica(Musica musica) {
        musicas.remove(musica);
    }

    // Método de reprodução do álbum
    public void reproduzir(Utilizador utilizador) {
        System.out.println("🎧 Reproduzindo álbum: " + titulo + " de " + artista);
        for (Musica musica : musicas) {
            musica.reproduzir(utilizador);  // Delegando a reprodução para cada música
        }
    }

    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public List<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<Musica> musicas) {
        this.musicas = musicas;
    }

    @Override
    public String toString() {
        return "Album{" +
                "titulo='" + titulo + '\'' +
                ", artista='" + artista + '\'' +
                ", numMusicas=" + musicas.size() +
                '}';
    }
}
