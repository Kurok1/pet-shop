package indi.pet.producer.repository;

import indi.pet.producer.domain.Shopkeeper;
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
     * 根据Name模糊查找
     * @param name 要搜索的宠物店名称
     * @return 返回符合结果的所有实体类集合
     */
    public List<Shopkeeper> findShopkeepersByNameLike(String name, Pageable pageable);

    /**
     * 登录操作
     * @param email 邮箱地址
     * @param password 密码
     * @return 如果都相等,返回对应的domain
     */
    public Shopkeeper findFirstByEmailEqualsAndPasswordEquals(String email,String password);

}
