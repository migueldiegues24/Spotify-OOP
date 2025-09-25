package model;

public class PlanoPremiumBase extends PlanoSubscricao {

    public PlanoPremiumBase() {
        super("PlanoPremiumBase", 10);
    }

    @Override
    public boolean podeCriarPlaylists() {
        return true; // Usuários PremiumBase podem criar playlists
    }

    public int pontosPorReproducao(int pontosAtuais) {
        return getPontosPorMusica();
    }

    @Override
    public PlanoPremiumBase clone() {
        return new PlanoPremiumBase(); // Clonagem do plano
    }
}
