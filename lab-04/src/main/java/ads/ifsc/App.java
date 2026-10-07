package ads.ifsc;

import java.util.HashMap;

public class App {
    private UserService service;
    private String hashAlgorithm;

    public App(String hashAlg) {
        this.service = new UserService(repository, hashAlg);
    }

    public void displayMenu(){
        IO.print("Menu principal:");
        IO.print("1. Cadastrar:");
        IO.print("2. Atualizar senha:");
        IO.print("3. Autenticar:");
        IO.print("0. Sair:");

        int opcao = IO.readln();

        switch(opcao){
            case 1:
                // register
                IO.print("Digite o seu nome:");
                String nome = IO.readln();
                IO.print("Digite o seu sobrenome:");
                String sobrenome = IO.readln();

                break;
            case 2:
                // listar
                break;
            case 3:
                // update
                break;
            case 0:
                break;
            default:
                // erro
        }
    }

    private void cadastrar(String login, String password, String hashAlgorithm){

    }

    public static void main(String[] args) {
        App app = new App();
        app.displayMenu();

        IO.print("Qual algoritmo de hash gostaria de usar?");
        IO.print("1. PBKDF2");
        IO.print("2. BCrypt");
        IO.print("0. Sair:");
        String opcao2 = IO.readln();
        switch(opcao2){
            case "1":
                App app = new App("PBKDF2");
                break;
            case "2":
                break;
            case "0":
                break;
        }

    }
}
