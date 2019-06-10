package indi.petshop.chatting.controller;

import indi.petshop.chatting.entity.User;
import indi.petshop.chatting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.05.27
 */
@RestController
public class UserController {

    final private static ConcurrentHashMap<String,String> cachedUserToken = new ConcurrentHashMap<>();

    final private static ConcurrentHashMap<String,Long> cachedUserExpiredTime = new ConcurrentHashMap<>();

    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String generateToken(){
        Random random = new Random();
        StringBuffer token = new StringBuffer();
        while (token.length()<6)
            token.append(random.nextInt(10));
        return token.toString();
    }

    @GetMapping("/chatting/user/token/{userId}")
    public Map<String,Object> token(@PathVariable("userId")String userId){
        Lock lock = new ReentrantLock();
        final Map<String,Object> map = new HashMap<>();
        if (lock.tryLock()) {
            try {
                String token = generateToken();
                while (cachedUserToken.containsValue(token))
                    token = generateToken();
                cachedUserToken.put(userId,token);
                cachedUserExpiredTime.put(userId+token,System.currentTimeMillis()/1000);
                map.put("flag",true);
                map.put("message","获取验证码成功");
                map.put("token",token);
                return map;
            }catch (Exception e){
                e.printStackTrace();
                map.put("flag",false);
                map.put("message","系统错误");
                return map;
            }finally {
                lock.unlock();
            }
        }
        return null;
    }

    @PostMapping("/chatting/user/add/{from}/{to}/{token}")
    public Map<String,Object> add(
        @PathVariable("from")String from,
        @PathVariable("to")String to,
        @PathVariable("token")String token){
        Lock lock = new ReentrantLock();

        if(lock.tryLock()){
            Map<String,Object> map = new HashMap<>();
            try{
                //校验时间，默认5分钟
                Long time = cachedUserExpiredTime.get(to+token);
                Long current = System.currentTimeMillis()/1000;
                if(time==null || (current-time)>5*60){
                    map.put("flag",false);
                    map.put("messgae","会话已经过期，请重试");
                    return map;
                }
                User fromUser = getUserRepository().findOne(from);
                User toUser = getUserRepository().findOne(to);

                Set<String> friends = fromUser.getFriends();
                friends.add(to);
                fromUser.setFriends(friends);
                getUserRepository().save(fromUser);

                friends = null;
                friends = toUser.getFriends();
                friends.add(from);
                toUser.setFriends(friends);
                getUserRepository().save(toUser);

                //删除缓存
                cachedUserToken.remove(to);
                cachedUserExpiredTime.remove(to+token);
                doClear();
                map.put("flag",true);
                map.put("message","添加好友成功");

                return map;

            }catch (Exception e){
                e.printStackTrace();
                map.put("flag",false);
                map.put("message",e.getMessage());
                return map;
            }finally {
                lock.unlock();
            }
        }
        return null;
    }

    private void doClear(){
        Set<String> userIds = new HashSet<>();
        cachedUserExpiredTime.forEach(
                (k,v)->{
                    Long current = System.currentTimeMillis()/1000;
                    if(v==null || (current-v)>5*60){
                        userIds.add(k.substring(0,k.length()-6));
                    }
                }
        );
        for (String userId : userIds){
            cachedUserToken.remove(userId);
        }
    }
}
