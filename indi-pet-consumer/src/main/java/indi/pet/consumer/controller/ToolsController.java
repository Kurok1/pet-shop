package indi.pet.consumer.controller;

import indi.pet.consumer.service.CommentService;
import indi.pet.consumer.service.ResourceService;
import indi.pet.consumer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.05
 */
@Controller
public class ToolsController {

    @Autowired
    ResourceService resourceService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @GetMapping("/clear/data")
    public void clear(){
//        resourceService.getResourceRepository().deleteAll();
//        userService.getUserRepository().deleteAll();
        commentService.getCommentRepository().deleteAll();
    }

}
