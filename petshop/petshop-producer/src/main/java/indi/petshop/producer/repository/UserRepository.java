package indi.pet.producer.repository;

import indi.pet.producer.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.20
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User,String> {
}
