package indi.pet.consumer.repository;

import indi.pet.consumer.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Repository("orderRepository")
public interface OrderRepository extends ElasticsearchRepository<Order,String> {

    public Page<Order> findOrdersByShopkeeperId(String id, Pageable pageable);

    public Page<Order> findOrdersByUserIdOrderByCreateTimeStampDesc(String id, Pageable pageable);

    public Long countByUserId(String id);

}
