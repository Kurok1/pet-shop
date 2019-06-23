package indi.petshop.consumer.repository;

import indi.petshop.consumer.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Repository("consumerMessageRepository")
public interface MessageRepository extends ElasticsearchRepository<Message,String> {

    public Page<Message> findByUserInOrderByTimestampDesc(Iterable<String> users, Pageable pageable);

    public Long countByUserIn(Iterable<String> users);
}
