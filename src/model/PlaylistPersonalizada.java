package model;

public class PlaylistPersonalizada extends Playlist {

    public PlaylistPersonalizada(String nome, Utilizador criador, boolean publica) {
        super(nome, criador, publica);
    }

    @Override
    public void exibirPlaylist() {
        System.out.println("Playlist Personalizada: " + getNome());
        for (Musica musica : getMusicas()) {
            System.out.println("- " + musica.getNome());
        }
    }

    @Override
    public void reproduzirPlaylist(Utilizador u) {
        // Reproduz na ordem definida pelo utilizador
        for (Musica musica : getMusicas()) {
            musica.reproduzir(u);
        }
    }
    
    @Override
    public String toString() {
        return "Playlist{" +
            "nome='" + super.nome + '\'' +
            ", numMusicas=" + super.musicas.size() +
            ", public= "+ super.publica + 
            '}';
    }
}
