package indi.pet.producer.controller;

import indi.pet.producer.domain.Order;
import indi.pet.producer.endpoint.OrderServerEndPoint;
import indi.pet.producer.service.ResourceService;
import indi.pet.producer.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.07
 */
@Controller
public class ToolsController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ToolsController(@Autowired KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public KafkaTemplate<String, Object> getKafkaTemplate() {
        return kafkaTemplate;
    }

    @Autowired
    ResourceService resourceService;

    private OrderServerEndPoint point;

    public ResourceService getResourceService() {
        return resourceService;
    }

    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public OrderServerEndPoint getPoint() {
        return point;
    }

    @Autowired
    public void setPoint(@Qualifier("orderServerEndPoint") OrderServerEndPoint point) {
        this.point = point;
    }

    @GetMapping("/clear/data")
    public void clear(){
        resourceService.getResourceRepository().deleteAll();
    }

    @GetMapping("/point/{id}/{message}")
    public void send(@PathVariable("id")String id,@PathVariable("message")String message){
        getPoint().sendToKeeper(id,message);
    }

    @GetMapping("/send/user/{id}")
    public void sendToUser(@PathVariable("id")String id){
        Order order=new Order();
        order.setStatus(OrderStatus.ORDER_RECEIVED);
        order.setUserId(id);
        getKafkaTemplate().send("pet-order-status",0,order);
    }
}
