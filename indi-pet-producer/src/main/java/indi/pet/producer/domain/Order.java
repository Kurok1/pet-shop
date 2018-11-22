package indi.pet.producer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 订单类
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Document(indexName = "petshop",type = "order")
public class Order implements Serializable {
    @Id
    private String id;

    /**
     * 买方
     */
    private String user;

    /**
     * 卖方
     */
    private String shopkeeper;

    private Long createTimeStamp;

    private Pet pet;

    /**
     * 订单状态
     */
    private Integer status;

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", shopkeeper='" + shopkeeper + '\'' +
                ", createTimeStamp=" + createTimeStamp +
                ", pet=" + pet +
                ", status=" + status +
                '}';
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getShopkeeper() {
        return shopkeeper;
    }

    public void setShopkeeper(String shopkeeper) {
        this.shopkeeper = shopkeeper;
    }

    public Long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
