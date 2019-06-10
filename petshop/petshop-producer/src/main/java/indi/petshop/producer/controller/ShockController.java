package indi.petshop.producer.controller;

import indi.petshop.producer.domain.Shock;
import indi.petshop.producer.service.ShockService;
import indi.petshop.producer.util.TokenValidator;
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
@RestController("producerShockController")
@RequestMapping("/producer/shock")
public class ShockController {
    private ShockService shockService;

    public ShockService getShockService() {
        return shockService;
    }

    @Autowired
    public void setShockService(@Qualifier("producerShockService") ShockService shockService) {
        this.shockService = shockService;
    }

    @PostMapping(path = "/save")
    public Map<String,Object> save(@RequestParam("token")String token, @RequestBody Shock shock){
        TokenValidator.validate(token);
        Map<String,Object> map=new HashMap<>();
        shock.setTimestamp(System.currentTimeMillis()/1000);
        getShockService().save(shock);
        map.put("flag",true);
        map.put("message","发布成功~");
        return map;
    }

    @PutMapping(path = "/update")
    public Map<String,Object> update(@RequestParam("token")String token, @RequestBody Shock shock){
        TokenValidator.validate(token);
        Map<String,Object> map=new HashMap<>();
        getShockService().save(shock);
        map.put("flag",true);
        map.put("message","修改成功~");
        return map;
    }

    @DeleteMapping("/{shockId}")
    public Map<String,Object> delete(@RequestParam("token")String token,@PathVariable("shockId")String shockId){
        TokenValidator.validate(token);
        getShockService().delete(shockId);
        Map<String,Object> map=new HashMap<>();
        map.put("flag",true);
        map.put("message","删除成功~");
        return map;
    }

    @GetMapping("/{page}")
    public Map<String,Object> getShocks(@RequestParam("token")String token,@PathVariable("page")int page){
        String keeper=TokenValidator.validate(token);
        Page<Shock> shocks=getShockService().getShocks(keeper,page-1);
        Map<String,Object> rtn=new HashMap<>();
        rtn.put("data",shocks.getContent());
        rtn.put("hasNext",page*10<getShockService().getCountByShopkeeper(keeper));
        rtn.put("flag",true);
        rtn.put("message","查询成功");
        return rtn;
    }
    @GetMapping("/search/{name}/{page}")
    public Map<String,Object> getShocksByName(@RequestParam("token")String token,@PathVariable("page")int page,@PathVariable("name")String name){
        TokenValidator.validate(token);
//        Page<Shock> shocks=getShockService().searchByName(name,id,page-1);
        Map<String,Object> rtn=new HashMap<>();
//        rtn.put("data",shocks.getContent());
//        rtn.put("current",shocks.getNumber());
//        rtn.put("size",shocks.getSize());//每一页的元素数量
//        rtn.put("totalPage",shocks.getTotalPages());//总共页数
//        rtn.put("count",shocks.getNumberOfElements());
        rtn.put("flag",true);

        return rtn;
    }

    @GetMapping("/single/{id}")
    public Shock getById(@PathVariable("id")String id,@RequestParam("token")String token){
        TokenValidator.validate(token);
        return getShockService().findById(id);
    }
}
