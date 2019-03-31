package indi.pet.producer.util;

import indi.pet.producer.domain.Shopkeeper;
import indi.pet.producer.exception.TokenAndIdNotEqualsException;
import indi.pet.producer.exception.TokenExpiredException;
import indi.pet.producer.service.ShopkeeperService;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * 提供静态方法用于验证token是否过期<br/>
 * 如果没有过期,返回redis中对应的id，否则抛出异常
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
public class TokenValidator {

    private static ShopkeeperService shopkeeperService;

    private static RedisTemplate<String,String> redisTemplate;

    static {
        redisTemplate= BeanUtil.getBean("redisTemplate",RedisTemplate.class);
        shopkeeperService=BeanUtil.getBean("shopkeeperService",ShopkeeperService.class);
    }

    /**
     * 为首次登录的用户的生成token并存储在Redis中
     * @param shopkeeper 首次登录的用户bean
     * @return 返回生成的token
     */
    public static String generateToken(Shopkeeper shopkeeper){
        String id=shopkeeper.getId();
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
     * 验证客户端传来的token值是否有效
     * @param token 客户端传递的token
     * @return 如果token未过期,返回token对应的id,否则抛出一个<code>TokenExpiredException</code>异常
     * @see TokenExpiredException
     */
    public static String validate(String token){

        if(!redisTemplate.hasKey(token))
            throw new TokenExpiredException();
        else return redisTemplate.opsForValue().get(token);
    }

    /**
     * 验证客户端传来的token和id是否对应
     * @param token 客户端传来的token
     * @param id 客户端传来的id
     * @return 如果token过期,抛出<code>TokenExpiredException</code>异常<br/>
     *          如果没有对应,抛出<code>TokenAndIdNotEqualsException</code>异常<br/>
     *          如果没有过期并且token和id一一对应,返回id
     * @see TokenExpiredException
     * @see TokenAndIdNotEqualsException
     */
    public static String validate(String token,String id){
        if(!redisTemplate.hasKey(token))
            throw new TokenExpiredException();
        else {
            String object=redisTemplate.opsForValue().get("token");
            if(id.equals(object))
                return object;
            else throw new TokenAndIdNotEqualsException();
        }
    }

    public static Shopkeeper validateToken(String token){
        if(redisTemplate.hasKey(token)){
            String id=redisTemplate.opsForValue().get(token);
            Shopkeeper shopkeeper=shopkeeperService.getById(id);
            redisTemplate.opsForValue().set(token,id,60*24*7, TimeUnit.MINUTES);
            return shopkeeper;
        }else return null;
    }
}
