package indi.pet.chatting.controller;

import indi.pet.chatting.endpoint.ChatEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.27
 */
@RestController
public class ChatController {

    private ChatEndPoint chatEndPoint;

    public ChatEndPoint getChatEndPoint() {
        return chatEndPoint;
    }

    @Autowired
    public void setChatEndPoint(ChatEndPoint chatEndPoint) {
        this.chatEndPoint = chatEndPoint;
    }

    @PostMapping("/chat/{connectType}/{fromId}/{toId}")
    public Map<String, Object> connected(@PathVariable("fromId") String fromId, @PathVariable("toId") String toId, @PathVariable("connectType") int type) {
        Map<String, Object> map = new HashMap<>();
        boolean flag = false;
        switch (type) {
            case 1: {//普通用户之间互连
                flag = getChatEndPoint().userConnectToUser(fromId, toId);
                map.put("flag", flag);
                String str = flag ? "连接成功" : "连接失败";
                map.put("message", str);
            }
            ;
            break;
            case 2: {//普通用户和商家之间互连
                flag = getChatEndPoint().userConnectToKeeper(fromId, toId);
                map.put("flag", flag);
                String str = flag ? "连接成功" : "连接失败";
                map.put("message", str);
            }
            ;
            break;
            case 3: {//商家和普通用户之间互连
                flag = getChatEndPoint().userConnectToKeeper(toId, fromId);
                map.put("flag", flag);
                String str = flag ? "连接成功" : "连接失败";
                map.put("message", str);
            }
            ;
            break;
            default: {
                map.put("flag", flag);
                map.put("message", "连接出错");
            }
        }
        return map;
    }
}
