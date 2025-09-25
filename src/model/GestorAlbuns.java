package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GestorAlbuns implements Serializable {
    private Map<String, Album> albuns;

    public GestorAlbuns() {
        this.albuns = new HashMap<>();
    }

    // 1) cria/atualiza um álbum
    public void setAlbum(Album a) {
        albuns.put(a.getTitulo().toLowerCase().trim(), a);
    }

    // 2) recupera álbum já criado
    public Album getAlbumByName(String nome) {
        return albuns.get(nome.toLowerCase().trim());
    }

    // 3) adiciona uma música ao álbum
    public void adicionarMusicaAlbum(String nomeAlbum, Musica m) {
        Album a = getAlbumByName(nomeAlbum);
        if (a == null) {
            System.out.println("⚠ Álbum '" + nomeAlbum + "' não existe.");
            return;
        }
        a.adicionarMusica(m);
        System.out.println("✓ Música '" + m.getNome() + "' adicionada ao álbum '" + nomeAlbum + "'.");
    }

    public List<Album> getAlbunsList() {
        return new ArrayList<>(albuns.values());
    }

    // 4) exibe músicas de um álbum
    public void exibirMusicasDeAlbum(String nomeAlbum) {
        Album a = getAlbumByName(nomeAlbum);
        if (a == null) {
            System.out.println("⚠ Álbum '" + nomeAlbum + "' não existe.");
            return;
        }
        if (a.getMusicas().isEmpty()) {
            System.out.println("⚠ Álbum '" + nomeAlbum + "' ainda não tem músicas.");
        } else {
            System.out.println("Músicas de '" + nomeAlbum + "':");
            a.getMusicas().forEach(m ->
                System.out.println("  - " + m.getNome() + " de " + m.getInterprete())
            );
        }
    }

    // Adiciona um álbum a partir de uma lista
    public void setAlbuns(List<Album> lista) {
        for (Album a : lista) {
            setAlbum(a);
        }
    }

    // Método para exibir todos os álbuns
    public void exibirAlbuns() {
        if (albuns.isEmpty()) {
            System.out.println("Nenhum álbum registrado.");
        } else {
            albuns.values().forEach(a -> System.out.println(a.toString()));
        }
    }

    public void reproduzirAlbum(Utilizador u, String titulo) {
        Album a = getAlbumByName(titulo);
        if (a == null) {
            System.out.println("Álbum não encontrado.");
        } else {
            a.reproduzir(u);
        }
    }
    
}
