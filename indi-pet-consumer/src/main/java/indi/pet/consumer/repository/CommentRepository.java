package indi.pet.consumer.repository;

import indi.pet.consumer.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@Repository("commentRepository")
public interface CommentRepository extends ElasticsearchRepository<Comment,String> {

    Page<Comment> findByMessage(String message, Pageable pageable);

}
