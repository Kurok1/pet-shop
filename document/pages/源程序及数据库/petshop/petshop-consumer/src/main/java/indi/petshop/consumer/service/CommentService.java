package indi.petshop.consumer.service;

import indi.petshop.consumer.domain.Comment;
import indi.petshop.consumer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@Service("consumerCommentService")
@Transactional
public class CommentService {

    private CommentRepository commentRepository;

    public CommentRepository getCommentRepository() {
        return commentRepository;
    }

    @Autowired
    public void setCommentRepository(@Qualifier("consumerCommentRepository") CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment){
        return getCommentRepository().save(comment);
    }

    public List<Comment> findByMessageId(String messageId){
        return getCommentRepository().findByMessage(messageId);
    }
}
