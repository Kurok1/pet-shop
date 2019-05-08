package indi.petshop.producer.util;

import indi.petshop.producer.domain.Shopkeeper;
import indi.petshop.producer.exception.TokenAndIdNotEqualsException;
import indi.petshop.producer.exception.TokenExpiredException;
import indi.petshop.producer.service.ShopkeeperService;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * �ṩ��̬����������֤token�Ƿ����<br/>
 * ���û�й���,����redis�ж�Ӧ��id�������׳��쳣
 * @author <a href="maimengzzz@gmail.com">����</a>
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
     * Ϊ�״ε�¼���û�������token���洢��Redis��
     * @param shopkeeper �״ε�¼���û�bean
     * @return �������ɵ�token
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
     * ��֤�ͻ��˴�����tokenֵ�Ƿ���Ч
     * @param token �ͻ��˴��ݵ�token
     * @return ���tokenδ����,����token��Ӧ��id,�����׳�һ��<code>TokenExpiredException</code>�쳣
     * @see TokenExpiredException
     */
    public static String validate(String token){

        if(!redisTemplate.hasKey(token))
            throw new TokenExpiredException();
        else return redisTemplate.opsForValue().get(token);
    }

    /**
     * ��֤�ͻ��˴�����token��id�Ƿ��Ӧ
     * @param token �ͻ��˴�����token
     * @param id �ͻ��˴�����id
     * @return ���token����,�׳�<code>TokenExpiredException</code>�쳣<br/>
     *          ���û�ж�Ӧ,�׳�<code>TokenAndIdNotEqualsException</code>�쳣<br/>
     *          ���û�й��ڲ���token��idһһ��Ӧ,����id
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
