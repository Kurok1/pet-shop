package indi.petshop.producer.controller;

import indi.petshop.producer.domain.Order;
import indi.petshop.producer.service.OrderService;
import indi.petshop.producer.service.ShockService;
import indi.petshop.producer.service.ShopkeeperService;
import indi.petshop.producer.service.UserService;
import indi.petshop.producer.util.OrderStatus;
import indi.petshop.producer.util.TokenValidator;
import indi.petshop.producer.wrapper.OrderStatusWrapper;
import indi.petshop.producer.wrapper.OrderWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@RestController("producerOrderController")
@RequestMapping("/producer/order")
public class OrderController{

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderController(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public KafkaTemplate<String, Object> getKafkaTemplate() {
        return kafkaTemplate;
    }

    private OrderService orderService;

    private ShopkeeperService shopkeeperService;

    private UserService userService;

    private ShockService shockService;

    public ShockService getShockService() {
        return shockService;
    }

    @Autowired
    public void setShockService(@Qualifier("producerShockService") ShockService shockService) {
        this.shockService = shockService;
    }

    public ShopkeeperService getShopkeeperService() {
        return shopkeeperService;
    }

    @Autowired
    public void setShopkeeperService(@Qualifier("producerShopkeeperService")ShopkeeperService shopkeeperService) {
        this.shopkeeperService = shopkeeperService;
    }

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(@Qualifier("producerUserService")UserService userService) {
        this.userService = userService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    @Autowired
    public void setOrderService(@Qualifier("producerOrderService") OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{page}/{status}")
    public Map<String,Object> getOrders(@RequestParam("token")String token,@PathVariable("page") int page,@PathVariable("status") int status){
        String id=TokenValidator.validate(token);
        Page<Order> orders=getOrderService().findOrdersByShopkeeperIdAndStatus(id,status,page-1);
        Map<String,Object> rtn=new HashMap<>();
        List<OrderWrapper> list=new LinkedList<>();
        orders.getContent().forEach(
            order -> {
                OrderWrapper wrapper=new OrderWrapper();
                wrapper.setAmount(order.getCount());
                wrapper.setId(order.getId());
                wrapper.setCreateTimeStamp(order.getCreateTimeStamp());
                wrapper.setStatus(order.getStatus());
                wrapper.setShock(getShockService().findById(order.getShockId()));
                list.add(wrapper);
            }
        );
        rtn.put("data",list);
        rtn.put("hasNext",page*10<getOrderService().getCountByShopkeeperAndStatus(id, status));
        rtn.put("flag",true);
        rtn.put("message","查询成功");
        return rtn;
    }

    @PutMapping("/update")
    public Map<String,Object> update(@RequestParam("token")String token, @RequestBody OrderStatusWrapper statusWrapper){
        TokenValidator.validate(token);
        Order order=getOrderService().getOrderRepository().findOne(statusWrapper.getId());
        int status=statusWrapper.getStatus();
        Map<String,Object> data=new HashMap<>();
        if(status==OrderStatus.ORDER_FINISHED && order.getStatus()<OrderStatus.USER_CONFIRM){
            data.put("flag",false);
            data.put("message","请等待用户确认完成");
            return data;
        }
        order.setStatus(status);
        Order save= getOrderService().save(order);
        boolean send;
        switch (order.getStatus()){
            case OrderStatus.ORDER_RECEIVED:
            case OrderStatus.ORDER_WORKING :
            case OrderStatus.ORDER_FINISHED :
            case OrderStatus.ORDER_CANCEL: {send=true;}break;
            default:send=false;
        }
        if(send)
            getKafkaTemplate().send("pet-order-status",0,save);

        data.put("flag",true);
        data.put("message","操作成功");
        return data;
    }

    @DeleteMapping("/{orderId}")
    public Map<String,Object> delete(@RequestParam("token")String token,@PathVariable("orderId")String orderId){
        TokenValidator.validate(token);
        Order order=getOrderService().findById(orderId);
        if(order.getUserId()!=null){
            order.setShopkeeperId(null);
            getOrderService().save(order);
        }else
            getOrderService().delete(orderId);

        Map<String,Object> map=new HashMap<>();
        map.put("flag",true);
        map.put("message","删除成功~");
        return map;

    }

    @GetMapping("/{orderId}")
    public OrderWrapper get(@PathVariable("orderId")String orderId,@RequestParam("token")String token){
        TokenValidator.validate(token);
        Order order=getOrderService().findById(orderId);
        OrderWrapper wrapper=new OrderWrapper();
        wrapper.setUser(getUserService().findById(order.getUserId()));
        wrapper.setShock(getShockService().findById(order.getShockId()));
        wrapper.setStatus(order.getStatus());
        wrapper.setCreateTimeStamp(order.getCreateTimeStamp());
        wrapper.setId(orderId);
        wrapper.setAmount(order.getCount());
        return wrapper;
    }
}
