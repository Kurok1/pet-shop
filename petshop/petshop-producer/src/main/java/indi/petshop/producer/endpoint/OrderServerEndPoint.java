package indi.pet.producer.endpoint;

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
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.20
 */
@Component("orderServerEndPoint")
@ServerEndpoint(value = "/server/order/{keeperId}")
public class OrderServerEndPoint {

    private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    private static Logger logger = LoggerFactory.getLogger(OrderServerEndPoint.class);

    @OnOpen
    public void onOpen(@PathParam("keeperId") String keeperId, Session session) {
        sessionMap.put(keeperId, session);
        logger.info("商家：" + keeperId + "已经连接成功");
    }

    @OnClose
    public void onClose(@PathParam("keeperId") String keeperId) {
        sessionMap.remove(keeperId);
        logger.info("商家：" + keeperId + "已经断开连接");
    }

    /**
     * 作为方法暴露给其他程序，用于向商家发送消息
     *
     * @param keeperId
     * @param message
     */
    public void sendToKeeper(String keeperId, String message) {
        Session session = sessionMap.get(keeperId);
        RemoteEndpoint.Basic sender = session.getBasicRemote();
        try {
            sender.sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
