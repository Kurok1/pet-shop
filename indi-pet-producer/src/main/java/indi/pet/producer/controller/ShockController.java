package indi.pet.producer.controller;

import indi.pet.producer.domain.Shock;
import indi.pet.producer.service.ShockService;
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
@RequestMapping("/shock")
public class ShockController {
    private ShockService shockService;

    public ShockService getShockService() {
        return shockService;
    }

    @Autowired
    public void setShockService(@Qualifier("shockService") ShockService shockService) {
        this.shockService = shockService;
    }

    @PostMapping(path = "/save")
    public Shock save(@RequestParam("token")String token,@RequestParam("id")String id, @RequestBody Shock shock){
        TokenValidator.validate(token,id);
        return getShockService().save(shock);
    }

    @PutMapping(path = "/update")
    public Shock update(@RequestParam("token")String token,@RequestParam("id")String id, @RequestBody Shock shock){
        TokenValidator.validate(token,id);
        return getShockService().save(shock);
    }

    @DeleteMapping("/{shockId}")
    public Map<String,Object> delete(@RequestParam("token")String token,@RequestParam("id")String id,@PathVariable("shockId")String shockId){
        TokenValidator.validate(token,id);
        getShockService().delete(shockId);
        Map<String,Object> map=new HashMap<>();
        map.put("flag",true);
        map.put("message","删除成功~");
        return map;
    }

    @GetMapping("/{page}")
    public Map<String,Object> getShocks(@RequestParam("token")String token,@RequestParam("id")String id,@PathVariable("page")int page){
        TokenValidator.validate(token,id);
        Page<Shock> shocks=getShockService().getShocks(id,page-1);
        Map<String,Object> rtn=new HashMap<>();
        rtn.put("data",shocks.getContent());
        rtn.put("current",shocks.getNumber());
        rtn.put("size",shocks.getSize());//每一页的元素数量
        rtn.put("totalPage",shocks.getTotalPages());//总共页数
        rtn.put("count",shocks.getNumberOfElements());
        rtn.put("flag",true);

        return rtn;
    }
    @GetMapping("/search/{name}/{page}")
    public Map<String,Object> getShocksByName(@RequestParam("token")String token,@RequestParam("id")String id,@PathVariable("page")int page,@PathVariable("name")String name){
        TokenValidator.validate(token,id);
        Page<Shock> shocks=getShockService().searchByName(name,id,page-1);
        Map<String,Object> rtn=new HashMap<>();
        rtn.put("data",shocks.getContent());
        rtn.put("current",shocks.getNumber());
        rtn.put("size",shocks.getSize());//每一页的元素数量
        rtn.put("totalPage",shocks.getTotalPages());//总共页数
        rtn.put("count",shocks.getNumberOfElements());
        rtn.put("flag",true);

        return rtn;
    }
}
