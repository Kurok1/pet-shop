package indi.petshop.consumer.controller;

import indi.petshop.consumer.domain.Pet;
import indi.petshop.consumer.exception.TokenExpiredException;
import indi.petshop.consumer.service.PetService;
import indi.petshop.consumer.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
@RestController("consumerPetController")
@RequestMapping("/consumer/pet")
public class PetController {

    private PetService petService;

    public PetService getPetService() {
        return petService;
    }

    @Autowired
    public void setPetService(@Qualifier("consumerPetService") PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/add")
    public Map<String,Object> add(@RequestParam("token")String token, @RequestBody Pet pet){
        Map<String,Object> data=new HashMap<>();
        if(TokenUtil.validate(token)){
            Pet save = getPetService().save(pet);
            data.put("flag",true);
            data.put("message","添加成功");
            data.put("pet",pet);
        }else throw new TokenExpiredException();
        return data;
    }

    @DeleteMapping("/{id}")
    public Map<String,Object> delete(@RequestParam("token")String token,@PathVariable("id")String id){
        Map<String,Object> data=new HashMap<>();
        if(TokenUtil.validate(token)){
            getPetService().delete(id);
            data.put("flag",true);
            data.put("message","删除成功");
        }else throw new TokenExpiredException();
        return data;
    }

    @PostMapping("/update")
    public Map<String,Object> update(@RequestParam("token")String token, @RequestBody Pet pet){
        Map<String,Object> data=new HashMap<>();
        if(TokenUtil.validate(token)){
            Pet save = getPetService().save(pet);
            data.put("flag",true);
            data.put("message","添加成功");
            data.put("pet",pet);
        }else throw new TokenExpiredException();
        return data;
    }
}
