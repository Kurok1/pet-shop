package indi.pet.consumer.controller;

import indi.pet.consumer.domain.Resource;
import indi.pet.consumer.exception.TokenExpiredException;
import indi.pet.consumer.service.ResourceService;
import indi.pet.consumer.util.MD5Util;
import indi.pet.consumer.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@Controller
@RequestMapping("/res")
public class ResourceController {

    final private static String UPLOAD_ROOT_DIRECTORY="F:\\uploads\\";

    private ResourceService resourceService;

    public ResourceService getResourceService() {
        return resourceService;
    }

    @Autowired
    public void setResourceService(@Qualifier("resourceService") ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping(path = "/upload")
    @ResponseBody
    public Map<String,Object> upload(@RequestParam("files") MultipartFile[] files){
        Map<String,Object> map=new HashMap<>();
        List<Resource> list=new ArrayList<>();
        if(files!=null && files.length!=0){
            for(MultipartFile file:files){
                if(!file.isEmpty()){
                    LocalDate localDate=LocalDate.now();
                    String fileName=null;
                    try {
                        fileName= MD5Util.getMD5Code(file.getOriginalFilename()+System.currentTimeMillis());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if(fileName!=null){
                        File baseDir=new File(UPLOAD_ROOT_DIRECTORY+localDate.toString()+"/");
                        if(!baseDir.exists()){
                            baseDir.mkdir();
                        }
                        String path=null;
                        path=UPLOAD_ROOT_DIRECTORY+localDate.toString()+"/"+fileName+file.getOriginalFilename();

                        File uploaded= new File(path);
                        try {
                            if(!uploaded.exists())
                                uploaded.createNewFile();
                            file.transferTo(uploaded);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Resource resource=new Resource();
                        resource.setPath(path);
                        resource.setHasUsed(false);
                        resource.setTimestamp(System.currentTimeMillis()/1000);
                        resource.setType(file.getContentType());
                        resource=resourceService.save(resource);
                        list.add(resource);
                    }
                }
            }
        }else {
            map.put("flag",false);
            map.put("message","没有任何内容");
            return map;
        }
        map.put("flag",true);
        map.put("message","上传成功");
        map.put("data",list);
        return map;
    }

    @PostMapping(path = "/save")
    @ResponseBody
    public Resource save(@RequestBody Resource resource){
        return getResourceService().save(resource);
    }

    @GetMapping(path = "/{id}")
    public void get(@PathVariable("id")String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Resource resource=getResourceService().findOne(id);
        File file=new File(resource.getPath());

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

    @DeleteMapping("/{id}")
    @ResponseBody
    public Map<String,Object> delete(@RequestParam("token")String token,@PathVariable("id")String id){
        if(TokenUtil.validate(token)){
            Resource one = getResourceService().findOne(id);
            one.setHasUsed(false);
            getResourceService().save(one);
        }else throw new TokenExpiredException();
        Map<String,Object> map=new HashMap<>();
        map.put("flag",true);
        map.put("message","删除成功");
        return map;
    }
}
