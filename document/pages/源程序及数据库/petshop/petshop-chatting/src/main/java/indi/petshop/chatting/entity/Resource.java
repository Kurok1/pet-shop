package indi.petshop.chatting.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Document(indexName = "resource",type = "resource")
public class Resource implements Serializable {
    @Id
    private String id;

    private String path;

    private Long timestamp;

    private String type;

    /**
     * 代表此资源是否真正被使用
     * true=>正在被使用，不能删除
     * false=>没有被使用，可以删除
     */
    private boolean hasUsed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isHasUsed() {
        return hasUsed;
    }

    public void setHasUsed(boolean hasUsed) {
        this.hasUsed = hasUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", hasUsed=" + hasUsed +
                '}';
    }
}
