package indi.pet.consumer.controller;

import indi.pet.consumer.domain.Comment;
import indi.pet.consumer.domain.User;
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
        comment.setTimestamp(System.currentTimeMillis()/1000);
        Map<String,Object> map=new HashMap<>();
        User user=TokenUtil.validateToken(token);
        if(user!=null){
            Comment save = getCommentService().save(comment);
            Map<String,Object> data=new HashMap<>();
            data.put("logo",user.getLogo());
            data.put("content",save.getContent());
            data.put("id",save.getId());
            map.put("flag",true);
            map.put("message","发表评论成功~");
            map.put("data",data);
        }else throw new TokenExpiredException();
        return map;
    }
}
