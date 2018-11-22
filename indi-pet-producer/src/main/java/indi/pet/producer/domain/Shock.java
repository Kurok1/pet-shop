package indi.pet.producer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 库存类
 */
@Document(indexName = "petshop",type = "shocks")
public class Shock implements Serializable {
    @Id
    private String id;

    /**
     * 具体描述，图片视频都行
     */
    private Resource description;

    private String title;

    /**
     * 文字描述
     */
    private String text;

    private Shopkeeper shopkeeper;

    /**
     * 进货时的数量
     */
    private int amount;

    /**
     * 剩余数量
     */
    private int last;

    private float price;

    private long timestamp;

    @Override
    public String toString() {
        return "Shock{" +
                "id='" + id + '\'' +
                ", description=" + description +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", shopkeeper=" + shopkeeper +
                ", amount=" + amount +
                ", last=" + last +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Resource getDescription() {
        return description;
    }

    public void setDescription(Resource description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Shopkeeper getShopkeeper() {
        return shopkeeper;
    }

    public void setShopkeeper(Shopkeeper shopkeeper) {
        this.shopkeeper = shopkeeper;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
