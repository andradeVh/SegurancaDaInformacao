package ads.ifsc;

import java.security.SecureRandom;

public class UserService {
    private UserRepository repository;
    private String hashAlg;
    private final SecureRandom S_RANDOM = new SecureRandom();

    public UserService(UserRepository db, String hashAlg) {
        this.repository = db;
        this.hashAlg = hashAlg;
    }

    public UserService(UserRepository db) {
        this.repository = db;
        this.hashAlg = "bcrypt";
    }

    private String toHash(char[] password) {
        if ("pbkdf2".equalsIgnoreCase(hashAlg)) {
            // precisa gerar salt
            try {
                byte[] salt = new byte[16];
                S_RANDOM.nextBytes(salt);

                /*
                 * int iterations = 210000;
                 * int keyLength = 128;
                 * String pbkdf2Algorithm = "PBKDF2WithHmacSHA512";
                 */
                byte[] hash = PasswordHashing.hashPasswordWithPBKDF2(password, salt, "PBKDF2WithHmacSHA512", 210000,
                        128);

                // precisa armazenar o hash junto com o salt
                // converte os dois pra base64
                String saltBase64 = java.util.Base64.getEncoder().encodeToString(salt);
                String hashBase64 = java.util.Base64.getEncoder().encodeToString(hash);

                // concatena os dois com "$" no meio
                return saltBase64 + "$" + hashBase64;
            } catch (Exception e) {
                throw new RuntimeException("Erro ao gerar hash da senha", e);
            }

        } else {
            byte[] hash = PasswordHashing.hashPasswordWithBCrypt(password);
            return new String(hash);
        }
    }

    private boolean verifyPassword(char[] password, String hashedPassword) {
        if ("pbkdf2".equalsIgnoreCase(hashAlg)) {
            try {
                // separa o salt do hash
                String[] parts = hashedPassword.split("\\$"); // no regex $ é caracter de fim de linha
                byte[] salt = java.util.Base64.getDecoder().decode(parts[0]);
                byte[] hash = java.util.Base64.getDecoder().decode(parts[1]);

                // faz a verificação do hash com o mesmo salt extraido do armazenamento
                return PasswordHashing.verifyPasswordWithPBKDF2(password, salt, "PBKDF2WithHmacSHA512", 210000, 128,
                        hash);
            } catch (Exception e) {
                return false;
            }
        } else {
            return PasswordHashing.verifyPasswordWithBCrypt(password, hashedPassword.getBytes());
        }
    }

    public boolean register(String login, String password) {
        if (repository.findByLogin(login) != null) {
            throw new UserAlreadyExistsException("usuario ja cadastrado");
        }

        // fazer o hash e depois fazer o save
        char[] charPassword = password.toCharArray();

        // if (hashAlg == "brycpt")
        String hashedPassword = toHash(charPassword);

        repository.save(new User(login, hashedPassword));

        return true;
    }

    public boolean updatePassword(String login, String currentPassword, String newPassword) {
        User user = repository.findByLogin(login);

        if (user == null) {
            throw new UserNotFoundException("usuario nao encontrado");
        }

        char[] charCurrentPassword = currentPassword.toCharArray();
        char[] charNewPassword = newPassword.toCharArray();

        if (!verifyPassword(charCurrentPassword, user.getPassword())) {
            throw new InvalidPasswordException("senha atual invalida");
        }

        if (currentPassword.equals(newPassword)) {
            throw new InvalidPasswordException("senha nova nao pode ser igual a senha atual");
        }

        String hashedNewPassword = toHash(charNewPassword);
        repository.update(new User(login, hashedNewPassword));
        return true;
    }

    public boolean authenticate(String login, String password) {
        User user = repository.findByLogin(login);

        // InvalidLoginException
        if (user == null) {
            throw new InvalidLoginException("login ou senha invalido");
        }
        char[] charPassword = password.toCharArray();

        if (!verifyPassword(charPassword, user.getPassword())) {
            throw new InvalidLoginException("login ou senha invalido");
        }
        return true;
    }
}
