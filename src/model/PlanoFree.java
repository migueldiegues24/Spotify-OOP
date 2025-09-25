package model;

public class PlanoFree extends PlanoSubscricao {

    public PlanoFree() {
        super("PlanoFree", 5);
    }

    @Override
    public boolean podeCriarPlaylists() {
        return false; // Usuários Free não podem criar playlists
    }

    public int pontosPorReproducao(int pontosAtuais) {
        return getPontosPorMusica();
    }

    @Override
    public PlanoFree clone() {
        return new PlanoFree(); // Clonagem do plano
    }
}
