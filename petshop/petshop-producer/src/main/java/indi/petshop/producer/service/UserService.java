package indi.petshop.producer.service;

import indi.petshop.producer.domain.User;
import indi.petshop.producer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.20
 */
@Service("producerUserService")
@Transactional
public class UserService {

    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(@Qualifier("producerUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(String id){
        return getUserRepository().findOne(id);
    }
}
