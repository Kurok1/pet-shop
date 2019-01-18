package indi.pet.producer.controller;

import indi.pet.producer.domain.Order;
import indi.pet.producer.service.OrderService;
import indi.pet.producer.util.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@RestController
@RequestMapping("/order")
public class OrderController{

    private OrderService orderService;

    public OrderService getOrderService() {
        return orderService;
    }

    @Autowired
    public void setOrderService(@Qualifier("orderService") OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{page}")
    public Map<String,Object> getOrders(@RequestParam("token")String token,@RequestParam("id")String id,@PathVariable("page") int page){
        TokenValidator.validate(token, id);
        Page<Order> orders=getOrderService().findOrdersByShopkeeperId(id,page-1);
        Map<String,Object> rtn=new HashMap<>();
        rtn.put("data",orders.getContent());
        rtn.put("current",orders.getNumber());
        rtn.put("size",orders.getSize());//每一页的元素数量
        rtn.put("totalPage",orders.getTotalPages());//总共页数
        rtn.put("count",orders.getNumberOfElements());
        rtn.put("flag",true);

        return rtn;
    }

    @PutMapping("/update")
    public Order update(@RequestParam("token")String token,@RequestParam("id")String id,@RequestBody Order order){
        TokenValidator.validate(token, id);
        return getOrderService().save(order);
    }

    @DeleteMapping("/{orderId}")
    public Map<String,Object> delete(@RequestParam("token")String token,@RequestParam("id")String id,@PathVariable("orderId")String orderId){
        TokenValidator.validate(token,id);
        getOrderService().delete(orderId);
        Map<String,Object> map=new HashMap<>();
        map.put("flag",true);
        map.put("message","删除成功~");
        return map;
    }

}
