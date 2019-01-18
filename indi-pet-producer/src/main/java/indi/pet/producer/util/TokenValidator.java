package indi.pet.producer.util;

import indi.pet.producer.exception.TokenAndIdNotEqualsException;
import indi.pet.producer.exception.TokenExpiredException;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 提供静态方法用于验证token是否过期<br/>
 * 如果没有过期,返回redis中对应的id，否则抛出异常
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
public class TokenValidator {

    private static RedisTemplate<String,String> redisTemplate;

    static {
        redisTemplate= BeanUtil.getBean("redisTemplate",RedisTemplate.class);
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
}
