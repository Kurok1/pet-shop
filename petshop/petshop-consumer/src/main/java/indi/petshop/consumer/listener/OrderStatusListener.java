package indi.petshop.consumer.listener;

import indi.petshop.consumer.domain.Order;
import indi.petshop.consumer.endpoint.UserOrderServerPoint;
import indi.petshop.consumer.util.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.20
 */
@Component
public class OrderStatusListener {

    private UserOrderServerPoint point;

    public UserOrderServerPoint getPoint() {
        return point;
    }

    @Autowired
    public void setPoint(@Qualifier("userOrderServerPoint") UserOrderServerPoint point) {
        this.point = point;
    }

    private static Logger logger= LoggerFactory.getLogger(OrderStatusListener.class);

    @KafkaListener(topics = "pet-order-status")
    public void listen(Order order){
        logger.info("收到订单 in "+ LocalDateTime.now());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringBuffer msg=new StringBuffer();
        msg.append("订单:").append(order.getId());
        switch (order.getStatus()){
            case OrderStatus.ORDER_RECEIVED: {msg.append(" 已经被商家查看");}break;
            case OrderStatus.ORDER_WORKING : {msg.append(" 已经完成备货,请取货");}break;
            case OrderStatus.ORDER_CANCEL : {msg.append(" 已经被取消...");}break;
            case OrderStatus.ORDER_FINISHED: {msg.append(" 已经完成");}break;
        }
        getPoint().sendMessage(order.getUserId(),msg.toString());
    }

}
