package indi.pet.chatting.repository;

import indi.pet.chatting.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.05.02
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User,String> {

}
