package indi.petshop.consumer.service;

import indi.petshop.consumer.domain.User;
import indi.petshop.consumer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.24
 */
@Transactional
@Service("consumerUserService")
public class UserService {

    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(@Qualifier("consumerUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user){
        return getUserRepository().save(user);
    }

    public User getOne(String id){
        return getUserRepository().findOne(id);
    }

    public Iterable<User> getAll(Iterable<String> ids){
        return getUserRepository().findAll(ids);
    }

    public User login(String username,String password){
        return getUserRepository().findByUsernameAndPassword(username, password);
    }

    public boolean exist(String username){
        return getUserRepository().findByUsername(username)!=null;
    }

    public void delete(String id){
        getUserRepository().delete(id);
    }
}
