package indi.pet.producer.controller;

import indi.pet.producer.domain.Shopkeeper;
import indi.pet.producer.service.ShopkeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@RestController
@RequestMapping("/keeper")
public class ShopkeeperController {
    private ShopkeeperService shopkeeperService;

    public ShopkeeperService getShopkeeperService() {
        return shopkeeperService;
    }

    @Autowired
    public void setShopkeeperService(ShopkeeperService shopkeeperService) {
        this.shopkeeperService = shopkeeperService;
    }

    @PostMapping(path = "/save")
    @PutMapping(path="/update")
    public Shopkeeper save(@RequestBody Shopkeeper shopkeeper){
        return getShopkeeperService().save(shopkeeper);
    }

    @GetMapping("/search/{page}")
    public List<Shopkeeper> searchByName(@RequestParam String name,@PathVariable int page){
        return getShopkeeperService().searchByName(name,page);
    }

    @GetMapping("/{id}")
    public Shopkeeper getById(@PathVariable("id")String id){
        return getShopkeeperService().getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable("id")String id){
        getShopkeeperService().deleteById(id);
        return true;
    }

    @PostMapping("/login")
    public Shopkeeper login(@RequestParam("email") String email,@RequestParam("password")String password){
        return getShopkeeperService().login(email, password);
    }

}
