package indi.petshop.consumer.controller;

import indi.petshop.consumer.domain.Order;
import indi.petshop.consumer.domain.User;
import indi.petshop.consumer.exception.TokenExpiredException;
import indi.petshop.consumer.service.OrderService;
import indi.petshop.consumer.service.ShockService;
import indi.petshop.consumer.service.ShopkeeperService;
import indi.petshop.consumer.util.OrderStatus;
import indi.petshop.consumer.util.TokenUtil;
import indi.petshop.consumer.wrapper.OrderWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@RestController("consumerOrderController")
@RequestMapping("/consumer/order")
public class OrderController {

    private static Logger logger= LoggerFactory.getLogger(OrderController.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderController(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    private OrderService orderService;

    private ShopkeeperService shopkeeperService;

    private ShockService shockService;

    public ShopkeeperService getShopkeeperService() {
        return shopkeeperService;
    }

    @Autowired
    public void setShopkeeperService(@Qualifier("consumerShopkeeperService") ShopkeeperService shopkeeperService) {
        this.shopkeeperService = shopkeeperService;
    }

    public ShockService getShockService() {
        return shockService;
    }

    @Autowired
    public void setShockService(@Qualifier("consumerShockService") ShockService shockService) {
        this.shockService = shockService;
    }

    public KafkaTemplate<String, Object> getKafkaTemplate() {
        return kafkaTemplate;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    @Autowired
    public void setOrderService(@Qualifier("consumerOrderService") OrderService orderService) {
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
            logger.info("订单发送成功 in"+ LocalDateTime.now());
            map.put("flag",true);
            map.put("message","订单发起成功");
        }else throw new TokenExpiredException();

        return map;
    }

    @DeleteMapping("/{orderId}")
    public Map<String,Object> delete(@RequestParam("token")String token,@PathVariable("orderId")String orderId){
        Map<String,Object> map=new HashMap<>();
        if(TokenUtil.validate(token)){
            Order order=getOrderService().findById(orderId);
            if(order.getShopkeeperId()!=null){
                order.setUserId(null);
                getOrderService().save(order);
            }else
                getOrderService().delete(orderId);
            map.put("flag",true);
            map.put("message","删除成功");
        }else throw new TokenExpiredException();
        return map;
    }

    @PutMapping("/confirm/{orderId}")
    public Map<String,Object> confirm(@RequestParam("token")String token,@PathVariable("orderId")String orderId){
        Map<String,Object> map=new HashMap<>();
        if(TokenUtil.validate(token)){
            Order order=getOrderService().findById(orderId);
            if(order.getStatus()<OrderStatus.ORDER_WORKING){
                map.put("flag",false);
                map.put("message","当前状态不允许确认");
            }else{
                order.setStatus(OrderStatus.USER_CONFIRM);
                getOrderService().save(order);
                map.put("flag",true);
                map.put("message","OK");
            }
        }else throw new TokenExpiredException();
        return map;
    }

    @GetMapping("/list/{page}")
    public Map<String,Object> page(@RequestParam("token")String token,@PathVariable("page")int page){
        Map<String,Object> map=new HashMap<>();
        User user = TokenUtil.validateToken(token);

        if(user!=null){
            //获取订单列表
            List<OrderWrapper> list=new LinkedList<>();
            Page<Order> orders=getOrderService().findOrdersByUserId(user.getId(),page-1);
            orders.getContent().forEach(
                order -> {
                    OrderWrapper wrapper=new OrderWrapper();
                    wrapper.setId(order.getId());
                    wrapper.setAmount(order.getCount());
                    wrapper.setCreateTimeStamp(order.getCreateTimeStamp());
                    wrapper.setStatus(order.getStatus());
                    wrapper.setShock(getShockService().findOne(order.getShockId()));
                    wrapper.setShopkeeper(getShopkeeperService().getOne(order.getShopkeeperId()));
                    list.add(wrapper);
                }
            );
            map.put("flag",true);
            map.put("data",list);
            map.put("hasNext",page*10<getOrderService().countByUserId(user.getId()));
        }else throw new TokenExpiredException();
        return map;
    }

    @GetMapping("/{orderId}")
    public OrderWrapper get(@PathVariable("orderId")String id,@RequestParam("token")String token){
        TokenUtil.validate(token);
        Order order=getOrderService().findById(id);
        OrderWrapper wrapper=new OrderWrapper();
        wrapper.setShopkeeper(getShopkeeperService().getOne(order.getShopkeeperId()));
        wrapper.setShock(getShockService().findOne(order.getShockId()));
        wrapper.setStatus(order.getStatus());
        wrapper.setCreateTimeStamp(order.getCreateTimeStamp());
        wrapper.setId(id);
        wrapper.setAmount(order.getCount());
        return wrapper;
    }
}
