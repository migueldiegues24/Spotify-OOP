package menus;

import java.io.Console;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import model.Album;
import model.Musica;

public class Menu {
    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE   = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN   = "\u001B[36m";
    private static final String ANSI_WHITE  = "\u001B[37m";

    // Menu inicial
    public static int menuInicial() {
        clear();
        StringBuilder sb = new StringBuilder( ANSI_GREEN + "----------- SPOTIFUM -----------\n\n" + ANSI_RESET);

        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        sb.append(ANSI_GREEN + "Data Atual: " + ANSI_RESET).append(agora.format(formatter)).append("\n\n");

        sb.append(ANSI_GREEN + "1)" + ANSI_RESET + " Iniciar sessão.\n");
        sb.append(ANSI_GREEN + "2)" + ANSI_RESET + " Registar novo utilizador.\n");
        sb.append(ANSI_RED + "0)" + ANSI_RESET + " Sair.\n\n");
        sb.append("Opção: ");

        System.out.println(sb.toString());

        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            System.out.println("Opção inválida! Tente novamente.");
            scanner.nextLine();
            return menuInicial();
        }
    }

    // Menu de login
    public static String[] menuLogin() {
        clear();
        Scanner scanner = new Scanner(System.in);

        System.out.println(ANSI_GREEN + "----------- LOGIN -----------\n" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "Email: " + ANSI_RESET);
        String email = scanner.nextLine();

        String password;
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword(ANSI_GREEN + "Password: " + ANSI_RESET);
            password = new String(passwordArray);
        } else {
            System.out.print(ANSI_GREEN + "Password: " + ANSI_RESET);
            password = scanner.nextLine();
        }

        return new String[] { email, password };
    }

    public static String register() {
        clear();
        System.out.println(ANSI_GREEN + "----------- REGISTAR NOVO UTILIZADOR -----------" + ANSI_RESET);
        System.out.println("Insira os dados no formato: " + ANSI_CYAN + "nome" + ANSI_RESET + ";" + ANSI_CYAN + "email" + ANSI_RESET + ";" + ANSI_CYAN + "morada" + ANSI_RESET + ";" + ANSI_CYAN + "password" + ANSI_RESET);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine(); // Retorna os dados do utilizador como uma string
    }

    public static int registerOptions() {
        clear();
        System.out.println("Escolha o tipo de plano:");
        System.out.println(ANSI_GREEN + "1) " + ANSI_RESET + "Free\n" + ANSI_GREEN + "2) " + ANSI_RESET + "Premium Base\n" + ANSI_GREEN + "3) " + ANSI_RESET +  "Premium Top");
        System.out.println(ANSI_RED + "0) " + ANSI_RESET + "Voltar\n");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt(); // Retorna o tipo de plano selecionado
    }

    // Menu do utilizador
    public static int menuUtilizador(String username) {
        clear();
        // Welcome message in green
        System.out.println(ANSI_GREEN + "Bem-vindo, "+  ANSI_WHITE + username + ANSI_GREEN + "!" + ANSI_RESET + "\n");

        // Menu header in cyan
        StringBuilder sb = new StringBuilder();
        sb.append(ANSI_GREEN + "----------- MENU UTILIZADOR -----------\n\n" + ANSI_RESET);

        // Options in yellow
        sb.append(ANSI_GREEN + "1) " + ANSI_RESET +  "Playlists.\n");
        sb.append(ANSI_GREEN + "2) " + ANSI_RESET+  "Musicas.\n");
        sb.append(ANSI_GREEN + "3) " + ANSI_RESET+  "Albuns.\n");
        sb.append(ANSI_GREEN + "4) " + ANSI_RESET+  "Ver planos de subscrição.\n");
        sb.append(ANSI_GREEN + "5) " + ANSI_RESET+  "Consultar estatísticas.\n");
        sb.append(ANSI_RED + "0) " + ANSI_RESET+  "Terminar sessão.\n" + ANSI_RESET);

        // Prompt in blue
        sb.append("Opção: ");
        System.out.print(sb.toString());

        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            // Error message in red
            System.out.println(ANSI_RED + "Opção inválida! Tente novamente." + ANSI_RESET);
            scanner.nextLine();
            return menuUtilizador(username);
        }
    }

    public static String menuCriarPlaylist() {
        clear();
        System.out.println(ANSI_GREEN + "----------- CRIAR PLAYLIST -----------" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "Nome da playlist: " + ANSI_RESET);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim(); // Retorna o nome da playlist
    }

    public static Musica menuEscolherMusicaPorGenero(List<Musica> musicas) {
        clear();
        Scanner sc = new Scanner(System.in);

        // Obter géneros únicos
        List<String> generos = musicas.stream()
            .map(Musica::getGenero)
            .distinct()
            .toList();

        while (true) {
            System.out.println(ANSI_GREEN + "----------- ESCOLHER GÉNERO -----------" + ANSI_RESET);
            for (int i = 0; i < generos.size(); i++) {
                System.out.printf(ANSI_GREEN + "%d) " + ANSI_RESET + "%s%n", i + 1, generos.get(i));
            }
            System.out.println(ANSI_RED + "0) " + ANSI_RESET + "Voltar");
            System.out.print("Escolha o número do género: ");
            int escolhaGenero = sc.hasNextInt() ? sc.nextInt() : -1;
            sc.nextLine();

            if (escolhaGenero == 0) {
                clear();
                return null;
            }
            if (escolhaGenero < 1 || escolhaGenero > generos.size()) {
                System.out.println("Opção inválida!");
                continue;
            }

            String generoEscolhido = generos.get(escolhaGenero - 1);
            List<Musica> musicasDoGenero = musicas.stream()
                .filter(m -> m.getGenero().equalsIgnoreCase(generoEscolhido))
                .toList();

            while (true) {
                clear();
                System.out.println(ANSI_GREEN + "----------- ESCOLHER MÚSICA -----------" + ANSI_RESET);
                for (int i = 0; i < musicasDoGenero.size(); i++) {
                    Musica m = musicasDoGenero.get(i);
                    System.out.printf(ANSI_GREEN + "%d) " + ANSI_RESET + "%s - %s%n", i + 1, m.getNome(), m.getInterprete());
                }
                System.out.println(ANSI_RED + "0) " + ANSI_RESET + "Voltar");
                System.out.print("Escolha o número da música: ");
                int escolhaMusica = sc.hasNextInt() ? sc.nextInt() : -1;
                sc.nextLine();

                if (escolhaMusica == 0) {
                    clear();
                    break;
                }
                if (escolhaMusica < 1 || escolhaMusica > musicasDoGenero.size()) {
                    System.out.println("Opção inválida!");
                    continue;
                }
                return musicasDoGenero.get(escolhaMusica - 1);
            }
        }
    }

    // Mostra géneros e devolve o índice escolhido
    public static int menuEscolherGenero(List<String> generos) {
        clear();
        System.out.println(ANSI_GREEN + "----------- ESCOLHER GÉNERO -----------" + ANSI_RESET);
        for (int i = 0; i < generos.size(); i++) {
            System.out.printf(ANSI_GREEN + "%d) " + ANSI_RESET + "%s%n", i + 1, generos.get(i));
        }
        System.out.println(ANSI_RED + "0) " + ANSI_RESET + "Voltar");
        System.out.print("Escolha o número do género: ");
        Scanner sc = new Scanner(System.in);
        int escolha = sc.hasNextInt() ? sc.nextInt() : -1;
        sc.nextLine();
        return escolha;
    }

    // Mostra músicas e devolve o índice escolhido
    public static int menuEscolherMusica(List<Musica> musicas) {
        clear();
        System.out.println(ANSI_GREEN + "----------- ESCOLHER MÚSICA -----------" + ANSI_RESET);
        for (int i = 0; i < musicas.size(); i++) {
            Musica m = musicas.get(i);
            System.out.printf(ANSI_GREEN + "%d) " + ANSI_RESET + "%s - %s%n", i + 1, m.getNome(), m.getInterprete());
        }
        System.out.println(ANSI_RED + "0) " + ANSI_RESET + "Voltar");
        System.out.print("Escolha o número da música: ");
        Scanner sc = new Scanner(System.in);
        int escolha = sc.hasNextInt() ? sc.nextInt() : -1;
        sc.nextLine();
        return escolha;
    }

    // Mostra informações detalhadas de uma música
    public static void mostrarInfoMusica(Musica m) {
        clear();
        System.out.println(ANSI_GREEN + "Informações da música" + ANSI_RESET);
        System.out.println("Nome: " + m.getNome());
        System.out.println("Intérprete: " + m.getInterprete());
        System.out.println("Editora: " + m.getEditora());
        System.out.println("Letra: " + m.getLetra());
        System.out.println("MusicaTexto:\n" + String.join("\n", m.getMusicaTexto()));
        System.out.println("Género: " + m.getGenero());
        System.out.println("Duração: " + m.getDuracao());
        System.out.println("Explícita: " + (m.isExplicita() ? "Sim" : "Não"));
        System.out.println("Vídeo URL: " + m.getVideoURL());
    }

    public static Musica menuEscolherMusicaDoArtista(List<Musica> musicas, String artista) {
        clear();
        List<Musica> musicasDoArtista = musicas.stream()
            .filter(m -> m.getInterprete().equalsIgnoreCase(artista))
            .toList();

        if (musicasDoArtista.isEmpty()) {
            System.out.println("Não existem músicas deste artista para adicionar.");
            pressEnterToContinue();
            return null;
        }

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(ANSI_GREEN + "----------- ESCOLHER MÚSICA DE " + artista + " -----------" + ANSI_RESET);
            for (int i = 0; i < musicasDoArtista.size(); i++) {
                Musica m = musicasDoArtista.get(i);
                System.out.printf(ANSI_GREEN + "%d) " + ANSI_RESET + "%s%n", i + 1, m.getNome());
            }
            System.out.println(ANSI_RED + "0) " + ANSI_RESET + "Voltar");
            System.out.print("Escolha o número da música para adicionar: ");
            int escolha = sc.hasNextInt() ? sc.nextInt() : -1;
            sc.nextLine();

            if (escolha == 0) return null;
            if (escolha < 1 || escolha > musicasDoArtista.size()) {
                System.out.println("Opção inválida!");
                pressEnterToContinue();
                continue;
            }
            return musicasDoArtista.get(escolha - 1);
        }
    }

    public static int menuRemoverMusicaPlaylist(List<model.Musica> musicas) {
        clear();
        System.out.println(ANSI_GREEN + "----------- ESCOLHER MÚSICA DA PLAYLIST -----------" + ANSI_RESET);
        for (int i = 0; i < musicas.size(); i++) {
            System.out.printf(ANSI_GREEN + "%d) " + ANSI_RESET +"%s - %s%n", i + 1, musicas.get(i).getNome(), musicas.get(i).getInterprete());
        }
        System.out.println(ANSI_RED + "0) " + ANSI_RESET +"Voltar");
        System.out.print("Escolhe o número da música para remover: ");
        Scanner sc = new Scanner(System.in);
        int escolha;
        while (true) {
            if (sc.hasNextInt()) {
                escolha = sc.nextInt();
                if (escolha >= 0 && escolha <= musicas.size()) {
                    break;
                } else {
                    System.out.print("Número inválido! Tente novamente: ");
                }
            } else {
                System.out.print("Entrada inválida! Tente novamente: ");
                sc.next();
            }
        }
        return escolha;
    }

    public static int menuPlaylists() {
        clear();
        System.out.println(ANSI_GREEN + "----------- GERIR PLAYLISTS -----------\n" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "1) " + ANSI_RESET +  "Criar nova playlist.");
        System.out.println(ANSI_GREEN + "2) " + ANSI_RESET + "Reproduzir playlist.");
        System.out.println(ANSI_GREEN + "3) " + ANSI_RESET + "Adicionar música a uma playlist.");
        System.out.println(ANSI_GREEN + "4) " + ANSI_RESET + "Remover música de uma playlist.");
        System.out.println(ANSI_GREEN + "5) " + ANSI_RESET + "Gerar uma playlist automática.");
        System.out.println(ANSI_GREEN + "6) " + ANSI_RESET + "Ver todas as playlists.");
        System.out.println(ANSI_GREEN + "7) " + ANSI_RESET + "Ver músicas de uma playlist.");
        System.out.println(ANSI_GREEN + "8) " + ANSI_RESET + "Ver biblioteca pessoal de playlists.");
        System.out.println(ANSI_GREEN + "9) " + ANSI_RESET + "Guardar playlist pública na biblioteca.");
        System.out.println(ANSI_GREEN + "10) " + ANSI_RESET + "Remover playlist da biblioteca (não és o criador).");
        System.out.println(ANSI_RED + "0) " + ANSI_RESET + "Voltar\n");

        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()) {
            return sc.nextInt();
        }
        sc.nextLine();
        return menuPlaylists();
    }

    // Menu de músicas
    public static int menuMusicas() {
        clear();
        StringBuilder sb = new StringBuilder(ANSI_GREEN + "----------- GERIR MÚSICAS -----------\n\n" + ANSI_RESET);
        sb.append(ANSI_GREEN + "1) " + ANSI_RESET + "Criar música.\n");
        sb.append(ANSI_GREEN + "2) " + ANSI_RESET + "Ver músicas disponíveis.\n");
        sb.append(ANSI_GREEN + "3) " + ANSI_RESET + "Procurar uma música.\n");
        sb.append(ANSI_GREEN + "4) " + ANSI_RESET + "Reproduzir uma música.\n");
        sb.append(ANSI_RED + "0) " + ANSI_RESET + "Voltar\n");
        System.out.println(sb.toString());

        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            System.out.println("Opção inválida! Tente novamente.");
            scanner.nextLine();
            return menuMusicas();
        }
    }

    // Função para criar música
    public static String criarMusica() {
        clear();
        System.out.println(ANSI_GREEN + "Insira os dados da música no formato:" + ANSI_RESET);
        System.out.println(
                ANSI_RED + "<nome>;<interprete>;<editora>;<letra>;<musicaTexto>;<genero>;<duracao>;<explicita>;<videoURL>" + ANSI_RESET);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    // Menu de Planos
    public static int menuPlanos() {
        clear();
        StringBuilder sb = new StringBuilder(ANSI_GREEN + "----------- PLANOS SUBSCRIÇÃO -----------\n\n" + ANSI_RESET);
        sb.append(ANSI_GREEN + "1) " + ANSI_RESET + "Consultar planos.\n");
        sb.append(ANSI_GREEN + "2) " + ANSI_RESET + "Ver plano atual.\n");
        sb.append(ANSI_GREEN + "3) " + ANSI_RESET + "Alterar plano atual.\n");
        sb.append(ANSI_RED + "0) " + ANSI_RESET + "Voltar\n");
        System.out.println(sb.toString());

        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            System.out.println("Opção inválida! Tente novamente.");
            scanner.nextLine();
            return menuPlanos();
        }
    }

    // Funções para gerar playlists automáticas
    public static String[] menuGerarPlaylist() {
        clear();
        System.out.println(ANSI_GREEN + "----------- GERAR PLAYLIST AUTOMÁTICA -----------" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "1) " + ANSI_RESET + "Favoritos");
        System.out.println(ANSI_GREEN + "2) " + ANSI_RESET + "Por gênero e tempo");
        System.out.println(ANSI_GREEN + "3) " + ANSI_RESET + "Apenas explícitas");
        System.out.print("Opção: ");
        Scanner sc = new Scanner(System.in);
        int tipo = sc.nextInt();
        sc.nextLine();
        switch (tipo) {
            case 1 -> {
                System.out.print(ANSI_GREEN + "Nome da nova playlist de favoritos: " + ANSI_RESET);
                String nome1 = sc.nextLine().trim();
                return new String[] { "1", nome1 };
            }
            case 2 -> {
                System.out.print(ANSI_GREEN + "Gênero musical: " + ANSI_RESET);
                String genero = sc.nextLine().trim();
                System.out.print(ANSI_GREEN + "Duração máxima (segundos): " + ANSI_RESET);
                int dur = sc.nextInt();
                sc.nextLine();
                System.out.print(ANSI_GREEN + "Nome da nova playlist: " + ANSI_RESET);
                String nome2 = sc.nextLine().trim();
                return new String[] { "2", genero, String.valueOf(dur), nome2 };
            }
            case 3 -> {
                System.out.print(ANSI_GREEN + "Nome da nova playlist de músicas explícitas: " + ANSI_RESET);
                String nome3 = sc.nextLine().trim();
                return new String[] { "3", nome3 };
            }
            default -> {
                System.out.println("Opção inválida! Tente novamente.");
                return menuGerarPlaylist();
            }
        }
    }

    // 1. Menu de álbuns
    public static int menuAlbuns() {
        clear();
        StringBuilder sb = new StringBuilder(ANSI_GREEN + "----------- GERIR ÁLBUNS -----------\n\n" + ANSI_RESET);
        sb.append(ANSI_GREEN + "1) " + ANSI_RESET + "Criar álbum.\n");
        sb.append(ANSI_GREEN + "2) " + ANSI_RESET + "Adicionar música a um álbum.\n");
        sb.append(ANSI_GREEN + "3) " + ANSI_RESET + "Ver músicas de um álbum.\n");
        sb.append(ANSI_GREEN + "4) " + ANSI_RESET + "Reproduzir álbum.\n");
        sb.append(ANSI_RED + "0) " + ANSI_RESET + "Voltar\n");
        System.out.println(sb.toString());


        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            System.out.println("Opção inválida! Tente novamente.");
            scanner.nextLine();
            return menuAlbuns();
        }
    }

    // 2. Ler linha completa para criar um álbum
    // definir o formato em Parsing.parserAlbum: e.g. "nome;intérprete;editora")
    public static String criarAlbum() {
        clear();
        System.out.println(ANSI_GREEN + "----------- CRIAR ÁLBUM -----------" + ANSI_RESET);
        System.out.println(ANSI_RED + "Insira os dados no formato: título;artista" + ANSI_RESET);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    // 3. Escolher álbum já existente (pelo nome)
    public static String menuEscolherAlbum(List <Album> albuns) {
        clear();
        Scanner sc = new Scanner(System.in);

        // Exibe a lista de álbuns com números
        System.out.println(ANSI_GREEN + "----------- ESCOLHER ÁLBUM -----------" + ANSI_RESET);
        for (int i = 0; i < albuns.size(); i++) {
            System.out.println((i + 1) + ") " + albuns.get(i).getTitulo() + " - " + albuns.get(i).getArtista());
        }
        // Solicita ao usuário que escolha um álbum pelo número
        System.out.print("Escolha o número do álbum: ");
        int escolha;
        while (true) {
            if (sc.hasNextInt()) {
                escolha = sc.nextInt();
                if (escolha > 0 && escolha <= albuns.size()) {
                    break;
                } else {
                    System.out.print("Número inválido! Tente novamente: ");
                }
            } else {
                System.out.print("Entrada inválida! Tente novamente: ");
                sc.next(); // Limpa a entrada inválida
            }
        }
        // Retorna o álbum escolhido
        return albuns.get(escolha - 1).getTitulo();
    }

    public static int menuEscolherPlaylist(List<model.Playlist> playlists) {
        clear();
        System.out.println(ANSI_GREEN + "----------- ESCOLHER PLAYLIST -----------" + ANSI_RESET);
        for (int i = 0; i < playlists.size(); i++) {
            System.out.printf(ANSI_GREEN + "%d) " + ANSI_RESET + "%s%n", i + 1, playlists.get(i).getNome());
        }
        System.out.println(ANSI_RED + "0) " + ANSI_RESET + "Voltar");
        System.out.print("Escolhe o número da playlist: ");
        Scanner sc = new Scanner(System.in);
        int escolha;
        while (true) {
            if (sc.hasNextInt()) {
                escolha = sc.nextInt();
                if (escolha >= 0 && escolha <= playlists.size()) {
                    break;
                } else {
                    System.out.print("Número inválido! Tente novamente: ");
                }
            } else {
                System.out.print("Entrada inválida! Tente novamente: ");
                sc.next();
            }
        }
        return escolha;
    }

    public static boolean menuConfirmarPublica() {
        clear();
        System.out.print(ANSI_GREEN + "Tornar playlist pública? (s/n): " + ANSI_RESET);
        Scanner sc = new Scanner(System.in);
        String resposta = sc.nextLine().trim().toLowerCase();
        return resposta.startsWith("s");
    }


    /** Imprime a mensagem e espera 3s antes de continuar */
    public static void printlnPausado(String msg) {
        System.out.println(msg);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    /** Mostra o prompt e devolve a linha lida pelo utilizador */
    public static String lerLinha(String prompt) {
        System.out.print(prompt);
        return new Scanner(System.in).nextLine().trim();
    }

    public static void pressEnterToContinue() {
        System.out.println(ANSI_GREEN + "Pressione Enter para continuar..." + ANSI_RESET);
        new Scanner(System.in).nextLine();
    }

    // Função para limpar a tela
    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
