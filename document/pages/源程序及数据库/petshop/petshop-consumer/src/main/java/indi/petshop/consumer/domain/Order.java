package indi.petshop.consumer.domain;

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

    private static final long serialVersionUID = 123456L;

    @Id
    private String id;

    /**
     * 买方
     */
    private String userId;

    /**
     * 卖方
     */
    private String shopkeeperId;

    private Long createTimeStamp;

    /**
     * 购买的物品id
     */
    private String shockId;

    /**
     * 数量
     */
    private int count;

    /**
     * 订单状态
     */
    private Integer status;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getShopkeeperId() {
        return shopkeeperId;
    }

    public void setShopkeeperId(String shopkeeperId) {
        this.shopkeeperId = shopkeeperId;
    }

    public String getShockId() {
        return shockId;
    }

    public void setShockId(String shockId) {
        this.shockId = shockId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", shopkeeperId='" + shopkeeperId + '\'' +
                ", createTimeStamp=" + createTimeStamp +
                ", shockId='" + shockId + '\'' +
                ", count=" + count +
                ", status=" + status +
                '}';
    }
}
