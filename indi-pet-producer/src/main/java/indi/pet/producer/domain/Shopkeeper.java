package indi.pet.producer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 宠物店主实体类
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Document(indexName = "petshop",type = "keeper")
public class Shopkeeper implements Serializable {
    @Id
    private String id;

    private String name;

    private String description;

    /**
     * 是否为以认证状态
     */
    private Boolean authenticated;

    /**
     * 专用邮箱,用于登录使用
     */
    private String email;

    /**
     * 登录密码
     */
    private String password;

    /**
     * logo图片,用图片id代替
     */
    private String logo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Shopkeeper{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", authenticated=" + authenticated +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
