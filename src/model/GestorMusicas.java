package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorMusicas implements Serializable {
    private Map<String, Musica> musicas;

    public GestorMusicas() {
        this.musicas = new HashMap<>();
    }

    public void adicionarMusica(Musica musica) {
        musicas.put(musica.getNome(), musica);
    }

    public void setMusica(Musica m) {
        String chave = (m.getNome() + " - " + m.getInterprete()).toLowerCase().trim();
        this.musicas.put(chave, m);
    }

    public Musica getMusicaByName(String nome) {
        String chave = nome.toLowerCase().trim();
        return this.musicas.get(chave);
    }

    public Musica getMusicaByNeI(String nome, String interprete) {
        String chave = (nome + " - " + interprete).toLowerCase().trim();
        return this.musicas.get(chave);
    }

    public boolean verificarMusica(String nome, String interprete) {
        String chave = nome + " - " + interprete;
        return this.musicas.containsKey(chave);
    }

    public Map<String, Musica> getMusicas() {
        return this.musicas;
    }

    public List<Musica> getMusicasList() {
        return new ArrayList<>(this.musicas.values());
    }

    public void reproduzirMusica(Utilizador u, Musica m) {
        m.reproduzir(u);
    }

}

