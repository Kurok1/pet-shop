package indi.petshop.consumer.util;

import indi.petshop.consumer.domain.User;
import indi.petshop.consumer.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
public class TokenUtil {

    private static RedisTemplate<String,String> redisTemplate;

    private static UserService userService;

    static {
        redisTemplate=BeanUtil.getBean("redisTemplate",RedisTemplate.class);
        userService=BeanUtil.getBean("consumerUserService",UserService.class);
    }

    /**
     * 为首次登录的用户的生成token并存储在Redis中
     * @param user 首次登录的用户bean
     * @return 返回生成的token
     */
    public static String generateToken(User user){
        String id=user.getId();
        Long timestamp=System.currentTimeMillis()/1000;
        String token=null;
        try {
            token=MD5Util.getMD5Code(id+timestamp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().set(token,id,60*24*7, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 使用token登录时，先从Redis里验证token是否过期，如果没有过期取出相应用户id，返回对应用户Bean，否则返回null
     * @param token 要验证的token
     * @return 如果token1在Redis中没有过期，返回对应用户，否则返回null
     */
    public static User validateToken(String token){
        if(redisTemplate.hasKey(token)){
            String id=redisTemplate.opsForValue().get(token);
            User user=userService.getOne(id);
            redisTemplate.opsForValue().set(token,id,60*24*7, TimeUnit.MINUTES);
            return user;
        }else return null;
    }

    /**
     * 用户进行其他操作时需要验证token，只作检验token是否过期
     * @param token 要验证的token
     * @return 如果没有过期返回true，否则返回false
     */
    public static boolean validate(String token){
        return redisTemplate.hasKey(token);
    }

}
