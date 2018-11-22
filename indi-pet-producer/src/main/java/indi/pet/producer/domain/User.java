package indi.pet.producer.domain;

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
    private Set<User> friends;

    /**
     * 拥有宠物列表
     */
    private Set<Pet> pets;

    /**
     * 关注的商家列表
     */
    private Set<Shopkeeper> shopkeepers;
}
