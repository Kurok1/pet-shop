package indi.petshop.consumer.service;

import indi.petshop.consumer.domain.Order;
import indi.petshop.consumer.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Service("consumerOrderService")
@Transactional
public class OrderService {

    private OrderRepository orderRepository;

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    @Autowired
    public void setOrderRepository(@Qualifier("consumerOrderRepository") OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<Order> findOrdersByUserId(String id, int page){
        Pageable pageable=new PageRequest(page,10);
        return getOrderRepository().findOrdersByUserIdOrderByCreateTimeStampDesc(id,pageable);
    }

    public Page<Order> findOrdersByShopkeeperId(String id,int page){
        Pageable pageable=new PageRequest(page,10);
        return getOrderRepository().findOrdersByShopkeeperId(id,pageable);
    }

    public Order save(Order order){
        return getOrderRepository().save(order);
    }

    public void delete(String id){
        getOrderRepository().delete(id);
    }

    public Long countByUserId(String id){
        return getOrderRepository().countByUserId(id);
    }

    public Order findById(String id){
        return getOrderRepository().findOne(id);
    }
}
