package indi.pet.producer.controller;

import indi.pet.producer.domain.Shock;
import indi.pet.producer.domain.Shopkeeper;
import indi.pet.producer.service.ShopkeeperService;
import indi.pet.producer.util.MD5Util;
import indi.pet.producer.util.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public void setShopkeeperService(@Qualifier("shopkeeperService") ShopkeeperService shopkeeperService) {
        this.shopkeeperService = shopkeeperService;
    }

    @PostMapping(path = "/save")
    public Map<String,Object> save(@RequestBody Shopkeeper shopkeeper){
        Map<String,Object> map=new HashMap<>();
        if(getShopkeeperService().existsByEmail(shopkeeper.getEmail())){
            map.put("flag",false);
            map.put("message","注册失败，该邮箱已经被注册");
            return map;
        }
        String password=shopkeeper.getPassword();//加密处理
        String code=null;
        Shopkeeper saved=null;
        try {
            password= MD5Util.getMD5Code(password);
            shopkeeper.setPassword(password);
            saved=getShopkeeperService().save(shopkeeper);
            code=TokenValidator.generateToken(saved);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(code!=null){
            shopkeeper.setPassword(password);
            map.put("keeper",saved);
            map.put("flag",true);
            map.put("message","注册成功，将为您自动登录");
            map.put("token",code);
            return map;
        }
        map.put("flag",false);
        map.put("message","注册失败，请稍后再试");
        return map;
    }

    @PutMapping(path="/update")
    public Shopkeeper update(@RequestBody Shopkeeper shopkeeper,@RequestParam("token")String token){
        TokenValidator.validate(token);
        return getShopkeeperService().save(shopkeeper);
    }


//    @GetMapping("/search/{page}")
//    public Map<String,Object> searchByName(@RequestParam String name, @PathVariable int page){
//        Page<Shopkeeper> shopkeepers = getShopkeeperService().searchByName(name, page-1);
//
//        Map<String,Object> rtn=new HashMap<>();
//        rtn.put("data",shopkeepers.getContent());
//        rtn.put("current",shopkeepers.getNumber());
//        rtn.put("size",shopkeepers.getSize());
//        rtn.put("total",shopkeepers.getTotalPages());
//        rtn.put("count",shopkeepers.getNumberOfElements());
//        return rtn;
//    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestParam("email") String email,@RequestParam("password")String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        password=MD5Util.getMD5Code(password);
        Map<String,Object> map=new HashMap<>();
        Shopkeeper shopkeeper=getShopkeeperService().login(email, password);
        if(shopkeeper!=null){
            //生成一个登陆code,并保存在redis中，返回给用户当做令牌
            long timestamp=System.currentTimeMillis()/1000;
            String code=TokenValidator.generateToken(shopkeeper);
            map.put("token",code);
            map.put("keeper",shopkeeper);
            map.put("timestamp",timestamp);
            map.put("flag",true);
        }else {
            map.put("flag",false);
            map.put("message","用户名或者密码错误，请重试");
        }
        return map;
    }

    @PutMapping("/login")
    public Map<String,Object> loginWithoutPassword(@RequestParam("token")String token){
        Assert.notNull(token,"token值不能为空");
        String id=TokenValidator.validate(token);
        Shopkeeper shopkeeper = getShopkeeperService().getById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("flag",true);
        map.put("keeper",TokenValidator.validateToken(token));
        map.put("token",token);
        map.put("message","可以免密登录");
        return map;
    }


    @PostMapping("/repassword")
    public Map<String,Object> validatePassword(@RequestParam("id")String id,@RequestParam("oldpassword")String old,@RequestParam("newpassword")String newpassword) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Assert.hasLength(id,"用户id不应该为空");
        Assert.hasLength(old,"旧密码不能为空");
        Assert.hasLength(newpassword,"新密码不能为空");
        Map<String,Object> map=new HashMap<>();
        Shopkeeper shopkeeper = getShopkeeperService().check(id, MD5Util.getMD5Code(old));
        if(shopkeeper!=null){
            //更新密码
            shopkeeper.setPassword(MD5Util.getMD5Code(newpassword));
            getShopkeeperService().save(shopkeeper);

            map.put("flag",true);
            map.put("message","修改密码成功");
        }else{
            map.put("flag",false);
            map.put("message","修改失败，请检查原密码是否正确");
        }
        return map;
    }
}
