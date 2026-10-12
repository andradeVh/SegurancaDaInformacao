package ads.ifsc;
import java.util.List;

public interface UserRepository {
    boolean save(User user);
    boolean update(User user);
    User findByLogin(String login);
    List<User> findAll();
}
