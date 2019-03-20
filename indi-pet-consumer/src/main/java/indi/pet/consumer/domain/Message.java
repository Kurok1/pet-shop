package indi.pet.consumer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 普通客户的动态消息
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Document(indexName = "petshop",type = "message")
public class Message implements Serializable {
    @Id
    private  String id;

    private String petId;

    private String content;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", petId='" + petId + '\'' +
                ", content='" + content + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", timestamp=" + timestamp +
                '}';
    }
}