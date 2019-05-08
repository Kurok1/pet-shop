package indi.pet.producer.repository;

import indi.pet.producer.domain.Shopkeeper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Repository("shopkeeperRepository")
public interface ShopkeeperRepository extends ElasticsearchRepository<Shopkeeper,String> {

    /**
     * 根据Email准确查找
     * @param email 要搜索的宠物店邮箱
     * @return 返回符合结果的所有实体类
     */
    public Shopkeeper findByEmail(String email);

    /**
     * 登录操作
     * @param email 邮箱地址
     * @param password 密码
     * @return 如果都相等,返回对应的domain
     */
    public Shopkeeper findFirstByEmailEqualsAndPasswordEquals(String email, String password);


    /**
     * 根据id和密码查询对应用户，用于修改密码
     * @param id 用户id
     * @param password 旧密码
     * @return 如果存在返回对应的实体，否则返回null
     */
    public Shopkeeper findByIdAndPassword(String id, String password);
}
