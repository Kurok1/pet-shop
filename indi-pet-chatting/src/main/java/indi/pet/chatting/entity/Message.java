package indi.pet.chatting.entity;

import java.io.Serializable;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.27
 */
public class Message implements Serializable {
    private String content;

    private String sender;

    private int senderType;

    private String receiver;

    private int receiverType;

    private Long timestamp;

    private int messageType;

    private String resource;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Message{" +
            "content='" + content + '\'' +
            ", sender='" + sender + '\'' +
            ", senderType=" + senderType +
            ", receiver='" + receiver + '\'' +
            ", receiverType=" + receiverType +
            ", timestamp=" + timestamp +
            ", messageType=" + messageType +
            ", resource='" + resource + '\'' +
            '}';
    }

    public Message() {
        super();
    }
}
