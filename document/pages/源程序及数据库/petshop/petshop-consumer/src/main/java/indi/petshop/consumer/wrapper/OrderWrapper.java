package indi.petshop.consumer.wrapper;

import indi.petshop.consumer.domain.Shock;
import indi.petshop.consumer.domain.Shopkeeper;

import java.io.Serializable;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.21
 */
public class OrderWrapper implements Serializable {

    private String id;

    private int amount;

    private Long createTimeStamp;

    private int status;

    private Shock shock;

    private Shopkeeper shopkeeper;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Shock getShock() {
        return shock;
    }

    public void setShock(Shock shock) {
        this.shock = shock;
    }

    public Shopkeeper getShopkeeper() {
        return shopkeeper;
    }

    public void setShopkeeper(Shopkeeper shopkeeper) {
        this.shopkeeper = shopkeeper;
    }
}
