package indi.pet.consumer.controller;

import indi.pet.consumer.domain.Comment;
import indi.pet.consumer.exception.TokenExpiredException;
import indi.pet.consumer.service.CommentService;
import indi.pet.consumer.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    public CommentService getCommentService() {
        return commentService;
    }

    @Autowired
    public void setCommentService(@Qualifier("commentService") CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/publish")
    public Map<String,Object> publish(@RequestParam("token")String token, @RequestBody Comment comment){
        Map<String,Object> map=new HashMap<>();
        if(TokenUtil.validate(token)){
            Comment save = getCommentService().save(comment);
            map.put("flag",true);
            map.put("message","发表评论成功~");
            map.put("data",save);
        }else throw new TokenExpiredException();
        return map;
    }
}
