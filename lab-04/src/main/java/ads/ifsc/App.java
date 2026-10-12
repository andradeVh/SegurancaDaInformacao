package ads.ifsc;

import java.util.HashMap;
import java.util.List;

public class App {
    private UserService service;
    // private String hashAlgorithm;

    public App(String hashAlg) {
        UserRepository repository = new InMemory();
        this.service = new UserService(repository, hashAlg);
    }

    public void displayMenu() {
        boolean menu = true;

        while (menu) {
            IO.println("Menu principal:");
            IO.println("1. Cadastrar:");
            IO.println("2. Atualizar senha:");
            IO.println("3. Autenticar:");
            IO.println("4. Listar todos os usuários:");
            IO.println("0. Sair:");

            String opcao = IO.readln();

            switch (opcao) {
                case "1":
                    // register
                    IO.println("Digite o login:");
                    String loginRegister = IO.readln();
                    IO.println("Digite a senha:");
                    String passwordRegister = IO.readln();

                    try {
                        service.register(loginRegister, passwordRegister);
                        IO.println("Usuário cadastrado com sucesso!");
                    } catch (UserAlreadyExistsException e) {
                        IO.print(e.getMessage());
                    }
                    break;
                case "2":
                    // updatePassword
                    IO.println("Digite o login:");
                    String login = IO.readln();
                    IO.println("Digite a senha:");
                    String currentPassword = IO.readln();
                    IO.println("Digite a nova senha:");
                    String newPassword = IO.readln();

                    try {
                        service.updatePassword(login, currentPassword, newPassword);
                        IO.println("Senha atualizada com sucesso!");
                    } catch (UserNotFoundException e) {
                        IO.print(e.getMessage());
                    } catch (InvalidPasswordException e) {
                        IO.print(e.getMessage());
                    }

                    break;
                case "3":
                    // authenticate
                    IO.println("Digite o login:");
                    String loginAuth = IO.readln();
                    IO.println("Digite a senha:");
                    String passwordAuth = IO.readln();

                    try {
                        service.authenticate(loginAuth, passwordAuth);
                        IO.println("Autenticação realizada com sucesso!");
                    } catch (InvalidLoginException e) {
                        IO.print(e.getMessage());
                    }
                    break;
                case "4":
                    // list all
                    List<User> usuario = service.listAll();

                    if (usuario.isEmpty() || usuario == null) {
                        IO.println("nenhum usuário cadastrado");
                    } else {
                        IO.println("usuários cadastrados:");
                        for (User user : usuario) {
                            IO.println("Login: " + user.getLogin());
                        }
                    }
                    
                    break;
                case "0":
                    menu = false;
                    break;
                default:
                    // erro
                    IO.print("Opção inválida");
            }

        }
    }

    public static void main(String[] args) {
        // App app = new App();
        // app.displayMenu();

        IO.println("Qual algoritmo de hash gostaria de usar?");
        IO.println("1. PBKDF2");
        IO.println("2. BCrypt");
        IO.println("0. Sair:");
        String opcao2 = IO.readln();
        App app = null;

        switch (opcao2) {
            case "1":
                app = new App("PBKDF2");
                break;
            case "2":
                app = new App("BCrypt");
                break;
            case "0":
                return;
            default:
                IO.print("Opção invalida. Utilizando BCrypt por padrão");
                app = new App("BCrypt");
        }

        app.displayMenu();

    }
}
