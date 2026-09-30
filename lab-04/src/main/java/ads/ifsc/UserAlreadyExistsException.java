package ads.ifsc;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String mensagem) {
        super(mensagem);
    }
}
