package indi.pet.producer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Document(indexName = "pet",type = "p")
public class Pet {
    @Id
    private String id;

    private User user;

    private String name;

    private String description;

    private String img;

    private Integer age;

    @Override
    public String toString() {
        return "Pet{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
