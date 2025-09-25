package model;

public class PlanoPremiumTop extends PlanoSubscricao {

    public PlanoPremiumTop() {
        super("PremiumTop", 100);
    }

    @Override
    public boolean podeCriarPlaylists() {
        return true; // Usuários PremiumTop podem criar playlists
    }

    @Override
    public int pontosPorReproducao(int pontosAtuais) {
        return (int) (getPontosPorMusica() + 0.025 * pontosAtuais);
    }

    @Override
    public PlanoPremiumTop clone() {
        return new PlanoPremiumTop(); // Clonagem do plano
    }
}
