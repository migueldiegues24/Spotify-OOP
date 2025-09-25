package menus;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import model.*;
import utils.Parsing;

public class Interactive {

    public static void start() {
        Estado estado = new Estado();
        estado.loadEstado();

        Scanner sc = new Scanner(System.in);
        boolean sair = false;
        Utilizador currentUser = null;

        GestorUtilizadores gestorUtilizadores = estado.getUtilizadoresEstado();

        GestorPlaylists gestorPlaylists = estado.getPlaylistsEstado();
        GestorMusicas gestorMusicas = estado.getMusicasEstado();
        GestorAlbuns gestorAlbuns = estado.getAlbunsEstado();
        GestorEstatisticas gestorEstatisticas = estado.getEstatisticasEstado();

        while (!sair && currentUser == null) {
            int opc = Menu.menuInicial();
            switch (opc) {
                case 1 -> {
                    // Realiza login
                    String[] cred = Menu.menuLogin();
                    currentUser = gestorUtilizadores.login(cred[0], cred[1]);
                    Menu.pressEnterToContinue();
                }
                case 2 -> {
                    // Registra novo utilizador
                    String dados = Menu.register(); // Dados do utilizador no formato nome,email,morada,password
                    Menu.clear();
                    int tipoPlano = Menu.registerOptions(); // Seleção do plano
                    Menu.clear();
                    // Registrar utilizador
                    gestorUtilizadores.registrarUtilizador(dados, tipoPlano);
                    Menu.pressEnterToContinue();
                }
                case 0 -> {
                    System.out.println("Programa encerrado.");
                    try {
                        estado.saveObjectToFile(); // Tenta salvar o estado
                    } catch (IOException e) {
                        System.out.println("Erro ao salvar o estado: " + e.getMessage());
                        e.printStackTrace(); // exibe o stack trace da exceção
                    }

                    sair = true;
                }
                default -> System.out.println("Opção inválida.");
            }
        }

        // Após o login
        while (currentUser != null) {
            int op = Menu.menuUtilizador(currentUser.getNome());
            switch (op) {
                case 1 -> managePlaylists(estado, currentUser, gestorPlaylists);
                case 2 -> manageMusicas(estado, currentUser, gestorMusicas);
                case 3 -> manageAlbuns(estado, currentUser, gestorAlbuns, gestorMusicas);
                case 4 -> managePlanos(estado, currentUser);
                case 5 -> {
                    Menu.clear();
                    mostrarEstatisticas(gestorEstatisticas, currentUser);
                    Menu.pressEnterToContinue();
                }
                case 0 -> {
                    System.out.println("Sessão terminada.");
                    try {
                        estado.saveObjectToFile(); // Tenta salvar o estado
                    } catch (IOException e) {
                        System.out.println("Erro ao salvar o estado: " + e.getMessage());
                        e.printStackTrace(); // exibe o stack trace da exceção
                    }

                    currentUser = null;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
        sc.close();
    }

    private static void managePlaylists(Estado estado, Utilizador utilizador, GestorPlaylists gestorPlaylists) {
        GestorMusicas gestorMusicas = estado.getMusicasEstado();
        int opPlaylists = Menu.menuPlaylists();
        List<Playlist> playlistsDoUtilizador = gestorPlaylists.getBibliotecas()
                    .getOrDefault(utilizador, List.of());
        switch (opPlaylists) {
            case 1 -> {
                // 1) Pede o nome
                String nomePlaylist = Menu.menuCriarPlaylist();
                if (nomePlaylist == null || nomePlaylist.trim().isEmpty()) {
                    System.out.println("Erro ao criar playlist — nome inválido.");
                    Menu.pressEnterToContinue();
                    break;
                }
                // 2) Pede se é pública
                boolean publica = Menu.menuConfirmarPublica();
                // 3) Verifica se já existe uma playlist com esse nome para o utilizador
                boolean existe = playlistsDoUtilizador.stream()
                    .anyMatch(pl -> pl.getNome().equalsIgnoreCase(nomePlaylist));
                if (existe) {
                    System.out.println("❌ Já tens uma playlist com esse nome.");
                    Menu.pressEnterToContinue();
                } else {
                    gestorPlaylists.criarPlaylist(utilizador, nomePlaylist, publica);
                    Menu.pressEnterToContinue();
                }
            }
            case 2 -> {
                if (playlistsDoUtilizador.isEmpty()) {
                    System.out.println("Não tens playlists na tua biblioteca.");
                    Menu.pressEnterToContinue();
                    break;
                }

                int escolha = Menu.menuEscolherPlaylist(playlistsDoUtilizador);
                if (escolha == 0) break;

                Playlist selecionada = playlistsDoUtilizador.get(escolha - 1);
                gestorPlaylists.reproduzirPlaylist(utilizador, selecionada.getNome());
                Menu.pressEnterToContinue();
            }
            case 3 -> {
                while (true) {
                    Musica musica = Menu.menuEscolherMusicaPorGenero(gestorMusicas.getMusicasList());
                    if (musica == null) break;

                    if (playlistsDoUtilizador.isEmpty()) {
                        System.out.println("Não tens playlists na tua biblioteca.");
                        Menu.pressEnterToContinue();
                        break;
                    }

                    int escolha = Menu.menuEscolherPlaylist(playlistsDoUtilizador);
                    if (escolha == 0) {
                        continue;
                    }
                    Playlist playlist = playlistsDoUtilizador.get(escolha - 1);

                    boolean jaExiste = playlist.getMusicas().stream()
                        .anyMatch(m -> m.getNome().equalsIgnoreCase(musica.getNome())
                                    && m.getInterprete().equalsIgnoreCase(musica.getInterprete()));
                    if (jaExiste) {
                        System.out.println("❌ Esta música já existe na playlist.");
                    } else {
                        playlist.adicionarMusica(musica);
                        System.out.println("✔ Música adicionada à playlist!");
                    }
                    Menu.pressEnterToContinue();
                    break;
                }
            }
            case 4 -> {
                if (playlistsDoUtilizador.isEmpty()) {
                    System.out.println("Não tens playlists na tua biblioteca.");
                    Menu.pressEnterToContinue();
                    break;
                }

                while (true) {
                    int escolhaPlaylist = Menu.menuEscolherPlaylist(playlistsDoUtilizador);
                    if (escolhaPlaylist == 0) break;

                    Playlist playlistSelecionada = playlistsDoUtilizador.get(escolhaPlaylist - 1);
                    List<Musica> musicasDaPlaylist = playlistSelecionada.getMusicas();

                    if (musicasDaPlaylist.isEmpty()) {
                        System.out.println("A playlist não tem músicas.");
                        Menu.pressEnterToContinue();
                        break;
                    }

                    int escolhaMusica = Menu.menuRemoverMusicaPlaylist(musicasDaPlaylist);
                    if (escolhaMusica == 0) {
                        continue;
                    }

                    Musica musicaRemover = musicasDaPlaylist.get(escolhaMusica - 1);
                    playlistSelecionada.removerMusica(musicaRemover);
                    System.out.println("✔ Música removida da playlist!");
                    Menu.pressEnterToContinue();
                    break;
                }
            }
            case 5 -> {
                String[] op = Menu.menuGerarPlaylist(); // [0]=tipo, [1]=genero ou nome, [2]=duracao, [3]=nomeplaylist]
                switch (op[0]) {
                    case "1" -> {
                        gestorPlaylists.gerarPlaylistAutomaticaFavoritos(op[1], utilizador, false);
                        Menu.pressEnterToContinue();
                    }
                    case "2" -> {
                        gestorPlaylists.gerarPlaylistAutomaticaPorGeneroETempo(
                                utilizador,
                                op[3], // nome da nova playlist
                                op[1], // gênero
                                Integer.parseInt(op[2]), // duracao máxima
                                gestorMusicas // o teu gestor de músicas
                        );
                        Menu.pressEnterToContinue();
                    }
                    case "3" -> {
                        gestorPlaylists.gerarPlaylistAutomaticaExplicita(
                                op[1], // nome da nova playlist
                                utilizador,
                                gestorMusicas);
                        Menu.pressEnterToContinue();
                    }

                }
            }
            case 6 -> {
                gestorPlaylists.exibirPublicas();
                Menu.pressEnterToContinue();
            }

            case 7 -> {
                if (playlistsDoUtilizador.isEmpty()) {
                    System.out.println("Não tens playlists na tua biblioteca.");
                    Menu.pressEnterToContinue();
                    break;
                }

                int escolha = Menu.menuEscolherPlaylist(playlistsDoUtilizador);
                if (escolha == 0) break;

                Playlist selecionada = playlistsDoUtilizador.get(escolha - 1);
                List<Musica> musicas = selecionada.getMusicas();

                Menu.clear();
                System.out.println("Músicas na playlist '" + selecionada.getNome() + "':");
                if (musicas.isEmpty()) {
                    System.out.println("A playlist não tem músicas.");
                } else {
                    for (Musica m : musicas) {
                        System.out.println("- " + m.getNome() + " de " + m.getInterprete());
                    }
                }
                Menu.pressEnterToContinue();
            }
            case 8 -> {
                Menu.clear();
                System.out.println(" Biblioteca de " + utilizador.getNome());

                List<Playlist> bib = gestorPlaylists.getBibliotecas().get(utilizador);
                if (bib == null || bib.isEmpty()) {
                    System.out.println("Sem playlists guardadas na tua biblioteca.");
                } else {
                    for (Playlist pl : bib) {
                        System.out.println("· " + pl.getNome() + " (" + pl.getClass().getSimpleName() + ")");
                    }
                }
                Menu.pressEnterToContinue();
            }

            case 9 -> {
                List<Playlist> publicasDisponiveis = gestorPlaylists.getPublicas().values().stream()
                    .filter(p -> playlistsDoUtilizador.stream().noneMatch(pl -> pl.getNome().equals(p.getNome())))
                    .toList();

                Menu.clear();
                if (publicasDisponiveis.isEmpty()) {
                    System.out.println("Não há playlists públicas disponíveis para adicionar.");
                    Menu.pressEnterToContinue();
                    break;
                }

                System.out.println("Playlists públicas disponíveis para adicionar:");
                for (int i = 0; i < publicasDisponiveis.size(); i++) {
                    System.out.printf("%d) %s%n", i + 1, publicasDisponiveis.get(i).getNome());
                }
                System.out.println("0) Voltar");
                System.out.print("Escolha o número da playlist para adicionar: ");
                Scanner sc = new Scanner(System.in);
                int escolha = sc.hasNextInt() ? sc.nextInt() : -1;
                sc.nextLine();

                if (escolha == 0) break;
                if (escolha < 1 || escolha > publicasDisponiveis.size()) {
                    System.out.println("Opção inválida.");
                    Menu.pressEnterToContinue();
                    break;
                }

                Playlist escolhida = publicasDisponiveis.get(escolha - 1);
                boolean ok = gestorPlaylists.adicionarPlaylistNaBiblioteca(utilizador, escolhida.getNome());
                if (ok) {
                    System.out.println("✔ Playlist '" + escolhida.getNome() + "' adicionada à tua biblioteca.");
                } else {
                    System.out.println("❌ Não foi possível adicionar a playlist.");
                }
                Menu.pressEnterToContinue();
            }
            case 10 -> {
                List<Playlist> playlistsNaoCriadas = playlistsDoUtilizador.stream()
                    .filter(p -> !p.getCriador().equals(utilizador))
                    .toList();

                Menu.clear();
                if (playlistsNaoCriadas.isEmpty()) {
                    System.out.println("Não tens playlists na tua biblioteca que não tenhas criado.");
                    Menu.pressEnterToContinue();
                    break;
                }

                int escolha = Menu.menuEscolherPlaylist(playlistsNaoCriadas);
                if (escolha == 0) break;

                Playlist aRemover = playlistsNaoCriadas.get(escolha - 1);
                gestorPlaylists.getBibliotecas().get(utilizador).remove(aRemover);
                System.out.println("✔ Playlist '" + aRemover.getNome() + "' removida da tua biblioteca.");
                Menu.pressEnterToContinue();
            }
            case 0 -> {
                return;
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void manageMusicas(Estado estado, Utilizador utilizador, GestorMusicas gm) {
        int op = Menu.menuMusicas();
        List<Musica> musicas = gm.getMusicasList();
        switch (op) {
            case 1 -> {
                // criar e registar música
                String linha = Menu.criarMusica();
                Musica m = Parsing.parserMusica(linha);
                if (m != null) {
                    // Verifica se já existe uma música com o mesmo nome e intérprete
                    String chave = (m.getNome() + " - " + m.getInterprete()).toLowerCase().trim();
                    if (gm.getMusicas().containsKey(chave)) {
                        System.out.println("❌ Já existe uma música com esse nome e intérprete.");
                    } else {
                        gm.setMusica(m);
                        System.out.println("Música '" + m.getNome() + "' adicionada.");
                    }
                } else {
                    System.out.println("Erro ao criar música — formato inválido.");
                }
                Menu.pressEnterToContinue();
            }
            case 2 -> {
                if (musicas.isEmpty()) {
                    System.out.println("Nenhuma música disponível.");
                    Menu.pressEnterToContinue();
                    break;
                }

                while (true) {
                    List<String> generos = musicas.stream()
                        .map(Musica::getGenero)
                        .distinct()
                        .toList();
               
                    int escolhaGenero = Menu.menuEscolherGenero(generos);
                    if (escolhaGenero == 0) break;

                    String generoEscolhido = generos.get(escolhaGenero - 1);
                    List<Musica> musicasDoGenero = musicas.stream()
                        .filter(m -> m.getGenero().equalsIgnoreCase(generoEscolhido))
                        .toList();

                    Menu.clear();
                    System.out.println("Músicas do género '" + generoEscolhido + "':");
                    if (musicasDoGenero.isEmpty()) {
                        System.out.println("Nenhuma música disponível deste género.");
                    } else {
                        musicasDoGenero.forEach(m -> System.out.println("- " + m.getNome() + " de " + m.getInterprete()));
                    }
                    Menu.pressEnterToContinue();
                    break;
                }
            }
            case 3 -> {             
                if (musicas.isEmpty()) {
                    System.out.println("Nenhuma música disponível.");
                    Menu.pressEnterToContinue();
                    break;
                }
        
                while (true) {
                    List<String> generos = musicas.stream()
                        .map(Musica::getGenero)
                        .distinct()
                        .toList();

                    int escolhaGenero = Menu.menuEscolherGenero(generos);
                    if (escolhaGenero == 0) break;

                    String generoEscolhido = generos.get(escolhaGenero - 1);
                    List<Musica> musicasDoGenero = musicas.stream()
                        .filter(m -> m.getGenero().equalsIgnoreCase(generoEscolhido))
                        .toList();

                    int escolhaMusica = Menu.menuEscolherMusica(musicasDoGenero);
                    if (escolhaMusica == 0) continue;

                    Musica m = musicasDoGenero.get(escolhaMusica - 1);
                    Menu.mostrarInfoMusica(m);
                    Menu.pressEnterToContinue();
                    break;
                }
            }
            case 4 -> {
                if (musicas.isEmpty()) {
                    System.out.println("Nenhuma música disponível.");
                    Menu.pressEnterToContinue();
                    break;
                }

                while (true) {
                    List<String> generos = musicas.stream()
                        .map(Musica::getGenero)
                        .distinct()
                        .toList();
 
                    int escolhaGenero = Menu.menuEscolherGenero(generos);
                    if (escolhaGenero == 0) break;

                    String generoEscolhido = generos.get(escolhaGenero - 1);
                    List<Musica> musicasDoGenero = musicas.stream()
                        .filter(m -> m.getGenero().equalsIgnoreCase(generoEscolhido))
                        .toList();

                    int escolhaMusica = Menu.menuEscolherMusica(musicasDoGenero);
                    if (escolhaMusica == 0) continue;

                    Musica m = musicasDoGenero.get(escolhaMusica - 1);
                    Menu.clear();
                    m.reproduzir(utilizador);
                    Menu.pressEnterToContinue();
                    return;
                }
            }
            case 0 -> {
                // volta
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void manageAlbuns(Estado estado, Utilizador utilizador, GestorAlbuns gestorAlbuns,
            GestorMusicas gestorMusicas) {
        int opAlbuns = Menu.menuAlbuns();
        switch (opAlbuns) {
            case 1 -> {
                String lin = Menu.criarAlbum();
                String[] partes = lin.split(";");
                String titulo = partes[0].trim();
                String artista = partes[1].trim();
                if (titulo.isEmpty() || artista.isEmpty() || partes.length != 2) {
                    System.out.println("Erro ao criar álbum — título ou artista inválido.");
                    Menu.pressEnterToContinue();
                    break;
                }

                if (gestorAlbuns.getAlbumByName(titulo) != null) {
                    System.out.println("❌ Já existe um álbum com esse título.");
                    Menu.pressEnterToContinue();
                    break;
                }
                Album a = new Album(titulo, artista);
                gestorAlbuns.setAlbum(a);
                System.out.println("Álbum '" + a.getTitulo() + "' criado.");
                Menu.pressEnterToContinue();
            }
            case 2 -> {
                List<Album> albuns = gestorAlbuns.getAlbunsList();
                if (albuns.isEmpty()) {
                    System.out.println("Não existem álbuns disponíveis.");
                    Menu.pressEnterToContinue();
                    break;
                }

                String tituloAlbum = Menu.menuEscolherAlbum(albuns);
                if (tituloAlbum == null || tituloAlbum.isEmpty()) break;

                Album albumSelecionado = gestorAlbuns.getAlbumByName(tituloAlbum);
                if (albumSelecionado == null) {
                    System.out.println("Álbum não encontrado.");
                    Menu.pressEnterToContinue();
                    break;
                }
                String artista = albumSelecionado.getArtista();

                Musica musicaSelecionada = Menu.menuEscolherMusicaPorGenero(gestorMusicas.getMusicasList());
                if (musicaSelecionada == null) break;

                // Verifica se a música já está no álbum
                boolean jaExiste = albumSelecionado.getMusicas().stream()
                    .anyMatch(m -> m.getNome().equalsIgnoreCase(musicaSelecionada.getNome())
                                && m.getInterprete().equalsIgnoreCase(musicaSelecionada.getInterprete()));
                if (jaExiste) {
                    System.out.println("❌ Esta música já existe no álbum.");
                } else {
                    albumSelecionado.adicionarMusica(musicaSelecionada);
                    System.out.println("✔ Música adicionada ao álbum!");
                }
                Menu.pressEnterToContinue();
            }
            case 3 -> {
                String na = Menu.menuEscolherAlbum(gestorAlbuns.getAlbunsList());
                Menu.clear();
                gestorAlbuns.exibirMusicasDeAlbum(na);
                Menu.pressEnterToContinue();
            }
            case 4 -> {
                String nomeAlbum = Menu.menuEscolherAlbum(gestorAlbuns.getAlbunsList());
                Menu.clear();
                gestorAlbuns.reproduzirAlbum(utilizador, nomeAlbum);
                Menu.pressEnterToContinue();
            }
            case 0 -> {
                /* Volta ao menu anterior */ }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void managePlanos(Estado estado, Utilizador utilizador) {
        int opPlanos = Menu.menuPlanos();
        switch (opPlanos) {
            case 1 -> {
                GestorPlanos.consultarPlanosDisponiveis();
                Menu.pressEnterToContinue();
            }
            case 2 -> {
                GestorPlanos.verPlanoAtual(utilizador);
                Menu.pressEnterToContinue();
            }
            case 3 -> {
                int novoPlano = Menu.registerOptions();
                if (novoPlano == 0) {
                    break;
                }
                GestorPlanos.alterarPlanoAtual(utilizador, novoPlano);
                Menu.pressEnterToContinue();
            }
            case 0 -> {
                /* Volta ao menu anterior */ }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void mostrarEstatisticas(GestorEstatisticas gestorEstatisticas, Utilizador utilizador) {
        if (gestorEstatisticas != null) {
            System.out.println("Música mais reproduzida: " + gestorEstatisticas.musicaMaisReproduzida().getNome());
            System.out.println("Intérprete mais escutado: " + gestorEstatisticas.interpreteMaisEscutado());
            System.out.println("Utilizador que mais reproduziu músicas: "
                    + gestorEstatisticas.utilizadorMaisReproducoes().getNome());
            System.out.println("Utilizador com mais pontos: " + gestorEstatisticas.utilizadorMaisPontos().getNome());
            System.out.println("Tipo de música mais reproduzido: " + gestorEstatisticas.generoMaisReproduzido());
            System.out.println("Número de playlists públicas: " + gestorEstatisticas.numeroPlaylistsPublicas());
            System.out.println(
                    "Utilizador com mais playlists: " + gestorEstatisticas.utilizadorMaisPlaylists().getNome());

            System.out.println("Pontos do utilizador " + utilizador.getNome() + ": " + utilizador.getPontos());
            System.out.println();
        } else {
            System.out.println("Erro: gestorEstatisticas não foi inicializado.");
        }
    }

}
