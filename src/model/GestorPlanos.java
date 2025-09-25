package model;

public class GestorPlanos {

    // Método para consultar os planos disponíveis
    public static void consultarPlanosDisponiveis() {
        System.out.println("----------- PLANOS DISPONÍVEIS -----------");
        System.out.println("1) Plano Free  - Pontos por música: 5");
        System.out.println("2) Plano Premium Base - Pontos por música: 10");
        System.out.println("3) Plano Premium Top  - Pontos por música: 100 + 2.5% dos pontos atuais");
        System.out.println("------------------------------------------");
    }

    // Método para ver o plano atual de um utilizador
    public static void verPlanoAtual(Utilizador u) {
        PlanoSubscricao plano = u.getPlano();
        if (plano != null) {
            System.out.println("Plano atual: " + plano.getNome() +
                    " (Pontos por música: " + plano.getPontosPorMusica() + ")");
        } else {
            System.out.println("O seu plano não permite ver essa informação.");
        }
    }

    // Método para alterar o plano atual de um utilizador
    public static void alterarPlanoAtual(Utilizador u, int opcao) {
        PlanoSubscricao novoPlano = switch (opcao) {
            case 1 -> new PlanoFree();
            case 2 -> new PlanoPremiumBase();
            case 3 -> new PlanoPremiumTop();
            default -> null;
        };

        if (novoPlano != null) {
            u.setPlano(novoPlano);  // Delegando para o Utilizador a alteração de seu plano
            System.out.println("Plano alterado com sucesso para: " + novoPlano.getNome());
        } else {
            System.out.println("Opção inválida. Plano não alterado.");
        }
    }
}
