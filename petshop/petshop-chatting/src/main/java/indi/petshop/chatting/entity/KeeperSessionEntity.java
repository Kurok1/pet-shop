package indi.pet.chatting.entity;

import javax.websocket.Session;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.27
 */
public class KeeperSessionEntity implements SessionEntity {

    private String keeperId;

    private Session session;

    private boolean isLook;

    private String timestamp;

    @Override
    public String getId() {
        return keeperId;
    }

    @Override
    public void setId(String keeperId) {
        this.keeperId = keeperId;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
    }

    @Override
    public int getType() {
        return Type.KEEPER;
    }

    @Override
    public boolean isLook() {
        return this.isLook;
    }

    @Override
    public void setLook(boolean look) {
        this.isLook = look;
    }

    @Override
    public String getLastTimeStamp() {
        return timestamp;
    }

    @Override
    public void setLastTimeStamp(String timeStamp) {
        this.timestamp = timeStamp;
    }
}
