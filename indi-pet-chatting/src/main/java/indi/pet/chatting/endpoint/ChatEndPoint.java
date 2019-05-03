package indi.pet.chatting.endpoint;

import indi.pet.chatting.entity.*;
import indi.pet.chatting.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.27
 */
@Component
@ServerEndpoint("/socket/{type}/{id}")
public class ChatEndPoint {

    private static Logger logger = LoggerFactory.getLogger(ChatEndPoint.class);

    private final static ConcurrentHashMap<String, LinkedList<SessionEntity>> userChatList = new ConcurrentHashMap<>();

    private final static ConcurrentHashMap<String, LinkedList<SessionEntity>> keeperChatList = new ConcurrentHashMap<>();

    private final static CopyOnWriteArraySet<SessionEntity> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(@PathParam("id") String id, @PathParam("type") int type, Session session) {
        boolean isUser;
        if (type == Type.USER)
            isUser = true;
        else if (type == Type.KEEPER)
            isUser = false;
        else return;
        if (isUser) {
            logger.info("用户： " + id + "已经成功连接");
            afterUserConnected(id, session);
        } else {
            logger.info("商家： " + id + "已经成功连接");
            afterKeeperConnected(id, session);
        }
    }

    private void afterUserConnected(String id, Session session) {
        if (!userChatList.containsKey(id))
            userChatList.put(id, new LinkedList<>());
        registerSession(id, true, session);
    }

    private void afterKeeperConnected(String id, Session session) {
        if (!keeperChatList.containsKey(id))
            keeperChatList.put(id, new LinkedList<>());
        registerSession(id, false, session);
    }

    private void registerSession(String id, boolean type, Session session) {
        SessionEntity entity = null;
        if (type)
            entity = new UserSessionEntity();
        else entity = new KeeperSessionEntity();
        entity.setId(id);
        entity.setSession(session);
        entity.setLastTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd : hh:mm:ss")));
        sessions.add(entity);
    }

    private SessionEntity findSessionEntity(String id, int type) {
        Optional<SessionEntity> optional = sessions.stream().filter(
            sessionEntity -> sessionEntity.getType() == type && id.equals(sessionEntity.getId())
        ).findFirst();
        return optional.orElse(null);
    }

    private SessionEntity findSessionEntity(List<SessionEntity> list, String id, int type) {
        Optional<SessionEntity> optional = list.stream().filter(
            sessionEntity -> sessionEntity.getType() == type && id.equals(sessionEntity.getId())
        ).findFirst();
        return optional.orElse(null);
    }

    private void closeSession(SessionEntity sessionEntity) {
        try {
            if(sessionEntity.getSession().isOpen())
                sessionEntity.getSession().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sessions.remove(sessionEntity);
    }

    @OnClose
    public void onClose(@PathParam("id") String id, @PathParam("type") int type) {
        boolean isUser;
        if (type == Type.USER)
            isUser = true;
        else if (type == Type.KEEPER)
            isUser = false;
        else return;
        if (isUser) {
            logger.info("用户： " + id + "已经断开连接");
        } else {
            logger.info("商家： " + id + "已经断开连接");
        }
        afterLeaved(id, type);

    }

    private void afterLeaved(String id, int type) {
        //关闭session
        closeSession(findSessionEntity(id, type));
        //移除对应的列表
        if (type == Type.USER)
            userChatList.remove(id);
        else if (type == Type.KEEPER)
            keeperChatList.remove(id);
    }

    @OnMessage
    public void onMessage(String message) {
        Message messageEntity = MessageUtil.toMessage(message);
        messageEntity.setTimestamp(System.currentTimeMillis()/1000);
        if (messageEntity.getSenderType() == Type.USER) {//用户发起
            LinkedList<SessionEntity> list = userChatList.get(messageEntity.getSender());
            //查找对应的session
            if (list == null) {//对方已经下线
                notifyLeaved(messageEntity.getSender(), Type.USER);
                return;
            }
            SessionEntity entity = findSessionEntity(list, messageEntity.getReceiver(), messageEntity.getReceiverType());
            entity.setLastTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd : hh:mm:ss")));
            sendMessage(messageEntity, entity);
        } else if (messageEntity.getSenderType() == Type.KEEPER) {//商家发起
            LinkedList<SessionEntity> list = keeperChatList.get(messageEntity.getSender());
            if (list == null) {//对方已经下线
                notifyLeaved(messageEntity.getSender(), Type.KEEPER);
                return;
            }
            SessionEntity entity = findSessionEntity(list, messageEntity.getReceiver(), messageEntity.getReceiverType());
            entity.setLastTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd : hh:mm:ss")));
            sendMessage(messageEntity, entity);
        }
    }

    private void sendMessage(Message message, SessionEntity sessionEntity) {

        if (sessionEntity != null && sessionEntity.isOpen()) {
            Session session = sessionEntity.getSession();
            RemoteEndpoint.Basic basic = session.getBasicRemote();
            try {
                basic.sendText(MessageUtil.toString(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyLeaved(String id, int type) {
        SessionEntity sessionEntity = findSessionEntity(id, type);
        if (sessionEntity != null && sessionEntity.isOpen()) {
            Session session = sessionEntity.getSession();
            RemoteEndpoint.Basic basic = session.getBasicRemote();
            try {
                basic.sendText(MessageUtil.toString(new LeaveInfo()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean userConnectToUser(String fromId, String toId) {
        UserSessionEntity from = (UserSessionEntity) findSessionEntity(fromId, Type.USER);
        UserSessionEntity to = (UserSessionEntity) findSessionEntity(toId, Type.USER);
        if (from == null && to == null)
            return false;
        LinkedList<SessionEntity> fromList = userChatList.get(fromId);
        LinkedList<SessionEntity> toList = userChatList.get(toId);

        synchronized (this) {//并发控制
            SessionEntity entity = findSessionEntity(fromList, toId, Type.USER);
            if (entity == null || !entity.isOpen()) {
                fromList.remove(entity);
                fromList.add(to);
                userChatList.put(fromId, fromList);
            }
            entity = findSessionEntity(toList, fromId, Type.USER);
            if (entity == null || !entity.isOpen()) {
                toList.remove(entity);
                toList.add(from);
                userChatList.put(toId, toList);
            }
        }

        return true;
    }

    public boolean userConnectToKeeper(String fromId, String toId) {
        UserSessionEntity from = (UserSessionEntity) findSessionEntity(fromId, Type.USER);
        KeeperSessionEntity to = (KeeperSessionEntity) findSessionEntity(toId, Type.KEEPER);
        if (from == null && to == null)
            return false;
        LinkedList<SessionEntity> fromList = userChatList.get(fromId);
        LinkedList<SessionEntity> toList = keeperChatList.get(toId);

        synchronized (this) {//并发控制
            SessionEntity entity = findSessionEntity(fromList, toId, Type.KEEPER);
            if (entity == null || !entity.isOpen()) {
                fromList.remove(entity);
                fromList.add(to);
                userChatList.put(fromId, fromList);
            }
            entity = findSessionEntity(toList, fromId, Type.USER);
            if (entity == null || !entity.isOpen()) {
                toList.remove(entity);
                toList.add(from);
                keeperChatList.put(toId, toList);
            }
        }

        return true;
    }

    public LinkedList<SessionEntity> getList(int type,String id){
        if(type==Type.USER)
            return userChatList.get(id);
        else if(type==Type.KEEPER)
            return keeperChatList.get(id);
        else return null;
    }

    public SessionEntity exist(int type,String id){
        return findSessionEntity(id, type);
    }
}
