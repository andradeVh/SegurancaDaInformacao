package ads.ifsc;

public class UserService {
    private InMemory repository;
    private String hashAlg;

    public UserService(UserRepository db, String hashAlg) {
        this.repository = db;
        this.hashAlg = hashAlg;
    }

    public UserService(UserRepository db) {
        this.repository = db;
        this.hashAlg = "bcrypt";
    }

    private boolean toHash(String password) {
        char[] charPassword = password.toCharArray();
        if (hashAlg.toLowerCase().equals("bcrypt")) {
            PasswordHashing.hashPasswordWithBCrypt(charPassword);
        } else if (hashAlg.toLowerCase().equals("pbkdf2")) {
            PasswordHashing.hashPasswordWithPBKDF2(charPassword);
        }
        return true;
    }

    public boolean register(String login, String password) {
        if (repository.findByLogin(login) != null) {
            throw new UserAlreadyExistsException("usuario ja cadastrado");
        }

        // fazer o hash e depois fazer o save
        char[] charPassword = password.toCharArray();

        // if (hashAlg == "brycpt")
        toHash(password);

        repository.save(new User(login, new String(charPassword)));

        return true;
    }

    public boolean updatePassword(String login, String currentPassword, String newPassword) {
        // UserNotFoundException
        if (repository.findByLogin(login) == null) {
            throw new UserNotFoundException("usuario nao encontrado");
        }
        // InvalidPasswordException
        if (repository.findByLogin(login).getPassword().equals(currentPassword)) {
            throw new InvalidPasswordException("senha invalida");
        }

        if (currentPassword.equals(newPassword)) {
            throw new InvalidPasswordException("senha invalida");
        }


        char[] charPassword = newPassword.toCharArray();

        toHash(newPassword);
 
        repository.update(new User(login, newPassword));
        return true;
    }


    public boolean authenticate(String login, String password) {
        // UserNotFoundException
        if (repository.findByLogin(login) == null) {
            throw new UserNotFoundException("usuario nao encontrado");
        }
        // InvalidPasswordException
        if (repository.findByLogin(login).getPassword().equals(password)) {
            throw new InvalidPasswordException("senha invalida");
        }
        return true;
    }
}
