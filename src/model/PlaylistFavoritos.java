package model;


public class PlaylistFavoritos extends Playlist {

    public PlaylistFavoritos(String nome, Utilizador criador, boolean publica) {
        super(nome, criador, publica);
    }

    @Override
    public void exibirPlaylist() {
        System.out.println("Playlist de Favoritos: " + getNome());
        for (Musica musica : getMusicas()) {
            System.out.println("- " + musica.getNome());
        }
    }

    @Override
    public void reproduzirPlaylist(Utilizador u) {
        // Como exemplo, aqui não embaralhamos as músicas (o utilizador pode modificar)
        for (Musica musica : getMusicas()) {
            musica.reproduzir(u);
        }
    }

    /**
     * Adiciona uma música à playlist de favoritos se ela for classificada como favorita
     * (no exemplo, é uma música que foi tocada pelo menos 5 vezes)
     */
    public void adicionarFavorito(Musica musica) {
        if (musica.getNumReproducoes() >= 5) {  // exemplo de critério
            adicionarMusica(musica);  // Delegando para o método da classe pai
        } else {
            System.out.println("Música não favorita ainda.");
        }
    }
}
