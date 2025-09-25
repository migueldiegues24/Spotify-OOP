package model;

import java.util.*;
import java.io.Serializable;

public class GestorEstatisticas implements Serializable {
    private final GestorPlaylists gp;
    private Estado estado;

    public GestorEstatisticas(Estado e, GestorPlaylists gp) {
        this.estado = e;
        this.gp = gp;
    }

    // Método para consultar a música mais reproduzida
    public Musica musicaMaisReproduzida() {
        return estado.getMusicasEstado().getMusicas().values().stream()
                .max(Comparator.comparingInt(Musica::getNumReproducoes))
                .orElse(null);
    }

    // Método para consultar o intérprete mais escutado
    public String interpreteMaisEscutado() {
        Map<String, Integer> cnt = new HashMap<>();
        for (Musica m : estado.getMusicasEstado().getMusicas().values()) {
            cnt.merge(m.getInterprete(), m.getNumReproducoes(), Integer::sum);
        }
        return cnt.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Nenhum");
    }

    // Consultar utilizador que mais reproduziu músicas
    public Utilizador utilizadorMaisReproducoes() {
        return estado.getUtilizadoresEstado().getUtilizadores().values().stream()
                .max(Comparator.comparingInt(Utilizador::getTotalReproducoes))
                .orElse(null);
    }

    // Consultar utilizador com mais pontos
    public Utilizador utilizadorMaisPontos() {
        return estado.getUtilizadoresEstado().getUtilizadores().values().stream()
                .max(Comparator.comparingInt(Utilizador::getPontos))
                .orElse(null);
    }

    // Consultar gênero mais reproduzido
    public String generoMaisReproduzido() {
        Map<String, Integer> cnt = new HashMap<>();
        for (Musica m : estado.getMusicasEstado().getMusicas().values()) {
            cnt.merge(m.getGenero(), m.getNumReproducoes(), Integer::sum);
        }
        return cnt.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Nenhum");
    }

    // Utilizador com mais playlists
    public Utilizador utilizadorMaisPlaylists() {
        return gp.getBibliotecas().entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public void alterarPlanoAtual(Utilizador utilizador, int opcaoPlano) {
        PlanoSubscricao novoPlano = switch (opcaoPlano) {
            case 1 -> new PlanoFree();
            case 2 -> new PlanoPremiumBase();
            case 3 -> new PlanoPremiumTop();
            default -> null;
        };
        if (novoPlano != null) {
            utilizador.setPlano(novoPlano);
            System.out.println("Plano alterado com sucesso para: " + novoPlano.getNome());
        } else {
            System.out.println("Opção inválida. Plano não alterado.");
        }
    }

    public int numeroPlaylistsPublicas() {
        return (int) gp.getPublicas().values().stream()
                .filter(Playlist::isPublica)
                .count();
    }
    
}
