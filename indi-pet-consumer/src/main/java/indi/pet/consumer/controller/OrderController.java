package indi.pet.consumer.controller;

import indi.pet.consumer.domain.Order;
import indi.pet.consumer.exception.TokenExpiredException;
import indi.pet.consumer.service.OrderService;
import indi.pet.consumer.util.OrderStatus;
import indi.pet.consumer.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private static Logger logger= LoggerFactory.getLogger(OrderController.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderController(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    private OrderService orderService;

    public KafkaTemplate<String, Object> getKafkaTemplate() {
        return kafkaTemplate;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    @Autowired
    public void setOrderService(@Qualifier("orderService") OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Map<String,Object> create(@RequestParam("token")String token, @RequestBody Order order){
        Map<String,Object> map=new HashMap<>();
        if(TokenUtil.validate(token)){
            logger.info("订单开始生成!");
            order.setCreateTimeStamp(System.currentTimeMillis()/1000);
            order.setStatus(OrderStatus.ORDER_BEGIN);
            logger.info("订单明细: "+order);

            getKafkaTemplate().send("pet-order",0,order);//发送订单,由商户端处理
//            getOrderService().save(order);
            logger.info("订单发送成功 in"+ LocalDateTime.now());
            map.put("flag",true);
            map.put("message","订单发起成功");
        }else throw new TokenExpiredException();

        return map;
    }


}
