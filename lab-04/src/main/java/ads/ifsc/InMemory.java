package ads.ifsc;

import java.util.HashMap;

public class InMemory implements UserRepository {
    private HashMap<String, User> dados;

    public InMemory(HashMap<String, User> dados) {
        this.dados = dados;
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
        dados.remove(login);
        dados.put(login, user);
        return true;
    }

    public User findByLogin(String login){
        return dados.get(login);
    }
}
