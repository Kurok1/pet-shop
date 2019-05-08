package indi.petshop.consumer.controller;

import indi.petshop.consumer.domain.Comment;
import indi.petshop.consumer.domain.User;
import indi.petshop.consumer.exception.TokenExpiredException;
import indi.petshop.consumer.service.CommentService;
import indi.petshop.consumer.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@RestController("consumerCommentController")
@RequestMapping("/consumer/comment")
public class CommentController {

    private CommentService commentService;

    public CommentService getCommentService() {
        return commentService;
    }

    @Autowired
    public void setCommentService(@Qualifier("consumerCommentService") CommentService commentService) {
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
