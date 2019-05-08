package indi.pet.chatting.entity;

import java.io.Serializable;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.27
 */
public class LeaveInfo implements Serializable {

    private boolean leaved;

    private String content;

    public boolean isLeaved() {
        return leaved;
    }

    public void setLeaved(boolean leaved) {
        this.leaved = leaved;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LeaveInfo() {
        super();
        this.leaved =  true;
        this.content = "对方已经离开";
    }
}
