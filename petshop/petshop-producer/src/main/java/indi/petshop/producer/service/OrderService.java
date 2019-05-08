package indi.pet.producer.service;

import indi.pet.producer.domain.Order;
import indi.pet.producer.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Service("orderService")
public class OrderService {
    private OrderRepository orderRepository;

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    @Autowired
    public void setOrderRepository(@Qualifier("orderRepository") OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order order){
        return getOrderRepository().save(order);
    }

    public Page<Order> findOrdersByUserId(String id, int page){
        Pageable pageable=new PageRequest(page,10);
        return getOrderRepository().findOrdersByUserId(id,pageable);
    }

    public Page<Order> findOrdersByShopkeeperIdAndStatus(String id,int status,int page){
        Pageable pageable=new PageRequest(page,10);
        return getOrderRepository().findOrdersByShopkeeperIdAndStatus(id,status,pageable);
    }

    public Long getCountByShopkeeperAndStatus(String id,int status){
        return getOrderRepository().countByShopkeeperIdAndStatus(id, status);
    }

    public Page<Order> findOrdersByShockId(String id,int page){
        Pageable pageable=new PageRequest(page,10);
        return getOrderRepository().findOrdersByShockId(id,pageable);
    }

    public void delete(String id){
        getOrderRepository().delete(id);
    }

    public Order findById(String id){
        return getOrderRepository().findOne(id);
    }
}
