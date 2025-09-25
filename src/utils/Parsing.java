package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.*;

public class Parsing {

    public static Utilizador parserUtilizador(String linha, int tipoPlano) {
        String[] parts = linha.split(";");

        if (parts.length < 4) {
            System.out.println("Erro: Formato inválido. Use <nome>;<email>;<morada>;<password>");
            return null;
        }

        String nome = parts[0].trim();
        String email = parts[1].trim();
        String morada = parts[2].trim();
        String password = parts[3].trim();

        PlanoSubscricao plano = switch (tipoPlano) {
            case 1 -> new PlanoFree();
            case 2 -> new PlanoPremiumBase();
            case 3 -> new PlanoPremiumTop();
            default -> null;
        };

        Utilizador utilizador = new Utilizador(nome, morada, email, password, 0);
        utilizador.setPlano(plano);

        return utilizador;
    }

    public static List<Utilizador> parseUtilizadoresInput(Estado estado) {
        List<Utilizador> novos = new ArrayList<>();

        try {
            File input = new File("input/utilizadores.txt");
            if (!input.exists()) {
                System.out.println("Ficheiro input/utilizadores.txt não encontrado.");
                return novos;
            }

            Scanner reader = new Scanner(input);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(";");
                if (parts.length != 5)
                    continue;

                String nome = parts[0];
                String email = parts[1];
                String morada = parts[2];
                String password = parts[3];
                int tipoPlano = Integer.parseInt(parts[4]);

                if (estado.getUtilizadoresEstado().verificarEmail(email))
                    continue;

                Utilizador u = new Utilizador(nome, morada, email, password, 0);
                PlanoSubscricao plano = switch (tipoPlano) {
                    case 1 -> new PlanoFree();
                    case 2 -> new PlanoPremiumBase();
                    case 3 -> new PlanoPremiumTop();
                    default -> null;
                };

                if (plano != null) {
                    u.setPlano(plano);
                    estado.getUtilizadoresEstado().setUtilizador(u);
                    novos.add(u);
                }
            }

            reader.close();
            System.out.println("Utilizadores importados: " + novos.size());

        } catch (Exception e) {
            System.out.println("Erro a importar utilizadores: " + e.getMessage());
        }

        return novos;
    }

    public static Musica parserMusica(String linha) {
        String[] parts = linha.split(";");

        if (parts.length < 9) {
            System.out.println(
                    "Erro: Formato inválido. Use <nome>;<interprete>;<editora>;<letra>;<musicaTexto>;<genero>;<duracao>;<explicita>;<videoURL>");
            return null;
        }

        String nome = parts[0].trim();
        String interprete = parts[1].trim();
        String editora = parts[2].trim();
        String letra = parts[3].trim();
        String[] musicaTexto = parts[4].trim().split(",");
        String genero = parts[5].trim();

        int duracao;
        try {
            duracao = Integer.parseInt(parts[6].trim());
        } catch (NumberFormatException e) {
            System.out.println("Erro: duração inválida → " + parts[6]);
            return null;
        }

        boolean explicita = Boolean.parseBoolean(parts[7].trim());
        String videoURL = parts[8].trim();
        if (videoURL.equalsIgnoreCase("null"))
            videoURL = null;

        // usa o construtor completo
        return new Musica(nome, interprete, editora, letra, musicaTexto, genero, duracao, explicita, videoURL);
    }

    public static List<Musica> parseMusicasInput(Estado estado) {
        List<Musica> novos = new ArrayList<>();

        try {
            File input = new File("input/musicas.txt");
            if (!input.exists()) {
                System.out.println("Ficheiro input/musicas.txt não encontrado.");
                return novos;
            }

            Scanner reader = new Scanner(input);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(";");
                if (parts.length != 9 && parts.length != 10)
                    continue;

                String nome = parts[0];
                String interprete = parts[1];

                if (estado.getMusicasEstado().verificarMusica(nome, interprete))
                    continue;

                Musica m = parserMusica(line); // usamos o método acima

                if (m != null) {
                    estado.getMusicasEstado().setMusica(m);
                    novos.add(m);
                }
            }

            reader.close();
            System.out.println("Músicas importadas: " + novos.size());

        } catch (Exception e) {
            System.out.println("Erro a importar músicas: " + e.getMessage());
        }

        return novos;
    }

    // Adicionar dentro da classe utils.Parsing, após o método parseMusicasInput:

    /**
     * Converte uma linha de texto em objeto Playlist, associando-a ao Estado.
     * Formato esperado:
     * nomePlaylist;tipo;emailUtilizador;nome1-interprete1,nome2-interprete2,...
     */
    public static Playlist parserPlaylist(String linha, Estado estado) {
        String[] parts = linha.split(";");
        if (parts.length < 5) {
            System.out.println(
                    "Erro: Formato inválido em playlists.txt. Use <nome>;<tipo>;<email>;<listaMusicas>;<Publica>");
            return null;
        }
        String nome = parts[0].trim();
        String tipo = parts[1].trim();
        String email = parts[2].trim();
        String lista = parts[3].trim();
        String pubStr = parts[4].trim();
        boolean publica = Boolean.parseBoolean(pubStr);

        // Obter utilizador criador
        Utilizador criador = estado.getUtilizadoresEstado().getUtilizadorByEmail(email);
        if (criador == null) {
            System.out.println("Erro: Utilizador '" + email + "' não encontrado para playlist '" + nome + "'.");
            return null;
        }

        Playlist playlist;
        switch (tipo) {
            case "Aleatoria" -> playlist = new PlaylistAleatoria(nome, criador, publica);
            case "Personalizada" -> playlist = new PlaylistPersonalizada(nome, criador, publica);
            case "Favoritos" -> playlist = new PlaylistFavoritos(nome, criador, publica);
            default -> {
                System.out.println("Erro: Tipo de playlist inválido: " + tipo);
                return null;
            }
        }

        // Adicionar músicas à playlist (apenas para Aleatoria e Personalizada)
        if (!"Favoritos".equals(tipo)) {
            String[] entradas = lista.split(",");
            for (String e : entradas) {
                String[] nei = e.split("-");
                if (nei.length != 2)
                    continue;
                String nomeM = nei[0].trim();
                String interp = nei[1].trim();
                Musica m = estado.getMusicasEstado().getMusicaByNeI(nomeM, interp);
                if (m != null) {
                    playlist.adicionarMusica(m);
                }
            }
        }
        return playlist;
    }

    /**
     * Lê o ficheiro de playlists e devolve todas as playlists carregadas.
     */
    public static List<Playlist> parsePlaylistsInput(Estado estado) {
        List<Playlist> novas = new ArrayList<>();
        try (Scanner reader = new Scanner(new File("input/playlists.txt"))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;
                
                // 1) parser de uma linha
                Playlist p = parserPlaylist(line, estado);
                if (p == null) continue;
                
                // Re-extrair o parts para obter o “publica”
                String[] parts = line.split(";");
                boolean isPublic = Boolean.parseBoolean(parts[4].trim());
                
                // 2) criar no gestor
                estado.getPlaylistsEstado().criarPlaylist(
                    p.getCriador(),      // Utilizador
                    p.getNome(),          // nome da playlist
                    isPublic              // visibilidade
                );
                
                // 3) recuperar a instância que acabámos de criar
                Playlist criada = estado.getPlaylistsEstado().getTodas().get(p.getNome());
                
                // 4) povoar músicas (mesmo parserPlaylist já fez isso, mas caso ajustes)
                if (criada != null) {
                    String lista = parts[3].trim();
                    String[] entradas = lista.split(",");
                    for (String e : entradas) {
                        String[] nei = e.split("-");
                        if (nei.length != 2) continue;
                        Musica m = estado.getMusicasEstado().getMusicaByNeI(nei[0].trim(), nei[1].trim());
                        if (m != null) criada.adicionarMusica(m);
                    }
                }
                
                novas.add(criada);
            }
            System.out.println("Playlists importadas: " + novas.size());
        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro input/playlists.txt não encontrado.");
        }
        return novas;
    }
    

    public static Album parserAlbum(String linha, Estado estado) {
        String[] parts = linha.split(";");
        String titulo = parts[0].trim();
        String artista = parts[1].trim();

        // agora sim, corresponde ao teu construtor:
        Album a = new Album(titulo, artista);

        // adiciona as músicas ao álbum, se vierem no parts[2]…
        if (parts.length > 2 && !parts[2].isBlank()) {
            for (String entrada : parts[2].split(",")) {
                String[] nei = entrada.trim().split("-");
                if (nei.length == 2) {
                    Musica m = estado.getMusicasEstado()
                            .getMusicaByNeI(nei[0].trim(), nei[1].trim());
                    if (m != null)
                        a.adicionarMusica(m);
                }
            }
        }

        // regista no estado
        estado.getAlbunsEstado().setAlbum(a);
        return a;
    }

    public static List<Album> parseAlbunsInput(Estado estado) {
        List<Album> resultado = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input/albuns.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank())
                    continue;
                Album a = parserAlbum(linha, estado);
                if (a != null) {
                    resultado.add(a);
                    estado.getAlbunsEstado().setAlbum(a);
                }
            }
            System.out.println("Álbuns importados: " + resultado.size());
        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro input/albuns.txt não encontrado.");
        } catch (IOException ex) {
            System.out.println("Erro a importar álbuns: " + ex.getMessage());
        }
        return resultado;
    }

}