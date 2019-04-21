package indi.pet.producer.listener;

import indi.pet.producer.domain.Order;
import indi.pet.producer.endpoint.OrderServerEndPoint;
import indi.pet.producer.service.OrderService;
import indi.pet.producer.util.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@Component
public class OrderListener {

    private OrderServerEndPoint point;

    private OrderService orderService;

    public OrderService getOrderService() {
        return orderService;
    }

    @Autowired
    public void setOrderService(@Qualifier("orderService") OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderServerEndPoint getPoint() {
        return point;
    }

    @Autowired
    public void setPoint(@Qualifier("orderServerEndPoint") OrderServerEndPoint point) {
        this.point = point;
    }

    private static Logger logger= LoggerFactory.getLogger(OrderListener.class);

    @KafkaListener(topics = "pet-order")
    public void listen(Order order){
        logger.info("收到订单 in"+ LocalDateTime.now());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getPoint().sendToKeeper(order.getShopkeeperId(),"您有新的订单，请前往订单页面查看");
        afterSend(order);
    }

    private void afterSend(Order order){
        order.setStatus(OrderStatus.ORDER_BEGIN);
        Order save=getOrderService().save(order);
        logger.info("订单："+save.getId()+"接收完成");
    }
}
