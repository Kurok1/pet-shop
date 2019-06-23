package indi.petshop.producer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Set;

@Document(indexName = "petshop",type = "user")
public class User implements Serializable {
    @Id
    private String id;

    private String username;

    private String password;

    private String logo;

    /**
     * 好友列表
     */
    private Set<String> friends;

    /**
     * 拥有宠物列表
     */
    private Set<String> pets;

    /**
     * 关注的商家列表
     */
    private Set<String> shopkeepers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Set<String> getFriends() {
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }

    public Set<String> getPets() {
        return pets;
    }

    public void setPets(Set<String> pets) {
        this.pets = pets;
    }

    public Set<String> getShopkeepers() {
        return shopkeepers;
    }

    public void setShopkeepers(Set<String> shopkeepers) {
        this.shopkeepers = shopkeepers;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", logo='" + logo + '\'' +
                ", friends=" + friends +
                ", pets=" + pets +
                ", shopkeepers=" + shopkeepers +
                '}';
    }
}
