package indi.petshop.producer.wrapper;

import indi.petshop.producer.domain.Shock;
import indi.petshop.producer.domain.User;

import java.io.Serializable;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.20
 */
public class OrderWrapper implements Serializable {
    private String id;

    private Integer amount;

    private User user;

    private Long createTimeStamp;

    private Integer status;

    private Shock shock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Shock getShock() {
        return shock;
    }

    public void setShock(Shock shock) {
        this.shock = shock;
    }
}
