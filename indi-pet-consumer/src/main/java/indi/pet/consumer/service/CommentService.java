package indi.pet.consumer.service;

import indi.pet.consumer.domain.Comment;
import indi.pet.consumer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@Service("commentService")
public class CommentService {

    private CommentRepository commentRepository;

    public CommentRepository getCommentRepository() {
        return commentRepository;
    }

    @Autowired
    public void setCommentRepository(@Qualifier("commentRepository") CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment){
        return getCommentRepository().save(comment);
    }
}
