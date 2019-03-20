package indi.pet.consumer.controller;

import indi.pet.consumer.domain.Message;
import indi.pet.consumer.exception.TokenExpiredException;
import indi.pet.consumer.service.MessageService;
import indi.pet.consumer.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping("/publish")
    public Map<String,Object> publish(@RequestParam("token")String token, @RequestBody Message message){
        Map<String,Object> map=new HashMap<>();
        if(TokenUtil.validate(token)){
            Message save = getMessageService().save(message);
            map.put("flag",true);
            map.put("message","发布成功");
            map.put("result",save);
        }else throw new TokenExpiredException();

        return map;
    }


}
