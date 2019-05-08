package indi.pet.consumer.repository;

import indi.pet.consumer.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.24
 */
@Repository("userRepository")
public interface UserRepository extends ElasticsearchRepository<User,String> {

    public User findByUsernameAndPassword(String username,String password);

    public User findByUsername(String username);
}
