package ads.ifsc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemory implements UserRepository {
    private Map<String, User> dados;

    public InMemory() {
        this.dados = new HashMap<>();
    }

    public InMemory(Map<String, User> dados) {
        this.dados = dados != null ? dados : new HashMap<>();
    }

    public boolean save(User user){
        String login = user.getLogin();
        if(dados.containsKey(login)){
            return false;
        }
        dados.put(login, user);
        return true;
    }
    public boolean update(User user){
        String login = user.getLogin();
        if(!dados.containsKey(login)){
            return false;
        }
        dados.put(login, user);
        return true;
    }

    public User findByLogin(String login){
        return dados.get(login);
    }

    public List<User> findAll(){
        return new ArrayList<>(dados.values());
    }
}
