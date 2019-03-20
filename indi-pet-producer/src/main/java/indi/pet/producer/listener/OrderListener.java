package indi.pet.producer.listener;

import indi.pet.producer.domain.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@Component
public class OrderListener {

    @KafkaListener(topics = "pet-order")
    public void listen(Order order){
        System.out.println(order);
    }

}
