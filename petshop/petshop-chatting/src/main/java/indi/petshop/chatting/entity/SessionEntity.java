package indi.pet.chatting.entity;

import javax.websocket.Session;
import java.io.Serializable;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.27
 */
public interface SessionEntity extends Serializable {

    public void setId(String id);

    public void setSession(Session session);

    public boolean isOpen();

    public int getType();

    public String getId();

    public Session getSession();

    public boolean isLook();

    public void setLook(boolean look);

    public String getLastTimeStamp();

    public void setLastTimeStamp(String timeStamp);
}
