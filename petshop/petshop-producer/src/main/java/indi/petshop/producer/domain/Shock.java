package indi.petshop.producer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

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
    private List<String> resources;

    private String logo;

    private String title;

    /**
     * 文字描述
     */
    private String text;

    private String shopkeeperId;

    /**
     * 剩余数量
     */
    private int last;

    private float price;

    private long timestamp;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
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

    public String getShopkeeperId() {
        return shopkeeperId;
    }

    public void setShopkeeperId(String shopkeeperId) {
        this.shopkeeperId = shopkeeperId;
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

    @Override
    public String toString() {
        return "Shock{" +
                "id='" + id + '\'' +
                ", resources=" + resources +
                ", logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", shopkeeperId='" + shopkeeperId + '\'' +
                ", last=" + last +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}
