package indi.pet.producer.controller;

import indi.pet.producer.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.07
 */
@Controller
public class ToolsController {

    @Autowired
    ResourceService resourceService;

    @GetMapping("/clear/data")
    public void clear(){
        resourceService.getResourceRepository().deleteAll();
    }
}
