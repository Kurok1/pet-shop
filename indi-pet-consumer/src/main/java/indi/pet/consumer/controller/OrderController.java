package indi.pet.consumer.controller;

import indi.pet.consumer.domain.Order;
import indi.pet.consumer.exception.TokenExpiredException;
import indi.pet.consumer.service.OrderService;
import indi.pet.consumer.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@RestController
@RequestMapping("/order")
public class OrderController {

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
            getKafkaTemplate().send("pet-order",0,order);//发送订单,由商户端处理
            getOrderService().save(order);
            map.put("flag",true);
            map.put("message","订单发起成功");
        }else throw new TokenExpiredException();

        return map;
    }


}
