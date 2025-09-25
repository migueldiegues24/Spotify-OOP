package model;

import java.io.Serializable;
import java.util.*;

public class GestorPlaylists implements Serializable {
    private final Map<Utilizador, List<Playlist>> bibliotecas = new HashMap<>();
    private final Map<String, Playlist> todas = new HashMap<>();
    private final Map<String, Playlist> publicas = new HashMap<>();


    public Map<String, Playlist> getTodas() {
        return Collections.unmodifiableMap(todas);
    }


    public Map<Utilizador,List<Playlist>> getBibliotecas() {
        return Collections.unmodifiableMap(bibliotecas);
    }


    public Map<String, Playlist> getPublicas() {
        return Collections.unmodifiableMap(publicas);
    }

    /** Cria e regista uma playlist no sistema (pública ou não) */
    public void criarPlaylist(Utilizador u, String nome, boolean publica) {
        if (!(u.getPlano() instanceof PlanoPremiumBase
           || u.getPlano() instanceof PlanoPremiumTop)) {
            System.out.println("❌ Só utilizadores Premium podem criar playlists.");
            return;
        }

        Playlist p = new PlaylistPersonalizada(nome, u, publica);
        System.out.println(p.toString());
        todas.put(nome, p);
        if (publica) {
            this.publicas.put(nome, p);
        }
        bibliotecas
          .computeIfAbsent(u, k -> new ArrayList<>())
          .add(p);
        System.out.printf("✔ Playlist '%s' criada. Pública? %b%n", nome, publica);
    }

    /** Incorpora uma playlist pública na biblioteca de outro utilizador */
    public void incorporarPlaylist(Utilizador destino, String nomePlaylist) {
        Playlist p = publicas.get(nomePlaylist);
        if (p == null) {
            System.out.println("❌ Playlist pública não encontrada.");
            return;
        }
        bibliotecas
          .computeIfAbsent(destino, k -> new ArrayList<>())
          .add(p);
        System.out.printf("✔ Playlist '%s' adicionada à tua biblioteca.%n", nomePlaylist);
    }

    /** Adiciona música a uma playlist existente (só o criador pode) */
    public void adicionarMusicaAPlaylist(String nomePlaylist, Musica m) {
        Playlist p = todas.get(nomePlaylist);
        if (p == null) {
            System.out.println("❌ Playlist não encontrada.");
            return;
        }
        if (!p.getCriador().equals(p.getCriador())) {
            System.out.println("❌ Só o criador pode adicionar músicas.");
            return;
        }
        p.adicionarMusica(m);
        System.out.println("✔ Música adicionada.");
    }

    /** Remove música de uma playlist existente (só o criador pode) */
    public void removerMusicaDePlaylist(String nomePlaylist, Musica m) {
        Playlist p = todas.get(nomePlaylist);
        if (p == null) {
            System.out.println("❌ Playlist não encontrada.");
            return;
        }
        if (!p.getCriador().equals(p.getCriador())) {
            System.out.println("❌ Só o criador pode remover músicas.");
            return;
        }
        p.removerMusica(m);
        System.out.println("✔ Música removida.");
    }

    /** Mostra as playlists do utilizador (criadas + incorporadas) */
    public void exibirPlaylists(Utilizador u) {
        List<Playlist> libs = bibliotecas.getOrDefault(u, Collections.emptyList());
        System.out.println(libs);
        
        libs.forEach(p -> {
            System.out.println("Exibindo playlist: " + p.getNome() + " Pública? " + p.isPublica());
            String flag = p.isPublica() ? "[P]" : "[R]";
            System.out.printf("%s %s (criador: %s)%n", flag, p.getNome(), p.getCriador().getNome());
        });
        
    }
    

    /** Lista as músicas de uma playlist (se estiver na biblioteca) */
    public void exibirMusicasDePlaylist(Utilizador u, String nomePlaylist) {
        List<Playlist> libs = bibliotecas.getOrDefault(u, Collections.emptyList());
        for (Playlist p : libs) {
            if (p.getNome().equals(nomePlaylist)) {
                System.out.printf("Músicas em '%s':%n", nomePlaylist);
                p.getMusicas().forEach(m ->
                    System.out.printf(" - %s | %s%n", m.getNome(), m.getInterprete()));
                return;
            }
        }
        System.out.println("❌ Não encontraste essa playlist na tua biblioteca.");
    }

    /** Executa uma playlist (se estiver na biblioteca do user) */
    public void reproduzirPlaylist(Utilizador u, String nomePlaylist) {
        List<Playlist> libs = bibliotecas.getOrDefault(u, Collections.emptyList());
        for (Playlist p : libs) {
            if (p.getNome().equals(nomePlaylist)) {
                p.reproduzirPlaylist(u);
                return;
            }
        }
        System.out.println("❌ Não encontraste '" + nomePlaylist + "' na tua biblioteca.");
    }

    /** Mostra todas as playlists públicas disponíveis */
    public void exibirPublicas() {
        if (publicas.isEmpty()) {
            System.out.println("Não há playlists públicas.");
            return;
        }
        publicas.values().forEach(p ->
            System.out.printf("%s (criador: %s)%n",
                p.getNome(), p.getCriador().getNome()));
    }

    /** Gera uma playlist de Favoritos (só PremiumTop) */
    public void gerarPlaylistAutomaticaFavoritos(String nomeNova, Utilizador u, boolean publica) {
        if (!(u.getPlano() instanceof PlanoPremiumTop)) {
            System.out.println("❌ Só PremiumTop podem gerar playlist de favoritos.");
            return;
        }
        Playlist p = new PlaylistFavoritos(nomeNova, u, publica);
        todas.put(nomeNova, p);
        bibliotecas
          .computeIfAbsent(u, k -> new ArrayList<>())
          .add(p);
        if (publica) publicas.put(nomeNova, p);
        System.out.printf("✔ Playlist de Favoritos '%s' gerada.%n", nomeNova);
    }

    /** Gera playlist por género e tempo (Premium) */
    public void gerarPlaylistAutomaticaPorGeneroETempo(
        Utilizador u,
        String nomeNova,
        String genero,
        int maxDuracao,
        GestorMusicas gm
    ) {
        if (!(u.getPlano() instanceof PlanoPremiumBase
           || u.getPlano() instanceof PlanoPremiumTop)) {
            System.out.println("❌ Só Premium podem gerar playlists automáticas.");
            return;
        }
        Playlist p = new PlaylistPersonalizada(nomeNova, u, false);
        int soma = 0;
        for (Musica m : gm.getMusicas().values()) {
            if (m.getGenero().equalsIgnoreCase(genero)
               && soma + m.getDuracao() <= maxDuracao) {
                p.adicionarMusica(m);
                soma += m.getDuracao();
            }
        }
        todas.put(nomeNova, p);
        bibliotecas
          .computeIfAbsent(u, k -> new ArrayList<>())
          .add(p);
        System.out.printf("✔ Playlist '%s' (%s, ≤%ds) gerada com %d músicas.%n",
            nomeNova, genero, maxDuracao, p.getMusicas().size());
    }

    /** Gera playlist só com músicas explícitas (Premium) */
    public void gerarPlaylistAutomaticaExplicita(
        String nomeNova,
        Utilizador u,
        GestorMusicas gm
    ) {
        if (!(u.getPlano() instanceof PlanoPremiumBase
           || u.getPlano() instanceof PlanoPremiumTop)) {
            System.out.println("❌ Só Premium podem gerar playlists automáticas.");
            return;
        }
        Playlist p = new PlaylistPersonalizada(nomeNova, u, false);
        gm.getMusicas().values().stream()
          .filter(Musica::isExplicita)
          .forEach(p::adicionarMusica);
        todas.put(nomeNova, p);
        bibliotecas
          .computeIfAbsent(u, k -> new ArrayList<>())
          .add(p);
        System.out.printf("✔ Playlist Explícita '%s' gerada com %d músicas.%n",
            nomeNova, p.getMusicas().size());
    }

    /** Incorpora uma playlist pública na biblioteca (helper boolean) */
    public boolean adicionarPlaylistNaBiblioteca(Utilizador u, String nomePlaylist) {
        Playlist p = publicas.values().stream()
            .filter(pl -> pl.getNome().equals(nomePlaylist))
            .findFirst().orElse(null);
        if (p == null) return false;
        bibliotecas.computeIfAbsent(u, __ -> new ArrayList<>()).add(p);
        return true;
    }
}
