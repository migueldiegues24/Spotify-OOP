package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GestorUtilizadores implements Serializable {
    private Map<String, Utilizador> utilizadores; // Mapeamento de utilizador por email

    // Construtor
    public GestorUtilizadores() {
        this.utilizadores = new HashMap<>();
    }

    // Na classe GestorUtilizadores
    public Map<String, Utilizador> getUtilizadores() {
        return this.utilizadores; // Retorna o mapa de utilizadores
    }

    public void setUtilizador(Utilizador u) {
        if (this.utilizadores.containsKey(u.getEmail())) {
            System.out.println("Utilizador com email '" + u.getEmail() + "' já existe.");
        } else {
            this.utilizadores.put(u.getEmail(), u); // Corrigido aqui
            System.out.println("Utilizador '" + u.getNome() + "' adicionado com sucesso.");
        }
    }

    // Adicionar um utilizador ao gestor
    public void adicionarUtilizador(Utilizador u) {
        if (!utilizadores.containsKey(u.getEmail())) {
            utilizadores.put(u.getEmail(), u);
            System.out.println("Utilizador '" + u.getNome() + "' adicionado com sucesso.");
        } else {
            System.out.println("Utilizador com o email '" + u.getEmail() + "' já existe.");
        }
    }

    // Obter utilizador por email
    public Utilizador getUtilizadorByEmail(String email) {
        return utilizadores.get(email);
    }

    // Verificar se um utilizador existe pelo email
    public boolean verificarEmail(String email) {
        return utilizadores.containsKey(email);
    }

    // Atualizar plano de subscrição do utilizador
    public void atualizarPlano(Utilizador u, PlanoSubscricao novoPlano) {
        u.setPlano(novoPlano); // Delegando ao Utilizador a responsabilidade de atualizar seu plano
        System.out.println("Plano de " + u.getNome() + " alterado para: " + novoPlano.getClass().getSimpleName());
    }

    // Método para exibir todos os utilizadores (opcional)
    public void exibirUtilizadores() {
        if (utilizadores.isEmpty()) {
            System.out.println("Nenhum utilizador registrado.");
        } else {
            utilizadores.values().forEach(u -> System.out.println(u.getNome() + " (" + u.getEmail() + ")"));
        }
    }

    // Método de login - retorne o utilizador ou null
    public Utilizador login(String email, String senha) {
        Utilizador u = getUtilizadorByEmail(email);
        if (u != null && u.getPassword().equals(senha)) {
            System.out.println("Login bem-sucedido! Bem-vindo " + u.getNome());
            return u; // Retorna o utilizador autenticado
        } else {
            System.out.println("Email ou senha incorretos.");
            return null;
        }
    }

    public void registrarUtilizador(String dados, int tipoPlano) {
        String[] dadosArray = dados.split(";");
        String nome = dadosArray[0];
        String email = dadosArray[1];
        String morada = dadosArray[2];
        String password = dadosArray[3];
        
        // Criar o utilizador
        Utilizador novoUtilizador = new Utilizador(nome, morada, email, password, 0);
        
        // Definindo o plano diretamente aqui
        PlanoSubscricao plano;
        switch (tipoPlano) {
            case 1: plano = new PlanoFree(); break;
            case 2: plano = new PlanoPremiumBase(); break;
            case 3: plano = new PlanoPremiumTop(); break;
            default: plano = new PlanoFree(); break;  // Default para o caso de entrada inválida
        }
        novoUtilizador.setPlano(plano);
        
        // Adicionando o utilizador ao gestor
        this.utilizadores.put(novoUtilizador.getEmail(), novoUtilizador);
        System.out.println("Utilizador " + nome + " registrado com sucesso!");
    }
    
    
    
}
