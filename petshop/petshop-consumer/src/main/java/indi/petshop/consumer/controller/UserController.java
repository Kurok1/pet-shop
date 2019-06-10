package indi.petshop.consumer.controller;

import indi.petshop.consumer.domain.User;
import indi.petshop.consumer.service.UserService;
import indi.petshop.consumer.util.MD5Util;
import indi.petshop.consumer.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@RestController("consumerUserController")
@RequestMapping("/consumer/user")
public class UserController {

    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(@Qualifier("consumerUserService") UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String,Object> register(@RequestBody User user)throws Exception{
        Map<String,Object> data=new HashMap<>();
        if(getUserService().exist(user.getUsername())){
            data.put("flag",false);
            data.put("message","注册失败,用户名已经被使用");
            return data;
        }
        user.setPassword(MD5Util.getMD5Code(user.getPassword()));
        User savedUser = getUserService().save(user);
        data.put("flag",true);
        data.put("user",savedUser);
        data.put("token",TokenUtil.generateToken(savedUser));
        data.put("message","注册成功");
        return data;
    }

    /**
     * 针对用户名和密码登录
     * @param username 用户名
     * @param password 密码
     * @return 返回相应数据
     */
    @PostMapping("/login")
    public Map<String,Object> loginWithPass(@RequestParam("username") String username,@RequestParam("password") String password)throws Exception{
        Map<String,Object> data=new HashMap<>();
        User login = getUserService().login(username, MD5Util.getMD5Code(password));
        if(login!=null){
            data.put("flag",true);
            data.put("message","登录成功");
            data.put("token",TokenUtil.generateToken(login));
            data.put("user",login);
        }else {
            data.put("flag",false);
            data.put("message","登录失败，请检查用户名和密码");
        }
        return data;
    }

    @PutMapping("/login")
    public Map<String,Object> loginWithToken(@RequestParam("token")String token){
        Map<String,Object> data=new HashMap<>();
        User login = TokenUtil.validateToken(token);
        if(login!=null){
            data.put("flag",true);
            data.put("message","登录成功");
            data.put("token",token);
            data.put("user",login);
        }else {
            data.put("flag",false);
            data.put("message","token过期,请重新登录");
        }
        return data;
    }

    @PutMapping("/update")
    public User update(@RequestBody User user,@RequestParam("token") String token){
        User login = TokenUtil.validateToken(token);
        return login==null?null:getUserService().save(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id")String id){
        return getUserService().getOne(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id")String id){
        getUserService().delete(id);
    }

    @GetMapping("list/{id}")
    public Iterable<User> list(@PathVariable("id")String id){
        Set<String> set = getUserService().getOne(id).getFriends();
        if(set==null || set.size()==0)
            return null;
        return getUserService().getAll(set);
    }
}
