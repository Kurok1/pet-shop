package indi.pet.consumer.service;

import indi.pet.consumer.domain.User;
import indi.pet.consumer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.24
 */
@Transactional
@Service("userService")
public class UserService {

    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(@Qualifier("userRepository") UserRepository userRepository) {
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
}
