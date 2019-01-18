package indi.pet.producer.controller;

import indi.pet.producer.domain.Resource;
import indi.pet.producer.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Controller
@RequestMapping("/res")
public class ResourceController {

    private ResourceService resourceService;

    public ResourceService getResourceService() {
        return resourceService;
    }

    @Autowired
    public void setResourceService(@Qualifier("resourceService") ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping(path = "/save")
    @ResponseBody
    public Resource save(@RequestBody Resource resource){
        return getResourceService().save(resource);
    }

    @GetMapping(path = "/{id}")
    public void get(@PathVariable("id")String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Resource resource=getResourceService().findById(id);
        String root=request.getServletContext().getRealPath("/");
        File dir = new File(root);
        File file=new File(dir.getAbsolutePath()+"/"+resource.getPath());

        if(file.exists()){
            FileInputStream inputStream = null;

            inputStream = new FileInputStream(file);
            byte[] data = new byte[(int)file.length()];
            int length = inputStream.read(data);
            inputStream.close();
            response.setContentType(resource.getType());
            response.setContentLength(length);
            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();

        }else response.sendError(SC_NOT_FOUND);
    }
}
