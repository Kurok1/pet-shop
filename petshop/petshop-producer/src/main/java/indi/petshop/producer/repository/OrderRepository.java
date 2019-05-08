package indi.pet.producer.repository;

import indi.pet.producer.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;



/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Repository("orderRepository")
public interface OrderRepository extends ElasticsearchRepository<Order,String> {

    public Page<Order> findOrdersByShopkeeperIdAndStatus(String id, Integer status, Pageable pageable);

    public Page<Order> findOrdersByUserId(String id, Pageable pageable);

    public Page<Order> findOrdersByShockId(String id, Pageable pageable);

    public Long countByShopkeeperIdAndStatus(String shopkeeper, Integer status);
}
