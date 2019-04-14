package indi.pet.producer.listener;

import indi.pet.producer.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@Component
public class OrderListener {

    private static Logger logger= LoggerFactory.getLogger(OrderListener.class);

    @KafkaListener(topics = "pet-order")
    public void listen(Order order){
        logger.info("收到订单 in"+ LocalDateTime.now());
        logger.info("订单明细:"+order);
    }

}
