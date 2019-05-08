package indi.petshop.consumer.repository;

import indi.petshop.consumer.domain.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@Repository("consumerCommentRepository")
public interface CommentRepository extends ElasticsearchRepository<Comment,String> {

    List<Comment> findByMessage(String message);

}
