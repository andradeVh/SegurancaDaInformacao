package ads.ifsc;

public class UserService {
    private InMemory repository;

    public UserService(UserRepository db, String hashAlg){

    }

    public boolean register(String login, String password){
        // if (usuario ja cadastrado) {
        //    throw new UserAlreadyExistsException("usuario ja cadastrado");
        // }

        // fazer o hash e depois fazer o save
        char[] charPassword = password.toCharArray();

        PasswordHashing.hashPasswordWithBCrypt(charPassword);


    }

    public boolean updatePassword(String login, String currentPassword, String newPassword){
        // UserNotFoundException
    }

    public boolean authenticate(String login, String password){
        // InvalidPasswordException
    }
}
