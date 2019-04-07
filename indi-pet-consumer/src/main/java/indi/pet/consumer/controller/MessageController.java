package indi.pet.consumer.controller;

import indi.pet.consumer.domain.Comment;
import indi.pet.consumer.domain.Message;
import indi.pet.consumer.domain.User;
import indi.pet.consumer.exception.TokenExpiredException;
import indi.pet.consumer.service.CommentService;
import indi.pet.consumer.service.MessageService;
import indi.pet.consumer.service.UserService;
import indi.pet.consumer.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    private MessageService messageService;

    public MessageService getMessageService() {
        return messageService;
    }

    @Autowired
    public void setMessageService(@Qualifier("messageService") MessageService messageService) {
        this.messageService = messageService;
    }

    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private CommentService commentService;

    public CommentService getCommentService() {
        return commentService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/publish")
    public Map<String,Object> publish(@RequestParam("token")String token, @RequestBody Message message){
        message.setTimestamp(System.currentTimeMillis()/1000);
        Map<String,Object> map=new HashMap<>();
        if(TokenUtil.validate(token)){
            Message save = getMessageService().save(message);
            map.put("flag",true);
            map.put("message","发布成功");
            map.put("result",save);
        }else throw new TokenExpiredException();

        return map;
    }

    @GetMapping("/get")
    public Map<String,Object> get(@RequestParam("token")String token,@RequestParam("currentPage")int currentPage){
        Map<String,Object> map=new HashMap<>();
        User user = TokenUtil.validateToken(token);
        if(user!=null){
            //获取好友列表
            Set<String> friends=user.getFriends();
            friends.add(user.getId());
            map.put("flag",true);
            map.put("message","OK~");
            List<Message> messages=getMessageService().findByUserIn(friends,currentPage-1);
            List<Map<String,Object>> data=new ArrayList<>();
            for (Message message:messages){
                Map<String,Object> item=new HashMap<>();
                item.put("content",message.getContent());
                item.put("imgs",message.getImgs());
                item.put("id",message.getId());
                item.put("title",message.getTitle());
                User createUser=getUserService().getOne(message.getUser());
                item.put("username",createUser.getUsername());
                item.put("logo",createUser.getLogo());
                item.put("timestamp",message.getTimestamp());
                data.add(item);
            }
            map.put("data",data);
            map.put("hasNext",currentPage*10<getMessageService().countByUserIn(friends));
        }else throw new TokenExpiredException();
        return map;
    }

    @GetMapping("/{messageId}")
    public Map<String,Object> tree(@RequestParam("token")String token,@PathVariable("messageId")String messageId){
        Map<String,Object> map=new HashMap<>();
        if(TokenUtil.validate(token)){
            map.put("comments",analysisComment(getCommentService().findByMessageId(messageId)));
            Message message=getMessageService().getOne(messageId);
            Map<String,Object> messageData=new HashMap<>();
            messageData.put("timestamp",message.getTimestamp());
            messageData.put("content",message.getContent());
            User one = getUserService().getOne(message.getUser());
            messageData.put("logo",one.getLogo());
            messageData.put("username",one.getUsername());
            messageData.put("title",message.getTitle());
            messageData.put("imgs",message.getImgs());
            map.put("message",messageData);
            map.put("flag",true);
        }else throw new TokenExpiredException();
        return map;
    }

    private List<Map<String,Object>> analysisComment(List<Comment> comments){
        if (comments!=null&&comments.size()>0){
            List<Map<String,Object>> list=new LinkedList<>();
            Set<Comment> parents=new HashSet<>();
            Set<Comment> childrens=new HashSet<>();
            comments.forEach(
                    comment -> {
                        if(comment.getParent()==null)
                            parents.add(comment);
                        else
                            childrens.add(comment);
                    }
            );
            for (Comment item:parents){
                Map<String,Object> data=new HashMap<>();
                data.put("logo",getUserService().getOne(item.getUserId()).getLogo());
                data.put("content",item.getContent());
                data.put("id",item.getId());
                List<Map<String,Object>> ch=new LinkedList<>();
                childrens.forEach(
                        children->{
                            if(item.getId().equals(children.getParent())){
                                Map<String,Object> map=new HashMap<>();
                                map.put("content",children.getContent());
                                map.put("logo",getUserService().getOne(children.getUserId()).getLogo());
                                ch.add(map);
                            }
                        }
                );
                data.put("children",ch);
                list.add(data);
            }
            return list;
        }else return null;
    }
}
