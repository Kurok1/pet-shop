package indi.petshop.chatting.repository;

import indi.petshop.chatting.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.05.02
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User,String> {
    Stream<User> findByIdIn(Iterable<String> ids);
}
