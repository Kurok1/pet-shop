package indi.petshop.consumer.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.20
 */
@Component("userOrderServerPoint")
@ServerEndpoint("/user/order/{userId}")
public class UserOrderServerPoint {
    private static Logger logger= LoggerFactory.getLogger(UserOrderServerPoint.class);

    private static Map<String, Session> userMap=new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("userId") String userId,Session session){
        userMap.put(userId, session);
        logger.info("用户：" + userId + "已经连接成功");
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId){
        userMap.remove(userId);
        logger.info("用户：" + userId + "已经断开连接");
    }

    public void sendMessage(String userId,String message){
        Session session=userMap.get(userId);
        RemoteEndpoint.Basic sender = session.getBasicRemote();
        try {
            sender.sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
