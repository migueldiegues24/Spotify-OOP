package model;

import java.util.Collections;

public class PlaylistAleatoria extends Playlist {

    public PlaylistAleatoria(String nome, Utilizador criador, boolean publica) {
        super(nome, criador, publica);
    }

    @Override
    public void exibirPlaylist() {
        System.out.println("Playlist Aleatória: " + getNome());
        for (Musica musica : getMusicas()) {
            System.out.println("- " + musica.getNome());
        }
    }

    @Override
    public void reproduzirPlaylist(Utilizador u) {
        // Embaralha a lista de músicas
        Collections.shuffle(getMusicas());

        // Reproduz cada música da playlist embaralhada
        for (Musica musica : getMusicas()) {
            musica.reproduzir(u);
        }
    }
}
